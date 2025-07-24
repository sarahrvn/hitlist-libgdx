package com.punchline.hitlist.personajes;

public class Estadistica {

    private final int VALOR_MIN = 0;
    private final int VALOR_MAX = 10;
    private String nombre;
    private int valor;

    public Estadistica(String nombre, int valor) {
        this.nombre = nombre;
        this.valor = valor;
    }




    public String getNombre() {
        return nombre;
    }

    public int getValor() {
        return valor;
    }

    public void setEstado(int valor){
        if(valor < VALOR_MIN) {
            this.valor = VALOR_MIN;
        } else if (valor > VALOR_MAX) {
            this.valor = VALOR_MAX;
        } else {
            this.valor = valor;
        }
    }

    public void ajustarValor(int cantidad){
        setEstado(this.valor + cantidad);
    }
}
