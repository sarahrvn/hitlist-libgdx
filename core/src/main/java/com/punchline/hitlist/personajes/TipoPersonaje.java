package com.punchline.hitlist.personajes;


public enum TipoPersonaje {
    BILLIE_EILISH("Billie Eilish", 10, 10, 10, 10, "sprites/Billie_Eilish.txt"),
    MICHAEL_JACKSON("Michael Jackson", 5, 7, 3, 4, "sprites/Michael_Jackson.txt"),
    FRIDA_KAHLO("Frida Kahlo", 3, 5, 7, 6, "sprites/Frida_Kahlo.txt"),
    LEBRON_JAMES("Lebron James", 8, 4, 2, 2, "sprites/Lebron_James.txt");


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
