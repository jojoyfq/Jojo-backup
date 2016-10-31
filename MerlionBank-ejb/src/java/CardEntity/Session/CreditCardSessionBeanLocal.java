/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.CreditCard;
import CardEntity.CreditCardApplication;
import CommonEntity.Customer;
import java.text.ParseException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Bella
 */
@Local
public interface CreditCardSessionBeanLocal {
    public List<String> getCreditCardType();
    public Customer getCustomer(Long customerID);
    public void setFileDestination(Long customerId,String fileDestination);
    public void newCreditCardApplication(Customer customer,String nationality,String cardType);
    public List<CreditCardApplication> getPendingCreditCardApplication();
    public CreditCard createCreditCard(Long customerID, String cardType) throws ParseException;
    public void approveCreditCardApplication(CreditCardApplication application ) throws ParseException;
    public void rejectCreditCardApplication(CreditCardApplication application);
}
