package edu.escuelaing.arem.app;

import edu.escuelaing.arem.sockets.SocketCliente;
import edu.escuelaing.arem.sockets.SocketServidor;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * Hello world!
 *
 */
public class App {

    public static ServerSocket serverSocket = null;
    private static Socket clientSocket;
  
    
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    public static void initialize() {
        
    }

    public static void listen() throws IOException {
        
    }

    
}
