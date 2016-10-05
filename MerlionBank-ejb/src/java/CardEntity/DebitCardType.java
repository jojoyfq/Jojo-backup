/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity;

import DepositEntity.SavingAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Bella
 */
@Entity
public class DebitCardType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String debitCardType;
    private double rebateRate;

    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "debitCardType")
    private List<DebitCard> debitCard = new ArrayList<DebitCard>();

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
        if (!(object instanceof DebitCardType)) {
            return false;
        }
        DebitCardType other = (DebitCardType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CardEntity.DebitCardType[ id=" + id + " ]";
    }
    
    public String getDebitCardType() {
        return debitCardType;
    }

    public void setDebitCardType(String debitCardType) {
        this.debitCardType = debitCardType;
    }

    public double getRebateRate() {
        return rebateRate;
    }

    public void setRebateRate(double rebateRate) {
        this.rebateRate = rebateRate;
    }

    public List<DebitCard> getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(List<DebitCard> debitCard) {
        this.debitCard = debitCard;
    }
    
}
