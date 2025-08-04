package com.punchline.hitlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
public class PantallaMenu {

    private final Texture FONDO;
    private final Texture TITULO;
    private final BitmapFont font;

    private final Rectangle rectJugar;
    private final Rectangle rectSalir;
    private final Vector3 touch = new Vector3();

    private boolean quiereJugar = false;
    private boolean quiereSalir = false;

    private final GlyphLayout layout = new GlyphLayout();
    public PantallaMenu() {
        FONDO = new Texture("fondos/Fondo_Menu.png");
        TITULO = new Texture("logos/Hitlist_Titulo.png");
        font = new BitmapFont();

        font.getData().setScale(3f);
        font.setColor(Color.ORANGE);

        rectJugar = new Rectangle();
        rectSalir = new Rectangle();
    }

    public void render(SpriteBatch batch, OrthographicCamera camara)
    {
        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        batch.draw(FONDO, 0, 0, camara.viewportWidth, camara.viewportHeight);

        float tituloAncho = camara.viewportWidth / 7f;
        float tituloAlto = tituloAncho * ((float) TITULO.getHeight() / TITULO.getWidth());

        float tituloX = camara.viewportWidth / 15f;
        float tituloY = camara.viewportHeight - tituloAlto - camara.viewportHeight / 20f;

        batch.draw(TITULO, tituloX, tituloY, tituloAncho, tituloAlto);

        String textoJugar = "Jugar";
        layout.setText(font, textoJugar);
        float jugarX = camara.viewportWidth / 10f;
        float jugarY = tituloY - 80f;
        font.draw(batch, textoJugar, jugarX, jugarY);

        String textoSalir = "Salir";
        layout.setText(font, textoSalir);
        float salirX = jugarX;
        float salirY = jugarY - 100f;
        font.draw(batch, textoSalir, salirX, salirY);

        batch.end();

        if (Gdx.input.justTouched())
        {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(touch);

            layout.setText(font, textoJugar);
            rectJugar.set(jugarX, jugarY - layout.height, layout.width, layout.height);

            layout.setText(font, textoSalir);
            rectSalir.set(salirX, salirY - layout.height, layout.width, layout.height);

            if (rectJugar.contains(touch.x, touch.y))
            {
                quiereJugar = true;
            }
            else if (rectSalir.contains(touch.x, touch.y))
            {
                quiereSalir = true;
            }
        }
    }

    public boolean quiereJugar()
    {
        return quiereJugar;
    }

    public boolean quiereSalir()
    {
        return quiereSalir;
    }

    public void dispose()
    {
        FONDO.dispose();
        TITULO.dispose();
        font.dispose();
    }
}
