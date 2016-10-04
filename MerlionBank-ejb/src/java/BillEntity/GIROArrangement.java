/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity;

import DepositEntity.SavingAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author ruijia
 */
@Entity
public class GIROArrangement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal deductionLimit;
    @ManyToOne
    private BillingOrganization billingOrganization;
    private String billReference;
    @ManyToOne
    private SavingAccount savingAccount;
    
    private String status;//active, terminated
    
    public GIROArrangement(){}

    public GIROArrangement(BigDecimal deductionLimit, BillingOrganization billingOrganization, String billReference, SavingAccount savingAccount) {
        this.deductionLimit = deductionLimit;
        this.billingOrganization = billingOrganization;
        this.billReference = billReference;
        this.savingAccount = savingAccount;
        this.status = "active";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    

    public BigDecimal getDeductionLimit() {
        return deductionLimit;
    }

    public void setDeductionLimit(BigDecimal deductionLimit) {
        this.deductionLimit = deductionLimit;
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
        if (!(object instanceof GIROArrangement)) {
            return false;
        }
        GIROArrangement other = (GIROArrangement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BillEntity.GIROArrangement[ id=" + id + " ]";
    }
    
}
