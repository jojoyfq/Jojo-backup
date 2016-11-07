/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity;

import BillEntity.OtherBank;
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
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author ruijia
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="TransactionRecord")
public class TransactionRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String code;
    private BigDecimal debit;
    private BigDecimal credit;
    private String status;
    private String description;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date transactionTime;
    private Long giverAccountNum;
    private Long recipientAccountNum;
    private String giverBankName;
    private String recipientBankName;
    @ManyToOne 
    private OtherBank otherBank;
    @ManyToOne
    private SavingAccount savingAccount;

    
    public TransactionRecord(){}
    
    public TransactionRecord(String code,BigDecimal debit,BigDecimal credit, String status, String description,Date transactionTime,Long giverAccountNum,Long recipientAccountNum, SavingAccount savingAccount, String giverBank, String recipientBank){
        this.code = code;
        this.debit = debit;
        this.credit = credit;
        this.status = status;
        this.description = description;
        this.transactionTime = transactionTime;
        this.giverAccountNum = giverAccountNum;
        this.recipientAccountNum = recipientAccountNum;
        this.giverBankName = giverBank;
        this.recipientBankName = recipientBank;
        this.savingAccount = savingAccount;
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
    
     public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getGiverAccountNum() {
        return giverAccountNum;
    }

    public void setGiverAccountNum(Long giverAccountNum) {
        this.giverAccountNum = giverAccountNum;
    }

    public Long getRecipientAccountNum() {
        return recipientAccountNum;
    }

    public void setRecipientAccountNum(Long recipientAccountNum) {
        this.recipientAccountNum = recipientAccountNum;
    }

    public OtherBank getOtherBank() {
        return otherBank;
    }

    public void setOtherBank(OtherBank otherBank) {
        this.otherBank = otherBank;
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

    public SavingAccount getSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(SavingAccount savingAccount) {
        this.savingAccount = savingAccount;
    }

    public String getGiverBankName() {
        return giverBankName;
    }

    public void setGiverBankName(String giverBankName) {
        this.giverBankName = giverBankName;
    }

    public String getRecipientBankName() {
        return recipientBankName;
    }

    public void setRecipientBankName(String recipientBankName) {
        this.recipientBankName = recipientBankName;
    }
    
    
    
}
