/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PayMeEntity;

import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Bella
 */
@Entity
public class PayMe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String phoneNumber;
    @OneToOne(mappedBy="payMe")
    private Customer customer;
    @OneToOne(mappedBy="payMe")
    private SavingAccount savingAccount;
    
    private BigDecimal balance;
    private String paymePassword;
    private String salt;
    
    public PayMe(){}
    
    public PayMe(String phoneNumber, Customer customer, SavingAccount savingAccount, BigDecimal balance, String paymePassword,String salt){
        this.phoneNumber = phoneNumber;
        this.customer = customer;
        this.savingAccount = savingAccount;
        this.balance = balance;
        this.paymePassword = paymePassword;
        this.salt = salt;
    }

    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.HALF_UP);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPaymePassword() {
        return paymePassword;
    }

    public void setPaymePassword(String paymePassword) {
        this.paymePassword = paymePassword;
    }

    public SavingAccount getSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(SavingAccount savingAccount) {
        this.savingAccount = savingAccount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PayMe)) {
            return false;
        }
        PayMe other = (PayMe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PayMeEntity.PayMe[ id=" + id + " ]";
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
        
}
