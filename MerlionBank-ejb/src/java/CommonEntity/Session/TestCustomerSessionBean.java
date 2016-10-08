/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author ruijia
 */
@Stateless
public class TestCustomerSessionBean implements TestCustomerSessionBeanLocal {

        @PersistenceContext
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
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
        }
        return md5;
    }
    
        @Override
    public void setStatus(Long customerId, BigDecimal balance){
        Customer customer = em.find(Customer.class, customerId);
        customer.setStatus("active");
        customer.getSavingAccounts().get(0).setStatus("active");
        customer.getSavingAccounts().get(0).setAvailableBalance(balance);
        customer.getSavingAccounts().get(0).setBalance(balance);
        customer.getOnlineAccount().setAccountStatus("active");
        em.flush();
    }
    
        @Override
    public void setPassword(Long customerId){
       Customer customer = em.find(Customer.class, customerId);
       String password = "ruijia9336";
       String password1 = this.passwordHash(password+customer.getOnlineAccount().getSalt());
       customer.getOnlineAccount().setPassword(password1);
       em.flush();
        
    }
    
}
