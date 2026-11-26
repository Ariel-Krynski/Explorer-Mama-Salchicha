package juego.reglas;

// Es un tipo de error propio del juego que hereda de RuntimeException: sirve para
// lanzar avisos o fallos de la partida pasando un mensaje de texto.
public class ErrorPartida extends RuntimeException {
    public ErrorPartida(String mensaje) {
        super(mensaje);
    }
}
