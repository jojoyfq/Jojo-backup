/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author Bella
 */
@Entity
public class SavingAccountType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accountType;
    private BigDecimal minAmount;
    private double interestRate1;
    private double interestRate2;
    private double interestRate3;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "savingAccountType")
    private List<SavingAccount> savingAccounts = new ArrayList<SavingAccount>();

    public List<SavingAccount> getSavingAccounts() {
        return savingAccounts;
    }

    public void setSavingAccounts(List<SavingAccount> savingAccounts) {
        this.savingAccounts = savingAccounts;
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
        if (!(object instanceof SavingAccountType)) {
            return false;
        }
        SavingAccountType other = (SavingAccountType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "DepositEntity.SavingAccountType[ id=" + id + " ]";
    }
    
     public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }
    
    public double getInterestRate1() {
        return interestRate1;
    }

    public void setInterestRate1(double interestRate1) {
        this.interestRate1 = interestRate1;
    }

    public double getInterestRate2() {
        return interestRate2;
    }

    public void setInterestRate2(double interestRate2) {
        this.interestRate2 = interestRate2;
    }

    public double getInterestRate3() {
        return interestRate3;
    }

    public void setInterestRate3(double interestRate3) {
        this.interestRate3 = interestRate3;
    }

}
