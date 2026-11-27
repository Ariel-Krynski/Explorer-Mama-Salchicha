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

    public T eliminar(int i) { // Elimina el elemento en la posición i y lo retorna
        if (i < 0 || i >= n) throw new IndexOutOfBoundsException(); // Valida el índice
        Nodo<T> a; // Nodo que se va a eliminar
        if (i == 0) { // Si se elimina el primero
            a = primero; // Guarda el primer nodo
            primero = primero.sig; // El primero pasa a ser el siguiente
            if (n == 1) ultimo = null; // Si era el único nodo queda null
        } else { // si es de otra posicion
            Nodo<T> p = nodo(i - 1); // Obtiene el nodo anterior
            a = p.sig; // a es el nodo a eliminar
            p.sig = a.sig; // El anterior se salta el nodo eliminado
            if (i == n - 1) ultimo = p; // Si se eliminó el último, actualiza ultimo
        }
        n--; // Disminuye el tamaño
        return a.dato; // Retorna el dato eliminado
    }

    public void vaciar() { // Elimina todos los elementos
        primero = ultimo = null; // Quita las referencias a primero y último
        n = 0; // Reinicia el tamaño
    } // Fin de vaciar

    @Override
    public int indiceDe(T x) { // Busca la primera posición de x
        int i = 0; // Contador
        for (Nodo<T> a = primero; a != null; a = a.sig, i++) // Recorre la lista
            if (x == null ? a.dato == null : x.equals(a.dato)) return i; // Compara considerando null
        return -1; // Si no lo encuentra, retorna -1
    }

    @Override
    public Iterator<T> iterator() { // Devuelve un iterador para recorrer la lista
        return new Iterator<T>() { // Crea un iterador anónimo
            private Nodo<T> actual = primero; // Cursor que empieza en el primer nodo

            public boolean hasNext() { // Indica si hay más elementos
                return actual != null; // Hay siguiente si actual no es null
            }

            public T next() { // Devuelve el siguiente elemento
                if (actual == null) throw new NoSuchElementException(); // Si no hay elemento, lanza excepción
                T x = actual.dato; // Guarda el dato actual
                actual = actual.sig; // Avanza al siguiente nodo
                return x; // Retorna el dato guardado
            }
        };
    }

    private Nodo<T> nodo(int i) { // Devuelve el nodo en la posición i
        if (i < 0 || i >= n) throw new IndexOutOfBoundsException();
        Nodo<T> a = primero;
        for (int j = 0; j < i; j++) a = a.sig;
        return a; // Retorna el nodo encontrado
    }
}
