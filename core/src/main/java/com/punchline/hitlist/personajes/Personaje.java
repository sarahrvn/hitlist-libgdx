package com.punchline.hitlist.personajes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Personaje {

    private TipoPersonaje tipo;
    private Estadistica fuerza, destreza, defensa, velocidad;
    private final Texture SPRITESHEET;
    private Sprite sprite;

    private int x = 40, y = 40;
    private int vida = 3;
    private int resistencia = 1000; //Tendencia a ser empujado hacia los límites del mapa y perder una vida (100 es poca, 1 es mucha);

    public Personaje(TipoPersonaje tipo) {
        this.tipo = tipo;
        this.fuerza = new Estadistica("Fuerza", tipo.getFuerza());
        this.destreza = new Estadistica("Destreza", tipo.getDestreza());
        this.defensa = new Estadistica("Defensa", tipo.getDefensa());
        this.velocidad = new Estadistica("Velocidad", tipo.getVelocidad());
        this.SPRITESHEET = new Texture(tipo.getRutaSprite());
        this.sprite = new Sprite(SPRITESHEET, 0, 0, 100, 142);
        this.sprite.setPosition(200, 200);
    }

    //Métodos
    public void dibujar(SpriteBatch batch) {
        batch.begin();
        sprite.draw(batch);

        batch.end();
    }

    //Getters y setters
    public String getNombre() {
        return this.tipo.getNombre();
    }

    public String getRutaSprite() {
        return this.tipo.getRutaSprite();
    }

    public Estadistica getFuerza() {
        return this.fuerza;
    }

    public Estadistica getDestreza() {
        return this.destreza;
    }

    public Estadistica getDefensa() {
        return this.defensa;
    }

    public Estadistica getVelocidad() {
        return this.velocidad;
    }
}
