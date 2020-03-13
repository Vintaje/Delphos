/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Models.Grade;
import Models.Mark;
import Models.Participante;
import Models.User;
import Server.Security;
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

    public static User userLogin(String usuario, String password) {
        User user = new User();
        try {
            String sentencia = "SELECT user.id, roles.id_rol, user.clave FROM " + USER_TB + " user, " + ROLRELATION_TB + " roles WHERE user.id=roles.id_usuario AND nombre = '" + usuario + "' AND password = '" + password + "'";
            StaticConnection.Conn_Records = StaticConnection.SQL_Statement.executeQuery(sentencia);

            if (StaticConnection.Conn_Records.next())//Si devuelve true es que existe.
            {

                user.setId(Conn_Records.getInt(1));
                user.setRol((byte) Conn_Records.getInt(2));
                user.setSecretKey(Security.recomponerClave(Conn_Records.getBytes(3)));

                return user;
            } else {
                user.setRol((byte) -1);
            }
        } catch (SQLException ex) {
            System.out.println("Error en el acceso a la BD.");
        }
        return user;//Si devolvemos null el usuario no existe.
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
                user.setSecretKey(Security.generarClaveSimetrica());
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

    public static synchronized boolean asignarCurso(Participante student) {
        boolean registrado = false;

        String sql = "INSERT INTO " + StaticConnection.STUDENT_TB + " VALUES ( "
                + student.getId() + ", " + student.getIdgrade() + ")";
        System.out.println(sql);
        try {
            if (SQL_Statement.executeUpdate(sql) == 1) {
                registrado = true;
            }
        } catch (Exception ex) {
            try {
                sql = "UPDATE " + STUDENT_TB + " SET IDCURSO = " + student.getIdgrade() + " WHERE ID=" + student.getId();
                if (SQL_Statement.executeUpdate(sql) == 1) {
                    registrado = true;
                }
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return registrado;
    }

    public static synchronized boolean asignarProfesor(Participante participante) {
        boolean registrado = false;
        String sql = "INSERT INTO " + StaticConnection.TEACHER_TB + " (COD_PROFESOR, COD_CURSO) VALUES ( "
                + participante.getId() + ", " + participante.getIdgrade() + ")";
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
        String Sentencia = "INSERT INTO " + USER_TB + "(nombre, password, telefono, direccion, edad, clave)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = Conn.prepareStatement(Sentencia);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPwd());
        ps.setString(3, user.getPhoneNumber());
        ps.setString(4, user.getAddress());
        ps.setInt(5, user.getAge());
        ps.setBytes(6, user.getSecretKey().getEncoded());
        int rowaff = ps.executeUpdate();
        if (rowaff > 0) {
            int id = getUserId(user.getName());
            System.out.println(id);
            Sentencia = "INSERT INTO " + ROLRELATION_TB + " (id_rol, id_usuario) VALUES(0," + id + ")";
            StaticConnection.SQL_Statement.executeUpdate(Sentencia);
        }
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

    public static ArrayList<Grade> listarGradesProfesor(int idProfesor) {
        ArrayList<Grade> listaGrades = new ArrayList<>();
        String sql = "SELECT C.IDCURSO, C.CODIGO, C.NOMBRE FROM " + GRADE_TB + " C, " + TEACHER_TB + " I"
                + " WHERE C.IDCURSO = I.COD_CURSO AND I.COD_PROFESOR = " + idProfesor;
        try {
            Conn_Records = SQL_Statement.executeQuery(sql);
            while (Conn_Records.next()) {
                Grade curso = new Grade();
                curso.setId(Conn_Records.getInt(1));
                curso.setCode(Conn_Records.getString(2));
                curso.setName(Conn_Records.getString(3));
                listaGrades.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaGrades;
    }

    public static ArrayList<Participante> listarParticipantesCurso(int idCurso) {
        ArrayList<Participante> listaParticipantes = new ArrayList<>();
        String sql = "SELECT U.ID, U.NOMBRE FROM " + USER_TB + " U, " + STUDENT_TB + " A "
                + " WHERE A.IDCURSO = " + idCurso + " AND  A.ID = U.ID";
        try {
            Conn_Records = SQL_Statement.executeQuery(sql);
            while (Conn_Records.next()) {
                Participante aux = new Participante();
                aux.setId(Conn_Records.getInt(1));
                aux.setName(Conn_Records.getString(2));
                listaParticipantes.add(aux);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaParticipantes;
    }

    public static synchronized boolean ponerNota(Mark nota) {
        boolean puesta = false;
        String sql = "INSERT INTO " + MARKS_TB + " (COD_ALUMNO, COD_PROFESOR, NOTA) VALUES ( "
                + nota.getCod_student() + ", " + nota.getCod_teacher() + ", " + nota.getMark() + ")";
        try {
            if (SQL_Statement.executeUpdate(sql) == 1) {
                puesta = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return puesta;
    }

    public static ArrayList<User> listarProfesoresAlumno(int id) {
        ArrayList<User> listaUsers = new ArrayList<>();
        String sql = "SELECT * FROM " + USER_TB + " U, " + TEACHER_TB + " I WHERE I.COD_CURSO = (SELECT IDCURSO FROM ALUMNO WHERE ID = " + id + " ) AND U.ID = I.COD_PROFESOR ";
        try {
            Conn_Records = SQL_Statement.executeQuery(sql);
            while (Conn_Records.next()) {
                User usuario = new User();
                usuario.setId(Conn_Records.getInt(1));
                usuario.setName(Conn_Records.getString(2));
                usuario.setPwd(Conn_Records.getString(3));
                usuario.setPhoneNumber(Conn_Records.getString(4));
                usuario.setAddress(Conn_Records.getString(5));
                usuario.setAge(Conn_Records.getInt(6));
                listaUsers.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaUsers;
    }

    public static Mark consultarMark(Mark nota) {
        Mark aux = null;
        String sql = "SELECT * FROM " + MARKS_TB + " WHERE COD_ALUMNO = " + nota.getCod_student() + " AND COD_PROFESOR = " + nota.getCod_teacher();
        try {
            Conn_Records = SQL_Statement.executeQuery(sql);
            if (Conn_Records.next()) {
                aux = new Mark();
                aux.setId(Conn_Records.getInt(1));
                aux.setCod_student(Conn_Records.getInt(2));
                aux.setCod_teacher(Conn_Records.getInt(3));
                aux.setMark(Float.toString(Conn_Records.getFloat(4)));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aux;
    }
}
