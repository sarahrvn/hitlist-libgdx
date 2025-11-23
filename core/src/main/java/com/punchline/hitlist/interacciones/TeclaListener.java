package com.punchline.hitlist.interacciones;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;

public class TeclaListener implements InputProcessor {
    // ... (Variables de movimiento P1 y P2 quedan igual) ...
    private boolean w = false, a = false, s = false, d = false;
    private boolean wJustPressed = false;
    private boolean up = false, down = false, left = false, right = false;
    private boolean upJustPressed = false;
    private boolean escapeJustPressed = false;
    private boolean enterJustPressed = false; // Agregado para menú

    // Variable para la acción de AGARRAR
    private boolean accionAgarrarJustPressed = false;

    @Override
    public boolean keyDown(int keycode) {
        // ... (Movimiento igual) ...
        if(keycode == Keys.W) { w = true; wJustPressed = true; }
        if(keycode == Keys.S) { s = true; }
        if(keycode == Keys.A) { a = true; }
        if(keycode == Keys.D) { d = true; }

        if(keycode == Keys.UP) { up = true; upJustPressed = true; }
        if(keycode == Keys.DOWN) { down = true; }
        if(keycode == Keys.LEFT) { left = true; }
        if(keycode == Keys.RIGHT) { right = true; }

        if(keycode == Keys.ESCAPE) { escapeJustPressed = true; }
        if(keycode == Keys.ENTER) { enterJustPressed = true; }

        // NUEVO: TECLA E PARA AGARRAR
        if(keycode == Keys.E) {
            accionAgarrarJustPressed = true;
        }

        return true;
    }

    // NUEVO: TAMBIÉN LA RUEDA DEL RATÓN
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.MIDDLE) {
            accionAgarrarJustPressed = true;
        }
        return true;
    }

    // Getter Unificado: Devuelve true si apretó la E o la Rueda
    public boolean isAccionAgarrar() {
        if (accionAgarrarJustPressed) {
            accionAgarrarJustPressed = false;
            return true;
        }
        return false;
    }

    // ... (Resto de getters y métodos keyUp, etc. quedan igual) ...
    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Keys.W) w = false;
        if(keycode == Keys.S) s = false;
        if(keycode == Keys.A) a = false;
        if(keycode == Keys.D) d = false;
        if(keycode == Keys.UP) up = false;
        if(keycode == Keys.DOWN) down = false;
        if(keycode == Keys.LEFT) left = false;
        if(keycode == Keys.RIGHT) right = false;
        return true;
    }

    // Getters de movimiento
    public boolean isP1ArribaJustPressed() { if (wJustPressed) { wJustPressed=false; return true;} return false; }
    public boolean isP1Izquierda() { return a; }
    public boolean isP1Derecha() { return d; }
    public boolean isEscapeJustPressed() { if (escapeJustPressed) { escapeJustPressed=false; return true;} return false; }
    public boolean isEnterJustPressed() { if (enterJustPressed) { enterJustPressed=false; return true;} return false; }

    // Agrega estos para el menú si faltaban
    public boolean isArribaJustPressed() { return isP1ArribaJustPressed() || (upJustPressed ? !(upJustPressed=false) : false); } // Simplificado
    public boolean isAbajoJustPressed() { return s || down; } // Simplificado para menú

    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
}
