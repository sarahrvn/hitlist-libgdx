package com.punchline.hitlist.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.punchline.hitlist.elementosJuego.MapaDisponible;


public class PantallaSeleccionMapa {


    private final Texture FONDO;
    private final BitmapFont font;


    private final Texture previewYate;
    private final Texture previewAlfombraRoja;
    private final Texture previewConcierto;
    private final Texture previewCartel;


    private final Rectangle rectYate;
    private final Rectangle rectAlfombraRoja;
    private final Rectangle rectConcierto;
    private final Rectangle rectCartel;


    private final Vector3 touch = new Vector3();


    private MapaDisponible mapaSeleccionado = null;


    public PantallaSeleccionMapa() {
        FONDO = new Texture("fondos/Fondo_Menu.png");
        font = new BitmapFont();
        font.getData().setScale(2.5f);
        font.setColor(Color.ORANGE);


        previewYate = new Texture("mapas/mapa_yate.png");
        previewAlfombraRoja = new Texture("mapas/mapa_alfombra_roja.png");
        previewConcierto = new Texture("mapas/mapa_concierto.png");
        previewCartel = new Texture("mapas/mapa_cartel.png");


        rectYate = new Rectangle();
        rectAlfombraRoja = new Rectangle();
        rectConcierto = new Rectangle();
        rectCartel = new Rectangle();
    }


    public void render(SpriteBatch batch, OrthographicCamera camara) {
        batch.setProjectionMatrix(camara.combined);
        batch.begin();


        batch.draw(FONDO, 0, 0, camara.viewportWidth, camara.viewportHeight);


        String titulo = "SELECCIONA UN MAPA";
        font.draw(batch, titulo, camara.viewportWidth / 2f - 200f, camara.viewportHeight - 50f);


        // Calcular tamaños y posiciones para las previsualizaciones (2x2)
        float espaciado = 30f;
        float anchoPreview = (camara.viewportWidth - espaciado * 3) / 2f * 0.7f;
        float altoPreview = anchoPreview * 0.6f;


        float xIzq = camara.viewportWidth * 0.15f;
        float xDer = camara.viewportWidth * 0.55f;
        float yArriba = camara.viewportHeight * 0.65f;
        float yAbajo = camara.viewportHeight * 0.25f;


        batch.draw(previewYate, xIzq, yArriba, anchoPreview, altoPreview);
        batch.draw(previewAlfombraRoja, xDer, yArriba, anchoPreview, altoPreview);
        batch.draw(previewConcierto, xIzq, yAbajo, anchoPreview, altoPreview);
        batch.draw(previewCartel, xDer, yAbajo, anchoPreview, altoPreview);




        BitmapFont fontPeq = new BitmapFont();
        fontPeq.getData().setScale(1.5f);
        fontPeq.setColor(Color.WHITE);


        fontPeq.draw(batch, "YATE", xIzq + 10f, yArriba - 10f);
        fontPeq.draw(batch, "ALFOMBRA ROJA", xDer + 10f, yArriba - 10f);
        fontPeq.draw(batch, "CONCIERTO", xIzq + 10f, yAbajo - 10f);
        fontPeq.draw(batch, "CARTEL", xDer + 10f, yAbajo - 10f);


        batch.end();


        // Actualizar rectangulos de colision
        rectYate.set(xIzq, yArriba, anchoPreview, altoPreview);
        rectAlfombraRoja.set(xDer, yArriba, anchoPreview, altoPreview);
        rectConcierto.set(xIzq, yAbajo, anchoPreview, altoPreview);
        rectCartel.set(xDer, yAbajo, anchoPreview, altoPreview);


        // Detectar clicks
        if (Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(touch);


            if (rectYate.contains(touch.x, touch.y)) {
                mapaSeleccionado = MapaDisponible.MAPA_YATE;
            } else if (rectAlfombraRoja.contains(touch.x, touch.y)) {
                mapaSeleccionado = MapaDisponible.MAPA_ALFOMBRA_ROJA;
            } else if (rectConcierto.contains(touch.x, touch.y)) {
                mapaSeleccionado = MapaDisponible.MAPA_CONCIERTO;
            } else if (rectCartel.contains(touch.x, touch.y)) {
                mapaSeleccionado = MapaDisponible.MAPA_CARTEL;
            }
        }


        fontPeq.dispose();
    }


    public boolean hayMapaSeleccionado() {
        return mapaSeleccionado != null;
    }


    public MapaDisponible getMapaSeleccionado() {
        return mapaSeleccionado;
    }


    public void dispose() {
        FONDO.dispose();
        font.dispose();
        previewYate.dispose();
        previewAlfombraRoja.dispose();
        previewConcierto.dispose();
        previewCartel.dispose();
    }
}
