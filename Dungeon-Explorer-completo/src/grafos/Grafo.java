package grafos;

import estructuras.Lista;
import estructuras.ListaEnlazada;
import estructuras.Cola;
import estructuras.ColaEnlazada;
import estructuras.Pila;
import estructuras.PilaEnlazada;

/**
 * Grafo dirigido ponderado. Cada vértice mantiene su lista de adyacencia con la Lista propia.
 */
public class Grafo<T> {
    private static class Arista {
        int destino;
        double peso;

        Arista(int d, double p) {
            destino = d;
            peso = p;
        }
    }

    private static class Vertice<E> {
        E dato;
        Lista<Arista> salidas = new ListaEnlazada<Arista>();

        Vertice(E d) {
            dato = d;
        }
    }

    private Lista<Vertice<T>> vertices = new ListaEnlazada<Vertice<T>>();
    @SuppressWarnings("unchecked")
    private Vertice<T>[] acceso = (Vertice<T>[]) new Vertice[8];
    private int cantidad;

    public int agregarVertice(T x) {
        int i = indice(x);
        if (i >= 0) return i;
        if (cantidad == acceso.length) {
            @SuppressWarnings("unchecked") Vertice<T>[] nuevo = (Vertice<T>[]) new Vertice[acceso.length * 2];
            for (int j = 0; j < cantidad; j++) nuevo[j] = acceso[j];
            acceso = nuevo;
        }
        Vertice<T> v = new Vertice<T>(x);
        vertices.agregar(v);
        acceso[cantidad] = v;
        return cantidad++;
    }

    public void conectar(T a, T b, double peso) {
        if (peso < 0 || Double.isNaN(peso) || Double.isInfinite(peso))
            throw new IllegalArgumentException("Dijkstra requiere pesos finitos no negativos");
        int i = agregarVertice(a), j = agregarVertice(b);
        Lista<Arista> l = acceso[i].salidas;
        for (Arista ar : l)
            if (ar.destino == j) {
                ar.peso = peso;
                return;
            }
        l.agregar(new Arista(j, peso));
    }

    private int indice(T x) {
        for (int i = 0; i < cantidad; i++) {
            T d = acceso[i].dato;
            if (d == null ? x == null : d.equals(x)) return i;
        }
        return -1;
    }

    public boolean adyacentes(T a, T b) {
        int i = indice(a), j = indice(b);
        if (i < 0 || j < 0) return false;
        for (Arista ar : acceso[i].salidas) if (ar.destino == j) return true;
        return false;
    }

    public double peso(T a, T b) {
        int i = indice(a), j = indice(b);
        if (i < 0 || j < 0) return -1;
        for (Arista ar : acceso[i].salidas) if (ar.destino == j) return ar.peso;
        return -1;
    }

    public Lista<T> bfs(T inicio) {
        Lista<T> r = new ListaEnlazada<T>();
        int s = indice(inicio);
        if (s < 0) return r;
        boolean[] vis = new boolean[cantidad];
        Cola<Integer> q = new ColaEnlazada<Integer>();
        vis[s] = true;
        q.encolar(s);
        while (!q.estaVacia()) {
            int x = q.desencolar();
            r.agregar(acceso[x].dato);
            for (Arista a : acceso[x].salidas)
                if (!vis[a.destino]) {
                    vis[a.destino] = true;
                    q.encolar(a.destino);
                }
        }
        return r;
    }

    public Lista<T> dfs(T inicio) {
        Lista<T> r = new ListaEnlazada<T>();
        int s = indice(inicio);
        if (s >= 0) {
            boolean[] vis = new boolean[cantidad];
            dfs(s, vis, r);
        }
        return r;
    }

    private void dfs(int x, boolean[] vis, Lista<T> r) {
        vis[x] = true;
        r.agregar(acceso[x].dato);
        for (Arista a : acceso[x].salidas) if (!vis[a.destino]) dfs(a.destino, vis, r);
    }

