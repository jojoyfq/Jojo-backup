/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity;

import BillEntity.GIROArrangement;
import BillEntity.RecurrentBillArrangement;
import CardEntity.DebitCard;
import CommonEntity.Customer;
import CommonEntity.OnlineAccount;
import PayMeEntity.PayMe;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author ruijia
 */
@Entity
public class SavingAccount implements Serializable {

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
    private BigDecimal availableBalance;
    private BigDecimal accumDailyInterest;
    private String status; //activated, inactive, terminated, below balance
    //@OneToOne(mappedBy="savingAccount")
    //private Customer customer;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private SavingAccountType savingAccountType = new SavingAccountType();

    @OneToMany (cascade={CascadeType.ALL},mappedBy="savingAccount")
    private List<GIROArrangement> giroArrangement;
    
    @OneToMany (cascade={CascadeType.ALL},mappedBy="savingAccount")
    private List<RecurrentBillArrangement> recurrentBillArrangement;
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="savingAccount")
    private List<TransactionRecord> transactionRecord;
    
    @OneToOne (cascade={CascadeType.ALL})
    private PayMe payMe;
 
    public List<RecurrentBillArrangement> getRecurrentBillArrangement() {
        return recurrentBillArrangement;
    }

    public void setRecurrentBillArrangement(List<RecurrentBillArrangement> recurrentBillArrangement) {
        this.recurrentBillArrangement = recurrentBillArrangement;
    }
    
    
    public List<GIROArrangement> getGiroArrangement() {
        return giroArrangement;
    }

    public void setGiroArrangement(List<GIROArrangement> giroArrangement) {
        this.giroArrangement = giroArrangement;
    }
    
    

    @OneToOne(cascade={CascadeType.ALL}) 
    private DebitCard debitCard; //one saving account is linked with one debit card


    

    public SavingAccount(Long accountNumber, BigDecimal balance, BigDecimal availableBalance, String status, Customer customer, SavingAccountType savingAccountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.availableBalance = availableBalance;
        this.status = status;
        this.customer = customer;
        this.savingAccountType = savingAccountType;
        this.transactionRecord = new ArrayList<>();
        this.giroArrangement = new ArrayList<>();
        this.recurrentBillArrangement = new ArrayList<>();
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SavingAccount)) {
            return false;
        }
        SavingAccount other = (SavingAccount) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
//test

    @Override
    public String toString() {
        return "DepositEntity.SavingAccount[ id=" + getId() + " ]";
    }

    /**
     * @return the accountNumber
     */
    public Long getAccountNumber() {
        return accountNumber;
    }
//test

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance.setScale(2, RoundingMode.HALF_UP);
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Boolean deductAmt(BigDecimal amount) {

        if (this.availableBalance.doubleValue() >= amount.doubleValue()) {
            System.out.print(this.availableBalance);
            System.out.print(amount);
            this.availableBalance.subtract(amount);
            System.out.print(this.availableBalance);
            this.balance.subtract(amount);
            return true;
        } else {
            return false;
        }
    }

    public SavingAccount() {
    }

    public SavingAccountType getSavingAccountType() {
        return savingAccountType;
    }

    public void setSavingAccountType(SavingAccountType savingAccountType) {
        this.savingAccountType = savingAccountType;
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
    
    public BigDecimal getAccumDailyInterest() {
        return accumDailyInterest;
    }

    public void setAccumDailyInterest(BigDecimal accumDailyInterest) {
        this.accumDailyInterest = accumDailyInterest;
    }
    
    public PayMe getPayMe() {
        return payMe;
    }

    public void setPayMe(PayMe payMe) {
        this.payMe = payMe;
    }
    
     public List<TransactionRecord> getTransactionRecord() {
        return transactionRecord;
    }

    public void setTransactionRecord(List<TransactionRecord> transactionRecord) {
        this.transactionRecord = transactionRecord;
    }

}
