/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CounterServiceEntity;
/*
import CommonEntity.Customer;
import CommonEntity.StaffRole;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author bella
 */
/*@Entity
public class CounterServiceRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String serviceType;
    private BigDecimal serviceCharge;
    private String description;
    private Boolean isCredit; //received cash or hand out cash
    private Timestamp time;
    private BigDecimal cashTransction;
    
    @ManyToOne
    private Staff staff;
    @ManyToOne
    private Customer customer;

   
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
        if (!(object instanceof CounterServiceRecord)) {
            return false;
        }
        CounterServiceRecord other = (CounterServiceRecord) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CounterServiceEntity.CounterServiceRecord[ id=" + getId() + " ]";
    }

    /**
     * @return the serviceType
     */
    /*public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType the serviceType to set
     */
    /*public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return the serviceCharge
     */
    /*public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    /**
     * @param serviceCharge the serviceCharge to set
     */
    /*public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    /**
     * @return the description
     */
    /*public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    /*public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the customer
     */
    /*public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    /*public void setCustomer(Customer customer) {
        this.customer = customer;
    //}
    
}*/
