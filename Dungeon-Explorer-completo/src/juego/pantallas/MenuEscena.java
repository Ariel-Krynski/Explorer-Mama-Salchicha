package juego.pantallas;

import java.awt.*;
import java.awt.event.KeyEvent;
import motor.Escena;
import ui.Texto;
import juego.reglas.Partida;
import juego.reglas.ServiciosMazmorra;

public class MenuEscena extends Escena {
    private Rectangle jugar = new Rectangle(365, 345, 230, 58);
    private Rectangle salir = new Rectangle(365, 420, 230, 52);
    @Override public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER) || (getEntrada().huboClick() && jugar.contains(getEntrada().getMouseX(), getEntrada().getMouseY())))
            getContexto().irA(new JuegoEscena(new Partida()));
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE) || (getEntrada().huboClick() && salir.contains(getEntrada().getMouseX(), getEntrada().getMouseY()))) getContexto().salir();
    }
    @Override public void dibujar(Graphics2D g) {
        int w=getContexto().getAncho(), h=getContexto().getAlto();
        fondo(g,w,h);
        Texto.centrado(g,"EXPLORA",w/2,190,48,new Color(255,226,153));
        Texto.centrado(g,"MAMÁ SALCHICHA",w/2,232,20,new Color(218,205,235));
        dibujarBoton(g,jugar,"COMENZAR",new Color(126,82,177)); dibujarBoton(g,salir,"SALIR",new Color(76,65,93));
        Texto.centrado(g,"Flechas / WASD: mover     E: interactuar     ESC: menú",w/2,550,15,new Color(202,194,216));
        Texto.centrado(g,"Récord: "+Partida.leerRecord(),w/2,585,16,new Color(255,220,130));
        String aviso=ServiciosMazmorra.obtener().avisosDatos(); if(aviso.length()>0) Texto.centrado(g,"Aviso de datos: "+aviso,w/2,620,11,new Color(255,170,150));
        dibujarEstrellas(g);
    }
    static void fondo(Graphics2D g,int w,int h) {
        g.setPaint(new GradientPaint(0,0,new Color(25,20,48),0,h,new Color(57,39,76))); g.fillRect(0,0,w,h);
        g.setColor(new Color(105,82,133,55)); for(int y=0;y<h;y+=48) g.drawLine(0,y,w,y);
        for(int x=0;x<w;x+=48) g.drawLine(x,0,x,h);
        g.setColor(new Color(15,12,29,150)); g.fillRoundRect(130,95,700,430,36,36);
        g.setColor(new Color(173,139,199)); g.setStroke(new BasicStroke(3)); g.drawRoundRect(130,95,700,430,36,36);
    }
    static void dibujarBoton(Graphics2D g,Rectangle r,String s,Color c) {
        g.setColor(new Color(0,0,0,80));g.fillRoundRect(r.x+4,r.y+6,r.width,r.height,18,18);
        g.setColor(c);g.fillRoundRect(r.x,r.y,r.width,r.height,18,18);
        g.setColor(new Color(235,218,255));g.setStroke(new BasicStroke(2));g.drawRoundRect(r.x,r.y,r.width,r.height,18,18);
        Texto.centrado(g,s,r.getCenterX(),(int)r.getCenterY()+7,21,Color.WHITE);
    }
    private void dibujarEstrellas(Graphics2D g) {
        g.setColor(new Color(255,224,150)); int x=220; for(int i=0;i<7;i++){int y=150+(i*67)%310;g.fillOval(x,y,4,4);g.drawLine(x-5,y+2,x+9,y+2);g.drawLine(x+2,y-5,x+2,y+9);x+=87;}
    }
}
