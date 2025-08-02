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

    public Hud()
    {
        camaraHud = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camaraHud);
        viewport.apply();

        camaraHud.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camaraHud.update();

        iconoVida = new Texture("hud/corazon_amarilloo.png");
        font = new BitmapFont();
    }

    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camaraHud.combined);
        batch.begin();

        for (int i = 0; i < vidas; i++)
        {
            batch.draw(iconoVida, 10 + i * 40, viewport.getWorldHeight() - 50, 32, 32);
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
}
