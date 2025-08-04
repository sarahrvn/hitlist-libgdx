package com.punchline.hitlist.elementosJuego;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {

    private OrthographicCamera camaraHud;
    private Viewport viewport;
    private BitmapFont font;
    private Texture iconoVida;

    private int vidas = 3;
    private float tiempoRestante = 60f; // Tiempo inicial en segundos
    private boolean pausado = false;


    public Hud() {
        camaraHud = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camaraHud);
        viewport.apply();

        camaraHud.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camaraHud.update();

        iconoVida = new Texture("hud/corazon_amarilloo.png");
        font = new BitmapFont();
    }

    public void mostrarPausa(boolean pausado) {
        this.pausado = pausado;
    }

    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camaraHud.combined);
        batch.begin();

        // Dibujar las vidas
        for (int i = 0; i < vidas; i++) {
            batch.draw(iconoVida, 10 + i * 40, viewport.getWorldHeight() - 50, 32, 32);
        }

        // Mostrar tiempo restante
        font.draw(batch, "Tiempo: " + (int) tiempoRestante, viewport.getWorldWidth() - 130, viewport.getWorldHeight() - 20);

        if (pausado) {
            font.draw(batch, "PAUSADO", viewport.getWorldWidth() / 2 - 40, viewport.getWorldHeight() / 2);
        }
        batch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        font.dispose();
        iconoVida.dispose();
    }

    public void setTiempoRestante(float tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }
}
