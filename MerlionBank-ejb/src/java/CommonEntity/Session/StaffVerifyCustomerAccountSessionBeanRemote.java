/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import Exception.EmailNotSendException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author a0113893
 */
@Remote
public interface StaffVerifyCustomerAccountSessionBeanRemote {
   public List<Customer> viewPendingVerificationList()/*throws ListEmptyException*/;  
public boolean verifySavingAccountCustomer (Long staffID, Long customerID, String result,Long savingAccountId) throws EmailNotSendException;

//staff verify customer and choose"reject" or "approve"
public boolean verifyFixedDepositAccountCustomer (Long staffID, Long customerID, String result,Long accountId) throws EmailNotSendException;
public boolean verifyLoanAccountCustomer (Long staffID, Long customerID, String result,Long accountId) throws EmailNotSendException;
public boolean verifyDiscretionaryAccountCustomer (Long staffID, Long customerID, String result,Long discretionaryAccountId) throws EmailNotSendException;
 
}
