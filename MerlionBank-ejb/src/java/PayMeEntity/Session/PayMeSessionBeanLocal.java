/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PayMeEntity.Session;

import Exception.PasswordNotMatchException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import javax.ejb.Local;

/**
 *
 * @author Bella
 */
@Local
public interface PayMeSessionBeanLocal {
    public boolean checkLogin(String ic, String password) throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException;
}
