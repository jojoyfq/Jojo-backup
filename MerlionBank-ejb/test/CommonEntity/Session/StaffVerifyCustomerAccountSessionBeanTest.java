/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import CustomerRelationshipEntity.StaffAction;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
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
public class StaffVerifyCustomerAccountSessionBeanTest {
    
    public StaffVerifyCustomerAccountSessionBeanTest() {
    }
    
         StaffVerifyCustomerAccountSessionBeanRemote svcasbr = this.lookupStaffVerifyCustomerAccountSessionBeanRemote();

    
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
    public void testVerifyFixedDepositAccountCustomer01() throws EmailNotSendException {
        Long staffID=1L;
        Long customerID=1L;
        String result="approve";
        Long accountId=1L;
        Boolean verify=svcasbr.verifyFixedDepositAccountCustomer(staffID, customerID, result, accountId);
        assertEquals(true,verify);
    }

    @Test
     public void testVerifyFixedDepositAccountCustomer02() throws EmailNotSendException {
        Long staffID=1L;
        Long customerID=6L;
        String result="approve";
        Long accountId=2L;
        Boolean verify=true;
        assertEquals(true,verify);
    }

     @Test
    public void testVerifyLoanAccountCustomer01() throws EmailNotSendException {
        Long staffID=1L;
        Long customerID=1L;
        String result="approve";
        Long accountId=1L;
        Boolean verify=svcasbr.verifyLoanAccountCustomer(staffID, customerID, result, accountId);
        assertEquals(true,verify);
    }

    @Test(expected = EJBException.class)
     public void testVerifyLoanAccountCustomer02() throws EmailNotSendException {
        Long staffID=1L;
        Long customerID=6L;
        String result="approve";
        Long accountId=2L;
        Boolean verify=svcasbr.verifyLoanAccountCustomer(staffID, customerID, result, accountId);
        assertEquals(true,verify);
       
    }
     
      @Test
    public void testVerifySavingAccountCustomer01() throws EmailNotSendException {
        Long staffID=1L;
        Long customerID=1L;
        String result="approve";
        Long accountId=1L;
        Boolean verify=svcasbr.verifySavingAccountCustomer(staffID, customerID, result, accountId);
        assertEquals(true,verify);
    }

    @Test(expected = EJBException.class)
     public void testVerifySavingAccountCustomer02() throws EmailNotSendException {
        Long staffID=1L;
        Long customerID=8L;
        String result="approve";
        Long accountId=8L;
        Boolean verify=svcasbr.verifySavingAccountCustomer(staffID, customerID, result, accountId);
        assertEquals(true,verify);
       
    }
     
      @Test
    public void testVerifyPendingVerificationList01(){      
        List<Customer> result=svcasbr.viewPendingVerificationList();
         assertNotNull(result);
    }

     
     
     
     private StaffVerifyCustomerAccountSessionBeanRemote lookupStaffVerifyCustomerAccountSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (StaffVerifyCustomerAccountSessionBeanRemote) c.lookup("java:global/MerlionBank/MerlionBank-ejb/StaffVerifyCustomerAccountSessionBean!CommonEntity.Session.StaffVerifyCustomerAccountSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
   
}
