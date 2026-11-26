package markov;

import arboles.ABB; // Estructura de arbol binario
import estructuras.Lista; // estructura de lista
import estructuras.ListaEnlazada; // y lista enlazada

import java.util.Random;// clase random para generar numeros aleotorios

/**
 * Markov de caracteres con contexto de orden k. Estados repetidos se agrupan en ABB; sucesores en Lista propia.
 */
public class ModeloMarkov {
    // Clase interna. representa UNA FILA de la tabla de Markov.
    // Es "privada" porque solo la usa Markov por dentro.
    // Es "static" porque no necesita acceder a los atributos de ModeloMarkov.
    private static class Transicion implements Comparable<Transicion> {
        // Lista que guarda todos los caracteres del estado en el texto.
        String estado;
        // Guarda las letras que vienen después
        Lista<Character> sig = new ListaEnlazada<Character>();
        // Constructor de la clase Transicion que inicializa el estado.
        Transicion(String e) {
            // Asigna la cadena recibida al atributo estado.
            estado = e;
        }
        //Metodo para que el ABB sepa ordenar las transiciones
        public int compareTo(Transicion o) {
            return estado.compareTo(o.estado);// Compara alfabéticamente el estado actual con el estado de la otra transición.
        }
    }
// atributos de la clase
    private ABB<Transicion> tabla = new ABB<Transicion>(); // Diccionario de estados
    private String corpus = "";// texto del que aprende
    private int orden = 1; // valor k, cuantas letras mira hacia atras
    private final Random azar = new Random(); // elegir letras en aleatorio


    // Metodo publico para entrenar el modelo a partir de un texto base y valor de k
    public void entrenar(String texto, int k) {
        if (texto == null) texto = ""; // si el texto dado es nulo, lo convierte en cadena vacia
        if (k < 1 || k > 4) throw new IllegalArgumentException("El orden debe estar entre 1 y 4"); // valida que k este entre 1 y 4, sino lanza excepcion
        corpus = texto; // guarda el texto
        orden = k; // guarda el orden
        tabla = new ABB<Transicion>(); // reinicia la tabla si se quiere entrenar denuevo

        // Recorre el texto con el tamaño de k
        for (int i = 0; i + k < texto.length(); i++) {
            String estado = texto.substring(i, i + k); //Extraemos el estado (los k caracteres desde la posición i)
            Transicion t = buscar(estado); // Buscamos si el estado ya existe en el ABB
            // Si no existe la creamos en la ABB
            if (t == null) {
                t = new Transicion(estado);
                tabla.insertar(t);
            }
            t.sig.agregar(texto.charAt(i + k)); // Agregamos la letra que sigue a la lista de sucesores
        }
    }
    /* Metodo buscas
    * Busca un estado en el ABB. Devuelve la Transicion si existe, o null si no.
     Crea una Transicion "falsa" solo para usarla como clave de búsqueda*/
    private Transicion buscar(String e) {
        return tabla.obtener(new Transicion(e));
    }
    // Método público que genera una nueva secuencia de texto de hasta max caracteres
    public String generar(int max) {
        if (corpus.trim().length() == 0) return "La mazmorra permanece en silencio."; // Si el texto esta vacio o solo tiene espacios devuelve un mensaje
        if (corpus.length() <= orden) return corpus; // Si el corpus es menor que k no se puede hacer nada
        int inicio = azar.nextInt(corpus.length() - orden + 1); // elige un lugar random para saber el estado iniciar
        String estado = corpus.substring(inicio, inicio + orden); // Extrae el estado inicial de longitud orden desde la posición elegida.
        StringBuilder salida = new StringBuilder(estado); // Empezamos el texto de salida con ese estado inicial
        // Bucle que intenta generar max caracteres nuevos, uno por cada iteración.
        for (int i = 0; i < max; i++) {
            Transicion t = buscar(estado); // Busca en el ABB la transición correspondiente al estado actual.
            // Si el estado no existe en el arbol o no tiene letras sucesoras
            if (t == null || t.sig.estaVacia()) {
                // Intenta reducir el contexto , ejemplo si el estado es 'zql' saca z y queda 'ql'
                if (estado.length() > 1) {
                    estado = estado.substring(1); // Quita esa primera letra
                    continue; // volvemos al inicio del for sin agregar esa letra
                }
                // Pero si con ni con una letra a salido, se elige otra inicio del corpus
                inicio = azar.nextInt(corpus.length() - orden + 1);
                estado = corpus.substring(inicio, inicio + orden);
                continue;
            }
            // En caso de que la generacion sea normal
            // Elegimos una letra al azar de la lista de sucesores
            char c = t.sig.obtener(azar.nextInt(t.sig.tamanio()));
            salida.append(c); // Agregamos el texto de salida
            estado = salida.substring(salida.length() - orden); // actualizamos, tomamos los últimos 'k' caracteres del texto generado
        }
        //Si el texto queda vacio o con espacios, se devuelve un mensaje
        String resultado = salida.toString();
        return resultado.trim().length() == 0 ? "Ecos antiguos recorren la sala." : resultado;
    }

    public int getOrden() {
        return orden;
    }
    // devuelve cuántos estados distintos tiene el modelo, tamaño del ABB
    public int cantidadEstados() {
        return tabla.tamanio();
    }
}
