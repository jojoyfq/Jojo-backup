/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity;

import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;

/**
 *
 * @author Bella
 */
@Entity
public class DebitCardTransaction extends TransactionRecord {
    private Long cardNumber;
    private String emv; // VISA, MASTERCARD, NETS

    
    public DebitCardTransaction(){}
    
    public DebitCardTransaction(String code, BigDecimal debit, BigDecimal credit, String status, String description,Date transactionTime,Long giverAccountNum,Long recipientAccountNum,SavingAccount savingAccount, String giverBank, String recipientBank, Long cardNumber, String emv){
        super(code,debit,credit,status,description,transactionTime,giverAccountNum,recipientAccountNum,savingAccount, giverBank, recipientBank);
        this.cardNumber = cardNumber;
        this.emv = emv;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getEmv() {
        return emv;
    }

    public void setEmv(String emv) {
        this.emv = emv;
    }
    
}
