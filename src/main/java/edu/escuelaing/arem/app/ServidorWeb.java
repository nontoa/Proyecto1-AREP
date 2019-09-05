/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Nicolas
 */
public class ServidorWeb {

    public static void main(String[] args) throws IOException {
        boolean flag = true;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8081);
        } catch (Exception e) {
            System.exit(1);
        }
        Socket clienteSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        while (flag) {
            try {
                System.out.println("Ready");
                clienteSocket = serverSocket.accept();
            } catch (Exception e) {
                System.exit(1);
            }
            out = new PrintWriter(clienteSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));

            String inputLine;
            String path = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
                if (inputLine.contains("GET")) {
                    String[] get = inputLine.split(" ");
                    path = get[1];
                }
            }
            System.out.println(path);
            
            out.println("HTTP/1.1 200 OK"+ "\r\n");
            out.println("Content-Type: text/html"+ "\r\n");
            out.println("<!DOCTYPE html>"+ "\r\n");
            out.println("<html>"+ "\r\n");
            out.println("<head>"+ "\r\n");
            out.println("<meta charset=\"UTF-8\">"+ "\r\n");
            out.println("<title>Title of the document</title>"+ "\r\n");
            out.println("</head>"+ "\r\n");
            out.println("<body>"+ "\r\n");
            out.println("My Web Site"+ "\r\n");
            out.println("</body>"+ "\r\n");
            out.println("</html>"+ "\r\n");
            out.flush();
            

        }
        out.close();
        in.close();
        clienteSocket.close();
        serverSocket.close();
    }

}
