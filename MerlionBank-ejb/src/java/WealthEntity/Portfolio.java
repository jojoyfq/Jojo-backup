/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author a0113893
 */
@Entity
public class Portfolio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal investAmount;
    private BigDecimal presentValue;
    private Double monthlyInterestRate;
    private Double expectedRateOfReturn;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    
    
    @ManyToOne
    private DiscretionaryAccount discretionaryAccount;
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="portfolio")
    private List<PortfolioTransaction> portfolioTransactions;
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="portfolio")
    private List<Product> products;

    public Double getExpectedRateOfReturn() {
        return expectedRateOfReturn;
    }

    public void setExpectedRateOfReturn(Double expectedRateOfReturn) {
        this.expectedRateOfReturn = expectedRateOfReturn;
    }
    
    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public BigDecimal getPresentValue() {
        return presentValue;
    }

    public void setPresentValue(BigDecimal presentValue) {
        this.presentValue = presentValue;
    }

    public Double getMonthlyInterestRate() {
        return monthlyInterestRate;
    }

    public void setMonthlyInterestRate(Double monthlyInterestRate) {
        this.monthlyInterestRate = monthlyInterestRate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public DiscretionaryAccount getDiscretionaryAccount() {
        return discretionaryAccount;
    }

    public void setDiscretionaryAccount(DiscretionaryAccount discretionaryAccount) {
        this.discretionaryAccount = discretionaryAccount;
    }

    public List<PortfolioTransaction> getPortfolioTransactions() {
        return portfolioTransactions;
    }

    public void setPortfolioTransactions(List<PortfolioTransaction> portfolioTransactions) {
        this.portfolioTransactions = portfolioTransactions;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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
        if (!(object instanceof Portfolio)) {
            return false;
        }
        Portfolio other = (Portfolio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WealthEntity.Portfolio[ id=" + id + " ]";
    }
    
}
