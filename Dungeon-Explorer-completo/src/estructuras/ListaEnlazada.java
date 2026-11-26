package estructuras;

import java.util.Iterator; // para recorrer la lista
import java.util.NoSuchElementException; // excepcion para cuando no hay elementos

/**
 * Implementación enlazada de la interfaz Lista del kit. Agregar al final O(1).
 */
public class ListaEnlazada<T> extends ListaBase<T> {  // hereda de ListaBase
    //representacion del nodo de la lista
    private static class Nodo<E> {
        E dato;
        Nodo<E> sig;
        // constructor del nodo
        Nodo(E d) {
            dato = d;
        }
    }

    private Nodo<T> primero, ultimo; // referencia al primero y ultimo nodo
    private int n;// cantidad de elementos

    // Agregar elemento al final de la lista
    public void agregar(T x) {
        Nodo<T> a = new Nodo<T>(x); // crea el nodo con el dato x
       // Si hay elementos lo conecta con el ultimo nodo, apuntando a este, trnasformandolo en el ultimo y aumentando el tamaño
        if (ultimo == null) primero = ultimo = a;
        else {
            ultimo.sig = a;
            ultimo = a;
        }
        n++;
    }

    public void insertar(int i, T x) {
        if (i < 0 || i > n) throw new IndexOutOfBoundsException();// cvalida el indice
        // si este se agrega ultimo utiliza agregar
        if (i == n) {
            agregar(x);
            return;
        }
        Nodo<T> a = new Nodo<T>(x); // crea un nuevo nodo
        if (i == 0) {// si es al principio
            a.sig = primero; // el nuevo apunta al antiguo primero
            primero = a; // el nodo nuevo pasa a ser primero
        } else { // pero si se inserta en el primero
            Nodo<T> p = nodo(i - 1); // obtiene el nodo anterior
            a.sig = p.sig; // el nuevo nodo apunta al anterior de esa posicion
            p.sig = a; // el anterior apunta al nuevo nodo
        }
        n++; // incrementa el tamaño
    }
// devuelve el dato de la posicion i y lo retorna
    public T obtener(int i) {
        return nodo(i).dato;
    }
// devuelve el tamaño
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
