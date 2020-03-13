/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Constant.ServerCst;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author vinta
 */
public class WebService implements Runnable{
    private Thread server;
    private ServerSocket ssocket;

    public WebService() {
        this.server = new Thread(this);
    }

    @Override
    public void run() {
        Security.generarClaves();
        try {
            this.ssocket = new ServerSocket(ServerCst.WEB_PORT);

            while (true) {
                System.out.println("Esperando Usuarios WEB");
                Socket client = this.ssocket.accept();
                new WebServiceClient(client).start();
            }
        } catch (Exception ex) {
            this.ssocket = null;
            ex.printStackTrace();
        }

    }

    public void start() {
        this.server.start();
    }
}
