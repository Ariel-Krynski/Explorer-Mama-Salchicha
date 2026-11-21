package grafos;

import estructuras.Lista;
import estructuras.ListaEnlazada;
import estructuras.Cola;
import estructuras.ColaEnlazada;
import estructuras.Pila;
import estructuras.PilaEnlazada;


// Grafo dirigido y ponderado - Cada vértice tiene una lista con sus conexiones

public class Grafo<T> {

    // Representa una conexión entre dos vértices
    private static class Arista {
        int destino;   // Índice del vértice al que se conecta
        double peso;   // Costo o peso de la conexión

        Arista(int d, double p) {
            destino = d;
            peso = p;
        }
    }

    // Representa cada vértice del grafo
    private static class Vertice<E> {
        E dato;                        // Valor que guarda el vértice
        Lista<Arista> salidas = new ListaEnlazada<Arista>(); // Conexiones que salen de este vértice

        Vertice(E d) {
            dato = d;
        }
    }

    // Lista que almacena todos los vértices del grafo
    private Lista<Vertice<T>> vertices = new ListaEnlazada<Vertice<T>>();

    // Arreglo auxiliar que permite acceder rápidamente a los vértices por su índice
    @SuppressWarnings("unchecked")
    private Vertice<T>[] acceso = (Vertice<T>[]) new Vertice[8];

    private int cantidad; // Cantidad de vértices que tiene el grafo

    // Agrega un vértice al grafo y devuelve su posición
    public int agregarVertice(T x) {

        // Comprobamos si el vértice ya existe
        int i = indice(x);

        // Si ya existe, devolvemos su posición
        if (i >= 0) return i;

        // Si el arreglo está lleno, aumentamos su capacidad
        if (cantidad == acceso.length) {

            @SuppressWarnings("unchecked")
            Vertice<T>[] nuevo = (Vertice<T>[]) new Vertice[acceso.length * 2];

            // Copiamos los vértices al nuevo arreglo
            for (int j = 0; j < cantidad; j++) nuevo[j] = acceso[j];

            acceso = nuevo;
        }

        // Creamos el nuevo vértice
        Vertice<T> v = new Vertice<T>(x);

        // Lo agregamos a la lista y al arreglo
        vertices.agregar(v);
        acceso[cantidad] = v;

        // Devolvemos su posición y aumentamos la cantidad
        return cantidad++;
    }

    // Crea una conexión dirigida entre dos vértices con un peso determinado
    public void conectar(T a, T b, double peso) {

        // Verificamos que el peso sea válido para utilizar Dijkstra
        if (peso < 0 || Double.isNaN(peso) || Double.isInfinite(peso))
            throw new IllegalArgumentException("Dijkstra requiere pesos finitos no negativos");

        // Buscamos o agregamos los dos vértices
        int i = agregarVertice(a), j = agregarVertice(b);

        // Obtenemos la lista de conexiones del primer vértice
        Lista<Arista> l = acceso[i].salidas;

        // Recorremos las conexiones para comprobar si ya existe una
        for (Arista ar : l)

            // Si ya existe la conexión, actualizamos su peso
            if (ar.destino == j) {
                ar.peso = peso;
                return;
            }

        // Si no existe, agregamos una nueva conexión
        l.agregar(new Arista(j, peso));
    }

    // Busca la posición de un vértice dentro del arreglo
    private int indice(T x) {

        for (int i = 0; i < cantidad; i++) {

            T d = acceso[i].dato;

            // Comparamos los datos teniendo en cuenta los valores nulos
            if (d == null ? x == null : d.equals(x)) return i;
        }

        // Si no lo encontramos, devolvemos -1
        return -1;
    }

    // Comprueba si dos vértices están conectados directamente
    public boolean adyacentes(T a, T b) {

        int i = indice(a), j = indice(b);

        // Si alguno de los vértices no existe, devolvemos false
        if (i < 0 || j < 0) return false;

        // Recorremos las conexiones del primer vértice
        for (Arista ar : acceso[i].salidas)
            if (ar.destino == j) return true;

        return false;
    }

    // Devuelve el peso de la conexión entre dos vértices
    public double peso(T a, T b) {

        int i = indice(a), j = indice(b);

        // Si alguno no existe, devolvemos -1
        if (i < 0 || j < 0) return -1;

        // Buscamos la conexión y devolvemos su peso
        for (Arista ar : acceso[i].salidas)
            if (ar.destino == j) return ar.peso;

        // Si no existe una conexión, devolvemos -1
        return -1;
    }

