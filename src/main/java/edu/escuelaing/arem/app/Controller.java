/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.app;

import edu.escuelaing.arem.app.App;

/**
 *
 * @author Nicolas
 */
public class Controller {

    public static void main(String[] args) throws Exception {
        App.initialize();
        App.listen();
    }
}
