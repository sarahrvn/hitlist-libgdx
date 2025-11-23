package com.punchline.hitlist.screens;

import com.badlogic.gdx.Gdx;
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
import com.punchline.hitlist.interacciones.TeclaListener;
import com.punchline.hitlist.interacciones.HiloTiempo;

public class PantallaJuego {

    private Mapa mapa;
    private final Personaje PERSONAJE_1;
    private final Hud HUD;
    private final OrthographicCamera camaraJuego;
    private final Viewport viewportJuego;
    private boolean enPausa = false;

    // Variables de Tiempo con Hilo
    private int segundosRestantes = 60;
    private boolean tiempoCumplido = false;
    private HiloTiempo hiloTiempo;

    private TeclaListener teclaListener;
    private SpriteBatch batch;

    // Constructor modificado para recibir el mapa y personaje seleccionados
    public PantallaJuego(MapaDisponible mapaSeleccionado, TipoPersonaje personajeSeleccionado) {
        mapa = new Mapa(mapaSeleccionado);
        PERSONAJE_1 = new Personaje(personajeSeleccionado);
        HUD = new Hud();

        camaraJuego = new OrthographicCamera();
        viewportJuego = new StretchViewport(1024, 576, camaraJuego);
        viewportJuego.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        camaraJuego.position.set(mapa.getAncho() / 2f, mapa.getAlto() / 2f, 0);
        camaraJuego.update();

        PERSONAJE_1.setPosition(mapa.getAncho() / 2f, mapa.getAlto() / 1.2f);

        batch = new SpriteBatch();
        teclaListener = new TeclaListener();

        // INICIAMOS EL HILO DE TIEMPO
        hiloTiempo = new HiloTiempo(this);
        hiloTiempo.start();
    }

    // Método llamado por el HiloTiempo cada segundo
    public void procesarSegundo() {
        if (!enPausa && !tiempoCumplido) {
            segundosRestantes--;

            if (segundosRestantes <= 0) {
                segundosRestantes = 0;
                tiempoCumplido = true;
                hiloTiempo.terminar();
            }
        }
    }

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

    public void update(float delta) {
        Gdx.input.setInputProcessor(this.teclaListener);

        if (teclaListener.isEscapeJustPressed()) {
            enPausa = !enPausa;
        }

        HUD.mostrarPausa(enPausa);

        if (!enPausa) {
            // INPUTS P1
            if (teclaListener.isP1ArribaJustPressed()) {
                PERSONAJE_1.saltar();
            }
            if (teclaListener.isP1Izquierda()) {
                PERSONAJE_1.caminarIzquierda();
            }
            if (teclaListener.isP1Derecha()) {
                PERSONAJE_1.caminarDerecha();
            }

            // FÍSICAS
            PERSONAJE_1.update(delta, mapa.getColisiones());
        }

        // Actualizamos el HUD con la variable que modifica el Hilo
        HUD.setTiempoRestante(segundosRestantes);
    }

    public boolean terminoElTiempo() {
        return tiempoCumplido;
    }

    public void ajustarCamara(int width, int height) {
        viewportJuego.update(width, height, true);
        camaraJuego.position.set(mapa.getAncho() / 2f, mapa.getAlto() / 2f, 0);
        camaraJuego.update();
    }


    public void dispose() {
        mapa.dispose();
        HUD.dispose();
        batch.dispose();

        // Detener el hilo al cerrar
        if (hiloTiempo != null) {
            hiloTiempo.terminar();
        }
    }
}
