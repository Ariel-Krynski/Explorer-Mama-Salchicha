package juego.reglas;

// La gema es un objeto del juego que no se puede heredar (final), cuenta como
// objeto interactivo y también da recompensa. Al crearse se llama "Gema", y
// tanto activar() como otorgar() devuelven 100.
public final class Gema extends ObjetoInteractivo implements Recompensa {
    public Gema() {
        super("Gema");
    }

    public int activar() {
        return otorgar();
    }

    public int otorgar() {
        return 100;
    }
}
