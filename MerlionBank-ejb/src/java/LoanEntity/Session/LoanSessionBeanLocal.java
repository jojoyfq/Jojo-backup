/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.LoanTermInvalidException;
import Exception.NotEnoughAmountException;
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
    
   //customer edit loan
public List<Loan> customerUpdateLoan(Long customerId,Long loanId, BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate);
    public List<Loan> customerCancelLoan(Long customerId, Long loanId);
     public List<Loan> customerAcceptLoan(Long customerId, Long loanId) throws EmailNotSendException;
   public Loan customerViewLoan(Long loanId);
   //one time loan payment
     public List<Loan> displayLoans(Long customerId) throws ListEmptyException;
     public List<SavingAccount> displaySavingAccounts(Long customerId)throws ListEmptyException;
     public BigDecimal loanPayBySaving(Long customerId, Long savingAccountId, Long loanId)throws NotEnoughAmountException;
     
     //loan payment by GIRO
     //loan payment by external organization
     
     //early redemption-display interest
     public BigDecimal displayRedemptionInterest(Long loanId);
     
     //early redemption-choose redemption method
     //early redemption- choose pay my saving account and display saving account (using function above)
     public Loan applyEarlyRedemption(Long customerId, Long loanId,Long savingAccountId)throws NotEnoughAmountException;
   
      //staff and customer create existing account
   //public List<Loan> CreateExistingLoanAccount(Long customerId,BigDecimal monthlyIncome,Long loanTypeId,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate )throws LoanTermInvalidException,EmailNotSendException;
}
