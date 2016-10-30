/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Bella
 */
@Entity
public class CreditCardApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long customerID;
    private String customerName;
    private String customerIC;
    private String customerNationality;
    private String creditCardType;
    private List<String> fileDestination;
    private String status;
    
    public CreditCardApplication(){}
    
    public CreditCardApplication(Long customerID, String customerName, String customerIC, String customerNationality,
            List<String> fileDestination, String creditCardType, String status){
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerIC = customerIC;
        this.customerNationality = customerNationality;
        this.fileDestination = fileDestination;
        this.creditCardType = creditCardType;
        this.status = status;
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
        if (!(object instanceof CreditCardApplication)) {
            return false;
        }
        CreditCardApplication other = (CreditCardApplication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CardEntity.CreditCardApplication[ id=" + id + " ]";
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerIC() {
        return customerIC;
    }

    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
    }

    public String getCustomerNationality() {
        return customerNationality;
    }

    public void setCustomerNationality(String customerNationality) {
        this.customerNationality = customerNationality;
    }

    public List<String> getFileDestination() {
        return fileDestination;
    }

    public void setFileDestination(List<String> fileDestination) {
        this.fileDestination = fileDestination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }
        
}
