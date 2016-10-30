/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CommonEntity.OnlineAccount;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import static org.joda.time.Period.years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Bella
 */
@Stateless
public class SavingAccountSessionBean implements SavingAccountSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private Customer customer;
    private Object cal;

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
        ArrayList<BigDecimal> dailyBalance = new ArrayList<BigDecimal>();

        for (int i = 0; i < savingAccounts.size(); i++) {
            System.out.println("***** inside the loop *****");
            String status = savingAccounts.get(i).getStatus();
            if (status.equals("active")) {

                //add the daily balance into the dailyBalance array list 
                availableBalance = savingAccounts.get(i).getAvailableBalance();
                dailyBalance = (ArrayList<BigDecimal>) savingAccounts.get(i).getDailyBalance();
                if (dailyBalance == null) {
                    System.out.println("it is null!!!!!!!!!!!!!!!!!!!!!!");
                }

                dailyBalance.add(availableBalance);
                System.out.println("**********daily balance array list: " + dailyBalance);
                savingAccounts.get(i).setDailyBalance(dailyBalance);
                em.flush();

                savingAccountType = savingAccounts.get(i).getSavingAccountType().getId();
                System.out.println("***** saving account type is: " + savingAccountType);

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

                    } else if (availableBalance.compareTo(BigDecimal.valueOf(150000)) == -1 && availableBalance.compareTo(BigDecimal.valueOf(50000)) == 1) {
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
                    } else if (availableBalance.compareTo(BigDecimal.valueOf(150000)) == 0) {
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
    public void monthlyInterestCrediting() {
        BigDecimal accumulatedDailyInterest;
        BigDecimal availableBalance;
        BigDecimal newAvailableBalance;
        BigDecimal additionalInterest;
        String status;
        Long savingAccountType;
        Query query1 = em.createQuery("SELECT a FROM SavingAccount a");
        List<SavingAccount> savingAccounts = new ArrayList(query1.getResultList());
        Long savingAccount;
        List<TransactionRecord> transactionRecord;
        BigDecimal transactionAmount = BigDecimal.ZERO;
        ArrayList<BigDecimal> dailyBalance = new ArrayList<BigDecimal>();
        int flag1 = 0;
        int flag2 = 0;
        BigDecimal sumOfDailyBalance = BigDecimal.ZERO;
        BigDecimal averageDailyBalance = BigDecimal.ZERO;
        //check the account balance 

        for (int i = 0; i < savingAccounts.size(); i++) {
            System.out.println("********** inside the accounting interating loop **********");
            status = savingAccounts.get(i).getStatus();
            if (status.equals("active")) {
                System.out.println("********** active account number: " + savingAccounts.get(i).getAccountNumber());
                accumulatedDailyInterest = savingAccounts.get(i).getAccumDailyInterest();
                availableBalance = savingAccounts.get(i).getAvailableBalance();
                newAvailableBalance = accumulatedDailyInterest.add(availableBalance);

                dailyBalance = (ArrayList<BigDecimal>) savingAccounts.get(i).getDailyBalance();
                System.out.println("*********** daily balance array list: " + dailyBalance);
                System.out.println("*********** daily balance list size:  " + dailyBalance.size());

                for (BigDecimal k : dailyBalance) {
                    sumOfDailyBalance = sumOfDailyBalance.add(k);
                }

                System.out.println("********** Sum of daily balance: " + sumOfDailyBalance);
                averageDailyBalance = sumOfDailyBalance.divide(BigDecimal.valueOf(dailyBalance.size()), RoundingMode.HALF_UP);
                System.out.println("********** Average of daily balance: " + averageDailyBalance);

                savingAccountType = savingAccounts.get(i).getSavingAccountType().getId();
                System.out.println("***** saving account type is: " + savingAccountType);

                if (savingAccountType == 11) {
                    //additional 0.25% interest if 
                    //check transaction record 
                    System.out.println("********** You are inside the addtional interest checking loop");
                    savingAccount = savingAccounts.get(i).getAccountNumber();
                    transactionRecord = getTransactionRecord(savingAccount);
                    System.out.println("********** transaction record: " + transactionRecord);

                    if (transactionRecord.isEmpty()) {
                        System.out.println("********** transaction record is empty ********** ");
                        flag1 = 0;
                        flag2 = 0;
                        savingAccounts.get(i).setAvailableBalance(newAvailableBalance);
                        em.flush();
                        savingAccounts.get(i).setAccumDailyInterest(BigDecimal.ZERO);
                        em.flush();
                    } else {
                        System.out.println("********** hehe transaction record: " + transactionRecord);
                        //b.No withdraw within the month, check code with CW
                        for (int j = 0; j < transactionRecord.size(); j++) {
                            //no cash withdraw   
                            if (transactionRecord.get(j).getCode().equals("CD")) {
                                System.out.println("The transaction CODE is: " + transactionRecord.get(j).getCode());
                                transactionAmount = transactionAmount.add((BigDecimal) transactionRecord.get(j).getCredit());
                                System.out.println("1. The crediting amount to the saving account is: " + transactionAmount);

                            } else if ((transactionRecord.get(j).getCode().equals("TF"))) {
                                System.out.println("The transaction CODE is: " + transactionRecord.get(j).getCode());

                                if (transactionRecord.get(j).getDebit() == null) {
                                    BigDecimal credit = (BigDecimal) transactionRecord.get(j).getCredit();
                                    transactionAmount = transactionAmount.add(credit);
                                    System.out.println("2. The crediting amount to the saving account is: " + transactionAmount);
                                } else {
                                    System.out.println("the TF is a debiting action");
                                }
                            } else if (transactionRecord.get(j).getCode().equals("CW")) {
                                flag2 = 1;  //there is cash withdraw    
                            } else {
                                System.out.println("The transaction is TFF");
                            }
                        }
                        if (flag2 == 0 && transactionAmount.compareTo(BigDecimal.ZERO) == 1) {
                            System.out.println("********** Inside the Flag checking IF loop **********");
                            //additional interest 0.25% 
                            additionalInterest = availableBalance.multiply(BigDecimal.valueOf(savingAccounts.get(i).getSavingAccountType().getInterestRate2()));
                            System.out.println("********** Additional interest is: " + additionalInterest);
                            newAvailableBalance.add(additionalInterest);
                            savingAccounts.get(i).setAvailableBalance(newAvailableBalance);
                            em.flush();
                            savingAccounts.get(i).setAccumDailyInterest(BigDecimal.ZERO);
                            em.flush();
                        } else {
                            System.out.println("********** Inside the Flag checking ELSE loop **********");
                            System.out.println("********** Grant the usual amount ");
                            savingAccounts.get(i).setAvailableBalance(newAvailableBalance);
                            em.flush();
                            savingAccounts.get(i).setAccumDailyInterest(BigDecimal.ZERO);
                            em.flush();
                        }
                    }
                    if (averageDailyBalance.compareTo(BigDecimal.valueOf(5000)) == -1) {
                        //deduct 5 dollar from the saving account and send a email 
                        System.out.println("********** the account has daily balance less than 5000");
                        availableBalance = savingAccounts.get(i).getAvailableBalance().subtract(BigDecimal.valueOf(5));
                        savingAccounts.get(i).setAvailableBalance(availableBalance);
                    }
                } else {
                    //if the account type is 12 || 13
                    System.out.println("********** Account type 12 and 13 have no additional interest");
                    savingAccounts.get(i).setAvailableBalance(newAvailableBalance);
                    em.flush();
                    savingAccounts.get(i).setAccumDailyInterest(BigDecimal.ZERO);
                    em.flush();

                    if (savingAccountType == 13) {
                        if (averageDailyBalance.compareTo(BigDecimal.valueOf(1000)) == -1) {
                            //deduct 5 dollar from the saving account and send a email 
                            System.out.println("********** the account has daily balance less than 1000");
                            availableBalance = savingAccounts.get(i).getAvailableBalance().subtract(BigDecimal.valueOf(5));
                            savingAccounts.get(i).setAvailableBalance(availableBalance);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void checkCustomerAge() {
        System.out.println("**************inside age checking");
        long savingAccountType;
        Date dateOfBirth;
        Query query1 = em.createQuery("SELECT a FROM SavingAccount a");
        List<SavingAccount> savingAccounts = new ArrayList(query1.getResultList());
        
        Query query2 = em.createQuery("SELECT b FROM SavingAccountType b");
        List<SavingAccountType> savingAccountTypes = new ArrayList(query2.getResultList());
        

        for (int i = 0; i < savingAccounts.size(); i++) {
            System.out.println("***** inside the loop *****");
            String status = savingAccounts.get(i).getStatus();
            if (status.equals("active")) {
                savingAccountType = savingAccounts.get(i).getSavingAccountType().getId();
                System.out.println("***** saving account type is: " + savingAccountType);
                
                if(savingAccountType == 12){
                    dateOfBirth = savingAccounts.get(i).getCustomer().getDateOfBirth();              
                    DateTime birthday = new DateTime(dateOfBirth);
                    Calendar cal = GregorianCalendar.getInstance();
                    DateTime today = new DateTime(cal.getTime());

                    Period period = new Period(today, birthday);
                    System.out.print(period.getYears() + " years, ");
                    System.out.print(period.getMonths() + " months, ");
                    
                    if(period.getYears() >= 25){
                        //change the youth account to everyday saving account
                        SavingAccountType type = new SavingAccountType();
                        type = savingAccountTypes.get(2);
                        System.out.println("********** saving account type is:" + type);
                        savingAccounts.get(i).setSavingAccountType(type);
                    }
                }
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