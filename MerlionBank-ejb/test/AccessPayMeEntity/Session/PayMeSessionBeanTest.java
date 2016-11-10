/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccessPayMeEntity.Session;

import Exception.ListEmptyException;
import Exception.PasswordNotMatchException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.LoanType;
import LoanEntity.Session.LoanApplicationSessionBeanRemote;
import PayMeEntity.Session.PayMeSessionBeanRemote;
import java.util.List;
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
public class PayMeSessionBeanTest {

    public PayMeSessionBeanTest() {
    }

    PayMeSessionBeanRemote scvasbr = this.lookupPayMeSessionBeanRemote();

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
    public void testCheckLogin01() throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException {
        String customerIc = "S9376011E";
        String password = "P@ssw0rd";
        boolean result = scvasbr.checkLogin(customerIc, password);
        assertEquals(true, result);
    }

    @Test(expected = PasswordNotMatchException.class)
    public void testCheckLogin02() throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException {
        String customerIc = "S9376011E";
        String password = "ruijialalala";
        boolean result = scvasbr.checkLogin(customerIc, password);
        assertEquals(false, result);
    }

    @Test
    public void testCheckPayMeLogin01() {
        String phoneNumber = "6581006551";
        String password = "P@ssw0rd";
        boolean result = scvasbr.checkPayMeLogin(phoneNumber, password);
        assertEquals(true, result);
    }

    @Test
    public void testCheckPayMeLogin02() {
        String phoneNumber = "6581006551";
        String password = "rujialalalla";
        boolean result = scvasbr.checkPayMeLogin(phoneNumber, password);
        assertEquals(false, result);
    }

    @Test
    public void testCreatePayMe() {
        String ic="S9376011E";
        String savingAccountNo="1";
        String phoneNumber="6581006551";
        String paymePassword="P@ssw0rd";
        boolean result = scvasbr.createPayMe(ic, savingAccountNo, phoneNumber, paymePassword);
        assertEquals(false, result);
    }
    
    @Test
    public void testGetBalance() {
        String phoneNumber="6581006551";
        
        String result = scvasbr.getBalance(phoneNumber);
       assertNotNull(result);
    }
    
    @Test
    public void testGetPhoneNumber() {
        String customerIc="S9376011E";       
        String result = scvasbr.getPhoneNumber(customerIc);
        assertNotNull(result);
        
    }
    
     @Test
    public void testGetSavingAccountString01() throws UserHasNoSavingAccountException {
        String customerIc = "S9376011E";
        List<String> result = scvasbr.getSavingAccountString(customerIc);
        assertNotNull(result);
    }

    @Test(expected = UserHasNoSavingAccountException.class)
   public void testGetSavingAccountString02() throws UserHasNoSavingAccountException {
        String customerIc = "S9276011E";
        List<String> result = scvasbr.getSavingAccountString(customerIc);
        assertNull(result);
    }
   
    @Test
    public void testVerifyTwoFactorAuthentication01(){
        String ic="S9376011E";
        String inputCode="000000";
        Boolean result=scvasbr.verifyTwoFactorAuthentication(ic, inputCode);
        assertEquals(true, result);
    }

    @Test(expected = EJBException.class)
   public void testVerifyTwoFactorAuthentication02(){
       String ic="S9307011E";
        String inputCode="000001";
         Boolean result=scvasbr.verifyTwoFactorAuthentication(ic, inputCode);
        assertEquals(true, result);
    }
  


    private PayMeSessionBeanRemote lookupPayMeSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (PayMeSessionBeanRemote) c.lookup("java:global/MerlionBank/MerlionBank-ejb/PayMeSessionBean!PayMeEntity.Session.PayMeSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
