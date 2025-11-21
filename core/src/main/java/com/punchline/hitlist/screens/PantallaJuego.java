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
import com.punchline.hitlist.utils.HiloTiempo; // Importamos tu nuevo hilo

public class PantallaJuego implements Screen {

    private Mapa mapa;
    private final Personaje PERSONAJE_1;
    private final Hud HUD;
    private final OrthographicCamera camaraJuego;
    private final Viewport viewportJuego;
    private boolean enPausa = false;

    // Variables de Tiempo
    private int segundosRestantes = 60;
    private boolean tiempoCumplido = false;
    private HiloTiempo hiloTiempo; // Referencia al hilo

    private TeclaListener teclaListener = new TeclaListener();
    private SpriteBatch batch;

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

        batch = new SpriteBatch();

        // INICIAMOS EL HILO AL ESTILO DE TU PROFESOR
        hiloTiempo = new HiloTiempo(this);
        hiloTiempo.start();
    }

    // Este es el método que llama el Hilo cada 1 segundo
    public void procesarSegundo() {
        if (!enPausa && !tiempoCumplido) {
            segundosRestantes--;

            if (segundosRestantes <= 0) {
                segundosRestantes = 0;
                tiempoCumplido = true;
                hiloTiempo.terminar(); // Matamos el hilo si terminó el tiempo
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

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

    public void update(float delta){
        Gdx.input.setInputProcessor(this.teclaListener);

        if (teclaListener.isEscapeJustPressed()) {
            enPausa = !enPausa;
        }

        HUD.mostrarPausa(enPausa);

        if (!enPausa) {
            // ---- INPUTS P1 ----
            if (teclaListener.isP1ArribaJustPressed()) {
                PERSONAJE_1.saltar();
            }
            if (teclaListener.isP1Izquierda()) {
                PERSONAJE_1.caminarIzquierda();
            }
            if (teclaListener.isP1Derecha()) {
                PERSONAJE_1.caminarDerecha();
            }

            // ---- FÍSICAS ----
            PERSONAJE_1.update(delta, mapa.getColisiones());
        }

        // Actualizamos el HUD con la variable que modifica el Hilo
        HUD.setTiempoRestante(segundosRestantes);
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

        // Importante: Detener el hilo al cerrar para que no siga corriendo en la nada
        if (hiloTiempo != null) {
            hiloTiempo.terminar();
        }
    }
}
