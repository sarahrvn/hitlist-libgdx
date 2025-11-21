package com.punchline.hitlist.interacciones;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class TeclaListener implements InputProcessor {
    // Jugador 1 (WASD)
    private boolean w = false, a = false, s = false, d = false;
    private boolean wJustPressed = false; // Para el salto

    // Jugador 2 (Flechitas)
    private boolean up = false, down = false, left = false, right = false;
    private boolean upJustPressed = false; // Para el salto

    // Generales
    private boolean escape = false, escapeJustPressed = false;

    @Override
    public boolean keyDown(int keycode) {
        // P1
        if(keycode == Keys.W) { w = true; wJustPressed = true; }
        if(keycode == Keys.S) { s = true; }
        if(keycode == Keys.A) { a = true; }
        if(keycode == Keys.D) { d = true; }

        // P2
        if(keycode == Keys.UP) { up = true; upJustPressed = true; }
        if(keycode == Keys.DOWN) { down = true; }
        if(keycode == Keys.LEFT) { left = true; }
        if(keycode == Keys.RIGHT) { right = true; }

        if(keycode == Keys.ESCAPE) { escape = true; escapeJustPressed = true; }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // P1
        if(keycode == Keys.W) w = false;
        if(keycode == Keys.S) s = false;
        if(keycode == Keys.A) a = false;
        if(keycode == Keys.D) d = false;

        // P2
        if(keycode == Keys.UP) up = false;
        if(keycode == Keys.DOWN) down = false;
        if(keycode == Keys.LEFT) left = false;
        if(keycode == Keys.RIGHT) right = false;

        if(keycode == Keys.ESCAPE) escape = false;

        return true;
    }

    // Getters para Jugador 1
    public boolean isP1ArribaJustPressed() {
        if (wJustPressed) {
            wJustPressed = false;
            return true;
        }
        return false;
    }
    public boolean isP1Abajo() { return s; }
    public boolean isP1Izquierda() { return a; }
    public boolean isP1Derecha() { return d; }

    // Getters para Jugador 2
    public boolean isP2ArribaJustPressed() {
        if (upJustPressed) {
            upJustPressed = false;
            return true;
        }
        return false;
    }
    public boolean isP2Abajo() { return down; }
    public boolean isP2Izquierda() { return left; }
    public boolean isP2Derecha() { return right; }

    public boolean isEscapeJustPressed() {
        if (escapeJustPressed) {
            escapeJustPressed = false;
            return true;
        }
        return false;
    }

    // Métodos obligatorios vacíos...
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
}
