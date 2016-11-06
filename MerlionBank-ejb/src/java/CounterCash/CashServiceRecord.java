/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CounterCash;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author shuyunhuang
 */
@Entity
public class CashServiceRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String serviceType;
    private String description;
    private Boolean isCredit; // if isCredit is true, customer credit amount, else give customer cash
    private Timestamp time;
    private BigDecimal cashTransaction;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsCredit() {
        return isCredit;
    }

    public void setIsCredit(Boolean isCredit) {
        this.isCredit = isCredit;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public BigDecimal getCashTransaction() {
        return cashTransaction;
    }

    public void setCashTransaction(BigDecimal cashTransaction) {
        this.cashTransaction = cashTransaction;
    }
    
    public CashServiceRecord(){
       
    }
    
    public CashServiceRecord(String serviceType, String description, Boolean isCredit, Timestamp time, BigDecimal cashTransaction){
        this.serviceType = serviceType;
        this.description = description;
        this.isCredit = isCredit;
        this.time = time;
        this.cashTransaction = cashTransaction;
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
        if (!(object instanceof CashServiceRecord)) {
            return false;
        }
        CashServiceRecord other = (CashServiceRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CounterCash.CashServiceRecord[ id=" + id + " ]";
    }
    
}
