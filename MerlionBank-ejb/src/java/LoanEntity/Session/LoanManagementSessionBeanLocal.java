/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.Loan;
import LoanEntity.LoanType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface LoanManagementSessionBeanLocal {
  //view all pending loans

    public List<Loan> staffViewPendingLoans();

//verify Loans 
    //public boolean staffAccessLoanRisk(Long customerId, Long loanId);

    public List<Loan> staffRejectLoans(Long staffId, Long loanId) throws EmailNotSendException;

    public List<Loan> staffApproveLoans(Long staffId, Long loanId) throws EmailNotSendException;

   public Loan staffUpdateLoan(Long staffId, Long loanId, BigDecimal principal, BigDecimal downpayment, Integer loanTerm, Date startDate)throws EmailNotSendException;

    //counter staff activate loan
    //public boolean staffGenerateLoanReport(Long loanId);

    public Loan staffActivateLoan(Long loanId,Date loanDate);

    public List<Loan> searchLoan(String customerIc) throws UserNotExistException, UserNotActivatedException, ListEmptyException;

    public List<LoanType> viewLoanTypeList();

    public List<LoanType> updateLoanType(Long loanTypeId, Double interest1, Double interest2);
    
   public double calculateRisk(Long customerId, Long longId);
}
