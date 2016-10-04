/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.DebitCard;
import CardEntity.DebitCardType;
import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import Exception.UserHasDebitCardException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class DebitCardSessionBean implements DebitCardSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private Calendar calS;

    @Override
    public DebitCard createDebitCard(Long savingAccountNum, Long customerID, String cardType) throws UserHasDebitCardException {
        //check whether this saving account has already link with a debit card
        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNum");
        q.setParameter("savingAccountNum", savingAccountNum);
        List<SavingAccount> temp = new ArrayList(q.getResultList());
        SavingAccount savingAccount = temp.get(0);

        if (savingAccount.getDebitCard() != null) {
            throw new UserHasDebitCardException("This saving account is already linked with one Debit Card!");
        } else {

            long cardNumber = generateCardNumber();
            System.out.println("Debit Card Number is: " + cardNumber);
            long cvv = generateCVV();
            System.out.println("CVV Number is: " + cvv);

            calS = GregorianCalendar.getInstance();
            Date startDate = calS.getTime();

            calS.add(GregorianCalendar.YEAR, 5);
            Date expiryDate = calS.getTime();

            System.out.println("Start Date is: " + startDate);
            System.out.println("End Date is: " + expiryDate);

            String cardHolder = em.find(Customer.class, customerID).getName();

            Query m = em.createQuery("SELECT b FROM DebitCardType b WHERE b.debitCardType = :cardType");
            m.setParameter("cardType", cardType);
            List<DebitCardType> debitCardTypes = new ArrayList(m.getResultList());
            DebitCardType debitCardType = debitCardTypes.get(0);

            DebitCard debitCard = new DebitCard(cardNumber, cardHolder, startDate, expiryDate, cvv, "inactive", savingAccount, debitCardType);
            em.persist(debitCard);
            savingAccount.setDebitCard(debitCard);
            em.persist(savingAccount);
            em.flush();

            return debitCard;
        }
    }

    private long generateCardNumber() {
        int a = 1;
        Long identifyNum = 656849l;
        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        long cardNumber = identifyNum * 10000000000l + number;

        Query q2 = em.createQuery("SELECT c.cardNumber FROM DebitCard c");
        List<Long> existingCardNum = new ArrayList(q2.getResultList());
        while (a == 1) {
            if ((existingCardNum.contains(cardNumber)) || (cardNumber / 1000000000000000l == 0)) {
                number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
                cardNumber = identifyNum * 10000000000l + number;
                a = 1;
            } else {
                a = 0;
            }
        }

        return cardNumber;
    }

    private Long generateCVV() {
        Random rnd = new Random();
        int number = 100 + rnd.nextInt(900);
        Long cvv = Long.valueOf(number);

        return cvv;
    }

    @Override
    public List<String> getDebitCardType() {
        Query q = em.createQuery("SELECT a.debitCardType FROM DebitCardType a");
        List<String> debitCardType = q.getResultList();
        return debitCardType;
    }

}
