package com.punchline.hitlist;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.punchline.hitlist.screens.PantallaJuego;
import com.punchline.hitlist.screens.PantallaLogo;

public class Principal extends ApplicationAdapter {
    private OrthographicCamera camara;
    private SpriteBatch batch;

    private PantallaLogo pantallaLogo;
    private PantallaJuego pantallaJuego;

    private boolean mostrarJuego = false;
    private float tiempo = 0;

    @Override
    public void create()
    {
        camara = new OrthographicCamera();
        batch = new SpriteBatch();

        pantallaLogo = new PantallaLogo();

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();


        // Fondo blanco para el logo.
        Gdx.gl.glClearColor(1, 1, 1, 1); // fondo blanco
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Actualizacion de la camara para el batch
        camara.update();
        batch.setProjectionMatrix(camara.combined);

        if (!mostrarJuego)
        {
            // Ajuste de la camara para que abarque toda la pantalla y centra el logo
            camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            camara.update();
            pantallaLogo.render(batch, camara);
            tiempo += delta;
            if (tiempo >= 7f)
            {
                mostrarJuego = true;
                pantallaJuego = new PantallaJuego();
                pantallaJuego.ajustarCamara(camara, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        }
        else
        {
            if (pantallaJuego != null)
            {
                pantallaJuego.render(batch, camara);
            }
        }
    }


    @Override
    public void resize(int width, int height)
    {
        // Ajusta la camara si ya se esta mostrando el juego en la pantalla
        if(pantallaJuego != null)
        {
            pantallaJuego.ajustarCamara(camara, width, height);
        }
    }

    @Override
    public void dispose()
    {
        if(pantallaLogo != null) pantallaLogo.dispose();
        if(pantallaJuego != null) pantallaJuego.dispose();
        batch.dispose();
    }
}
