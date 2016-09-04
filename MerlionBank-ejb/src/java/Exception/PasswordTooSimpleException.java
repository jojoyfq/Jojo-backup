/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Exception;

/**
 *
 * @author a0113893
 */
public class PasswordTooSimpleException extends Exception {

    public PasswordTooSimpleException() {
        super();
    }
    
    public PasswordTooSimpleException(String msg) {
        super(msg);
    }
}    