    // Realiza un recorrido en anchura (BFS)
    public Lista<T> bfs(T inicio) {

        Lista<T> r = new ListaEnlazada<T>();

        // Buscamos la posición del vértice inicial
        int s = indice(inicio);

        // Si no existe, devolvemos una lista vacía
        if (s < 0) return r;

        // Arreglo para marcar los vértices que ya fueron visitados
        boolean[] vis = new boolean[cantidad];

        // Utilizamos una cola para recorrer por niveles
        Cola<Integer> q = new ColaEnlazada<Integer>();

        // Marcamos el vértice inicial y lo agregamos a la cola
        vis[s] = true;
        q.encolar(s);

        // Continuamos mientras haya vértices pendientes
        while (!q.estaVacia()) {

            // Sacamos el primer vértice de la cola
            int x = q.desencolar();

            // Agregamos su dato a la lista de resultados
            r.agregar(acceso[x].dato);

            // Recorremos todas las conexiones del vértice actual
            for (Arista a : acceso[x].salidas)

                // Si el destino todavía no fue visitado
                if (!vis[a.destino]) {

                    // Lo marcamos para no visitarlo nuevamente
                    vis[a.destino] = true;

                    // Lo agregamos a la cola para visitarlo después
                    q.encolar(a.destino);
                }
        }

        return r;
    }

    // Realiza un recorrido en profundidad (DFS)
    public Lista<T> dfs(T inicio) {

        Lista<T> r = new ListaEnlazada<T>();

        // Buscamos la posición del vértice inicial
        int s = indice(inicio);

        // Si el vértice existe, comenzamos el recorrido
        if (s >= 0) {

            boolean[] vis = new boolean[cantidad];

            // Llamamos al método recursivo
            dfs(s, vis, r);
        }

        return r;
    }

    // Método recursivo que realiza el recorrido en profundidad
    private void dfs(int x, boolean[] vis, Lista<T> r) {

        // Marcamos el vértice actual como visitado
        vis[x] = true;

        // Agregamos su dato a la lista
        r.agregar(acceso[x].dato);

        // Recorremos sus conexiones
        for (Arista a : acceso[x].salidas)

            // Si el destino todavía no fue visitado, seguimos recorriendo
            if (!vis[a.destino]) dfs(a.destino, vis, r);
    }

    /**
     * Devuelve la ruta de menor costo entre dos vértices.
     * Si no existe un camino, devuelve una lista vacía.
     */

    // Clase auxiliar que guarda los vértices pendientes según su distancia
    private static class Prioridad {
        int vertice;       // Posición del vértice
        double distancia;  // Distancia acumulada hasta ese vértice
        Prioridad sig;     // Referencia al siguiente elemento

        Prioridad(int v, double d) {
            vertice = v;
            distancia = d;
        }
    }

    // Busca el camino de menor costo utilizando el algoritmo de Dijkstra
    public Lista<T> dijkstra(T origen, T destino) {

        Lista<T> r = new ListaEnlazada<T>();

        // Buscamos las posiciones del origen y del destino
        int s = indice(origen), fin = indice(destino);

        // Si alguno no existe, devolvemos una lista vacía
        if (s < 0 || fin < 0) return r;

        // Guarda la distancia mínima conocida desde el origen
        double[] dist = new double[cantidad];

        // Guarda el vértice anterior para reconstruir el camino
        int[] ant = new int[cantidad];

        // Indica qué vértices ya tienen su distancia definitiva
        boolean[] fijo = new boolean[cantidad];

        // Inicializamos las distancias y los anteriores
        for (int i = 0; i < cantidad; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            ant[i] = -1;
        }

        // Lista enlazada que mantiene los vértices ordenados por prioridad
        Prioridad prioridad = null;

        // La distancia desde el origen hasta sí mismo es 0
        dist[s] = 0;

        // Agregamos el origen a la lista de prioridades
        prioridad = actualizarPrioridad(prioridad, s, 0);

        // Continuamos mientras haya vértices pendientes
        while (prioridad != null) {

            // Tomamos el primer elemento de la lista
            Prioridad actual = prioridad;
            prioridad = prioridad.sig;

            int u = actual.vertice;

            // Si ya fue procesado o su distancia quedó desactualizada, lo salteamos
            if (fijo[u] || actual.distancia != dist[u]) continue;

            // Marcamos el vértice como definitivo
            fijo[u] = true;

            // Si llegamos al destino, podemos terminar
            if (u == fin) break;

            // Recorremos las conexiones del vértice actual
            for (Arista a : acceso[u].salidas)

                // Comprobamos si encontramos un camino más corto
                if (!fijo[a.destino] && dist[u] + a.peso < dist[a.destino]) {

                    // Actualizamos la distancia mínima
                    dist[a.destino] = dist[u] + a.peso;

                    // Guardamos el vértice anterior
                    ant[a.destino] = u;

                    // Actualizamos su prioridad
                    prioridad = actualizarPrioridad(prioridad, a.destino, dist[a.destino]);
                }
        }

        // Si la distancia sigue siendo infinita, no existe un camino
        if (dist[fin] == Double.POSITIVE_INFINITY) return r;

        // Utilizamos una pila para reconstruir el camino desde el destino
        Pila<Integer> p = new PilaEnlazada<Integer>();

        // Guardamos los vértices anteriores hasta llegar al origen
        for (int x = fin; x != -1; x = ant[x])
            p.apilar(x);

        // Sacamos los elementos de la pila para obtener el camino en orden
        while (!p.estaVacia())
            r.agregar(acceso[p.desapilar()].dato);

        return r;
    }

