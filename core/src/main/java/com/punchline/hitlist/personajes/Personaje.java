package com.punchline.hitlist.personajes;

public abstract class Personaje {

    private String nombre;
    private int vida = 3;
    private int resistencia = 1000; //Tendencia a ser empujado hacia los límites del mapa y perder una vida (100 es poca, 1 es mucha);
    private Categoria categoria;
    private Estadistica fuerza;
    private Estadistica destreza;
    private Estadistica defensa;
    private Estadistica velocidad;

    public Personaje(int valorFuerza) {
        this.fuerza = new Estadistica("Fuerza", valorFuerza);
    }
}
