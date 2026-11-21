package markov;

import arboles.ABB;
import estructuras.Lista;
import estructuras.ListaEnlazada;

import java.util.Random;

/**
 * Markov de caracteres con contexto de orden k. Estados repetidos se agrupan en ABB; sucesores en Lista propia.
 */
public class ModeloMarkov {
    private static class Transicion implements Comparable<Transicion> {
        String estado;
        Lista<Character> sig = new ListaEnlazada<Character>();

        Transicion(String e) {
            estado = e;
        }

        public int compareTo(Transicion o) {
            return estado.compareTo(o.estado);
        }
    }

    private ABB<Transicion> tabla = new ABB<Transicion>();
    private String corpus = "";
    private int orden = 1;
    private final Random azar = new Random();

    public void entrenar(String texto, int k) {
        if (texto == null) texto = "";
        if (k < 1 || k > 4) throw new IllegalArgumentException("El orden debe estar entre 1 y 4");
        corpus = texto;
        orden = k;
        tabla = new ABB<Transicion>();
        for (int i = 0; i + k < texto.length(); i++) {
            String estado = texto.substring(i, i + k);
            Transicion t = buscar(estado);
            if (t == null) {
                t = new Transicion(estado);
                tabla.insertar(t);
            }
            t.sig.agregar(texto.charAt(i + k));
        }
    }

    private Transicion buscar(String e) {
        return tabla.obtener(new Transicion(e));
    }

    public String generar(int max) {
        if (corpus.trim().length() == 0) return "La mazmorra permanece en silencio.";
        if (corpus.length() <= orden) return corpus;
        int inicio = azar.nextInt(corpus.length() - orden + 1);
        String estado = corpus.substring(inicio, inicio + orden);
        StringBuilder salida = new StringBuilder(estado);
        for (int i = 0; i < max; i++) {
            Transicion t = buscar(estado);
            if (t == null || t.sig.estaVacia()) {
                if (estado.length() > 1) {
                    estado = estado.substring(1);
                    continue;
                }
                inicio = azar.nextInt(corpus.length() - orden + 1);
                estado = corpus.substring(inicio, inicio + orden);
                continue;
            }
            char c = t.sig.obtener(azar.nextInt(t.sig.tamanio()));
            salida.append(c);
            estado = salida.substring(salida.length() - orden);
        }
        String resultado = salida.toString();
        return resultado.trim().length() == 0 ? "Ecos antiguos recorren la sala." : resultado;
    }

    public int getOrden() {
        return orden;
    }

    public int cantidadEstados() {
        return tabla.tamanio();
    }
}
