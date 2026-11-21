package arboles;

import estructuras.Lista;
import estructuras.ListaEnlazada;
import estructuras.Pila;
import estructuras.PilaEnlazada;

/**
 * Árbol binario de búsqueda; los repetidos se ignoran.
 */
public class ABB<T extends Comparable<T>> {
    private static class Nodo<E> {
        E d;
        Nodo<E> i, f;

        Nodo(E x) {
            d = x;
        }
    }

    private Nodo<T> raiz;
    private int n;

    public void insertar(T x) {
        if (x == null) throw new IllegalArgumentException("clave nula");
        raiz = insertar(raiz, x);
    }

    private Nodo<T> insertar(Nodo<T> a, T x) {
        if (a == null) {
            n++;
            return new Nodo<T>(x);
        }
        int c = x.compareTo(a.d);
        if (c < 0) a.i = insertar(a.i, x);
        else if (c > 0) a.f = insertar(a.f, x);
        return a;
    }

    public boolean buscar(T x) {
        return obtener(x) != null;
    }

    public T obtener(T x) {
        if (x == null) throw new IllegalArgumentException("clave nula");
        Nodo<T> a = raiz;
        while (a != null) {
            int c = x.compareTo(a.d);
            if (c == 0) return a.d;
            a = c < 0 ? a.i : a.f;
        }
        return null;
    }

    public boolean eliminar(T x) {
        if (x == null) throw new IllegalArgumentException("clave nula");
        int antes = n;
        raiz = eliminar(raiz, x);
        return n < antes;
    }

    private Nodo<T> eliminar(Nodo<T> a, T x) {
        if (a == null) return null;
        int c = x.compareTo(a.d);
        if (c < 0) a.i = eliminar(a.i, x);
        else if (c > 0) a.f = eliminar(a.f, x);
        else {
            n--;
            if (a.i == null) return a.f;
            if (a.f == null) return a.i;
            Nodo<T> s = a.f;
            while (s.i != null) s = s.i;
            a.d = s.d;
            n++;
            a.f = eliminar(a.f, s.d);
        }
        return a;
    }

    public Lista<T> inOrdenIterativo() {
        Lista<T> r = new ListaEnlazada<T>();
        Pila<Nodo<T>> p = new PilaEnlazada<Nodo<T>>();
        Nodo<T> a = raiz;
        while (a != null || !p.estaVacia()) {
            while (a != null) {
                p.apilar(a);
                a = a.i;
            }
            a = p.desapilar();
            r.agregar(a.d);
            a = a.f;
        }
        return r;
    }

    public int tamanio() {
        return n;
    }
}
