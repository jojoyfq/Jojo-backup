/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PayMeEntity.Session;

import static CardEntity.Session.DebitCardSessionBean.SALT_LENGTH;
import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CommonEntity.OnlineAccount;
import DepositEntity.SavingAccount;
import DepositEntity.Session.TransferSessionBeanLocal;
import Exception.PasswordNotMatchException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import PayMeEntity.PayMe;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
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
public class PayMeSessionBean implements PayMeSessionBeanLocal{

   @PersistenceContext
    private EntityManager em;
    private static final Random RANDOM = new SecureRandom();
    public static final int SALT_LENGTH = 32;
   
   @Override
   public boolean checkLogin(String ic, String password) throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (temp.isEmpty()) {
            System.out.println("Username " + ic + " does not exist!");
            throw new UserNotExistException("Username " + ic + " does not exist, please try again");
        }

        int size = temp.size();
        Customer customer = temp.get(size - 1);
        if (customer.getStatus().equals("terminated")) {
            System.out.println("Username " + ic + " does not exist!");
            throw new UserNotExistException("Username " + ic + " does not exist, please try again");
        } else if (customer.getStatus().equals("inactive")) {
            System.out.println("Username " + ic + " please activate your account!");

            throw new UserNotActivatedException("Username " + ic + " please activate your account!");

        } else if (customer.getOnlineAccount().getAccountStatus().equals("locked")) {
            System.out.println("Username " + ic + " Account locked! Please Reset Password!");
            throw new UserNotExistException("Username " + ic + " Account locked! Please Reset Password!");

        } else if (customer.getStatus().equals("unverified")) {
            System.out.println("Username " + ic + "Please wait for your account to be verified!");
            throw new UserNotExistException("Username " + ic + "Please wait for your account to be verified!");

        } else {
            System.out.println("Username " + ic + " IC check pass!");
        }

        if (!passwordHash(password + customer.getOnlineAccount().getSalt()).equals(customer.getOnlineAccount().getPassword())) {

//            Long i = Long.parseLong("1");
            return false;
        }
          return true;
//        return customer.getId();
    }
   
   @Override
   public PayMe createPayMe(Long customerID, String savingAccountNo, String paymePassword){
       //get Customer Entity
       Customer customer = em.find(Customer.class, customerID);
       Long savingAccount = Long.valueOf(savingAccountNo);
       //get Saving Account Entity
       Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccount");
        q.setParameter("savingAccount", savingAccount);
        List<SavingAccount> temp = new ArrayList(q.getResultList());
        SavingAccount savingAccountL = temp.get(0);
       
       //password salt and hash
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789@#$%^&*!?+-";
        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }
        String passwordDatabase = passwordHash(paymePassword + salt);
        System.out.println("PayMePassword after hash&salt:" + passwordDatabase);
        //set initial balance to 0
        BigDecimal balance = new BigDecimal("0.00");
        
        //Create New PayMe Account
        PayMe payMe = new PayMe(customer, savingAccountL, balance, passwordDatabase);
        em.persist(payMe);
        customer.setPayMe(payMe);
        em.persist(customer);
        savingAccountL.setPayme(payMe);
        em.persist(savingAccountL);
        em.flush();
        
        return payMe;              
   }
   
   @Override
    public List<String> getSavingAccountString(Long customerID) throws UserHasNoSavingAccountException{
        List<String> savingAccountString = new ArrayList<String>();
        String savingAccountNo;
        String accountType;
        
        Customer customer = em.find(Customer.class, customerID);
        List<SavingAccount> savingAccounts = customer.getSavingAccounts();
        if (savingAccounts.isEmpty()) {
            throw new UserHasNoSavingAccountException("No Saving Account Found!");
        } else {
            for (int i = 0; i < savingAccounts.size(); i++) {
                    savingAccountNo = Long.toString(savingAccounts.get(i).getAccountNumber());
                    accountType = savingAccounts.get(i).getSavingAccountType().getAccountType();
                    savingAccountString.set(i,savingAccountNo + "," + accountType);              
            }
            return savingAccountString;
        }
    }
    
    @Override
    public String getPhoneNumber(Long customerID){
        Customer customer = em.find(Customer.class, customerID);
        return customer.getPhoneNumber();
    }
    
    @Override
    public String getBalance(Long customerID){
        Customer customer = em.find(Customer.class, customerID);
        return customer.getPayMe().getBalance().toString();
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
}
