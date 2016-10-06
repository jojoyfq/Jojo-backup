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
public class UserHasDebitCardException extends Exception {

    
    public UserHasDebitCardException() {
        super();
    }

    public UserHasDebitCardException(String msg) {
        super(msg);
    }
}
