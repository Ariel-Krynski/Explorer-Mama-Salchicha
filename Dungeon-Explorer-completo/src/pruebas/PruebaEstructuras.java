package pruebas;

import arboles.ArbolBinario;
import arboles.ABB;
import grafos.Grafo;
import tokens.Trie;
import markov.ModeloMarkov;
import estructuras.*;

/**
 * Batería autocontenida de 20 comprobaciones. Ejecutar: java -cp out verificador.PruebaEstructuras
 */
public class PruebaEstructuras {
    private static int ok = 0;

    private static void comprobar(boolean b, String m) {
        if (!b) throw new RuntimeException("FALLÓ: " + m);
        ok++;
    }

    public static void main(String[] args) {
        ArbolBinario<Integer> a = new ArbolBinario<Integer>();
        comprobar(a.altura() == 0, "árbol vacío altura");
        comprobar(a.cantidadHojas() == 0, "árbol vacío hojas");
        comprobar(a.preOrden().estaVacia(), "preorden vacío");
        a.insertar(7);
        comprobar(a.buscar(7), "buscar nodo único");
        comprobar(a.altura() == 1, "altura nodo único");
        comprobar(a.cantidadHojas() == 1, "hoja única");
        a.insertar(3);
        a.insertar(9);
        comprobar(a.cantidadNodos() == 3, "cantidad nodos");
        comprobar(a.preOrden().obtener(0) == 7, "preorden");
        comprobar(a.inOrden().obtener(0) == 3, "inorden");
        comprobar(a.postOrden().obtener(0) == 3, "postorden");
        comprobar(a.porNiveles().obtener(2) == 9, "niveles");
        ArbolBinario<Integer> reconstruido = new ArbolBinario<Integer>();
        reconstruido.reconstruirDesdePreOrden(a.preOrden());
        comprobar(reconstruido.preOrden().tamanio() == 3 && reconstruido.altura() == 2, "reconstrucción preorden");
        ArbolBinario<Integer> completo = new ArbolBinario<Integer>();
        for (int z = 0; z < 6; z++) completo.insertar(z);
        ArbolBinario<Integer> copia = new ArbolBinario<Integer>();
        copia.reconstruirDesdePreOrden(completo.preOrden());
        comprobar(copia.preOrden().obtener(3).equals(completo.preOrden().obtener(3)) && copia.inOrden().tamanio() == 6, "reconstrucción árbol completo");
        ABB<Integer> b = new ABB<Integer>();
        b.insertar(5);
        b.insertar(2);
        b.insertar(8);
        comprobar(b.buscar(2), "ABB buscar");
        comprobar(b.eliminar(5), "ABB eliminar raíz");
        comprobar(!b.buscar(5), "ABB ausencia tras borrar");
        comprobar(b.inOrdenIterativo().obtener(0) == 2, "ABB iterativo");
        Grafo<Integer> g = new Grafo<Integer>();
        g.agregarVertice(1);
        comprobar(g.bfs(1).tamanio() == 1, "BFS aislado");
        comprobar(g.dfs(1).tamanio() == 1, "DFS aislado");
        comprobar(g.dijkstra(1, 2).estaVacia(), "camino inexistente");
        g.conectar(1, 2, 3);
        comprobar(g.dijkstra(1, 2).tamanio() == 2, "Dijkstra ruta");
        Grafo<Integer> ponderado = new Grafo<Integer>();
        ponderado.conectar(1, 2, 8);
        ponderado.conectar(1, 3, 1);
        ponderado.conectar(3, 2, 1);
        comprobar(ponderado.dijkstra(1, 2).obtener(1) == 3, "Dijkstra elige menor costo");
        comprobar(ponderado.tieneCiclo() == false, "grafo acíclico");
        ponderado.conectar(2, 1, 1);
        comprobar(ponderado.tieneCiclo(), "detección de ciclo");
        Grafo<Integer> vacio = new Grafo<Integer>();
        comprobar(vacio.bfs(99).estaVacia(), "grafo sin vértices");
        Trie t = new Trie();
        t.insertar("gema");
        t.insertar("gem");
        comprobar(t.buscar("gema"), "trie exacto");
        comprobar(t.buscarPrefijo("ge"), "trie prefijo");
        comprobar(t.tokenizar("gemas").obtener(0).equals("gema"), "coincidencia más larga");
        comprobar(!t.buscar("puerta"), "trie sin coincidencia");
        ModeloMarkov m = new ModeloMarkov();
        m.entrenar("abcabcabc", 2);
        comprobar(m.getOrden() == 2, "Markov orden");
        comprobar(m.generar(20).length() > 0, "Markov genera");
        for (int k = 1; k <= 4; k++) {
            m.entrenar("la gema brilla en la cripta", k);
            comprobar(m.getOrden() == k && m.generar(12).length() > 0, "Markov orden " + k);
        }
        ModeloMarkov sinSucesor = new ModeloMarkov();
        sinSucesor.entrenar("abc", 2);
        comprobar(sinSucesor.generar(30).length() > 0, "Markov sin sucesores recupera contexto");
        comprobar(new ListaEnlazada<Integer>().estaVacia(), "lista vacía");
        Pila<Integer> p = new PilaEnlazada<Integer>();
        p.apilar(4);
        comprobar(p.desapilar() == 4, "pila LIFO");
        Cola<Integer> q = new ColaEnlazada<Integer>();
        q.encolar(6);
        comprobar(q.desencolar() == 6, "cola FIFO");
        comprobar(verificador.VerificadorEstructuras.verificarLista(new ListaEnlazada<Integer>()), "verificador oficial de Lista");
        comprobar(verificador.VerificadorEstructuras.verificarPila(new PilaEnlazada<Integer>()), "verificador oficial de Pila");
        comprobar(verificador.VerificadorEstructuras.verificarCola(new ColaEnlazada<Integer>()), "verificador oficial de Cola");
        System.out.println("OK: " + ok + " comprobaciones propias + verificador del kit.");
    }
}
