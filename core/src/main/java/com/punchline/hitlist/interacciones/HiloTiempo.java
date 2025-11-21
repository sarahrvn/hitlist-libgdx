package com.punchline.hitlist.utils;

import com.punchline.hitlist.screens.PantallaJuego;

public class HiloTiempo extends Thread {

    private PantallaJuego pantallaJuego;
    private boolean ejecutando = true;

    public HiloTiempo(PantallaJuego pantallaJuego) {
        this.pantallaJuego = pantallaJuego;
    }

    @Override
    public void run() {
        while (ejecutando) {
            try {
                // Esperar 1 segundo (1000 milisegundos)
                Thread.sleep(1000);

                // Llamar al método de la pantalla para restar el tiempo
                if (ejecutando) {
                    pantallaJuego.procesarSegundo();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void terminar() {
        this.ejecutando = false;
    }
}
