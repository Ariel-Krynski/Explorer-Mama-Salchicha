package juego.reglas;

// Es una interfaz que marca qué objetos dan una recompensa y obliga a que
// implementen otorgar() para devolver un valor numérico (int).
public interface Recompensa {
    int otorgar();
}
