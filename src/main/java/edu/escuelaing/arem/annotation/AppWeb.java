/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.annotation;

import edu.escuelaing.arem.appWeb.Web;
import java.text.DecimalFormat;

/**
 *
 * @author Nicolas
 */
public class AppWeb {

    public static DecimalFormat round = new DecimalFormat("#.00");
    
    @Web("param")
    public static String greeting(String name) {        
        name = name.replace("?Data=","");
        try {
            double numero = Double.parseDouble(name);
            return "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "  <meta charset=\"utf-8\" />\n"
                    + "  <title>Bievenida</title>  \n"
                    + "</head>\n"
                    + "<body style=\"text-align: center\">   \n"
                    + "    <h1>La raiz cuadrada de " + round.format(numero) + " es "+ round.format(Math.sqrt(numero))+" </h1>	\n"                                      
                    + "</body>\n"
                    + "</html>\n"
                    + "";
        } catch (Exception e) {
            return "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "  <meta charset=\"utf-8\" />\n"
                    + "  <title>Bievenida</title>  \n"
                    + "</head>\n"
                    + "<body style=\"text-align: center\">   \n"
                    + "    <h1>Hola " + name + "! , Bienvenid@ </h1>	\n"                                   
                    + "</body>\n"
                    + "</html>\n"
                    + "";
        }
    }

}
