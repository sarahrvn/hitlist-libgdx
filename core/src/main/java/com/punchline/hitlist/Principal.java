package com.punchline.hitlist;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.punchline.hitlist.elementosJuego.MapaDisponible;
import com.punchline.hitlist.screens.*;
import com.punchline.hitlist.personajes.TipoPersonaje;

public class Principal extends ApplicationAdapter {
    private OrthographicCamera camara;
    private SpriteBatch batch;

    private PantallaLogo pantallaLogo;
    private PantallaJuego pantallaJuego;
    private PantallaTitulo pantallaTitulo;
    private PantallaMenu pantallaMenu;
    private PantallaSeleccionPersonaje pantallaSeleccionPersonaje;
    private PantallaSeleccionMapa pantallaSeleccionMapa;
    private EstadoScreen estadoActual;

    private TipoPersonaje personajeElegido;
    private MapaDisponible mapaElegido;

    private float tiempo = 0;

    @Override
    public void create() {
        camara = new OrthographicCamera();
        batch = new SpriteBatch();

        pantallaLogo = new PantallaLogo();
        estadoActual = EstadoScreen.LOGO;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();
        batch.setProjectionMatrix(camara.combined);

        switch (estadoActual) {
            case LOGO:
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

                if (pantallaTitulo.SaltarAMenu()) {
                    pantallaMenu = new PantallaMenu();
                    estadoActual = EstadoScreen.MENU;
                }
                break;

            case MENU:
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                camara.update();

                pantallaMenu.render(batch, camara);

                if (pantallaMenu.quiereJugar()) {
                    pantallaSeleccionPersonaje = new PantallaSeleccionPersonaje();
                    estadoActual = EstadoScreen.SELECCION_DE_PERSONAJE;
                } else if (pantallaMenu.quiereSalir()) {
                    Gdx.app.exit();
                }
                break;

            case SELECCION_DE_PERSONAJE:
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                camara.update();

                pantallaSeleccionPersonaje.render(batch, camara);

                if (pantallaSeleccionPersonaje.hayPersonajeSeleccionado()) {
                    personajeElegido = pantallaSeleccionPersonaje.getPersonajeSeleccionado();
                    pantallaSeleccionMapa = new PantallaSeleccionMapa();
                    estadoActual = EstadoScreen.SELECCION_DE_MAPA;
                }
                break;

            case SELECCION_DE_MAPA:
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                camara.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                camara.update();

                pantallaSeleccionMapa.render(batch, camara);

                if (pantallaSeleccionMapa.hayMapaSeleccionado()) {
                    mapaElegido = pantallaSeleccionMapa.getMapaSeleccionado();
                    pantallaJuego = new PantallaJuego(mapaElegido, personajeElegido);
                    pantallaJuego.ajustarCamara(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    estadoActual = EstadoScreen.JUEGO;
                    estadoActual = EstadoScreen.JUEGO;


                }

                break;

            case JUEGO:
                pantallaJuego.render(delta);
                if (pantallaJuego.debeVolverAlMenu()) {
                    pantallaMenu = new PantallaMenu();
                    estadoActual = EstadoScreen.MENU;
                    pantallaJuego = null;
                }
                break;
        }
    }

    public void resize(int width, int height)
    {
        if (pantallaJuego != null) {
            pantallaJuego.ajustarCamara(width, height);
        }
    }


    @Override
    public void dispose() {
        if (pantallaLogo != null) pantallaLogo.dispose();
        if (pantallaTitulo != null) pantallaTitulo.dispose();
        if (pantallaMenu != null) pantallaMenu.dispose();
        if (pantallaSeleccionPersonaje != null) pantallaSeleccionPersonaje.dispose();
        if (pantallaSeleccionMapa != null) pantallaSeleccionMapa.dispose();
        if (pantallaJuego != null) pantallaJuego.dispose();
        batch.dispose();
    }
}
