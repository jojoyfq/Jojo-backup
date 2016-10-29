/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity;

import CommonEntity.Customer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author a0113893
 */
@Entity
public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long accountNumber;
    private BigDecimal principal;
    private BigDecimal downpayment;
    private Integer loanTerm;
    private BigDecimal outstandingBalance;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    private String status;
    private BigDecimal monthlyPayment;
    private BigDecimal latePayment;
    private Integer paidTerm;
    private BigDecimal loanAmount;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date loanDate;
    private Double interestRate1;
    private Double interestRate2;
    private String homeType;
    private String homeAddress;
    private Long postalCode;
    private String carMode;
    private String institution;
    private String major;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date graduationDate;
    //private Integer payTime;

//    public Integer getPayTime() {
//        return payTime;
//    }
//
//    public void setPayTime(Integer payTime) {
//        this.payTime = payTime;
//    }
//    

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    
    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Long getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Long postalCode) {
        this.postalCode = postalCode;
    }

    public String getCarMode() {
        return carMode;
    }

    public void setCarMode(String carMode) {
        this.carMode = carMode;
    }


    
    public Double getInterestRate1() {
        return interestRate1;
    }

    public void setInterestRate1(Double interestRate1) {
        this.interestRate1 = interestRate1;
    }

    public Double getInterestRate2() {
        return interestRate2;
    }

    public void setInterestRate2(Double interestRate2) {
        this.interestRate2 = interestRate2;
    }
    

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }
    
    

    public Integer getPaidTerm() {
        return paidTerm;
    }

    public void setPaidTerm(Integer paidTerm) {
        this.paidTerm = paidTerm;
    }
    
    

    public BigDecimal getLatePayment() {
        return latePayment;
    }

    public void setLatePayment(BigDecimal latePayment) {
        this.latePayment = latePayment;
    }
    
    

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    private Customer customer;
   // @ManyToOne
    //private Staff staff;
    @ManyToOne
    private LoanType loanType;

    public Loan() {
    }

    public Loan(Long accountNumber, BigDecimal principal, BigDecimal downpayment, Integer loanTerm, String status, BigDecimal monthlyPayment, Integer paidTerm, BigDecimal loanAmount, Customer customer) {
        this.accountNumber = accountNumber;
        this.principal = principal;
        this.downpayment = downpayment;
        this.loanTerm = loanTerm;
        this.status = status;
        this.monthlyPayment = monthlyPayment;
        this.paidTerm = paidTerm;
        this.loanAmount = loanAmount;
        this.customer = customer;
    }

 

    
    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getDownpayment() {
        return downpayment;
    }

    public void setDownpayment(BigDecimal downpayment) {
        this.downpayment = downpayment;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

//    public Staff getStaff() {
//        return staff;
//    }
//
//    public void setStaff(Staff staff) {
//        this.staff = staff;
//    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
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
        if (!(object instanceof Loan)) {
            return false;
        }
        Loan other = (Loan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LoanEntity.Loan[ id=" + id + " ]";
    }
    
}
