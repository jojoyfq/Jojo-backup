/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.DebitCard;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Bella
 */
@Stateless
public class DebitCardSessionBean implements DebitCardSessionBeanLocal{
    @PersistenceContext
    private EntityManager em;
    
//    public DebitCard createDebitCard(Long savingAccountNum ){
//        
//    }
    
//    private long generateCardNumber() {
//        int a = 1;
//        Long identifyNum = 656849l;
//        Random rnd = new Random();
//        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
//        long cardNumber = identifyNum*10000000000l + number;
//        
//        Query q2 = em.createQuery("SELECT c.cardNumber FROM DebitCard c");
//        List<Long> existingCardNum = new ArrayList(q2.getResultList());
//        while (a == 1) {
//
//            if ((existingCardNum.contains(cardNumber)) || (cardNumber / 1000000000000000l == 0)) {
//                number = 100000000 + rnd.nextInt(900000000);
//                accountNumber = Long.valueOf(number);
//                a = 1;
//            } else {
//                a = 0;
//            }
//        }
//
//        return accountNumber;
//    }
   
   
}
