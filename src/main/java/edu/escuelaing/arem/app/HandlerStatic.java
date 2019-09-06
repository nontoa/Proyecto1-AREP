/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.app;

import java.lang.reflect.Method;

/**
 *
 * @author Nicolas
 */
public class HandlerStatic implements Handler {

    private Method method;

    public String process() throws Exception {
        return (String) method.invoke(method, null);
    }

    public String process(Object[] arg) throws Exception {
        return (String) method.invoke(method, arg);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public HandlerStatic(Method method) {
        this.method = method;
    }

}
