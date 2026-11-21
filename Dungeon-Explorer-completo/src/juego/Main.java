package juego;

import motor.Ventana;
import juego.pantallas.MenuEscena;
import juego.reglas.ServiciosMazmorra;

public class Main {
    public static void main(String[] args) {
        ServiciosMazmorra.obtener();
        Ventana.abrir("Dungeon Explorer", 960, 640, new MenuEscena());
    }
}
