/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.CardTransaction;
import CardEntity.DebitCard;
import CardEntity.DebitChargeback;
import Exception.ChargebackException;
import Exception.DebitCardException;
import Exception.NoTransactionRecordFoundException;
import Exception.UserHasDebitCardException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Bella
 */
@Local
public interface DebitCardSessionBeanLocal {
    public DebitCard createDebitCard(Long savingAccountNum, Long customerID, String cardType)throws UserHasDebitCardException; 
    public List<String> getDebitCardType();
    public boolean verifyDebitCard(String cardHolder, Long cardNo, Date expiryDate, Long cvv) throws DebitCardException;
    public void setPassword(Long cardNo, String password);
    public List<String> getDebitCardString(Long customerID)throws DebitCardException;
    public List<DebitCard> getDebitCard(Long customerID)throws DebitCardException;
    public List<CardTransaction> getEStatement(Long customerID, Long debitCardNo, Date currentTime) throws NoTransactionRecordFoundException;
    public boolean checkDebitCardBalance(String cardNo, String cvv, String cardHolder, String amount);
    public void createChargeback(String merchantName, Date transactionDate, BigDecimal transactionAmount, String chargebackDescription, String debitCardNo) throws ChargebackException;
    public List<DebitChargeback> getPendingDebitChargeback();
    public void setChargebackStatus(DebitChargeback chargeback, String status);
}
