/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import DepositEntity.FixedDepositAccount;
import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.LoanTermInvalidException;
import Exception.NotEnoughAmountException;
import LoanEntity.Loan;
import LoanEntity.LoanType;
import Other.Session.sendEmail;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joda.time.DateTime;

/**
 *
 * @author a0113893
 */
@Stateless
public class LoanSessionBean implements LoanSessionBeanLocal {
@PersistenceContext
    private EntityManager em;
 @EJB

    private LoanApplicationSessionBeanLocal lasb;

   @Override 
     public List<Loan> displayLoans(Long customerId) throws ListEmptyException{
         Customer customer=em.find(Customer.class,customerId);
         List<Loan>loans=customer.getLoans();
         if (loans.isEmpty())
             throw new ListEmptyException("You do not have any loans.");
                     return loans;
     }
     

@Override
public Loan customerViewLoan(Long loanId){
   Loan loan=em.find(Loan.class,loanId); 
   return loan;
}


@Override
public List<Loan> customerUpdateLoan(Long customerId,Long loanId, BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate){
    Customer customer=em.find(Customer.class,customerId);
    Loan loan=em.find(Loan.class,loanId);
    LoanType loanType = loan.getLoanType();

        BigDecimal monthlyPayment2 = new BigDecimal(0);
        BigDecimal term = new BigDecimal(loanTerm);
        Double temp = 0.0;
        if (loanType.getName().equals("SIBOR Package")) {
            System.out.println("principal "+principal);
            System.out.println("downpayment "+downpayment);
            System.out.println("loanTerm "+loanTerm);
             System.out.println("loan.getInterestRate1()"+loan.getInterestRate1());
            System.out.println("loan.getInterestRate2()"+loan.getInterestRate2());
            monthlyPayment2 = lasb.fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getSIBOR(), loanType.getSIBORrate1());
       loan.setOutstandingBalance(monthlyPayment2.multiply(term));
        } else if (loanType.getName().equals("Fixed Interest Package")) {
            monthlyPayment2 = lasb.fixedCalculator(principal.subtract(downpayment), loanTerm, loan.getInterestRate1(), temp);
            
            BigDecimal monthly2 = lasb.fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getSIBOR(), loanType.getFixedRate2());
            BigDecimal temp2 = new BigDecimal(36);
            BigDecimal temp3 = new BigDecimal(loanTerm - 36);
            loan.setOutstandingBalance(monthlyPayment2.multiply(temp2).add(monthly2.multiply(temp3)));
        } else if (loanType.getType().equals("Car")) {
            monthlyPayment2 = lasb.fixedCalculator(principal.subtract(downpayment), loanTerm, loan.getInterestRate1(), temp);
            loan.setOutstandingBalance(monthlyPayment2.multiply(term));
        } else if (loanType.getType().equals("Education")) {
            monthlyPayment2 = lasb.fixedCalculator(principal.subtract(downpayment), loanTerm, loan.getInterestRate1(), temp);
            loan.setOutstandingBalance(monthlyPayment2.multiply(term));
        }

        loan.setMonthlyPayment(monthlyPayment2);
        loan.setPrincipal(principal);
        loan.setDownpayment(downpayment);
        loan.setLoanTerm(loanTerm);
         Date currentTime = Calendar.getInstance().getTime();
        DateTime payDate = new DateTime(currentTime);
        DateTime currentTime1 = payDate.plusMonths(1);
        Date currentTimestamp = currentTime1.toDate();

        loan.setStartDate(currentTimestamp);
        
        loan.setStatus("inactive");
        em.flush();
      return customer.getLoans();
    
}

@Override
public List<Loan> customerCancelLoan(Long customerId, Long loanId){
     Customer customer=em.find(Customer.class,customerId);
      Loan loan=em.find(Loan.class,loanId);
      loan.setStatus("terminated");
    return customer.getLoans();
}
   
