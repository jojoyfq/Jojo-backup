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
public class NoTransactionRecordFoundException extends Exception {

    
    public NoTransactionRecordFoundException() {
        super();
    }

    
    public NoTransactionRecordFoundException(String msg) {
        super(msg);
    }
}
