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
        Customer customer = em.find(Customer.class, customerID);
        
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
    public List<TransactionRecord> getTransactionRecord(Long savingAccountNumber) {
        List<TransactionRecord> displayList = new ArrayList();
        
        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNumber");
        q.setParameter("savingAccountNumber", savingAccountNumber);
        SavingAccount savingAccount = (SavingAccount) q.getSingleResult();
        displayList = savingAccount.getTransactionRecord();
        
        return displayList;   
    }

    @Override
    public void checkPendingTransaction(Long savingAccountNum) throws UserHasPendingTransactionException, UserCloseAccountException {
        Query t = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNum");
        t.setParameter("savingAccountNum", savingAccountNum);
        SavingAccount savingAccount = (SavingAccount) t.getSingleResult();
        
        if (savingAccount.getAvailableBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new UserCloseAccountException("Please transfer the remaining amount to another account before close this account!");
        } else {
            String status = "pending";
            
            List<TransactionRecord> record = savingAccount.getTransactionRecord();
            if(record.isEmpty()){
                System.out.print("User has no pending transaction!");
                savingAccount.setStatus("terminated");
                em.flush();
            }else{
                for(int i=0;i<record.size();i++){
                    if(record.get(i).getStatus().equals(status)){
                        throw new UserHasPendingTransactionException("This Saving Account Has pending Transactions!");
                    }
                }
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
        SavingAccount savingAccount = (SavingAccount)q.getSingleResult();

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
            TransactionRecord transactionRecord = new TransactionRecord("CW", withdrawAmount,null, "settled", "Cash Withdraw", currentTimestamp, accountNum, null,savingAccount,"MerlionBank","MerlionBank");
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
        SavingAccount savingAccount = (SavingAccount)q.getSingleResult();

        BigDecimal updatedBalance = savingAccount.getAvailableBalance().add(depositAmount);
        savingAccount.setAvailableBalance(updatedBalance);
        BigDecimal updatedTotalBalance = savingAccount.getBalance().add(depositAmount);
        savingAccount.setBalance(updatedTotalBalance);
        em.flush();
        System.out.print("cash deposit successful");
        System.out.print(savingAccount.getAvailableBalance());

        Date currentTime = Calendar.getInstance().getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());
        TransactionRecord transactionRecord = new TransactionRecord("CD", null, depositAmount, "settled", "Cash Deposit", currentTimestamp, null, accountNum,savingAccount,"MerlionBank","MerlionBank");
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

    //daily interest accrual 
    @Override
    public void dailyInterestAccrued() {
        Query query1 = em.createQuery("SELECT a FROM SavingAccount a");
        List<SavingAccount> savingAccounts = new ArrayList(query1.getResultList());

        BigDecimal availableBalance;
        BigDecimal remainingBalance;
        BigDecimal dailyInterest;
        BigDecimal specialInterest;
        double interestRate;
        BigDecimal accumulatedDailyInterest;
        long savingAccountType;

        for (int i = 0; i < savingAccounts.size(); i++) {
            System.out.println("***** inside the loop *****");
            String status = savingAccounts.get(i).getStatus();
            if (status.equals("active")) {
                //System.out.println("***** display saving active account *****");
                //System.out.println(savingAccounts);
                
                savingAccountType = savingAccounts.get(i).getSavingAccountType().getId();
                System.out.println(savingAccounts.get(i).getSavingAccountType().getId());
                
                if (savingAccountType == 13) {
                    System.out.println("********** Everyday Saving Account daily interest ********** ");
                    availableBalance = savingAccounts.get(i).getAvailableBalance();
                    
                    System.out.println("***** account balance: " + availableBalance);
                    
                    if (availableBalance.compareTo(BigDecimal.valueOf(150000)) == 1) {
                        System.out.println("********** Everyday Saving Account with balance greater than 150,000 **********");
                        BigDecimal firstInterest = BigDecimal.valueOf(50000 * savingAccounts.get(i).getSavingAccountType().getInterestRate1());
                        System.out.println("***** your first interest: " + firstInterest);
                        
                        BigDecimal secondInterest = BigDecimal.valueOf(100000 * savingAccounts.get(i).getSavingAccountType().getInterestRate2());
                        System.out.println("***** your second interest: " + secondInterest);
                        
                        remainingBalance = availableBalance.subtract(BigDecimal.valueOf(150000));
                        BigDecimal remainingInterest = remainingBalance.multiply(BigDecimal.valueOf(savingAccounts.get(i).getSavingAccountType().getInterestRate3()));
                        System.out.println("***** your remaining interest: " + remainingInterest);

                        
                        dailyInterest = firstInterest.add(secondInterest).add(remainingInterest);
                        System.out.println("********** Daily interest is: " + dailyInterest);
                        
                        accumulatedDailyInterest = savingAccounts.get(i).getAccumDailyInterest();
                        System.out.println("********** Accumlated interest is: " + accumulatedDailyInterest);
                        savingAccounts.get(i).setAccumDailyInterest(accumulatedDailyInterest.add(dailyInterest));

                    } else if (availableBalance.compareTo(BigDecimal.valueOf(150000)) == -1 && availableBalance.compareTo(BigDecimal.valueOf(50000)) == 1){
                        System.out.println("********** Everyday Saving Account with balance in between 150,000 and 50,000 **********");
                        BigDecimal firstInterest = BigDecimal.valueOf(50000 * savingAccounts.get(i).getSavingAccountType().getInterestRate1());
                        System.out.println("***** your first interest: " + firstInterest);
                        
                        remainingBalance = BigDecimal.valueOf(150000).subtract(availableBalance);
                        System.out.println("***** your remaining Balance is* " + remainingBalance);
                        BigDecimal remainingInterest = remainingBalance.multiply(BigDecimal.valueOf(savingAccounts.get(i).getSavingAccountType().getInterestRate2()));
                        System.out.println("***** your remaining interest: " + remainingInterest);
                        
                        dailyInterest = firstInterest.add(remainingInterest);
                        System.out.println("********** Daily interest is: " + dailyInterest);
                        accumulatedDailyInterest = savingAccounts.get(i).getAccumDailyInterest().add(dailyInterest);
                        System.out.println("********** Accumlated interest is: " + accumulatedDailyInterest);
                        savingAccounts.get(i).setAccumDailyInterest(accumulatedDailyInterest);

                    } else if ((availableBalance.compareTo(BigDecimal.valueOf(50000)) == -1) || (availableBalance.compareTo(BigDecimal.valueOf(50000)) == 0)) {
                        System.out.println("********** Everyday Saving Account with balance $0 - $50,000 **********");
                        dailyInterest = availableBalance.multiply(BigDecimal.valueOf(savingAccounts.get(i).getSavingAccountType().getInterestRate1()));
                        System.out.println("********** Daily interest is: " + dailyInterest);
                        accumulatedDailyInterest = savingAccounts.get(i).getAccumDailyInterest().add(dailyInterest);
                        System.out.println("********** Accumlated interest is: " + accumulatedDailyInterest);
                        savingAccounts.get(i).setAccumDailyInterest(accumulatedDailyInterest);
                    }
                    else if(availableBalance.compareTo(BigDecimal.valueOf(150000)) == 0){
                        System.out.println("********** Everyday Saving Account with balance is $150000 **********");
                        BigDecimal firstInterest = BigDecimal.valueOf(50000 * savingAccounts.get(i).getSavingAccountType().getInterestRate1());
                        System.out.println("***** your first interest: " + firstInterest);
                        
                        BigDecimal remainingInterest = BigDecimal.valueOf(100000 * savingAccounts.get(i).getSavingAccountType().getInterestRate2());
                        System.out.println("***** your remaining interest: " + remainingInterest);
                        
                        dailyInterest = firstInterest.add(remainingInterest);
                        System.out.println("********** Daily interest is: " + dailyInterest);
                        accumulatedDailyInterest = savingAccounts.get(i).getAccumDailyInterest().add(dailyInterest);
                        System.out.println("********** Accumlated interest is: " + accumulatedDailyInterest);
                        savingAccounts.get(i).setAccumDailyInterest(accumulatedDailyInterest);
                    }
                } else if (savingAccountType == 11) {
                    availableBalance = savingAccounts.get(i).getAvailableBalance();
                    System.out.println("***** Account balance: " + availableBalance);
                    System.out.println("********** Monthly Saving Account daily interest ********** ");
                    //Monthly Saving Account
                    dailyInterest = availableBalance.multiply(BigDecimal.valueOf(savingAccounts.get(i).getSavingAccountType().getInterestRate1()));
                    System.out.println("********** Daily interest is: " + dailyInterest);
                    accumulatedDailyInterest = savingAccounts.get(i).getAccumDailyInterest();
                    System.out.println("********** Accumlated interest is: " + accumulatedDailyInterest);
                    savingAccounts.get(i).setAccumDailyInterest(accumulatedDailyInterest.add(dailyInterest));
                } else if (savingAccountType == 12) {
                    System.out.println("********** Youth Saving Account daily interest ********** ");
                    //Youth saving account
                    availableBalance = savingAccounts.get(i).getAvailableBalance();
                    System.out.println("***** Account balance: " + availableBalance);
                    dailyInterest = availableBalance.multiply(BigDecimal.valueOf(savingAccounts.get(i).getSavingAccountType().getInterestRate1()));
                    System.out.println("********** Daily interest is: " + dailyInterest);
                    accumulatedDailyInterest = savingAccounts.get(i).getAccumDailyInterest();
                    System.out.println("********** Accumlated interest is: " + accumulatedDailyInterest);
                    savingAccounts.get(i).setAccumDailyInterest(accumulatedDailyInterest.add(dailyInterest));
                }
            }
        }
    }
    
    @Override
    public void monthlyInterestCrediting(){
        BigDecimal accumulatedDailyInterest;
        BigDecimal availableBalance;
        BigDecimal newAvailableBalance;
        String status;
        
        Query query1 = em.createQuery("SELECT a FROM SavingAccount a");
        List<SavingAccount> savingAccounts = new ArrayList(query1.getResultList());
        
        for (int i = 0; i < savingAccounts.size(); i++) {
            System.out.println("***** inside the loop *****");
            status = savingAccounts.get(i).getStatus();
            if (status.equals("active")){
                accumulatedDailyInterest = savingAccounts.get(i).getAccumDailyInterest();
                availableBalance = savingAccounts.get(i).getAvailableBalance();
                newAvailableBalance = accumulatedDailyInterest.add(availableBalance);
                savingAccounts.get(i).setAvailableBalance(newAvailableBalance);
                em.flush();
                savingAccounts.get(i).setAccumDailyInterest(BigDecimal.ZERO);
                em.flush();
            }
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
