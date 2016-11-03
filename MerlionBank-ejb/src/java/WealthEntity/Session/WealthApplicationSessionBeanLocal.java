/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import CommonEntity.Customer;
import Exception.EmailNotSendException;
import Exception.NotEnoughAmountException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import WealthEntity.DiscretionaryAccount;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface WealthApplicationSessionBeanLocal {

    public Customer createDiscretionaryAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo) throws UserExistException, EmailNotSendException;

    public void createDiscretionaryAccountExistingCustomer(Long customerID) throws EmailNotSendException;

    public Customer tellerCreateDiscretionaryAccount(Long staffId, String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String enterPassword) throws UserExistException, EmailNotSendException;

    public Long searchCustomer(String customerIc) throws UserNotExistException, UserNotActivatedException;

    public void staffCreateDiscretionaryAccountExistingCustomer(Long staffId, Long customerID) throws EmailNotSendException;

    //Customer activate account 2nd step, same as saving account, need to verify balance before update password
    public String verifyDiscretionaryAccountBalance(String ic);
    public String verifyExistingDiscretionaryAccountBalance(Long discretionaryAccountId) throws NotEnoughAmountException;
    public List<DiscretionaryAccount> updateAccountStatus(Long discretionaryAccountId);
        
    

}
