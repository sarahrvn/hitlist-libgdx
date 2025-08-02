package com.punchline.hitlist.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Personaje {

    private TipoPersonaje tipo;
    private Estadistica fuerza, destreza, defensa, velocidad;
    private final Texture SPRITESHEET;
    private Sprite sprite;

    private int vida = 3;
    private int resistencia = 1000;

    public Personaje(TipoPersonaje tipo) {
        this.tipo = tipo;
        this.fuerza = new Estadistica("Fuerza", tipo.getFuerza());
        this.destreza = new Estadistica("Destreza", tipo.getDestreza());
        this.defensa = new Estadistica("Defensa", tipo.getDefensa());
        this.velocidad = new Estadistica("Velocidad", tipo.getVelocidad());
        this.SPRITESHEET = new Texture(tipo.getRutaSprite());
        this.sprite = new Sprite(SPRITESHEET, 0, 0, 100, 142);
        // No posicionamos aquí, lo hacemos desde PantallaJuego
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x - sprite.getWidth()/2f, y - sprite.getHeight()/2f); // Centrar el sprite en x,y
    }

    public void dibujar(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void realizarMovimientos() {
        mover();
        golpear();
    }

    private void mover() {
        boolean saltar = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean izquierda = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean derecha = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean esquivar = Gdx.input.isKeyPressed(Input.Keys.S);

        float x = sprite.getX(), y = sprite.getY(), delta = Gdx.graphics.getDeltaTime();
        float modificadorMovimiento = ((float) (this.velocidad.getValor()) / 2) * delta;
        boolean puedeMoverseX = (izquierda != derecha), puedeMoverseY = (saltar != esquivar);
        if(puedeMoverseX) {
            if (derecha) {
                x += modificadorMovimiento;
            } else if (izquierda) {
                x -= modificadorMovimiento;
            }

        }

        if(puedeMoverseY) {
            if (saltar) {
                y += modificadorMovimiento;
            }
        }
        sprite.setPosition(x, y);
    }

    private void golpear() {
        // Implementar si quieres
    }

    // Getters y setters (sin cambios)...
}
