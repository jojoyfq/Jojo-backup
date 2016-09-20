/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import DepositEntity.FixedDepositAccount;
import static DepositEntity.FixedDepositAccount_.customer;
import DepositEntity.FixedDepositRate;
import DepositEntity.SavingAccount;
import java.math.BigDecimal;
import java.math.MathContext;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Calendar;
/**
 *
 * @author shuyunhuang
 *
 */
@Stateless
public class FixedDepositAccountSessionBean implements FixedDepositAccountSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    FixedDepositAccount account; 
    private BigDecimal savingBalance; //from manage bean
    private Long accountNumber; //generate 
    private BigDecimal fixedAccountBalance; //from managed bean
    private String fixedDuration;//from managed bean
    //private BigDecimal amount; // amount the account should have 
    
    private static final Random RANDOM = new SecureRandom();
    private FixedDepositRate rate;
    
    private Double interestRate;
    private Long rateIdLong;
    private Integer rateIdInt;
    private Customer customer;
    
    public Long createFixedAccount(Long customerId, BigDecimal amount, Date dateOfStart, Date dateOfEnd, String duration){
         
         customer = em.find(Customer.class, customerId);
         System.out.println("customer IC"+customer.getIc());
        //generate and check account number
        accountNumber = Math.round(Math.random() * 99999999);
        int a = 1;
        while(a==1) {
           Query q2 = em.createQuery("SELECT c.accountNumber FROM FixedDepositAccount c");
           List<Long> existingAcctNum = new ArrayList(q2.getResultList());
 
           if(!existingAcctNum.isEmpty()){
           if(existingAcctNum.contains(accountNumber))
               accountNumber = Math.round(Math.random() * 999999999);
           else
               a =0; }
           a=0;
        }
        System.out.println(" accountNumber"+accountNumber);
        
        BigDecimal balance = new BigDecimal("0.0000"); //initial balance 
        //find interest rate according to duration
       
        if(duration.equalsIgnoreCase("3")){
            rateIdInt = 1;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class,rateIdLong);}
        
        else if(duration.equalsIgnoreCase("6")){
                 rateIdInt = 2;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class,rateIdLong);}
        
        else if(duration.equalsIgnoreCase("12")){
            rateIdInt = 3;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class,rateIdLong);}       
        else{
            rateIdInt = 4;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class,rateIdLong);}
        
        interestRate = rate.getInterestRate();
        account = new FixedDepositAccount(accountNumber,amount,balance, dateOfStart, dateOfEnd, duration, "inactive", interestRate);
        em.persist(account);
        account.setCustomer(customer);
         System.out.println(" accountid"+account.getId());
        //customer may alr have fixed acct
        List<FixedDepositAccount> fixedAccounts = new ArrayList<FixedDepositAccount>();
        fixedAccounts.add(account);
        customer.setFixedDepositeAccounts(fixedAccounts);
        
        em.persist(customer);
        em.flush();
        System.out.println(" customer"+customer.getIc());
        //log an action
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Create a new Fixed deposit account", customer);
        em.persist(customer);
        List<CustomerAction> customerActions=new ArrayList<CustomerAction>();
        customerActions.add(0,action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();
        
        System.out.println("Fixed Deposit account created successfullly");
        return accountNumber;
    }

//    @Override
//    public Boolean createFixedAccount(String ic, BigDecimal amount, Date dateOfStart, Date dateOfEnd, String duration, String status, Double interest) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    public Long createFixedAccount(String ic, BigDecimal amount, Date dateOfStart, Date dateOfEnd, String duration) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    }
         
        
        
        /*account = new FixedDepositAccount(amount, dateOfStart,
                dateOfEnd, duration, interest);
        account.setCustomer(customer);
        List<FixedDepositAccount> fixedAccounts = new ArrayList<FixedDepositAccount>();
        fixedAccounts.add(account);
        customer.setFixedDepositeAccounts(fixedAccounts);

        //auto generate account number
        em.persist(account);

        return true;
    }
    }
}*/


