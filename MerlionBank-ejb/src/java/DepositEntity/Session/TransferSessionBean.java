/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import DepositEntity.Payee;
import DepositEntity.SavingAccount;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Bella
 */
@Stateless
public class TransferSessionBean implements TransferSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    SavingAccount giverBankAccount;
    SavingAccount receipientBankAccount;
    private String giverBankName;
    private String receipientBankName;
    private Long giverBankAccountNum;
    private Long receipientBankAccountNum;
    private Date transferTime;
    private BigDecimal transferAmount;
    private Long payeeAccount;
    private String payeeName;
    private Long customerID;

    @Override
    public Boolean intraOneTimeTransferCheck(Long giverBankAccountNum, Long recipientBankAccountNum, BigDecimal transferAmount) {
        BigDecimal giverBalance;
        BigDecimal recipientBalance;
        BigDecimal updatedGiverBalance;
        BigDecimal updatedRecipientBalance;
        
         Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :giverBankAccountNum");
            q.setParameter("giverBankAccountNum",giverBankAccountNum );
            List<SavingAccount> giverSavingAccounts = q.getResultList();
            SavingAccount giverSavingAccount = giverSavingAccounts.get(0);
            giverBalance = giverSavingAccount.getAvailableBalance();
            
            //if balance<transferAmount, the transfer is not allowed, return false
            if(giverBalance.compareTo(transferAmount) == -1){           
                return false;      
            } else {
                       
                Query m = em.createQuery("SELECT b FROM SavingAccount b WHERE b.accountNumber = :recipientBankAccountNum");
                m.setParameter("recipientBankAccountNum",recipientBankAccountNum );
                List<SavingAccount> recipientSavingAccounts = m.getResultList();
                
                //if the query returns an empty result, then the recipientAccountNum doesn't exists
                if(recipientSavingAccounts.isEmpty()){
                    return false;
                }else {
              
                    SavingAccount recipientSavingAccount = recipientSavingAccounts.get(0);
                    recipientBalance = recipientSavingAccount.getAvailableBalance();
                    //update the available balance of giver BankAccount (-)
                    updatedGiverBalance = giverBalance.subtract(transferAmount);
                    giverSavingAccount.setAvailableBalance(updatedGiverBalance); 
                    //update the available balance of recipient BankAccount (+)
                    updatedRecipientBalance = recipientBalance.add(transferAmount);
                    recipientSavingAccount.setAvailableBalance(updatedRecipientBalance);    
                    return true;
                }
            }
         
    }
    
    @Override
    public Boolean addPayee(Long payeeAccount, String payeeName, Long customerID){
                
        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :payeeAccount");
        q.setParameter("payeeAccount",payeeAccount );
            List<SavingAccount> payeeAccountLists = q.getResultList();
            if(payeeAccountLists.isEmpty()){
                return false;
            }else {
                SavingAccount payeeAccountList = payeeAccountLists.get(0);
                
                    Payee payee = new Payee();
                    payee.setId(customerID);
                    payee.setSavingAccount(payeeAccountList);
                    em.persist(payee);
                    em.flush();
                    Query m = em.createQuery("SELECT b FROM Customer b WHERE b.id = :customerID");
                    m.setParameter("customerID",customerID );
                    List<Customer> customers = m.getResultList();
                    Customer customer = customers.get(0);
                    customer.getPayees().add(payee);
                    return true; 
            }
    }

    public SavingAccount getGiverBankAccount() {
        return giverBankAccount;
    }

    public void setGiverBankAccount(SavingAccount giverBankAccount) {
        this.giverBankAccount = giverBankAccount;
    }

    public SavingAccount getReceipientBankAccount() {
        return receipientBankAccount;
    }

    public void setReceipientBankAccount(SavingAccount receipientBankAccount) {
        this.receipientBankAccount = receipientBankAccount;
    }

    public String getGiverBankName() {
        return giverBankName;
    }

    public void setGiverBankName(String giverBankName) {
        this.giverBankName = giverBankName;
    }

    public String getReceipientBankName() {
        return receipientBankName;
    }

    public void setReceipientBankName(String receipientBankName) {
        this.receipientBankName = receipientBankName;
    }

    public Long getGiverBankAccountNum() {
        return giverBankAccountNum;
    }

    public void setGiverBankAccountNum(Long giverBankAccountNum) {
        this.giverBankAccountNum = giverBankAccountNum;
    }
    
    public Long getReceipientBankAccountNum() {
        return receipientBankAccountNum;
    }

    public void setReceipientBankAccountNum(Long receipientBankAccountNum) {
        this.receipientBankAccountNum = receipientBankAccountNum;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
   
    public Long getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(Long payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }
    
    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    
    
}
