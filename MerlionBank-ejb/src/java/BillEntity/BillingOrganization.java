/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author ruijia
 */
@Entity
public class BillingOrganization implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    private OtherBank bank;
    
    private Long accountNumber;
    
    @OneToMany (cascade={CascadeType.ALL},mappedBy="billingOrganization")
    private List<GIROArrangement> GIROArrangement;

    @OneToMany (cascade={CascadeType.ALL},mappedBy="billingOrganization")
    private List<RecurrentBillArrangement> recurrentBillArrangement;

    public List<RecurrentBillArrangement> getRecurrentBillArrangement() {
        return recurrentBillArrangement;
    }

    public void setRecurrentBillArrangement(List<RecurrentBillArrangement> recurrentBillArrangement) {
        this.recurrentBillArrangement = recurrentBillArrangement;
    }
    
    public BillingOrganization(){}
    
    public BillingOrganization(String name, OtherBank bank, Long accountNumber, List<GIROArrangement> GIROArrangement, List<RecurrentBillArrangement> recurrentBillArrangement) {
        this.name = name;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.GIROArrangement = GIROArrangement;
        this.recurrentBillArrangement = recurrentBillArrangement;
    }
    
    

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OtherBank getBank() {
        return bank;
    }

    public void setBank(OtherBank bank) {
        this.bank = bank;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<GIROArrangement> getGIROArrangement() {
        return GIROArrangement;
    }

    public void setGIROArrangement(List<GIROArrangement> GIROArrangement) {
        this.GIROArrangement = GIROArrangement;
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
        if (!(object instanceof BillingOrganization)) {
            return false;
        }
        BillingOrganization other = (BillingOrganization) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BillEntity.BillingOrganization[ id=" + id + " ]";
    }
    
}
