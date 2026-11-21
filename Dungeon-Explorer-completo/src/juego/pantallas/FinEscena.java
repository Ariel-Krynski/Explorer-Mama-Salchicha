package juego.pantallas;

import java.awt.*;
import java.awt.event.KeyEvent;
import juego.reglas.Partida;
import motor.Escena;
import ui.Texto;

public class FinEscena extends Escena {
    private final Partida partida; private final boolean victoria;
    private Rectangle volver=new Rectangle(350,390,260,58);
    public FinEscena(Partida partida,boolean victoria){this.partida=partida;this.victoria=victoria;}
    @Override public void actualizar(double dt){
        if(getEntrada().fuePresionada(KeyEvent.VK_ENTER)||(getEntrada().huboClick()&&volver.contains(getEntrada().getMouseX(),getEntrada().getMouseY())))getContexto().irA(new JuegoEscena(new Partida()));
        if(getEntrada().fuePresionada(KeyEvent.VK_ESCAPE))getContexto().irA(new MenuEscena());
    }
    @Override public void dibujar(Graphics2D g){
        MenuEscena.fondo(g,960,640);
        Texto.centrado(g,victoria?"":"FIN DE LA AVENTURA",480,205,38,victoria?new Color(255,220,130):new Color(245,160,170));
        Texto.centrado(g,victoria?"Recuperaste los cachorros":"Mamá Salchicha lo ha logrado",480,255,20,new Color(224,211,239));
        Texto.centrado(g,"Puntaje: "+partida.getPuntaje(),480,315,28,Color.WHITE);
        Texto.centrado(g,"Perritos encontrados: "+partida.getCristales()+" / 3",480,350,18,new Color(255,220,133));
        MenuEscena.dibujarBoton(g,volver,"JUGAR OTRA VEZ",new Color(126,82,177));
        Texto.centrado(g,"ENTER: volver a jugar     ESC: menú",480,500,16,new Color(210,198,226));
    }
}
