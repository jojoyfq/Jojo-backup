/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.CreditCard;
import CardEntity.CreditCardApplication;
import CardEntity.CreditCardType;
import CardEntity.CreditChargeback;
import CommonEntity.Customer;
import Exception.ChargebackException;
import Exception.CreditCardException;
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
import java.util.Random;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author Bella
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private static final Random RANDOM = new SecureRandom();
    public static final int SALT_LENGTH = 32;

    @Override
    public List<String> getCreditCardType() {
        List<String> typeList = new ArrayList();
        Query m = em.createQuery("SELECT b FROM CreditCardType b");
        List<CreditCardType> types = m.getResultList();
        for (int i = 0; i < types.size(); i++) {
            typeList.add(types.get(i).getCreditCardType());
        }
        return typeList;
    }

    @Override
    public Customer getCustomer(Long customerID) {
        Customer customer = em.find(Customer.class, customerID);
        return customer;
    }

    @Override
    public void setFileDestination(Long customerId, String fileDestination) {
        Customer customer = em.find(Customer.class, customerId);
        List<String> creditCardFile = customer.getCreditCardFileDestination();
        creditCardFile.add(fileDestination);
        customer.setCreditCardFileDestination(creditCardFile);
        em.flush();
    }

    @Override
    public void newCreditCardApplication(Customer customer, String nationality, String cardType) {
        CreditCardApplication application = new CreditCardApplication(customer.getId(), customer.getName(),
                customer.getIc(), nationality, customer.getCreditCardFileDestination(), cardType, "unverified");
        em.persist(application);
        em.flush();
    }

    @Override
    public List<CreditCardApplication> getPendingCreditCardApplication() {
        Query m = em.createQuery("SELECT b FROM CreditCardApplication b");
        List<CreditCardApplication> applications = m.getResultList();
        List<CreditCardApplication> result = new ArrayList();

        if (applications == null) {
            return null;
        } else {
            for (int i=0; i<applications.size();i++) {
                if(applications.get(i).getStatus().equals("unverified")){
                    result.add(applications.get(i));
                }
            }
            return result;
        }
    }
    
    @Override
    public void approveCreditCardApplication(CreditCardApplication application ) throws ParseException{
        application.setStatus("approved");
        Long customerID = application.getCustomerID();
        String cardType = application.getCreditCardType();
        em.persist(application);
        
        this.createCreditCard(customerID, cardType);      
    }
    
    @Override
    public void rejectCreditCardApplication(CreditCardApplication application){
        application.setStatus("rejected");
        em.persist(application);
    }
    
    @Override
    public CreditCard createCreditCard(Long customerID, String cardType) throws ParseException {

                long cardNumber = generateCardNumber();
                System.out.println("Credit Card Number is: " + cardNumber);
                long cvv = generateCVV();
                System.out.println("Credit Card CVV Number is: " + cvv);

                SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

                Calendar calS = GregorianCalendar.getInstance();
                String startDateS = dt.format(calS.getTime());
                Date startDate;
                startDate = dt.parse(startDateS);

                calS.add(GregorianCalendar.YEAR, 5);
                String expiryDateS = dt.format(calS.getTime());
                Date expiryDate;
                expiryDate = dt.parse(expiryDateS);

                Customer customer = em.find(Customer.class, customerID);
                String cardHolder = customer.getName();

                Query m = em.createQuery("SELECT b FROM CreditCardType b WHERE b.creditCardType = :cardType");
                m.setParameter("cardType", cardType);
                CreditCardType creditCardType = (CreditCardType)m.getSingleResult();

                BigDecimal initialBalance = new BigDecimal("0");
                CreditCard creditCard = new CreditCard(cardNumber,cardHolder,startDate,expiryDate,cvv,creditCardType,customer,initialBalance);
                em.persist(creditCard);
                customer.getCreditCard().add(creditCard);
                em.persist(customer);
                em.flush();

                return creditCard;
    }
    
    private long generateCardNumber() {
        int a = 1;
        Long identifyNum = 656849l;
        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        long cardNumber = identifyNum * 10000000000l + number;

        Query q2 = em.createQuery("SELECT c.cardNumber FROM CreditCard c");
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
    public boolean verifyCreditCard(String cardHolder, Long cardNo, Date expiryDate, Long cvv) throws CreditCardException {
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        String formatExpiryDate = dt.format(expiryDate);
        Query m = em.createQuery("SELECT b FROM CreditCard b WHERE b.cardNumber = :cardNo");
        m.setParameter("cardNo", cardNo);
        List<CreditCard> creditCards = new ArrayList(m.getResultList());
        if (creditCards.isEmpty()) {
            throw new CreditCardException("The Card Number is incorrect");
        } else {
            CreditCard creditCard = creditCards.get(0);
            String formateExpiryDateD = dt.format(creditCard.getExpiryDate());
            if (!creditCard.getCardHolder().equalsIgnoreCase(cardHolder)) {
                throw new CreditCardException("The Card Holder Name is incorrect");
            } else if (!formatExpiryDate.equals(formateExpiryDateD)) {
                throw new CreditCardException("The Expiry Date is incorrect");
            } else if (!creditCard.getCvv().equals(cvv)) {
                throw new CreditCardException("The cvv number is incorrect");
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

        //find the Credit card and set password
        Query m = em.createQuery("SELECT b FROM CreditCard b WHERE b.cardNumber = :cardNo");
        m.setParameter("cardNo", cardNo);
        CreditCard creditCard = (CreditCard)m.getSingleResult();
        creditCard.setPassword(passwordDatabase);
        creditCard.setStatus("active");
        creditCard.setSalt(salt);
        em.flush();
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
    
    @Override
    public List<String> getCreditCardNumbers(Long customerID){
        List<String> creditCardNumbers = new ArrayList();
        String creditString;
        
        Customer customer = em.find(Customer.class, customerID);
        List<CreditCard> creditCard = customer.getCreditCard();
        if(creditCard == null){
            return null;
        }else{
            for(int i=0; i<creditCard.size();i++){
                creditString = creditCard.get(i).getCardNumber()+","+creditCard.get(i).getCreditCardType().getCreditCardType();
                creditCardNumbers.add(creditString);
            }
        }
        return creditCardNumbers;
    }
    
    @Override
    public boolean cancelCreditCard(String cardNo) throws CreditCardException {
        String[] cardString = cardNo.split(",");
        String card = cardString[0];
        Long cardL = Long.parseLong(card);

        Query m = em.createQuery("SELECT b FROM CreditCard b WHERE b.cardNumber = :cardNoL");
        m.setParameter("cardNoL", cardL);
        CreditCard creditCard = (CreditCard) m.getSingleResult();

        if ( creditCard.getCreditCardTransactions() == null) {
            return true;
        }else if(creditCard.getBalance().compareTo(BigDecimal.ZERO) == -1){
              throw new CreditCardException("credit card selected has debt, please make payment to your debt first!");      
                    
        }else if(creditCard.getBalance().compareTo(BigDecimal.ZERO) == 1){
              throw new CreditCardException("credit card selected has prepaid balance, please transfer your amount first!");     
        }else {
            for (int i = 0; i < creditCard.getCreditCardTransactions().size(); i++) {
                if (creditCard.getCreditCardTransactions().get(i).getStatus().equals("pending")) {
                    throw new CreditCardException("credit card selected has pending transaction, cannot be cancelled!");
                }
            }
            creditCard.setStatus("terminated");
            em.flush();
            return true;
        }
    }
    
    @Override
    public CreditCard getCreditCardForClose(String cardNo) {
        Long cardL = Long.parseLong(cardNo.split(",")[0]);

        Query m = em.createQuery("SELECT b FROM CreditCard b WHERE b.cardNumber = :cardNoL");
        m.setParameter("cardNoL", cardL);
        CreditCard creditCard = (CreditCard) m.getSingleResult();

        if (creditCard.getStatus().equals("terminated")) {
            return null;
        } else {
            return creditCard;
        }
    }
    
    @Override
    public void createChargeback(String merchantName, Date transactionDate, BigDecimal transactionAmount, String chargebackDescription, String creditCardNo) throws ChargebackException {
        Long debitCardNoL = Long.parseLong(creditCardNo);
        Query m = em.createQuery("SELECT b FROM CreditCard b WHERE b.cardNumber = :debitCardNoL");
        m.setParameter("debitCardNoL", debitCardNoL);
        CreditCard creditCard = (CreditCard) m.getSingleResult();
        if (creditCard == null) {
            throw new ChargebackException("The Card Number Entered is not valid!");
        } else {
            Date currentTime = Calendar.getInstance().getTime();
            //chargeback has 5 status: staff unverified, VISA/MasterCard unverified, Merchant Bank unverified, rejected, approved
            CreditChargeback chargeback = new CreditChargeback(merchantName, transactionDate, transactionAmount, chargebackDescription, currentTime, "staff unverified", creditCard);
            creditCard.getChargeback().add(chargeback);
            em.persist(chargeback);
            em.persist(creditCard);
            em.flush();
        }
    }
    
    @Override
    public List<CreditChargeback> getPendingCreditChargeback() {
        String status = "staff unverified";
        Query m = em.createQuery("SELECT b FROM CreditChargeback b WHERE b.status = :status");
        m.setParameter("status", status);
        List<CreditChargeback> creditChargeback = m.getResultList();
        return creditChargeback;
    }
    
    @Override
    public void setChargebackStatus(CreditChargeback chargeback, String status) {
        Long cid = chargeback.getId();
        CreditChargeback cChargeback = em.find(CreditChargeback.class, cid);
        cChargeback.setStatus(status);
        em.persist(cChargeback);
        em.flush();
    }
    
}
