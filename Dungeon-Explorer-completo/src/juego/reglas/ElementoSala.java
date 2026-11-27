package juego.reglas;

// Representa una sala del juego (no se puede heredar) y guarda un id y un nombre.
// Al crearse controla que el id no sea negativo y que el nombre no esté vacío;
// también permite leer esos datos y considera que dos salas son iguales si tienen
// el mismo id.
public final class ElementoSala {
    private final int id;
    private final String nombre;

    public ElementoSala(int id, String nombre) {
        if (id < 0 || nombre == null || nombre.trim().length() == 0) {
            throw new IllegalArgumentException("sala inválida");
        }
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ElementoSala && ((ElementoSala) o).id == id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
