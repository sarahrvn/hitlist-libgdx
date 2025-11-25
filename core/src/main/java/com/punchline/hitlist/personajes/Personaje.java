package com.punchline.hitlist.personajes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.punchline.hitlist.elementosJuego.GestorSonidos;
import com.punchline.hitlist.elementosJuego.SonidoDisponible;
import com.punchline.hitlist.elementosJuego.Arma;

public class Personaje {
    private Sprite sprite;
    private final Rectangle boundingBox;
    private final TextureAtlas ATLAS;
    private final TipoPersonaje TIPO_PERSONAJE;

    // Estadísticas
    private Estadistica fuerza;
    private Estadistica destreza;
    private Estadistica defensa;
    private Estadistica velocidad;

    // Arma
    private Arma armaEquipada = null;

    // Movimiento
    private float velocidadY = 0;
    private float velocidadX = 0;

    // Variable modificada por stat
    private float velocidadCaminar;

    // Gravedad
    private final float GRAVEDAD = -1500;
    private final float VELOCIDAD_SALTO = 540;

    // Lógica de saltos
    private boolean enElSuelo = false;
    private int MAX_SALTOS = 2;
    private int saltosDisponibles = MAX_SALTOS;

    // Lógica de pared wall slide
    private boolean tocandoPared = false;
    private boolean intentandoMoverse = false;
    private final float VELOCIDAD_DESLIZAMIENTO = -80;

    // Animación
    private float tiempoAnimacionCorrer = 0f;
    private final float TIEMPO_CAMBIO_SPRITE = 0.2f;
    private boolean usandoCorrer1 = true;
    private float tiempoUltimoPaso = 0f;
    private final float INTERVALO_PASOS = 0.2f;

    // Combate
    private boolean atacando = false;
    private float tiempoAtaque = 0; // Cuánto tiempo lleva activo el hitbox
    private float tiempoCooldown = 0; // Tiempo de espera para volver a pegar
    private final float DURACION_GOLPE = 0.5f; // El golpe dura 0.5 segundos
    private final float COOLDOWN_GOLPE = 1f; // Espera 1 segundo entre golpes
    private int vidas = 3;
    private boolean estaMuerto = false;
    private float danioAcumulado = 0;

    // Hitbox del golpe (caja roja)
    private Rectangle hitboxAtaque;

    // Variable para debuguear (para ver la caja roja)
    private boolean mostrarHitbox = true;

    public Personaje(TipoPersonaje tipo) {
        this.TIPO_PERSONAJE = tipo;
        this.ATLAS = new TextureAtlas(tipo.getRutaSprite());
        this.sprite = ATLAS.createSprite("idle");

        this.fuerza = new Estadistica("Fuerza", tipo.getFuerza());
        this.destreza = new Estadistica("Destreza", tipo.getDestreza());
        this.defensa = new Estadistica("Defensa", tipo.getDefensa());
        this.velocidad = new Estadistica("Velocidad", tipo.getVelocidad());

        recalcularAtributos();
        boundingBox = new Rectangle(0, 0, sprite.getWidth(), sprite.getHeight());

        // Hitbox del ataque (tamaño de ejemplo: 40x40)
        hitboxAtaque = new Rectangle(0, 0, 40, 40);
    }

    public void update(float delta, Array<Rectangle> colisiones) {
        delta = Math.min(delta, 1/30f);

        // Sprite
        if (intentandoMoverse) {
            tiempoAnimacionCorrer += delta;
            tiempoUltimoPaso += delta;
            if (enElSuelo && tiempoUltimoPaso >= INTERVALO_PASOS) {
                tiempoUltimoPaso = 0f;
            }
            if (tiempoAnimacionCorrer >= TIEMPO_CAMBIO_SPRITE) {
                tiempoAnimacionCorrer = 0f;
                usandoCorrer1 = !usandoCorrer1;
                actualizarSprite();
            }
        } else {
            actualizarSprite();
        }

        boundingBox.x += velocidadX * delta;
        checkColisionX(colisiones);

        // Lógica wall slide y gravedad normal
        boolean agarradoPared = !enElSuelo && tocandoPared && velocidadY < 0 && intentandoMoverse;
        if (agarradoPared) {
            velocidadY = VELOCIDAD_DESLIZAMIENTO;
            saltosDisponibles = MAX_SALTOS;
        } else {
            velocidadY += GRAVEDAD * delta;
        }

        boundingBox.y += velocidadY * delta;
        checkColisionY(colisiones);

        // Sonido al caer
        if (!enElSuelo && velocidadY <= 0) {
            GestorSonidos.getInstancia().reproducirSonido(SonidoDisponible.CAIDA, 0.4f);
        }

        sprite.setPosition(boundingBox.x, boundingBox.y);
        velocidadX = 0;
        intentandoMoverse = false;

        updateCombate(delta);
    }

