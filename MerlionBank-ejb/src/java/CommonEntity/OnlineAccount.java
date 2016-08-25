/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author apple
 */
@Entity
public class OnlineAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String onlineAccountNumber;

    public String getOnlineAccountNumber() {
        return onlineAccountNumber;
    }

    public void setOnlineAccountNumber(String onlineAccountNumber) {
        this.onlineAccountNumber = onlineAccountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    

@OneToOne(mappedBy="onlineAccount")
private Customer customer;
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (onlineAccountNumber != null ? onlineAccountNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OnlineAccount)) {
            return false;
        }
        OnlineAccount other = (OnlineAccount) object;
        if ((this.onlineAccountNumber == null && other.onlineAccountNumber != null) || (this.onlineAccountNumber != null && !this.onlineAccountNumber.equals(other.onlineAccountNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OnlineAccount[ id=" + onlineAccountNumber + " ]";
    }
    
}
