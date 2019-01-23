/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginudppsp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sportak
 */
public class Servidor extends Thread {

    private int puertoServidor = 5555;
    private byte[] buffer = new byte[1024];
    private String nombreServidor;

    private String usuarioCorrecto = "";
    private String contraseñaCorrecta = "";

    private boolean servidorCerrado = false;

    @Override
    public void run() {
        try {
            //InetAddress direccionIp = InetAddress.getByName("localhost");
            DatagramSocket servidor_socket = new DatagramSocket(this.puertoServidor);
            System.out.println("[" + this.nombreServidor + "]" + " Me he iniciado");
            while (this.contraseñaCorrecta.length() == 0 && this.servidorCerrado == false) {
                this.buffer = new byte[1024];
                DatagramPacket servidor_packet = new DatagramPacket(this.buffer, this.buffer.length);
                System.out.println("[" + this.nombreServidor + "]" + " Me dispongo a esperar ");
                servidor_socket.receive(servidor_packet);
                String mensaje = new String(servidor_packet.getData());
                System.out.println("[" + this.nombreServidor + "]" + " He recibido del cliente " + mensaje);

                //Respuesta a cliente
                String respuesta = procesarRespuesta(mensaje);
                System.out.println("[" + this.nombreServidor + "]" + " Voy a responderle al cliente con " + respuesta);
                int puertoCliente = servidor_packet.getPort();
                InetAddress direccionCliente = servidor_packet.getAddress();
                this.buffer = respuesta.getBytes();
                DatagramPacket respuesta_packet = new DatagramPacket(this.buffer, this.buffer.length, direccionCliente, puertoCliente);
                servidor_socket.send(respuesta_packet);

            }
            if (this.usuarioCorrecto.length() > 0 && this.contraseñaCorrecta.length() > 0) {
                System.out.println("El usuario " + this.usuarioCorrecto + " se ha conectado con la contraseña " + this.contraseñaCorrecta + " con exito!");
            }
            System.out.println("Cerrando servidor...");
            this.cerrando();

            servidor_socket.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Servidor(String nombreServidor) {
        this.nombreServidor = nombreServidor;
    }

    public void cerrando() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(".");
        }
    }

    public String procesarRespuesta(String mensaje) {
        String[] despedazado = mensaje.split(":");

        if (despedazado[0].equalsIgnoreCase("usuario")) {
            if (despedazado[1].trim().equalsIgnoreCase("laura")) {
                mensaje = "correcto";
                System.out.println("Usuario correcto");
                this.usuarioCorrecto = despedazado[1].trim();
            } else {
                mensaje = "incorrecto";
                System.out.println("Usuario incorrecto");
            }
        } else if (despedazado[0].equalsIgnoreCase("contraseña")) {
            if (despedazado[1].trim().equalsIgnoreCase("l123")) {
                mensaje = "correcto";
                System.out.println("Contraseña correcta");
                this.contraseñaCorrecta = despedazado[1].trim();
            } else {
                mensaje = "incorrecto";
                System.out.println("Contraseña incorrecta");
            }
        } else {
            System.out.println("Petición del cliente invalida");
        }
        return mensaje;
    }

    public void cerrarServidor() throws Exception {
        this.servidorCerrado = true;
        throw new Exception("Servidor cerrado , tiempo limite sobrepasado!");
        
    }

    public String getNombreServidor() {
        return nombreServidor;
    }

    public void setNombreServidor(String nombreServidor) {
        this.nombreServidor = nombreServidor;
    }

}
