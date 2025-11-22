package com.punchline.hitlist.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.punchline.hitlist.personajes.TipoPersonaje;


public class PantallaSeleccionPersonaje {


    private final Texture FONDO;
    private final BitmapFont font;
    private final BitmapFont fontPeq;
    private final BitmapFont fontStats;


    private final Texture previewBillie;
    private final Texture previewMichael;
    private final Texture previewFrida;
    private final Texture previewLebron;


    private final Rectangle rectBillie;
    private final Rectangle rectMichael;
    private final Rectangle rectFrida;
    private final Rectangle rectLebron;


    private final Vector3 touch = new Vector3();


    private TipoPersonaje personajeSeleccionado = null;


    public PantallaSeleccionPersonaje() {
        FONDO = new Texture("fondos/Fondo_Menu.png");


        font = new BitmapFont();
        font.getData().setScale(2.5f);
        font.setColor(Color.ORANGE);


        fontPeq = new BitmapFont();
        fontPeq.getData().setScale(1.5f);
        fontPeq.setColor(Color.WHITE);


        fontStats = new BitmapFont();
        fontStats.getData().setScale(1.2f);
        fontStats.setColor(Color.CYAN);


        // Cargar imagenes preview de cada personaje
        previewBillie = new Texture("sprites/Billie preview.png");
        previewMichael = new Texture("sprites/Michael preview.png");
        previewFrida = new Texture("sprites/Frida preview.png");
        previewLebron = new Texture("sprites/Lebron preview.png");


        rectBillie = new Rectangle();
        rectMichael = new Rectangle();
        rectFrida = new Rectangle();
        rectLebron = new Rectangle();
    }


    public void render(SpriteBatch batch, OrthographicCamera camara) {
        batch.setProjectionMatrix(camara.combined);
        batch.begin();


        batch.draw(FONDO, 0, 0, camara.viewportWidth, camara.viewportHeight);


        String titulo = "SELECCIONA TU PERSONAJE";
        font.draw(batch, titulo, camara.viewportWidth / 2f - 250f, camara.viewportHeight - 50f);


        // Calcular posiciones para 2x2 grid
        float espaciado = 30f;
        float altoPreview = 160f;
        float anchoPreview = altoPreview; // Mantener proporciones cuadradas


        float xIzq = camara.viewportWidth * 0.20f;
        float xDer = camara.viewportWidth * 0.60f;
        float yArriba = camara.viewportHeight * 0.60f;
        float yAbajo = camara.viewportHeight * 0.20f;


        // Dibujar imagenes preview de personajes
        batch.draw(previewBillie, xIzq, yArriba, anchoPreview, altoPreview);
        batch.draw(previewMichael, xDer, yArriba, anchoPreview, altoPreview);
        batch.draw(previewFrida, xIzq, yAbajo, anchoPreview, altoPreview);
        batch.draw(previewLebron, xDer, yAbajo, anchoPreview, altoPreview);


        // Dibujar nombres
        fontPeq.draw(batch, TipoPersonaje.BILLIE_EILISH.getNombre(), xIzq + 10f, yArriba - 10f);
        fontPeq.draw(batch, TipoPersonaje.MICHAEL_JACKSON.getNombre(), xDer + 10f, yArriba - 10f);
        fontPeq.draw(batch, TipoPersonaje.FRIDA_KAHLO.getNombre(), xIzq + 10f, yAbajo - 10f);
        fontPeq.draw(batch, TipoPersonaje.LEBRON_JAMES.getNombre(), xDer + 10f, yAbajo - 10f);


        // Dibujar estadísticas
        dibujarEstadisticas(batch, TipoPersonaje.BILLIE_EILISH, xIzq, yArriba + altoPreview + 10f);
        dibujarEstadisticas(batch, TipoPersonaje.MICHAEL_JACKSON, xDer, yArriba + altoPreview + 10f);
        dibujarEstadisticas(batch, TipoPersonaje.FRIDA_KAHLO, xIzq, yAbajo + altoPreview + 10f);
        dibujarEstadisticas(batch, TipoPersonaje.LEBRON_JAMES, xDer, yAbajo + altoPreview + 10f);


        batch.end();


        // Actualizar rectangulos de colision
        rectBillie.set(xIzq, yArriba, anchoPreview, altoPreview);
        rectMichael.set(xDer, yArriba, anchoPreview, altoPreview);
        rectFrida.set(xIzq, yAbajo, anchoPreview, altoPreview);
        rectLebron.set(xDer, yAbajo, anchoPreview, altoPreview);


        // Detectar clicks
        if (Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(touch);


            if (rectBillie.contains(touch.x, touch.y)) {
                personajeSeleccionado = TipoPersonaje.BILLIE_EILISH;
            } else if (rectMichael.contains(touch.x, touch.y)) {
                personajeSeleccionado = TipoPersonaje.MICHAEL_JACKSON;
            } else if (rectFrida.contains(touch.x, touch.y)) {
                personajeSeleccionado = TipoPersonaje.FRIDA_KAHLO;
            } else if (rectLebron.contains(touch.x, touch.y)) {
                personajeSeleccionado = TipoPersonaje.LEBRON_JAMES;
            }
        }
    }


    private void dibujarEstadisticas(SpriteBatch batch, TipoPersonaje tipo, float x, float y) {
        float lineHeight = 20f;
        fontStats.draw(batch, "FUE: " + tipo.getFuerza(), x + 5f, y);
        fontStats.draw(batch, "DES: " + tipo.getDestreza(), x + 5f, y - lineHeight);
        fontStats.draw(batch, "DEF: " + tipo.getDefensa(), x + 5f, y - lineHeight * 2);
        fontStats.draw(batch, "VEL: " + tipo.getVelocidad(), x + 5f, y - lineHeight * 3);
    }


    public boolean hayPersonajeSeleccionado() {
        return personajeSeleccionado != null;
    }


    public TipoPersonaje getPersonajeSeleccionado() {
        return personajeSeleccionado;
    }


    public void dispose() {
        FONDO.dispose();
        font.dispose();
        fontPeq.dispose();
        fontStats.dispose();
        previewBillie.dispose();
        previewMichael.dispose();
        previewFrida.dispose();
        previewLebron.dispose();
    }
}
