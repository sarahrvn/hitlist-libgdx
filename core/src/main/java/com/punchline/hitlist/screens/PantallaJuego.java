package com.punchline.hitlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.punchline.hitlist.elementosJuego.Mapa;
import com.punchline.hitlist.elementosJuego.MapaDisponible;
import com.punchline.hitlist.interacciones.TeclaListener;
import com.punchline.hitlist.personajes.Personaje;
import com.punchline.hitlist.personajes.TipoPersonaje;
import com.punchline.hitlist.elementosJuego.Hud;

public class PantallaJuego implements Screen {

    private Mapa mapa;
    private final Personaje PERSONAJE_1;
    private final Hud HUD;
    private final OrthographicCamera camaraJuego;
    private final Viewport viewportJuego;
    private boolean enPausa = false;
    private float tiempoTranscurrido = 0;
    private boolean tiempoCumplido = false;
    private TeclaListener teclaListener = new TeclaListener();
    private SpriteBatch batch; // NUEVO: agregar el batch como atributo

    public PantallaJuego() {
        mapa = new Mapa(MapaDisponible.MAPA_CIUDAD);
        PERSONAJE_1 = new Personaje(TipoPersonaje.SABRINA_CARPENTER);
        HUD = new Hud();

        camaraJuego = new OrthographicCamera();
        viewportJuego = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camaraJuego);
        viewportJuego.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        camaraJuego.position.set(mapa.getAncho() / 2f, mapa.getAlto() / 2f, 0);
        camaraJuego.update();

        PERSONAJE_1.setPosition(mapa.getAncho() / 2f, mapa.getAlto() / 2f);

        batch = new SpriteBatch(); // NUEVO: crear el batch aquí
    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar lógica
        update(delta);

        // Renderizar fondo
        batch.begin();
        batch.setProjectionMatrix(camaraJuego.combined);
        mapa.renderFondo(batch, camaraJuego, viewportJuego);
        batch.end();

        // Renderizar mapa
        mapa.renderMapa(camaraJuego);

        // Renderizar personaje
        batch.setProjectionMatrix(camaraJuego.combined);
        batch.begin();
        PERSONAJE_1.dibujar(batch);
        batch.end();

        // Renderizar HUD
        HUD.render(batch);
    }

    public void update(float delta){

        // KeyListener
        Gdx.input.setInputProcessor(this.teclaListener);

        // Manejar pausa
        if (teclaListener.isEscapeJustPressed()) {
            enPausa = !enPausa;
        }

        HUD.mostrarPausa(enPausa);

        if (!enPausa) {
            tiempoTranscurrido += delta;

            if (tiempoTranscurrido >= 60 && !tiempoCumplido) {
                tiempoCumplido = true;
            }

            this.PERSONAJE_1.realizarMovimientos(mapa.getColisiones());
        }

        HUD.setTiempoRestante(Math.max(0, 60 - tiempoTranscurrido));
    }

    public boolean terminoElTiempo() {
        return tiempoCumplido;
    }

    @Override
    public void resize(int width, int height) {
        viewportJuego.update(width, height, true);
    }

    @Override
    public void pause() {
        enPausa = true;
    }

    @Override
    public void resume() {

    }

    @Override
    public void show(){

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        mapa.dispose();
        HUD.dispose();
        batch.dispose();
    }
}
