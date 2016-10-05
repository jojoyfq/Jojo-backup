/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import CommonEntity.StaffRole;
import Exception.ListEmptyException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.Loan;
import LoanEntity.LoanType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author a0113893
 */
@Stateless
public class LoanManagementSessionBean implements LoanManagementSessionBeanLocal {
@PersistenceContext
    private EntityManager em;

 public List<Loan> staffVerifyLoans();
    
    public boolean staffAccessLoanRisk(Long customerId,Long loanId);
    
    public boolean staffGenerateLoanReport(Long loanId);
    
    @Override
    public Loan staffActivateLoan(Long loanId){
        Loan loan=em.find(Loan.class,loanId);
        loan.setStatus("active");
        em.flush();
        return loan;
        
    }
    
    @Override
    public List<Loan> searchLoan(String customerIc)throws UserNotExistException,UserNotActivatedException,ListEmptyException{
         System.out.println("testing: "+customerIc);
     Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", customerIc);
        List<Customer> temp = new ArrayList(q.getResultList());
        System.out.println("testing: "+temp.size());
        if (temp.isEmpty()) {
            System.out.println("Username " + customerIc + " does not exist!");
          throw new UserNotExistException("Username " + customerIc + " does not exist, please try again");
        }
        
            int size=temp.size();
            Customer customer=temp.get(size-1);
            //System.out.println("testing: "+customer.getIc());
            if (customer.getStatus().equals("terminated")){
                 System.out.println("Username " + customerIc + " does not exist!");
            throw new UserNotExistException("Username " + customerIc + " does not exist, please try again");    
            }
            else if (customer.getStatus().equals("inactive")){
                 System.out.println("Username " + customerIc + "Customer has not activated his or her account!"); 
             throw new UserNotActivatedException("Username " + customerIc + "Customer has not activated his or her account!");
            }
            else {
              System.out.println("Username " + customerIc + " IC check pass!");  
            }
            List<Loan> loans=customer.getLoans();
            if (loans.isEmpty())
                throw new ListEmptyException ("There are no loans under this customer");
            
            return loans;
            
    }
    
    @Override
    public List<LoanType> viewLoanTypeList(){
       Query query = em.createQuery("SELECT a FROM LoanType a");
        List<LoanType> loanTypes = new ArrayList(query.getResultList());
        return loanTypes;
    }
    
    @Override
    public LoanType updateLoanType(Long loanTypeId,Double interest1,Double interest2){
        LoanType loanType=em.find(LoanType.class,loanTypeId);
     if (loanType.getName().equals("SIBOR Package")){
         loanType.setSIBOR(interest1);
         loanType.setSIBORrate1(interest2);
     }
        else if (loanType.getName().equals("Fixed Interest Package")){
        loanType.setFixedRate(interest1);
        }
        else if (loanType.getName().equals("Car loan")){
            loanType.setInterestRate(interest1);
        }
    return loanType;
        
    }
  
}
