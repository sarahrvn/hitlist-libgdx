package com.punchline.hitlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.punchline.hitlist.elementosJuego.Mapa;
import com.punchline.hitlist.elementosJuego.MapaDisponible;
import com.punchline.hitlist.personajes.Personaje;
import com.punchline.hitlist.personajes.TipoPersonaje;
import com.punchline.hitlist.elementosJuego.Hud;

public class PantallaJuego {

    private Mapa mapa;
    private final Personaje personaje1;
    private final Hud hud;

    private final OrthographicCamera camaraJuego;
    private final Viewport viewportJuego;
    private boolean enPausa = false;
    private float tiempoTranscurrido = 0;
    private boolean tiempoCumplido = false;

    public PantallaJuego() {
        // Crear el mapa, el personaje y el HUD
        mapa = new Mapa(MapaDisponible.MAPA_CIUDAD);  // Pasar el mapa con el fondo y las colisiones
        personaje1 = new Personaje(TipoPersonaje.TIPAZO);  // Personaje sin Box2D, solo con rectángulo
        hud = new Hud();

        // Crear la cámara del juego y el viewport
        camaraJuego = new OrthographicCamera();
        viewportJuego = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camaraJuego);
        viewportJuego.apply();  // Asegúrate de aplicar el viewport

        // Centrar la cámara en el mapa
        camaraJuego.position.set(mapa.getAncho() / 2f, mapa.getAlto() / 2f, 0);
        camaraJuego.update();  // No olvidar actualizar la cámara después de moverla

        // Posicionar el personaje inicialmente en el centro del mapa
        personaje1.setPosition(mapa.getAncho() / 2f, mapa.getAlto() / 2f);
    }

    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(1, 1, 1, 1); // Fondo blanco (puede ser 0,0,0,1 si querés negro)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();



        // ← Aquí controlás el toggle de pausa con ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            enPausa = !enPausa;
        }

        // ← Avisás al HUD si está pausado
        hud.mostrarPausa(enPausa);

        // ← Solo actualizás lógica y timer si NO está pausado
        if (!enPausa) {
            tiempoTranscurrido += delta;

            if (tiempoTranscurrido >= 60 && !tiempoCumplido) {
                tiempoCumplido = true;
            }

            personaje1.realizarMovimientos(mapa.getColisiones());
        }

        hud.setTiempoRestante(Math.max(0, 60 - tiempoTranscurrido));



        batch.begin();
        batch.setProjectionMatrix(camaraJuego.combined);
        mapa.renderFondo(batch, camaraJuego, viewportJuego);
        batch.end();

        mapa.renderMapa(camaraJuego);

        batch.setProjectionMatrix(camaraJuego.combined);
        batch.begin();
        personaje1.dibujar(batch);
        batch.end();

        hud.render(batch);
    }

    public boolean terminoElTiempo() {
        return tiempoCumplido;
    }


    public void dispose() {
        mapa.dispose();
        hud.dispose();
    }


}
