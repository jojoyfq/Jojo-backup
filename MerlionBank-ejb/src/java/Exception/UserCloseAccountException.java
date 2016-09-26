/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

/**
 *
 * @author Bella
 */
public class UserCloseAccountException extends Exception {

    /**
     * Creates a new instance of <code>UserCloseAccountException</code> without
     * detail message.
     */
    public UserCloseAccountException() {
        super();
    }

    
    public UserCloseAccountException(String msg) {
        super(msg);
    }
}
