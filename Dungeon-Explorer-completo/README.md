# Dungeon Explorer — Game Jam Java II

Videojuego 2D de exploración desarrollado sobre el kit entregado para la Game Jam. Requiere **JDK 11 o superior**.

## Compilar y ejecutar

- Linux/macOS: `./compilar.sh` y luego `./ejecutar.sh`
- Windows: `compilar.bat` y luego `ejecutar.bat`
- Desde un IDE: compilar `src` como carpeta de fuentes y ejecutar `juego.Main`.
- Pruebas: `java -cp out pruebas.PruebaEstructuras`

El ejecutable por defecto ahora inicia Dungeon Explorer. Para abrir la demo original del kit: `./ejecutar.sh demo.Main`.

## Cómo jugar

- Flechas o WASD: caminar.
- E: interactuar con cofres, criaturas y santuario.
- P: pausar.
- Backspace: volver a la habitación anterior (pila).
- 1, 2, 3 o 4: cambiar el orden k del modelo de Markov y generar otra descripción.
- ESC: volver al menú y guardar.

Hay seis habitaciones conectadas. Reuní las tres gemas, recorré todas las salas y llegá al santuario. Las puertas habilitadas y la ruta sugerida dependen del grafo persistido; la exploración queda registrada en el árbol.

## Estructuras y uso real

| Paquete | Estructura | Uso dentro del juego |
|---|---|---|
| `estructuras` | `ListaEnlazada`, `PilaEnlazada`, `ColaEnlazada` | Lista de adyacencia y resultados; deshacer habitación; BFS y recorrido por niveles |
| `arboles` | `ArbolBinario<T>` | Registro de salas visitadas, condición para habilitar el santuario y persistencia |
| `arboles` | `ABB<T>` | Índice de estados del modelo de Markov y recorridos in-order iterativos |
| `tokens` | `Trie` | Validación y tokenización por coincidencia más larga del comando de interacción |
| `grafos` | `Grafo<T>` | Puertas reales, BFS de salas alcanzables, DFS, Dijkstra y detección de ciclos |
| `markov` | `ModeloMarkov` | Descripciones de habitaciones visibles; órdenes k=1..4 seleccionables |
| `persistencia` | `AlmacenJuego` | Lectura/escritura tolerante de los cuatro archivos y backups |

`Pila` y `Cola` conservan sus interfaces del kit y se implementan en clases enlazadas propias. La interfaz `Lista` se amplió con `Iterable`, `contiene` y `quitar`: esos métodos ya estaban implementados en `ListaBase` y eran requeridos por `motor.Mundo`, pero faltaban en la interfaz original; esta corrección permite compilar el kit sin tocar `motor`.

## Persistencia

Los archivos se encuentran en `datos/` y son texto plano:

- `arbol.txt`: un identificador de sala por línea, en preorden. La forma completa del árbol se reconstruye a partir de la cantidad de nodos y el orden preorden.
- `grafo.txt`: una arista dirigida por línea: `origen;destino;peso`.
- `corpus.txt`: corpus de lore que entrena Markov.
- `ranking.txt`: mejor puntaje alcanzado.

Antes de sobrescribir cada archivo se copia a su correspondiente `.bak`. Si falta un archivo se usan datos iniciales. Las líneas inválidas se informan en el menú y se omiten; el juego continúa.

## Documentación de entrega

- `docs/Informe-tecnico.md`: formatos, decisiones y análisis Big-O.
- `docs/diagrama-clases.md`: diagrama de paquetes y dependencias.
- `src/pruebas/PruebaEstructuras.java`: batería propia de 30+ verificaciones y ejecución del verificador del kit para Lista, Pila y Cola.

## Restricciones

El proyecto utiliza Java estándar y AWT/Swing del kit. No utiliza colecciones de `java.util`, `java.nio.file` ni serialización de objetos. BPE aparece como extra en el enunciado y no se implementó; sí está implementado el trie obligatorio. Los arreglos de trabajo se limitan a los TADs (árbol y grafo), al código preexistente del motor y al `split` de lectura de aristas. No se modificaron los paquetes `motor`, `contratos`, `ui` ni `verificador`.
