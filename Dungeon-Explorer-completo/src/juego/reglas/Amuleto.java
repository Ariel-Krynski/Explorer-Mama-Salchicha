package juego.reglas;

// El amuleto es un objeto del juego que no se puede heredar (final),
// cuenta como objeto interactivo y también da recompensa. Al crearse
// se llama "Amuleto", y tanto activar() como otorgar() devuelven 125.
public final class Amuleto extends ObjetoInteractivo implements Recompensa {
    public Amuleto() {
        super("Amuleto");
    }

    public int activar() {
        return otorgar();
    }

    public int otorgar() {
        return 125;
    }
}
