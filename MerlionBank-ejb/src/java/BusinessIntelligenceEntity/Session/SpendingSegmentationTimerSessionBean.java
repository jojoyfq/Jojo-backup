/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessIntelligenceEntity.Session;

import BusinessIntelligenceEntity.Category;
import BusinessIntelligenceEntity.Coupon;
import BusinessIntelligenceEntity.Interest;
import BusinessIntelligenceEntity.Shop;
import CardEntity.DebitCardTransaction;
import CommonEntity.Customer;
import Exception.EmailNotSendException;
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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author a0113893
 */
@Stateless
public class SpendingSegmentationTimerSessionBean implements SpendingSegmentationTimerSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
//Timer

    public void debitCardSegmentation() {
        Query query = em.createQuery("SELECT a FROM DebitCardTransaction a");
        List<DebitCardTransaction> allTransactions = new ArrayList(query.getResultList());
        List<DebitCardTransaction> transactions = new ArrayList<DebitCardTransaction>();
        
         Query q = em.createQuery("SELECT a FROM Shop a");
            List<Shop> shops = new ArrayList(q.getResultList());

        Date todayDate = Calendar.getInstance().getTime();
        DateTime current2 = new DateTime(todayDate);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM");
        String today = dtf.print(current2);

        for (int i = 0; i < allTransactions.size(); i++) {
            DateTime transacTime = new DateTime(allTransactions.get(i).getTransactionTime());
            String transac = dtf.print(transacTime);
            if (transac.equals(today)) {
                transactions.add(allTransactions.get(i));
            }
        }

        //start segmentation
        for (int i = 0; i < transactions.size(); i++) {
            String description = transactions.get(i).getDescription();
            BigDecimal amount=transactions.get(i).getDebit();

            Customer customer = transactions.get(i).getDebitCard().getSavingAccount().getCustomer();
            List<Interest> interests = new ArrayList<Interest>();

            try {
                interests = customer.getInterests();
            } catch (NullPointerException ex) {
                interests=addInterests(customer);
            }

            //find category
           
            Boolean flag = false;
            Category category = new Category();

            for (int j = 0; j < shops.size(); j++) {
                if (description.equals(shops.get(j).getName())) {
                    category = shops.get(j).getCategory();
                    flag = true;
                }
            }
            
            if (flag==false){
                Shop shop=new Shop(description,"unverified",null);
                em.persist(shop);
                em.flush();
            }else{
                Interest interest=new Interest();
                for (int k=0;k<interests.size();k++){
                   if (interests.get(k).getCategory().equals(category)){
                      interests.get(k).setAmount(interests.get(k).getAmount().add(amount));
                      em.flush();
                   }
                }
            }

        }
    }

    public void creditCardSegmentation() {
        
         Query query = em.createQuery("SELECT a FROM CreditCardTransaction a");
        List<DebitCardTransaction> allTransactions = new ArrayList(query.getResultList());
        List<DebitCardTransaction> transactions = new ArrayList<DebitCardTransaction>();
        
         Query q = em.createQuery("SELECT a FROM Shop a");
            List<Shop> shops = new ArrayList(q.getResultList());

        Date todayDate = Calendar.getInstance().getTime();
        DateTime current2 = new DateTime(todayDate);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM");
        String today = dtf.print(current2);

        for (int i = 0; i < allTransactions.size(); i++) {
            DateTime transacTime = new DateTime(allTransactions.get(i).getTransactionTime());
            String transac = dtf.print(transacTime);
            if (transac.equals(today)&& allTransactions.get(i).getDebit()!=null) {
                transactions.add(allTransactions.get(i));
            }
        }

        //start segmentation
        for (int i = 0; i < transactions.size(); i++) {
            String description = transactions.get(i).getDescription();
            BigDecimal amount=transactions.get(i).getDebit();

            Customer customer = transactions.get(i).getDebitCard().getSavingAccount().getCustomer();
            List<Interest> interests = new ArrayList<Interest>();

            try {
                interests = customer.getInterests();
            } catch (NullPointerException ex) {
                interests=addInterests(customer);
            }

            //find category
           
            Boolean flag = false;
            Category category = new Category();

            for (int j = 0; j < shops.size(); j++) {
                if (description.equals(shops.get(j).getName())) {
                    category = shops.get(j).getCategory();
                    flag = true;
                }
            }
            
            if (flag==false){
                Shop shop=new Shop(description,"unverified",null);
                em.persist(shop);
                em.flush();
            }else{
                Interest interest=new Interest();
                for (int k=0;k<interests.size();k++){
                   if (interests.get(k).getCategory().equals(category)){
                      interests.get(k).setAmount(interests.get(k).getAmount().add(amount));
                      em.flush();
                   }
                }
            }

        }

    }

    private List<Interest> addInterests(Customer customer) {
Query q = em.createQuery("SELECT a FROM Category a");
            List<Category> categories = new ArrayList(q.getResultList());
            BigDecimal temp=new BigDecimal(0);
            List<Interest> interests=new ArrayList<Interest>();
            
            for (int i=0;i<categories.size();i++){
                Category category=categories.get(i);
                Interest interest=new Interest(temp,category,customer);
                em.persist(interest);
                em.flush();
                interests.add(interest);
            }
            
            customer.setInterests(interests);
            em.flush();
            return customer.getInterests();
    }
    
    public void sendBirthdayCoupon() {
         Query query = em.createQuery("SELECT a FROM Customer a");
        List<Customer> allCustomers = new ArrayList(query.getResultList());
        List<Customer> customers = new ArrayList<Customer>();
        
         Date todayDate = Calendar.getInstance().getTime();
        DateTime current2 = new DateTime(todayDate);

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-DD");
        String today = dtf.print(current2);
        
        for (int i=0;i<allCustomers.size();i++){
            DateTime birthday=new DateTime(allCustomers.get(i).getDateOfBirth());
            String customerBirthday=dtf.print(birthday);
        
            if (allCustomers.get(i).getStatus().equals("active") && allCustomers.get(i).getStatus().equals("locked")&& customerBirthday.equals(today) )
                customers.add(allCustomers.get(i));
                        }
        
        for (int j=0;j<customers.size();j++){
            Customer customer=customers.get(j);
            Interest interest=identifyInterest(customer);
            Category category=interest.getCategory();
            
            List<Coupon> coupons=category.getCoupons();
            List<Coupon> validCoupons=new ArrayList<Coupon>();
            
            for (int k=0;k<coupons.size();k++){
                if (coupons.get(k).getStatus().equals("active"))
                    validCoupons.add(coupons.get(k));
                
            }
            
            try {
            SendBirthdayEmail(customer.getName(), validCoupons, customer.getEmail());

        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            
        }

        }
        
        }
    
    private Interest identifyInterest(Customer customer){
        List<Interest> interests=customer.getInterests();
        BigDecimal maxAmount=interests.get(0).getAmount();
        Interest interest=interests.get(0);
        
        for (int i=0;i<interests.size();i++){
            if (interests.get(i).getAmount().compareTo(maxAmount)==1){
                maxAmount=interests.get(i).getAmount();
                interest=interests.get(i);
            }           
            }
         return interest;   
        }
    
    private void SendBirthdayEmail(String name, List<Coupon> coupons, String email) throws MessagingException {
        String subject = "Merlion Bank - Happy Birthday";
        System.out.println("Inside send SavingAccount Activation email");
        
        String couponContent="";
        
        for (int i=0;i<coupons.size();i++){
            couponContent=couponContent
                    +"<br />***************************************************************</h2><br />"
                    +"<br />Shop Name: " + coupons.get(i).getShopName() + "</h2><br />"
                    + "<br />Coupon Description: " + coupons.get(i).getDescription()+ "</h2><br />"
                    +"<br />Coupon Code: " + coupons.get(i).getCouponCode()+ "</h2><br />";
        }
        

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Happy Birthday! Here are your birthday gifts from our partners. </h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + couponContent
                + "<p style=\"color: #ff0000;\">Just simply pay by Merlion Bank credit card/debit card, you can enjoy these coupons!Coupons will only be valid for your birthday month!</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>MerLION Platform User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }
        
    }

