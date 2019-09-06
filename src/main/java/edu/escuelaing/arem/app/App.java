package edu.escuelaing.arem.app;

import edu.escuelaing.arem.appWeb.Web;
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
    private static HashMap<String, Handler> hashMap = new HashMap<String, Handler>();
    private static PrintWriter out;
    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    private static String requestUrl = "";

    public static void initialize() {
        try {
            String nameClass = "edu.escuelaing.arem.app.App";
            Class<?> c = Class.forName(nameClass);
            System.out.println(c.getMethods());
            for (Method m : c.getMethods()) {

                if (m.isAnnotationPresent(Web.class)) {
                    System.out.println(m.getName());
                    Class[] params = m.getParameterTypes();
                    hashMap.put("/apps/" + m.getAnnotation(Web.class).value(),
                            new HandlerStatic(c.getDeclaredMethod(m.getName(), params)));
                }
            }
            Object[] param = new Object[]{"JOhn"};            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listen() throws Exception {
        while (true) {
            serverSocket = SocketServidor.runServer();
            clientSocket = SocketCliente.receiveRequest(serverSocket);
            setRequest(clientSocket);
            postPage();
            clientSocket.close();
            serverSocket.close();
        }
    }

    private static void postPage() throws IOException {
        if (requestUrl.contains("/apps")) {
            readApps(requestUrl);
        } else if (requestUrl.contains("html")) {
            System.out.println(requestUrl);
            readHtml(requestUrl);
        } else if (requestUrl.contains("PNG")) {
            readJpg(requestUrl, clientSocket);
        } else {
            notFound();
        }
    }

    public static void setRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
            if (inputLine.contains("GET")) {
                requestUrl = inputLine.split(" ")[1];
                System.out.println("Adress to show: " + requestUrl);
            }
        }        
    }

    private static void readApps(String inputLine) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        int idApps = inputLine.indexOf("/apps");
        String subStrg = "";
        System.out.println(inputLine + "  line ");
        for (int ji = idApps; ji < inputLine.length() && inputLine.charAt(ji) != ' '; ++ji) {
            subStrg += inputLine.charAt(ji);
        }
        try {
            out.write("HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n");

            //System.out.println(subStrg.substring(0, id));
            if (subStrg.contains(":")) {
                int id = subStrg.indexOf(":");
                out.write(hashMap.get(subStrg.substring(0, id)).process(new Object[]{subStrg.substring(id + 1)}));
            } else {
                out.write(hashMap.get(subStrg).process());
            }
            out.close();
        } catch (Exception e) {
            notFound();
            
        }
    }

    private static void readHtml(String inputLine) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        int i = inputLine.indexOf('/') + 1;
        String urlInputLine = "";
        while (!urlInputLine.endsWith(".html") && i < inputLine.length()) {
            urlInputLine += (inputLine.charAt(i++));
        }
        try {
            BufferedReader readerFile = new BufferedReader(new FileReader(classLoader.getResource(urlInputLine).getFile()));
            String page = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n"
                    + "\r\n";
            out.println(page);
            while (readerFile.ready()) {
                out.println(readerFile.readLine());
            }
            out.close();
            System.out.println(out.toString());
        } catch (Exception e) {
            notFound();
            //System.err.println("Err: not is possible read the html " + urlInputLine);
        }
    }

    private static void readJpg(String inputLine, Socket clientSocket) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        String urlInputLine = "";
        int i = inputLine.indexOf('/') + 1;
        while (!urlInputLine.endsWith(".PNG") && i < inputLine.length()) {
            urlInputLine += (inputLine.charAt(i++));
        }
        try {

            File image = new File(classLoader.getResource(urlInputLine).getFile());
            BufferedImage bImage = ImageIO.read(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "PNG", bos);
            byte[] github = bos.toByteArray();
            DataOutputStream outImg = new DataOutputStream(clientSocket.getOutputStream());
            outImg.writeBytes("HTTP/1.1 200 OK \r\n");
            outImg.writeBytes("Content-Type: image/PNG\r\n");
            outImg.writeBytes("Content-Length: " + github.length);
            outImg.writeBytes("\r\n\r\n");
            outImg.write(github);
            outImg.close();
            out.println(outImg.toString());
        } catch (Exception e) {
            notFound();
            
        }

    }

    private static void notFound() {
        try {
            BufferedReader readerFile = new BufferedReader(new FileReader(classLoader.getResource("notFound.html").getFile()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            String page = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n"
                    + "\r\n";
            out.println(page);
            while (readerFile.ready()) {
                out.println(readerFile.readLine());
            }
            out.close();
        } catch (IOException ex) {
            System.err.println("Err:");
        }
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 40567; // returns default port if heroku-port isn't set (i.e. on localhost)
    }

}
