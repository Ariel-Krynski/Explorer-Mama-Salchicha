
package juego.pantallas;

import java.awt.*;
import java.awt.event.KeyEvent;
import juego.reglas.Partida;
import motor.Escena;
import ui.Texto;
import juego.reglas.ServiciosMazmorra;
import estructuras.Lista;
import estructuras.ListaEnlazada;

/** Escena principal: exploración top-down con habitaciones conectadas. */
public class JuegoEscena extends Escena {
    private final Partida partida;
    private double px=480, py=330;
    private double invulnerable=0;
    private String aviso="¡Busca a los tres cachorritos perdidos!";
    private String rutaSantuario="";
    private double tiempoAviso=4;
    private boolean pausa=false;
    private static final int IZQ=0, DER=1, ARR=2, ABA=3;

    public JuegoEscena(Partida partida) { this.partida=partida; }

    @Override
    public void alEntrar() {
        px=480;
        py=330;
        aviso=ServiciosMazmorra.obtener().descripcion();
        tiempoAviso=5;
        ServiciosMazmorra.obtener().visitar(partida.getHabitacion());
        actualizarRutaSantuario();
    }

    @Override
    public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            ServiciosMazmorra.obtener().guardar();
            getContexto().irA(new MenuEscena());
            return;
        }

        if (getEntrada().fuePresionada(KeyEvent.VK_BACK_SPACE)) {
            Integer anterior=ServiciosMazmorra.obtener().deshacer();

            if(anterior!=null) {
                partida.setHabitacion(anterior);
                px=480;
                py=330;
                actualizarRutaSantuario();
                aviso="Volviste a la sala anterior.";
                tiempoAviso=2;
            }
        }

        if (getEntrada().fuePresionada(KeyEvent.VK_P)) pausa=!pausa;

        if (getEntrada().fuePresionada(KeyEvent.VK_1)) {
            ServiciosMazmorra.obtener().cambiarOrden(1);
            aviso=ServiciosMazmorra.obtener().descripcion();
            tiempoAviso=4;
        }

        if (getEntrada().fuePresionada(KeyEvent.VK_2)) {
            ServiciosMazmorra.obtener().cambiarOrden(2);
            aviso=ServiciosMazmorra.obtener().descripcion();
            tiempoAviso=4;
        }

        if (getEntrada().fuePresionada(KeyEvent.VK_3)) {
            ServiciosMazmorra.obtener().cambiarOrden(3);
            aviso=ServiciosMazmorra.obtener().descripcion();
            tiempoAviso=4;
        }

        if (getEntrada().fuePresionada(KeyEvent.VK_4)) {
            ServiciosMazmorra.obtener().cambiarOrden(4);
            aviso=ServiciosMazmorra.obtener().descripcion();
            tiempoAviso=4;
        }

        if (pausa) return;

        if (invulnerable>0) invulnerable-=dt;
        if (tiempoAviso>0) tiempoAviso-=dt;

        double velocidad=205, dx=0, dy=0;

        if (getEntrada().estaPresionada(KeyEvent.VK_LEFT)||getEntrada().estaPresionada(KeyEvent.VK_A))
            dx-=velocidad*dt;

        if (getEntrada().estaPresionada(KeyEvent.VK_RIGHT)||getEntrada().estaPresionada(KeyEvent.VK_D))
            dx+=velocidad*dt;

        if (getEntrada().estaPresionada(KeyEvent.VK_UP)||getEntrada().estaPresionada(KeyEvent.VK_W))
            dy-=velocidad*dt;

        if (getEntrada().estaPresionada(KeyEvent.VK_DOWN)||getEntrada().estaPresionada(KeyEvent.VK_S))
            dy+=velocidad*dt;

        mover(dx,0);
        mover(0,dy);

        revisarPuertas();
        revisarEnemigo();

        if (getEntrada().fuePresionada(KeyEvent.VK_E) &&
                ServiciosMazmorra.obtener().comandoValido("abrir"))
            interactuar();

        if (partida.estaPerdida()) {
            partida.guardarRecord();
            getContexto().irA(new FinEscena(partida,false));
        }

        if (partida.estaGanada()) {
            partida.guardarRecord();
            getContexto().irA(new FinEscena(partida,true));
        }
    }

    private void mover(double dx,double dy) {
        double nx=px+dx, ny=py+dy;

        if (nx<108 || nx>852) {
            int dir=nx<108?IZQ:DER;
            if (Math.abs(py-330)>54 || destino(dir)<0) nx=px;
        }

        if (ny<118 || ny>542) {
            int dir=ny<118?ARR:ABA;
            if (Math.abs(px-480)>54 || destino(dir)<0) ny=py;
        }

        // Pilares y escombros: obstáculos simples para dar forma al recorrido.
        if (partida.getHabitacion()==2 && colisiona(nx,ny,350,250,90,55)) nx=px;
        if (partida.getHabitacion()==2 && colisiona(nx,ny,350,250,90,55)) ny=py;

        if (partida.getHabitacion()==4 && colisiona(nx,ny,560,365,90,60)) {
            nx=px;
            ny=py;
        }

        px=Math.max(105,Math.min(855,nx));
        py=Math.max(115,Math.min(545,ny));
    }

    private boolean colisiona(double x,double y,int rx,int ry,int rw,int rh) {
        return x+12>rx&&x-12<rx+rw&&y+12>ry&&y-12<ry+rh;
    }

    private int destino(int dir) {
        int r=partida.getHabitacion(), candidato=-1;

        if (r==0 && dir==DER) candidato=1;

        if (r==1 && dir==IZQ) candidato=0;
        if (r==1 && dir==ARR) candidato=2;
        if (r==1 && dir==DER) candidato=3;

        if (r==2 && dir==ABA) candidato=1;

        if (r==3 && dir==IZQ) candidato=1;
        if (r==3 && dir==DER) candidato=4;

        if (r==4 && dir==IZQ) candidato=3;
        if (r==4 && dir==ABA) candidato=5;

        if (r==5 && dir==ARR) candidato=4;

        return candidato>=0 &&
                ServiciosMazmorra.obtener().hayPuerta(r,candidato) ? candidato : -1;
    }

    private void revisarPuertas() {
        int dir=-1;

        if(px<108) dir=IZQ;
        else if(px>852) dir=DER;
        else if(py<118) dir=ARR;
        else if(py>542) dir=ABA;

        if(dir>=0) {
            int d=destino(dir);

            if(d>=0) {
                ServiciosMazmorra.obtener().registrarMovimiento(partida.getHabitacion());

                partida.setHabitacion(d);
                ServiciosMazmorra.obtener().visitar(d);

                Lista<Integer> alcanzables=ServiciosMazmorra.obtener().recorridoBfs(d);
                Lista<Integer> profundidad=ServiciosMazmorra.obtener().recorridoDfs(d);

                actualizarRutaSantuario();

                if(dir==DER) px=130;
                if(dir==IZQ) px=830;
                if(dir==ARR) py=530;
                if(dir==ABA) py=130;

                aviso="Cámara "+(d+1)+" descubierta.";
                tiempoAviso=2.5;
            }
        }
    }

    // Calcula y guarda la ruta de menor costo desde la cámara actual hasta el Santuario.
    private void actualizarRutaSantuario() {
        Lista<Integer> ruta=ServiciosMazmorra.obtener().rutaSalida(partida.getHabitacion());
        rutaSantuario=formatearRuta(ruta);
    }

    // Convierte los identificadores devueltos por Dijkstra en números de cámaras.
    private String formatearRuta(Lista<Integer> ruta) {
        if (ruta.estaVacia())
            return "Ruta al Santuario: no hay un camino disponible";

        String texto="Ruta al Santuario: ";

        for(int i=0;i<ruta.tamanio();i++) {
            if(i>0) texto+=" > ";
            texto+="Cámara "+(ruta.obtener(i)+1);
        }

        return texto;
    }

    private void revisarEnemigo() {
        int r=partida.getHabitacion();

        if ((r==3||r==5) && !partida.enemigoDerrotado(r)) {
            double ex=r==3?680:300, ey=r==3?315:350;

            if(Math.hypot(px-ex,py-ey)<36 && invulnerable<=0) {
                partida.recibirDanio();
                invulnerable=1.5;
                px=480;
                py=330;
                aviso="¡Te golpeó una criatura! Perdiste una vida.";
                tiempoAviso=2.5;
            }
        }
    }

    private void interactuar() {
        int r=partida.getHabitacion();

        if ((r==1||r==2||r==4) && !partida.tieneCofreAbierto(r)) {
            double cx=r==1?690:(r==2?480:260);
            double cy=r==1?330:(r==2?220:420);

            if(Math.hypot(px-cx,py-cy)<75) {
                partida.abrirCofre(r);
                aviso="¡Rescataste a un cachorrito! Ya viaja en tu lomo. +100 puntos";
                tiempoAviso=3;
                return;
            }
        }

        if ((r==3||r==5) && !partida.enemigoDerrotado(r)) {
            double ex=r==3?680:300, ey=r==3?315:350;

            if(Math.hypot(px-ex,py-ey)<85) {
                partida.derrotarEnemigo(r);
                aviso="¡La criatura se disipó! +150 puntos";
                tiempoAviso=3;
                return;
            }
        }

        if(r==5 && Math.hypot(px-480,py-300)<95) {
            if(partida.getCristales()>=3 &&
                    ServiciosMazmorra.obtener().exploroTodasLasSalas()) {
                partida.ganar();
                aviso="¡La familia está reunida! ¡Llegaron a casa!";
            }
            else if(partida.getCristales()<3) {
                aviso="Faltan "+(3-partida.getCristales())+" cachorritos para reunir a la familia.";
                tiempoAviso=3;
            }
            else {
                aviso="El árbol de exploración indica que aún faltan salas por visitar.";
                tiempoAviso=3;
            }

            return;
        }

        aviso="No hay nada para usar aquí. Acércate a un objeto.";
        tiempoAviso=2;
    }

    @Override
    public void dibujar(Graphics2D g) {
        dibujarHabitacion(g);
        dibujarObjetos(g);
        dibujarJugador(g);
        dibujarHud(g);

        if(pausa) {
            g.setColor(new Color(10,8,20,190));
            g.fillRect(0,0,960,640);
            Texto.centrado(g,"PAUSA",480,300,42,Color.WHITE);
            Texto.centrado(g,"Presiona P para continuar",480,340,18,new Color(220,210,240));
        }
    }

    private void dibujarHabitacion(Graphics2D g) {
        int r=partida.getHabitacion();

        g.setPaint(new GradientPaint(0,0,new Color(42,39,66),0,640,new Color(25,26,43)));
        g.fillRect(0,0,960,640);

        // Suelo de piedra.
        for(int y=120;y<545;y+=48) {
            for(int x=110;x<850;x+=64) {
                g.setColor(((x/64+y/48)%2==0)?new Color(70,66,91):new Color(64,61,83));
                g.fillRoundRect(x,y,62,46,7,7);
                g.setColor(new Color(115,105,133,80));
                g.drawRoundRect(x,y,62,46,7,7);
            }
        }

        // Paredes con huecos para las puertas.
        g.setColor(new Color(35,29,52));
        g.fillRect(75,75,810,42);
        g.fillRect(75,543,810,42);
        g.fillRect(75,75,40,510);
        g.fillRect(845,75,40,510);

        g.setColor(new Color(135,111,157));
        g.setStroke(new BasicStroke(5));
        g.drawRect(78,78,804,504);

        puerta(g,IZQ,destino(IZQ)>=0);
        puerta(g,DER,destino(DER)>=0);
        puerta(g,ARR,destino(ARR)>=0);
        puerta(g,ABA,destino(ABA)>=0);

        // Antorchas.
        antorcha(g,145,145);
        antorcha(g,815,145);
        antorcha(g,145,500);
        antorcha(g,815,500);

        Texto.centrado(g,"CÁMARA "+(r+1),480,105,17,new Color(235,218,255));

        if(r==0)
            Texto.centrado(g,"Una puerta al este te espera",480,485,15,new Color(190,180,210));

        if(r==5)
            Texto.centrado(g,"EL REFUGIO DE LA FAMILIA",480,155,18,new Color(255,220,145));
    }

    private void puerta(Graphics2D g,int d,boolean activa) {
        int x=0,y=0,w=0,h=0;

        if(d==IZQ) { x=76; y=275; w=42; h=110; }
        if(d==DER) { x=842; y=275; w=42; h=110; }
        if(d==ARR) { x=425; y=76; w=110; h=42; }
        if(d==ABA) { x=425; y=542; w=110; h=42; }

        g.setColor(activa?new Color(119,75,145):new Color(30,27,42));
        g.fillRoundRect(x,y,w,h,10,10);

        g.setColor(activa?new Color(239,194,112):new Color(80,70,92));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(x,y,w,h,10,10);

        if(activa) {
            g.setColor(new Color(255,220,150,110));
            g.fillRoundRect(x+8,y+8,w-16,h-16,8,8);
        }
    }

    private void antorcha(Graphics2D g,int x,int y) {
        g.setColor(new Color(255,171,75,35));
        g.fillOval(x-25,y-25,50,50);

        g.setColor(new Color(115,72,48));
        g.fillRect(x-4,y+2,8,19);

        g.setColor(new Color(255,177,72));
        g.fillOval(x-7,y-9,14,18);

        g.setColor(new Color(255,235,160));
        g.fillOval(x-3,y-6,6,10);
    }

    private void dibujarObjetos(Graphics2D g) {
        int r=partida.getHabitacion();

        if(r==1||r==2||r==4) {
            int x=r==1?690:(r==2?480:260);
            int y=r==1?330:(r==2?220:420);

            boolean abierto=partida.tieneCofreAbierto(r);

            g.setColor(new Color(0,0,0,80));
            g.fillOval(x-30,y+18,60,17);

            g.setColor(new Color(116,66,42));
            g.fillRoundRect(x-25,y-5,50,35,8,8);

            g.setColor(new Color(191,126,58));
            g.fillRect(x-25,y-7,50,12);

            g.setColor(new Color(255,211,112));
            g.fillRect(x-3,y-5,7,35);

            g.setColor(new Color(245,203,110));
            g.drawRoundRect(x-25,y-5,50,35,8,8);

            if(abierto) {
                dibujarCachorro(g,x,y-30,0.8);
                g.setColor(new Color(255,245,210));
                g.fillOval(x+13,y-42,5,5);
            } else {
                g.setColor(new Color(91,48,33));
                g.fillRoundRect(x-22,y-19,44,17,6,6);
            }
        }

        if((r==3||r==5)&&!partida.enemigoDerrotado(r))
            dibujarSlime(g,r==3?680:300,r==3?315:350);

        if(r==2) {
            g.setColor(new Color(95,80,111));
            g.fillRoundRect(350,250,90,55,12,12);
            g.setColor(new Color(139,119,151));
            g.drawRoundRect(350,250,90,55,12,12);
        }

        if(r==4) {
            g.setColor(new Color(95,80,111));
            g.fillRoundRect(560,365,90,60,12,12);
            g.setColor(new Color(139,119,151));
            g.drawRoundRect(560,365,90,60,12,12);
        }

        if(r==5) {
            g.setColor(new Color(255,214,119,35));
            g.fillOval(405,225,150,150);

            g.setColor(new Color(130,91,190));
            g.fillOval(435,255,90,90);

            g.setColor(new Color(255,221,139));
            g.fillOval(452,272,56,56);

            g.setColor(new Color(255,255,230));
            g.fillOval(469,289,22,22);

            Texto.centrado(g,"E",480,410,17,Color.WHITE);
        }
    }

    private void dibujarSlime(Graphics2D g,int x,int y) {
        g.setColor(new Color(0,0,0,75));
        g.fillOval(x-29,y+17,58,15);

        g.setColor(new Color(114,185,139));
        g.fillOval(x-27,y-23,54,48);

        g.setColor(new Color(153,222,166));
        g.fillOval(x-18,y-20,34,24);

        g.setColor(new Color(42,61,63));
        g.fillOval(x-12,y-4,6,8);
        g.fillOval(x+7,y-4,6,8);

        g.setColor(Color.WHITE);
        g.fillOval(x-10,y-4,2,3);
        g.fillOval(x+9,y-4,2,3);
    }

    // Dibuja un cachorrito visto desde arriba, para los cofres y el lomo de mamá.
    private void dibujarCachorro(Graphics2D g,int x,int y,double escala) {
        Graphics2D c=(Graphics2D)g.create();

        c.translate(x,y);
        c.scale(escala,escala);

        c.setColor(new Color(0,0,0,55));
        c.fillOval(-10,5,21,8);

        c.setColor(new Color(190,126,67));
        c.fillOval(-9,-5,18,14);

        c.setColor(new Color(218,158,91));
        c.fillOval(1,-8,12,12);

        c.setColor(new Color(125,75,48));
        c.fillOval(1,-10,5,8);
        c.fillOval(9,-10,5,8);

        c.setColor(new Color(45,35,30));
        c.fillOval(9,-4,3,3);

        c.setColor(Color.WHITE);
        c.fillOval(5,-6,3,3);

        c.dispose();
    }

    private void dibujarJugador(Graphics2D g) {
        int x=(int)px,y=(int)py;

        if(invulnerable>0 && ((int)(invulnerable*12)%2==0))
            return;

        g.setColor(new Color(0,0,0,85));
        g.fillOval(x-31,y+17,64,13);

        // Patitas cortas de la mamá salchicha
        g.setColor(new Color(133,78,43));
        g.fillRoundRect(x-23,y-2,10,19,7,7);
        g.fillRoundRect(x-23,y+10,10,10,6,6);
        g.fillRoundRect(x+13,y-2,10,19,7,7);
        g.fillRoundRect(x+13,y+10,10,10,6,6);

        // Cuerpo largo característico de una dachshund
        g.setColor(new Color(170,105,55));
        g.fillRoundRect(x-31,y-12,62,28,19,19);

        g.setColor(new Color(211,151,91));
        g.fillRoundRect(x-25,y-9,48,19,15,15);

        // Cabeza y hocico
        g.setColor(new Color(177,111,59));
        g.fillOval(x+19,y-16,27,27);

        g.setColor(new Color(224,169,111));
        g.fillOval(x+34,y-7,17,13);

        // Orejas caídas
        g.setColor(new Color(105,59,39));
        g.fillOval(x+20,y-18,10,18);
        g.fillOval(x+35,y-17,10,17);

        g.setColor(new Color(45,34,29));
        g.fillOval(x+46,y-4,5,5);

        g.setColor(Color.WHITE);
        g.fillOval(x+35,y-9,4,4);

        g.setColor(new Color(45,34,29));
        g.fillOval(x+37,y-8,2,3);

        // Los bebés rescatados viajan acomodados sobre el lomo
        int bebes=partida.getCristales();

        if(bebes>=1) dibujarCachorro(g,x-17,y-11,0.72);
        if(bebes>=2) dibujarCachorro(g,x+1,y-11,0.72);
        if(bebes>=3) dibujarCachorro(g,x-8,y+2,0.62);

        // Cola
        g.setColor(new Color(133,78,43));
        g.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g.drawLine(x-29,y-4,x-38,y-13);
    }

    private void dibujarHud(Graphics2D g) {
        g.setColor(new Color(17,14,30,225));
        g.fillRoundRect(18,14,924,72,18,18);

        g.setColor(new Color(145,117,174));
        g.drawRoundRect(18,14,924,72,18,18);

        Texto.izquierda(g,"BEBÉS: "+partida.getCristales()+"/3",38,43,18,new Color(255,220,133));
        Texto.izquierda(g,"PUNTOS: "+partida.getPuntaje(),220,43,18,Color.WHITE);
        Texto.derecha(g,"VIDAS: "+"♥ ".repeat(Math.max(0,partida.getVidas())),915,43,19,new Color(255,125,147));

        // Muestra la ruta calculada mediante Dijkstra.
        Texto.izquierda(g,rutaSantuario,38,75,12,new Color(218,205,239));

        Texto.centrado(g,"WASD / FLECHAS: mover     E: interactuar     BACKSPACE: volver     1-4: Markov k     P: pausa     ESC: menú",480,615,13,new Color(213,204,226));

        if(tiempoAviso>0) {
            g.setColor(new Color(20,15,32,210));
            g.fillRoundRect(235,570,490,34,12,12);
            Texto.centrado(g,aviso,480,592,14,new Color(255,236,191));
        }
    }
}
