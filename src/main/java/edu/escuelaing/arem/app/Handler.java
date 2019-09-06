/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.app;

/**
 *
 * @author Nicolas
 */
public interface Handler {

    public String process() throws Exception;

    public String process(Object[] arg) throws Exception;
}
