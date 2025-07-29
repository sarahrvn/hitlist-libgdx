package com.punchline.hitlist;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.punchline.hitlist.pantallaJuego.Mapa;
import com.punchline.hitlist.pantallaJuego.MapaDisponible;

public class Principal extends ApplicationAdapter {
    private OrthographicCamera camara;
    private SpriteBatch batch;
    private Mapa mapa;

    @Override
    public void create() {
        camara = new OrthographicCamera();
        batch = new SpriteBatch();

        // Eleccion del mapa a cargar
        mapa = new Mapa(MapaDisponible.MAPA_BOSQUE);
        mapa.ajustarCamara(camara, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();
        batch.setProjectionMatrix(camara.combined);

        mapa.renderFondo(batch, camara);
        mapa.renderMapa(camara);
    }

    @Override
    public void resize(int width, int height) {
        mapa.ajustarCamara(camara, width, height);
    }

    @Override
    public void dispose() {
        mapa.dispose();
        batch.dispose();
    }
}
