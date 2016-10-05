/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import Exception.ListEmptyException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.Loan;
import LoanEntity.LoanType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface LoanManagementSessionBeanLocal {
  
    public List<Loan> staffVerifyLoans();
    
    public boolean staffAccessLoanRisk(Long customerId,Long loanId);
    
    public boolean staffGenerateLoanReport(Long loanId);
    
    public Loan staffActivateLoan(Long loanId);
    
     public List<Loan> searchLoan(String customerIc)throws UserNotExistException,UserNotActivatedException,ListEmptyException;
    
    public List<LoanType> viewLoanTypeList();
    
    public LoanType updateLoanType(Long loanTypeId,Double interest1,Double interest2);
}
