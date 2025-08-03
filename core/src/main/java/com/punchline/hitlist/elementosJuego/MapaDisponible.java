package com.punchline.hitlist.elementosJuego;

public enum MapaDisponible {
    MAPA_BOSQUE("mapas/mapa_colisiones.tmx", "fondos/fondo.png");

    private final String MAPA_TMX;
    private final String FONDO_PNG;

    MapaDisponible(String MAPA_TMX, String FONDO_PNG) {
        this.MAPA_TMX = MAPA_TMX;
        this.FONDO_PNG = FONDO_PNG;
    }

    public String getmapaTmx() {
        return MAPA_TMX;
    }

    public String getfondoPng() {
        return FONDO_PNG;
    }
}
