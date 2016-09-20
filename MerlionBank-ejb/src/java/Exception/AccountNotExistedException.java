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
public class AccountNotExistedException extends Exception{
    private static final long serialVersionUID = 1L;

    public AccountNotExistedException() {
        super();
    }
    
    public AccountNotExistedException(String msg) {
        super(msg);
    }
}
