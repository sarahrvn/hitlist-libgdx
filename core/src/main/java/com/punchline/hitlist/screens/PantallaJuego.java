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
    private final Personaje PERSONAJE_1;
    private final Hud HUD;

    private final OrthographicCamera camaraJuego;
    private final Viewport viewportJuego;
    private boolean enPausa = false;
    private float tiempoTranscurrido = 0;
    private boolean tiempoCumplido = false;

    public PantallaJuego() {

        mapa = new Mapa(MapaDisponible.MAPA_CIUDAD);
        PERSONAJE_1 = new Personaje(TipoPersonaje.SABRINA_CARPENTER);
        HUD = new Hud();

        camaraJuego = new OrthographicCamera();
        viewportJuego = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camaraJuego);
        viewportJuego.apply();

        camaraJuego.position.set(mapa.getAncho() / 2f, mapa.getAlto() / 2f, 0);
        camaraJuego.update();

        PERSONAJE_1.setPosition(mapa.getAncho() / 2f, mapa.getAlto() / 2f);
    }

    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            enPausa = !enPausa;
        }

        HUD.mostrarPausa(enPausa);

        if (!enPausa) {
            tiempoTranscurrido += delta;

            if (tiempoTranscurrido >= 60 && !tiempoCumplido) {
                tiempoCumplido = true;
            }

            PERSONAJE_1.realizarMovimientos(mapa.getColisiones());
        }

        HUD.setTiempoRestante(Math.max(0, 60 - tiempoTranscurrido));



        batch.begin();
        batch.setProjectionMatrix(camaraJuego.combined);
        mapa.renderFondo(batch, camaraJuego, viewportJuego);
        batch.end();

        mapa.renderMapa(camaraJuego);

        batch.setProjectionMatrix(camaraJuego.combined);
        batch.begin();
        PERSONAJE_1.dibujar(batch);
        batch.end();

        HUD.render(batch);
    }

    public boolean terminoElTiempo() {
        return tiempoCumplido;
    }


    public void dispose() {
        mapa.dispose();
        HUD.dispose();
    }


}
