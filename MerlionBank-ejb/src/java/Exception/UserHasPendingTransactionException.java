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
public class UserHasPendingTransactionException extends Exception {

   
    public UserHasPendingTransactionException() {
        super();
    }
    
    public UserHasPendingTransactionException(String msg) {
        super(msg);
    }
}
