/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import DepositEntity.FixedDepositAccount;
import LoanEntity.Loan;
import LoanEntity.LoanType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author a0113893
 */
@Stateless
public class LoanSessionBean implements LoanSessionBeanLocal {
@PersistenceContext
    private EntityManager em;
private BigDecimal monthlyPayment;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
@Override
public Long createHomeLoan(Customer customer,Long loanTypeId,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate){
    
        //generate and check account number
        Long accountNum = this.generateLoanAccountNumber();
        LoanType loanType=em.find(LoanType.class,loanTypeId);
        BigDecimal outstandingBalance=new BigDecimal("0");
        if (loanType.getName().equals("SIBOR Package")){
        outstandingBalance=calculateSiborOutstandingBalance(principal.subtract(downpayment),loanTerm,loanType.getSIBOR(),loanType.getSIBORrate1());
        }
        else if (loanType.getName().equals("Fixed Interest Package")){
         outstandingBalance=calculateFixedOutstandingBalance(principal,loanTerm,loanType.getFixedRate());   
        }
        
        Loan newLoan=new Loan(accountNum,principal,downpayment,loanTerm,outstandingBalance,startDate,"inactive",customer);
        em.persist(newLoan);
       
        newLoan.setLoanType(loanType);
        newLoan.setMonthlyPayment(monthlyPayment);

        List<Loan> loans = new ArrayList<Loan>();
        //customer may alr have fixed acct
        if (customer.getLoans()== null) {
            loans.add(newLoan);
            customer.setLoans(loans);
        } else {//alr have fixed acct
            customer.getLoans().add(newLoan);
        }

        em.flush();
    return accountNum;
}

private long generateLoanAccountNumber() {
        int a = 1;
        Random rnd = new Random();
        int number = 10000000 + rnd.nextInt(90000000);
        Long accountNumber = Long.valueOf(number);
        Query q2 = em.createQuery("SELECT c.accountNumber FROM Loan c");
        List<Long> existingAcctNum = new ArrayList(q2.getResultList());
        while (a == 1) {

            if ((existingAcctNum.contains(accountNumber)) || (number / 10000000 == 0)) {
                number = 10000000 + rnd.nextInt(90000000);
                accountNumber = Long.valueOf(number);
                a = 1;
            } else {
                a = 0;
            }
        }

        return accountNumber;
    }

private BigDecimal calculateFixedOutstandingBalance(BigDecimal principal,Integer loanTerm,Double fixedRate){
    BigDecimal baseRate=new BigDecimal(fixedRate);
    BigDecimal month=new BigDecimal("12");
    BigDecimal trueRate=baseRate.divide(month);
    BigDecimal temp=new BigDecimal("1");
    BigDecimal temp2=temp.add(trueRate);
    temp2=temp2.pow(loanTerm);
    temp2=temp.subtract(temp2);
    BigDecimal term=new BigDecimal(loanTerm);
    monthlyPayment=trueRate.multiply(principal).divide(temp2);
    BigDecimal oustandingAmount=monthlyPayment.multiply(term);
        System.out.println("monthly payment is"+monthlyPayment);
        System.out.println("Outstanding Amount is"+oustandingAmount);
return oustandingAmount;
}

private BigDecimal calculateSiborOutstandingBalance(BigDecimal principal,Integer loanTerm,Double SIBOR,Double SIBORrate1){
    BigDecimal baseRate=new BigDecimal(SIBOR);
    BigDecimal baseRate2=new BigDecimal(SIBORrate1);
      BigDecimal month=new BigDecimal("12");
    BigDecimal trueRate=baseRate.add(baseRate2).divide(month);
    BigDecimal temp=new BigDecimal("1");
    BigDecimal temp2=temp.add(trueRate);
    temp2=temp2.pow(loanTerm);
    temp2=temp.subtract(temp2);
    BigDecimal term=new BigDecimal(loanTerm);
    monthlyPayment=trueRate.multiply(principal).divide(temp2);
    BigDecimal oustandingAmount=monthlyPayment.multiply(term);
        System.out.println("monthly payment is"+monthlyPayment);
        System.out.println("Outstanding Amount is"+oustandingAmount);
return oustandingAmount;
}

public Long createCarLoan(Customer customer,Long loanTypeId,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate){
    
        //generate and check account number
        Long accountNum = this.generateLoanAccountNumber();
        LoanType loanType=em.find(LoanType.class,loanTypeId);
        BigDecimal outstandingBalance;
      
        outstandingBalance=calculateCarOutstandingBalance(principal.subtract(downpayment),loanTerm,loanType.getInterestRate());
       
        
        Loan newLoan=new Loan(accountNum,principal,downpayment,loanTerm,outstandingBalance,startDate,"inactive",customer);
        em.persist(newLoan);
       
        newLoan.setLoanType(loanType);
        newLoan.setMonthlyPayment(monthlyPayment);

        List<Loan> loans = new ArrayList<Loan>();
        //customer may alr have fixed acct
        if (customer.getLoans()== null) {
            loans.add(newLoan);
            customer.setLoans(loans);
        } else {//alr have fixed acct
            customer.getLoans().add(newLoan);
        }

        em.flush();
    return accountNum;
}

private BigDecimal calculateCarOutstandingBalance(BigDecimal principal,Integer loanTerm,Double fixedRate){
    BigDecimal baseRate=new BigDecimal(fixedRate);
    BigDecimal month=new BigDecimal("12");
    BigDecimal trueRate=baseRate.divide(month);
    BigDecimal temp=new BigDecimal("1");
    BigDecimal temp2=temp.add(trueRate);
    temp2=temp2.pow(loanTerm);
    temp2=temp.subtract(temp2);
    BigDecimal term=new BigDecimal(loanTerm);
    monthlyPayment=trueRate.multiply(principal).divide(temp2);
    BigDecimal oustandingAmount=monthlyPayment.multiply(term);
        System.out.println("monthly payment is"+monthlyPayment);
        System.out.println("Outstanding Amount is"+oustandingAmount);
return oustandingAmount;
}

public List<Loan> customerViewListOfLoan(Long customrId){
   Customer customer=em.find(Customer.class,customrId); 
   List<Loan> loans=customer.getLoans();
   return loans;
}

public Loan customerViewLoan(Long loanId){
   Loan loan=em.find(Loan.class,loanId); 
   return loan;
}


}
