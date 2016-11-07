/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.CreditCard;
import CardEntity.CreditCardApplication;
import CardEntity.CreditChargeback;
import CommonEntity.Customer;
import Exception.ChargebackException;
import Exception.CreditCardException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.mail.MessagingException;

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
    public boolean verifyCreditCard(String cardHolder, Long cardNo, Date expiryDate, Long cvv) throws CreditCardException;
    public void setPassword(Long cardNo, String password);
    public List<String> getCreditCardNumbers(Long customerID);
    public boolean cancelCreditCard(String cardNo) throws CreditCardException;
    public CreditCard getCreditCardForClose(String cardNo);
    public void createChargeback(String merchantName, Date transactionDate, BigDecimal transactionAmount, String chargebackDescription, String creditCardNo) throws ChargebackException ;
    public List<CreditChargeback> getPendingCreditChargeback();
    public void setChargebackStatus(CreditChargeback chargeback, String status);
    public BigDecimal getOutStandAmount(String creditCardString);
    public boolean payBySavingAccount(String savingAccount, String creditCardString, String amount) throws CreditCardException;
}
