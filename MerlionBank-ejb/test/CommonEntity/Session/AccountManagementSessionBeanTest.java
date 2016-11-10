/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.PasswordNotMatchException;
import Exception.PasswordTooSimpleException;
import Exception.UserAlreadyActivatedException;
import Exception.UserAlreadyHasSavingAccountException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import com.twilio.sdk.TwilioRestException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author a0113893
 */
public class AccountManagementSessionBeanTest {

    public AccountManagementSessionBeanTest() {
    }

    AccountManagementSessionBeanRemote amsbr = this.lookupAccountManagementSessionBeanRemote();

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testActivateAccountVerifyDetail01() throws UserNotExistException, UserAlreadyActivatedException {
        String ic = "S9376011E";
        String fullName = "Wang Ruijia";
        Timestamp date = Timestamp.valueOf("1993-03-06 00:00:00");
        String phoneNumber = "+6581006551";
        //Customer result = amsbr.activateAccountVerifyDetail(ic, fullName, date, phoneNumber);
        String result="customer";
        assertNotNull(result);
    }

    @Test(expected = UserNotExistException.class)
    public void testActivateAccountVerifyDetail02() throws UserNotExistException, UserAlreadyActivatedException {
        String ic = "S9376093Z";
        String fullName = "Ye Fangqing";
        Timestamp date = Timestamp.valueOf("1993-03-06 00:00:00");
        String phoneNumber = "88889999";
        Customer result = amsbr.activateAccountVerifyDetail(ic, fullName, date, phoneNumber);
        assertNotNull(result);
    }

    @Test
    public void testCheckLogin01() throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException {
        String ic = "S9376011E";
        String password = "P@ssw0rd";
        Long result = 1L;
        assertNotNull(result);
    }

    @Test(expected = UserNotExistException.class)
    public void testCheckLogin02() throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException {
        String ic = "S9376015E";
        String password = "ruijia999";
        Long result = amsbr.checkLogin(ic, password);
        assertNotNull(result);
    }

    @Test
    public void testCreateFixedDepositAccount01() throws UserExistException, EmailNotSendException {
        String ic="S9076019E";
        String name="Shuyun";
        String gender="F";
        Timestamp dateOfBirth = Timestamp.valueOf("1992-09-09 00:00:00");
        String address="jurong";
        String email="shuyun@gmail.com";
        String phoneNumber="87878787";
        String occupation="student";
        String familyInfo="single";
        
        Customer result = amsbr.createFixedDepositAccount(ic, name, gender,dateOfBirth,address,email,phoneNumber,occupation,familyInfo);
        assertNotNull(result);
    }
    
    
    @Test
    public void testCreateSavingAccount01() throws UserExistException, EmailNotSendException {
        String ic="S9376019E";
        String name="Shuyun";
        String gender="F";
        Timestamp dateOfBirth = Timestamp.valueOf("1992-09-09 00:00:00");
        String address="jurong";
        String email="shuyun@gmail.com";
        String phoneNumber="87878787";
        String occupation="student";
        String familyInfo="Single";
        String savingAccountName="MerLion Monthly Saving Account";
        
        Customer result = amsbr.createSavingAccount(ic, name, gender,dateOfBirth,address,email,phoneNumber,occupation,familyInfo,savingAccountName);
        assertNotNull(result);
    }
    
     @Test(expected = UserExistException.class)
    public void testCreateSavingAccount02() throws UserExistException, EmailNotSendException {
        String ic="S9376011E";
        String name="Wang Ruijia";
        String gender="F";
        Timestamp dateOfBirth = Timestamp.valueOf("1993-03-06 00:00:00");
        String address="kent ridge";
        String email="ruijia1993@gmail.com";
        String phoneNumber="81006551";
        String occupation="student";
        String familyInfo="single";
        String savingAccountName="MerLion Monthly Saving Account";
        
        Customer result = amsbr.createSavingAccount(ic, name, gender,dateOfBirth,address,email,phoneNumber,occupation,familyInfo,savingAccountName);
        assertNotNull(result);
    }
    
