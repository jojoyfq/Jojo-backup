/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessIntelligenceEntity.Session;

import BusinessIntelligenceEntity.Category;
import BusinessIntelligenceEntity.Interest;
import BusinessIntelligenceEntity.Shop;
import CardEntity.DebitCardTransaction;
import CommonEntity.Customer;
import WealthEntity.DiscretionaryAccount;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
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
}
