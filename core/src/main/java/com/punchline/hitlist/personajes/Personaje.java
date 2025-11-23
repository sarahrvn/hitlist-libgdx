package com.punchline.hitlist.personajes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Personaje {
    private Sprite sprite;
    private final Rectangle boundingBox;
    private final TextureAtlas ATLAS;

    // Estadísticas
    private Estadistica fuerza;
    private Estadistica destreza;
    private Estadistica defensa;
    private Estadistica velocidad;

    // Físicas y movimiento
    private float velocidadY = 0;
    private float velocidadX = 0;

    // Variables calculadas en base a stats
    private float velocidadCaminarActual;

    // Constantes fijas
    private final float GRAVEDAD = -1500;
    private final float VELOCIDAD_SALTO = 540;

    // Lógica de saltos
    private boolean enElSuelo = false;
    private int saltosDisponibles = 2;

    // Lógica de pared (WALL SLIDE)
    private boolean tocandoPared = false;
    private int direccionPared = 0;
    private final float VELOCIDAD_DESLIZAMIENTO = -100;
    private boolean intentandoMoverse = false;

    // Animación de correr
    private float tiempoAnimacionCorrer = 0f;
    private final float TIEMPO_CAMBIO_SPRITE = 0.2f;
    private boolean usandoCorrer1 = true;

    public Personaje(TipoPersonaje tipo) {
        this.ATLAS = new TextureAtlas(tipo.getRutaSprite());
        this.sprite = ATLAS.createSprite("idle");

        // Estadísticas
        this.fuerza = new Estadistica("Fuerza", tipo.getFuerza());
        this.destreza = new Estadistica("Destreza", tipo.getDestreza());
        this.defensa = new Estadistica("Defensa", tipo.getDefensa());
        this.velocidad = new Estadistica("Velocidad", tipo.getVelocidad());

        // Calcular atributos según estadísticas
        recalcularAtributos();

        // Hitbox
        boundingBox = new Rectangle(0, 0, sprite.getWidth(), sprite.getHeight());
    }

    private void recalcularAtributos() {
        // FÓRMULA DE VELOCIDAD:
        // Base 100 + (30 por cada punto de stat)
        this.velocidadCaminarActual = 100f + (this.velocidad.getValor() * 30f);
    }

    public void update(float delta, Array<Rectangle> colisiones) {

        // Actualizar timer de animación si se está moviendo
        if (intentandoMoverse) {
            tiempoAnimacionCorrer += delta;

            // Cambiar sprite cada medio segundo
            if (tiempoAnimacionCorrer >= TIEMPO_CAMBIO_SPRITE) {
                tiempoAnimacionCorrer = 0f;
                usandoCorrer1 = !usandoCorrer1; // Alternar entre correr1 y correr2

                // Actualizar el sprite actual
                String nombreSprite = usandoCorrer1 ? "correr1" : "correr2";
                boolean estabaFlipeado = sprite.isFlipX();
                sprite = ATLAS.createSprite(nombreSprite);

                // Mantener la dirección del flip
                if (estabaFlipeado && !sprite.isFlipX()) {
                    sprite.flip(true, false);
                } else if (!estabaFlipeado && sprite.isFlipX()) {
                    sprite.flip(true, false);
                }
            }
        }

        // 1. Mover en X y detectar paredes
        boundingBox.x += velocidadX * delta;
        checkColisionX(colisiones);

        // 2. Lógica de wall slide
        boolean agarradoPared = !enElSuelo && tocandoPared && velocidadY < 0 && intentandoMoverse;

        if (agarradoPared) {
            velocidadY = VELOCIDAD_DESLIZAMIENTO;
            saltosDisponibles = 2;
        } else {
            velocidadY += GRAVEDAD * delta;
        }

        // 3. Mover en Y
        boundingBox.y += velocidadY * delta;
        checkColisionY(colisiones);

        // 4. Actualizar sprite
        sprite.setPosition(boundingBox.x, boundingBox.y);

        velocidadX = 0;
        intentandoMoverse = false;
    }

    public void dibujar(SpriteBatch batch) {
        sprite.draw(batch);
    }

    // ---- CONTROLES ----

    public void saltar() {
        if (saltosDisponibles > 0) {
            velocidadY = VELOCIDAD_SALTO;
            saltosDisponibles--;
            enElSuelo = false;
        }
    }

    public void caminarIzquierda() {
        velocidadX = -velocidadCaminarActual;
        intentandoMoverse = true;

        // Solo cambiar sprite si no estaba moviéndose antes (para evitar resetear la animación)
        if (velocidadX == 0) {
            sprite = ATLAS.createSprite("correr1");
            usandoCorrer1 = true;
            tiempoAnimacionCorrer = 0f;
        }

        if (!sprite.isFlipX()) sprite.flip(true, false);
    }

    public void caminarDerecha() {
        velocidadX = velocidadCaminarActual;
        intentandoMoverse = true;

        // Solo cambiar sprite si no estaba moviéndose antes (para evitar resetear la animación)
        if (velocidadX == 0) {
            sprite = ATLAS.createSprite("correr1");
            usandoCorrer1 = true;
            tiempoAnimacionCorrer = 0f;
        }

        if (sprite.isFlipX()) sprite.flip(true, false);
    }

    public void esquivar() {
        // Implementar según necesites
    }

    // ---- COLISIONES ----

    private void checkColisionX(Array<Rectangle> colisiones) {
        tocandoPared = false;
        direccionPared = 0;

        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                if (velocidadX > 0) {
                    boundingBox.x = colision.x - boundingBox.width;
                    tocandoPared = true;
                    direccionPared = 1;
                } else if (velocidadX < 0) {
                    boundingBox.x = colision.x + colision.width;
                    tocandoPared = true;
                    direccionPared = -1;
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
        boundingBox.setPosition(x, y);
        sprite.setPosition(x, y);
    }

    public Rectangle getBoundingRectangle() {
        return boundingBox;
    }

    // ---- MÉTODOS PARA SISTEMA DE RESPAWN ----

    /**
     * Obtiene la hitbox del personaje para detectar colisiones con el vacío
     */
    public Rectangle getHitbox() {
        return boundingBox;
    }

    /**
     * Resetea las velocidades al respawnear para evitar que el personaje
     * siga cayendo o moviéndose después del respawn
     */
    public void resetearVelocidad() {
        velocidadX = 0;
        velocidadY = 0;
        enElSuelo = false;
        saltosDisponibles = 2;
        tocandoPared = false;
        direccionPared = 0;
        intentandoMoverse = false;

        // Resetear animación a idle
        tiempoAnimacionCorrer = 0f;
        usandoCorrer1 = true;
        sprite = ATLAS.createSprite("idle");
    }

    public void dispose() {
        ATLAS.dispose();
    }
}
