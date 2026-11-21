package arboles;

import estructuras.Lista;
import estructuras.ListaEnlazada;
import estructuras.Cola;
import estructuras.ColaEnlazada;


// Árbol binario genérico - Los elementos se insertan por niveles

public class ArbolBinario<T> {

    // Clase interna que representa cada nodo del árbol
    private static class Nodo<E> {
        E dato;             // Valor que guarda el nodo
        Nodo<E> izq, der;   // Referencias al hijo izquierdo y derecho

        Nodo(E d) {
            dato = d;
        }
    }

    private Nodo<T> raiz; // Primer nodo del árbol
    private int cantidad; // Cantidad de elementos que tiene el árbol

    // Inserta un elemento manteniendo el árbol completo
    public void insertar(T x) {

        // Calculamos la posición que ocupará el nuevo elemento
        int indice = cantidad + 1;

        // Buscamos la potencia de 2 más cercana para determinar el recorrido
        int bit = 1;
        while ((bit << 1) <= indice) bit <<= 1;

        // Ajustamos el valor para comenzar a recorrer desde el nivel correspondiente
        bit >>= 1;

        // Llamamos al método recursivo para insertar el elemento
        raiz = insertarRec(raiz, indice, bit, x);

        // Aumentamos la cantidad de elementos
        cantidad++;
    }

    // Busca la posición correspondiente para insertar el nuevo nodo
    private Nodo<T> insertarRec(Nodo<T> n, int indice, int bit, T x) {

        // Si encontramos una posición vacía, creamos el nuevo nodo
        if (n == null) return new Nodo<T>(x);

        // Utilizamos los bits del índice para decidir hacia dónde avanzar
        // Si el resultado es 0, avanzamos por la izquierda
        if ((indice & bit) == 0)
            n.izq = insertarRec(n.izq, indice, bit >> 1, x);

            // Si el resultado no es 0, avanzamos por la derecha
        else
            n.der = insertarRec(n.der, indice, bit >> 1, x);

        return n;
    }

    // Indica si un elemento se encuentra en el árbol
    public boolean buscar(T x) {
        return buscar(raiz, x);
    }

    // Recorre el árbol buscando el elemento
    private boolean buscar(Nodo<T> n, T x) {

        // Si llegamos a un nodo vacío, significa que no lo encontramos
        if (n == null) return false;

        // Comprobamos si el dato del nodo actual es igual al buscado
        if (igual(n.dato, x)) return true;

        // Buscamos por la izquierda o por la derecha
        return buscar(n.izq, x) || buscar(n.der, x);
    }

