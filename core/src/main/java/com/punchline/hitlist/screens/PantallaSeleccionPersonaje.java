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
import com.badlogic.gdx.math.Rectangle;
import com.punchline.hitlist.interacciones.TeclaListener;
import com.punchline.hitlist.personajes.TipoPersonaje;

public class PantallaSeleccionPersonaje {

    private final Texture FONDO;

    // Fuentes generadas
    private BitmapFont fontNombre;
    private BitmapFont fontStats;

    // Textura blanca simple para bordes
    private final Texture texturaCuadrado;

    private final Texture previewBillie;
    private final Texture previewMichael;
    private final Texture previewFrida;
    private final Texture previewLebron;

    // Lógica de Selección (Grid 2x2)
    // 0: Michael (Arriba-Izq), 1: Billie (Arriba-Der)
    // 2: Lebron (Abajo-Izq),   3: Frida (Abajo-Der)
    private int indiceSeleccionado = 0;

    private final TeclaListener teclaListener;
    private TipoPersonaje personajeFinal = null;

    // Para medir ancho del texto y animaciones
    private final GlyphLayout layout = new GlyphLayout();
    private float tiempoAnimacion = 0;

    public PantallaSeleccionPersonaje() {
        FONDO = new Texture("fondos/Fondo_Seleccion_Personaje.png");

        // Input
        teclaListener = new TeclaListener();
        Gdx.input.setInputProcessor(teclaListener);

        // --- GENERAR FUENTES ---
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/ari-w9500.ttf"));

            // 1. Fuente para Nombres
            FreeTypeFontGenerator.FreeTypeFontParameter paramNombre = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramNombre.size = 36;
            paramNombre.borderWidth = 2;
            paramNombre.borderColor = Color.BLACK;
            paramNombre.shadowOffsetX = 3;
            paramNombre.shadowOffsetY = 3;
            fontNombre = generator.generateFont(paramNombre);

            // 2. Fuente para Stats
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

        // --- CREAR TEXTURA GENERICA PARA BORDES ---
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.WHITE);
        pix.fill();
        texturaCuadrado = new Texture(pix);
        pix.dispose();

