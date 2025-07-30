package com.punchline.hitlist.screens;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaMenu {

    private final Texture FONDO;
    private final Texture TITULO;

    public PantallaMenu() {
        FONDO = new Texture("fondos/Fondo_Menu.png");
        TITULO = new Texture("logos/Hitlist_Titulo.png");
    }

    // Dibujado y centrado del logo en la pantalla.
    public void render(SpriteBatch batch, OrthographicCamera camara) {
        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        // Dibujar fondo para que siempre ocupe toda la pantalla
        batch.draw(FONDO, 0, 0, camara.viewportWidth, camara.viewportHeight);

        // Dibujar título proporcional al tamaño de la pantalla
        float tituloAncho = camara.viewportWidth / 6f;
        float tituloAlto = tituloAncho * ((float) TITULO.getHeight() / TITULO.getWidth());

        float tituloX = camara.viewportWidth / 15f;
        float tituloY = camara.viewportHeight - tituloAlto - camara.viewportHeight / 20f;

        batch.draw(TITULO, tituloX, tituloY, tituloAncho, tituloAlto);

        batch.end();
    }

    public void dispose() {
        FONDO.dispose();
        TITULO.dispose();
    }
}
