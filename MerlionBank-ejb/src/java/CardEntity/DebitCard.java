/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity;

import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import DepositEntity.SavingAccountType;
import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Bella
 */
@Entity
public class DebitCard implements Serializable {
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
    @OneToOne(mappedBy="debitCard")
    private SavingAccount savingAccount;//debit card is link to one saving account
    @ManyToOne
    private DebitCardType debitCardType = new DebitCardType();
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "debitCard")
    List<DebitChargeback> chargeback = new ArrayList();
    
    public DebitCard(){
    }
    

    public DebitCard(Long cardNumber, String cardHolder, Date startDate, Date expiryDate, Long cvv, String status, SavingAccount savingAccount,DebitCardType debitCardType, String password,String salt){
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.status = status;
        this.savingAccount = savingAccount;
        this.debitCardType = debitCardType;
        this.password = password;
        this.salt = salt;
    }
    
    public DebitCardType getDebitCardType() {
        return debitCardType;
    }

    public void setDebitCardType(DebitCardType debitCardType) {
        this.debitCardType = debitCardType;
    }
    
    public SavingAccount getSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(SavingAccount savingAccount) {
        this.savingAccount = savingAccount;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Long getCvv() {
        return cvv;
    }

    public void setCvv(Long cvv) {
        this.cvv = cvv;
    }
    
    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DebitChargeback> getChargeback() {
        return chargeback;
    }

    public void setChargeback(List<DebitChargeback> chargeback) {
        this.chargeback = chargeback;
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
        if (!(object instanceof DebitCard)) {
            return false;
        }
        DebitCard other = (DebitCard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CardEntity.DebitCard[ id=" + id + " ]";
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
    
}
