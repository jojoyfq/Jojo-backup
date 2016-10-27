/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import BillEntity.GIROArrangement;
import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
import Exception.EmailNotSendException;
import Exception.LoanTermInvalidException;
import Exception.NotEnoughAmountException;
import LoanEntity.Loan;
import LoanEntity.LoanType;
import Other.Session.sendEmail;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;

/**
 *
 * @author a0113893
 */
@Stateless
public class LoanTimerSessionBean implements LoanTimerSessionBeanLocal {
@PersistenceContext
    private EntityManager em;
 @EJB

private LoanApplicationSessionBean lasb;
 //Timer
    @Override
      public void closeAccounts(){
         Query query = em.createQuery("SELECT a FROM Loan a");
        List<Loan> currentLoans = new ArrayList(query.getResultList()); 
       List<Loan>  loans=new ArrayList<Loan>();
       
       for (int i=0;i<currentLoans.size();i++){
          if (currentLoans.get(i).getStatus().equals("staffVerified") ||currentLoans.get(i).getStatus().equals("customerVerified")|| currentLoans.get(i).getStartDate()!=null)
              loans.add(currentLoans.get(i));
      }
       Date todayDate=Calendar.getInstance().getTime();
       for (int j=0;j<loans.size();j++){
           if (todayDate.after(loans.get(j).getStartDate())){
               loans.get(j).setStatus("terminated");
               
               //Close GIRO
               Query q = em.createQuery("SELECT a FROM GIROArrangement a");
        List<GIROArrangement> GIROArrangements = new ArrayList(q.getResultList());
        
        for (int i=0;i<GIROArrangements.size();i++){
            if (GIROArrangements.get(i).equals("Merlion Bank Loan") && GIROArrangements.get(i).getStatus().equals("active") && GIROArrangements.get(i).getBillingOrganization().equals(loans.get(j).getAccountNumber().toString()))
               GIROArrangements.get(i).setStatus("terminated");
        } 
             
               try{
     sendLoanTerminationEmail(loans.get(j).getCustomer().getName(),loans.get(j).getCustomer().getEmail(),loans.get(j).getAccountNumber());
     
              } catch (MessagingException ex) {
            System.out.println("Error sending email.");
        }
           }
       }
           
      }
      
      private void sendLoanTerminationEmail(String name, String email,Long accountNumber) throws MessagingException {
      String subject = "Merlion Bank - Loan Application Closed";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Sorry! You application of Loan "+ accountNumber+"has been terminated due to long time no reponses.</h1><br />"
                + "<p style=\"color: #ff0000;\">For more enquiries, please kindly contact +65 8888 8888. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please log in and use case management page and submit your enquiry there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);  
    }
     
