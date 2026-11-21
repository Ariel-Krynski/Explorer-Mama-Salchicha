package juego.reglas;
public final class Gema extends ObjetoInteractivo implements Recompensa{public Gema(){super("Gema");}public int activar(){return otorgar();}public int otorgar(){return 100;}}
