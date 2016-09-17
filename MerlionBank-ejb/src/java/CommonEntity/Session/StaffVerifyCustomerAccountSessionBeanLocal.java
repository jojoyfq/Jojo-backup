/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import Exception.EmailNotSendException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface StaffVerifyCustomerAccountSessionBeanLocal {
 //system retrieve list of pending verification customers
public List<Customer> viewPendingVerificationList();  
public boolean verifySavingAccountCustomer (Long staffID, Long customerID, String result,Long savingAccountId) throws EmailNotSendException;

//staff verify customer and choose"reject" or "approve"
public boolean verifyFixedDepositAccountCustomer (Long staffID, Long customerID, String result,Long accountId) throws EmailNotSendException;
}
