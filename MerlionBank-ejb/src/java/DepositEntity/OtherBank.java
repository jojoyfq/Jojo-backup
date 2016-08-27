/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author ruijia
 */
@Entity
public class OtherBank implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String bankName;
    @OneToOne(cascade ={CascadeType.ALL})
    private InterbankFASTRecord interbankFASTRecord;
    
    

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
        if (!(object instanceof OtherBank)) {
            return false;
        }
        OtherBank other = (OtherBank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DepositEntity.OtherBank[ id=" + id + " ]";
    }

    /**
     * @return the bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName the bankName to set
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return the interbankFASTRecord
     */
    public InterbankFASTRecord getInterbankFASTRecord() {
        return interbankFASTRecord;
    }

    /**
     * @param interbankFASTRecord the interbankFASTRecord to set
     */
    public void setInterbankFASTRecord(InterbankFASTRecord interbankFASTRecord) {
        this.interbankFASTRecord = interbankFASTRecord;
    }
    
}
