/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.app;

import java.net.*;
import java.io.*;

public class ServidorWeb {

    public static void main(String[] args) throws IOException {
        while (true) {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(getPort());
            } catch (IOException e) {
                System.err.println("Could not listen on port: " + getPort());
                System.exit(1);
            }
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
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

            //System.out.println(path);            
            if (path.equals("/index.html")) {
                out.write(index());
            } else {
                out.write(basic());
            }
            out.close();

            in.close();

            clientSocket.close();

            serverSocket.close();
        }
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 8081;
    }

    public static String index() {
        String outputLine = "";
        outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Title of the document</title>\n"
                + "</head>\n"
                + "<center>\n"
                + "<h1> Bienvenidos </h1>\n"
                + "</center> \n"
                + "<br>\n"
                + "<h2> Paginas Web: <h2>\n"
                + "<a href = \"https://www.w3schools.com\" > Visit W3Schools</a>\n"                
                + "</html>\n";

        return (outputLine);
    }

    public static String basic() {
        String outputLine = "";
        outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Title of the document</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Mi propio mensajeAAAAAAAAAAAAAAAA</h1>\n"
                + "</body>\n"
                + "</html>\n";

        return (outputLine);
    }
}
