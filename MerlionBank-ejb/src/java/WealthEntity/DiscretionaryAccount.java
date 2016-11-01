/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity;

import CommonEntity.Customer;
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
public class DiscretionaryAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long accountNumber;
     @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    private BigDecimal balance;
    private BigDecimal totalBalance;
    private BigDecimal accumDailyInterest;
    private String status;
    private BigDecimal commission;
    
    @ManyToOne
    private Customer customer;
    
     @OneToMany(cascade={CascadeType.ALL},mappedBy="discretionaryAccount")
    private List<Portfolio> portfolios;

    public DiscretionaryAccount() {
    }

    public DiscretionaryAccount(Long accountNumber, Date startDate, Date endDate, BigDecimal balance, BigDecimal accumDailyInterest, String status, Customer customer,BigDecimal totalBalance) {
        this.accountNumber = accountNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.balance = balance;
        this.accumDailyInterest = accumDailyInterest;
        this.status = status;
        this.customer = customer;
        this.totalBalance=totalBalance;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }
     
     

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAccumDailyInterest() {
        return accumDailyInterest;
    }

    public void setAccumDailyInterest(BigDecimal accumDailyInterest) {
        this.accumDailyInterest = accumDailyInterest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        if (!(object instanceof DiscretionaryAccount)) {
            return false;
        }
        DiscretionaryAccount other = (DiscretionaryAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WealthEntity.DiscretionaryAccount[ id=" + id + " ]";
    }
    
}
