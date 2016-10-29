/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import LoanEntity.Loan;
import Other.Session.sendEmail;
import WealthEntity.DiscretionaryAccount;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
public class WealthTimerSessionBean implements WealthTimerSessionBeanLocal {

   @PersistenceContext
    private EntityManager em;
   
   @Override
   public void closeAccount(){
        Query query = em.createQuery("SELECT a FROM DiscretionaryAccount a");
        List<DiscretionaryAccount> currentAccounts = new ArrayList(query.getResultList()); 
       List<DiscretionaryAccount>  accounts=new ArrayList<DiscretionaryAccount>();
       BigDecimal temp=new BigDecimal(0);
       for (int i=0;i<currentAccounts.size();i++){
          if ((currentAccounts.get(i).getStatus().equals("active") && currentAccounts.get(i).getTotalBalance().compareTo(temp)==0))
             currentAccounts.get(i).setStatus("terminated");
      }
   }
   
   @Override
   public void interestCrediting(){
       Query query = em.createQuery("SELECT a FROM DiscretionaryAccount a");
        List<DiscretionaryAccount> currentAccounts = new ArrayList(query.getResultList()); 
       List<DiscretionaryAccount>  accounts=new ArrayList<DiscretionaryAccount>();
       BigDecimal temp=new BigDecimal(0);
       Date currentTime=Calendar.getInstance().getTime();
       DateTime today=new DateTime(currentTime);
       DateTime accountDate=new DateTime();
       
       for (int i=0;i<currentAccounts.size();i++){
            accountDate=new DateTime(currentAccounts.get(i).getStartDate());
       
          if (currentAccounts.get(i).getStatus().equals("active") && accountDate.getDayOfMonth()==today.getDayOfMonth())
             accounts.add(currentAccounts.get(i));
      }
          for (int j=0;j<accounts.size();j++){
              BigDecimal balance=accounts.get(j).getBalance();
              BigDecimal rate=new BigDecimal(0.002);
              BigDecimal interest=balance.multiply(rate);
              accounts.get(j).setBalance(accounts.get(j).getBalance().add(interest));
              accounts.get(j).setTotalBalance(accounts.get(j).getTotalBalance().add(interest));
              
          }
       }
   
    @Override
   public void commissionFeeCalculation(){
       Query query = em.createQuery("SELECT a FROM DiscretionaryAccount a");
        List<DiscretionaryAccount> currentAccounts = new ArrayList(query.getResultList()); 
       List<DiscretionaryAccount>  accounts=new ArrayList<DiscretionaryAccount>();
       
       BigDecimal fee=new BigDecimal(0);
       
       Date currentTime=Calendar.getInstance().getTime();
       DateTime today=new DateTime(currentTime);
       DateTime accountDate=new DateTime();
       
       for (int i=0;i<currentAccounts.size();i++){
            accountDate=new DateTime(currentAccounts.get(i).getStartDate());
       
          if (currentAccounts.get(i).getStatus().equals("active") && accountDate.getDayOfYear()==today.getDayOfYear())
             accounts.add(currentAccounts.get(i));
      }
       BigDecimal rate1=new BigDecimal(0.015);
       BigDecimal rate2=new BigDecimal(0.011);
       BigDecimal rate3=new BigDecimal(0.010);
       BigDecimal rate4=new BigDecimal(0.006);
       BigDecimal rate5=new BigDecimal(0.0025);
       
       BigDecimal tier1=new BigDecimal(50000);
       BigDecimal tier2=new BigDecimal(500000);
       BigDecimal tier3=new BigDecimal(1000000);
       BigDecimal tier4=new BigDecimal(2000000);
       
       
          for (int j=0;j<accounts.size();j++){
              BigDecimal balance=accounts.get(j).getTotalBalance();
            
              if (balance.compareTo(tier1)==-1 || balance.compareTo(tier1)==0){
                  fee=balance.multiply(rate1);
              }
              
              else if (balance.compareTo(tier1)==1 && (balance.compareTo(tier2)==0 || balance.compareTo(tier2)==-1)){
                  fee=balance.multiply(rate2);
              }
               else if (balance.compareTo(tier2)==1 && (balance.compareTo(tier3)==0 || balance.compareTo(tier3)==-1)){
                  fee=balance.multiply(rate3);
              }
              else if (balance.compareTo(tier3)==1 && (balance.compareTo(tier4)==0 || balance.compareTo(tier4)==-1)){
                  fee=balance.multiply(rate4);
              }
              else if (balance.compareTo(tier4)==1){
                  fee=balance.multiply(rate5);
              }
              
              accounts.get(j).setCommission(fee);  
               try{
     sendCommissionEmail(accounts.get(j).getCustomer().getName(),accounts.get(j).getCustomer().getEmail(),accounts.get(j).getAccountNumber());
     
              } catch (MessagingException ex) {
            System.out.println("Error sending email.");
        }
          }
       }
   
    private void sendCommissionEmail(String name, String email,Long accountNumber) throws MessagingException {
      String subject = "Merlion Bank - Pay Discretionary account annual commission fee";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  You need to pay annual commission fee for your discretionary account, Account number:  "+ accountNumber+".</h1><br />"
                + "<p style=\"color: #ff0000;\">For more enquiries, please kindly contact +65 8888 8888. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please log in and use case management page and submit your enquiry there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);  
    }
   
}
