/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.app;

import edu.escuelaing.arem.app.App;
import static edu.escuelaing.arem.app.App.serverSocket;
import edu.escuelaing.arem.sockets.SocketCliente;
import edu.escuelaing.arem.sockets.SocketServidor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Nicolas
 */
public class Controller {
    
    final static ExecutorService service = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        
        
        ServerSocket serverSocket = SocketServidor.runServer();
        App.initialize();        
        while(true){                       
            Socket clientSocket = SocketCliente.receiveRequest(serverSocket);
            service.execute(new App(clientSocket));

        }
        
    }
}
