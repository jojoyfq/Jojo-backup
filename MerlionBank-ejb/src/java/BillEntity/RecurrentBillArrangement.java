/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity;

import DepositEntity.SavingAccount;
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
 * @author ruijia
 */
@Entity
public class RecurrentBillArrangement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;
    @ManyToOne
    private BillingOrganization billingOrganization;


    private String billReference;
    @ManyToOne
    private SavingAccount savingAccount;
    private Integer billTimes;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    private Integer billInterval;
    private Integer timesRemaining;

    public RecurrentBillArrangement() {
    }

    public RecurrentBillArrangement(BigDecimal amount, BillingOrganization billingOrganization, String billReference, SavingAccount savingAccount, Integer billTimes, Date startDate, Integer billInterval, Integer timesRemaining) {
        this.amount = amount;
        this.billingOrganization = billingOrganization;
        this.billReference = billReference;
        this.savingAccount = savingAccount;
        this.billTimes = billTimes;
        this.startDate = startDate;
        this.billInterval = billInterval;
        this.timesRemaining = timesRemaining;
    }

    
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    
    public BillingOrganization getBillingOrganization() {
        return billingOrganization;
    }

    public void setBillingOrganization(BillingOrganization billingOrganization) {
        this.billingOrganization = billingOrganization;
    }
    
    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public SavingAccount getSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(SavingAccount savingAccount) {
        this.savingAccount = savingAccount;
    }

    public Integer getBillTimes() {
        return billTimes;
    }

    public void setBillTimes(Integer billTimes) {
        this.billTimes = billTimes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getBillInterval() {
        return billInterval;
    }

    public void setBillInterval(Integer billInterval) {
        this.billInterval = billInterval;
    }

    public Integer getTimesRemaining() {
        return timesRemaining;
    }

    public void setTimesRemaining(Integer timesRemaining) {
        this.timesRemaining = timesRemaining;
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
        if (!(object instanceof RecurrentBillArrangement)) {
            return false;
        }
        RecurrentBillArrangement other = (RecurrentBillArrangement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BillEntity.RecurrentBillArrangement[ id=" + id + " ]";
    }
    
}
