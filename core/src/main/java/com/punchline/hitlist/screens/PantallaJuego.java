package com.punchline.hitlist.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.punchline.hitlist.elementosJuego.Mapa;
import com.punchline.hitlist.elementosJuego.MapaDisponible;
import com.punchline.hitlist.personajes.Personaje;
import com.punchline.hitlist.personajes.TipoPersonaje;

public class PantallaJuego {

    private final Mapa mapa;
    private Personaje personaje1 = new Personaje(TipoPersonaje.TIPAZO);; //PROVISIONAL. En realidad, la PantallaSeleccion es la encargada de, justamente, indicar el personaje elegido y pasárselo a PantallaJuego.

    public PantallaJuego() {
        mapa = new Mapa(MapaDisponible.MAPA_BOSQUE);
    }

    public void ajustarCamara(OrthographicCamera camara, int ancho, int alto) {
        mapa.ajustarCamara(camara, ancho, alto);
    }

    public void render(SpriteBatch batch, OrthographicCamera camara) {
        mapa.renderFondo(batch, camara);
        mapa.renderMapa(camara);
        personaje1.dibujar(batch);
        personaje1.realizarMovimientos();
    }

    public void dispose() {
        mapa.dispose();
    }
}
