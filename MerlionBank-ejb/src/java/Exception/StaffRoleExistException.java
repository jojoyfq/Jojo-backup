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
public class StaffRoleExistException extends Exception{
    private static final long serialVersionUID = 1L;

    public StaffRoleExistException() {
        super();
    }
    
    public StaffRoleExistException(String msg) {
        super(msg);
    }
}
