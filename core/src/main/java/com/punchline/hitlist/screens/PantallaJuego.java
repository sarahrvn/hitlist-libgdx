package com.punchline.hitlist.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.punchline.hitlist.elementosJuego.Mapa;
import com.punchline.hitlist.elementosJuego.MapaDisponible;
import com.punchline.hitlist.personajes.Personaje;

public class PantallaJuego {

    private final Mapa mapa;

    public PantallaJuego() {
        mapa = new Mapa(MapaDisponible.MAPA_BOSQUE);
    }

    public void ajustarCamara(OrthographicCamera camara, int ancho, int alto) {
        mapa.ajustarCamara(camara, ancho, alto);
    }

    public void render(SpriteBatch batch, OrthographicCamera camara, Personaje personaje1) {
        mapa.renderFondo(batch, camara);
        mapa.renderMapa(camara);
        personaje1.dibujar(batch);
    }

    public void dispose() {
        mapa.dispose();
    }
}
