package com.punchline.hitlist.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // Importa SpriteBatch
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array; // Importa Array

public class Personaje {
    private final Sprite sprite;
    private final Rectangle boundingBox;  // Caja de colisión para el personaje

    private float velocidad = 3f;

    public Personaje(TipoPersonaje tipo) {
        // Cargar el sprite usando el tipo
        sprite = new Sprite(new Texture(tipo.getRutaSprite()));
        sprite.setOriginCenter();

        // Caja de colisión
        boundingBox = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void dibujar(SpriteBatch batch) {
        sprite.setPosition(boundingBox.x, boundingBox.y);
        sprite.draw(batch);
    }

    public void realizarMovimientos(Array<Rectangle> colisiones) {
        float movimientoX = 0, movimientoY = 0;

        // Leer las teclas para mover al personaje
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            movimientoX = velocidad;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            movimientoX = -velocidad;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            movimientoY = velocidad;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            movimientoY = -velocidad;
        }

        // Mover el personaje
        boundingBox.x += movimientoX;
        boundingBox.y += movimientoY;

        // Verificar colisiones con los bloques
        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                // Si hay colisión, revertimos el movimiento
                boundingBox.x -= movimientoX;
                boundingBox.y -= movimientoY;
            }
        }
    }

    public void setPosition(float x, float y) {
        boundingBox.setPosition(x, y);
    }

    public Rectangle getBoundingRectangle() {
        return boundingBox;
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}
