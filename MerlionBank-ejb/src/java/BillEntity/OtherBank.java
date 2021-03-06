/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity;

import DepositEntity.TransactionRecord;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author ruijia
 */
@Entity
public class OtherBank implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String swiftCode;
    private String UEN;
    private String address;
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="otherBank")
    private List<TransactionRecord> transactionRecord;
    private String status;//active, terminated
    @OneToMany(cascade={CascadeType.ALL},mappedBy="bank")
    private List<BillingOrganization> billingOrganization;

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getUEN() {
        return UEN;
    }

    public void setUEN(String UEN) {
        this.UEN = UEN;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
   
    public List<BillingOrganization> getBillingOrganization() {
        return billingOrganization;
    }

    public void setBillingOrganization(List<BillingOrganization> billingOrganization) {
        this.billingOrganization = billingOrganization;
    }
    
    

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OtherBank(String name, String swiftCode, String UEN, String address) {
        this.name = name;
        this.swiftCode = swiftCode;
        this.UEN = UEN;
        this.address = address;
        this.status = "active";
        this.transactionRecord = new ArrayList<>();
        this.billingOrganization = new ArrayList<>();
        
    }
    
   


    public OtherBank() {
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
        if (!(object instanceof OtherBank)) {
            return false;
        }
        OtherBank other = (OtherBank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BillEntity.OtherBank[ id=" + id + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TransactionRecord> getTransactionRecord() {
        return transactionRecord;
    }

    public void setTransactionRecord(List<TransactionRecord> transactionRecord) {
        this.transactionRecord = transactionRecord;
    }
    
}
