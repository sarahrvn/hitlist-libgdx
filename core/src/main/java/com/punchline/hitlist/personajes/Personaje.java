package com.punchline.hitlist.personajes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Personaje {

    private TipoPersonaje tipo;
    private Estadistica fuerza, destreza, defensa, velocidad;
    private final Texture SPRITE;

    private int x = 100, y = 100;
    private int vida = 3;
    private int resistencia = 1000; //Tendencia a ser empujado hacia los límites del mapa y perder una vida (100 es poca, 1 es mucha);

    public Personaje(TipoPersonaje tipo) {
        this.tipo = tipo;
        this.fuerza = new Estadistica("Fuerza", tipo.getFuerza());
        this.destreza = new Estadistica("Destreza", tipo.getDestreza());
        this.defensa = new Estadistica("Defensa", tipo.getDefensa());
        this.velocidad = new Estadistica("Velocidad", tipo.getVelocidad());
        this.SPRITE = new Texture(tipo.getRutaSprite());
    }

    //Métodos
    public void dibujar(SpriteBatch batch) {
        batch.draw(SPRITE, x, y);
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
