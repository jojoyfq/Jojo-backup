/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author apple
 */
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String IC;
    private String name;
    private String gender;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfBirth;
    private String addresss;
    private String email;
    private Long phoneNumber;
    private String occupation; //company info
    private String familyInfo;
    private BigDecimal financialAsset;
    private String financialGoal;
    private Double riskRating;
  
    @OneToOne(cascade={CascadeType.ALL})
    
    
   private OnlineAccount onlineAccount;//same as IC

    public Customer(String IC, String name, String gender, Date dateOfBirth, String addresss, String email, Long phoneNumber, String occupation, String familyInfo, BigDecimal financialAsset, String financialGoal, Double riskRating, OnlineAccount onlineAccount) {
        this.IC = IC;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.addresss = addresss;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.occupation = occupation;
        this.familyInfo = familyInfo;
        this.financialAsset = financialAsset;
        this.financialGoal = financialGoal;
        this.riskRating = riskRating;
        this.onlineAccount = onlineAccount;
    }
 //  public OnlineAccount getOnlineAccountNumber(){
 //  return onlineAccountNumber;
 //  }
    public String getIC() {
        return IC;
    }

    public void setIC(String IC) {
        this.IC = IC;
    }
 

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (IC != null ? IC.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.IC == null && other.IC != null) || (this.IC != null && !this.IC.equals(other.IC))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + IC + " ]";
    }
    
}
