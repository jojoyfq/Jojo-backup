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
import com.twilio.sdk.TwilioRestException;
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

    public String getPhoneNumber(String ic);

    public String getBalance(String ic);

    public boolean verifyTwoFactorAuthentication(String ic, String inputCode);

    public String sendTwoFactorAuthentication(String ic) throws TwilioRestException;

    public PayMe createPayMe(String ic, String savingAccountNo, String phoneNumber, String paymePassword);

    public boolean checkPayMeLogin(String phoneNumber, String password);

    public boolean topUp(String phoneNumber, String amount);

    public boolean sendToMyAccount(String phoneNumber, String amount);
    
    public boolean payMeSent(String phoneNumber, String otherPhone, String amount);
}
