package com.punchline.hitlist.personajes;

public enum TipoPersonaje {
    SABRINA_CARPENTER("Sabrina Carpenter", 5, 7, 3, 4, "sprites/sabrina_carpenter.png"),
    RICARDO_FORT("Ricardo Fort", 3, 5, 7, 6, "sprites/ricardo_fort.png"),
    VEGETTA777("Vegetta777", 8, 4, 2, 2, "sprites/vegetta777.png");

    private final String NOMBRE;
    private final int FUERZA, DESTREZA, DEFENSA, VELOCIDAD;
    private final String RUTASPRITE;

    TipoPersonaje(String nombre, int fuerza, int destreza, int defensa, int velocidad, String rutaSprite) {
        this.NOMBRE = nombre;
        this.FUERZA = fuerza;
        this.DESTREZA = destreza;
        this.DEFENSA = defensa;
        this.VELOCIDAD = velocidad;
        this.RUTASPRITE = rutaSprite;
    }

    public String getNombre() {
        return this.NOMBRE;
    }

    public int getFuerza() {
        return this.FUERZA;
    }

    public int getDestreza() {
        return this.DESTREZA;
    }

    public int getDefensa() {
        return this.DEFENSA;
    }

    public int getVelocidad() {
        return this.VELOCIDAD;
    }

    public String getRutaSprite() {
        return this.RUTASPRITE;
    }
}
