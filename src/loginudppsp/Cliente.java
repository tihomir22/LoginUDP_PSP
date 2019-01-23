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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sportak
 */
public class Cliente extends Thread {

    private int puertoServidor = 5555;
    private byte[] buffer = new byte[1024];
    private String nombreCliente;
    Scanner teclado = new Scanner(System.in);
    InetAddress direccionServidor;

    @Override
    public void run() {
        try {
            DatagramSocket cliente_socket = new DatagramSocket();
            System.out.println(this.nombreCliente + " me he iniciado");
            direccionServidor = InetAddress.getByName("localhost");

            this.recibirYEnviar(cliente_socket, "Introduce cuenta de usuario", "usuario");
            this.recibirYEnviar(cliente_socket, "Introduce contraseña de usuario", "contraseña");

            System.out.println("Cerrando cliente...");
            cliente_socket.close();
        } catch (SocketException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Cliente() {
    }

    public Cliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void recibirYEnviar(DatagramSocket cliente_socket, String pregunta, String tipo) throws IOException {
        String mensaje = "";
        do {
            System.out.println(pregunta);
            mensaje = teclado.nextLine();
            mensaje = tipo + ":" + mensaje;
            buffer = mensaje.getBytes();
            System.out.println(this.nombreCliente + " voy a enviar el mensaje " + mensaje);
            DatagramPacket cliente_paquete = new DatagramPacket(this.buffer, this.buffer.length, direccionServidor, this.puertoServidor);
            cliente_socket.send(cliente_paquete);

            //Respuesta
            this.buffer = new byte[1024];
            DatagramPacket respuesta_servidor = new DatagramPacket(this.buffer, this.buffer.length);
            cliente_socket.receive(respuesta_servidor);

            mensaje = new String(respuesta_servidor.getData());
            System.out.println(this.nombreCliente + "  Recibido desde el servidor " + mensaje);

        } while (!mensaje.trim().equalsIgnoreCase("correcto"));

    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

}
