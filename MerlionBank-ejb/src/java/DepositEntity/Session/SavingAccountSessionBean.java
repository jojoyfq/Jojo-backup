/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CommonEntity.Staff;
import CustomerRelationshipEntity.StaffAction;
import DepositEntity.SavingAccount;
import DepositEntity.SavingAccountType;
import DepositEntity.TransactionRecord;
import DepositEntity.TransferRecord;
import Exception.UserCloseAccountException;
import Exception.UserHasNoInactiveSavingAccountException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserHasPendingTransactionException;
import Exception.UserNotEnoughBalanceException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Bella
 */
@Stateless
public class SavingAccountSessionBean implements SavingAccountSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private Customer customer;

    @Override
    public List<SavingAccount> getSavingAccount(Long customerID) throws UserHasNoSavingAccountException {
        List<SavingAccount> activeSavingAccounts = new ArrayList();

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = q.getResultList();
        Customer customer = customers.get(0);
        if (customer.getSavingAccounts().isEmpty()) {
            System.out.print("Customer has no saving account");
            throw new UserHasNoSavingAccountException("User has no saving account!");
        } else {
            for (int i = 0; i < customer.getSavingAccounts().size(); i++) {
                if (customer.getSavingAccounts().get(i).getStatus().equals("active")) {
                    activeSavingAccounts.add(customer.getSavingAccounts().get(i));
                }
            }
            return activeSavingAccounts;
        }
    }

    @Override
    public List<String> getSavingAccountType() {
        String savingAccountType;
        List<String> savingAccountString = new ArrayList<String>();

        Query q = em.createQuery("SELECT a FROM SavingAccountType a");
        List<SavingAccountType> savingAccountTypes = q.getResultList();

        if (savingAccountTypes.isEmpty()) {
            System.out.print("The accountType Table is Empty");
            savingAccountString.add("false");
            return savingAccountString;
        } else {
            System.out.print(savingAccountTypes.size());
            for (int i = 0; i < savingAccountTypes.size(); i++) {
                savingAccountType = savingAccountTypes.get(i).getAccountType();
                System.out.print(savingAccountTypes.get(i).getAccountType());
                savingAccountString.add(savingAccountType);
            }
            return savingAccountString;
        }
    }

    @Override
    public Double getInterestRate(String accountType, String interestName) {
        Query q = em.createQuery("SELECT a FROM SavingAccountType a WHERE a.accountType = :accountType");
        q.setParameter("accountType", accountType);
        List<SavingAccountType> savingAccountTypes = q.getResultList();
        SavingAccountType temp = savingAccountTypes.get(0);

        if (interestName.equals("InterestRate1")) {
            return temp.getInterestRate1();
        } else if (interestName.equals("InterestRate2")) {
            return temp.getInterestRate2();
        } else {
            return temp.getInterestRate3();
        }
    }

    @Override
    public void setInterestRate(String accountType, Double interest1, Double interest2, Double interest3) {
        Query q = em.createQuery("SELECT a FROM SavingAccountType a WHERE a.accountType = :accountType");
        q.setParameter("accountType", accountType);
        List<SavingAccountType> savingAccountTypes = q.getResultList();
        SavingAccountType temp = savingAccountTypes.get(0);

        temp.setInterestRate1(interest1);
        temp.setInterestRate2(interest2);
        temp.setInterestRate3(interest3);

        em.flush();
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
            //haha
        }
    }

    @Override
    public List<Long> getInactiveSavingAccountNumbers(Long customerID) throws UserHasNoInactiveSavingAccountException {
        List<Long> savingAccountNumbers = new ArrayList();
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = q.getResultList();
        Customer customer = customers.get(0);
        if (customer.getSavingAccounts().isEmpty()) {
            throw new UserHasNoInactiveSavingAccountException("User has no Inactive saving account!");
        } else {
            for (int i = 0; i < customer.getSavingAccounts().size(); i++) {
                if (customer.getSavingAccounts().get(i).getStatus().equals("inactive")) {
                    savingAccountNumbers.add(customer.getSavingAccounts().get(i).getAccountNumber());
                }
            }
            return savingAccountNumbers;
        }
    }

    @Override
    public List<Long> getNotTerminatedAccountNumbers(Long customerID) throws UserHasNoSavingAccountException {
        List<Long> savingAccountNumbers = new ArrayList();
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = q.getResultList();
        Customer customer = customers.get(0);
        if (customer.getSavingAccounts().isEmpty()) {
            throw new UserHasNoSavingAccountException("User has no saving account!");
        } else {
            for (int i = 0; i < customer.getSavingAccounts().size(); i++) {
                //Get all the not terminated saving accounts
                if (!customer.getSavingAccounts().get(i).getStatus().equals("terminated")) {
                    savingAccountNumbers.add(customer.getSavingAccounts().get(i).getAccountNumber());
                }
            }
            return savingAccountNumbers;
        }
    }

    @Override
    public List<SavingAccount> getSavingAccountForCloseAccount(Long savingAccountNum) {
        List savingAccountForCloseAccount = new ArrayList();

        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNum");
        q.setParameter("savingAccountNum", savingAccountNum);
        List<SavingAccount> savingAccounts = q.getResultList();
        SavingAccount savingAccount = savingAccounts.get(0);

        savingAccountForCloseAccount.add(savingAccount);
//        savingAccountForCloseAccount.add(savingAccount.getAccountNumber());
//        savingAccountForCloseAccount.add(savingAccount.getAvailableBalance());
//        savingAccountForCloseAccount.add(savingAccount.getSavingAccountType().getAccountType());

        return savingAccountForCloseAccount;
    }

    @Override
    public List<List> getTransactionRecord(Long savingAccountNumber) {
        System.out.print("inside the getTransactionRecord SessionBean");

        List<List> displayList = new ArrayList();

        Query m = em.createQuery("SELECT a FROM TransactionRecord a WHERE a.giverAccountNum = :giverAccountNum");
        m.setParameter("giverAccountNum", savingAccountNumber);
        List<TransactionRecord> record1 = m.getResultList();
        System.out.print("record1 size" + record1.size());
        List<List> temp = new ArrayList();
        temp = addTransferList(record1, "debit");
        displayList.addAll(temp);

        System.out.print("in the middle of transaction session Bean" + displayList.size());

        Query n = em.createQuery("SELECT a FROM TransactionRecord a WHERE a.recipientAccountNum = :recipientAccountNum");
        n.setParameter("recipientAccountNum", savingAccountNumber);
        List<TransactionRecord> record2 = n.getResultList();
        List<List> temp2 = new ArrayList();
        temp2 = addTransferList(record2, "credit");
        displayList.addAll(temp2);

        return displayList;

    }

    private List<List> addTransferList(List<TransactionRecord> record, String type) {

        List<List> list = new ArrayList();
        int count = 0;
        if (type.equals("credit")) {
            for (int i = 0; i < record.size(); i++) {
                if (record.get(i).getStatus().equals("settled")) {
                    list.add(count, new ArrayList<>());
                    list.get(count).add(0, record.get(i).getTransactionTime());
                    list.get(count).add(1, record.get(i).getCode());
                    list.get(count).add(2, record.get(i).getDescription());
                    list.get(count).add(3, null);
                    list.get(count).add(4, record.get(i).getAmount());
                    count = count + 1;
                }

            }

        } else if (type.equals("debit")) {
            System.out.print("Inside the addTransferList method");
            for (int i = 0; i < record.size(); i++) {
                if (record.get(i).getStatus().equals("settled")) {
                    list.add(count, new ArrayList<>());
                    list.get(count).add(0, record.get(i).getTransactionTime());
                    list.get(count).add(1, record.get(i).getCode());
                    list.get(count).add(2, record.get(i).getDescription());
                    list.get(count).add(3, record.get(i).getAmount());
                    list.get(count).add(4, null);
                    count = count + 1;
                }

            }

        }
        System.out.print("Inside the addTransferList method " + list.size());
        return list;
    }

    @Override
    public void checkPendingTransaction(Long savingAccountNum) throws UserHasPendingTransactionException, UserCloseAccountException {
        Query t = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNum");
        t.setParameter("savingAccountNum", savingAccountNum);
        List<SavingAccount> savingAccounts = t.getResultList();
        SavingAccount savingAccount = savingAccounts.get(0);
        if (savingAccount.getAvailableBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new UserCloseAccountException("Please transfer the remaining amount to another account before close this account!");
        } else {

            String status = "pending";
            //check whether got pending transaction as giverAccount
            Query q = em.createQuery("SELECT a FROM TransactionRecord a WHERE a.giverAccountNum = :giverAccountNum AND a.status =:giverStatus");
            q.setParameter("giverAccountNum", savingAccountNum);
            q.setParameter("giverStatus", status);
            List<TransactionRecord> record1 = q.getResultList();

            //check whether got pending transaction as a recipientAccount
            Query w = em.createQuery("SELECT b FROM TransactionRecord b WHERE b.recipientAccountNum = :recipientAccountNum AND b.status =:recipientStatus");
            w.setParameter("recipientAccountNum", savingAccountNum);
            w.setParameter("recipientStatus", status);
            List<TransactionRecord> record2 = w.getResultList();

            if (record1.isEmpty() && record2.isEmpty()) {
                System.out.print("User has no pending transaction!");
                Query m = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNum");
                m.setParameter("savingAccountNum", savingAccountNum);
                List<SavingAccount> records3 = m.getResultList();
                SavingAccount record3 = records3.get(0);
                record3.setStatus("terminated");

                em.flush();
            } else {
                throw new UserHasPendingTransactionException("This Saving Account Has pending Transactions!");
            }
        }
    }

    @Override
    public void checkInactiveSavingAccount(Long inactiveSavingAccountNum) throws UserNotEnoughBalanceException {
        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :inactiveSavingAccountNum");
        q.setParameter("inactiveSavingAccountNum", inactiveSavingAccountNum);
        List<SavingAccount> savingAccounts = q.getResultList();
        SavingAccount savingAccount = savingAccounts.get(0);

        BigDecimal minAmount = savingAccount.getSavingAccountType().getMinAmount();
        if (savingAccount.getAvailableBalance().compareTo(minAmount) == -1) {
            throw new UserNotEnoughBalanceException("You Do Not Have Enough Balance, We Are Sorry That We Cannot Activate Your Account. Please Go and Top Up Your Account! ");
        } else {
            savingAccount.setStatus("active");
            em.flush();
        }
    }

    @Override
    public void cashWithdraw(Long accountNum, BigDecimal withdrawAmount) throws UserNotEnoughBalanceException {
        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :accountNum");
        q.setParameter("accountNum", accountNum);
        List<SavingAccount> savingAccounts = q.getResultList();
        SavingAccount savingAccount = savingAccounts.get(0);

        if (savingAccount.deductAmt(withdrawAmount)) {
            BigDecimal updatedBalance = savingAccount.getAvailableBalance().subtract(withdrawAmount);
            savingAccount.setAvailableBalance(updatedBalance);
            BigDecimal updatedTotalBalance = savingAccount.getBalance().subtract(withdrawAmount);
            savingAccount.setBalance(updatedTotalBalance);
            em.flush();
            System.out.print("cash withdraw successful");
            System.out.print(savingAccount.getAvailableBalance());

            Date currentTime = Calendar.getInstance().getTime();
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());
            TransactionRecord transactionRecord = new TransactionRecord("CW", withdrawAmount, "settled", "Cash Withdraw", currentTimestamp, accountNum, null);
            em.persist(transactionRecord);
            em.flush();
        } else {
            throw new UserNotEnoughBalanceException("This Saving Account Does not Have Enough Balance!");
        }
    }

    @Override
    public void cashDeposit(Long accountNum, BigDecimal depositAmount) {
        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :accountNum");
        q.setParameter("accountNum", accountNum);
        List<SavingAccount> savingAccounts = q.getResultList();
        SavingAccount savingAccount = savingAccounts.get(0);

        BigDecimal updatedBalance = savingAccount.getAvailableBalance().add(depositAmount);
        savingAccount.setAvailableBalance(updatedBalance);
        BigDecimal updatedTotalBalance = savingAccount.getBalance().add(depositAmount);
        savingAccount.setBalance(updatedTotalBalance);
        em.flush();
        System.out.print("cash deposit successful");
        System.out.print(savingAccount.getAvailableBalance());

        Date currentTime = Calendar.getInstance().getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());
        TransactionRecord transactionRecord = new TransactionRecord("CD", depositAmount, "settled", "Cash Deposit", currentTimestamp, null, accountNum);
        em.persist(transactionRecord);
        em.flush();
    }

    @Override
    public void logAction(String description, Long customerId) {
        List<CustomerAction> actions = new ArrayList<>();
        customer = em.find(Customer.class, customerId);
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
    public void logStaffAction(String description, Long customerId, Staff staff) {
        List<StaffAction> actions = new ArrayList<>();
        StaffAction action = new StaffAction(Calendar.getInstance().getTime(), description, customerId, staff);
        em.persist(action);
        System.out.print(action.getDescription());
        if (staff.getStaffActions() == null) {
            actions.add(action);
            staff.setStaffActions(actions);
            em.persist(actions);
        } else {
            staff.getStaffActions().add(action);
        }
        em.flush();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
