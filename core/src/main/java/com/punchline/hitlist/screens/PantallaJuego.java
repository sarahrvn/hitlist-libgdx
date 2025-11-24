package com.punchline.hitlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.punchline.hitlist.elementosJuego.*;
import com.punchline.hitlist.interacciones.TeclaListener;
import com.punchline.hitlist.interacciones.HiloTiempo;
import com.punchline.hitlist.personajes.Personaje;
import com.punchline.hitlist.personajes.TipoPersonaje;

import java.util.Iterator;
import java.util.Random;

public class PantallaJuego {
    private Mapa mapa;
    private final Personaje PERSONAJE_1;
    private final Hud HUD;
    private final OrthographicCamera camaraJuego;
    private final Viewport viewportJuego;

    // Estados
    private boolean enPausa = false;
    private boolean volverAlMenu = false;

    // Variables de tiempo con hilo
    private int segundosRestantes = 60;
    private boolean tiempoCumplido = false;
    private HiloTiempo hiloTiempo;

    private TeclaListener teclaListener;
    private SpriteBatch batch;

    // Posición de spawn
    private final Vector2 POSICION_SPAWN;

    private Array<Espada> espadasEnJuego;
    private Random random;

    private int contadorSegundosArma = 0;
    private boolean debeSpawnearEspada = false;

    private static final Texture TEXTURA_ESPADA = new Texture("elementos/espada.png");

    public PantallaJuego(MapaDisponible mapaSeleccionado, TipoPersonaje personajeSeleccionado) {
        mapa = new Mapa(mapaSeleccionado);
        PERSONAJE_1 = new Personaje(personajeSeleccionado);
        HUD = new Hud();

        camaraJuego = new OrthographicCamera();
        viewportJuego = new StretchViewport(1024, 576, camaraJuego);
        viewportJuego.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        camaraJuego.position.set(mapa.getAncho() / 2f, mapa.getAlto() / 2f, 0);
        camaraJuego.update();

        float spawnX = mapa.getAncho() / 2f;
        float spawnY = mapa.getAlto() * 0.8f;
        POSICION_SPAWN = new Vector2(spawnX, spawnY);
        PERSONAJE_1.setPosition(POSICION_SPAWN.x, POSICION_SPAWN.y);

        batch = new SpriteBatch();
        teclaListener = new TeclaListener();
        espadasEnJuego = new Array<>();
        random = new Random();

        cargarSonidos();
        reproducirMusicaMapa();

        hiloTiempo = new HiloTiempo(this);
        hiloTiempo.start();
    }

    private void cargarSonidos() {
        GestorSonidos.getInstancia().cargarSonido(SonidoDisponible.SALTO);
        GestorSonidos.getInstancia().cargarSonido(SonidoDisponible.CAIDA);
    }

    private void reproducirMusicaMapa() {
        GestorSonidos.getInstancia().cargarMusica(SonidoDisponible.MUSICA_COMBATE);
        GestorSonidos.getInstancia().reproducirMusica(SonidoDisponible.MUSICA_COMBATE, true);
    }

    public void procesarSegundo() {
        if (!enPausa && !tiempoCumplido) {
            segundosRestantes--;
            contadorSegundosArma++;
            if (contadorSegundosArma >= 8) {
                debeSpawnearEspada = true;
                contadorSegundosArma = 0;
            }
            if (segundosRestantes <= 0) {
                segundosRestantes = 0;
                tiempoCumplido = true;
                volverAlMenu = true;
                hiloTiempo.terminar();
            }
        }
    }

    // ✅ Spawn genérico: todas las espadas se ven iguales
    private void spawnearEspadaReal() {
        float w = mapa.getAncho();
        float[] posX = { w * 0.2f, w * 0.5f, w * 0.8f };
        float x = posX[random.nextInt(3)];
        float y = POSICION_SPAWN.y;

        Espada nueva = new Espada(x, y, null, TEXTURA_ESPADA);
        espadasEnJuego.add(nueva);
    }

    public void update(float delta) {
        if (debeSpawnearEspada) {
            spawnearEspadaReal();
            debeSpawnearEspada = false;
        }

        Gdx.input.setInputProcessor(teclaListener);
        if (teclaListener.isEscapeJustPressed()) {
            enPausa = !enPausa;
            if(enPausa) GestorSonidos.getInstancia().pausarMusica();
            else GestorSonidos.getInstancia().reanudarMusica();
        }
        HUD.mostrarPausa(enPausa);

        if (!enPausa) {
            if (teclaListener.isP1ArribaJustPressed()) PERSONAJE_1.saltar();
            if (teclaListener.isP1Izquierda()) PERSONAJE_1.caminarIzquierda();
            if (teclaListener.isP1Derecha()) PERSONAJE_1.caminarDerecha();

            if (teclaListener.isP1AgarrarJustPressed()) {
                verificarAgarre();
            }

            PERSONAJE_1.update(delta, mapa.getColisiones());
            verificarColisionVacio();

            Iterator<Espada> iter = espadasEnJuego.iterator();
            while(iter.hasNext()) {
                Espada e = iter.next();
                e.update(delta, mapa.getColisiones());
                if (!e.isActiva()) {
                    e.dispose();
                    iter.remove();
                }
            }
        }
        HUD.setTiempoRestante(segundosRestantes);
    }

    // ✅ MODIFICADO: ya no importa qué espada sea
    private void verificarAgarre() {
        Rectangle hitboxPJ = PERSONAJE_1.getHitbox();

        for(Espada espada : espadasEnJuego) {
            if(hitboxPJ.overlaps(espada.getArea())) {

                // El personaje equipa SU arma propia
                Arma armaPropia = PERSONAJE_1.getArmaAsignada();
                PERSONAJE_1.equiparArma(armaPropia);

                espada.destruir();
                break;
            }
        }
    }

    private void verificarColisionVacio() {
        for (Rectangle vacio : mapa.getColisionesVacio()) {
            if (PERSONAJE_1.getHitbox().overlaps(vacio)) {
                respawnearPersonaje();
                break;
            }
        }
    }

    private void respawnearPersonaje() {
        if (HUD.quitarVida()) {
            PERSONAJE_1.setPosition(POSICION_SPAWN.x, POSICION_SPAWN.y);
            PERSONAJE_1.resetearVelocidad();
        } else {
            volverAlMenu = true;
            if (hiloTiempo != null) hiloTiempo.terminar();
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
        for(Espada e : espadasEnJuego) e.render(batch);
        PERSONAJE_1.dibujar(batch);
        batch.end();

        HUD.render(batch);
    }

    public boolean debeVolverAlMenu() { return volverAlMenu; }

    public void ajustarCamara(int width, int height) {
        viewportJuego.update(width, height, true);
        camaraJuego.position.set(mapa.getAncho() / 2f, mapa.getAlto() / 2f, 0);
        camaraJuego.update();
    }

    public void dispose() {
        mapa.dispose();
        HUD.dispose();
        batch.dispose();
        PERSONAJE_1.dispose();
        TEXTURA_ESPADA.dispose();
        if (hiloTiempo != null) hiloTiempo.terminar();
        GestorSonidos.getInstancia().detenerMusica();
    }
}
