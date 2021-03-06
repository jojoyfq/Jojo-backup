/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.CardTransaction;
import CardEntity.DebitCard;
import CardEntity.DebitCardType;
import CardEntity.DebitChargeback;
import CommonEntity.Customer;
import static CommonEntity.Session.AccountManagementSessionBean.SALT_LENGTH;
import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
import Exception.ChargebackException;
import Exception.DebitCardException;
import Exception.NoTransactionRecordFoundException;
import Exception.UserHasDebitCardException;
import java.math.BigDecimal;
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
    public boolean checkDebitCardBalance(String cardNo, String cvv, String cardHolder, String amount) {

        BigDecimal posAmount = new BigDecimal(amount);
        Long cardNoL = Long.parseLong(cardNo);
        Long cvvl = Long.parseLong(cvv);

        Query m = em.createQuery("SELECT b FROM DebitCard b WHERE b.cardNumber = :cardNoL");
        m.setParameter("cardNoL", cardNoL);
        DebitCard debitCard = (DebitCard) m.getSingleResult();

        if (debitCard == null) {
            return false;
        } else {
            if (!debitCard.getCardHolder().equalsIgnoreCase(cardHolder)) {
                System.out.print("card Holder name is incorrect!");
                return false;
            } else if (!debitCard.getCvv().equals(cvvl)) {
                System.out.print("The cvv number is incorrect");
                return false;
            } else if (amount != null) {
                if (debitCard.getSavingAccount().getAvailableBalance().compareTo(posAmount) == 1) {
                    //update user's balance
                    BigDecimal updatedAvailable = debitCard.getSavingAccount().getAvailableBalance().subtract(posAmount);
                    BigDecimal updatedBalance = debitCard.getSavingAccount().getBalance().subtract(posAmount);
                    debitCard.getSavingAccount().setAvailableBalance(updatedAvailable);
                    debitCard.getSavingAccount().setBalance(updatedBalance);

                    Date currentTime = Calendar.getInstance().getTime();
                    java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

                    TransactionRecord transactionRecord = new TransactionRecord("POS", posAmount, null, "settled", cardNo + ", Cold Storage", currentTimestamp, debitCard.getSavingAccount().getAccountNumber(), null, debitCard.getSavingAccount(), "MerlionBank", null);
                    debitCard.getSavingAccount().getTransactionRecord().add(transactionRecord);
                    em.persist(transactionRecord);
                    em.flush();
                    return true;
                } else {
                    System.out.print("Not enough balance");
                    return false;
                }
            } else {
                System.out.print("Some thing bad happen!");
                return false;
            }
        }
    }

    @Override
    public void createChargeback(String merchantName, Date transactionDate, BigDecimal transactionAmount, String chargebackDescription, String debitCardNo) throws ChargebackException {
        Long debitCardNoL = Long.parseLong(debitCardNo);
        Query m = em.createQuery("SELECT b FROM DebitCard b WHERE b.cardNumber = :debitCardNoL");
        m.setParameter("debitCardNoL", debitCardNoL);
        DebitCard debitCard = (DebitCard) m.getSingleResult();
        if (debitCard == null) {
            throw new ChargebackException("The Card Number Entered is not valid!");
        } else {
            Date currentTime = Calendar.getInstance().getTime();
            //chargeback has 5 status: staff unverified, VISA/MasterCard unverified, Merchant Bank unverified, rejected, approved
            DebitChargeback chargeback = new DebitChargeback(merchantName,transactionDate,transactionAmount,chargebackDescription,currentTime,"staff unverified",debitCard);
            debitCard.getChargeback().add(chargeback);
            em.persist(chargeback);
            em.persist(debitCard);
            em.flush();
        }
    }
    
    @Override
    public List<DebitChargeback> getPendingDebitChargeback(){
        String status = "staff unverified";
        Query m = em.createQuery("SELECT b FROM DebitChargeback b WHERE b.status = :status");
        m.setParameter("status", status);
        List<DebitChargeback> debitChargeback = m.getResultList();
        return debitChargeback;
    }
    
    @Override
    public void setChargebackStatus(DebitChargeback chargeback, String status){
        Long cid = chargeback.getId();
        DebitChargeback dChargeback = em.find(DebitChargeback.class, cid);
        dChargeback.setStatus(status);
        em.persist(dChargeback);
        em.flush();
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
    public List<DebitCard> getDebitCard(Long customerID) throws DebitCardException {
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

    @Override
    public List<CardTransaction> getEStatement(Long customerID, Long debitCardNo, Date currentTime) throws NoTransactionRecordFoundException {
        List<CardTransaction> cardTransactionFiltered = new ArrayList<>();
        String pattern = "MM-YYYY";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String currentTimeFormated = simpleDateFormat.format(currentTime);
        System.out.println(currentTimeFormated);
        String transactionTime;
        //Search For Customer Email
        Customer customer = em.find(Customer.class, customerID);
        String email = customer.getEmail();

        Query m = em.createQuery("SELECT b FROM CardTransaction b WHERE b.cardNumber = :debitCardNo");
        m.setParameter("debitCardNo", debitCardNo);
        List<CardTransaction> cardTransactions = new ArrayList(m.getResultList());
        if (cardTransactions.isEmpty()) {
            throw new NoTransactionRecordFoundException("No Transaction Record Found!");
        } else {
            //search for card transactions that match the current month
            for (int i = 0; i < cardTransactions.size(); i++) {
                transactionTime = simpleDateFormat.format(cardTransactions.get(i).getTransactionTime());
                if (transactionTime.equals(currentTimeFormated)) {
                    cardTransactionFiltered.add(cardTransactions.get(i));
                }
            }
            return cardTransactionFiltered;
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
