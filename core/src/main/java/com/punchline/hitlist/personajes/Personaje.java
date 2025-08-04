package com.punchline.hitlist.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // Importa SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array; // Importa Array

public class Personaje {
    private TipoPersonaje tipo;
    private Estadistica fuerza, destreza, defensa, velocidad;
    private final TextureAtlas ATLAS;
    private Sprite sprite;

    private int vida = 3;
    private int resistencia = 1000;
    private final Rectangle boundingBox;  // Caja de colisión para el personaje

    //gravedad
    float velocidadY = 0;
    final float GRAVEDAD = -800;
    final float VELOCIDAD_SALTO = 400;
    boolean enElSuelo = false;

    public Personaje(TipoPersonaje tipo) {
        this.tipo = tipo;
        this.fuerza = new Estadistica("Fuerza", tipo.getFuerza());
        this.destreza = new Estadistica("Destreza", tipo.getDestreza());
        this.defensa = new Estadistica("Defensa", tipo.getDefensa());
        this.velocidad = new Estadistica("Velocidad", tipo.getVelocidad());

        this.ATLAS = new TextureAtlas(tipo.getRutaSprite());
        this.sprite = ATLAS.createSprite("idle");
        sprite.setOriginCenter();


        // Caja de colisión
        boundingBox = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void dibujar(SpriteBatch batch) {
        sprite.setPosition(boundingBox.x, boundingBox.y);
        sprite.draw(batch);
    }

    public void realizarMovimientos(Array<Rectangle> colisiones) {
        boolean saltar = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean izquierda = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean derecha = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean esquivar = Gdx.input.isKeyPressed(Input.Keys.S);


        float delta = Gdx.graphics.getDeltaTime();
        float modificadorMovimiento = ((float) (this.velocidad.getValor()) * 10) * delta;
        float dx = 0, dy = 0;
        boolean puedeMoverseX = (izquierda != derecha), puedeMoverseY = (saltar != esquivar);

        if(puedeMoverseX) {
            this.sprite = ATLAS.createSprite("correr1");

            if (derecha) {
                dx = modificadorMovimiento;
                if (sprite.isFlipX()) {
                    sprite.flip(true, false);
                }

            } else if (izquierda) {
                dx = -modificadorMovimiento;
                if (!sprite.isFlipX()) {
                    sprite.flip(true, false);
                }
            }
        }

        if(puedeMoverseY) {
            if (saltar && enElSuelo) {
                velocidadY = VELOCIDAD_SALTO;
                enElSuelo = false;
            }
        }

        velocidadY += GRAVEDAD * delta;
        dy = velocidadY * delta;

        if (!puedeMoverseX && !puedeMoverseY) {
            sprite = ATLAS.createSprite("idle");
        }

        boundingBox.x += dx;
        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                boundingBox.x -= dx;
                break;
            }
        }

        boundingBox.y += dy;
        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                if (velocidadY < 0) {
                    enElSuelo = true;
                } else {
                    enElSuelo = false;
                }
                boundingBox.y -= dy;
                velocidadY = 0;
                break;
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
