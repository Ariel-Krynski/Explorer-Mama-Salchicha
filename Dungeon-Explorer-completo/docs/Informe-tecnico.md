# Informe técnico — Dungeon Explorer

## 1. Mecánica y estructuras

El jugador recorre seis habitaciones, abre tres cofres, supera dos criaturas y llega al santuario. Las estructuras no son decorativas: el grafo habilita puertas y calcula rutas; el árbol registra las habitaciones visitadas y condiciona el acceso final; Markov genera el texto de ambientación; el trie valida el comando de interacción.

- **Árbol binario genérico:** almacena IDs de salas visitadas. La inserción recursiva ocupa la siguiente posición de un árbol completo; la forma se conserva al reconstruir desde preorden. El santuario se habilita cuando el árbol contiene todas las salas requeridas y el grafo conserva un ciclo.
- **ABB:** indexa los estados de Markov por su contexto `String`. Los estados repetidos se agrupan; no se insertan claves duplicadas. Las listas de sucesores conservan cada aparición y, por lo tanto, su frecuencia.
- **Trie:** contiene el vocabulario de comandos y términos del mundo. `tokenizar` avanza con la coincidencia más larga; `comandoValido` usa ese resultado para validar `abrir`.
- **Grafo dirigido ponderado:** representa las conexiones entre salas. Una puerta solo permite pasar si existe la arista correspondiente. BFS muestra cuántas salas son alcanzables; DFS está disponible para inspección; Dijkstra calcula una ruta mínima al santuario. La detección de ciclos participa en la condición de apertura final.
- **Markov:** modelo de caracteres con `k` configurable entre 1 y 4. Se entrena desde `datos/corpus.txt`; las descripciones generadas aparecen en pantalla. Con k pequeño hay más variación y menos coherencia local; con k grande se conservan fragmentos más largos del corpus, pero se reducen las alternativas y aumenta la posibilidad de retroceso de contexto.
- **Lista, Pila y Cola:** la lista propia sostiene adyacencias y resultados; la pila permite volver a la sala anterior y también se usa en Dijkstra y ABB; la cola sostiene BFS y recorrido por niveles.

## 2. Persistencia y formatos

Todas las operaciones de archivos están en `persistencia.AlmacenJuego`, con `try-with-resources`.

| Archivo | Formato | Recuperación |
|---|---|---|
| `arbol.txt` | Un ID entero por línea, recorrido preorden | Se leen los IDs válidos en una Lista propia y se reconstruye la forma completa según la cantidad de nodos |
| `grafo.txt` | `origen;destino;peso`, una arista dirigida por línea | Las líneas dañadas se informan y se omiten; si falta el archivo se crea el grafo inicial |
| `corpus.txt` | Texto libre de lore | Si falta o queda vacío se utiliza un corpus inicial |
| `ranking.txt` | Un entero: mejor puntaje | Si falta se parte de cero; si está dañado se informa y se parte de cero |

Antes de cada sobrescritura se copia el archivo existente a `nombre.bak`. No se usa serialización.

## 3. Costos Big-O

Sea `n` la cantidad de elementos de una lista, `V` los vértices, `E` las aristas, `S` los estados de Markov, `L` la longitud de una palabra y `N` la cantidad de caracteres del texto.

| Estructura / operación | Costo | Observación |
|---|---:|---|
| Lista enlazada: agregar al final | O(1) | Mantiene referencia al último nodo |
| Lista enlazada: obtener / insertar / eliminar por índice | O(n) | Búsqueda lineal del nodo |
| Pila: apilar / desapilar / tope | O(1) | Operaciones en el primer nodo |
| Cola: encolar / desencolar / frente | O(1) | Referencias a frente y fin |
| Árbol binario: insertar en siguiente posición completa | O(log n) | Sigue recursivamente los bits del índice siguiente |
| Árbol binario: buscar / altura / hojas | O(n) | En el peor caso visita todos los nodos |
| Árbol binario: recorridos | O(n) | Cada nodo se visita una vez |
| ABB: buscar / insertar / eliminar | O(h) | `h` es la altura; peor caso O(n), balanceado O(log n) |
| ABB: in-order iterativo | O(n) | Pila auxiliar O(h) |
| Trie: insertar / buscar / prefijo | O(L·A) | `A` es la cantidad máxima de hermanos revisados por carácter |
| Trie: tokenizar | O(N·L·A) peor caso | En cada posición puede avanzar hasta la longitud máxima de token |
| Grafo: BFS / DFS | O(V+E) | Lista de adyacencia y visitados |
| Grafo: Dijkstra | O((V+E)·V) con lista de prioridad enlazada ordenada | Inserción ordenada O(V), extracción O(1) |
| Grafo: detectar ciclo | O(V+E) | DFS con marcas de visitado y pila de recursión |
| Markov: entrenamiento | O(N·(h+f)) | `h` altura ABB y `f` costo de agregar sucesor a lista |
| Markov: buscar estado | O(h) | Búsqueda en ABB |
| Markov: generar m caracteres | O(m·(h+f)) | Consulta de estado y elección de sucesor |

## 4. Markov: comparación de k

En la partida se puede presionar 1, 2, 3 o 4 para reentrenar el modelo con el mismo corpus y generar otra descripción. `k=1` condiciona cada carácter solo por el anterior y permite más variación; `k=2` usa pares de caracteres; `k=3` y `k=4` retienen contextos más largos y suelen reproducir secuencias más parecidas al corpus. El modelo evita salida vacía: ante un estado sin sucesores reduce el contexto y, si hace falta, reinicia desde una posición válida del corpus.

## 5. Decisiones de implementación

- En el ABB, los estados repetidos no crean otro nodo; cada sucesor se agrega a la lista y la repetición conserva su peso estadístico.
- En Dijkstra, los pesos deben ser no negativos. Se utiliza una lista propia ordenada por distancia; los empates conservan el orden de inserción.
- En el grafo, los pares origen-destino son únicos; reconectar una arista actualiza su peso.
- En el ranking solo se conserva el máximo. Los archivos de respaldo se actualizan antes de sobrescribir.
- El tokenizador devuelve el token más largo reconocido desde cada posición. Si no hay coincidencia, conserva el carácter como token para no perder información.

## 6. Alcance del extra BPE

El enunciado plantea BPE como trabajo extra. No se implementó para priorizar todos los requisitos obligatorios; el tokenizador obligatorio por trie sí está integrado y se utiliza al validar la interacción.

## 7. Pruebas

`pruebas.PruebaEstructuras` cubre árbol vacío y de un nodo, recorridos, ABB y eliminación, grafo sin aristas, camino inexistente, BFS/DFS, Dijkstra, trie exacto/prefijo/sin coincidencias/coincidencia más larga, Markov y estado sin sucesores. Además ejecuta el verificador original del kit para Lista, Pila y Cola.
