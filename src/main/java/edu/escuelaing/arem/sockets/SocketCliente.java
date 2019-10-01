/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.sockets;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *
 * @author Nicolas
 */
public class SocketCliente {

    public static Socket receiveRequest(ServerSocket serverSocket) {
        Socket request = null;
        try {
            System.out.println("Ready to receive...");
            request = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        return request;
    }
    
   
}
