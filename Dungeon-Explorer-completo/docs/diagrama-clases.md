# Diagrama de clases y paquetes

```mermaid
flowchart TD
  Main[juego.Main] --> Menu[juego.pantallas.MenuEscena]
  Menu --> Juego[juego.pantallas.JuegoEscena]
  Juego --> Fin[juego.pantallas.FinEscena]
  Juego --> Partida[juego.reglas.Partida]
  Juego --> Servicios[juego.reglas.ServiciosMazmorra]
  Servicios --> Arbol[arboles.ArbolBinario]
  Servicios --> Grafo[grafos.Grafo]
  Servicios --> Markov[markov.ModeloMarkov]
  Servicios --> Trie[tokens.Trie]
  Servicios --> Persistencia[persistencia.AlmacenJuego]
  Markov --> ABB[arboles.ABB]
  Arbol --> Lista[estructuras.Lista]
  Grafo --> Lista
  Grafo --> Cola[estructuras.Cola]
  Grafo --> Pila[estructuras.Pila]
  ABB --> Pila
  Arbol --> Cola
  Persistencia --> Arbol
  Persistencia --> Grafo
  Partida --> Objeto[juego.reglas.ObjetoInteractivo]
  Objeto --> Gema[juego.reglas.Gema]
  Objeto --> Amuleto[juego.reglas.Amuleto]
  Gema --> Recompensa[juego.reglas.Recompensa]
  Amuleto --> Recompensa
  Main --> Motor[Kit: motor.Ventana / motor.Escena]
```

Los paquetes del kit `motor`, `contratos`, `ui` y `verificador` se mantienen sin cambios. `estructuras` conserva las interfaces originales y agrega implementaciones enlazadas propias.
