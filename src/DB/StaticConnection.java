/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

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

    public static boolean userLogin(String usuario, String password) {

        try {
            String sentencia = "SELECT * FROM " + USER_TB +" WHERE nombre = '" + usuario + "' AND password = '" + password + "'";
            StaticConnection.Conn_Records = StaticConnection.SQL_Statement.executeQuery(sentencia);
            if (StaticConnection.Conn_Records.next())//Si devuelve true es que existe.
            {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error en el acceso a la BD.");
        }
        return false;//Si devolvemos null el usuario no existe.
    }

    public static boolean userExist(String user) {
        try {
            String sentencia = "SELECT * FROM " + USER_TB +" WHERE nombre = '" + user + "'";
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

    
    public static void Borrar_Dato(String tabla, String DNI) throws SQLException {
        String Sentencia = "DELETE FROM " + tabla + " WHERE username = '" + DNI + "'";
        StaticConnection.SQL_Statement.executeUpdate(Sentencia);
    }

    //----------------------------------------------------------
    public static void Modificar_Dato(String tabla, String DNI, String Nuevo_Nombre) throws SQLException {
        String Sentencia = "UPDATE " + tabla + " SET Nombre = '" + Nuevo_Nombre + "' WHERE DNI = '" + DNI + "'";
        StaticConnection.SQL_Statement.executeUpdate(Sentencia);
    }

    //----------------------------------------------------------
    public static void InsertUser(User user) throws SQLException {
        String Sentencia = "INSERT INTO " + USER_TB +"(nombre, password, telefono, direccion, edad)"+ " VALUES ('" + user.getName() + "', '" + user.getPwd() + "','"+user.getPhoneNumber()+"', '" + user.getAddress() + "', " + user.getAge() + ")";
        StaticConnection.SQL_Statement.executeUpdate(Sentencia);
    }

}
