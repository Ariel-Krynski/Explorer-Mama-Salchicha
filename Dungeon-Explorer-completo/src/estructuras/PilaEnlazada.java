package estructuras;

public class PilaEnlazada<T> implements Pila<T> {
    // Definicion del nodo
    private static class Nodo<E> {
        E d;
        Nodo<E> s;
// constructor del nodo
        Nodo(E x, Nodo<E> n) {
            d = x;
            s = n;
        }
    }

    private Nodo<T> tope; // referencia al nodo
    private int n; // cantidad de elementos

   // Agrega un elemento en el tope
    public void apilar(T x) {
        tope = new Nodo<T>(x, tope); // nodo que apunta al siguiente
        n++; // incrementa
    }

    // Quita y retorna el elemento del tope
    public T desapilar() {
        if (tope == null) throw new IllegalStateException("pila vacía");
        T x = tope.d;
        tope = tope.s;
        n--;
        return x;
    }
    // Consulta el elemento del tope sin quitarlo
    public T tope() {
        if (tope == null) throw new IllegalStateException("pila vacía");
        return tope.d;
    }

    public boolean estaVacia() {
        return n == 0;
    }

    public int tamanio() {
        return n;
    }
    // elimina todos los elementos
    public void vaciar() {
        tope = null;
        n = 0;
    }
}
