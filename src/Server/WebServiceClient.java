/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import DB.StaticConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author vinta
 */
public class WebServiceClient implements Runnable {

    private Thread thread;
    private Socket client;

    public WebServiceClient(Socket client) {
        this.thread = new Thread(this);
        this.client = client;
    }

    public void run() {

        PrintWriter out = null;

        try {
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "8859_1"), true);
            //Comentar desde aquí     
            out.println("HTTP/1.1 200 ok");
            out.println("Server: Delphos 1.0v");
            out.println("Date: " + "");
            out.println("Content-Type: text/html");
            out.println("");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");

            out.println(" <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            out.println("<title>DAM2 2016 2017</title>");
            out.println("</head>");
            out.print("<body>");
            out.print("<table>");
            out.print("<tr>");
            out.print("<th>Nombre</th><th>Nota</th>");
            out.print("</tr>");
            ArrayList<String[]> marks = StaticConnection.consultarNotas();
            for (String[] mark : marks) {

                out.print("<tr>"
                        + "<td>"
                        + mark[0]
                        + "</td>"
                        + "<td>"
                        + mark[1]
                        + "</td></tr>");
            }
            out.print("</table>");
            out.print("</body>");

            out.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    void start() {
        this.thread.start();
    }
}
