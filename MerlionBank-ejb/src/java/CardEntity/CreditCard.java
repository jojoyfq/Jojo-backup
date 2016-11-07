/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity;

import CommonEntity.Customer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Bella
 */
@Entity
public class CreditCard implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long cardNumber;
    private String cardHolder;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expiryDate;
    private Long cvv;
    private String password;
    private String salt;
    private String status;
    private BigDecimal balance;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date payDate;
    
    @ManyToOne
    private CreditCardType creditCardType = new CreditCardType();
    @ManyToOne
    private Customer customer = new Customer();
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "creditCard")
    List<CreditChargeback> chargeback = new ArrayList();
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "creditCard")
    List<CreditCardTransaction> creditCardTransactions = new ArrayList();

    public CreditCard(){}
    
    public CreditCard(Long cardNumber,String cardHolder,Date startDate,Date expiryDate,Long cvv, CreditCardType creditCardType,Customer customer,BigDecimal balance,Date payDate){
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.creditCardType = creditCardType;
        this.customer = customer;
        this.balance = balance;
        this.payDate = payDate;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }
    

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getCvv() {
        return cvv;
    }

    public void setCvv(Long cvv) {
        this.cvv = cvv;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CreditChargeback> getChargeback() {
        return chargeback;
    }

    public void setChargeback(List<CreditChargeback> chargeback) {
        this.chargeback = chargeback;
    }

    public List<CreditCardTransaction> getCreditCardTransactions() {
        return creditCardTransactions;
    }

    public void setCreditCardTransactions(List<CreditCardTransaction> creditCardTransactions) {
        this.creditCardTransactions = creditCardTransactions;
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
        if (!(object instanceof CreditCard)) {
            return false;
        }
        CreditCard other = (CreditCard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CardEntity.CreditCard[ id=" + id + " ]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
          
    
}
