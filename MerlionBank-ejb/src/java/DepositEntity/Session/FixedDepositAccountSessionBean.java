/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import DepositEntity.FixedDepositAccount;
import DepositEntity.SavingAccount;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    private BigDecimal savingBalance;
    private Long FixedDepositaccountNumber;
    private BigDecimal fixedAccountBalance;
    private String fixedDuration;

//interestRate table
    private Double interestRate3 = 0.0015;
    private Double interestRate6 = 0.0020;
    private Double interestRate12 = 0.0035;
    private Double interestRate24 = 0.01;

    public Boolean createFixedAccount(String ic, BigDecimal amount, Date dateOfStart,
            Date dateOfEnd, String duration,
            Double interest) {

        //check balance
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> customers = q.getResultList();
        Customer customer = customers.get(0);
        
        
         if(savingBalance.compareTo(amount) == -1){
            
         return false;
         }
         else{  
         

        
        account = new FixedDepositAccount(amount, dateOfStart,
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
}


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
