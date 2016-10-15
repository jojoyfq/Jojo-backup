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
private BigDecimal monthlyPayment;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
@Override
public Long createHomeLoan(Customer customer,Long loanTypeId,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate){
    
        //generate and check account number
    monthlyPayment=new BigDecimal("0");
        Long accountNum = this.generateLoanAccountNumber();
        LoanType loanType=em.find(LoanType.class,loanTypeId);
        BigDecimal outstandingBalance=new BigDecimal("0");
        if (loanType.getName().equals("SIBOR Package")){
        outstandingBalance=calculateSiborOutstandingBalance(principal.subtract(downpayment),loanTerm,loanType.getSIBOR(),loanType.getSIBORrate1());
        }
        else if (loanType.getName().equals("Fixed Interest Package")){
         outstandingBalance=calculateFixedOutstandingBalance(principal.subtract(downpayment),loanTerm,loanType.getFixedRate());   
        }
         
         Date currentTime = Calendar.getInstance().getTime();
           DateTime payDate = new DateTime(currentTime);
           DateTime currentTime1 = payDate.plusMonths(1);
           Date currentTimestamp=currentTime1.toDate(); 
        
        Loan newLoan=new Loan(accountNum,principal,downpayment,loanTerm,outstandingBalance,currentTimestamp,"inactive",customer);
        em.persist(newLoan);
       
        newLoan.setLoanType(loanType);
        newLoan.setLoanAmount(principal.subtract(downpayment));
        newLoan.setMonthlyPayment(monthlyPayment);
        newLoan.setPayTime(0);

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

@Override
public Long createCarLoan(Customer customer,Long loanTypeId,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate){
     monthlyPayment=new BigDecimal("0");
        //generate and check account number
        Long accountNum = this.generateLoanAccountNumber();
        LoanType loanType=em.find(LoanType.class,loanTypeId);
        BigDecimal outstandingBalance;
      
        outstandingBalance=calculateCarOutstandingBalance(principal.subtract(downpayment),loanTerm,loanType.getInterestRate());
       
        Date currentTime = Calendar.getInstance().getTime();
           DateTime payDate = new DateTime(currentTime);
           DateTime currentTime1 = payDate.plusMonths(1);
           Date currentTimestamp=currentTime1.toDate();
        Loan newLoan=new Loan(accountNum,principal,downpayment,loanTerm,outstandingBalance,currentTimestamp,"inactive",customer);
        em.persist(newLoan);
       
        newLoan.setLoanType(loanType);
        newLoan.setMonthlyPayment(monthlyPayment);
        newLoan.setLoanAmount(principal.subtract(downpayment));
        newLoan.setPayTime(0);

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

@Override
public Long createEducationLoan(Customer customer,Long loanTypeId,BigDecimal principal,Integer loanTerm,Date startDate){
     monthlyPayment=new BigDecimal("0");
        //generate and check account number
        Long accountNum = this.generateLoanAccountNumber();
        LoanType loanType=em.find(LoanType.class,loanTypeId);
        BigDecimal outstandingBalance;
      
        outstandingBalance=calculateCarOutstandingBalance(principal,loanTerm,loanType.getEducationRate());
       
        Date currentTime = Calendar.getInstance().getTime();
           DateTime payDate = new DateTime(currentTime);
           DateTime currentTime1 = payDate.plusMonths(1);
           Date currentTimestamp=currentTime1.toDate();
           
           BigDecimal temp=new BigDecimal("0");
        Loan newLoan=new Loan(accountNum,principal,temp,loanTerm,outstandingBalance,currentTimestamp,"inactive",customer);
        em.persist(newLoan);
       
        newLoan.setLoanType(loanType);
        newLoan.setMonthlyPayment(monthlyPayment);
        newLoan.setLoanAmount(principal);
        newLoan.setPayTime(0);
       

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


@Override
public List<Loan> customerViewListOfLoan(Long customrId){
   Customer customer=em.find(Customer.class,customrId); 
   List<Loan> loans=customer.getLoans();
   return loans;
}

@Override
public Loan customerViewLoan(Long loanId){
   Loan loan=em.find(Loan.class,loanId); 
   return loan;
}

@Override
public BigDecimal calcultateMonthlyPayment(BigDecimal principal,BigDecimal downpayment, Integer loanTerm,Long loanTypeId){
    LoanType loanType=em.find(LoanType.class,loanTypeId);
    Double noValue=0.0;
    BigDecimal monthlyPayment2=new BigDecimal("0");
     if (loanType.getName().equals("SIBOR Package"))
         monthlyPayment2=calculateMonthlyPaymentBalance(principal.subtract(downpayment),loanTerm,loanType.getSIBOR(),loanType.getSIBORrate1());
    else if (loanType.getName().equals("Fixed Interest Package"))
        
     monthlyPayment2=calculateMonthlyPaymentBalance(principal.subtract(downpayment),loanTerm,loanType.getFixedRate(),noValue);
        else if (loanType.getName().equals("Car loan"))
     monthlyPayment2=calculateMonthlyPaymentBalance(principal.subtract(downpayment),loanTerm,loanType.getInterestRate(),noValue);
        else if  (loanType.getName().equals("Education loan"))  
      monthlyPayment2=calculateMonthlyPaymentBalance(principal.subtract(downpayment),loanTerm,loanType.getEducationRate(),noValue);   
     
return monthlyPayment2;
}

private BigDecimal calculateMonthlyPaymentBalance(BigDecimal principal,Integer loanTerm,Double interest1,Double interest2){
    BigDecimal monthlyPayment2=new BigDecimal("0");
 BigDecimal baseRate=new BigDecimal(interest1);
    BigDecimal baseRate2=new BigDecimal(interest2);
      BigDecimal month=new BigDecimal("12");
    BigDecimal trueRate=baseRate.add(baseRate2).divide(month);
    BigDecimal temp=new BigDecimal("1");
    BigDecimal temp2=temp.add(trueRate);
    temp2=temp2.pow(loanTerm);
    temp2=temp.subtract(temp2);
    BigDecimal term=new BigDecimal(loanTerm);
    monthlyPayment2=trueRate.multiply(principal).divide(temp2);
    return monthlyPayment2;
}

@Override
public List<Loan> customerUpdateLoan(Long customerId,Long loanId, BigDecimal downpayment,Integer loanTerm,Date startDate){
    Customer customer=em.find(Customer.class,customerId);
    Loan loan=em.find(Loan.class,loanId);
        loan.setDownpayment(downpayment);
        loan.setLoanTerm(loanTerm);
        loan.setStartDate(startDate);
        loan.setStatus("staffVerified");
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
     public List<Loan> displayLoans(Long customerId) throws ListEmptyException{
         Customer customer=em.find(Customer.class,customerId);
         List<Loan>loans=customer.getLoans();
         if (loans.isEmpty())
             throw new ListEmptyException("You do not have any loans.");
                     return loans;
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
             throw new ListEmptyException("You do not have any loans.");
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
         loan.setLoanAmount(calculateRemainingLoanAmount(loan));
         loan.setMonthlyPayment(temp);
         loan.setLatePayment(temp);
         loan.setPayTime(loan.getPayTime()+1);
         
         
         savingAccount.setAvailableBalance(savingAccount.getAvailableBalance().subtract(amount));
         savingAccount.setBalance(savingAccount.getBalance().subtract(amount));
         
           Date currentTime = Calendar.getInstance().getTime();
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

                TransactionRecord transactionRecord = new TransactionRecord("LP",amount,null,"settled", "Loan Payment",currentTimestamp,savingAccount.getAccountNumber(),loan.getAccountNumber(),savingAccount,"MerlionBank","MerlionBank");
                savingAccount.getTransactionRecord().add(transactionRecord);
                em.persist(transactionRecord);
                em.flush();
                
                return amount;
     }
     
     private BigDecimal calculateRemainingLoanAmount(Loan loan){
         BigDecimal loanAmount=loan.getLoanAmount();
         monthlyPayment=loan.getMonthlyPayment();
         LoanType loanType=loan.getLoanType();
         Double interestRate=0.0;
            
            if (loanType.getName().equals("SIBOR Package")){
                interestRate=1.0+loanType.getSIBOR()+loanType.getSIBORrate1();   
     }
        else if (loanType.getName().equals("Fixed Interest Package")){
            interestRate=1.0+loanType.getFixedRate();
        
        }
        else if (loanType.getName().equals("Car loan")){
            interestRate=1.0+loanType.getInterestRate();
        }
            else if (loanType.getName().equals("Education loan")){
            interestRate=1.0+loanType.getEducationRate();
        }
        
         BigDecimal temp=new BigDecimal(interestRate);
         loanAmount=loanAmount.subtract(monthlyPayment.divide(temp));
         return loanAmount;
         
     }
    
     @Override 
     public BigDecimal displayRedemptionInterest(Long loanId){
         Loan loan=em.find(Loan.class,loanId);
         BigDecimal amount=loan.getLoanAmount();
         BigDecimal redemptionRate=new BigDecimal("1.015");
         amount=amount.multiply(redemptionRate);
         return amount;
         
     }
     
     
     @Override
     public Loan applyEarlyRedemption(Long customerId, Long loanId,Long savingAccountId)throws NotEnoughAmountException{
         Loan loan=em.find(Loan.class,loanId);
         Customer customer=em.find(Customer.class,customerId);
         SavingAccount savingAccount=em.find(SavingAccount.class,savingAccountId);
         
         BigDecimal amount=loan.getLoanAmount();
         BigDecimal redemptionRate=new BigDecimal("1.015");
         amount=amount.multiply(redemptionRate).add(loan.getLoanAmount());
         
         if (amount.compareTo(savingAccount.getAvailableBalance())== 1)
             throw new NotEnoughAmountException("There is not enough amount of money in this savingAccount");
         
         BigDecimal temp=new BigDecimal("0");
         loan.setLoanAmount(temp);
         loan.setMonthlyPayment(temp);
         loan.setLatePayment(temp);
         loan.setPayTime(loan.getPayTime()+1);
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
     
     //staff and customer create existing account
     @Override
    public List<Loan> CreateExistingLoanAccount(Long customerId,BigDecimal monthlyIncome,Long loanTypeId,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate )throws LoanTermInvalidException,EmailNotSendException{
        Customer customer=em.find(Customer.class,customerId);
        LoanType loanType=em.find(LoanType.class,loanTypeId);
        Long accountNum=0L;
        if (loanType.getType().equals("Home")){
            if (loanTerm >420)
                throw new LoanTermInvalidException ("Home repayment period can only be max 35 years");
            if (principal.multiply(new BigDecimal ("0.2")).compareTo(downpayment)==-1)
                throw new LoanTermInvalidException ("Down Payment must be greater than 20% of your total asset value");
       
            accountNum=createHomeLoan(customer,loanTypeId,principal,downpayment,loanTerm,startDate);
        }
        else if (loanType.getType().equals("Car")){
            if (loanTerm >84)
                throw new LoanTermInvalidException ("Car repayment period can only be max 7 years");
          accountNum=createCarLoan(customer,loanTypeId,principal,downpayment,loanTerm,startDate);  
        }
         else if (loanType.getType().equals("Education")){
            if (loanTerm >96)
                throw new LoanTermInvalidException ("Education repayment period can only be max 8 years");
          accountNum=createEducationLoan(customer,loanTypeId,principal,loanTerm,startDate);  
        }
        
         try{
               sendLoanVerificationEmail(customer.getName(),customer.getEmail(),accountNum);
              }catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
       return customer.getLoans();
    } 
   
    private void sendLoanVerificationEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - New Loan application submitted";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Congratulations! You have successfully submitted a Merlion Online Banking Loan application!</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Loan Account Number: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that your loan request will be reviewed by our staff. We will send email notification once it is confirmed. Meanwhile you can Log in and go to Loan management to track the status.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }
}