    // Compara dos elementos teniendo en cuenta que pueden ser nulos
    private boolean igual(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    // Devuelve la altura del árbol
    public int altura() {
        return altura(raiz);
    }

    // Calcula la altura de forma recursiva
    private int altura(Nodo<T> n) {

        // Si el nodo está vacío, su altura es 0
        if (n == null) return 0;

        // Calculamos la altura del subárbol izquierdo y del derecho
        int a = altura(n.izq), b = altura(n.der);

        // Devolvemos 1 más la altura del subárbol más alto
        return 1 + (a > b ? a : b);
    }

    // Devuelve la cantidad de nodos del árbol
    public int cantidadNodos() {
        return contar(raiz);
    }

    // Cuenta los nodos de forma recursiva
    private int contar(Nodo<T> n) {

        // Si no hay nodo, devolvemos 0
        if (n == null) return 0;

        // Contamos el nodo actual y los nodos de ambos subárboles
        return 1 + contar(n.izq) + contar(n.der);
    }

    // Devuelve la cantidad de hojas del árbol
    public int cantidadHojas() {
        return hojas(raiz);
    }

    // Cuenta cuántas hojas tiene el árbol
    private int hojas(Nodo<T> n) {

        // Si el nodo está vacío, no cuenta como hoja
        if (n == null) return 0;

        // Si no tiene hijos, significa que es una hoja
        if (n.izq == null && n.der == null) return 1;

        // Sumamos las hojas de los dos subárboles
        return hojas(n.izq) + hojas(n.der);
    }

    // Devuelve los elementos recorriendo primero la raíz
    public Lista<T> preOrden() {

        // Creamos una lista para guardar los resultados
        Lista<T> r = new ListaEnlazada<T>();

        // Llamamos al método que realiza el recorrido
        pre(raiz, r);

        return r;
    }

    // Realiza el recorrido en preorden
    private void pre(Nodo<T> n, Lista<T> r) {

        // Caso base: si no hay nodo, terminamos
        if (n == null) return;

        // Primero agregamos el dato del nodo actual
        r.agregar(n.dato);

        // Después recorremos el subárbol izquierdo
        pre(n.izq, r);

        // Finalmente recorremos el subárbol derecho
        pre(n.der, r);
    }

    // Devuelve los elementos recorriendo primero el subárbol izquierdo
    public Lista<T> inOrden() {

        Lista<T> r = new ListaEnlazada<T>();

        ino(raiz, r);

        return r;
    }

    // Realiza el recorrido en inorden
    private void ino(Nodo<T> n, Lista<T> r) {

        // Caso base
        if (n == null) return;

        // Primero recorremos la izquierda
        ino(n.izq, r);

        // Después agregamos el nodo actual
        r.agregar(n.dato);

        // Finalmente recorremos la derecha
        ino(n.der, r);
    }

    // Devuelve los elementos recorriendo primero los hijos
    public Lista<T> postOrden() {

        Lista<T> r = new ListaEnlazada<T>();

        post(raiz, r);

        return r;
    }

    // Realiza el recorrido en postorden
    private void post(Nodo<T> n, Lista<T> r) {

        // Caso base
        if (n == null) return;

        // Primero recorremos la izquierda
        post(n.izq, r);

        // Después recorremos la derecha
        post(n.der, r);

        // Al final agregamos el nodo actual
        r.agregar(n.dato);
    }

    // Devuelve los elementos recorriendo el árbol por niveles
    public Lista<T> porNiveles() {

        Lista<T> r = new ListaEnlazada<T>();

        // Si el árbol está vacío, devolvemos la lista vacía
        if (raiz == null) return r;

        // Utilizamos una cola para recorrer los nodos por orden de llegada
        Cola<Nodo<T>> q = new ColaEnlazada<Nodo<T>>();

        // Agregamos la raíz a la cola
        q.encolar(raiz);

        // Continuamos mientras haya nodos pendientes
        while (!q.estaVacia()) {

            // Sacamos el primer nodo de la cola
            Nodo<T> n = q.desencolar();

            // Agregamos su dato a la lista
            r.agregar(n.dato);

            // Si tiene hijo izquierdo, lo agregamos a la cola
            if (n.izq != null) q.encolar(n.izq);

            // Si tiene hijo derecho, también lo agregamos
            if (n.der != null) q.encolar(n.der);
        }

        return r;
    }


     // Reconstruye el árbol utilizando los valores de un recorrido en preorden.


    public void reconstruirDesdePreOrden(Lista<T> valores) {

        // Verificamos que la lista recibida no sea nula
        if (valores == null) throw new IllegalArgumentException("lista nula");

        // Actualizamos la cantidad de elementos
        cantidad = valores.tamanio();

        // Creamos un contador para saber qué posición de la lista estamos leyendo
        int[] cursor = new int[]{0};

        // Llamamos al método recursivo para reconstruir el árbol
        raiz = reconstruir(valores, cursor, cantidad);
    }

    // Construye los nodos del árbol de forma recursiva
    private Nodo<T> reconstruir(Lista<T> valores, int[] cursor, int n) {

        // Si no quedan nodos para construir, terminamos
        if (n <= 0) return null;

        // Creamos un nodo con el siguiente valor de la lista
        Nodo<T> nodo = new Nodo<T>(valores.obtener(cursor[0]++));

        // Calculamos cuántos nodos corresponden al subárbol izquierdo
        int izquierda = izquierdaCompleta(n);

        // Construimos el subárbol izquierdo
        nodo.izq = reconstruir(valores, cursor, izquierda);

        // Construimos el subárbol derecho con los nodos restantes
        nodo.der = reconstruir(valores, cursor, n - 1 - izquierda);

        return nodo;
    }

    // Calcula cuántos nodos debe tener el subárbol izquierdo
    // para mantener la forma de un árbol binario completo
    private int izquierdaCompleta(int n) {

        // Si hay cero o un nodo, no hay elementos a la izquierda
        if (n <= 1) return 0;

        // Calculamos la altura del árbol
        int h = 0, p = 1;

        while (p <= n) {
            p *= 2;
            h++;
        }

        h--;

        // Calculamos la cantidad de nodos que hay antes del último nivel
        int nodosAntes = (1 << h) - 1;

        // Calculamos cuántos nodos hay en el último nivel
        int ultimo = n - nodosAntes;

        // Calculamos la capacidad máxima del último nivel izquierdo
        int capacidad = 1 << (h - 1);

        // Devolvemos la cantidad de nodos que corresponden a la izquierda
        return (capacidad - 1) + (ultimo < capacidad ? ultimo : capacidad);
    }

    // Elimina todos los elementos del árbol
    public void vaciar() {

        // Eliminamos la referencia a la raíz
        raiz = null;

        // Reiniciamos la cantidad de elementos
        cantidad = 0;
    }
}