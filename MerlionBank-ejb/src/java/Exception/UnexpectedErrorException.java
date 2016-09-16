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
public class UnexpectedErrorException extends Exception{
    private static final long serialVersionUID = 1L;

    public UnexpectedErrorException() {
        super();
    }
    
    public UnexpectedErrorException(String msg) {
        super(msg);
    }
}