/*
 @Override
 public ArrayList<String> displayLastAccount(String IC){
 Query q = em.createQuery("SELECT a FROM Customer a WHERE a.IC = :IC");
 q.setParameter("IC",IC );
 List<Customer> customers = q.getResultList();
 Customer customer = customers.get(0);
        
 ArrayList<FixedDepositAccount> accounts = customer.getFixedDepositeAccounts();
 int size = accounts.size();
 account = accounts.get(size-1);
        
 Date endDate = account.getEndDate();
 BigDecimal amt = account.getAmount();
 Long accountNumber = account.getAccountNumber();
 DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
 String dateTemp = df.format(endDate);
 String acctNum =  accountNumber.toString();
 String amtTemp = amt.toString();
        
 ArrayList<String> result = null;
 result.add(1,dateTemp);
 result.add(0,acctNum);
 result.add(2,amtTemp);
 return result;
        
 }
    
 /*   public ArrayList<ArrayList<String>> displayAllAccount(String IC){
 Query q = em.createQuery("SELECT a FROM Customer a WHERE a.IC = :IC");
 q.setParameter("IC",IC );
 List<Customer> customers = q.getResultList();
 Customer customer = customers.get(0);
        
 ArrayList<FixedDepositAccount> accounts = customer.getFixedDepositeAccounts();
 int size = accounts.size();
 account = accounts.get(size-1);
        
 Date endDate = account.getEndDate();
 BigDecimal amt = account.getAmount();
 Long accountNumber = account.getAccountNumber();
 DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
 String dateTemp = df.format(endDate);
 String acctNum =  accountNumber.toString();
 String amtTemp = amt.toString();
        
 ArrayList<String> result = null;
 result.add(1,dateTemp);
 result.add(0,acctNum);
 result.add(2,amtTemp);
 return result;
        
 }
 */
    //trasnfer the principal amount with interest to saving account
//calculate interest 
/*
 @Override
 public BigDecimal normalWithdraw(String IC, Long FixedDepositAccountNumber){
 Query q = em.createQuery("SELECT a FROM Customer a WHERE a.IC = IC");
 q.setParameter("IC",IC );
 List<Customer> customers = q.getResultList();
 Customer customer = customers.get(0);
        
 ArrayList<FixedDepositAccount> accounts = customer.getFixedDepositeAccounts();
 int size = accounts.size();
 account = accounts.get(size-1);
 fixedAccountBalance = account.getAmount(); //principal amt
        
 //rate is parsed to bigdecimal
 fixedDuration = account.getDuration();
 BigDecimal totalInterest;
        
 if(fixedDuration.equals("3")){
 totalInterest = fixedAccountBalance.multiply(new BigDecimal (interestRate3/12));     
 }
 else if(fixedDuration.equals("6")){
 totalInterest = fixedAccountBalance.multiply(new BigDecimal (interestRate6/12));
 }
 else if(fixedDuration.equals("12")){
 totalInterest = fixedAccountBalance.multiply(new BigDecimal (interestRate12/12));
 }
        
 else {
 totalInterest = fixedAccountBalance.multiply(new BigDecimal (interestRate24/12));
 }
        
 BigDecimal totalAmount; 
 totalAmount = fixedAccountBalance.add(totalInterest);   
        
 return totalInterest; 
 }
   
 //set the amount in saving account
 @Override
 public void earlyWithdraw(String IC, String fixedDepositAccount){
 Query q = em.createQuery("SELECT a FROM Customer a WHERE a.IC = IC");
 q.setParameter("IC",IC);
 List<Customer> customers = q.getResultList();
 Customer customer = customers.get(0);
        
 ArrayList<FixedDepositAccount> accounts = customer.getFixedDepositeAccounts();
 int size = accounts.size();
 account = accounts.get(size-1);
 }

 */
/*
 //change account status to "closed"
 @Override
 public void closeFixedDepositAccount(String IC){
        
 }
    
 // Add business logic below. (Right-click in editor and choose
 // "Insert Code > Add Business Method")

 public void createFixedAccount(BigDecimal amount, Date dateOfStart, String duration) {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }


 @Override
 public void normalWithdraw(String fixedDepositeAccount) {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }
 */
