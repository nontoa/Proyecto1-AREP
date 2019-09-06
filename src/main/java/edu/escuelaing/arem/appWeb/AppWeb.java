/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.appWeb;


/**
 *
 * @author Nicolas
 */
public class AppWeb {

    @Web("greeting")
    public static String greeting() {
        return "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "  <meta charset=\"utf-8\" />\n"
                + "  <title>ARSW - Laboratorio 1</title>  \n"
                + "</head>\n"
                + "<body style=\"text-align: center\">   \n"
                + "    <h1>Hello world! ,  the POJOs works</h1>	\n"
                + "</body>\n"
                + "</html>\n"
                + "";
    }

    @Web("greetingparam")
    public static String greeting(String name) {
        return "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "  <meta charset=\"utf-8\" />\n"
                + "  <title>ARSW - Laboratorio 1</title>  \n"
                + "</head>\n"
                + "<body style=\"text-align: center\">   \n"
                + "    <h1>Hello " + name + "! ,  the POJOs works</h1>	\n"
                + "</body>\n"
                + "</html>\n"
                + "";
    }

}
