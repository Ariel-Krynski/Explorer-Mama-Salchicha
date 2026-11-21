package tokens;

import estructuras.Lista;
import estructuras.ListaEnlazada;

/**
 * Trie de caracteres; tokenización por coincidencia más larga.
 */
public class Trie {
    private static class Nodo {
        char c;
        boolean fin;
        Nodo hijos;
        Nodo sig;

        Nodo(char x) {
            c = x;
        }
    }

    private Nodo raiz = new Nodo('\0');

    private Nodo hijo(Nodo n, char c) {
        for (Nodo a = n.hijos; a != null; a = a.sig) if (a.c == c) return a;
        return null;
    }

    public void insertar(String palabra) {
        if (palabra == null || palabra.length() == 0) return;
        Nodo n = raiz;
        for (int i = 0; i < palabra.length(); i++) {
            char c = Character.toLowerCase(palabra.charAt(i));
            Nodo h = hijo(n, c);
            if (h == null) {
                h = new Nodo(c);
                h.sig = n.hijos;
                n.hijos = h;
            }
            n = h;
        }
        n.fin = true;
    }

    public boolean buscar(String s) {
        Nodo n = caminar(s);
        return n != null && n.fin;
    }

    public boolean buscarPrefijo(String s) {
        return caminar(s) != null;
    }

    private Nodo caminar(String s) {
        if (s == null) return null;
        Nodo n = raiz;
        for (int i = 0; i < s.length(); i++) {
            n = hijo(n, Character.toLowerCase(s.charAt(i)));
            if (n == null) return null;
        }
        return n;
    }

    public Lista<String> tokenizar(String texto) {
        Lista<String> r = new ListaEnlazada<String>();
        if (texto == null) return r;
        int pos = 0;
        while (pos < texto.length()) {
            if (Character.isWhitespace(texto.charAt(pos))) {
                pos++;
                continue;
            }
            Nodo n = raiz;
            int mejor = -1;
            String token = "";
            for (int j = pos; j < texto.length(); j++) {
                n = hijo(n, Character.toLowerCase(texto.charAt(j)));
                if (n == null) break;
                if (n.fin) {
                    mejor = j;
                    token = texto.substring(pos, j + 1);
                }
            }
            if (mejor >= pos) {
                r.agregar(token);
                pos = mejor + 1;
            } else {
                r.agregar(texto.substring(pos, pos + 1));
                pos++;
            }
        }
        return r;
    }
}
