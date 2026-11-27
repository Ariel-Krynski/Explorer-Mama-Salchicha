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
    // Agrega un elemento al final de la cola
    public void encolar(T x) {
        Nodo<T> a = new Nodo<T>(x);
        if (fin == null) frente = fin = a; // Si la cola está vacía, el nuevo nodo es frente y fin
        else {
            fin.s = a;
            fin = a;
        }
        n++;
    }
    // Quita y retorna el elemento del frente
    public T desencolar() {
        if (frente == null) throw new IllegalStateException("cola vacía");
       // Guarda el dato del frente y pasa a ser el siguiente
        T x = frente.d;
        frente = frente.s;
        if (frente == null) fin = null;// Si la cola quedó vacía, fin también queda null
        n--;
        return x;
    }
    // Consulta el elemento del frente sin quitarlo
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