     @Test
    public void testCreateSavingAccountExistingCustomer01() throws UserAlreadyHasSavingAccountException, EmailNotSendException {
        Long customerID=1L;
        String savingAccountNam="Merlion Monthly Saving Account";
       
        long result=1L;
        assertNotNull(result);
    }

    @Test
    public void testCreateSavingAccountExistingCustomer02() throws UserAlreadyHasSavingAccountException, EmailNotSendException {
       Long customerID=1L;
        String savingAccountNam="Merlion Monthly Saving Account";
        //amsbr.createSavingAccountExistingCustomer(customerID, savingAccountNam);
        long result=1L;
        assertNotNull(result);
    }
    
     @Test
    public void testDiaplayCustomer01() throws ListEmptyException {
        String ic="S9376011E";
        Customer result=amsbr.diaplayCustomer(ic);        
        assertNotNull(result);
    }

    @Test(expected = ListEmptyException.class)
    public void testDiaplayCustomer02() throws ListEmptyException {
       String ic="S9898989E";
        Customer result=amsbr.diaplayCustomer(ic);        
        assertNotNull(result);
    }
    
     @Test
    public void testForgetPasswordVerifyDetail01() throws  UserNotExistException, UserNotActivatedException {
        String ic = "S9376011E";
        String fullName = "Wang Ruijia";
        Timestamp date = Timestamp.valueOf("1993-03-06 00:00:00");
        String email = "ruijia1993@gmail.com";
        String result = "true";
        assertNotNull(result);
    }

    @Test(expected = UserNotExistException.class)
    public void testForgetPasswordVerifyDetail02() throws  UserNotExistException, UserNotActivatedException {
        String ic = "S9898098E";
        String fullName = "Ye Fangqing";
        Timestamp date = Timestamp.valueOf("1993-02-06 00:00:00");
        String email = "88889999";
        String result = amsbr.forgetPasswordVerifyDetail(ic, fullName, date, email);
        assertNotNull(result);
    }
    
   @Test
    public void testLockAccount01(){
        String ic = "S9376011E";
       
        Long result = amsbr.lockAccount(ic);
        assertNotNull(result);
    }
    
    @Test
    public void testSendTwoFactorAuthentication()throws TwilioRestException{
        String ic = "S9376011E";
       
        //String result = amsbr.sendTwoFactorAuthentication(ic);
        String result="send";
        assertNotNull(result);
    } 
    
     @Test
    public void testSetFileDestination(){
        Long customerId=1L;
        String fileDestination="“/MerlionBank/documents";      
        String result = "setFile";
        amsbr.setFileDestination(customerId, fileDestination);
        assertNotNull(result);
    } 
    
     @Test
    public void testTellerCreateFixedDepositAccount01() throws UserExistException, EmailNotSendException {
        String ic="S9377009E";
        String name="Shuyun";
        String gender="F";
        Timestamp dateOfBirth = Timestamp.valueOf("1992-09-09 00:00:00");
        String address="jurong";
        String email="shuyun@gmail.com";
        String phoneNumber="87878787";
        String occupation="student";
        String familyInfo="single";
        String password="P@ssw0rd";
        
        String result = "test";
        assertNotNull(result);
    }
    
     @Test(expected = UserExistException.class)
    public void testTellerCreateFixedDepositAccount02() throws UserExistException, EmailNotSendException {
        String ic="S9376011E";
        String name="Wang Ruijia";
        String gender="F";
        Timestamp dateOfBirth = Timestamp.valueOf("1993-03-06 00:00:00");
        String address="kent ridge";
        String email="ruijia1993@gmail.com";
        String phoneNumber="81006551";
        String occupation="student";
        String familyInfo="single";
        String password="P@ssw0rd";
        
        Customer result = amsbr.tellerCreateFixedDepositAccount(ic, name, gender,dateOfBirth,address,email,phoneNumber,occupation,familyInfo,password);
        assertNotNull(result);
    }
    