    public void dibujar(SpriteBatch batch) { sprite.draw(batch); }

    // ---- MOVIMIENTOS ---
    public void saltar() {
        if (saltosDisponibles > 0) {
            velocidadY = VELOCIDAD_SALTO;
            saltosDisponibles--;
            enElSuelo = false;
            GestorSonidos.getInstancia().reproducirSonido(SonidoDisponible.SALTO, 0.5f);
        }
    }
    public void caminarIzquierda() {
        velocidadX = -velocidadCaminar;
        intentandoMoverse = true;
        actualizarSprite();
    }
    public void caminarDerecha() {
        velocidadX = velocidadCaminar;
        intentandoMoverse = true;
        actualizarSprite();
    }

    private void checkColisionX(Array<Rectangle> colisiones) {
        tocandoPared = false;
        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                if (velocidadX > 0) {
                    boundingBox.x = colision.x - boundingBox.width;
                    tocandoPared = true;
                } else if (velocidadX < 0) {
                    boundingBox.x = colision.x + colision.width;
                    tocandoPared = true;
                }
                break;
            }
        }
    }
    private void checkColisionY(Array<Rectangle> colisiones) {
        enElSuelo = false;
        for (Rectangle colision : colisiones) {
            if (boundingBox.overlaps(colision)) {
                if (velocidadY < 0) {
                    boundingBox.y = colision.y + colision.height;
                    enElSuelo = true;
                    saltosDisponibles = MAX_SALTOS;
                    velocidadY = 0;
                } else if (velocidadY > 0) {
                    boundingBox.y = colision.y - boundingBox.height;
                    velocidadY = 0;
                }
                break;
            }
        }
    }

    // ---- COMBATE ----
    public void equiparArma() {
        this.armaEquipada = this.TIPO_PERSONAJE.getArmaAsignada();
        recalcularAtributos();
        actualizarSprite();
    }

    public void atacar() {
        // Solo ataca si no está ya atacando y si el cooldown terminó
        if (!atacando && tiempoCooldown <= 0) {
            atacando = true;
            tiempoAtaque = 0; // Reinicia cronómetro del golpe

            posicionarHitbox();
            actualizarSprite();
        }
    }

    private void updateCombate(float delta) {
        if (tiempoCooldown > 0) {
            tiempoCooldown -= delta;
        }

        if (atacando) {
            tiempoAtaque += delta;

            posicionarHitbox();

            // Si pasó el tiempo del golpe deja de atacar
            if (tiempoAtaque >= DURACION_GOLPE) {
                atacando = false;
                tiempoCooldown = COOLDOWN_GOLPE;
                actualizarSprite();
            }
        }
    }

    private void posicionarHitbox() {
        // Inicializar el hitbox y moverlo junto con el personaje por si se mueve mientras pega
        if (!sprite.isFlipX())
            hitboxAtaque.setPosition(boundingBox.x + boundingBox.width, boundingBox.y + 20);
        else
            hitboxAtaque.setPosition(boundingBox.x - hitboxAtaque.getWidth(), boundingBox.y + 20);
    }

    public void recibirGolpe(int fuerzaAtacante, int direccionEmpuje) {
        // Calcular danio
        float reduccionDefensa = this.defensa.getValor() * 0.5f;
        float danioRecibido = (10 + fuerzaAtacante * 2) - reduccionDefensa;
        if (danioRecibido < 1) danioRecibido = 1; // Mínimo 1 de daño

        this.danioAcumulado += danioRecibido;

        // Calcular empuje
        // Si tiene 0 danio, factor es 1. Si tiene 100 danio, factor es 3.
        float factorVuelo = 1f + (this.danioAcumulado / 40f);

        // Base del empuje depende de la fuerza del enemigo
        float empujeBaseX = 300f + (fuerzaAtacante * 40f);
        float empujeBaseY = 200f + (fuerzaAtacante * 20f);

        // Aplicamos la fórmula: Base * FactorVuelo
        this.velocidadX = empujeBaseX * factorVuelo * direccionEmpuje;
        this.velocidadY = empujeBaseY * factorVuelo; // Siempre un poco hacia arriba

        GestorSonidos.getInstancia().reproducirSonido(SonidoDisponible.CAIDA, 1f);
        // Estado
        this.enElSuelo = false;
        this.intentandoMoverse = false; // "Mareo" momentáneo (se queda quieto un toque)
    }

    public void sacarVida() {
        if (this.vidas > 0) {
            this.vidas--;
        }
        if (vidas <= 0) {
            this.estaMuerto = true;
        }
    }

    public boolean estaMuerto() {
        return this.estaMuerto;
    }

    private void recalcularAtributos() {
        int fBase = this.TIPO_PERSONAJE.getFuerza();
        int dBase = this.TIPO_PERSONAJE.getDefensa();
        int vBase = this.TIPO_PERSONAJE.getVelocidad();

        int modF = 0, modD = 0, modV = 0;

        if (armaEquipada != null) {
            modF = this.armaEquipada.getModFuerza();
            modD = this.armaEquipada.getModDefensa();
            modV = this.armaEquipada.getModVelocidad();
        }

        this.fuerza.setEstadistica(fBase + modF);
        this.defensa.setEstadistica(dBase + modD);
        this.velocidad.setEstadistica(vBase + modV);

        this.velocidadCaminar = 100f + (this.velocidad.getValor() * 30f);
    }

    private void actualizarSprite() {
        String nombreRegion;

        if (atacando) {
            nombreRegion = "golpe1";
        } else if (intentandoMoverse) {
            if (usandoCorrer1) {
                nombreRegion = "correr1";
            } else {
                nombreRegion = "correr2";
            }
        } else {
            nombreRegion = "idle";
        }

        if(isArmaEquipada()) {
            nombreRegion = nombreRegion + "_arma";
        }

        this.sprite = ATLAS.createSprite(nombreRegion);
        if(velocidadX < 0){
            if (!sprite.isFlipX()) {
                sprite.flip(true, false);
            }
        } else if (velocidadX > 0) {
            if (sprite.isFlipX()) {
                sprite.flip(true, false);
            }
        }

        sprite.setPosition(boundingBox.x, boundingBox.y);
    }

    // Resetear valores pj
    public void resetear() {
        this.velocidadX = 0;
        this.velocidadY = 0;
        this.enElSuelo = false;
        this.saltosDisponibles = MAX_SALTOS;
        this.intentandoMoverse = false;
        this.danioAcumulado = 0;

        // Le saca el arma
        this.armaEquipada = null;

        // Resetea stats a base
        recalcularAtributos();

        actualizarSprite();
    }

    public Arma getArmaAsignada() {
        return this.TIPO_PERSONAJE.getArmaAsignada();
    }
    public boolean isArmaEquipada() {
        if (this.armaEquipada != null) {
            return true;
        }
        return false;
    }



    public boolean isAtacando() { return this.atacando;}

    public Rectangle getHitbox() { return this.boundingBox; }
    public Rectangle getHitboxAtaque() { return this.hitboxAtaque; }

    public int getVidas() { return this.vidas;}

    public Estadistica getFuerza() { return this.fuerza; }
    public float getDanioAcumulado() {
        return danioAcumulado;
    }

    public void setPosition(float x, float y) {
        boundingBox.setPosition(x - boundingBox.width / 2f, y - boundingBox.height / 2f);
        sprite.setPosition(boundingBox.x, boundingBox.y);
    }

    public void dispose() { ATLAS.dispose(); }
}
