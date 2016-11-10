/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CustomerRelationshipEntity.StaffAction;
import Exception.ListEmptyException;
import Exception.UserAlreadyActivatedException;
import Exception.UserNotExistException;
import java.sql.Timestamp;
import java.util.List;
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
public class StaffCustomerViewActionSessionBeanTest {
    
    public StaffCustomerViewActionSessionBeanTest() {
    }
    
     StaffCustomerViewActionSessionBeanRemote scvasbr = this.lookupStaffCustomerViewActionSessionBeanRemote();
    
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
    public void testViewStaffAction01() throws ListEmptyException {
        Long staffId=1L;
        List<StaffAction> result =scvasbr.viewStaffAction(staffId) ;
        assertNotNull(result);
    }

    @Test(expected = ListEmptyException.class)
     public void testViewStaffAction02() throws ListEmptyException {
        Long staffId=100L;
        List<StaffAction> result =scvasbr.viewStaffAction(staffId) ;
        assertNull(result);
    }
     
      @Test
    public void testViewCustomerAction01() throws ListEmptyException {
        Long customerId=1L;
        List<CustomerAction> result =scvasbr.viewCustomerAction(customerId);
        assertNotNull(result);
    }




    private StaffCustomerViewActionSessionBeanRemote lookupStaffCustomerViewActionSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (StaffCustomerViewActionSessionBeanRemote) c.lookup("java:global/MerlionBank/MerlionBank-ejb/StaffCustomerViewActionSessionBean!CommonEntity.Session.StaffCustomerViewActionSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
