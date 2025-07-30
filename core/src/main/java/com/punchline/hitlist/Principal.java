package com.punchline.hitlist;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.punchline.hitlist.personajes.Personaje;
import com.punchline.hitlist.personajes.TipoPersonaje;
import com.punchline.hitlist.screens.PantallaJuego;
import com.punchline.hitlist.screens.PantallaLogo;
import com.punchline.hitlist.screens.PantallaTitulo;
import com.punchline.hitlist.screens.EstadoScreen;

public class Principal extends ApplicationAdapter {
    private OrthographicCamera camara;
    private SpriteBatch batch;

    private PantallaLogo pantallaLogo;
    private PantallaJuego pantallaJuego;
    private PantallaTitulo pantallaTitulo;
    private EstadoScreen estadoActual;

    private Personaje personaje1; //PROVISIONAL. PRUEBA. PROTOTIPO

    private float tiempo = 0;

    @Override
    public void create()
    {
        camara = new OrthographicCamera();
        batch = new SpriteBatch();

        pantallaLogo = new PantallaLogo();
        estadoActual = EstadoScreen.LOGO;

        personaje1 = new Personaje(TipoPersonaje.TIPAZO);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();


        // Fondo blanco para el logo.
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Actualizacion de la camara para el batch
        camara.update();
        batch.setProjectionMatrix(camara.combined);

        switch (estadoActual)
        {
            case LOGO:
                // Ajuste de la camara para que abarque toda la pantalla y centra el logo
                camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                camara.update();
                pantallaLogo.render(batch, camara);
                tiempo += delta;
                if (tiempo >= 7f) {
                    Gdx.gl.glClearColor(1, 1, 1, 1);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                    pantallaTitulo = new PantallaTitulo();
                    estadoActual = EstadoScreen.TITULO;
                }
                break;

            case TITULO:
                Gdx.gl.glClearColor(0.98f, 0.49f, 0.043f, 1f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                pantallaTitulo.render(batch, camara);

                if (pantallaTitulo.SaltarAJuego())
                {
                    pantallaJuego = new PantallaJuego();
                    pantallaJuego.ajustarCamara(camara, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    estadoActual = EstadoScreen.JUEGO;
                }
                break;

            case JUEGO:
                //batch.begin();
                //personaje1.dibujar(batch);
                pantallaJuego.render(batch,camara,personaje1);
                //batch.begin();
                break;
        }
    }

    @Override
    public void resize(int width, int height)
    {
        if (estadoActual == EstadoScreen.JUEGO && pantallaJuego != null)
        {
            pantallaJuego.ajustarCamara(camara, width, height);
        }
    }

    @Override
    public void dispose()
    {
        if (pantallaLogo != null) pantallaLogo.dispose();
        if (pantallaTitulo != null) pantallaTitulo.dispose();
        if (pantallaJuego != null) pantallaJuego.dispose();
        batch.dispose();
    }
}
