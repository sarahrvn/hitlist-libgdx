package com.punchline.hitlist.pantallaJuego;

public enum MapasDisponibles {
    MAPA_BOSQUE("mapas/mapa.tmx", "fondos/fondo.png");

    private final String mapaTmx;
    private final String fondoPng;

    MapasDisponibles(String mapaTmx, String fondoPng) {
        this.mapaTmx = mapaTmx;
        this.fondoPng = fondoPng;
    }

    public String getmapaTmx() {
        return mapaTmx;
    }

    public String getfondoPng() {
        return fondoPng;
    }
}
