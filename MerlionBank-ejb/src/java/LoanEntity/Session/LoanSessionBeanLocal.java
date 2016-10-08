/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import Exception.EmailNotSendException;
import LoanEntity.Loan;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface LoanSessionBeanLocal {
   
    // loan calculator
   public BigDecimal calcultateMonthlyPayment(BigDecimal principal,BigDecimal downpayment, Integer loanTerm,Long loanTypeId);
   
   //create new account call these two functions
public Long createHomeLoan(Customer customer,String loanTypeName,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate);
public Long createCarLoan(Customer customer,String loanTypeName,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate);
   
   //view loan summary
   public List<Loan> customerViewListOfLoan(Long customrId);
   public Loan customerViewLoan(Long loanId);
   
   //customer edit loan
    public List<Loan> customerUpdateLoan(Long customerId,Long loanId, BigDecimal downpayment,Integer loanTerm,Date startDate);
    public List<Loan> customerCancelLoan(Long customerId, Long loanId);
     public List<Loan> customerAcceptLoan(Long customerId, Long loanId) throws EmailNotSendException;
   
   
   
}
