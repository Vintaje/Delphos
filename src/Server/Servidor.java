/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Clients.ServerClient;
import Constant.ServerCst;
import DB.StaticConnection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinta
 */
public class Servidor implements Runnable {

    private Thread server;
    private ServerSocket ssocket;

    public Servidor() {
        this.server = new Thread(this);
    }

    @Override
    public void run() {

        try {
            this.ssocket = new ServerSocket(ServerCst.PORT);
            StaticConnection.nueva();
            while (!ssocket.isClosed()) {
                System.out.println("Esperando Usuarios");
                Socket client = this.ssocket.accept();
                new ServerClient(client).start();
            }
        } catch (IOException ex) {
            this.ssocket = null;
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void start() {
        this.server.start();
    }

}