        // Cargar imagenes
        previewBillie = new Texture("fondos/Billie_Menu.png");
        previewMichael = new Texture("fondos/Michael_Menu.png");
        previewFrida = new Texture("fondos/Frida_Menu.png");
        previewLebron = new Texture("fondos/Lebron_Menu.png");
    }

    public void render(SpriteBatch batch, OrthographicCamera camara) {
        // --- LOGICA DE INPUT ---
        manejarInput();

        // Actualizar tiempo animación
        tiempoAnimacion += Gdx.graphics.getDeltaTime();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        // 1. Fondo Ciudad
        batch.draw(FONDO, 0, 0, camara.viewportWidth, camara.viewportHeight);

        // --- CALCULOS DE POSICION ---
        float mitadAncho = camara.viewportWidth / 2f;
        float mitadAlto = camara.viewportHeight / 2f;
        float margen = 15f;

        float anchoCard = mitadAncho - (margen * 2);
        float altoCard = mitadAlto - (margen * 2);

        float xIzq = margen;
        float xDer = mitadAncho + margen;
        float yAbajo = margen;
        float yArriba = mitadAlto + margen;

        // --- DIBUJAR TARJETAS ---

        // 0. MICHAEL (Arriba Izquierda) -> Alineación Texto y Stats: DERECHA
        dibujarTarjeta(batch, previewMichael, TipoPersonaje.MICHAEL_JACKSON,
            xIzq, yArriba, anchoCard, altoCard, Color.GOLD,
            (indiceSeleccionado == 0), true);

        // 1. BILLIE (Arriba Derecha) -> Alineación Texto y Stats: IZQUIERDA
        dibujarTarjeta(batch, previewBillie, TipoPersonaje.BILLIE_EILISH,
            xDer, yArriba, anchoCard, altoCard, Color.GOLD,
            (indiceSeleccionado == 1), false);

        // 2. LEBRON (Abajo Izquierda) -> Alineación Texto y Stats: DERECHA
        dibujarTarjeta(batch, previewLebron, TipoPersonaje.LEBRON_JAMES,
            xIzq, yAbajo, anchoCard, altoCard, Color.GOLD,
            (indiceSeleccionado == 2), true);

        // 3. FRIDA (Abajo Derecha) -> Alineación Texto y Stats: IZQUIERDA
        dibujarTarjeta(batch, previewFrida, TipoPersonaje.FRIDA_KAHLO,
            xDer, yAbajo, anchoCard, altoCard, Color.GOLD,
            (indiceSeleccionado == 3), false);

        batch.end();
    }

    private void manejarInput() {
        if (personajeFinal != null) return;

        if (teclaListener.isDerechaJustPressed()) {
            if (indiceSeleccionado % 2 == 0) indiceSeleccionado++;
        }
        if (teclaListener.isIzquierdaJustPressed()) {
            if (indiceSeleccionado % 2 != 0) indiceSeleccionado--;
        }
        if (teclaListener.isAbajoJustPressed()) {
            if (indiceSeleccionado < 2) indiceSeleccionado += 2;
        }
        if (teclaListener.isArribaJustPressed()) {
            if (indiceSeleccionado >= 2) indiceSeleccionado -= 2;
        }

        if (teclaListener.isEnterJustPressed()) {
            switch (indiceSeleccionado) {
                case 0: personajeFinal = TipoPersonaje.MICHAEL_JACKSON; break;
                case 1: personajeFinal = TipoPersonaje.BILLIE_EILISH; break;
                case 2: personajeFinal = TipoPersonaje.LEBRON_JAMES; break;
                case 3: personajeFinal = TipoPersonaje.FRIDA_KAHLO; break;
            }
        }
    }

    private void dibujarTarjeta(SpriteBatch batch, Texture imagen, TipoPersonaje tipo,
                                float x, float y, float w, float h, Color colorBorde,
                                boolean seleccionado, boolean alinearTextoDerecha) {

        float grosor = 6f;

        // Borde seleccionado o normal
        batch.setColor(seleccionado ? Color.WHITE : colorBorde);

        // --- MARCO HUECO ---
        batch.draw(texturaCuadrado, x, y + h - grosor, w, grosor); // Arriba
        batch.draw(texturaCuadrado, x, y, w, grosor);              // Abajo
        batch.draw(texturaCuadrado, x, y, grosor, h);              // Izquierda
        batch.draw(texturaCuadrado, x + w - grosor, y, grosor, h); // Derecha

        batch.setColor(Color.WHITE);

        // --- IMAGEN ---
        batch.draw(imagen, x + grosor, y + grosor, w - (grosor * 2), h - (grosor * 2));

        // --- NOMBRE ---
        if (seleccionado) {
            fontNombre.setColor(Color.ORANGE);
            float escalaBase = 1.0f;
            float variacion = (float)Math.sin(tiempoAnimacion * 6) * 0.1f;
            fontNombre.getData().setScale(escalaBase + variacion);
        } else {
            fontNombre.setColor(Color.WHITE);
            fontNombre.getData().setScale(1.0f);
        }

        String nombre = tipo.getNombre();
        layout.setText(fontNombre, nombre);

        float padding = 20f;
        float textoX;

        if (alinearTextoDerecha) {
            textoX = (x + w) - layout.width - padding;
        } else {
            textoX = x + padding;
        }

        float textoY = y + h - padding;
        fontNombre.draw(batch, layout, textoX, textoY);


        // --- STATS (Modificado para cambiar de lado) ---
        float statsY = y + 100;
        float lineHeight = 22f;
        float statsX;

        if (alinearTextoDerecha) {
            // Calculamos desde la derecha hacia adentro.
            // 120f es un ancho aproximado seguro para que entre "FUE: 10"
            statsX = (x + w) - 120f;
        } else {
            // Margen izquierdo normal
            statsX = x + 20f;
        }

        fontStats.draw(batch, "FUE: " + tipo.getFuerza(), statsX, statsY);
        fontStats.draw(batch, "DES: " + tipo.getDestreza(), statsX, statsY - lineHeight);
        fontStats.draw(batch, "DEF: " + tipo.getDefensa(), statsX, statsY - lineHeight * 2);
        fontStats.draw(batch, "VEL: " + tipo.getVelocidad(), statsX, statsY - lineHeight * 3);
    }

    public boolean hayPersonajeSeleccionado() {
        return personajeFinal != null;
    }

    public TipoPersonaje getPersonajeSeleccionado() {
        return personajeFinal;
    }

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
