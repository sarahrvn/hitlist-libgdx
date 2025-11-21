package com.punchline.hitlist.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Personaje {
    private Sprite sprite;
    private final Rectangle boundingBox;
    private final TextureAtlas ATLAS;

    // Físicas y movimiento
    private float velocidadY = 0;
    private float velocidadX = 0;
    private final float GRAVEDAD = -1500; // Ajustado para sentirse más pesado/realista
    private final float VELOCIDAD_SALTO = 600;
    private final float VELOCIDAD_CAMINAR = 300;

    // Lógica de saltos
    private boolean enElSuelo = false;
    private int saltosDisponibles = 2; // Empezamos con 2

    // Lógica de pared
    private boolean tocandoPared = false;
    private int direccionPared = 0; // -1 Izquierda, 1 Derecha
    private final float VELOCIDAD_DESLIZAMIENTO = -100; // Velocidad lenta de caída (negativa)

    // Variable auxiliar para saber si el jugador está presionando teclas
    private boolean intentandoMoverse = false;

    public Personaje(TipoPersonaje tipo) {
        this.ATLAS = new TextureAtlas(tipo.getRutaSprite());
        this.sprite = ATLAS.createSprite("idle");

        // Creamos el bounding box
        boundingBox = new Rectangle(0, 0, sprite.getWidth(), sprite.getHeight());
    }

    // Método principal que se llama en cada frame
    public void update(float delta, Array<Rectangle> colisiones) {

        // 1. Mover en X y detectar paredes
        boundingBox.x += velocidadX * delta;
        checkColisionX(colisiones);

        // 2. Lógica de wall slide
        // Condición: Está en el aire + Toca pared + Está cayendo + Está empujando contra la pared
        boolean agarradoPared = !enElSuelo && tocandoPared && velocidadY < 0 && intentandoMoverse;

        if (agarradoPared) {
            // Fricción en vez de gravedad normal
            velocidadY = VELOCIDAD_DESLIZAMIENTO;

            saltosDisponibles = 2;
        } else {
            // Gravedad normal
            velocidadY += GRAVEDAD * delta;
        }

        // 3. Mover en Y
        boundingBox.y += velocidadY * delta;
        checkColisionY(colisiones);

        // 4. Actualizar sprite
        sprite.setPosition(boundingBox.x, boundingBox.y);

        // Resetear inputs del frame
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
            saltosDisponibles--; // Restamos un salto
            enElSuelo = false;
        }
    }

    public void caminarIzquierda() {
        velocidadX = -VELOCIDAD_CAMINAR;
        intentandoMoverse = true;
        if (!sprite.isFlipX()) sprite.flip(true, false);
    }

    public void caminarDerecha() {
        velocidadX = VELOCIDAD_CAMINAR;
        intentandoMoverse = true;
        if (sprite.isFlipX()) sprite.flip(true, false);
    }

    public void esquivar() {
        // Lógica futura de esquivar
    }

    // ---- COLISIONES ----

    private void checkColisionX(Array<Rectangle> colisiones) {
        tocandoPared = false; // No toca pared al inicio del frame
        direccionPared = 0;

        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                if (velocidadX > 0) { // Chocó pared derecha
                    boundingBox.x = colision.x - boundingBox.width;
                    tocandoPared = true;
                    direccionPared = 1;
                }
                else if (velocidadX < 0) { // Chocó pared izquierda
                    boundingBox.x = colision.x + colision.width;
                    tocandoPared = true;
                    direccionPared = -1;
                }
                break;
            }
        }
    }

    private void checkColisionY(Array<Rectangle> colisiones) {
        enElSuelo = false; // Asumimos que estamos en el aire hasta probar lo contrario

        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                if (velocidadY < 0) { // Cayendo (tocando suelo)
                    boundingBox.y = colision.y + colision.height;
                    enElSuelo = true;
                    saltosDisponibles = 2; // Resetea los saltos
                    velocidadY = 0;
                } else if (velocidadY > 0) { // Saltando (tocando techo)
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

    public void dispose() {
        ATLAS.dispose();
    }
}
