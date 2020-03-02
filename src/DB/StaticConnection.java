/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Models.Grade;
import Models.Student;
import Models.User;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author vinta
 */
public class StaticConnection {

    //********************* Atributos *************************
    private static java.sql.Connection Conn;
    //Atributo a través del cual hacemos la conexión física.
    private static java.sql.Statement SQL_Statement;
    //Atributo que nos permite ejecutar una sentencia SQL
    private static java.sql.ResultSet Conn_Records;
    //
    private static final String DB = "delphos";
    private static final String USER = "root";
    private static final String PWD = "";
    private static final String USER_TB = "usuarios";
    private static final String STUDENT_TB = "alumno";
    private static final String GRADE_TB = "curso";
    private static final String TEACHER_TB = "imparte";
    private static final String ROL_TB = "rol";
    private static final String ROLRELATION_TB = "roles_asignados";
    private static final String MARKS_TB = "notas";

    public static void nueva() {
        try {
            //Cargar el driver/controlador
            String controlador = "com.mysql.jdbc.Driver";
            Class.forName(controlador);

            String URL_BD = "jdbc:mysql://localhost/" + DB;
            Conn = java.sql.DriverManager.getConnection(URL_BD, USER, PWD);
            SQL_Statement = Conn.createStatement();
            System.out.println("Conexion realizada con éxito");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public static void cerrarBD() {
        try {
            // resultado.close();
            Conn.close();
            System.out.println("Desconectado de la Base de Datos"); // Opcional para seguridad
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error de Desconexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static byte userLogin(String usuario, String password) {

        try {
            String sentencia = "SELECT user.id, roles.id_rol FROM " + USER_TB + " user, " + ROLRELATION_TB + " roles WHERE user.id=roles.id_usuario AND nombre = '" + usuario + "' AND password = '" + password + "'";
            StaticConnection.Conn_Records = StaticConnection.SQL_Statement.executeQuery(sentencia);

            if (StaticConnection.Conn_Records.next())//Si devuelve true es que existe.
            {
                System.out.println("Hay Cosas");
                return (byte) Conn_Records.getInt(2);
            }
        } catch (SQLException ex) {
            System.out.println("Error en el acceso a la BD.");
        }
        return -1;//Si devolvemos null el usuario no existe.
    }

    public static synchronized boolean userExist(String user) {
        try {
            String sentencia = "SELECT * FROM " + USER_TB + " WHERE nombre = '" + user + "'";
            StaticConnection.Conn_Records = StaticConnection.SQL_Statement.executeQuery(sentencia);
            if (StaticConnection.Conn_Records.next())//Si devuelve true es que existe.
            {
                return true;
            }
        } catch (Exception ex) {
            System.out.println("Error en el acceso a la BD.");
        }
        return false;
    }

    public static synchronized boolean userRegiser(User user) {
        try {
            if (!userExist(user.getName())) {
                InsertUser(user);
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static synchronized int getUserId(String usuario) {
        try {
            String sentencia = "SELECT user.id FROM " + USER_TB + " user WHERE nombre = '" + usuario + "'";
            StaticConnection.Conn_Records = StaticConnection.SQL_Statement.executeQuery(sentencia);

            if (StaticConnection.Conn_Records.next())//Si devuelve true es que existe.
            {
                System.out.println("Hay Cosas");
                return (int) Conn_Records.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error en el acceso a la BD.");
        }
        return -1;//Si devolvemos null el usuario no existe.
    }

    public static synchronized ArrayList<Grade> getGrades() {
        ArrayList<Grade> grades = new ArrayList<>();
        String sentence = "SELECT * FROM " + GRADE_TB + "";

        try {
            StaticConnection.Conn_Records = StaticConnection.SQL_Statement.executeQuery(sentence);
            while (StaticConnection.Conn_Records.next()) {
                grades.add(new Grade(Conn_Records.getInt("IDCURSO"), Conn_Records.getString("CODIGO"), Conn_Records.getString("NOMBRE")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(grades.size());
        return grades;
    }

    public static synchronized ArrayList<User> usersList() {
        ArrayList<User> users = new ArrayList<>();

        try {
            String sentencia = "SELECT user.id, user.nombre, user.password, user.telefono, user.direccion, user.edad, roles.id_rol FROM " + USER_TB + " user, " + ROLRELATION_TB + " roles WHERE roles.id_usuario = user.id";
            StaticConnection.Conn_Records = StaticConnection.SQL_Statement.executeQuery(sentencia);

            while (StaticConnection.Conn_Records.next())//Si devuelve true es que existe.
            {
                User user = new User(Conn_Records.getInt("user.id"), Conn_Records.getString("user.nombre"), Conn_Records.getString("user.password"),
                        Conn_Records.getString("user.telefono"), Conn_Records.getString("user.direccion"), Conn_Records.getInt("user.edad"), (byte) Conn_Records.getInt("roles.id_rol"));
                users.add(user);
            }
        } catch (SQLException ex) {
            System.out.println("Error en el acc   eso a la BD.");
        }
        return users;
    }

    public static synchronized boolean insertarCurso(Grade grade) {
        boolean registrado = false;
        String sql = "INSERT INTO " + StaticConnection.GRADE_TB + " (CODIGO, NOMBRE) VALUES ( '"
                + grade.getCode() + "', '" + grade.getName() + "')";
        System.out.println(sql);
        try {
            if (SQL_Statement.executeUpdate(sql) == 1) {
                registrado = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrado;
    }

    public static synchronized boolean actualizarCurso(Grade grade) {
        boolean actualizado = false;
        String sql = "UPDATE " + StaticConnection.GRADE_TB + " SET CODIGO = '" + grade.getCode()
                + "', NOMBRE = '" + grade.getName() + "' WHERE IDCURSO = " + grade.getId();
        System.out.println(sql);
        try {
            if (SQL_Statement.executeUpdate(sql) == 1) {
                actualizado = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actualizado;
    }

    public static synchronized boolean asignarCurso(Student student) {
        boolean registrado = false;
        String sql = "INSERT INTO " + StaticConnection.STUDENT_TB + " VALUES ( "
                + student.getId() + ", " + student.getIdgrade() + ")";
        System.out.println(sql);
        try {
            if (SQL_Statement.executeUpdate(sql) == 1) {
                registrado = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrado;
    }

    public static synchronized boolean asignarProfesor(User user, int idGrade) {
        boolean registrado = false;
        String sql = "INSERT INTO " + StaticConnection.TEACHER_TB + " (COD_PROFESOR, COD_CURSO) VALUES ( "
                + user.getId() + ", " + idGrade + ")";
        System.out.println(sql);
        try {
            if (SQL_Statement.executeUpdate(sql) == 1) {
                registrado = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrado;
    }

    public static void Borrar_Dato(String tabla, String DNI) throws SQLException {
        String Sentencia = "DELETE FROM " + tabla + " WHERE username = '" + DNI + "'";
        StaticConnection.SQL_Statement.executeUpdate(Sentencia);
    }

    //----------------------------------------------------------
    public synchronized static void ModifyRole(User user) throws SQLException {
        String Sentencia = "UPDATE " + ROLRELATION_TB + " SET id_rol = " + user.getRol() + " WHERE id_usuario = " + user.getId();
        StaticConnection.SQL_Statement.executeUpdate(Sentencia);
    }

    //----------------------------------------------------------
    public synchronized static void InsertUser(User user) throws SQLException {
        String Sentencia = "INSERT INTO " + USER_TB + "(nombre, password, telefono, direccion, edad)"
                + " VALUES ('" + user.getName() + "', '" + user.getPwd() + "','" + user.getPhoneNumber() + "', '" + user.getAddress() + "', " + user.getAge() + ")";
        StaticConnection.SQL_Statement.executeUpdate(Sentencia);
        int id = getUserId(user.getName());
        Sentencia = "INSERT INTO " + ROLRELATION_TB + " (id_rol, id_usuario) VALUES(0," + id + ")";
        StaticConnection.SQL_Statement.executeUpdate(Sentencia);
    }

    public static ArrayList<String> getRoles() {
        ArrayList<String> roles = new ArrayList<>();
        try {

            String Sentencia = "SELECT DESCRPICION FROM ROL";
            Conn_Records = StaticConnection.SQL_Statement.executeQuery(Sentencia);
            while (Conn_Records.next()) {
                roles.add(Conn_Records.getString("DESCRPICION"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return roles;
    }

}
