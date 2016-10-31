/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.CreditCard;
import CardEntity.CreditCardApplication;
import CardEntity.CreditCardType;
import CommonEntity.Customer;
import Exception.CreditCardException;
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

                CreditCard creditCard = new CreditCard(cardNumber,cardHolder,startDate,expiryDate,cvv,creditCardType,customer);
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
    
    
}
