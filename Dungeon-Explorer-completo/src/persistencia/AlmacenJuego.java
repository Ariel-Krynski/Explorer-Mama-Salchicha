package persistencia;

import java.io.*;

import arboles.ArbolBinario;
import grafos.Grafo;
import estructuras.Lista;

/**
 * Persistencia de texto. Formatos: arbol.txt = un entero por línea en preorden; como el árbol se inserta en forma completa, su cantidad de nodos determina la forma y el preorden permite reconstruirla; grafo.txt = origen;destino;peso; corpus.txt = texto libre; ranking.txt = entero. Antes de sobrescribir se copia a .bak.
 */
public class AlmacenJuego {
    private final File dir = new File("datos");
    private String avisos = "";

    private void avisar(String s) {
        avisos += s + " | ";
        System.err.println(s);
    }

    public String getAvisos() {
        return avisos;
    }

    public AlmacenJuego() {
        if (!dir.exists()) dir.mkdirs();
    }

    private void backup(String nombre) throws IOException {
        File f = new File(dir, nombre);
        if (!f.exists()) return;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8")); BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, nombre + ".bak")), "UTF-8"))) {
            String s;
            while ((s = in.readLine()) != null) {
                out.write(s);
                out.newLine();
            }
        }
    }

    public void guardarArbol(ArbolBinario<Integer> a) {
        String n = "arbol.txt";
        try {
            backup(n);
            try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, n)), "UTF-8"))) {
                Lista<Integer> l = a.preOrden();
                for (int i = 0; i < l.tamanio(); i++) {
                    w.write(String.valueOf(l.obtener(i)));
                    w.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar arbol.txt: " + e.getMessage());
        }
    }

    public ArbolBinario<Integer> leerArbol() {
        ArbolBinario<Integer> a = new ArbolBinario<Integer>();
        Lista<Integer> valores = new estructuras.ListaEnlazada<Integer>();
        File f = new File(dir, "arbol.txt");
        if (!f.exists()) return a;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String s;
            while ((s = r.readLine()) != null) try {
                valores.agregar(Integer.valueOf(s.trim()));
            } catch (NumberFormatException e) {
                avisar("Línea dañada en arbol.txt: " + s);
            }
        } catch (IOException e) {
            avisar("No se pudo leer arbol.txt: " + e.getMessage());
        }
        a.reconstruirDesdePreOrden(valores);
        return a;
    }

    public void guardarGrafo(Grafo<Integer> g) {
        String n = "grafo.txt";
        try {
            backup(n);
            try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, n)), "UTF-8"))) {
                for (int i = 0; i < g.cantidadVertices(); i++)
                    for (int j = 0; j < g.cantidadVertices(); j++)
                        if (g.adyacentes(g.vertice(i), g.vertice(j))) {
                            w.write(g.vertice(i) + ";" + g.vertice(j) + ";" + g.peso(g.vertice(i), g.vertice(j)));
                            w.newLine();
                        }
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar grafo.txt: " + e.getMessage());
        }
    }

    public void cargarGrafo(Grafo<Integer> g) {
        File f = new File(dir, "grafo.txt");
        if (!f.exists()) {
            grafoInicial(g);
            return;
        }
        for (int i = 0; i < 6; i++) g.agregarVertice(i);
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String s;
            while ((s = r.readLine()) != null) {
                try {
                    String[] p = s.split(";");
                    if (p.length != 3) throw new NumberFormatException();
                    g.conectar(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Double.parseDouble(p[2]));
                } catch (Exception e) {
                    avisar("Línea dañada en grafo.txt: " + s);
                }
            }
        } catch (IOException e) {
            avisar("No se pudo leer grafo.txt: " + e.getMessage());
            if (g.cantidadAristas() == 0) grafoInicial(g);
            return;
        }
        if (g.cantidadAristas() == 0) grafoInicial(g);
    }

    public void guardarCorpus(String corpus) {
        String n = "corpus.txt";
        try {
            backup(n);
            try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, n)), "UTF-8"))) {
                w.write(corpus);
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar corpus.txt: " + e.getMessage());
        }
    }

    public String leerCorpus() {
        File f = new File(dir, "corpus.txt");
        if (!f.exists())
            return "La cripta guarda secretos antiguos. La gema ilumina la puerta. Una sombra protege el tesoro. El héroe busca la salida.";
        StringBuilder b = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String s;
            while ((s = r.readLine()) != null) b.append(s).append(' ');
        } catch (IOException e) {
            avisar("No se pudo leer corpus.txt: " + e.getMessage());
        }
        return b.length() == 0 ? "La cripta guarda secretos antiguos." : b.toString();
    }

    public void guardarRanking(int puntaje) {
        String n = "ranking.txt";
        int anterior = leerRanking();
        if (puntaje <= anterior) return;
        try {
            backup(n);
            try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, n)), "UTF-8"))) {
                w.write(String.valueOf(puntaje));
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar ranking.txt: " + e.getMessage());
        }
    }

    public int leerRanking() {
        File f = new File(dir, "ranking.txt");
        if (!f.exists()) return 0;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String s = r.readLine();
            return s == null ? 0 : Integer.parseInt(s.trim());
        } catch (Exception e) {
            avisar("Ranking dañado; se inicia en cero.");
            return 0;
        }
    }

    public static void grafoInicial(Grafo<Integer> g) {
        for (int i = 0; i < 6; i++) g.agregarVertice(i);
        g.conectar(0, 1, 1);
        g.conectar(1, 0, 1);
        g.conectar(1, 2, 2);
        g.conectar(2, 1, 2);
        g.conectar(1, 3, 1);
        g.conectar(3, 1, 1);
        g.conectar(3, 4, 2);
        g.conectar(4, 3, 2);
        g.conectar(4, 5, 1);
        g.conectar(5, 4, 1);
    }
}
