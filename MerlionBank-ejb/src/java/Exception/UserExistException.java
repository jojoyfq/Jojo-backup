/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Exception;

/**
 *
 * @author songhan
 */
public class UserExistException extends Exception{
    private static final long serialVersionUID = 1L;

    public UserExistException() {
        super();
    }
    
    public UserExistException(String msg) {
        super(msg);
    }
}
