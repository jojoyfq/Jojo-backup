/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity;

import CommonEntity.Customer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author ruijia
 */
@Entity
public class SavingAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long accountNumber;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private String status; //activated, inactive, terminated, below balance
    //@OneToOne(mappedBy="savingAccount")
    //private Customer customer;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private SavingAccountType savingAccountType = new SavingAccountType();
    

    public SavingAccount(Long accountNumber, BigDecimal balance, BigDecimal availableBalance, String status, Customer customer, SavingAccountType savingAccountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.availableBalance = availableBalance;
        this.status = status;
        this.customer = customer;
        this.savingAccountType = savingAccountType;
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SavingAccount)) {
            return false;
        }
        SavingAccount other = (SavingAccount) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
//test

    @Override
    public String toString() {
        return "DepositEntity.SavingAccount[ id=" + getId() + " ]";
    }

    /**
     * @return the accountNumber
     */
    public Long getAccountNumber() {
        return accountNumber;
    }
//test

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Boolean deductAmt(BigDecimal amount) {

        if (this.availableBalance.doubleValue() >= amount.doubleValue()) {
            this.availableBalance.subtract(amount);
            return true;
        } else {
            return false;
        }
    }

    public SavingAccount() {
    }

    public SavingAccountType getSavingAccountType() {
        return savingAccountType;
    }

    public void setSavingAccountType(SavingAccountType savingAccountType) {
        this.savingAccountType = savingAccountType;
    }

}