@Override
    public List<Loan> customerAcceptLoan(Long customerId, Long loanId) throws EmailNotSendException{
      Loan loan=em.find(Loan.class,loanId);
     Customer customer=em.find(Customer.class,customerId);
     loan.setStatus("customerVerified");  
     
      Date currentTime = Calendar.getInstance().getTime();
           DateTime payDate = new DateTime(currentTime);
           DateTime currentTime1 = payDate.plusMonths(1);
           Date currentTimestamp=currentTime1.toDate();
           
           loan.setStartDate(currentTimestamp);
     
     try{
     sendApproveEmail(loan.getCustomer().getName(),loan.getCustomer().getEmail(),loan.getAccountNumber());
     } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
     return customer.getLoans();
    }
    
     private void sendApproveEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Loan Application Approved";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Cour loan has been approved by our bank staff.</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Loan Account Number: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to go to our counter to sign the loan contract. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }
    
  
     @Override
     public List<SavingAccount> displaySavingAccounts(Long customerId)throws ListEmptyException{
         Customer customer=em.find(Customer.class,customerId);
         List<SavingAccount>currentSavingAccounts=customer.getSavingAccounts();
         List<SavingAccount>savingAccounts=new ArrayList<SavingAccount>();
         
         for (int i=0;i<currentSavingAccounts.size();i++){
             if (currentSavingAccounts.get(i).getStatus().equals("active"))
                 savingAccounts.add(currentSavingAccounts.get(i));
         }
         if (savingAccounts.isEmpty())
             throw new ListEmptyException("You do not have any active saving accounts.");
                     return savingAccounts;
     }
     
     @Override
     public BigDecimal loanPayBySaving(Long customerId, Long savingAccountId, Long loanId)throws NotEnoughAmountException{
         Customer customer=em.find(Customer.class,customerId);
         SavingAccount savingAccount=em.find(SavingAccount.class,savingAccountId);
         Loan loan=em.find(Loan.class, loanId);
         
         BigDecimal amount=loan.getMonthlyPayment().add(loan.getLatePayment());
         if (amount.compareTo(savingAccount.getAvailableBalance())== 1)
             throw new NotEnoughAmountException("There is not enough amount of money in this savingAccount");
         
         BigDecimal temp=new BigDecimal("0");
         loan.setOutstandingBalance(loan.getOutstandingBalance().subtract(loan.getMonthlyPayment()));
         loan.setMonthlyPayment(temp);
         loan.setLatePayment(temp);
         loan.setPaidTerm(loan.getPaidTerm()+1);
         
         
         savingAccount.setAvailableBalance(savingAccount.getAvailableBalance().subtract(amount));
         savingAccount.setBalance(savingAccount.getBalance().subtract(amount));
         
           Date currentTime = Calendar.getInstance().getTime();
           loan.setLoanDate(currentTime);
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

                TransactionRecord transactionRecord = new TransactionRecord("LP",amount,null,"settled", "Loan Payment",currentTimestamp,savingAccount.getAccountNumber(),loan.getAccountNumber(),savingAccount,"MerlionBank","MerlionBank");
                savingAccount.getTransactionRecord().add(transactionRecord);
                em.persist(transactionRecord);
                em.flush();
                
                return amount;
     }
     
    
     @Override 
     public BigDecimal displayRedemptionInterest(Long loanId){
         Loan loan=em.find(Loan.class,loanId);
         BigDecimal amount=loan.getOutstandingBalance();
         BigDecimal redemptionRate=new BigDecimal("1.015");
         amount=amount.multiply(redemptionRate);
         return amount;
         
     }
     
     
     @Override
     public Loan applyEarlyRedemption(Long customerId, Long loanId,Long savingAccountId)throws NotEnoughAmountException{
         Loan loan=em.find(Loan.class,loanId);
         Customer customer=em.find(Customer.class,customerId);
         SavingAccount savingAccount=em.find(SavingAccount.class,savingAccountId);
         
         BigDecimal amount=loan.getOutstandingBalance();
         BigDecimal redemptionRate=new BigDecimal("1.015");
         amount=amount.multiply(redemptionRate);
         
         if (amount.compareTo(savingAccount.getAvailableBalance())== 1)
             throw new NotEnoughAmountException("There is not enough amount of money in this savingAccount");
         
         BigDecimal temp=new BigDecimal("0");
         loan.setLoanAmount(temp);
         loan.setMonthlyPayment(temp);
         loan.setLatePayment(temp);
         loan.setPaidTerm(loan.getPaidTerm()+1);
         loan.setOutstandingBalance(temp);
         loan.setStatus("completed");
         
         savingAccount.setAvailableBalance(savingAccount.getAvailableBalance().subtract(amount));
         savingAccount.setBalance(savingAccount.getBalance().subtract(amount));
         
            Date currentTime = Calendar.getInstance().getTime();
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

                TransactionRecord transactionRecord = new TransactionRecord("LP",amount,null,"settled", "Loan Early Redemption",currentTimestamp,savingAccount.getAccountNumber(),loan.getAccountNumber(),savingAccount,"MerlionBank","MerlionBank");
                em.persist(transactionRecord);
                em.flush();
                
                return loan;
         
     }
    
}