    /**
     * Actualiza la prioridad de un vértice y mantiene la lista ordenada.
     */

    private Prioridad actualizarPrioridad(Prioridad primero, int vertice, double distancia) {

        Prioridad anterior = null, actual = primero;

        // Buscamos si el vértice ya se encuentra en la lista
        while (actual != null) {

            if (actual.vertice == vertice) {

                // Si es el primero, avanzamos el inicio de la lista
                if (anterior == null) primero = actual.sig;

                    // Si no, conectamos el elemento anterior con el siguiente
                else anterior.sig = actual.sig;

                break;
            }

            anterior = actual;
            actual = actual.sig;
        }

        // Creamos una nueva prioridad con la distancia actualizada
        Prioridad nuevo = new Prioridad(vertice, distancia);

        // Si la lista está vacía o la distancia es menor que la primera,
        // insertamos el nuevo elemento al principio
        if (primero == null || distancia < primero.distancia) {
            nuevo.sig = primero;
            return nuevo;
        }

        // Buscamos la posición donde corresponde insertar el elemento
        actual = primero;

        while (actual.sig != null && actual.sig.distancia <= distancia)
            actual = actual.sig;

        // Insertamos el nuevo elemento en la posición encontrada
        nuevo.sig = actual.sig;
        actual.sig = nuevo;

        return primero;
    }

    // Comprueba si el grafo tiene algún ciclo
    public boolean tieneCiclo() {

        // Arreglo para marcar los vértices visitados
        boolean[] visitado = new boolean[cantidad];

        // Arreglo para marcar los vértices que están en el recorrido actual
        boolean[] pila = new boolean[cantidad];

        // Revisamos todos los vértices del grafo
        for (int i = 0; i < cantidad; i++)
            if (ciclo(i, visitado, pila)) return true;

        return false;
    }

    // Método recursivo que detecta ciclos en el grafo dirigido
    private boolean ciclo(int x, boolean[] vis, boolean[] p) {

        // Si el vértice ya está en el recorrido actual, encontramos un ciclo
        if (p[x]) return true;

        // Si ya fue visitado y no está en el recorrido actual, no seguimos
        if (vis[x]) return false;

        // Marcamos el vértice como visitado
        vis[x] = true;

        // Indicamos que está dentro del recorrido actual
        p[x] = true;

        // Revisamos todas sus conexiones
        for (Arista a : acceso[x].salidas)
            if (ciclo(a.destino, vis, p)) return true;

        // Terminamos de recorrer este vértice
        p[x] = false;

        return false;
    }

    // Devuelve la cantidad de vértices del grafo
    public int cantidadVertices() {
        return cantidad;
    }

    // Cuenta la cantidad total de conexiones del grafo
    public int cantidadAristas() {

        int n = 0;

        // Sumamos las conexiones de cada vértice
        for (int i = 0; i < cantidad; i++)
            n += acceso[i].salidas.tamanio();

        return n;
    }

    // Devuelve el dato del vértice ubicado en la posición indicada
    public T vertice(int i) {

        // Verificamos que el índice sea válido
        if (i < 0 || i >= cantidad) throw new IndexOutOfBoundsException();

        return acceso[i].dato;
    }

    // Elimina todos los vértices y conexiones del grafo
    public void limpiar() {

        // Vaciamos la lista de vértices
        vertices.vaciar();

        // Creamos un nuevo arreglo vacío
        @SuppressWarnings("unchecked")
        Vertice<T>[] nuevo = (Vertice<T>[]) new Vertice[8];

        acceso = nuevo;

        // Reiniciamos la cantidad de vértices
        cantidad = 0;
    }
}