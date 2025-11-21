package com.punchline.hitlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator; // Necesario para fuentes HD
import com.punchline.hitlist.interacciones.TeclaListener; // Tu listener

public class PantallaMenu {

    private final Texture FONDO;
    private final Texture TITULO;
    private BitmapFont font; // Quitamos el final para poder generarla

    // Lógica de selección
    private int opcionSeleccionada = 0;
    private final String[] opciones = {"Jugar", "Salir"};

    // Input
    private final TeclaListener teclaListener;

    // Variables de estado
    private boolean quiereJugar = false;
    private boolean quiereSalir = false;

    // Layout para medir texto
    private final GlyphLayout layout = new GlyphLayout();

    // Variables para la animación
    private float tiempoAnimacion = 0;

    public PantallaMenu() {
        FONDO = new Texture("fondos/Fondo_Menu.png");
        TITULO = new Texture("logos/Hitlist_Titulo.png");

        // Inicializamos tu listener
        teclaListener = new TeclaListener();
        // ¡IMPORTANTE! Decirle a LibGDX que use este listener ahora
        Gdx.input.setInputProcessor(teclaListener);

        // --- GENERACIÓN DE FUENTE HD ---
        // Esto soluciona el problema de la "pésima calidad".
        // Debes tener un archivo .ttf en tu carpeta assets (ej: assets/fuentes/arial.ttf)
        // Si no tienes uno, descarga cualquier fuente gratuita de Google Fonts.
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/tu_fuente.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 40; // Tamaño real en píxeles (HD)
            parameter.borderWidth = 2;
            parameter.borderColor = Color.BLACK;
            parameter.shadowOffsetX = 3;
            parameter.shadowOffsetY = 3;

            font = generator.generateFont(parameter);
            generator.dispose(); // Importante limpiar el generador
        } catch (Exception e) {
            // Fallback por si no tienes el archivo .ttf aún
            System.out.println("No se encontró fuente .ttf, usando default (baja calidad)");
            font = new BitmapFont();
            font.getData().setScale(3f); // Esto es lo que causaba la pixelación
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camara) {
        // 1. Input
        manejarInput();

        // 2. Actualizar tiempo para la animación
        tiempoAnimacion += Gdx.graphics.getDeltaTime();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        // Fondo
        batch.draw(FONDO, 0, 0, camara.viewportWidth, camara.viewportHeight);

        // Título
        float tituloAncho = camara.viewportWidth / 5f;
        float tituloAlto = tituloAncho * ((float) TITULO.getHeight() / TITULO.getWidth());
        float tituloX = (camara.viewportWidth - tituloAncho) / 2f;
        float tituloY = camara.viewportHeight - tituloAlto - 50f;
        batch.draw(TITULO, tituloX, tituloY, tituloAncho, tituloAlto);


        // --- OPCIONES CON ANIMACIÓN ---
        float centroXPantalla = camara.viewportWidth / 2f;
        float yBase = tituloY - 100f;

        for (int i = 0; i < opciones.length; i++) {
            String textoADibujar = opciones[i];

            if (i == opcionSeleccionada) {
                font.setColor(Color.ORANGE);
                textoADibujar = "<< " + textoADibujar + " >>";

                // ANIMACIÓN:
                // Variamos la escala entre 1.0 y 1.2 (si usas FreeType)
                // Si usas la fuente default pixelada, cambia el 1.0f base por 3.0f
                float escalaBase = 1.0f;
                float variacion = (float)Math.sin(tiempoAnimacion * 6) * 0.1f; // Velocidad 6, Intensidad 0.1

                font.getData().setScale(escalaBase + variacion);

            } else {
                font.setColor(Color.WHITE);
                // Resetear escala a normal
                font.getData().setScale(1.0f);
            }

            // IMPORTANTE: Medir el texto DESPUÉS de aplicar el setScale
            // Si no, el centrado quedaría desfasado cuando el texto crece
            layout.setText(font, textoADibujar);

            float textoX = centroXPantalla - (layout.width / 2f);
            float textoY = yBase - (i * 100f); // Separación vertical

            font.draw(batch, layout, textoX, textoY);
        }

        batch.end();
    }

    private void manejarInput() {
        // Moverse arriba (W o Flecha arriba)
        if (teclaListener.isArribaJustPressed()) {
            opcionSeleccionada--;
            if (opcionSeleccionada < 0) opcionSeleccionada = opciones.length - 1;
        }

        // Moverse abajo (S o Flecha abajo)
        if (teclaListener.isAbajoJustPressed()) {
            opcionSeleccionada++;
            if (opcionSeleccionada >= opciones.length) opcionSeleccionada = 0;
        }

        // Seleccionar (Enter)
        if (teclaListener.isEnterJustPressed()) {
            if (opcionSeleccionada == 0) {
                quiereJugar = true;
            } else if (opcionSeleccionada == 1) {
                quiereSalir = true;
            }
        }
    }

    public boolean quiereJugar() { return quiereJugar; }
    public boolean quiereSalir() { return quiereSalir; }

    public void dispose() {
        FONDO.dispose();
        TITULO.dispose();
        font.dispose();
    }
}
