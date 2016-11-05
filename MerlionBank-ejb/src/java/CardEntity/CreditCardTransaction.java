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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Bella
 */
@Entity
public class CreditCardTransaction extends TransactionRecord {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private CreditCard creditCard;
    private String emv; // VISA, MASTERCARD, NETS
    
    public CreditCardTransaction(){}
    
    public CreditCardTransaction(String code, BigDecimal debit, BigDecimal credit, String status, String description,Date transactionTime,Long giverAccountNum,Long recipientAccountNum,SavingAccount savingAccount, String giverBank, String recipientBank, CreditCard creditCard, String emv){
        super(code,debit,credit,status,description,transactionTime,giverAccountNum,recipientAccountNum,savingAccount, giverBank, recipientBank);
        this.creditCard = creditCard;
        this.emv = emv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditCardTransaction)) {
            return false;
        }
        CreditCardTransaction other = (CreditCardTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CardEntity.CreditCardTransaction[ id=" + id + " ]";
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getEmv() {
        return emv;
    }

    public void setEmv(String emv) {
        this.emv = emv;
    }
        
}
