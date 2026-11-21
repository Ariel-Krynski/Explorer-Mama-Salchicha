package estructuras;

public class PilaEnlazada<T> implements Pila<T> {
    private static class Nodo<E> {
        E d;
        Nodo<E> s;

        Nodo(E x, Nodo<E> n) {
            d = x;
            s = n;
        }
    }

    private Nodo<T> tope;
    private int n;

    public void apilar(T x) {
        tope = new Nodo<T>(x, tope);
        n++;
    }

    public T desapilar() {
        if (tope == null) throw new IllegalStateException("pila vacía");
        T x = tope.d;
        tope = tope.s;
        n--;
        return x;
    }

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

    public void vaciar() {
        tope = null;
        n = 0;
    }
}
