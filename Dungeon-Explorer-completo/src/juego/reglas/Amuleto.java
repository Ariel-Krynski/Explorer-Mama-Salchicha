package juego.reglas;
public final class Amuleto extends ObjetoInteractivo implements Recompensa{public Amuleto(){super("Amuleto");}public int activar(){return otorgar();}public int otorgar(){return 125;}}
