/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.DebitCard;
import CardEntity.DebitCardType;
import CommonEntity.Customer;
import static CommonEntity.Session.AccountManagementSessionBean.SALT_LENGTH;
import DepositEntity.SavingAccount;
import Exception.DebitCardException;
import Exception.UserHasDebitCardException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static final Random RANDOM = new SecureRandom();
    public static final int SALT_LENGTH = 32;

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

            try {

                long cardNumber = generateCardNumber();
                System.out.println("Debit Card Number is: " + cardNumber);
                long cvv = generateCVV();
                System.out.println("CVV Number is: " + cvv);

                SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

                calS = GregorianCalendar.getInstance();
                String startDateS = dt.format(calS.getTime());
                Date startDate;
                startDate = dt.parse(startDateS);

                calS.add(GregorianCalendar.YEAR, 5);
                String expiryDateS = dt.format(calS.getTime());
                Date expiryDate;
                expiryDate = dt.parse(expiryDateS);

                String cardHolder = em.find(Customer.class, customerID).getName();

                Query m = em.createQuery("SELECT b FROM DebitCardType b WHERE b.debitCardType = :cardType");
                m.setParameter("cardType", cardType);
                List<DebitCardType> debitCardTypes = new ArrayList(m.getResultList());
                DebitCardType debitCardType = debitCardTypes.get(0);

                DebitCard debitCard = new DebitCard(cardNumber, cardHolder, startDate, expiryDate, cvv, "inactive", savingAccount, debitCardType, "", "");
                em.persist(debitCard);
                savingAccount.setDebitCard(debitCard);
                em.persist(savingAccount);
                em.flush();

                return debitCard;
            } catch (Exception e) {
                System.out.print("debit card creation encounter error!");
                return null;
            }
        }
    }

    @Override
    public boolean verifyDebitCard(String cardHolder, Long cardNo, Date expiryDate, Long cvv) throws DebitCardException {
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        String formatExpiryDate = dt.format(expiryDate);
        Query m = em.createQuery("SELECT b FROM DebitCard b WHERE b.cardNumber = :cardNo");
        m.setParameter("cardNo", cardNo);
        List<DebitCard> debitCards = new ArrayList(m.getResultList());
        if (debitCards.isEmpty()) {
            throw new DebitCardException("The Card Number is incorrect");
        } else {
            DebitCard debitCard = debitCards.get(0);
            String formateExpiryDateD = dt.format(debitCard.getExpiryDate());
            if (!debitCard.getCardHolder().equalsIgnoreCase(cardHolder)) {
                throw new DebitCardException("The Card Holder Name is incorrect");
            } else if (!formatExpiryDate.equals(formateExpiryDateD)) {
                throw new DebitCardException("The Expiry Date is incorrect");
            } else if (!debitCard.getCvv().equals(cvv)) {
                throw new DebitCardException("The cvv number is incorrect");
            } else {
                return true;
            }
        }
    }

    @Override
    public void setPassword(Long cardNo, String password) {
        //password salt and hash
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789@#$%^&*!?+-";
        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }
        String passwordDatabase = passwordHash(password + salt);
        System.out.println("Password after hash&salt:" + passwordDatabase);

        //find the debit card and set password
        Query m = em.createQuery("SELECT b FROM DebitCard b WHERE b.cardNumber = :cardNo");
        m.setParameter("cardNo", cardNo);
        List<DebitCard> debitCards = new ArrayList(m.getResultList());
        DebitCard debitCard = debitCards.get(0);
        debitCard.setPassword(passwordDatabase);
        debitCard.setStatus("active");
        debitCard.setSalt(salt);
        em.flush();
    }

    @Override
    public List<String> getDebitCardString(Long customerID) throws DebitCardException {
        List<String> debitCardString = new ArrayList<String>();
        String cardNo;
        String cardType;

        Customer customer = em.find(Customer.class, customerID);
        List<SavingAccount> savingAccounts = customer.getSavingAccounts();
        if (savingAccounts.isEmpty()) {
            throw new DebitCardException("No Debit Card Record Found!");
        } else {
            for (int i = 0; i < savingAccounts.size(); i++) {
                if (savingAccounts.get(i).getDebitCard() != null) {
                    cardNo = Long.toString(savingAccounts.get(i).getDebitCard().getCardNumber());
                    cardType = savingAccounts.get(i).getDebitCard().getDebitCardType().getDebitCardType();
                    debitCardString.add(cardNo + "," + cardType);
                }
            }
            return debitCardString;
        }
    }

    @Override
    public List<DebitCard> getDebitCard(Long customerID)throws DebitCardException {
        List<DebitCard> debitCard = new ArrayList();
        Customer customer = em.find(Customer.class, customerID);
        if (customer.getSavingAccounts().isEmpty()) {
            throw new DebitCardException("No Debit Card Record Found!");
        } else {
            for (int i = 0; i < customer.getSavingAccounts().size(); i++) {
                debitCard.add(customer.getSavingAccounts().get(i).getDebitCard());
            }
            return debitCard;
        }
    }

    private String passwordHash(String pass) {
        String md5 = null;

        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(pass.getBytes(), 0, pass.length());

            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
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
