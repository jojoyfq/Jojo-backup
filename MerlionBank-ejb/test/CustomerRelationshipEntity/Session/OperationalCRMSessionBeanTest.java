/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerRelationshipEntity.Session;

import CommonEntity.Customer;
import CommonEntity.Session.StaffCustomerViewActionSessionBeanRemote;
import Exception.UserNotExistException;
import java.sql.Timestamp;
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
public class OperationalCRMSessionBeanTest {

    public OperationalCRMSessionBeanTest() {
    }

    OperationalCRMSessionBeanRemote scvasbr = this.lookupOperationalCRMSessionBeanRemote();

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
    public void testSearchCustomer01() throws UserNotExistException {
        String ic = "S9376011E";
        Customer result = scvasbr.searchCustomer(ic);
        assertNotNull(result);
    }

    @Test(expected = UserNotExistException.class)
    public void testSearchCustomer02() throws UserNotExistException {
        String ic = "S9898989E";
        Customer result = scvasbr.searchCustomer(ic);
        assertNotNull(result);
    }

    @Test
    public void testUpdateProfile() {
        Long staffID=1L;
        Long customerID=1L;
        String ic="S9376011E";
        String name="Wang Ruijia";
        Timestamp date = Timestamp.valueOf("1993-03-06 00:00:00");
        String address="kent ridge";
        String email="ruijia1993@gmail.com";
        String phoneNumber="81006551";
        String occupation="student";
        String familyInfo="single";
        String financialGoal="none";
        scvasbr.updateProfile(staffID, customerID, ic, name, date, address, email, phoneNumber, occupation, familyInfo, financialGoal);
         String result="update";
        assertNotNull(result);
    }

    private OperationalCRMSessionBeanRemote lookupOperationalCRMSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (OperationalCRMSessionBeanRemote) c.lookup("java:global/MerlionBank/MerlionBank-ejb/OperationalCRMSessionBean!CustomerRelationshipEntity.Session.OperationalCRMSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

}
