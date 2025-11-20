package com.punchline.hitlist.interacciones;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class TeclaListener implements InputProcessor {
    private boolean arriba = false, abajo = false;
    private boolean espacio = false, enter = false;
    private boolean escape = false;

    private boolean escapeJustPressed = false;
    private boolean arribaJustPressed = false, abajoJustPressed = false;
    private boolean espacioJustPressed = false, enterJustPressed = false;

    @Override
    public boolean keyDown(int keycode) {

        if(keycode==Keys.W || keycode==Keys.UP) {
            this.arriba = true;
            this.arribaJustPressed = true;
        }

        if(keycode==Keys.S || keycode==Keys.DOWN) {
            this.abajo = true;
            this.abajoJustPressed = true;
        }

        if(keycode==Keys.SPACE) {
            this.espacio = true;
            this.espacioJustPressed = true;
        }

        if(keycode==Keys.ENTER) {
            this.enter = true;
            this.enterJustPressed = true;
        }

        if(keycode==Keys.ESCAPE){
            this.escape = true;
            this.escapeJustPressed = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if(keycode==Keys.ESCAPE){
            this.escape = false;
        }

        if(keycode==Keys.DOWN) {
            this.abajo = false;
        }

        if(keycode==Keys.W) {
            this.arriba = false;
        }

        return false;
    }

    public boolean isArriba() {
        return this.arriba;
    }

    public boolean isAbajo() {
        return this.abajo;
    }

    public boolean isEnter() {
        return this.enter;
    }

    public boolean isEspacio() {
        return this.espacio;
    }

    public boolean isEscape() {
        return escape;
    }

    public boolean isArribaJustPressed() {
        if (this.arribaJustPressed) {
            this.arribaJustPressed = false;
            return true;
        }
        return false;
    }

    public boolean isAbajoJustPressed() {
        if (this.abajoJustPressed) {
            this.abajoJustPressed = false;
            return true;
        }
        return false;
    }

    public boolean isEspacioJustPressed() {
        if (this.espacioJustPressed) {
            this.espacioJustPressed = false;
            return true;
        }
        return false;
    }

    public boolean isEnterJustPressed() {
        if (this.enterJustPressed) {
            this.enterJustPressed = false;
            return true;
        }
        return false;
    }

    public boolean isEscapeJustPressed() {
        return escapeJustPressed;
    }





    // Obligatorios
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
