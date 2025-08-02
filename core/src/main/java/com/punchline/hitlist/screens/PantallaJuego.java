package com.punchline.hitlist.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
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

    public PantallaJuego() {
        // Crear el mapa, el personaje y el HUD usando el enum MapaDisponible
        mapa = new Mapa(MapaDisponible.MAPA_BOSQUE);
        personaje1 = new Personaje(TipoPersonaje.TIPAZO);
        hud = new Hud();

        // Crear la cámara del juego y el viewport
        camaraJuego = new OrthographicCamera();
        viewportJuego = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camaraJuego);
        viewportJuego.apply();

        // Centrar la cámara en el mapa
        camaraJuego.position.set(mapa.getAncho() / 2f, mapa.getAlto() / 2f, 0);
        camaraJuego.update();

        // Posicionar el personaje inicialmente en el centro del mapa
        personaje1.setPosition(mapa.getAncho() / 2f, mapa.getAlto() / 2f);
    }

    public void ajustarCamara(int ancho, int alto) {
        // Ajustar el viewport y la cámara
        //viewportJuego.update(ancho, alto, true);
        camaraJuego.position.set(mapa.getAncho() / 2f, mapa.getAlto() / 2f, 0);
        camaraJuego.update();

        // Asegurar que el HUD también se ajuste al tamaño de la pantalla
        hud.resize(ancho, alto);


    }

    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // o el color de fondo que quieras (negro, por ejemplo)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camaraJuego.update();

        // Fondo: se dibuja usando la misma cámara (centrado y escalado)

        batch.begin();
        batch.setProjectionMatrix(camaraJuego.combined);

        mapa.renderFondo(batch, camaraJuego, viewportJuego);
        batch.end();

        mapa.renderMapa(camaraJuego);

        // Personaje: usar batch nuevamente
        batch.setProjectionMatrix(camaraJuego.combined);
        batch.begin();
        personaje1.dibujar(batch);
        personaje1.realizarMovimientos();
        batch.end();

        // HUD: tiene su propia cámara
       hud.render(batch);
    }




    public void dispose() {
        mapa.dispose();
        hud.dispose();
    }
}
