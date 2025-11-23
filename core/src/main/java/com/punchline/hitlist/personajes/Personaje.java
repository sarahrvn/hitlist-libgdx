package com.punchline.hitlist.personajes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.punchline.hitlist.elementosJuego.GestorSonidos;
import com.punchline.hitlist.elementosJuego.SonidoDisponible;
import com.punchline.hitlist.elementosJuego.Arma;

public class Personaje {
    private Sprite sprite;
    private final Rectangle boundingBox;
    private final TextureAtlas ATLAS;
    private final TipoPersonaje TIPO_PERSONAJE;

    private Estadistica fuerza;
    private Estadistica destreza;
    private Estadistica defensa;
    private Estadistica velocidad;

    private Arma armaEquipada = null;

    private float velocidadY = 0;
    private float velocidadX = 0;
    private float velocidadCaminarActual;
    private final float GRAVEDAD = -1500;
    private final float VELOCIDAD_SALTO = 540;
    private boolean enElSuelo = false;
    private int saltosDisponibles = 2;
    private boolean tocandoPared = false;
    private boolean intentandoMoverse = false;

    private float tiempoAnimacionCorrer = 0f;
    private final float TIEMPO_CAMBIO_SPRITE = 0.1f;
    private boolean usandoCorrer1 = true;
    private float tiempoUltimoPaso = 0f;
    private final float INTERVALO_PASOS = 0.1f;

    public Personaje(TipoPersonaje tipo) {
        this.TIPO_PERSONAJE = tipo;
        this.ATLAS = new TextureAtlas(tipo.getRutaSprite());
        this.sprite = ATLAS.createSprite("idle");

        this.fuerza = new Estadistica("Fuerza", tipo.getFuerza());
        this.destreza = new Estadistica("Destreza", tipo.getDestreza());
        this.defensa = new Estadistica("Defensa", tipo.getDefensa());
        this.velocidad = new Estadistica("Velocidad", tipo.getVelocidad());

        recalcularAtributos();
        boundingBox = new Rectangle(0, 0, sprite.getWidth(), sprite.getHeight());
    }

    public Arma getArmaAsignada() {
        return TIPO_PERSONAJE.getArmaAsignada();
    }

    private void recalcularAtributos() {
        int fBase = TIPO_PERSONAJE.getFuerza();
        int dBase = TIPO_PERSONAJE.getDefensa();
        int vBase = TIPO_PERSONAJE.getVelocidad();

        int modF = 0, modD = 0, modV = 0;

        if (armaEquipada != null) {
            modF = limitarModificador(armaEquipada.getModFuerza());
            modD = limitarModificador(armaEquipada.getModDefensa());
            modV = limitarModificador(armaEquipada.getModVelocidad());
        }

        this.fuerza.setEstadistica(fBase + modF);
        this.defensa.setEstadistica(dBase + modD);
        this.velocidad.setEstadistica(vBase + modV);

        this.velocidadCaminarActual = 100f + (this.velocidad.getValor() * 30f);
    }

    private int limitarModificador(int valor) {
        if (valor > 2) return 2;
        if (valor < -2) return -2;
        return valor;
    }

    public void equiparArma(Arma arma) {
        this.armaEquipada = arma;
        recalcularAtributos();
        actualizarSpriteVisual();
    }

    private void actualizarSpriteVisual() {
        boolean flip = sprite.isFlipX();
        String nombreRegion;

        if (intentandoMoverse) {
            if (usandoCorrer1) {
                if (armaEquipada != null) nombreRegion = "correr1_arma";
                else nombreRegion = "correr1";
            } else {
                if (armaEquipada != null) nombreRegion = "correr2_arma";
                else nombreRegion = "correr2";
            }
        } else {
            if (armaEquipada != null) nombreRegion = "idle_arma";
            else nombreRegion = "idle";
        }

        Sprite nuevo = ATLAS.createSprite(nombreRegion);
        if (nuevo == null) {
            nuevo = ATLAS.createSprite(nombreRegion.replace("_arma", ""));
        }

        this.sprite = nuevo;
        if (flip && !sprite.isFlipX()) sprite.flip(true, false);
        else if (!flip && sprite.isFlipX()) sprite.flip(true, false);

        sprite.setPosition(boundingBox.x, boundingBox.y);
    }

