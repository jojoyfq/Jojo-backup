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
public class NotEnoughAmountException extends Exception{
    private static final long serialVersionUID = 1L;

    public NotEnoughAmountException() {
        super();
    }
    
    public NotEnoughAmountException(String msg) {
        super(msg);
    }
}
