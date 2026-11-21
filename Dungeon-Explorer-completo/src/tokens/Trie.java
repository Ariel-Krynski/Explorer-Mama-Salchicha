package tokens;

import estructuras.Lista;
import estructuras.ListaEnlazada;


// Trie de caracteres. Permite guardar palabras y buscar coincidencias más largas.

public class Trie {

    // Clase interna que representa cada nodo del Trie
    private static class Nodo {
        char c;       // Caracter que guarda el nodo
        boolean fin;  // Indica si en este nodo termina una palabra
        Nodo hijos;   // Primer hijo del nodo
        Nodo sig;     // Siguiente hermano del nodo

        Nodo(char x) {
            c = x;
        }
    }

    // Nodo inicial del Trie, que no representa ninguna letra
    private Nodo raiz = new Nodo('\0');

    // Busca si un nodo tiene un hijo con el caracter indicado
    private Nodo hijo(Nodo n, char c) {

        // Recorremos los hijos del nodo actual
        for (Nodo a = n.hijos; a != null; a = a.sig)

            // Si encontramos el caracter, devolvemos ese nodo
            if (a.c == c) return a;

        // Si no encontramos el caracter, devolvemos null
        return null;
    }

    // Agrega una palabra al Trie
    public void insertar(String palabra) {

        // Si la palabra es nula o está vacía, no hacemos nada
        if (palabra == null || palabra.length() == 0) return;

        Nodo n = raiz;

        // Recorremos todos los caracteres de la palabra
        for (int i = 0; i < palabra.length(); i++) {

            // Convertimos el caracter a minúscula
            char c = Character.toLowerCase(palabra.charAt(i));

            // Buscamos si ya existe un hijo con ese caracter
            Nodo h = hijo(n, c);

            // Si no existe, creamos un nuevo nodo
            if (h == null) {
                h = new Nodo(c);

                // Lo agregamos al principio de la lista de hijos
                h.sig = n.hijos;
                n.hijos = h;
            }

            // Avanzamos al siguiente nodo
            n = h;
        }

        // Indicamos que en este nodo termina una palabra
        n.fin = true;
    }

    // Comprueba si una palabra completa existe en el Trie
    public boolean buscar(String s) {

        // Buscamos el último nodo correspondiente a la palabra
        Nodo n = caminar(s);

        // Devolvemos true solamente si existe y marca el final de una palabra
        return n != null && n.fin;
    }

    // Comprueba si existe un prefijo en el Trie
    public boolean buscarPrefijo(String s) {

        // Si podemos llegar al último caracter, el prefijo existe
        return caminar(s) != null;
    }

    // Recorre el Trie siguiendo los caracteres de una cadena
    private Nodo caminar(String s) {

        // Si la cadena es nula, no podemos buscarla
        if (s == null) return null;

        Nodo n = raiz;

        // Recorremos todos los caracteres de la cadena
        for (int i = 0; i < s.length(); i++) {

            // Buscamos el hijo que tenga el caracter correspondiente
            n = hijo(n, Character.toLowerCase(s.charAt(i)));

            // Si no existe, significa que no encontramos el recorrido
            if (n == null) return null;
        }

        // Devolvemos el último nodo encontrado
        return n;
    }

    // Divide un texto utilizando las palabras almacenadas en el Trie
    public Lista<String> tokenizar(String texto) {

        // Creamos una lista para guardar los resultados
        Lista<String> r = new ListaEnlazada<String>();

        // Si el texto es nulo, devolvemos la lista vacía
        if (texto == null) return r;

        int pos = 0;

        // Recorremos el texto mientras queden caracteres
        while (pos < texto.length()) {

            // Si encontramos un espacio en blanco, lo salteamos
            if (Character.isWhitespace(texto.charAt(pos))) {
                pos++;
                continue;
            }

            Nodo n = raiz;

            // Guarda la posición de la coincidencia más larga encontrada
            int mejor = -1;

            // Guarda el texto correspondiente a esa coincidencia
            String token = "";

            // Intentamos encontrar la coincidencia más larga desde la posición actual
            for (int j = pos; j < texto.length(); j++) {

                // Buscamos el siguiente caracter en el Trie
                n = hijo(n, Character.toLowerCase(texto.charAt(j)));

                // Si no existe el camino, dejamos de buscar
                if (n == null) break;

                // Si encontramos el final de una palabra, guardamos la coincidencia
                if (n.fin) {
                    mejor = j;

                    // Guardamos el fragmento original del texto
                    token = texto.substring(pos, j + 1);
                }
            }

            // Si encontramos una palabra, agregamos la coincidencia más larga
            if (mejor >= pos) {
                r.agregar(token);

                // Continuamos después del último caracter encontrado
                pos = mejor + 1;
            }

            // Si no encontramos ninguna palabra, agregamos un solo caracter
            else {
                r.agregar(texto.substring(pos, pos + 1));

                // Avanzamos una posición
                pos++;
            }
        }

        return r;
    }
}