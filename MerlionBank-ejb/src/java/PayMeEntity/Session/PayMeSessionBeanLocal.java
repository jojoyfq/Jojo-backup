/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PayMeEntity.Session;

import Exception.PasswordNotMatchException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import PayMeEntity.PayMe;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Bella
 */
@Local
public interface PayMeSessionBeanLocal {
    public boolean checkLogin(String ic, String password) throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException;
    public List<String> getSavingAccountString(Long customerID) throws UserHasNoSavingAccountException;
    public String getPhoneNumber(Long customerID);
    public String getBalance(Long customerID);
    public PayMe createPayMe(Long customerID, String savingAccountNo, String paymePassword);
}
