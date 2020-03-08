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
import Models.Mark;
import Models.Participante;
import Models.User;
import Server.Security;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author vinta
 */
public class ServerClient implements Runnable {

    private Thread tClient;
    private Socket client;
    private ObjectInputStream receive;
    private ObjectOutputStream send;
    private SecretKey secretKey;

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
                short task = -1;
                System.out.println("Cliente: " + client.getInetAddress() + " A la espera de Ordenes");

                task = (short) this.receive.readObject();

                System.out.println(task);
                manageTask(task);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                case ClientCst.GET_MY_GRADES:
                    getMyGrades();
                    break;
                case ClientCst.GET_MY_STUDENTS:
                    getMyStudents();
                    break;
                case ClientCst.GET_MY_TEACHERS:
                    getMyTeachers();
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
        this.send.flush();
    }

    public void login() throws IOException, ClassNotFoundException {

        User user = (User) this.receive.readObject();

        System.out.println(user);
        User response = StaticConnection.userLogin(user.getName(), user.getPwd());
        System.out.println(response.getSecretKey());
        if (response.getRol() != -1) {
            this.secretKey = response.getSecretKey();
        }
        System.out.println(response);
        this.send.writeObject(response);
        System.out.println(response);
        this.send.flush();
    }

    public void activateUser() throws IOException, ClassNotFoundException, SQLException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException {

        User user = (User) Security.descifrar(this.secretKey, this.receive.readObject());
        System.out.println("Modify: "+user);
        StaticConnection.ModifyRole(user);

        Object obj = Security.cifrarConClaveSimetrica(true, this.secretKey);
        this.send.writeObject(obj);
        this.send.flush();
    }

    public void setUserRol() {
    }

    public void addGrade() throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException {
        Grade grade = (Grade) Security.descifrar(this.secretKey, this.receive.readObject());
        System.out.println(grade);
        boolean res = StaticConnection.insertarCurso(grade);
        Object obj = Security.cifrarConClaveSimetrica(res, this.secretKey);
        this.send.writeObject(obj);
        this.send.flush();
    }

    public void setGrade() throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException {
        Participante student = (Participante) Security.descifrar(this.secretKey, this.receive.readObject());
        System.out.println(student);
        boolean res = false;
        if (student.getRol() == 1) {
            res = StaticConnection.asignarCurso(student);
        } else if (student.getRol() == 2) {
            res = StaticConnection.asignarProfesor(student);
        }
        Object obj = Security.cifrarConClaveSimetrica(res, this.secretKey);
        this.send.writeObject(obj);
        this.send.flush();
    }

    public void editGrade() throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException {
        Grade grade = (Grade) Security.descifrar(this.secretKey, this.receive.readObject());
        System.out.println(grade);
        boolean res = StaticConnection.actualizarCurso(grade);
        Object obj = Security.cifrarConClaveSimetrica(res, this.secretKey);
        this.send.writeObject(obj);
        this.send.flush();
    }

    public void setMarks() throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException {
        Mark mark = (Mark) Security.descifrar(this.secretKey, this.receive.readObject());
        System.out.println(mark);
        boolean res = StaticConnection.ponerNota(mark);
        Object obj = Security.cifrarConClaveSimetrica(res, this.secretKey);
        this.send.writeObject(obj);
        this.send.flush();
    }

    public void getMarks() {
        try {
            Object obj = Security.descifrar(this.secretKey, this.receive.readObject());
            Mark mark = (Mark) obj;

            Mark res = StaticConnection.consultarMark(mark);
            Object obje = Security.cifrarConClaveSimetrica(res, this.secretKey);
            this.send.writeObject(obje);
            this.send.flush();
        } catch (Exception ex) {

        }
    }

    public void getUsers() {
        try {
            ArrayList<User> res = StaticConnection.usersList();
            System.out.println("Vector " + res.size());
            Object obj = Security.cifrarConClaveSimetrica(res, this.secretKey);
            this.send.writeObject(obj);
            this.send.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getRoles() {
        try {
            ArrayList<String> res = StaticConnection.getRoles();
            Object obj = Security.cifrarConClaveSimetrica(res, this.secretKey);
            this.send.writeObject(obj);
            this.send.flush();
            System.out.println("Roles Enviados");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getGrades() {
        try {
            ArrayList<Grade> res = StaticConnection.getGrades();
            Object obj = Security.cifrarConClaveSimetrica(res, this.secretKey);
            this.send.writeObject(obj);
            this.send.flush();
            System.out.println("Grades enviados");
        } catch (Exception ex) {
        }
    }

    private void delGrade() {
    }

    private void getMyGrades() {
        try {
            int id_user = (int) Security.descifrar(this.secretKey, this.receive.readObject());
            ArrayList<Grade> res = StaticConnection.listarGradesProfesor(id_user);
            this.send.writeObject(Security.cifrarConClaveSimetrica(res, this.secretKey));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getMyStudents() {
        try {
            Object obj = Security.descifrar(this.secretKey, this.receive.readObject());
            int grade = (int) obj;
            ArrayList<Participante> res = StaticConnection.listarParticipantesCurso(grade);
            this.send.writeObject(Security.cifrarConClaveSimetrica(res, this.secretKey));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getMyTeachers() {
        try {
            Object obj = Security.descifrar(this.secretKey, this.receive.readObject());
            int id = (int) obj;
            ArrayList<User> res = StaticConnection.listarProfesoresAlumno(id);
            this.send.writeObject(Security.cifrarConClaveSimetrica(res, this.secretKey));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
