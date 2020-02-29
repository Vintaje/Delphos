/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clients;

import java.net.Socket;
import Constant.ClientCst;
import DB.StaticConnection;
import Models.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author vinta
 */
public class ServerClient implements Runnable {

    private Thread tClient;
    private Socket client;
    private ObjectInputStream receive;
    private ObjectOutputStream send;

    public ServerClient() {
    }

    public ServerClient(Socket client) {

        this.tClient = new Thread(this);
        this.client = client;
        try {
            this.receive = new ObjectInputStream(this.client.getInputStream());
            this.send = new ObjectOutputStream(this.client.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void run() {

    }

    public void manageTask(short task) {
        try {
            switch (task) {
                case ClientCst.REGISTER:
                    register();
                    break;
                case ClientCst.LOGIN:
                    login();
                    break;
                case ClientCst.ACTIVATE_USER:
                    activateUser();
                    break;
                case ClientCst.SET_USER_ROL:
                    setUserRol();
                    break;
                case ClientCst.ADD_GRADE:
                    addGrade();
                    break;
                case ClientCst.SET_GRADE:
                    setGrade();
                    break;
                case ClientCst.EDIT_GRADE:
                    editGrade();
                    break;
                case ClientCst.SET_MARKS:
                    break;
                case ClientCst.GET_MARKS:
                    break;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("TASK: " + task);
        }
    }

    public void start() {
        this.tClient.start();
    }

    public void register() throws IOException, ClassNotFoundException {
        User user = (User) this.receive.readObject();
        this.send.writeBoolean(StaticConnection.userRegiser(user));
    }

    public void login() {
    }

    public void activateUser() {
    }

    public void setUserRol() {
    }

    public void addGrade() {
    }

    public void setGrade() {
    }

    public void editGrade() {
    }

    public void setMarks() {
    }

    public void getMarks() {
    }

}