      @Override
       public void calculateLatePayment(Date currentDate) throws EmailNotSendException{
         Query query = em.createQuery("SELECT a FROM Loan a");
        List<Loan> currentLoans = new ArrayList(query.getResultList()); 
       List<Loan>  loans=new ArrayList<Loan>();
       BigDecimal lateRate=new BigDecimal("0.02");
 
       for (int i=0;i<currentLoans.size();i++){
          if (currentLoans.get(i).getStatus().equals("active"))
              loans.add(currentLoans.get(i));
      }
       
       Loan loan=new Loan();
       for (int j=0;j<loans.size();j++){
           loan=loans.get(j);
           DateTime payDate = new DateTime(loan.getLoanDate());
           DateTime compareDate = payDate.plusMonths(loan.getPaidTerm()+1);
           DateTime current2 = new DateTime(currentDate);
           
           Months inBetween = Months.monthsBetween(compareDate, current2);
        int numOfMonths = inBetween.getMonths();
        BigDecimal duration=new BigDecimal (numOfMonths);
        
          if (current2.isAfter(compareDate)){
             BigDecimal latePayment=loan.getLatePayment();
              BigDecimal monthlyPayment= loan.getMonthlyPayment();
              
              BigDecimal temp=latePayment.add(monthlyPayment).multiply(lateRate);
              latePayment.add(temp);
              loan.setLatePayment(latePayment);
              try{
               sendLatePaymentNotificationEmail(loan.getCustomer().getName(),loan.getCustomer().getEmail(),loan.getAccountNumber());
              }catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
                         
          }     
           
       }
       }
       
@Override        
public void updateMonthlyPayment(Date currentDate) throws EmailNotSendException{
            Query query = em.createQuery("SELECT a FROM Loan a");
        List<Loan> currentLoans = new ArrayList(query.getResultList()); 
       List<Loan>  loans=new ArrayList<Loan>();
       BigDecimal monthlyPayment2=new BigDecimal(0);
 
       for (int i=0;i<currentLoans.size();i++){
          if (currentLoans.get(i).getStatus().equals("active"))
              loans.add(currentLoans.get(i));
      }
       
       Loan loan=new Loan();
       for (int j=0;j<loans.size();j++){
           loan=loans.get(j);
           LoanType loanType=loan.getLoanType();
           DateTime payDate = new DateTime(loan.getLoanDate());
           DateTime compareDate = payDate.plusMonths(loan.getPaidTerm());
           //compareDate=compareDate.minusDays(3);
           
           DateTime current2 = new DateTime(currentDate);
           
           String newstring1 = new SimpleDateFormat("yyyy-MM-dd").format(compareDate);
           String newstring2 = new SimpleDateFormat("yyyy-MM-dd").format(current2);
           BigDecimal temp=new BigDecimal("zero");
           Double temp2=0.0;
           
           if (newstring1.equals(newstring2)){
               if (loanType.getName().equals("SIBOR Package")) {
                monthlyPayment2 = lasb.fixedCalculator(loan.getPrincipal().subtract(loan.getDownpayment()), loan.getLoanTerm(), loanType.getSIBOR(), loanType.getSIBORrate1());
            } else if (loanType.getName().equals("Fixed Interest Package")) {
                if (loan.getPaidTerm()<=36)
                monthlyPayment2 = lasb.fixedCalculator(loan.getPrincipal().subtract(loan.getDownpayment()), loan.getLoanTerm(), loan.getInterestRate1(), temp2);
            } else
               monthlyPayment2 = lasb.fixedCalculator(loan.getPrincipal().subtract(loan.getDownpayment()), loan.getLoanTerm(), loanType.getSIBOR(), loan.getInterestRate2());
                
        } else if (loanType.getType().equals("Car")) {
             monthlyPayment2 = lasb.fixedCalculator(loan.getPrincipal().subtract(loan.getDownpayment()), loan.getLoanTerm(), loan.getInterestRate1(), temp2);
        } else if (loanType.getType().equals("Education")) {
            monthlyPayment2 = lasb.fixedCalculator(loan.getPrincipal().subtract(loan.getDownpayment()), loan.getLoanTerm(), loan.getInterestRate1(), temp2);

        }
              
               loan.getMonthlyPayment().add(monthlyPayment2);
              em.flush();
              try{
               sendRepaymentNotificationEmail(loan.getCustomer().getName(),loan.getCustomer().getEmail(),loan.getAccountNumber());
              }catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
              }
           
       }
       
           
           
     private void sendRepaymentNotificationEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Your Loan Payment is late";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Your loan payment is overdued.</h1><br />"
                + "<h1>Late interest 0.6% daily will be charged to your loan outstanding amount</h1>"
                + "<h2 align=\"center\">Loan Account Number: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that If you did not make the payment after furthermore 3 months, your will be enrolled into out bad debt process. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }  
           
      private void sendLatePaymentNotificationEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Your loan bill for this month is here";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Please make the payment of your monthly loan payment.</h1><br />"
                + "<h1>You have 1 month to make the payment.Otherwise late interest 0.6% daily will be charged to youe pending amount</h1>"
                + "<h2 align=\"center\">Loan Account Number: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that that if you have already applied for loan GIRO deduction, please ensure you have enough amount in your linked account. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }  
    
