package com.punchline.hitlist.elementosJuego;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class MundoFisico {
    private World mundo;
    private Box2DDebugRenderer debugRenderer;

    public MundoFisico() {
        // Crear el mundo físico con gravedad
        mundo = new World(new Vector2(0, -9.8f), true);  // Gravedad hacia abajo
        debugRenderer = new Box2DDebugRenderer();
    }

    public void actualizar() {
        mundo.step(1 / 60f, 6, 2); // Actualiza el mundo a 60 FPS
    }

    public void renderDebug(OrthographicCamera camara) {
        debugRenderer.render(mundo, camara.combined);
    }

    public World getMundo() {
        return mundo;  // Obtener el mundo de Box2D para usarlo en otros lugares
    }
}
