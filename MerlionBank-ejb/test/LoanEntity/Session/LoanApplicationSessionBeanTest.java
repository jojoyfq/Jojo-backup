/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import CustomerRelationshipEntity.Session.OperationalCRMSessionBeanRemote;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.LoanTermInvalidException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.LoanType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
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
public class LoanApplicationSessionBeanTest {

    public LoanApplicationSessionBeanTest() {
    }

    LoanApplicationSessionBeanRemote scvasbr = this.lookupLoanApplicationSessionBeanRemote();

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
    public void testStaffCreateLoanAccount01() throws LoanTermInvalidException {
        Long staffId = 1L;
        Long customerId = 1L;
        BigDecimal monthlyIncome = new BigDecimal(5000);
        Long loanTypeId = 2L;
        BigDecimal principal = new BigDecimal(50000);
        BigDecimal downpayment = new BigDecimal(0);
        Integer loanTerm = 60;
        String homeType = "";
        String homeAddress = "";
        Long postCode = 0L;
        String carModel = "";
        BigDecimal existingDebit = new BigDecimal(0);
        Long postalCode = 0L;
        String carMode = "";
        String institution = "NUS";
        String major = "Information System";
        Timestamp date = Timestamp.valueOf("2016-11-10 00:00:00");
        Long result = scvasbr.StaffCreateLoanAccount(staffId, customerId, monthlyIncome, loanTypeId, principal, downpayment, loanTerm, homeType, homeAddress, postCode, carModel, existingDebit, postalCode, carMode, institution, major, date);
        assertNotNull(result);
    }

    @Test(expected = LoanTermInvalidException.class)
    public void testStaffCreateLoanAccount02() throws LoanTermInvalidException {
        Long staffId = 1L;
        Long customerId = 1L;
        BigDecimal monthlyIncome = new BigDecimal(5000);
        Long loanTypeId = 2L;
        BigDecimal principal = new BigDecimal(50000);
        BigDecimal downpayment = new BigDecimal(0);
        Integer loanTerm = 480;
        String homeType = "";
        String homeAddress = "";
        Long postCode = 0L;
        String carModel = "";
        BigDecimal existingDebit = new BigDecimal(0);
        Long postalCode = 0L;
        String carMode = "";
        String institution = "NUS";
        String major = "Information System";
        Timestamp date = Timestamp.valueOf("2016-11-10 00:00:00");
        Long result = scvasbr.StaffCreateLoanAccount(staffId, customerId, monthlyIncome, loanTypeId, principal, downpayment, loanTerm, homeType, homeAddress, postCode, carModel, existingDebit, postalCode, carMode, institution, major, date);
        assertNotNull(result);
    }

    @Test
    public void testCreateNewLoanAccount01() throws EmailNotSendException, LoanTermInvalidException {

        Long customerId = 1L;
        BigDecimal monthlyIncome = new BigDecimal(5000);
        Long loanTypeId = 2L;
        BigDecimal principal = new BigDecimal(50000);
        BigDecimal downpayment = new BigDecimal(0);
        Integer loanTerm = 60;
        String homeType = "";
        String homeAddress = "";
        Long postCode = 0L;
        String carModel = "";
        BigDecimal existingDebit = new BigDecimal(0);
        Long postalCode = 0L;
        String carMode = "";
        String institution = "NUS";
        String major = "Information System";
        Timestamp date = Timestamp.valueOf("2016-11-10 00:00:00");
        Long result = scvasbr.createLoanAccount(customerId, monthlyIncome, loanTypeId, principal, downpayment, loanTerm, homeType, homeAddress, postCode, carModel, existingDebit, postalCode, carMode, institution, major, date);
        assertNotNull(result);
    }

    @Test(expected = LoanTermInvalidException.class)
    public void testCreateNewLoanAccount02() throws EmailNotSendException, LoanTermInvalidException {

        Long customerId = 1L;
        BigDecimal monthlyIncome = new BigDecimal(5000);
        Long loanTypeId = 2L;
        BigDecimal principal = new BigDecimal(50000);
        BigDecimal downpayment = new BigDecimal(0);
        Integer loanTerm = 480;
        String homeType = "";
        String homeAddress = "";
        Long postCode = 0L;
        String carModel = "";
        BigDecimal existingDebit = new BigDecimal(0);
        Long postalCode = 0L;
        String carMode = "";
        String institution = "NUS";
        String major = "Information System";
        Timestamp date = Timestamp.valueOf("2016-11-10 00:00:00");
        Long result = scvasbr.createLoanAccount(customerId, monthlyIncome, loanTypeId, principal, downpayment, loanTerm, homeType, homeAddress, postCode, carModel, existingDebit, postalCode, carMode, institution, major, date);
        assertNotNull(result);
    }

    @Test
    public void testDisplayLoanType01() throws ListEmptyException {
        String name = "home";
        List<LoanType> result = scvasbr.displayLoanType(name);
        assertNotNull(result);
    }

    @Test(expected = ListEmptyException.class)
    public void testDisplayLoanType02() throws ListEmptyException {
        String name = "bus";
        List<LoanType> result = scvasbr.displayLoanType(name);
        assertNull(result);
    }

    @Test
    public void testDisplayPackageDetail() {
        String name = "SIBOR Package";
        String result = scvasbr.displayPackageDetail(name);
        assertNotNull(result);
    }

    @Test
    public void testFindTypeByName() {
        String name = "SIBOR Package";
        LoanType result = scvasbr.findTypeByName(name);
        assertNotNull(result);
    }

    @Test
    public void findTypeIdByName() {
        String name = "SIBOR Package";
        Long result = scvasbr.findTypeIdByName(name);
        assertNotNull(result);
    }

    @Test
    public void testFixedCalculator() {
        BigDecimal amount=new BigDecimal(50000);
        Integer loanTerm=36;
        Double rate1=0.005;
        Double rate2=0.007;
        BigDecimal result = scvasbr.fixedCalculator(amount, loanTerm, rate1, rate2);
        assertNotNull(result);
    }
    
    @Test
    public void testSearchCustomer01() throws UserNotExistException, UserNotActivatedException {
        String customerIc = "S9376011E";
        Customer result = scvasbr.searchCustomer(customerIc);
        assertNotNull(result);
    }

    @Test(expected = UserNotExistException.class)
    public void testSearchCustomer02() throws UserNotExistException, UserNotActivatedException {
        String customerIc = "S9898989";
        Customer result = scvasbr.searchCustomer(customerIc);
        assertNotNull(result);
    }
    
    @Test
    public void testCalculateTDSRTest() {
        BigDecimal monthlyIncome=new BigDecimal(50000);
        BigDecimal existingDebit=new BigDecimal(10000);
      
        BigDecimal result = scvasbr.calculateTDSR(monthlyIncome, existingDebit);
        assertNotNull(result);
    }
    
     @Test
    public void testCalculateMSRTest() {
        BigDecimal monthlyIncome=new BigDecimal(50000);
        BigDecimal existingDebit=new BigDecimal(10000);
      
        BigDecimal result = scvasbr.calculateMSR(monthlyIncome, existingDebit);
        assertNotNull(result);
    }
    

    private LoanApplicationSessionBeanRemote lookupLoanApplicationSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (LoanApplicationSessionBeanRemote) c.lookup("java:global/MerlionBank/MerlionBank-ejb/LoanApplicationSessionBean!LoanEntity.Session.LoanApplicationSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
