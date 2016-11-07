/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
public class DebitChargeback implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //variable related to each chargeback request
    private String merchantName;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date transactionDate;
    private BigDecimal transactionAmount;
    private String chargebackDescription;
    //chargeback variables
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date chargebackDate;
    private String status;
    //link to card
    @ManyToOne
    DebitCard debitCard;
    
    public DebitChargeback(String merchantName,Date transactionDate,BigDecimal transactionAmount,String chargebackDescription,Date chargebackDate,String status,DebitCard debitCard){
        this.merchantName = merchantName;
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.chargebackDescription = chargebackDescription;
        this.chargebackDate = chargebackDate;
        this.status = status;
        this.debitCard = debitCard;
    }
    
    public DebitChargeback(){
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getChargebackDescription() {
        return chargebackDescription;
    }

    public void setChargebackDescription(String chargebackDescription) {
        this.chargebackDescription = chargebackDescription;
    }

    public Date getChargebackDate() {
        return chargebackDate;
    }

    public void setChargebackDate(Date chargebackDate) {
        this.chargebackDate = chargebackDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
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
        if (!(object instanceof DebitChargeback)) {
            return false;
        }
        DebitChargeback other = (DebitChargeback) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CardEntity.Chargeback[ id=" + id + " ]";
    }
    
}
