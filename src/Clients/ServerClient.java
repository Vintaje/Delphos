/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clients;

import java.net.Socket;
import Constant.ClientCst;
import DB.StaticConnection;
import Models.Grade;
import Models.Participante;
import Models.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        try {
            while (client.isConnected()) {
                short task;
                System.out.println("Cliente: " + client.getInetAddress() + " A la espera de Ordenes");

                task = (short) this.receive.readObject();

                System.out.println(task);
                manageTask(task);

            }
        } catch (Exception ex) {
            closeConnection();
        }
    }

    public void closeConnection() {
        try {
            client.close();
        } catch (IOException ex1) {
            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex1);
        }
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
                    setMarks();
                    break;
                case ClientCst.GET_MARKS:
                    getMarks();
                    break;
                case ClientCst.GET_USERS:
                    getUsers();
                    break;
                case ClientCst.GET_ROLES:
                    getRoles();
                    break;
                case ClientCst.GET_GRADES:
                    getGrades();
                    break;
                case ClientCst.DEL_GRADE:
                    delGrade();
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
        boolean response = StaticConnection.userRegiser(user);
        System.out.println(response);
        this.send.writeObject(response);

    }

    public void login() throws IOException, ClassNotFoundException {

        User user = (User) this.receive.readObject();
        byte response = StaticConnection.userLogin(user.getName(), user.getPwd());

        this.send.writeObject(response);

    }

    public void activateUser() throws IOException, ClassNotFoundException, SQLException {
        User user = (User) this.receive.readObject();
        System.out.println(user);
        StaticConnection.ModifyRole(user);
        this.send.writeObject(true);
    }

    public void setUserRol() {
    }

    public void addGrade() throws IOException, ClassNotFoundException {
        Grade grade = (Grade) this.receive.readObject();
        System.out.println(grade);
        boolean res = StaticConnection.insertarCurso(grade);
        this.send.writeObject(res);
    }

    public void setGrade() throws IOException, ClassNotFoundException {
        Participante student = (Participante) this.receive.readObject();
        System.out.println(student);
        boolean res = false;
        if (student.getRol() == 1) {
            res = StaticConnection.asignarCurso(student);
        } else if (student.getRol() == 2) {
            res = StaticConnection.asignarProfesor(student);
        }
        this.send.writeObject(res);
    }

    public void editGrade() throws IOException, ClassNotFoundException {
        Grade grade = (Grade) this.receive.readObject();
        System.out.println(grade);
        boolean res = StaticConnection.actualizarCurso(grade);
        this.send.writeObject(res);
    }

    public void setMarks() throws IOException, ClassNotFoundException {

    }

    public void getMarks() {
    }

    public void getUsers() {
        try {
            ArrayList<User> users = StaticConnection.usersList();
            System.out.println(users.size());
            this.send.writeObject(users);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getRoles() {
        try {
            ArrayList<String> roles = StaticConnection.getRoles();
            this.send.writeObject(roles);
            System.out.println("Roles Enviados");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getGrades() {
        try {
            ArrayList<Grade> grades = StaticConnection.getGrades();
            this.send.writeObject(grades);
            System.out.println("Grades enviados");
        } catch (Exception ex) {
        }
    }

    private void delGrade() {
    }

}
