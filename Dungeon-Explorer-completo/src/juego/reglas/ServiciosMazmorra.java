package juego.reglas;

import arboles.ArbolBinario;
import grafos.Grafo;
import markov.ModeloMarkov;
import tokens.Trie;
import persistencia.AlmacenJuego;
import estructuras.Pila;
import estructuras.PilaEnlazada;
import estructuras.Lista;
import estructuras.ListaEnlazada;

// Es una única instancia (Singleton) que centraliza los datos y estructuras del juego:
// carga las 6 salas, el mapa como grafo, el árbol de salas visitadas, el modelo de Markov
// para textos, el Trie de comandos válidos, la pila de historial y la persistencia en disco.
// Además ofrece métodos para consultar puertas, nombres, rutas y recorridos, marcar
// visitas, generar descripciones, validar comandos, deshacer movimientos y guardar o leer récords.
public final class ServiciosMazmorra {
    private static final ServiciosMazmorra INSTANCIA = new ServiciosMazmorra();
    private final AlmacenJuego datos = new AlmacenJuego();
    private final Grafo<Integer> grafo = new Grafo<Integer>();
    private final ArbolBinario<Integer> visitadas = new ArbolBinario<Integer>();
    private final ModeloMarkov markov = new ModeloMarkov();
    private final Trie vocabulario = new Trie();
    private final Pila<Integer> historial = new PilaEnlazada<Integer>();
    private final Lista<ElementoSala> salas = new ListaEnlazada<ElementoSala>();

    private ServiciosMazmorra() {
        salas.agregar(new ElementoSala(0, "Vestíbulo"));
        salas.agregar(new ElementoSala(1, "Galería"));
        salas.agregar(new ElementoSala(2, "Cámara de piedra"));
        salas.agregar(new ElementoSala(3, "Guarida"));
        salas.agregar(new ElementoSala(4, "Tesorería"));
        salas.agregar(new ElementoSala(5, "Santuario"));
        datos.cargarGrafo(grafo);
        if (grafo.cantidadVertices() == 0) {
            AlmacenJuego.grafoInicial(grafo);
        }
        ArbolBinario<Integer> guardado = datos.leerArbol();
        Lista<Integer> l = guardado.porNiveles();
        for (int i = 0; i < l.tamanio(); i++) {
            visitadas.insertar(l.obtener(i));
        }
        String corpus = datos.leerCorpus();
        markov.entrenar(corpus, 2);
        datos.guardarCorpus(corpus);
        vocabulario.insertar("abrir");
        vocabulario.insertar("cofre");
        vocabulario.insertar("gema");
        vocabulario.insertar("salir");
        vocabulario.insertar("puerta");
        vocabulario.insertar("interactuar");
        vocabulario.insertar("cripta");
        vocabulario.insertar("sombra");
        vocabulario.insertar("tesoro");
    }

    public static ServiciosMazmorra obtener() {
        return INSTANCIA;
    }

    public boolean hayPuerta(int a, int b) {
        return grafo.adyacentes(a, b);
    }

    public String nombreSala(int id) {
        return salas.obtener(id).getNombre();
    }

    public Lista<Integer> rutaSalida(int desde) {
        return grafo.dijkstra(desde, 5);
    }

    public Lista<Integer> recorridoBfs(int desde) {
        return grafo.bfs(desde);
    }

    public Lista<Integer> recorridoDfs(int desde) {
        return grafo.dfs(desde);
    }

    public boolean tieneCiclo() {
        return grafo.tieneCiclo();
    }

    public void visitar(int sala) {
        if (!visitadas.buscar(sala)) {
            visitadas.insertar(sala);
            datos.guardarArbol(visitadas);
        }
    }

    public String descripcion() {
        return "Markov k=" + markov.getOrden() + ": " + markov.generar(38);
    }

    public int ordenMarkov() {
        return markov.getOrden();
    }

    public void cambiarOrden(int k) {
        String corpus = datos.leerCorpus();
        markov.entrenar(corpus, k);
    }

    public boolean exploroTodasLasSalas() {
        for (int i = 1; i < 6; i++) {
            if (!visitadas.buscar(i)) {
                return false;
            }
        }
        return grafo.tieneCiclo();
    }

    public boolean comandoValido(String s) {
        Lista<String> tokens = vocabulario.tokenizar(s);
        return tokens.tamanio() > 0 && vocabulario.buscar(tokens.obtener(0));
    }

    public void registrarMovimiento(int sala) {
        historial.apilar(sala);
    }

    public Integer deshacer() {
        return historial.estaVacia() ? null : historial.desapilar();
    }

    public void guardar() {
        datos.guardarArbol(visitadas);
        datos.guardarGrafo(grafo);
    }

    public String avisosDatos() {
        return datos.getAvisos();
    }

    public int record() {
        return datos.leerRanking();
    }

    public void guardarPuntaje(int p) {
        datos.guardarRanking(p);
    }
}