      @Override
    public void autoBadDebt(Date currentDate)throws EmailNotSendException{
         Query query = em.createQuery("SELECT a FROM Loan a");
        List<Loan> currentLoans = new ArrayList(query.getResultList()); 
       List<Loan>  loans=new ArrayList<Loan>();
 
       for (int i=0;i<currentLoans.size();i++){
          if (currentLoans.get(i).getStatus().equals("active"))
              loans.add(currentLoans.get(i));
      }
       
       Loan loan=new Loan();
       for (int j=0;j<loans.size();j++){
           loan=loans.get(j);
           DateTime payDate = new DateTime(loan.getLoanDate());
           DateTime compareDate = payDate.plusMonths(loan.getPaidTerm()+4);
           DateTime current2 = new DateTime(currentDate);
        
          if (current2.isAfter(compareDate)){
             loan.setStatus("bad debt");
              
              try{
               sendBadDebtNotificationEmail(loan.getCustomer().getName(),loan.getCustomer().getEmail(),loan.getAccountNumber());
              }catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
                         
          }     
           
       }
        
    }
    
     private void sendBadDebtNotificationEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Your loan has been marked as bad debt";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Your loan has not been paid for 4 months.</h1><br />"
                + "<h1>Sooner a lawyer will contact you regarding this bad debt in short time. Your credit rating will be badly affected.</h1>"
                + "<h2 align=\"center\">Loan Account Number: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that if you want to paid back the loan, please fo to our main branch. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }  
     
    
     public void loanPayByGIRO()throws NotEnoughAmountException{
         Query query = em.createQuery("SELECT a FROM GIROArrangement a");
        List<GIROArrangement> GIROArrangements = new ArrayList(query.getResultList());
        List<GIROArrangement> temp=new ArrayList<GIROArrangement>();
        
        for (int i=0;i<GIROArrangements.size();i++){
            if (GIROArrangements.get(i).equals("Merlion Bank Loan") && GIROArrangements.get(i).getStatus().equals("active"))
                temp.add(GIROArrangements.get(i));
        }
        
        for (int i=0;i<temp.size();i++){
            GIROArrangement giroArrangement=temp.get(i);
            Long loanAccountNum=Long.valueOf(giroArrangement.getBillReference());
            SavingAccount savingAccount= giroArrangement.getSavingAccount();
      
      Query q = em.createQuery("SELECT a FROM Loan a WHERE a.accountNumber = :accountNumber");
        q.setParameter("accountNumber", loanAccountNum);
        Loan loan = (Loan) q.getSingleResult();
        
      BigDecimal amount=loan.getMonthlyPayment().add(loan.getLatePayment());
         if (amount.compareTo(savingAccount.getAvailableBalance())== 1)
             throw new NotEnoughAmountException("There is not enough amount of money in this savingAccount");
         
         BigDecimal temp1=new BigDecimal("0");
         loan.setOutstandingBalance(loan.getOutstandingBalance().subtract(loan.getMonthlyPayment()));
         loan.setMonthlyPayment(temp1);
         loan.setLatePayment(temp1);
         loan.setPaidTerm(loan.getPaidTerm()+1);
         
         
         savingAccount.setAvailableBalance(savingAccount.getAvailableBalance().subtract(amount));
         savingAccount.setBalance(savingAccount.getBalance().subtract(amount));
         
           Date currentTime = Calendar.getInstance().getTime();
           loan.setLoanDate(currentTime);
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

                TransactionRecord transactionRecord = new TransactionRecord("LP",amount,null,"settled", "GIRO Loan Payment",currentTimestamp,savingAccount.getAccountNumber(),loan.getAccountNumber(),savingAccount,"MerlionBank","MerlionBank");
                savingAccount.getTransactionRecord().add(transactionRecord);
                em.persist(transactionRecord);
                em.flush();
        
        }
        
        
        
      }
}
