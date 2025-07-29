package com.punchline.hitlist.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.punchline.hitlist.elementosJuego.Mapa;
import com.punchline.hitlist.elementosJuego.MapaDisponible;

public class PantallaJuego {

    private final Mapa mapa;

    public PantallaJuego() {
        mapa = new Mapa(MapaDisponible.MAPA_BOSQUE);
    }

    public void ajustarCamara(OrthographicCamera camara, int ancho, int alto) {
        mapa.ajustarCamara(camara, ancho, alto);
    }

    public void render(SpriteBatch batch, OrthographicCamera camara) {
        mapa.renderFondo(batch, camara);
        mapa.renderMapa(camara);
    }

    public void dispose() {
        mapa.dispose();
    }
}
