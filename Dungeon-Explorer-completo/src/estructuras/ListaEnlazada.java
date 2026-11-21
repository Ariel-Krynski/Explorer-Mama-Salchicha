package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación enlazada de la interfaz Lista del kit. Agregar al final O(1).
 */
public class ListaEnlazada<T> extends ListaBase<T> {
    private static class Nodo<E> {
        E dato;
        Nodo<E> sig;

        Nodo(E d) {
            dato = d;
        }
    }

    private Nodo<T> primero, ultimo;
    private int n;

    public void agregar(T x) {
        Nodo<T> a = new Nodo<T>(x);
        if (ultimo == null) primero = ultimo = a;
        else {
            ultimo.sig = a;
            ultimo = a;
        }
        n++;
    }

    public void insertar(int i, T x) {
        if (i < 0 || i > n) throw new IndexOutOfBoundsException();
        if (i == n) {
            agregar(x);
            return;
        }
        Nodo<T> a = new Nodo<T>(x);
        if (i == 0) {
            a.sig = primero;
            primero = a;
        } else {
            Nodo<T> p = nodo(i - 1);
            a.sig = p.sig;
            p.sig = a;
        }
        n++;
    }

    public T obtener(int i) {
        return nodo(i).dato;
    }

    public int tamanio() {
        return n;
    }

    public T eliminar(int i) {
        if (i < 0 || i >= n) throw new IndexOutOfBoundsException();
        Nodo<T> a;
        if (i == 0) {
            a = primero;
            primero = primero.sig;
            if (n == 1) ultimo = null;
        } else {
            Nodo<T> p = nodo(i - 1);
            a = p.sig;
            p.sig = a.sig;
            if (i == n - 1) ultimo = p;
        }
        n--;
        return a.dato;
    }

    public void vaciar() {
        primero = ultimo = null;
        n = 0;
    }

    @Override
    public int indiceDe(T x) {
        int i = 0;
        for (Nodo<T> a = primero; a != null; a = a.sig, i++)
            if (x == null ? a.dato == null : x.equals(a.dato)) return i;
        return -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = primero;

            public boolean hasNext() {
                return actual != null;
            }

            public T next() {
                if (actual == null) throw new NoSuchElementException();
                T x = actual.dato;
                actual = actual.sig;
                return x;
            }
        };
    }

    private Nodo<T> nodo(int i) {
        if (i < 0 || i >= n) throw new IndexOutOfBoundsException();
        Nodo<T> a = primero;
        for (int j = 0; j < i; j++) a = a.sig;
        return a;
    }
}
