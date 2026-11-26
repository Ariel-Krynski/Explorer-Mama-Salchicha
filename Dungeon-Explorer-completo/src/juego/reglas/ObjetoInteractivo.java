package juego.reglas;

// Es la clase base (abstracta) para todos los objetos interactivos del juego:
// guarda su nombre (controlando que no esté vacío al crearse), permite leerlo
// con getNombre() y obliga a que cada objeto hijo defina cómo funciona activar().
public abstract class ObjetoInteractivo {
    private final String nombre;

    protected ObjetoInteractivo(String n) {
        if (n == null || n.trim().length() == 0) {
            throw new IllegalArgumentException("nombre vacío");
        }
        nombre = n;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract int activar();
}
