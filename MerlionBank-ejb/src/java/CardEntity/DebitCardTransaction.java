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
import javax.persistence.ManyToOne;

/**
 *
 * @author Bella
 */
@Entity
public class DebitCardTransaction extends TransactionRecord {
    @ManyToOne
    private DebitCard debitCard;
    private String emv; // VISA, MASTERCARD, NETS

    
    public DebitCardTransaction(){}
    
    public DebitCardTransaction(String code, BigDecimal debit, BigDecimal credit, String status, String description,Date transactionTime,Long giverAccountNum,Long recipientAccountNum,SavingAccount savingAccount, String giverBank, String recipientBank, DebitCard debitCard, String emv){
        super(code,debit,credit,status,description,transactionTime,giverAccountNum,recipientAccountNum,savingAccount, giverBank, recipientBank);
        this.debitCard = debitCard;
        this.emv = emv;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    
    public String getEmv() {
        return emv;
    }

    public void setEmv(String emv) {
        this.emv = emv;
    }
    
}