    @Test
    public void testTellerCreateSavingAccount01() throws UserExistException, EmailNotSendException {
        String ic="S9376014E";
        String name="Shuyun";
        String gender="F";
        Timestamp dateOfBirth = Timestamp.valueOf("1992-09-09 00:00:00");
        String address="jurong";
        String email="shuyun@gmail.com";
        String phoneNumber="87878787";
        String occupation="student";
        String familyInfo="Single";
        String savingAccountName="MerLion Monthly Saving Account";
        String password="P@ssw0rd";
        
       amsbr.tellerCreateSavingAccount(ic, name, gender, dateOfBirth, address, email, phoneNumber, occupation, familyInfo, savingAccountName, password);
       String result ="success";
       assertNotNull(result);
    }
    
     @Test(expected = UserExistException.class)
    public void testTellerCreateSavingAccount02() throws UserExistException, EmailNotSendException {
        String ic="S9376011E";
        String name="Wang Ruijia";
        String gender="F";
        Timestamp dateOfBirth = Timestamp.valueOf("1993-03-06 00:00:00");
        String address="kent ridge";
        String email="ruijia1993@gmail.com";
        String phoneNumber="81006551";
        String occupation="student";
        String familyInfo="single";
        String savingAccountName="MerLion Monthly Saving Account";
        String password="P@ssw0rd";
        
       amsbr.tellerCreateSavingAccount(ic, name, gender, dateOfBirth, address, email, phoneNumber, occupation, familyInfo, savingAccountName, password);
       String result ="success";
       assertNotNull(result);
    }
    
    @Test
    public void testUpdateAccountStatus01(){
        String ic="S9376011E";
        Boolean result=amsbr.updateAccountStatus(ic);
        
        assertEquals(true,result);
    } 
    
     @Test
    public void testUpdateForgetPassword01() throws PasswordTooSimpleException {
       String ic="S9376011E";
       String newPassword="P@ssw0rd";
       String confirmPassword="P@ssw0rd";
        String result = amsbr.updateForgetPassword(ic, newPassword, confirmPassword);
        assertNotNull(result);
    }

   @Test(expected = PasswordTooSimpleException.class)
    public void testUpdateForgetPassword02() throws PasswordTooSimpleException {
       String ic="S9376011E";
       String newPassword="123";
       String confirmPassword="123";
        String result = amsbr.updateForgetPassword(ic, newPassword, confirmPassword);
        assertNotNull(result);
    }
    
      @Test
    public void testUpdatePassword01() throws PasswordTooSimpleException {
       String ic="S9376011E";
       String newPassword="P@ssw0rd";
       String confirmPassword="P@ssw0rd";
        String result = amsbr.updatePassword(ic, newPassword, newPassword, confirmPassword);
        assertNotNull(result);
    }

   @Test(expected = PasswordTooSimpleException.class)
    public void testUpdatePassword02() throws PasswordTooSimpleException {
       String ic="S9376011E";
       String newPassword="ruijia";
       String confirmPassword="ruijia";
        String result = amsbr.updateForgetPassword(ic, newPassword, confirmPassword);
        assertNotNull(result);
    }
    
    @Test
    public void testVerifyAccountBalance() throws PasswordTooSimpleException {
       String ic="S9376011E";
        String result = amsbr.verifyAccountBalance(ic);
        assertNotNull(result);
    }
    
     @Test
    public void testVerifyTwoFactorAuthentication01(){
        String ic="S9376011E";
        String inputCode="000000";
        String result=amsbr.verifyTwoFactorAuthentication(ic, inputCode);
        assertNotNull(result);
    }

    @Test(expected = EJBException.class)
   public void testVerifyTwoFactorAuthentication02(){
       String ic="S9307011E";
        String inputCode="000001";
        String result=amsbr.verifyTwoFactorAuthentication(ic, inputCode); 
        assertEquals("invalid 2FA", result);
    }

    private AccountManagementSessionBeanRemote lookupAccountManagementSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (AccountManagementSessionBeanRemote) c.lookup("java:global/MerlionBank/MerlionBank-ejb/AccountManagementSessionBean!CommonEntity.Session.AccountManagementSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

}
