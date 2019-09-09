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
 * Esta clase es la encargada de manejar todo el framework.
 */
public class App {

    public static ServerSocket serverSocket = null;
    private static Socket clientSocket;
    private static HashMap<String, Handler> hashMap = new HashMap<String, Handler>();
    private static PrintWriter out;
    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    private static String requestUrl = "";

    /**
     * Este metodo se encarga de guardar en el hasmap cada metodo.
     */
    public static void initialize() {
        try {            
            File archivo = new File(System.getProperty("user.dir") + "/src/main/java/edu/escuelaing/arem/annotation");
            File[] ficheros = archivo.listFiles();            
            for (File ruta : ficheros) {
                String name = ruta.getName();
                name = "edu.escuelaing.arem.annotation." + name.substring(0, name.indexOf("."));
                Class<?> c = Class.forName(name);
                for (Method m : c.getMethods()) {
                    if (m.getAnnotations().length > 0) {                        
                        Class[] params = m.getParameterTypes();
                        hashMap.put("/pojo/" + m.getAnnotation(Web.class).value(),
                            new HandlerStatic(c.getDeclaredMethod(m.getName(), params)));
                    }
                }
            }
            }catch (Exception e) {
            e.printStackTrace();
        }

        }
        /**
         * Este metodo se encarga de crear el servidor para que escuche por un
         * puerto determinado.
         */
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

    /**
     * Este metodo se encarga de buscar la pagina notFound y mostrarla en el
     * browser.
     */
    public static void notFound() {
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
            System.err.println("Error:");
        }
    }

    /**
     * Este metodo se encarga de buscar la pagina index y mostrarla en el
     * browser.
     */
    public static void indexPage() {
        try {
            BufferedReader readerFile = new BufferedReader(new FileReader(classLoader.getResource("index.html").getFile()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            String page = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n"
                    + "\r\n";
            out.println(page);
            while (readerFile.ready()) {
                out.println(readerFile.readLine());
            }
            out.close();
        } catch (IOException ex) {
            System.err.println("Error:");
        }
    }

    /**
     * Este metodo se encarga de comparar la peticion y enviarla al metodo
     * correspondiente.
     *
     * @throws IOException
     */
    public static void postPage() throws IOException {
        if (requestUrl.contains("html")) {
            readPage(requestUrl);
        } else if (requestUrl.contains("/pojo")) {
            readApps(requestUrl);
        } else if (requestUrl.contains("PNG")) {
            readImages(requestUrl, clientSocket);
        } else {
            notFound();
        }

    }

    /**
     * Este metodo se encarga de mostrar las peticiones del cliente.
     *
     * @param clientSocket
     * @throws IOException
     */
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

    /**
     * Este metodo se encarga de llamar al pojo para mostrar una pagina en el
     * browser.
     *
     * @param inputLine
     * @throws IOException
     */
    public static void readApps(String inputLine) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        int idApps = inputLine.indexOf("/pojo");
        String subStrg = "";
        System.out.println(inputLine + "  line ");
        for (int ji = idApps; ji < inputLine.length() && inputLine.charAt(ji) != ' '; ++ji) {
            subStrg += inputLine.charAt(ji);
        }
        try {
            out.write("HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n");
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

    /**
     * Este metodo se encarga de buscar la pagina con extension html y mostrarla
     * en el browser.
     */
    public static void readPage(String inputLine) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        int pag = inputLine.indexOf('/') + 1;
        String urlInputLine = "";
        while (!urlInputLine.endsWith(".html") && pag < inputLine.length()) {
            urlInputLine += (inputLine.charAt(pag++));
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
        }
    }

    /**
     * Este metodo se encarga de buscar la pagina con extension PNG y mostrarla
     * en el browser.
     */
    public static void readImages(String inputLine, Socket clientSocket) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        String urlInputLine = "";
        int img = inputLine.indexOf('/') + 1;
        while (!urlInputLine.endsWith(".PNG") && img < inputLine.length()) {
            urlInputLine += (inputLine.charAt(img++));
        }
        try {
            File image = new File(classLoader.getResource(urlInputLine).getFile());
            BufferedImage bImage = ImageIO.read(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "PNG", bos);
            byte[] imagen = bos.toByteArray();
            DataOutputStream outImg = new DataOutputStream(clientSocket.getOutputStream());
            outImg.writeBytes("HTTP/1.1 200 OK \r\n");
            outImg.writeBytes("Content-Type: image/PNG\r\n");
            outImg.writeBytes("Content-Length: " + imagen.length);
            outImg.writeBytes("\r\n\r\n");
            outImg.write(imagen);
            outImg.close();
            out.println(outImg.toString());
        } catch (Exception e) {
            notFound();

        }

    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 40567;
    }

}
