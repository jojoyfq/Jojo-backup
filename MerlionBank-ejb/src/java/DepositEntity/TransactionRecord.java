/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author ruijia
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class TransactionRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal amount;
    private Boolean isDebit;
    private String description;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date transactionTime;
    @ManyToOne
    private SavingAccount savingAccount;
    
    
    
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
        if (!(object instanceof TransactionRecord)) {
            return false;
        }
        TransactionRecord other = (TransactionRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DepositEntity.TransactionRecord[ id=" + id + " ]";
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the isDebit
     */
    public Boolean getIsDebit() {
        return isDebit;
    }

    /**
     * @param isDebit the isDebit to set
     */
    public void setIsDebit(Boolean isDebit) {
        this.isDebit = isDebit;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the transactionTime
     */
    public Date getTransactionTime() {
        return transactionTime;
    }

    /**
     * @param transactionTime the transactionTime to set
     */
    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    /**
     * @return the savingAccount
     */
    public SavingAccount getSavingAccount() {
        return savingAccount;
    }

    /**
     * @param savingAccount the savingAccount to set
     */
    public void setSavingAccount(SavingAccount savingAccount) {
        this.savingAccount = savingAccount;
    }
    
    
    
}
