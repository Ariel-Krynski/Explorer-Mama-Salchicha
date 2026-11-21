package estructuras;

public class ColaEnlazada<T> implements Cola<T> {
    private static class Nodo<E> {
        E d;
        Nodo<E> s;

        Nodo(E x) {
            d = x;
        }
    }

    private Nodo<T> frente, fin;
    private int n;

    public void encolar(T x) {
        Nodo<T> a = new Nodo<T>(x);
        if (fin == null) frente = fin = a;
        else {
            fin.s = a;
            fin = a;
        }
        n++;
    }

    public T desencolar() {
        if (frente == null) throw new IllegalStateException("cola vacía");
        T x = frente.d;
        frente = frente.s;
        if (frente == null) fin = null;
        n--;
        return x;
    }

    public T frente() {
        if (frente == null) throw new IllegalStateException("cola vacía");
        return frente.d;
    }

    public boolean estaVacia() {
        return n == 0;
    }

    public int tamanio() {
        return n;
    }

    public void vaciar() {
        frente = fin = null;
        n = 0;
    }
}
