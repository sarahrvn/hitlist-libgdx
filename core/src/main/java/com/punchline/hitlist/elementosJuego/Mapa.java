package com.punchline.hitlist.elementosJuego;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Mapa {
    private final TiledMap mapa;
    private final OrthogonalTiledMapRenderer renderMapa;
    private final Texture fondo;

    private final int mapaPixelAncho, mapaPixelAlto;

    public Mapa(MapaDisponible tipoMapa) {
        fondo = new Texture(tipoMapa.getfondoPng());
        mapa = new TmxMapLoader().load(tipoMapa.getmapaTmx());
        renderMapa = new OrthogonalTiledMapRenderer(mapa);

        MapProperties props = mapa.getProperties();
        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);
        int mapWidth = props.get("width", Integer.class);
        int mapHeight = props.get("height", Integer.class);

        mapaPixelAncho = tileWidth * mapWidth;
        mapaPixelAlto = tileHeight * mapHeight;
    }

    // Getters para obtener las dimensiones del mapa
    public int getAncho() {
        return mapaPixelAncho;
    }

    public int getAlto() {
        return mapaPixelAlto;
    }

    // Dibujar el fondo ajustado al tamaño del viewport
    public void renderFondo(SpriteBatch batch, OrthographicCamera camara, Viewport viewport) {
        batch.draw(
            fondo,
            camara.position.x - viewport.getWorldWidth() / 2f,
            camara.position.y - viewport.getWorldHeight() / 2f,
            viewport.getWorldWidth(),
            viewport.getWorldHeight()
        );
    }



    public void renderMapa(OrthographicCamera camara) {
        renderMapa.setView(camara);
        renderMapa.render();
    }

    public void dispose() {
        fondo.dispose();
        mapa.dispose();
    }
}
