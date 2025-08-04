package com.punchline.hitlist.jugadores;

import com.punchline.hitlist.personajes.Personaje;
import com.punchline.hitlist.personajes.TipoPersonaje;

import java.util.HashMap;

public class Jugador {
    private String nombre;
    private Personaje combatiente;

    private HashMap<String, Personaje> compras = new HashMap<>();

    public Jugador(String nombre){
        this.nombre = nombre;
    }

    //esto falta terminar
    public Personaje asignarPersonaje(String nombrePersonaje) {
        return personajeElegido = compras.get(nombrePersonaje);
    }

    public void comprar(TipoPersonaje tipoElegido) {
        compras.put(tipoElegido.getNombre(), new Personaje(tipoElegido));
    }

}
