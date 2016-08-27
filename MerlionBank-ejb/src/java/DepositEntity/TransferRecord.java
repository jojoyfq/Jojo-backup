/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author ruijia
 */
@Inheritance(strategy=InheritanceType.JOINED)
@Entity
public class TransferRecord extends TransactionRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String thOtherAccountNumber;
    private String transferMethod;//intrabank, interbank, cheque
    
    

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
        if (!(object instanceof TransferRecord)) {
            return false;
        }
        TransferRecord other = (TransferRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DepositEntity.TransferRecord[ id=" + id + " ]";
    }

    /**
     * @return the thOtherAccountNumber
     */
    public String getThOtherAccountNumber() {
        return thOtherAccountNumber;
    }

    /**
     * @param thOtherAccountNumber the thOtherAccountNumber to set
     */
    public void setThOtherAccountNumber(String thOtherAccountNumber) {
        this.thOtherAccountNumber = thOtherAccountNumber;
    }

    /**
     * @return the transferMethod
     */
    public String getTransferMethod() {
        return transferMethod;
    }

    /**
     * @param transferMethod the transferMethod to set
     */
    public void setTransferMethod(String transferMethod) {
        this.transferMethod = transferMethod;
    }
    
}
