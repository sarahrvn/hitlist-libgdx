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

    private int mapaPixelAncho, mapaPixelAlto;

    @Override
    public void create() {
        fondo = new Texture("fondos/fondo.png");
        batch = new SpriteBatch();

        // Cargar mapa y obtener dimensiones en píxeles
        mapa = new TmxMapLoader().load("mapas/mapa.tmx");
        renderMapa = new OrthogonalTiledMapRenderer(mapa);

        MapProperties props = mapa.getProperties();
        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);
        int mapWidth = props.get("width", Integer.class);
        int mapHeight = props.get("height", Integer.class);


        mapaPixelAncho = tileWidth * mapWidth;
        mapaPixelAlto = tileHeight * mapHeight;

        // Crear cámara ajustada
        camara = new OrthographicCamera();
        ajustarCamara(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Calcula el zoom para que el mapa entre en pantalla.

    }


    // Calcula cuánto debería hacer zoom la cámara para que el mapa entero entre en pantalla.
    private void ajustarCamara(int anchoPantalla, int altoPantalla) {
        float escalaX = (float) anchoPantalla / mapaPixelAncho;
        float escalaY = (float) altoPantalla / mapaPixelAlto;
        float zoom = 1f / Math.min(escalaX, escalaY); // zoom para que el mapa entre en pantalla

        camara.setToOrtho(false, anchoPantalla, altoPantalla);
        camara.zoom = zoom;
        camara.position.set(mapaPixelAncho / 2f, mapaPixelAlto / 2f, 0);
        camara.update();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();

        // Dibujar fondo
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(fondo, camara.position.x - (camara.viewportWidth * camara.zoom) / 2f,
            camara.position.y - (camara.viewportHeight * camara.zoom) / 2f,
            camara.viewportWidth * camara.zoom,
            camara.viewportHeight * camara.zoom);
        batch.end();

        // Renderizar mapa
        renderMapa.setView(camara);
        renderMapa.render();
    }

    @Override
    public void resize(int width, int height) {
        ajustarCamara(width, height);
    }

    @Override
    public void dispose() {
        fondo.dispose();
        batch.dispose();
        mapa.dispose();
    }
}
