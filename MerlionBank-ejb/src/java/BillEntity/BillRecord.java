/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity;

import DepositEntity.TransactionRecord;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
public class BillRecord extends TransactionRecord {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private BillingOrganization billingOrganization;
    private String billReference;

    public BillRecord() {
    }

    public BillRecord(Long id, BillingOrganization billingOrganization, String billReference, String code, BigDecimal amount, String status, String description, Date transactionTime, Long giverAccountNum, Long recipientAccountNum) {
        super(code, amount, status, description, transactionTime, giverAccountNum, recipientAccountNum);
        this.id = id;
        this.billingOrganization = billingOrganization;
        this.billReference = billReference;
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
        if (!(object instanceof BillRecord)) {
            return false;
        }
        BillRecord other = (BillRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BillEntity.BillRecord[ id=" + id + " ]";
    }
    
}
