/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.Loan;
import LoanEntity.LoanType;
import Other.Session.sendEmail;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
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

@EJB
private LoanSessionBeanLocal lsbl;

@Override
public List<Loan> staffViewPendingLoans(){
    Query query = em.createQuery("SELECT a FROM Loan a");
        List<Loan> loans = new ArrayList(query.getResultList());
        List<Loan>newLoans=new ArrayList<Loan>();
        for (int i=0;i<loans.size();i++){
            if (loans.get(i).getStatus().equals("inactive"))
                newLoans.add(loans.get(i));
        }
        return newLoans;
} 

@Override
 public List<Loan> staffRejectLoans(Long staffId, Long loanId) throws EmailNotSendException{
     Loan loan=em.find(Loan.class,loanId);
     Staff staff=em.find(Staff.class,staffId);
     loan.setStatus("terminated");
     try{
     sendRejectVerificationEmail(loan.getCustomer().getName(),loan.getCustomer().getEmail(),loan.getAccountNumber());
     } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
     List<Loan> loans=staffViewPendingLoans();
   return loans;
 }
    
@Override
 public List<Loan> staffApproveLoans(Long staffId, Long loanId)throws EmailNotSendException{
    Loan loan=em.find(Loan.class,loanId);
     Staff staff=em.find(Staff.class,staffId);
     loan.setStatus("customerVerified");
      try{
     sendApproveEmail(loan.getCustomer().getName(),loan.getCustomer().getEmail(),loan.getAccountNumber());
     } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
      
     List<Loan> loans=staffViewPendingLoans();
     return loans;
}
 
 private void sendApproveEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Loan Application Approved";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Your loan has been approved by our bank staff.</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Loan Account Number: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to go to our counter to sign the loan contract. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

private void sendRejectVerificationEmail(String name, String email,Long accountNumber) throws MessagingException {
      String subject = "Merlion Bank - Loan Application Rejected";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Sorry! You application of Loan "+ accountNumber+"has been rejected.</h1><br />"
                + "<p style=\"color: #ff0000;\">For more enquiries, please kindly contact +65 8888 8888. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please log in and use case management page and submit your enquiry there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);  
    }

@Override
public BigDecimal calcultateMonthlyPayment(BigDecimal principal, BigDecimal downpayment, Integer loanTerm, Long loanTypeId){
    BigDecimal payment=lsbl.calcultateMonthlyPayment(principal, downpayment, loanTerm, loanTypeId);
    return payment;
}

@Override
    public List<Loan> staffUpdateLoan(Long staffId, Long loanId, BigDecimal downpayment, Integer loanTerm, Date startDate)throws EmailNotSendException{
        Loan loan=em.find(Loan.class,loanId);
        Staff staff=em.find(Staff.class,staffId);
        loan.setDownpayment(downpayment);
        loan.setLoanTerm(loanTerm);
        loan.setStartDate(startDate);
        loan.setStaff(staff);
        loan.setStatus("staffVerified");
        
        
         try{
     sendMofifiedEmail(loan.getCustomer().getName(),loan.getCustomer().getEmail(),loan.getAccountNumber());
     } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
      
     List<Loan> loans=staffViewPendingLoans();
     return loans;
    }
    
    private void sendMofifiedEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Your loan has been modified- Pending verification";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Cour loan has been modified by our bank staff.</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Loan Account Number: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to go to our counter to sign the loan contract. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }
    
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
  
    //Timer
    @Override
      public void closeAccounts(){
         Query query = em.createQuery("SELECT a FROM Loan a");
        List<Loan> currentLoans = new ArrayList(query.getResultList()); 
       List<Loan>  loans=new ArrayList<Loan>();
       
       for (int i=0;i<currentLoans.size();i++){
          if (currentLoans.get(i).getStatus().equals("staffVerified") ||currentLoans.get(i).getStatus().equals("customerVerified"))
              loans.add(currentLoans.get(i));
      }
       Date todayDate=Calendar.getInstance().getTime();
       for (int j=0;j<loans.size();j++){
           if (todayDate.after(loans.get(j).getStartDate())){
               loans.get(j).setStatus("terminated");
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
}
