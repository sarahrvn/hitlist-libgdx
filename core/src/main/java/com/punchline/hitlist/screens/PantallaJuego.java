package com.punchline.hitlist.screens;

import com.badlogic.gdx.Gdx;
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

import java.util.Random;

public class PantallaJuego {
    private Mapa mapa;
    private Array<Personaje> personajes;
    private final Hud HUD;
    private final OrthographicCamera camaraJuego;
    private final Viewport viewportJuego;

    // Estados
    private boolean enPausa = false;
    private boolean volverAlMenu = false;
    int indiceGanador = 0;

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

    public PantallaJuego(MapaDisponible mapaSeleccionado, TipoPersonaje p1Seleccionado, TipoPersonaje p2Seleccionado) {
        mapa = new Mapa(mapaSeleccionado);
        HUD = new Hud();

        camaraJuego = new OrthographicCamera();
        viewportJuego = new StretchViewport(1024, 576, camaraJuego);
        viewportJuego.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        camaraJuego.position.set(mapa.getAncho() / 2f, mapa.getAlto() / 2f, 0);
        camaraJuego.update();

        float spawnX = mapa.getAncho() / 2f;
        float spawnY = mapa.getAlto() * 0.8f;
        POSICION_SPAWN = new Vector2(spawnX, spawnY);

        personajes = new Array<>();

        // Crear Jugador 1
        Personaje p1 = new Personaje(p1Seleccionado);
        p1.setPosition(POSICION_SPAWN.x - 30f, POSICION_SPAWN.y);
        personajes.add(p1);

        // Crear Jugador 2
        Personaje p2 = new Personaje(p2Seleccionado);
        p2.setPosition(POSICION_SPAWN.x + 30f, POSICION_SPAWN.y);
        p2.caminarIzquierda(); // Mirar al rival
        personajes.add(p2);

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
    private void spawnearEspadaReal() {
        float w = mapa.getAncho();
        float[] posX = { w * 0.2f, w * 0.5f, w * 0.8f };
        float x = posX[random.nextInt(3)];
        float y = POSICION_SPAWN.y;

        // Actualizado: Usamos el constructor de 3 argumentos de tu clase Espada.
        // La clase Espada ahora maneja la textura internamente (static).
        Espada nueva = new Espada(x, y, null);
        espadasEnJuego.add(nueva);
    }

    public void update(float delta) {

        Gdx.input.setInputProcessor(teclaListener);
        if (teclaListener.isEscapeJustPressed()) {
            enPausa = !enPausa;
            if(enPausa) GestorSonidos.getInstancia().pausarMusica();
            else GestorSonidos.getInstancia().reanudarMusica();
        }
        HUD.mostrarPausa(enPausa);

        if (!enPausa) {
            manejarInput();

            // Actualizar personajes (Bucle indexado)
            for (int i = 0; i < personajes.size; i++) {
                personajes.get(i).update(delta, mapa.getColisiones());
            }

            verificarColisionVacio();
            verificarCombate();

            // Espadas
            if (debeSpawnearEspada) {
                spawnearEspadaReal();
                debeSpawnearEspada = false;
            }

            // Actualizar espadas y eliminar inactivas (Bucle INVERSO para borrar seguro)
            for (int i = espadasEnJuego.size - 1; i >= 0; i--) {
                Espada espada = espadasEnJuego.get(i);
                espada.update(delta, mapa.getColisiones());

                if (!espada.isActiva()) {
                    espada.dispose();
                    espadasEnJuego.removeIndex(i); // Borrado eficiente sin iterador
                }
            }
        }
        HUD.setTiempoRestante(segundosRestantes);
    }

    private void manejarInput() {
        // Obtenemos los personajes directamente por índice, es seguro pues siempre hay 2
        Personaje p1 = personajes.get(0);
        Personaje p2 = personajes.get(1);

        // Inputs P1
        if (teclaListener.isP1ArribaJustPressed()) { p1.saltar(); }
        if (teclaListener.isP1Izquierda()) { p1.caminarIzquierda(); }
        if (teclaListener.isP1Derecha()) { p1.caminarDerecha(); }
        if (teclaListener.isP1AgarrarJustPressed()) { verificarAgarre(p1); }
        if (teclaListener.isP1AtacarJustPressed()) { p1.atacar(); }

        // Inputs P2
        if (teclaListener.isP2ArribaJustPressed()) { p2.saltar(); }
        if (teclaListener.isP2Izquierda()) { p2.caminarIzquierda(); }
        if (teclaListener.isP2Derecha()) { p2.caminarDerecha(); }
        if (teclaListener.isP2AgarrarJustPressed()) { verificarAgarre(p2); }
        if (teclaListener.isP2AtacarJustPressed()) { p2.atacar(); }
    }

    private void verificarAgarre(Personaje personaje) {
        Rectangle hitboxPJ = personaje.getHitbox();

        // Bucle indexado normal
        for (int i = 0; i < espadasEnJuego.size; i++) {
            Espada espada = espadasEnJuego.get(i);

            if(hitboxPJ.overlaps(espada.getArea()) && !personaje.isArmaEquipada()) {
                personaje.equiparArma();
                espada.destruir();
            }
        }
    }

    private void verificarCombate() {
        // Doble bucle indexado, evita cualquier problema de anidamiento
        for (int i = 0; i < personajes.size; i++) {
            Personaje atacante = personajes.get(i);

            if (atacante.isAtacando()) {
                for (int j = 0; j < personajes.size; j++) {
                    Personaje victima = personajes.get(j);

                    if (atacante == victima) continue;

                    if (atacante.getHitboxAtaque().overlaps(victima.getHitbox())) {
                        int direccion = (atacante.getHitbox().x < victima.getHitbox().x) ? 1 : -1;
                        victima.recibirGolpe(atacante.getFuerza().getValor(), direccion);
                    }
                }
            }
        }
    }

    private void verificarColisionVacio() {
        for (int i = 0; i < personajes.size; i++) {
            Personaje personaje = personajes.get(i);

            // mapa.getColisionesVacio() probablemente sea un Array o List, si es Array usa size, si es List usa size()
            // Asumiremos que es Iterable o Array. Si mapa devuelve un Array de LibGDX, mejor usar bucle for i.
            // Si es un ArrayList de Java, el foreach está bien porque no estamos dentro de otro foreach de la MISMA lista.
            // Pero para consistencia, si puedes, usa for i. Como no tengo el código de Mapa, dejo el foreach simple
            // ya que NO anida iteradores de la misma lista 'personajes'.
            for (Rectangle vacio : mapa.getColisionesVacio()) {
                if (personaje.getHitbox().overlaps(vacio)) {
                    personaje.sacarVida();
                    GestorSonidos.getInstancia().reproducirSonido(SonidoDisponible.CAIDA);
                    if (personaje.estaMuerto()) {
                        terminarPartida(personaje);
                    } else {
                        respawnearPersonaje(personaje);
                    }
                }
            }
        }
    }

    private void respawnearPersonaje(Personaje personaje) {
        if (personaje == personajes.get(0)) {
            personaje.setPosition(POSICION_SPAWN.x - 10f, POSICION_SPAWN.y);
        } else if (personaje == personajes.get(1)) {
            personaje.setPosition(POSICION_SPAWN.x + 10f, POSICION_SPAWN.y);
        }
        personaje.resetear();
    }

    private void terminarPartida(Personaje personajePerdedor) {
        if (hiloTiempo != null) { hiloTiempo.terminar(); }

        if (personajePerdedor == this.personajes.get(0)) {
            indiceGanador = 1;
        } else if (personajePerdedor == this.personajes.get(1)) {
            indiceGanador = 0;
        }
        volverAlMenu = true;
    }

    public int getIndiceGanador() { return this.indiceGanador; }

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

        // Bucle indexado para renderizar espadas
        for (int i = 0; i < espadasEnJuego.size; i++) {
            espadasEnJuego.get(i).render(batch);
        }

        // Bucle indexado para renderizar personajes
        for (int i = 0; i < personajes.size; i++) {
            personajes.get(i).dibujar(batch);
        }

        batch.end();

        HUD.render(batch, personajes);
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
        // Bucle indexado para dispose
        for (int i = 0; i < personajes.size; i++) {
            personajes.get(i).dispose();
        }
        if (hiloTiempo != null) hiloTiempo.terminar();
        GestorSonidos.getInstancia().detenerMusica();
    }
}
