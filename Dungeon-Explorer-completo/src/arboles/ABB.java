package arboles;

import estructuras.Lista;
import estructuras.ListaEnlazada;
import estructuras.Pila;
import estructuras.PilaEnlazada;


// Árbol binario de búsqueda. Si se intenta insertar un valor repetido, no se agrega.

public class ABB<T extends Comparable<T>> {

    // Clase interna que representa cada nodo del árbol
    private static class Nodo<E> {
        E d;          // Dato que guarda el nodo
        Nodo<E> i, f; // Referencias al hijo izquierdo y al hijo derecho

        Nodo(E x) {
            d = x;
        }
    }

    private Nodo<T> raiz; // Primer nodo del árbol
    private int n;        // Cantidad de elementos que tiene el árbol

    // Inserta un nuevo elemento en el árbol
    public void insertar(T x) {

        // No permitimos insertar valores nulos
        if (x == null) throw new IllegalArgumentException("clave nula");

        // Llamamos al método recursivo para encontrar dónde insertar el valor
        raiz = insertar(raiz, x);
    }

    // Busca la posición donde corresponde insertar el nuevo elemento
    private Nodo<T> insertar(Nodo<T> a, T x) {

        // Si llegamos a una posición vacía, creamos un nuevo nodo
        if (a == null) {
            n++;
            return new Nodo<T>(x);
        }

        // Comparamos el nuevo valor con el dato del nodo actual
        int c = x.compareTo(a.d);

        // Si es menor, continuamos buscando por la izquierda
        if (c < 0) a.i = insertar(a.i, x);

            // Si es mayor, continuamos buscando por la derecha
        else if (c > 0) a.f = insertar(a.f, x);

        // Si es igual, no hacemos nada porque no agregamos repetidos

        return a;
    }

    // Indica si un elemento se encuentra en el árbol
    public boolean buscar(T x) {
        return obtener(x) != null;
    }

    // Busca un elemento y lo devuelve si existe
    public T obtener(T x) {

        if (x == null) throw new IllegalArgumentException("clave nula");

        Nodo<T> a = raiz;

        // Recorremos el árbol hasta encontrar el elemento o llegar a una posición vacía
        while (a != null) {

            int c = x.compareTo(a.d);

            // Si los valores son iguales, encontramos el elemento
            if (c == 0) return a.d;

            // Si es menor, seguimos por la izquierda; si es mayor, por la derecha
            a = c < 0 ? a.i : a.f;
        }

        // Si llegamos hasta acá, significa que el elemento no existe
        return null;
    }

    // Elimina un elemento del árbol. Devuelve true si pudo eliminarlo
    public boolean eliminar(T x) {

        if (x == null) throw new IllegalArgumentException("clave nula");

        // Guardamos la cantidad de elementos antes de eliminar
        int antes = n;

        // Llamamos al método recursivo para realizar la eliminación
        raiz = eliminar(raiz, x);

        // Si la cantidad disminuyó, significa que se eliminó un elemento
        return n < antes;
    }

    // Método recursivo que busca y elimina un elemento
    private Nodo<T> eliminar(Nodo<T> a, T x) {

        // Si llegamos a una posición vacía, no hay nada que eliminar
        if (a == null) return null;

        int c = x.compareTo(a.d);

        // Si el valor es menor, buscamos por la izquierda
        if (c < 0) a.i = eliminar(a.i, x);

            // Si el valor es mayor, buscamos por la derecha
        else if (c > 0) a.f = eliminar(a.f, x);

        else {

            // Encontramos el elemento que queremos eliminar
            n--;

            // Si no tiene hijo izquierdo, lo reemplazamos por el derecho
            if (a.i == null) return a.f;

            // Si no tiene hijo derecho, lo reemplazamos por el izquierdo
            if (a.f == null) return a.i;

            // Si tiene dos hijos, buscamos el menor valor del subárbol derecho
            Nodo<T> s = a.f;

            while (s.i != null) {
                s = s.i;
            }

            // Reemplazamos el dato del nodo por el sucesor encontrado
            a.d = s.d;

            // Eliminamos el nodo que contenía el sucesor
            a.f = eliminar(a.f, s.d);

            // Volvemos a sumar porque el contador ya disminuyó al reemplazar
            // el dato, pero realmente solo eliminamos un elemento
            n++;
        }

        return a;
    }

    // Devuelve los elementos del árbol ordenados de menor a mayor
    public Lista<T> inOrdenIterativo() {

        // Creamos una lista donde vamos a guardar el recorrido
        Lista<T> r = new ListaEnlazada<T>();

        // Utilizamos una pila para recorrer el árbol sin usar recursividad
        Pila<Nodo<T>> p = new PilaEnlazada<Nodo<T>>();

        Nodo<T> a = raiz;

        // Continuamos mientras haya nodos por recorrer o elementos en la pila
        while (a != null || !p.estaVacia()) {

            // Vamos guardando los nodos y avanzando hacia la izquierda
            while (a != null) {
                p.apilar(a);
                a = a.i;
            }

            // Recuperamos el último nodo que guardamos
            a = p.desapilar();

            // Agregamos su dato a la lista
            r.agregar(a.d);

            // Después continuamos recorriendo por la derecha
            a = a.f;
        }

        return r;
    }

    // Devuelve la cantidad de elementos que tiene el árbol
    public int tamanio() {
        return n;
    }
}