/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;

import Exception.EmailNotSendException;
import Exception.PasswordNotMatchException;
import Exception.PasswordTooSimpleException;
import Exception.UserAlreadyActivatedException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import com.twilio.sdk.TwilioRestException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface AccountManagementSessionBeanLocal {

//Create Account   

    public void createSavingAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String savingAccountName) throws UserExistException, EmailNotSendException;

//Update Account Details  
    public Customer diaplayCustomer(String ic);

    public void updateProfile(String ic, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialGoal) throws UserExistException;

//Activation function
    public String activateAccountVerifyDetail(String ic, String fullName, Date dateOfBirth, String phoneNumber) throws UserNotExistException, UserAlreadyActivatedException;
 

    public String verifyAccountBalance(String ic);


    
    public String updatePassword(String ic, String oldPassword, String newPassword, String confirmPassword) throws PasswordTooSimpleException;
public boolean updateAccountStatus(String ic);

//Forget Password
    public String forgetPasswordVerifyDetail(String ic, String fullName, Date dateOfBirth, String email) throws UserNotExistException, UserNotActivatedException;

    public String sendTwoFactorAuthentication(String ic) throws TwilioRestException;

    public String verifyTwoFactorAuthentication(String ic, String inputCode);

    public String updateForgetPassword(String ic, String newPassword, String confirmPassword) throws PasswordTooSimpleException;

//Log in
     public Long checkLogin(String ic, String password) throws UserNotExistException, PasswordNotMatchException,UserNotActivatedException;

 
}
