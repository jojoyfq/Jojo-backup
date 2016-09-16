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
public class UserHasNoSavingAccountException extends Exception {

    public UserHasNoSavingAccountException() {
        super();
    }

    
    public UserHasNoSavingAccountException(String msg) {
        super(msg);
    }
}
