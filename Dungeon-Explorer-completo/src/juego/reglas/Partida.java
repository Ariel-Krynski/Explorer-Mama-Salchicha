package juego.reglas;

// Guarda y maneja el estado principal de una partida (6 habitaciones, habitación actual,
// cristales, puntaje, vidas, si se ganó o perdió, y qué cofres o enemigos ya se usaron
// usando bits). Permite reiniciar, moverse por las habitaciones, abrir cofres
// (dando Amuleto en el id 2 o Gema en el resto), sumar puntos por enemigos o ganar,
// restar vidas y guardar o leer el récord mediante ServiciosMazmorra.
public class Partida {
    public static final int HABITACIONES = 6;
    private int habitacion;
    private int cristales;
    private int puntaje;
    private int vidas;
    private int cofresAbiertos;
    private int enemigosDerrotados;
    private boolean ganada;

    public Partida() {
        reiniciar();
    }

    public void reiniciar() {
        habitacion = 0;
        cristales = 0;
        puntaje = 0;
        vidas = 3;
        ganada = false;
        cofresAbiertos = 0;
        enemigosDerrotados = 0;
    }

    public int getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(int habitacion) {
        if (habitacion < 0 || habitacion >= HABITACIONES) {
            throw new ErrorPartida("Habitación fuera de la mazmorra");
        }
        this.habitacion = habitacion;
    }

    public int getCristales() {
        return cristales;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getVidas() {
        return vidas;
    }

    public boolean tieneCofreAbierto(int id) {
        return (cofresAbiertos & (1 << id)) != 0;
    }

    public boolean enemigoDerrotado(int id) {
        return (enemigosDerrotados & (1 << id)) != 0;
    }

    public void abrirCofre(int id) {
        if (id < 0 || id >= HABITACIONES) {
            throw new IllegalArgumentException("cofre inválido");
        }
        if (!tieneCofreAbierto(id)) {
            cofresAbiertos |= (1 << id);
            cristales++;
            ObjetoInteractivo recompensa = (id == 2) ? new Amuleto() : new Gema();
            puntaje += recompensa.activar();
        }
    }

    public void derrotarEnemigo(int id) {
        if (id < 0 || id >= HABITACIONES) {
            throw new IllegalArgumentException("enemigo inválido");
        }
        if (!enemigoDerrotado(id)) {
            enemigosDerrotados |= (1 << id);
            puntaje += 150;
        }
    }

    public void recibirDanio() {
        vidas--;
    }

    public boolean estaGanada() {
        return ganada;
    }

    public void ganar() {
        ganada = true;
        puntaje += 300;
    }

    public boolean estaPerdida() {
        return vidas <= 0;
    }

    public void guardarRecord() {
        ServiciosMazmorra.obtener().guardarPuntaje(puntaje);
        ServiciosMazmorra.obtener().guardar();
    }

    public static int leerRecord() {
        return ServiciosMazmorra.obtener().record();
    }
}
