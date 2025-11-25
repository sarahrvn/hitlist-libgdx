package com.punchline.hitlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.punchline.hitlist.interacciones.TeclaListener;
import com.punchline.hitlist.personajes.TipoPersonaje;
import com.punchline.hitlist.elementosJuego.GestorSonidos;
import com.punchline.hitlist.elementosJuego.SonidoDisponible;

public class PantallaSeleccionPersonaje {

    // ... (Variables existentes) ...
    private final Texture FONDO;
    private BitmapFont fontNombre;
    private BitmapFont fontStats;
    private final Texture texturaCuadrado;
    private final Texture previewBillie;
    private final Texture previewMichael;
    private final Texture previewFrida;
    private final Texture previewLebron;
    private int indiceSeleccionado = 0;
    private final TeclaListener teclaListener;
    private TipoPersonaje personajeFinal = null;
    private final GlyphLayout layout = new GlyphLayout();
    private float tiempoAnimacion = 0;

    public PantallaSeleccionPersonaje() {
        FONDO = new Texture("fondos/Fondo_Seleccion_Personaje.png");
        teclaListener = new TeclaListener();
        Gdx.input.setInputProcessor(teclaListener);

        // --- AUDIO ---
        GestorSonidos.getInstancia().cargarMusica(SonidoDisponible.MUSICA_MENU);
        GestorSonidos.getInstancia().reproducirMusica(SonidoDisponible.MUSICA_MENU, true);
        GestorSonidos.getInstancia().cargarSonido(SonidoDisponible.MENU);

        // --- FUENTES ---
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/ari-w9500.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter paramNombre = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramNombre.size = 36;
            paramNombre.borderWidth = 2;
            paramNombre.borderColor = Color.BLACK;
            paramNombre.shadowOffsetX = 3;
            paramNombre.shadowOffsetY = 3;
            fontNombre = generator.generateFont(paramNombre);

            FreeTypeFontGenerator.FreeTypeFontParameter paramStats = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramStats.size = 25;
            paramStats.borderWidth = 2;
            paramStats.borderColor = Color.BLACK;
            fontStats = generator.generateFont(paramStats);
            fontStats.setColor(Color.GOLD);
            generator.dispose();
        } catch (Exception e) {
            fontNombre = new BitmapFont();
            fontNombre.getData().setScale(2f);
            fontStats = new BitmapFont();
        }

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.WHITE);
        pix.fill();
        texturaCuadrado = new Texture(pix);
        pix.dispose();

        previewBillie = new Texture("fondos/Billie_Menu.png");
        previewMichael = new Texture("fondos/Michael_Menu.png");
        previewFrida = new Texture("fondos/Frida_Menu.png");
        previewLebron = new Texture("fondos/Lebron_Menu.png");
    }

    public void render(SpriteBatch batch, OrthographicCamera camara) {
        manejarInput();
        tiempoAnimacion += Gdx.graphics.getDeltaTime();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(FONDO, 0, 0, camara.viewportWidth, camara.viewportHeight);

        float mitadAncho = camara.viewportWidth / 2f;
        float mitadAlto = camara.viewportHeight / 2f;
        float margen = 15f;
        float anchoCard = mitadAncho - (margen * 2);
        float altoCard = mitadAlto - (margen * 2);
        float xIzq = margen;
        float xDer = mitadAncho + margen;
        float yAbajo = margen;
        float yArriba = mitadAlto + margen;

        dibujarTarjeta(batch, previewMichael, TipoPersonaje.MICHAEL_JACKSON, xIzq, yArriba, anchoCard, altoCard, Color.GOLD, (indiceSeleccionado == 0), true);
        dibujarTarjeta(batch, previewBillie, TipoPersonaje.BILLIE_EILISH, xDer, yArriba, anchoCard, altoCard, Color.GOLD, (indiceSeleccionado == 1), false);
        dibujarTarjeta(batch, previewLebron, TipoPersonaje.LEBRON_JAMES, xIzq, yAbajo, anchoCard, altoCard, Color.GOLD, (indiceSeleccionado == 2), true);
        dibujarTarjeta(batch, previewFrida, TipoPersonaje.FRIDA_KAHLO, xDer, yAbajo, anchoCard, altoCard, Color.GOLD, (indiceSeleccionado == 3), false);

        batch.end();
    }

    private void manejarInput() {
        if (personajeFinal != null) return;

        boolean seMovio = false;

        if (teclaListener.isDerechaJustPressed()) {
            if (indiceSeleccionado % 2 == 0) { indiceSeleccionado++; seMovio = true; }
        }
        if (teclaListener.isIzquierdaJustPressed()) {
            if (indiceSeleccionado % 2 != 0) { indiceSeleccionado--; seMovio = true; }
        }
        if (teclaListener.isAbajoJustPressed()) {
            if (indiceSeleccionado < 2) { indiceSeleccionado += 2; seMovio = true; }
        }
        if (teclaListener.isArribaJustPressed()) {
            if (indiceSeleccionado >= 2) { indiceSeleccionado -= 2; seMovio = true; }
        }

        // Reproducir sonido solo si hubo movimiento
        if (seMovio) {
            GestorSonidos.getInstancia().reproducirSonido(SonidoDisponible.MENU);
        }

        if (teclaListener.isEnterJustPressed()) {
            GestorSonidos.getInstancia().reproducirSonido(SonidoDisponible.MENU);
            switch (indiceSeleccionado) {
                case 0: personajeFinal = TipoPersonaje.MICHAEL_JACKSON; break;
                case 1: personajeFinal = TipoPersonaje.BILLIE_EILISH; break;
                case 2: personajeFinal = TipoPersonaje.LEBRON_JAMES; break;
                case 3: personajeFinal = TipoPersonaje.FRIDA_KAHLO; break;
            }
        }
    }

    // ... (El resto del código dibujarTarjeta y dispose se mantiene igual) ...
    private void dibujarTarjeta(SpriteBatch batch, Texture imagen, TipoPersonaje tipo, float x, float y, float w, float h, Color colorBorde, boolean seleccionado, boolean alinearTextoDerecha) {
        // ... (Tu código de dibujo) ...
        float grosor = 6f;
        batch.setColor(seleccionado ? Color.WHITE : colorBorde);
        batch.draw(texturaCuadrado, x, y + h - grosor, w, grosor);
        batch.draw(texturaCuadrado, x, y, w, grosor);
        batch.draw(texturaCuadrado, x, y, grosor, h);
        batch.draw(texturaCuadrado, x + w - grosor, y, grosor, h);
        batch.setColor(Color.WHITE);
        batch.draw(imagen, x + grosor, y + grosor, w - (grosor * 2), h - (grosor * 2));

        if (seleccionado) {
            fontNombre.setColor(Color.ORANGE);
            float variacion = (float)Math.sin(tiempoAnimacion * 6) * 0.1f;
            fontNombre.getData().setScale(1.0f + variacion);
        } else {
            fontNombre.setColor(Color.WHITE);
            fontNombre.getData().setScale(1.0f);
        }

        String nombre = tipo.getNombre();
        layout.setText(fontNombre, nombre);
        float padding = 20f;
        float textoX = alinearTextoDerecha ? (x + w) - layout.width - padding : x + padding;
        float textoY = y + h - padding;
        fontNombre.draw(batch, layout, textoX, textoY);

        float statsY = y + 100;
        float lineHeight = 22f;
        float statsX = alinearTextoDerecha ? (x + w) - 120f : x + 20f;
        fontStats.draw(batch, "FUE: " + tipo.getFuerza(), statsX, statsY);
        fontStats.draw(batch, "DES: " + tipo.getDestreza(), statsX, statsY - lineHeight);
        fontStats.draw(batch, "DEF: " + tipo.getDefensa(), statsX, statsY - lineHeight * 2);
        fontStats.draw(batch, "VEL: " + tipo.getVelocidad(), statsX, statsY - lineHeight * 3);
    }

    public boolean hayPersonajeSeleccionado() { return personajeFinal != null; }
    public TipoPersonaje getPersonajeSeleccionado() { return personajeFinal; }
    public void dispose() {
        FONDO.dispose();
        fontNombre.dispose();
        fontStats.dispose();
        previewBillie.dispose();
        previewMichael.dispose();
        previewFrida.dispose();
        previewLebron.dispose();
        texturaCuadrado.dispose();
    }
}
