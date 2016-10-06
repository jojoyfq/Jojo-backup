/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity;

import DepositEntity.TransactionRecord;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;

/**
 *
 * @author Bella
 */
@Entity
public class CardTransaction extends TransactionRecord {
    private Long cardNumber;
    
    public CardTransaction(){}
    
    public CardTransaction(String code, BigDecimal amount,String status, String description,Date transactionTime,Long giverAccountNum,Long recipientAccountNum,Long cardNumber){
        super(code,amount,status,description,transactionTime,giverAccountNum,recipientAccountNum);
        this.cardNumber = cardNumber;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    
}
