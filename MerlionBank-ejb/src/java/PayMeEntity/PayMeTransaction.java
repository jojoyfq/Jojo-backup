/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PayMeEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Bella
 */
@Entity
public class PayMeTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private BigDecimal debit;
    private BigDecimal credit;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date transactionTime;
    private String phoneNumber;
    private Long savingAccount;
    @ManyToOne
    private PayMe payme;

    public PayMeTransaction(){}
    
    public PayMeTransaction(String description, BigDecimal debit, BigDecimal credit,Date transactionTime, String phoneNumber,Long savingAccount, PayMe payme){
         this.description = description;
         this.debit = debit;
         this.credit = credit;
         this.transactionTime = transactionTime;
         this.phoneNumber = phoneNumber;
         this.savingAccount = savingAccount;
         this.payme = payme;
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
        if (!(object instanceof PayMeTransaction)) {
            return false;
        }
        PayMeTransaction other = (PayMeTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PayMeEntity.PayMeTransaction[ id=" + id + " ]";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    
    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(Long savingAccount) {
        this.savingAccount = savingAccount;
    }

    public PayMe getPayme() {
        return payme;
    }

    public void setPayme(PayMe payme) {
        this.payme = payme;
    }
    
    
    
}
