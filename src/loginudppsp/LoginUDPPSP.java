/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginudppsp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sportak
 */
public class LoginUDPPSP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        Servidor server = new Servidor("Servidor autista");
        Cliente cliente = new Cliente("Cliente aun más autista");

        server.start();
        Thread.sleep(1000);
        cliente.start();
        Thread.sleep(15000);
        try {
            server.cerrarServidor();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }

}