    public void update(float delta, Array<Rectangle> colisiones) {
        delta = Math.min(delta, 1/30f);

        if (intentandoMoverse) {
            tiempoAnimacionCorrer += delta;
            tiempoUltimoPaso += delta;
            if (enElSuelo && tiempoUltimoPaso >= INTERVALO_PASOS) {
                tiempoUltimoPaso = 0f;
            }
            if (tiempoAnimacionCorrer >= TIEMPO_CAMBIO_SPRITE) {
                tiempoAnimacionCorrer = 0f;
                usandoCorrer1 = !usandoCorrer1;
                actualizarSpriteVisual();
            }
        } else {
            actualizarSpriteVisual();
        }

        boolean estabaEnElAire = !enElSuelo;
        boundingBox.x += velocidadX * delta;
        checkColisionX(colisiones);

        boolean agarradoPared = !enElSuelo && tocandoPared && velocidadY < 0 && intentandoMoverse;
        if (agarradoPared) {
            velocidadY = -100;
            saltosDisponibles = 2;
        } else {
            velocidadY += GRAVEDAD * delta;
        }

        boundingBox.y += velocidadY * delta;
        checkColisionY(colisiones);

        if (estabaEnElAire && enElSuelo && velocidadY <= 0) {
            GestorSonidos.getInstancia().reproducirSonido(SonidoDisponible.CAIDA, 0.4f);
        }

        sprite.setPosition(boundingBox.x, boundingBox.y);
        velocidadX = 0;
        intentandoMoverse = false;
    }

    public void dibujar(SpriteBatch batch) { sprite.draw(batch); }

    public void saltar() {
        if (saltosDisponibles > 0) {
            velocidadY = VELOCIDAD_SALTO;
            saltosDisponibles--;
            enElSuelo = false;
            GestorSonidos.getInstancia().reproducirSonido(SonidoDisponible.SALTO, 0.5f);
        }
    }
    public void caminarIzquierda() {
        velocidadX = -velocidadCaminarActual;
        intentandoMoverse = true;
        if (velocidadX == 0) actualizarSpriteVisual();
        if (!sprite.isFlipX()) sprite.flip(true, false);
    }
    public void caminarDerecha() {
        velocidadX = velocidadCaminarActual;
        intentandoMoverse = true;
        if (velocidadX == 0) actualizarSpriteVisual();
        if (sprite.isFlipX()) sprite.flip(true, false);
    }

    private void checkColisionX(Array<Rectangle> colisiones) {
        tocandoPared = false;
        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                if (velocidadX > 0) {
                    boundingBox.x = colision.x - boundingBox.width;
                    tocandoPared = true;
                } else if (velocidadX < 0) {
                    boundingBox.x = colision.x + colision.width;
                    tocandoPared = true;
                }
                break;
            }
        }
    }
    private void checkColisionY(Array<Rectangle> colisiones) {
        enElSuelo = false;
        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                if (velocidadY < 0) {
                    boundingBox.y = colision.y + colision.height;
                    enElSuelo = true;
                    saltosDisponibles = 2;
                    velocidadY = 0;
                } else if (velocidadY > 0) {
                    boundingBox.y = colision.y - boundingBox.height;
                    velocidadY = 0;
                }
                break;
            }
        }
    }

    public void setPosition(float x, float y) {
        boundingBox.setPosition(x - boundingBox.width / 2f, y - boundingBox.height / 2f);
        sprite.setPosition(boundingBox.x, boundingBox.y);
    }
    public Rectangle getHitbox() { return boundingBox; }

    // --- MODIFICADO: ELIMINA EL ARMA AL RESPAWNEAR ---
    public void resetearVelocidad() {
        velocidadX = 0;
        velocidadY = 0;
        enElSuelo = false;
        saltosDisponibles = 2;
        intentandoMoverse = false;

        // Quitamos el arma
        this.armaEquipada = null;
        // Reseteamos stats a base
        recalcularAtributos();

        actualizarSpriteVisual();
    }

    public void dispose() { ATLAS.dispose(); }
}
