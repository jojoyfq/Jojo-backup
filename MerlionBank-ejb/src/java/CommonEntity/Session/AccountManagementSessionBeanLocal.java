/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;

import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.LoanTermInvalidException;
import Exception.PasswordNotMatchException;
import Exception.PasswordTooSimpleException;
import Exception.UserAlreadyActivatedException;
import Exception.UserAlreadyHasSavingAccountException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.LoanType;
import com.twilio.sdk.TwilioRestException;
import java.math.BigDecimal;
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

    public Customer createSavingAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String savingAccountName) throws UserExistException, EmailNotSendException;

//Create Another Saving Account
    public void createSavingAccountExistingCustomer(Long customerID, String savingAccountName) throws UserAlreadyHasSavingAccountException, EmailNotSendException;
    
//Update Account Details  
public Customer diaplayCustomer(String ic) throws ListEmptyException;
    public void updateProfile(String ic, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialGoal) throws UserExistException;

//Activation function
    public Customer activateAccountVerifyDetail(String ic, String fullName, Date dateOfBirth, String phoneNumber) throws UserNotExistException, UserAlreadyActivatedException;
 

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
 public Long lockAccount(String customerIc);
 public Customer diaplayCustomerId(Long id);

//Create Fixed Deposit Account - 1st page - create online banking account
    public Customer createFixedDepositAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo) throws UserExistException, EmailNotSendException;
 //Create Fixed Deposit Account - 2nd page - configure fixed deposit account 
    public Long createFixedAccount(Customer customer, BigDecimal amount, String duration)throws EmailNotSendException;

 


//Teller Create Fixed Deposit Account - 1st page - create online banking account
    //Teller Create Loan Account - 1st page - create online banking account
    public Customer tellerCreateFixedDepositAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo,String enterPassword) throws UserExistException, EmailNotSendException;

 //Teller Create saving Account 
    public void tellerCreateSavingAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String savingAccountName,String enterPassword) throws UserExistException, EmailNotSendException;

  //Create Loan Account - 2nd page - display different type 
       //Create Loan Account - 2nd page - display different type 
   // public List<LoanType> displayLoanType(String type) throws ListEmptyException;
    
   ///Create Loan Account - 3rd page - configure home loan  
   //public Long createLoanAccount(Customer customer,BigDecimal monthlyIncome,Long loanTypeId,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate )throws EmailNotSendException,LoanTermInvalidException;

    //Teller Create Loan Account - 3nd page - configureloan
   // public Long StaffCreateLoanAccount(Long customerId,BigDecimal monthlyIncome,Long loanTypeId,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate )throws LoanTermInvalidException;
}
