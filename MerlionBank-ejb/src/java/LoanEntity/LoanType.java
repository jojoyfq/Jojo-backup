/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity;

import DepositEntity.SavingAccount;
import java.io.Serializable;
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
 * @author a0113893
 */
@Entity
public class LoanType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type;
    private String name;
    //private Double FHR18;
    //private Double FHR18rate;
    private Double fixedRate;
    private Double fixedRate2;
    private Double SIBOR;
    private Double SIBORrate1;
    private Double interestRate;
    private Double educationRate;
    private String description;
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "loanType")
    private List<Loan> loans;
    

    public LoanType() {
    }

    public Double getFixedRate2() {
        return fixedRate2;
    }

    public void setFixedRate2(Double fixedRate2) {
        this.fixedRate2 = fixedRate2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LoanType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public Double getEducationRate() {
        return educationRate;
    }

    public void setEducationRate(Double educationRate) {
        this.educationRate = educationRate;
    }

    
    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Double getFHR18() {
//        return FHR18;
//    }
//
//    public void setFHR18(Double FHR18) {
//        this.FHR18 = FHR18;
//    }
//
//    public Double getFHR18rate() {
//        return FHR18rate;
//    }
//
//    public void setFHR18rate(Double FHR18rate) {
//        this.FHR18rate = FHR18rate;
//    }

    public Double getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(Double fixedRate) {
        this.fixedRate = fixedRate;
    }


    public Double getSIBOR() {
        return SIBOR;
    }

    public void setSIBOR(Double SIBOR) {
        this.SIBOR = SIBOR;
    }

    public Double getSIBORrate1() {
        return SIBORrate1;
    }

    public void setSIBORrate1(Double SIBORrate1) {
        this.SIBORrate1 = SIBORrate1;
    }


    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
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
        if (!(object instanceof LoanType)) {
            return false;
        }
        LoanType other = (LoanType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LoanEntity.LoanType[ id=" + id + " ]";
    }
    
}
