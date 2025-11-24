package com.punchline.hitlist.elementosJuego;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Arma {
    private Sprite sprite;
    private Rectangle boundingBox;
    private float velocidadY = 0;
    private final float GRAVEDAD = -1000;
    private boolean activa = true; // Si es false desaparece (porque la agarraron)

    public Arma(float x, float y) {
        Texture texturaEspada = new Texture("items/espada.png");
        this.sprite = new Sprite(texturaEspada);
        this.sprite.setPosition(x, y);
        this.boundingBox = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta, Array<Rectangle> colisiones) {
        if (!activa) return;

        // Física
        velocidadY += GRAVEDAD * delta;
        boundingBox.y += velocidadY * delta;

        // Colisión con el piso
        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                // Si toca suelo, se queda quieta arriba del bloque
                boundingBox.y = colision.y + colision.height;
                velocidadY = 0;
                break;
            }
        }

        sprite.setPosition(boundingBox.x, boundingBox.y);
    }

    public void render(SpriteBatch batch) {
        if (activa) { sprite.draw(batch); }
    }

    public Rectangle getArea() { return boundingBox; }
    public boolean isActiva() { return activa; }
    public void destruir() { this.activa = false; }

    public void dispose() { sprite.getTexture().dispose(); }
}
