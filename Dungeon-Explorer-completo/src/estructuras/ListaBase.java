package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class ListaBase<T> implements Lista<T> {// clase abstracta para listas

    // Operaciones derivadas
    @Override
    // indica si la lista esta vacia
    public boolean estaVacia() {
        return tamanio() == 0; // lo esta si el tamaño es 0
    }

    @Override
    public int indiceDe(T elemento) { // la primera posicion
        for (int i = 0; i < tamanio(); i++) { // recorre desde 0
            T actual = obtener(i); // obtine el elemento de la posicion i
            boolean iguales = actual == null ? elemento == null : actual.equals(elemento); // compara y considera null
            // si son iguales retorna la posicion
            if (iguales) {
                return i;
            }
        }
        return -1; // si no lo encuentra retorna -1
    }

    @Override
    // indica si el elemento existe en la lista y existe si indiceDe no devuelve -1
    public boolean contiene(T elemento) {
        return indiceDe(elemento) >= 0;
    }

    @Override
    //quita la primera aparicion de un elemento
    public boolean quitar(T elemento) {
        int indice = indiceDe(elemento); // busca su indice
        // si no existe no se puede quitar
        if (indice < 0) {
            return false;
        }
        // si este existe elimina el elemento de la lista
        eliminar(indice);
        return true;
    }

    // Recorrido con for-each
    @Override
    public Iterator<T> iterator() {  // Devuelve un iterador para recorrer la lista
        return new Iterator<T>() { // Iterador anónimo
            private int actual = 0;// Índice actual del recorrido

            @Override
            public boolean hasNext() { // indica si hay mas elementos
                return actual < tamanio(); // sigue si actual es menor al tamaño
            }

            @Override
            public T next() { // devuelve el siguiente elemento
                if (!hasNext()) { // si no tiene
                    throw new NoSuchElementException(); // lanza excepcion
                }
                return obtener(actual++); // retorna el elemento actual y incrementa el indice
            }
        };
    }
}
