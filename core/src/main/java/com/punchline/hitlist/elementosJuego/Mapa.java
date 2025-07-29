package com.punchline.hitlist.elementosJuego;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class Mapa {
    private final TiledMap mapa;
    private final OrthogonalTiledMapRenderer renderMapa;
    private final Texture fondo;

    private final int mapaPixelAncho, mapaPixelAlto;

    public Mapa(MapaDisponible tipoMapa) {


        fondo = new Texture(tipoMapa.getfondoPng());
        mapa = new TmxMapLoader().load(tipoMapa.getmapaTmx());
        renderMapa = new OrthogonalTiledMapRenderer(mapa);
        // Crea y dibuja el mapa y el fondo que selecciona en el enum.


        //Calculo para descubrir cual es la anchura y la altura total del mapa.
        MapProperties props = mapa.getProperties();
        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);
        int mapWidth = props.get("width", Integer.class);
        int mapHeight = props.get("height", Integer.class);

        mapaPixelAncho = tileWidth * mapWidth;
        mapaPixelAlto = tileHeight * mapHeight;
    }

    public void ajustarCamara(OrthographicCamera camara, int anchoPantalla, int altoPantalla) {
        // Ajusta el zoom y la posición de la camara para que el mapa entre de forma completa en pantalla.
        float escalaX = (float) anchoPantalla / mapaPixelAncho;
        float escalaY = (float) altoPantalla / mapaPixelAlto;
        float zoom = 1f / Math.min(escalaX, escalaY);


        // Centra la camara al centro del mapa.
        camara.setToOrtho(false, anchoPantalla, altoPantalla);
        camara.zoom = zoom;
        camara.position.set(mapaPixelAncho / 2f, mapaPixelAlto / 2f, 0);
        camara.update();
    }

    public void renderFondo(SpriteBatch batch, OrthographicCamera camara) {
        // Dibujado del fondo

        batch.begin();
        batch.draw(fondo,
            camara.position.x - (camara.viewportWidth * camara.zoom) / 2f,
            camara.position.y - (camara.viewportHeight * camara.zoom) / 2f,
            camara.viewportWidth * camara.zoom,
            camara.viewportHeight * camara.zoom);
        batch.end();
    }

    public void renderMapa(OrthographicCamera camara) {
        // Dibujado del mapa
        renderMapa.setView(camara);
        renderMapa.render();
    }

    public void dispose() {
        fondo.dispose();
        mapa.dispose();
    }
}
