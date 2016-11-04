/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import LoanEntity.Loan;
import Other.Session.sendEmail;
import WealthEntity.DiscretionaryAccount;
import WealthEntity.Portfolio;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
       System.out.println("********** inside the interest crediting method");
       Query query = em.createQuery("SELECT a FROM DiscretionaryAccount a");
        List<DiscretionaryAccount> currentAccounts = new ArrayList(query.getResultList()); 
       List<DiscretionaryAccount>  accounts=new ArrayList<DiscretionaryAccount>();
       BigDecimal temp=new BigDecimal(0);
       Date currentTime=Calendar.getInstance().getTime();
       DateTime today=new DateTime(currentTime);
       DateTime accountDate=new DateTime();
       
       System.out.println("********** size of current accont" + currentAccounts.size());
       System.out.println("********** today is" + today.getDayOfMonth());
       System.out.println("********** account start day is" + accountDate.getDayOfMonth());
       for (int i=0;i<currentAccounts.size();i++){
           System.out.println("inside the for loop");
            accountDate=new DateTime(currentAccounts.get(i).getStartDate());
//            System.out.println("********** status of the account" + currentAccounts.get(i).getStatus());
          if (currentAccounts.get(i).getStatus().equals("active") && accountDate.getDayOfMonth()==today.getDayOfMonth())
             accounts.add(currentAccounts.get(i));
             System.out.println("********** current account number" + currentAccounts.get(i).getAccountNumber());
      }
          for (int j=0;j<accounts.size();j++){
              BigDecimal balance=accounts.get(j).getBalance();
              BigDecimal rate=new BigDecimal(0.002);
              BigDecimal month=new BigDecimal(12);
              BigDecimal interest=balance.multiply(rate).divide(month,MathContext.DECIMAL128);
              interest.setScale(4, RoundingMode.HALF_UP);
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
    
    public void checkStaffViolation(){
        Query query = em.createQuery("SELECT a FROM Portfolio a");
        List<Portfolio> portfolios = new ArrayList(query.getResultList()); 
       List<Portfolio>  selected=new ArrayList<Portfolio>();
       
       BigDecimal temp=new BigDecimal(0);
       
       Date currentTime=Calendar.getInstance().getTime();
       DateTime today=new DateTime(currentTime);
           DateTime compareDate = today.minusMonths(1);
           DateTime accountDate=new DateTime();
       
       for (int i=0;i<portfolios.size();i++){
           int size=portfolios.get(i).getPortfolioTransactions().size();
            accountDate=new DateTime(portfolios.get(i).getPortfolioTransactions().get(size-1));
       
          if (portfolios.get(i).getStatus().equals("active") && compareDate.isAfter(accountDate) && portfolios.get(i).getType().equals("Tailored plan")){
              try{
            sendViolationEmail(portfolios.get(i).getStaff().getStaffName(),portfolios.get(i).getStaff().getStaffEmail(),portfolios.get(i).getId()); 
     
          }catch (MessagingException ex) {
            System.out.println("Error sending email.");
        }
          }
        
    }
    }
    
    private void sendViolationEmail(String name, String email,Long accountNumber) throws MessagingException {
      String subject = "Merlion Bank - No portfolio transaction within one month";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  The portfolio has no transaction within one month. Portfolio id:  "+ accountNumber+".</h1><br />"
                + "<p style=\"color: #ff0000;\">For more enquiries, please kindly contact +65 8888 8888. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please look for merlion bank internal support.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank Staff Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);  
    }
   
    @Override
    public void closePortfolio(){
         Query query = em.createQuery("SELECT a FROM Portfolio a");
        List<Portfolio> portfolios = new ArrayList(query.getResultList()); 
       List<Portfolio>  selected=new ArrayList<Portfolio>();
       
       Date currentTime=Calendar.getInstance().getTime();
        for (int i=0;i<portfolios.size();i++){
        if (portfolios.get(i).getStatus().equals("active") && currentTime.after(portfolios.get(i).getEndDate()) ){
            Portfolio portfolio=portfolios.get(i);
            DiscretionaryAccount discretionaryAccount=portfolio.getDiscretionaryAccount();
            portfolio.setStatus("completed");
            discretionaryAccount.setBalance(discretionaryAccount.getBalance().add(portfolio.getPresentValue()));
        }
        
    }
        }
    
     @Override
     public void updateDiscretionaryAccountInterestRate(){
       Query query = em.createQuery("SELECT a FROM DiscretionaryAccount a");
        List<DiscretionaryAccount> currentAccounts = new ArrayList(query.getResultList()); 
       List<DiscretionaryAccount>  accounts=new ArrayList<DiscretionaryAccount>();
       BigDecimal temp=new BigDecimal(0);
       BigDecimal cutline=new BigDecimal(250000);
       BigDecimal interestRate=new BigDecimal(0.024);
       
       
       for (int i=0;i<currentAccounts.size();i++){
       
          if (currentAccounts.get(i).getStatus().equals("active"))
             accounts.add(currentAccounts.get(i));
      }
       
       for (int j=0;j<accounts.size();j++){
           temp=accounts.get(j).getTotalBalance();
           if (temp.compareTo(cutline)==1)
               accounts.get(j).setAccumDailyInterest(interestRate);
       }
         
     }
    
    @Override
    public void preDefinedPlanInterestCrediting(){
        System.out.println("inside the preDefinedPlanInterestCrediting method");
         Query query = em.createQuery("SELECT a FROM Portfolio a");
        List<Portfolio> portfolios = new ArrayList(query.getResultList()); 
       List<Portfolio>  selected=new ArrayList<Portfolio>();
       
       BigDecimal temp=new BigDecimal(0);
       
       Date currentTime=Calendar.getInstance().getTime();
       DateTime today=new DateTime(currentTime);
       BigDecimal one=new BigDecimal(1);
//           DateTime compareDate = today.minusMonths(1);
//           DateTime accountDate=new DateTime();
       
       for (int i=0;i<portfolios.size();i++){
          
          if (portfolios.get(i).getStatus().equals("active") && (portfolios.get(i).getType().equals("Retirement planning")||portfolios.get(i).getType().equals("Education planning"))){
            Date start=portfolios.get(i).getStartDate();
            DateTime startDate=new DateTime(start);
            Portfolio portfolio=portfolios.get(i);
              if (today.getDayOfMonth()==startDate.getDayOfMonth()){
                  BigDecimal month=new BigDecimal(12);
                  
        BigDecimal trueRate = new BigDecimal(portfolio.getMonthlyInterestRate());
        trueRate=trueRate.divide(month,MathContext.DECIMAL128);
        trueRate.setScale(4, RoundingMode.HALF_UP);
        portfolio.getDiscretionaryAccount().setTotalBalance( portfolio.getDiscretionaryAccount().getTotalBalance().subtract(portfolio.getPresentValue()));
        portfolio.setPresentValue(portfolio.getPresentValue().multiply(one.add(trueRate)));
        em.flush();
        portfolio.getDiscretionaryAccount().setTotalBalance( portfolio.getDiscretionaryAccount().getTotalBalance().add(portfolio.getPresentValue()));
        em.flush();
                           
          }
       }
          }
        
    }
    
   
    
}
