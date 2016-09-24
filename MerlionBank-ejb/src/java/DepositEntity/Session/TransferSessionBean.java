/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import DepositEntity.Payee;
import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
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
import java.text.SimpleDateFormat;
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


    @Override
    public void intraOneTimeTransferCheck(Long customerID,Long giverBankAccountNum, Long recipientBankAccountNum, BigDecimal transferAmount) throws TransferException {

        BigDecimal giverBalance;
        BigDecimal recipientBalance;
        BigDecimal updatedGiverBalance;
        BigDecimal updatedRecipientBalance;

        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :giverBankAccountNum");
        q.setParameter("giverBankAccountNum", giverBankAccountNum);
        List<SavingAccount> giverSavingAccounts = q.getResultList();
        SavingAccount giverSavingAccount = giverSavingAccounts.get(0);
        giverBalance = giverSavingAccount.getAvailableBalance();

        //check whether customer has exceed the transfer limit amount
        if(!this.checkTransferLimit(customerID, giverBankAccountNum, transferAmount)){
            throw new TransferException("Saving account " + giverBankAccountNum + " Has Exceed the daily Transfer Limit");
        }
        //if balance<transferAmount, the transfer is not allowed, return false
        else if (giverBalance.compareTo(transferAmount) == -1) {
            throw new TransferException("Saving account " + giverBankAccountNum + " does not have enough fund!");
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

                TransferRecord transferRecord = new TransferRecord("TF", transferAmount, "settled", "iBanking Transfer", currentTimestamp, giverBankAccountNum, recipientBankAccountNum, "intraTransfer", "MerlionBank", "MerlionBank");
                em.persist(transferRecord);
                em.flush();

                System.out.println("transfer successfully!");
            }
        }

    }
    
    @Override
    public BigDecimal getTransferLimit(Long customerID){
        Customer customer = em.find(Customer.class,customerID);
       return customer.getIntraTransferLimit();
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
    public void deletePayee(Long customerID, Long accountNum) {
        Customer customer = em.find(Customer.class,customerID);
        List<Payee> currentList = customer.getPayees();
        List<Payee> newList = new ArrayList<Payee>();

        for (int i = 0; i < currentList.size(); i++) {
            if (!currentList.get(i).getSavingAccount().getAccountNumber().equals(accountNum)) {
                newList.add(currentList.get(i));
            }
        }

        customer.setPayees(newList);
        em.flush();
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
                if (customer.getSavingAccounts().get(i).getStatus().equals("active")) {
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

    public List<List> getTransactionRecord(Long savingAccountNumber) {
        List<List> displayList = new ArrayList();

        Query m = em.createQuery("SELECT a FROM TransactionRecord a WHERE a.giverAccountNum = :giverAccountNum");
        m.setParameter("giverAccountNum", savingAccountNumber);
        List<TransactionRecord> record1 = m.getResultList();
        displayList.addAll(addTransferList(record1, "debit"));

        Query n = em.createQuery("SELECT a FROM TransactionRecord a WHERE a.recipientAccountNum = :recipientAccountNum");
        n.setParameter("recipientAccountNum", savingAccountNumber);
        List<TransactionRecord> record2 = n.getResultList();
        displayList.addAll(addTransferList(record2, "credit"));

        return displayList;

    }

    public List<List> addTransferList(List<TransactionRecord> record, String type) {
        List<List> list = new ArrayList();
        int count = 0;
        if (type.equals("credit")) {
            for (int i = 0; i < record.size(); i++) {
                if (record.get(i).equals("settled")) {
                    list.get(count).add(0, record.get(i).getTransactionTime());
                    list.get(count).add(1, record.get(i).getCode());
                    list.get(count).add(2, record.get(i).getDescription());
                    list.get(count).add(3, null);
                    list.get(count).add(4, record.get(i).getAmount());
                    count = count + 1;
                }

            }

        } else if (type.equals("debit")) {
            for (int i = 0; i < record.size(); i++) {
                if (record.get(i).equals("settled")) {
                    list.get(count).add(0, record.get(i).getTransactionTime());
                    list.get(count).add(1, record.get(i).getCode());
                    list.get(count).add(2, record.get(i).getDescription());
                    list.get(count).add(3, record.get(i).getAmount());
                    list.get(count).add(4, null);
                    count = count + 1;
                }

            }
        }
        return list;
    }

    @Override
    public void changeTransferLimit(Long customerID, BigDecimal transferLimit) {
        System.out.print(customerID);
        Customer customer1 = em.find(Customer.class, customerID);
        System.out.print(customer1.getName());
        customer1.setIntraTransferLimit(transferLimit);
        em.flush();
    }

    @Override
    public boolean checkTransferLimit(Long customerID, Long savingAccountNum, BigDecimal transferAmount) {
        String description = "iBanking Transfer";
        BigDecimal accumulatedAmount = new BigDecimal(0);
        BigDecimal totalAmount = new BigDecimal(0);

        Customer customer = em.find(Customer.class, customerID);
        BigDecimal intraLimit = customer.getIntraTransferLimit();

        Date currentTime = Calendar.getInstance().getTime(); //get current time
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String todayString = dateFormat.format(currentTimestamp);

        Query m = em.createQuery("SELECT a FROM TransactionRecord a WHERE a.giverAccountNum = :giverAccountNum and a.description = :description");
        m.setParameter("giverAccountNum", savingAccountNum);
        m.setParameter("description", description);
        List<TransactionRecord> record1 = m.getResultList();

        if (record1.isEmpty()) {
            totalAmount = accumulatedAmount.add(transferAmount);
            return totalAmount.compareTo(intraLimit) != 1;
        } else {
            for (int i = 0; i < record1.size(); i++) {
                if (dateFormat.format(record1.get(i).getTransactionTime()).equals(todayString)) {
                    accumulatedAmount = accumulatedAmount.add(record1.get(i).getAmount());
                }
            }
            totalAmount = accumulatedAmount.add(transferAmount);
            return totalAmount.compareTo(intraLimit) != 1;
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


}
