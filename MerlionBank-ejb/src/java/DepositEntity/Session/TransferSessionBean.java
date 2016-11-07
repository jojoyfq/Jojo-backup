/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import BillEntity.OtherBank;
import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import DepositEntity.Payee;
import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
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
    public void intraOneTimeTransferCheck(Long customerID, Long giverBankAccountNum, Long recipientBankAccountNum, BigDecimal transferAmount) throws TransferException {

        BigDecimal giverAvailableBalance;
        BigDecimal giverBalance;
        BigDecimal recipientAvailableBalance;
        BigDecimal recipientBalance;
        BigDecimal updatedGiverAvailableBalance;
        BigDecimal updatedGiverBalance;
        BigDecimal updatedRecipientAvailableBalance;
        BigDecimal updatedRecipientBalance;

        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :giverBankAccountNum");
        q.setParameter("giverBankAccountNum", giverBankAccountNum);
        List<SavingAccount> giverSavingAccounts = q.getResultList();
        SavingAccount giverSavingAccount = giverSavingAccounts.get(0);
        giverAvailableBalance = giverSavingAccount.getAvailableBalance();
        giverBalance = giverSavingAccount.getBalance();

        //check whether customer has exceed the transfer limit amount
        if (!this.checkTransferLimit(customerID, giverBankAccountNum, transferAmount)) {
            throw new TransferException("Saving account " + giverBankAccountNum + " Has Exceed the daily Transfer Limit");
        } //if balance<transferAmount, the transfer is not allowed, return false
        else if (giverAvailableBalance.compareTo(transferAmount) == -1) {
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
                recipientAvailableBalance = recipientSavingAccount.getAvailableBalance();
                recipientBalance = recipientSavingAccount.getBalance();
                //update the available balance of giver BankAccount (-)
                updatedGiverAvailableBalance = giverAvailableBalance.subtract(transferAmount);
                giverSavingAccount.setAvailableBalance(updatedGiverAvailableBalance);
                //update the balance of giver BankAccount (-)
                updatedGiverBalance = giverBalance.subtract(transferAmount);
                giverSavingAccount.setBalance(updatedGiverBalance);
                //update the available balance of recipient BankAccount (+)
                updatedRecipientAvailableBalance = recipientAvailableBalance.add(transferAmount);
                recipientSavingAccount.setAvailableBalance(updatedRecipientAvailableBalance);
                //update the balance of recipient BankAccount (+)
                updatedRecipientBalance = recipientBalance.add(transferAmount);
                recipientSavingAccount.setBalance(updatedRecipientBalance);
                em.flush();

                Date currentTime = Calendar.getInstance().getTime();
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

                TransactionRecord transaction1 = new TransactionRecord("TF", transferAmount, null, "settled", "iBanking Transfer", currentTimestamp, giverBankAccountNum, recipientBankAccountNum, giverSavingAccount, "MerlionBank", "MerlionBank");
                giverSavingAccount.getTransactionRecord().add(transaction1);
                em.persist(transaction1);
                em.flush();

                TransactionRecord transaction2 = new TransactionRecord("TF", null, transferAmount, "settled", "iBanking Transfer", currentTimestamp, giverBankAccountNum, recipientBankAccountNum, recipientSavingAccount, "MerlionBank", "MerlionBank");
                recipientSavingAccount.getTransactionRecord().add(transaction2);
                em.persist(transaction2);
                em.flush();

                logAction("Transfer to " + recipientBankAccountNum, customerID);

                System.out.println("transfer successfully!");
            }
        }

    }

    @Override
    public List<OtherBank> viewOtherBank() {
        Query query = em.createQuery("SELECT a FROM OtherBank a");
        List<OtherBank> currentAccounts = new ArrayList(query.getResultList());
        List<OtherBank> accounts = new ArrayList<OtherBank>();
        //BigDecimal temp=new BigDecimal(0);
        for (int i = 0; i < currentAccounts.size(); i++) {
            if (!currentAccounts.get(i).getName().equals("Merlion Bank")) {
                accounts.add(currentAccounts.get(i));
            }
        }
        return accounts;
    }

    @Override
    public void interOneTimeTransferCheck(Long customerID, Long giverBankAccountNum, Long recipientBankAccountNum, String recipientBankAccountName, BigDecimal transferAmount) throws TransferException {

        BigDecimal giverAvailableBalance;
        BigDecimal giverBalance;
        BigDecimal recipientAvailableBalance;
        BigDecimal recipientBalance;
        BigDecimal updatedGiverAvailableBalance;
        BigDecimal updatedGiverBalance;
        BigDecimal updatedRecipientAvailableBalance;
        BigDecimal updatedRecipientBalance;

        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :giverBankAccountNum");
        q.setParameter("giverBankAccountNum", giverBankAccountNum);
        List<SavingAccount> giverSavingAccounts = q.getResultList();
        SavingAccount giverSavingAccount = giverSavingAccounts.get(0);
        giverAvailableBalance = giverSavingAccount.getAvailableBalance();
        giverBalance = giverSavingAccount.getBalance();

        //check whether customer has exceed the transfer limit amount
        if (!this.checkTransferLimit(customerID, giverBankAccountNum, transferAmount)) {
            throw new TransferException("Saving account " + giverBankAccountNum + " Has Exceed the daily Transfer Limit");
        } //if balance<transferAmount, the transfer is not allowed, return false
        else if (giverAvailableBalance.compareTo(transferAmount) == -1) {
            throw new TransferException("Saving account " + giverBankAccountNum + " does not have enough fund!");
        }

        //update the available balance of giver BankAccount (-)
        updatedGiverAvailableBalance = giverAvailableBalance.subtract(transferAmount);
        giverSavingAccount.setAvailableBalance(updatedGiverAvailableBalance);
        //update the balance of giver BankAccount (-)
        updatedGiverBalance = giverBalance.subtract(transferAmount);
        giverSavingAccount.setBalance(updatedGiverBalance);

        em.flush();

        Date currentTime = Calendar.getInstance().getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

        TransactionRecord transaction1 = new TransactionRecord("INTF", transferAmount, null, "settled", "iBanking Transfer", currentTimestamp, giverBankAccountNum, recipientBankAccountNum, giverSavingAccount, "MerlionBank", recipientBankAccountName);
        giverSavingAccount.getTransactionRecord().add(transaction1);
        em.persist(transaction1);
        em.flush();

        logAction("Transfer to " + recipientBankAccountNum, customerID);

        System.out.println("transfer successfully!");

    }

    @Override
    public void payeeTransferCheck(Long customerID, Long giverBankAccountNum, Long recipientBankAccountNum, BigDecimal transferAmount) throws TransferException {
        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :giverBankAccountNum");
        q.setParameter("giverBankAccountNum", giverBankAccountNum);
        SavingAccount giverSavingAccount = (SavingAccount) q.getSingleResult();

        BigDecimal giverAvailableBalance = giverSavingAccount.getAvailableBalance();
        BigDecimal giverBalance = giverSavingAccount.getBalance();

        //check whether customer has exceed the transfer limit amount
        if (!this.checkTransferLimit(customerID, giverBankAccountNum, transferAmount)) {
            throw new TransferException("Saving account " + giverBankAccountNum + " Has Exceed the daily Transfer Limit");
        } else {
            Query m = em.createQuery("SELECT b FROM SavingAccount b WHERE b.accountNumber = :recipientBankAccountNum");
            m.setParameter("recipientBankAccountNum", recipientBankAccountNum);
            SavingAccount recipientAccount = (SavingAccount) m.getSingleResult();
            BigDecimal recipientAvailableBalance = recipientAccount.getAvailableBalance();
            BigDecimal recipientBalance = recipientAccount.getBalance();
            //update the available balance of giver BankAccount (-)
            BigDecimal updatedGiverAvailableBalance = giverAvailableBalance.subtract(transferAmount);
            giverSavingAccount.setAvailableBalance(updatedGiverAvailableBalance);
            //update the balance of giver BankAccount (-)
            BigDecimal updatedGiverBalance = giverBalance.subtract(transferAmount);
            giverSavingAccount.setBalance(updatedGiverBalance);
            //update the available balance of recipient BankAccount (+)
            BigDecimal updatedRecipientAvailableBalance = recipientAvailableBalance.add(transferAmount);
            recipientAccount.setAvailableBalance(updatedRecipientAvailableBalance);
            //update the balance of recipient BankAccount (+)
            BigDecimal updatedRecipientBalance = recipientBalance.add(transferAmount);
            recipientAccount.setBalance(updatedRecipientBalance);
            em.flush();

            Date currentTime = Calendar.getInstance().getTime();
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

            TransactionRecord transaction1 = new TransactionRecord("TF", transferAmount, null, "settled", "iBanking Transfer", currentTimestamp, giverBankAccountNum, recipientBankAccountNum, giverSavingAccount, "MerlionBank", "MerlionBank");
            giverSavingAccount.getTransactionRecord().add(transaction1);
            em.persist(transaction1);
            em.flush();

            TransactionRecord transaction2 = new TransactionRecord("TF", null, transferAmount, "settled", "iBanking Transfer", currentTimestamp, giverBankAccountNum, recipientBankAccountNum, recipientAccount, "MerlionBank", "MerlionBank");
            recipientAccount.getTransactionRecord().add(transaction2);
            em.persist(transaction2);
            em.flush();

            logAction("Transfer to " + recipientBankAccountNum, customerID);

            System.out.println("transfer successfully!");

        }
    }

    @Override
    public void logAction(String description, Long customerId) {
        List<CustomerAction> actions = new ArrayList<>();
        Customer customer = em.find(Customer.class, customerId);
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), description, customer);
        em.persist(action);
        System.out.print(action.getDescription());
        if (customer.getCustomerActions() == null) {
            actions.add(action);
            customer.setCustomerActions(actions);
            em.persist(actions);
        } else {
            customer.getCustomerActions().add(action);
        }
        em.persist(customer);
        em.flush();
    }

    @Override
    public BigDecimal getTransferLimit(Long customerID) {
        Customer customer = em.find(Customer.class, customerID);
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
        Customer customer = em.find(Customer.class, customerID);
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

        Customer customer = em.find(Customer.class, customerID);
        System.out.print("customer is: " + customer.getName());

        for (int i = 0; i < customer.getPayees().size(); i++) {
            String savingAccount = Long.toString(customer.getPayees().get(i).getSavingAccount().getAccountNumber());
            String payeeName = customer.getPayees().get(i).getSavingAccount().getCustomer().getName();
            payeeAccountList.add(savingAccount + "," + payeeName);
        }
        return payeeAccountList;
    }

    @Override
    public List<String> getSavingAccountNumbers(Long customerID) throws UserHasNoSavingAccountException {
        String savingString;
        List<String> savingAccountNumbers = new ArrayList();
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = q.getResultList();
        Customer customer = customers.get(0);
        if (customer.getSavingAccounts().isEmpty()) {
            throw new UserHasNoSavingAccountException("User has no saving account!");
        } else {
            for (int i = 0; i < customer.getSavingAccounts().size(); i++) {
                if (customer.getSavingAccounts().get(i).getStatus().equals("active")) {
                    savingString = customer.getSavingAccounts().get(i).getAccountNumber() + "," + customer.getSavingAccounts().get(i).getSavingAccountType().getAccountType();
                    savingAccountNumbers.add(savingString);
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

    @Override
    public boolean checkPayeeValidity(Long payeeAccount) {
        Query m = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :payeeAccount");
        m.setParameter("payeeAccount", payeeAccount);
        SavingAccount savingAccount = (SavingAccount) m.getSingleResult();
        if (savingAccount != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<TransactionRecord> getTransactionRecord(Long savingAccountNumber) {
        List<TransactionRecord> displayList = new ArrayList();

        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNumber");
        q.setParameter("savingAccountNumber", savingAccountNumber);
        SavingAccount savingAccount = (SavingAccount) q.getSingleResult();
        displayList = savingAccount.getTransactionRecord();

        return displayList;
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

        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNum");
        q.setParameter("savingAccountNum", savingAccountNum);
        SavingAccount savingAccount = (SavingAccount) q.getSingleResult();
        List<TransactionRecord> record = savingAccount.getTransactionRecord();

        if (record.isEmpty()) {
            totalAmount = accumulatedAmount.add(transferAmount);
            return totalAmount.compareTo(intraLimit) != 1;
        } else {
            for (int i = 0; i < record.size(); i++) {
                if (dateFormat.format(record.get(i).getTransactionTime()).equals(todayString) && record.get(i).getDescription().equals(description)) {
                    accumulatedAmount = accumulatedAmount.add(record.get(i).getDebit());
                }
            }
            totalAmount = accumulatedAmount.add(transferAmount);
            return totalAmount.compareTo(intraLimit) != 1;
        }

    }
    
    @Override
    public boolean intraOneTimeTransferCheckMobile(String customerIC, String giverBankAccountString, String recipientBankAccountString, String transferAmountStr) throws TransferException {

        BigDecimal giverAvailableBalance;
        BigDecimal giverBalance;
        BigDecimal recipientAvailableBalance;
        BigDecimal recipientBalance;
        BigDecimal updatedGiverAvailableBalance;
        BigDecimal updatedGiverBalance;
        BigDecimal updatedRecipientAvailableBalance;
        BigDecimal updatedRecipientBalance;

        Long giverBankAccountNum1 = Long.parseLong(giverBankAccountString);
        Long recipientBankAccountNum1 = Long.parseLong(recipientBankAccountString);
        BigDecimal transferAmount1 = new BigDecimal(transferAmountStr);

        Query m = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :customerIC");
        m.setParameter("customerIC", customerIC);
        Customer customer = (Customer) m.getSingleResult();
        Long customerID = customer.getId();

        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :giverBankAccountNum");
        q.setParameter("giverBankAccountNum", giverBankAccountNum1);
        List<SavingAccount> giverSavingAccounts = q.getResultList();
        SavingAccount giverSavingAccount = giverSavingAccounts.get(0);
        giverAvailableBalance = giverSavingAccount.getAvailableBalance();
        giverBalance = giverSavingAccount.getBalance();

        //check whether customer has exceed the transfer limit amount
        if (!this.checkTransferLimit(customerID, giverBankAccountNum1, transferAmount1)) {
            throw new TransferException("Saving account " + giverBankAccountNum1 + " Has Exceed the daily Transfer Limit");
        } //if balance<transferAmount, the transfer is not allowed, return false
        else if (giverAvailableBalance.compareTo(transferAmount1) == -1) {
            throw new TransferException("Saving account " + giverBankAccountNum1 + " does not have enough fund!");
        } else {

            Query g = em.createQuery("SELECT b FROM SavingAccount b WHERE b.accountNumber = :recipientBankAccountNum");
            g.setParameter("recipientBankAccountNum", recipientBankAccountNum1);
            List<SavingAccount> recipientSavingAccounts = g.getResultList();

            //if the query returns an empty result, then the recipientAccountNum doesn't exists
            if (recipientSavingAccounts.isEmpty()) {
                throw new TransferException("The Recipient Account Number You have Entered is incorrect!");
            } else {

                SavingAccount recipientSavingAccount = recipientSavingAccounts.get(0);
                recipientAvailableBalance = recipientSavingAccount.getAvailableBalance();
                recipientBalance = recipientSavingAccount.getBalance();
                //update the available balance of giver BankAccount (-)
                updatedGiverAvailableBalance = giverAvailableBalance.subtract(transferAmount1);
                giverSavingAccount.setAvailableBalance(updatedGiverAvailableBalance);
                //update the balance of giver BankAccount (-)
                updatedGiverBalance = giverBalance.subtract(transferAmount1);
                giverSavingAccount.setBalance(updatedGiverBalance);
                //update the available balance of recipient BankAccount (+)
                updatedRecipientAvailableBalance = recipientAvailableBalance.add(transferAmount1);
                recipientSavingAccount.setAvailableBalance(updatedRecipientAvailableBalance);
                //update the balance of recipient BankAccount (+)
                updatedRecipientBalance = recipientBalance.add(transferAmount1);
                recipientSavingAccount.setBalance(updatedRecipientBalance);
                em.flush();

                Date currentTime = Calendar.getInstance().getTime();
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

                TransactionRecord transaction1 = new TransactionRecord("TF", transferAmount1, null, "settled", "iBanking Transfer", currentTimestamp, giverBankAccountNum1, recipientBankAccountNum1, giverSavingAccount, "MerlionBank", "MerlionBank");
                giverSavingAccount.getTransactionRecord().add(transaction1);
                em.persist(transaction1);
                em.flush();

                TransactionRecord transaction2 = new TransactionRecord("TF", null, transferAmount1, "settled", "iBanking Transfer", currentTimestamp, giverBankAccountNum1, recipientBankAccountNum1, recipientSavingAccount, "MerlionBank", "MerlionBank");
                recipientSavingAccount.getTransactionRecord().add(transaction2);
                em.persist(transaction2);
                em.flush();

                logAction("Transfer to " + recipientBankAccountNum1, customerID);

                System.out.println("transfer successfully!");
                return true;
            }
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
