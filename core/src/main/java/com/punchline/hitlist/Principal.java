package com.punchline.hitlist;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Principal extends ApplicationAdapter {
    private OrthographicCamera camara;
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderMapa;

    private Texture fondo;
    private SpriteBatch batch;

    private int anchoPantalla, altoPantalla;


    @Override
    public void create() {
        // Tamaño de pantalla en píxeles
        anchoPantalla = Gdx.graphics.getWidth();
        altoPantalla = Gdx.graphics.getHeight();

        // Cargar fondo
        fondo = new Texture("fondos/fondo.png");
        batch = new SpriteBatch();

        // Cargar mapa
        mapa = new TmxMapLoader().load("mapas/mapa.tmx");
        renderMapa = new OrthogonalTiledMapRenderer(mapa);

        // Obtener dimensiones del mapa
        MapProperties props = mapa.getProperties();
        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);
        int mapWidth = props.get("width", Integer.class);
        int mapHeight = props.get("height", Integer.class);

        float mapaPixelAncho = tileWidth * mapWidth;
        float mapaPixelAlto = tileHeight * mapHeight;

        // Crear cámara con tamaño de pantalla, pero sin escalar
        camara = new OrthographicCamera(anchoPantalla, altoPantalla);
        camara.position.set(mapaPixelAncho / 2f, mapaPixelAlto / 2f, 0);
        camara.update();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camara.update();

        // Dibujar fondo que ocupa toda la pantalla
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, camara.position.x - anchoPantalla / 2f, camara.position.y - altoPantalla / 2f, anchoPantalla, altoPantalla);
        batch.end();

        // Renderizar mapa centrado
        renderMapa.setView(camara);
        renderMapa.render();
    }

    @Override
    public void resize(int width, int height) {
        camara.viewportWidth = width;
        camara.viewportHeight = height;
        camara.update();
    }

    @Override
    public void dispose() {
        fondo.dispose();
        batch.dispose();
        renderMapa.dispose();
        mapa.dispose();
    }
}
