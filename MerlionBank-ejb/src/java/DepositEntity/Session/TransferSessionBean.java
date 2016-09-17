/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import DepositEntity.Payee;
import DepositEntity.SavingAccount;
import DepositEntity.TransferRecord;
import Exception.PayeeNotFoundException;
import Exception.TransferException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import Exception.UserHasNoSavingAccountException;
import java.util.Calendar;

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
    public void intraOneTimeTransferCheck(Long giverBankAccountNum, Long recipientBankAccountNum, BigDecimal transferAmount)throws TransferException {
        BigDecimal giverBalance;
        BigDecimal recipientBalance;
        BigDecimal updatedGiverBalance;
        BigDecimal updatedRecipientBalance;

        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :giverBankAccountNum");
        q.setParameter("giverBankAccountNum", giverBankAccountNum);
        List<SavingAccount> giverSavingAccounts = q.getResultList();
        SavingAccount giverSavingAccount = giverSavingAccounts.get(0);
        giverBalance = giverSavingAccount.getAvailableBalance();

        //if balance<transferAmount, the transfer is not allowed, return false
        if (giverBalance.compareTo(transferAmount) == -1) {
            throw new TransferException("Your saving account " + giverBankAccountNum + " does not have enough fund!");
        } else {

            Query m = em.createQuery("SELECT b FROM SavingAccount b WHERE b.accountNumber = :recipientBankAccountNum");
            m.setParameter("recipientBankAccountNum", recipientBankAccountNum);
            List<SavingAccount> recipientSavingAccounts = m.getResultList();

            //if the query returns an empty result, then the recipientAccountNum doesn't exists
            if (recipientSavingAccounts.isEmpty()) {
                throw new TransferException("The Recipient Account Number You have Entered is incorrect!");
            } else {

                SavingAccount recipientSavingAccount = recipientSavingAccounts.get(0);
                recipientBalance = recipientSavingAccount.getAvailableBalance();
                //update the available balance of giver BankAccount (-)
                updatedGiverBalance = giverBalance.subtract(transferAmount);
                giverSavingAccount.setAvailableBalance(updatedGiverBalance);
                //update the available balance of recipient BankAccount (+)
                updatedRecipientBalance = recipientBalance.add(transferAmount);
                recipientSavingAccount.setAvailableBalance(updatedRecipientBalance);
                
                Date currentTime = Calendar.getInstance().getTime();
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());
                
                TransferRecord transferRecord = new TransferRecord("TF", transferAmount, "settled", "iBanking Transfer",currentTimestamp,giverBankAccountNum,recipientBankAccountNum, "intraTransfer","MerlionBank","MerlionBank");
                em.persist(transferRecord);
                em.flush();
                
                System.out.println("transfer successfully!");
            }
        }

    }

    @Override
    public void addPayee(Long payeeAccount, String payeeName, Long customerID) throws PayeeNotFoundException {

        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :payeeAccount");
        q.setParameter("payeeAccount", payeeAccount);
        List<SavingAccount> payeeAccountLists = q.getResultList();
        if (payeeAccountLists.isEmpty()) {
            throw new PayeeNotFoundException("The Payee Account You have Entered is incorrect! Please Enter Again!");
        } else {
            SavingAccount payeeAccountList = payeeAccountLists.get(0);

            Payee payee = new Payee();
            payee.setSavingAccount(payeeAccountList);
            em.persist(payee);
            em.flush();
            Query m = em.createQuery("SELECT b FROM Customer b WHERE b.id = :customerID");
            m.setParameter("customerID", customerID);
            List<Customer> customers = m.getResultList();
            Customer customer = customers.get(0);
            customer.getPayees().add(payee);
            
            System.out.print("Add Payee Success!");
        }
    }

    @Override
    public List getPayeeList(Long customerID) {
        List payeeAccountList = new ArrayList();
        Query m = em.createQuery("SELECT b FROM Customer b WHERE b.id = :customerID");
        m.setParameter("customerID", customerID);
        List<Customer> customers = m.getResultList();
        Customer customer = customers.get(0);

        System.out.print("customer is: " + customer.getName());

        for (int i = 0; i < customer.getPayees().size(); i++) {
            Long savingAccount = customer.getPayees().get(i).getSavingAccount().getAccountNumber();
            payeeAccountList.add(savingAccount);
        }
        return payeeAccountList;
    }

    @Override
    public List<Long> getSavingAccountNumbers(Long customerID) throws UserHasNoSavingAccountException {
        List<Long> savingAccountNumbers = new ArrayList();
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = q.getResultList();
        Customer customer = customers.get(0);
        if (customer.getSavingAccounts().isEmpty()) {
            throw new UserHasNoSavingAccountException("User has no saving account!");
        } else {
            for (int i = 0; i < customer.getSavingAccounts().size(); i++) {
                if(customer.getSavingAccounts().get(i).getStatus().equals("active")){
                savingAccountNumbers.add(customer.getSavingAccounts().get(i).getAccountNumber());
                }
            }
            return savingAccountNumbers;
        }
    }

    @Override
    public String searchPayeeName(Long payeeAccount) {
        Query m = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :payeeAccount");
        m.setParameter("payeeAccount", payeeAccount);
        List<SavingAccount> savingAccounts = m.getResultList();
        SavingAccount savingAccount = savingAccounts.get(0);
        return savingAccount.getCustomer().getName();
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