    /**
     * Devuelve la ruta de costo mínimo; lista vacía si no hay camino.
     */
    private static class Prioridad {
        int vertice;
        double distancia;
        Prioridad sig;

        Prioridad(int v, double d) {
            vertice = v;
            distancia = d;
        }
    }

    public Lista<T> dijkstra(T origen, T destino) {
        Lista<T> r = new ListaEnlazada<T>();
        int s = indice(origen), fin = indice(destino);
        if (s < 0 || fin < 0) return r;
        double[] dist = new double[cantidad];
        int[] ant = new int[cantidad];
        boolean[] fijo = new boolean[cantidad];
        for (int i = 0; i < cantidad; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            ant[i] = -1;
        }
        Prioridad prioridad = null;
        dist[s] = 0;
        prioridad = actualizarPrioridad(prioridad, s, 0);
        while (prioridad != null) {
            Prioridad actual = prioridad;
            prioridad = prioridad.sig;
            int u = actual.vertice;
            if (fijo[u] || actual.distancia != dist[u]) continue;
            fijo[u] = true;
            if (u == fin) break;
            for (Arista a : acceso[u].salidas)
                if (!fijo[a.destino] && dist[u] + a.peso < dist[a.destino]) {
                    dist[a.destino] = dist[u] + a.peso;
                    ant[a.destino] = u;
                    prioridad = actualizarPrioridad(prioridad, a.destino, dist[a.destino]);
                }
        }
        if (dist[fin] == Double.POSITIVE_INFINITY) return r;
        Pila<Integer> p = new PilaEnlazada<Integer>();
        for (int x = fin; x != -1; x = ant[x]) p.apilar(x);
        while (!p.estaVacia()) r.agregar(acceso[p.desapilar()].dato);
        return r;
    }

    /**
     * Actualiza una prioridad existente y mantiene una sola entrada por vértice: O(V).
     */
    private Prioridad actualizarPrioridad(Prioridad primero, int vertice, double distancia) {
        Prioridad anterior = null, actual = primero;
        while (actual != null) {
            if (actual.vertice == vertice) {
                if (anterior == null) primero = actual.sig;
                else anterior.sig = actual.sig;
                break;
            }
            anterior = actual;
            actual = actual.sig;
        }
        Prioridad nuevo = new Prioridad(vertice, distancia);
        if (primero == null || distancia < primero.distancia) {
            nuevo.sig = primero;
            return nuevo;
        }
        actual = primero;
        while (actual.sig != null && actual.sig.distancia <= distancia) actual = actual.sig;
        nuevo.sig = actual.sig;
        actual.sig = nuevo;
        return primero;
    }

    public boolean tieneCiclo() {
        boolean[] visitado = new boolean[cantidad], pila = new boolean[cantidad];
        for (int i = 0; i < cantidad; i++) if (ciclo(i, visitado, pila)) return true;
        return false;
    }

    private boolean ciclo(int x, boolean[] vis, boolean[] p) {
        if (p[x]) return true;
        if (vis[x]) return false;
        vis[x] = true;
        p[x] = true;
        for (Arista a : acceso[x].salidas) if (ciclo(a.destino, vis, p)) return true;
        p[x] = false;
        return false;
    }

    public int cantidadVertices() {
        return cantidad;
    }

    public int cantidadAristas() {
        int n = 0;
        for (int i = 0; i < cantidad; i++) n += acceso[i].salidas.tamanio();
        return n;
    }

    public T vertice(int i) {
        if (i < 0 || i >= cantidad) throw new IndexOutOfBoundsException();
        return acceso[i].dato;
    }

    public void limpiar() {
        vertices.vaciar();
        @SuppressWarnings("unchecked") Vertice<T>[] nuevo = (Vertice<T>[]) new Vertice[8];
        acceso = nuevo;
        cantidad = 0;
    }
}
