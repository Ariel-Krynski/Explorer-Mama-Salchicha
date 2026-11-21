package arboles;

import estructuras.Lista;
import estructuras.ListaEnlazada;
import estructuras.Cola;
import estructuras.ColaEnlazada;

/**
 * Árbol binario genérico insertado por niveles. Todos los cálculos/recorridos recursivos tienen caso base.
 */
public class ArbolBinario<T> {
    private static class Nodo<E> {
        E dato;
        Nodo<E> izq, der;

        Nodo(E d) {
            dato = d;
        }
    }

    private Nodo<T> raiz;
    private int cantidad;

    public void insertar(T x) {
        int indice = cantidad + 1;
        int bit = 1;
        while ((bit << 1) <= indice) bit <<= 1;
        bit >>= 1;
        raiz = insertarRec(raiz, indice, bit, x);
        cantidad++;
    }

    private Nodo<T> insertarRec(Nodo<T> n, int indice, int bit, T x) {
        if (n == null) return new Nodo<T>(x);
        if ((indice & bit) == 0) n.izq = insertarRec(n.izq, indice, bit >> 1, x);
        else n.der = insertarRec(n.der, indice, bit >> 1, x);
        return n;
    }

    public boolean buscar(T x) {
        return buscar(raiz, x);
    }

    private boolean buscar(Nodo<T> n, T x) {
        if (n == null) return false;
        if (igual(n.dato, x)) return true;
        return buscar(n.izq, x) || buscar(n.der, x);
    }

    private boolean igual(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    public int altura() {
        return altura(raiz);
    }

    private int altura(Nodo<T> n) {
        if (n == null) return 0;
        int a = altura(n.izq), b = altura(n.der);
        return 1 + (a > b ? a : b);
    }

    public int cantidadNodos() {
        return contar(raiz);
    }

    private int contar(Nodo<T> n) {
        if (n == null) return 0;
        return 1 + contar(n.izq) + contar(n.der);
    }

    public int cantidadHojas() {
        return hojas(raiz);
    }

    private int hojas(Nodo<T> n) {
        if (n == null) return 0;
        if (n.izq == null && n.der == null) return 1;
        return hojas(n.izq) + hojas(n.der);
    }

    public Lista<T> preOrden() {
        Lista<T> r = new ListaEnlazada<T>();
        pre(raiz, r);
        return r;
    }

    private void pre(Nodo<T> n, Lista<T> r) {
        if (n == null) return;
        r.agregar(n.dato);
        pre(n.izq, r);
        pre(n.der, r);
    }

    public Lista<T> inOrden() {
        Lista<T> r = new ListaEnlazada<T>();
        ino(raiz, r);
        return r;
    }

    private void ino(Nodo<T> n, Lista<T> r) {
        if (n == null) return;
        ino(n.izq, r);
        r.agregar(n.dato);
        ino(n.der, r);
    }

    public Lista<T> postOrden() {
        Lista<T> r = new ListaEnlazada<T>();
        post(raiz, r);
        return r;
    }

    private void post(Nodo<T> n, Lista<T> r) {
        if (n == null) return;
        post(n.izq, r);
        post(n.der, r);
        r.agregar(n.dato);
    }

    public Lista<T> porNiveles() {
        Lista<T> r = new ListaEnlazada<T>();
        if (raiz == null) return r;
        Cola<Nodo<T>> q = new ColaEnlazada<Nodo<T>>();
        q.encolar(raiz);
        while (!q.estaVacia()) {
            Nodo<T> n = q.desencolar();
            r.agregar(n.dato);
            if (n.izq != null) q.encolar(n.izq);
            if (n.der != null) q.encolar(n.der);
        }
        return r;
    }

    /**
     * Reconstruye exactamente la forma completa del árbol desde sus valores en preorden.
     */
    public void reconstruirDesdePreOrden(Lista<T> valores) {
        if (valores == null) throw new IllegalArgumentException("lista nula");
        cantidad = valores.tamanio();
        int[] cursor = new int[]{0};
        raiz = reconstruir(valores, cursor, cantidad);
    }

    private Nodo<T> reconstruir(Lista<T> valores, int[] cursor, int n) {
        if (n <= 0) return null;
        Nodo<T> nodo = new Nodo<T>(valores.obtener(cursor[0]++));
        int izquierda = izquierdaCompleta(n);
        nodo.izq = reconstruir(valores, cursor, izquierda);
        nodo.der = reconstruir(valores, cursor, n - 1 - izquierda);
        return nodo;
    }

    private int izquierdaCompleta(int n) {
        if (n <= 1) return 0;
        int h = 0, p = 1;
        while (p <= n) {
            p *= 2;
            h++;
        }
        h--;
        int nodosAntes = (1 << h) - 1;
        int ultimo = n - nodosAntes;
        int capacidad = 1 << (h - 1);
        return (capacidad - 1) + (ultimo < capacidad ? ultimo : capacidad);
    }

    public void vaciar() {
        raiz = null;
        cantidad = 0;
    }
}
