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
import DepositEntity.FixedDepositAccount;
import DepositEntity.FixedDepositRate;
import DepositEntity.SavingAccount;
import DepositEntity.TransferRecord;
import java.math.BigDecimal;
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
import java.util.GregorianCalendar;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import Exception.EmailNotSendException;
import Other.Session.sendEmail;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 *
 * @author shuyunhuang
 *
 */
@Stateless
public class FixedDepositAccountSessionBean implements FixedDepositAccountSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private FixedDepositAccount account;
    private BigDecimal savingBalance; //from manage bean
    private Long accountNum; //generate 
    private BigDecimal fixedAccountBalance; //from managed bean
    private String fixedDuration;//from managed bean
    //private BigDecimal amount; // amount the account should have 

    private static final Random RANDOM = new SecureRandom();
    private FixedDepositRate rate;

    private Double interestRate;
    private Long rateIdLong;
    private Integer rateIdInt;
    private Customer customer;
    private BigDecimal interest;

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    @Override
    public Long createFixedAccount(Long customerId, BigDecimal amount, Date dateOfStart, Date dateOfEnd, String duration) {

        customer = em.find(Customer.class, customerId);
        //generate and check account number
        accountNum = this.generateFixedDepositNumber();
        BigDecimal balance = new BigDecimal("0"); //initial balance 
        //find interest rate according to duration

        if (duration.equalsIgnoreCase("3")) {
            rateIdInt = 1;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class, rateIdLong);
        } else if (duration.equalsIgnoreCase("6")) {
            rateIdInt = 2;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class, rateIdLong);
        } else if (duration.equalsIgnoreCase("12")) {
            rateIdInt = 3;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class, rateIdLong);
        } else {
            rateIdInt = 4;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class, rateIdLong);
        }

        interestRate = rate.getInterestRate();

        account = new FixedDepositAccount(accountNum, amount, balance, dateOfStart, dateOfEnd, duration, "inactive", interestRate);
        em.persist(account);
        account.setCustomer(customer);

        List<FixedDepositAccount> fixedAccounts = new ArrayList<FixedDepositAccount>();
        //customer may alr have fixed acct
        if (customer.getFixedDepositeAccounts() == null) {
            fixedAccounts.add(account);
            customer.setFixedDepositeAccounts(fixedAccounts);
            em.persist(fixedAccounts);
        } else {//alr have fixed acct
            customer.getFixedDepositeAccounts().add(account);
        }

        em.persist(customer);
        em.flush();

        System.out.print("Demi" + account.getStartDate());
        System.out.println("Fixed Deposit account created successfullly");
        //for testting
       
        return accountNum;
    }
    
    @Override
    public Long createFixedDepositCounter(Long customerId, BigDecimal amount, Date dateOfStart, Date dateOfEnd, String duration){
        customer = em.find(Customer.class, customerId);
        //generate and check account number
        accountNum = this.generateFixedDepositNumber();
        BigDecimal balance = amount; 
        //find interest rate according to duration

        if (duration.equalsIgnoreCase("3")) {
            rateIdInt = 1;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class, rateIdLong);
        } else if (duration.equalsIgnoreCase("6")) {
            rateIdInt = 2;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class, rateIdLong);
        } else if (duration.equalsIgnoreCase("12")) {
            rateIdInt = 3;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class, rateIdLong);
        } else {
            rateIdInt = 4;
            rateIdLong = rateIdInt.longValue();
            rate = em.find(FixedDepositRate.class, rateIdLong);
        }

        interestRate = rate.getInterestRate();

        account = new FixedDepositAccount(accountNum, amount, balance, dateOfStart, dateOfEnd, duration, "active", interestRate);
        em.persist(account);
        account.setCustomer(customer);

        List<FixedDepositAccount> fixedAccounts = new ArrayList<FixedDepositAccount>();
        //customer may alr have fixed acct
        if (customer.getFixedDepositeAccounts() == null) {
            fixedAccounts.add(account);
            customer.setFixedDepositeAccounts(fixedAccounts);
            em.persist(fixedAccounts);
        } else {//alr have fixed acct
            customer.getFixedDepositeAccounts().add(account);
        }

        em.persist(customer);
        em.flush();
        return accountNum; 
    }
    
    private long generateFixedDepositNumber() {
        int a = 1;
        Random rnd = new Random();
        int number = 10000000 + rnd.nextInt(90000000);
        Long accountNumber = Long.valueOf(number);
        Query q2 = em.createQuery("SELECT c.accountNumber FROM FixedDepositAccount c");
        List<Long> existingAcctNum = new ArrayList(q2.getResultList());
        while (a == 1) {

            if ((existingAcctNum.contains(accountNumber)) || (number / 10000000 == 0)) {
                number = 10000000 + rnd.nextInt(90000000);
                accountNumber = Long.valueOf(number);
                a = 1;
            } else {
                a = 0;
            }
        }

        return accountNumber;
    }

    public Long createFixedAccount(String ic, BigDecimal amount, Date dateOfStart, Date dateOfEnd, String duration) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean transferToFixedDeposit(Long savingAccountNum, Long fixedDepositAccountNum, BigDecimal amount, Long customerId) {
        BigDecimal accountBalance;
        BigDecimal updateSavingAccountBalance;

        //check the saving account bablance
        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNum");
        q.setParameter("savingAccountNum", savingAccountNum);
        List<SavingAccount> savingAccountLists = q.getResultList();
        SavingAccount savingAccounts = savingAccountLists.get(0);
        //for testing
        System.out.print("saving balance =" + savingAccounts.getAvailableBalance().toString());

        accountBalance = savingAccounts.getAvailableBalance();

        //if balance < amount, rertun false, else return true
        if (accountBalance.compareTo(amount) == -1) {
            System.out.print("saving account not enough balance");
            return false;
        } else {
            //proceed to transfer
            //no need to check the status of the acct here
            FixedDepositAccount fixedDepositAccount = this.getAccount(fixedDepositAccountNum);

            //update the balance of the saving account (-)
            updateSavingAccountBalance = accountBalance.subtract(amount);
            savingAccounts.setAvailableBalance(updateSavingAccountBalance);

            //update the balance of the fixed deposit account (+)         
            fixedDepositAccount.setBalance(amount);
            System.out.print("transfer to fixed done");
            //      em.persist(customer);
            //create a transaction record for saving account
            String description1 = "Transfer to fixed deposit " + fixedDepositAccountNum;
            Date currentTime = Calendar.getInstance().getTime();
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());
            TransferRecord transferRecord = new TransferRecord("TFF", amount, "settled", description1, currentTimestamp, savingAccountNum, null, "intraTransfer", "MerlionBank", "MerlionBank");
            em.persist(transferRecord);
            em.flush();
            System.out.println("Fixed Deposit account transferred successfullly");
            return true;
        }

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
        em.flush();
    }
    
    @Override
    public void logStaffAction(String description, Long customerId, Staff staff){
        List<StaffAction> actions = new ArrayList<>();
        StaffAction action = new StaffAction(Calendar.getInstance().getTime(),description,customerId,staff);
        em.persist(action);
        System.out.print(action.getDescription());
        if(staff.getStaffActions() == null){
            actions.add(action);
            staff.setStaffActions(actions);
            em.persist(actions);
        }else{
            staff.getStaffActions().add(action);
        }
        em.flush();
    }

    //shows active and inactive fixed accts for display page
    @Override
    public List<FixedDepositAccount> getFixedDepositAccounts(Long customerId) {
        customer = em.find(Customer.class, customerId);
        List<FixedDepositAccount> fixedDeposits = new ArrayList<>();
        for (int i = 0; i < customer.getFixedDepositeAccounts().size(); i++) {
            if (!customer.getFixedDepositeAccounts().get(i).getStatus().equals("terminated")) {
                fixedDeposits.add(customer.getFixedDepositeAccounts().get(i));
            }
        }
        return fixedDeposits;
    }

    @Override
    public List<Long> getNoMoneyFixedDeposit(Long customerId) {
        customer = em.find(Customer.class, customerId);
        List<Long> fixedDepositsAcctNum = new ArrayList<>();
        for (int i = 0; i < customer.getFixedDepositeAccounts().size(); i++) {
            account = customer.getFixedDepositeAccounts().get(i);
            if (account.getStatus().equals("inactive")) {
                fixedDepositsAcctNum.add(account.getAccountNumber());
            }
        }
        return fixedDepositsAcctNum;
    }

    @Override
    public BigDecimal amountLessBalance(Long accountNum) {
        account = this.getAccount(accountNum);
        System.out.print("balance"+account.getBalance());
        if(account.getBalance().equals(BigDecimal.valueOf(0))){
            return account.getAmount();
        }
        else if((account.getAmount().subtract(account.getBalance())).compareTo(BigDecimal.valueOf(0)) == 1) {
            System.out.print("balance =" +account.getBalance());
            System.out.print("needed =" +account.getAmount().subtract(account.getBalance()));
            return account.getAmount().subtract(account.getBalance());
        } else {
           System.out.print("this is wrong");
           return BigDecimal.valueOf(0);
            
        }
    }

    @Override
    public List<String> formatDate(Long accountNum) {
        account = this.getAccount(accountNum);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        List<String> datesString = new ArrayList<>();
        datesString.add(0, df.format(account.getStartDate()));
        datesString.add(1, df.format(account.getEndDate()));
        return datesString;

    }

    @Override
    public BigDecimal getAmount(Long accountNum) {
        return this.getAccount(accountNum).getAmount();
    }

    @Override
    public BigDecimal getBalance(Long accountNum) {
        return this.getAccount(accountNum).getBalance();
    }

    @Override
    public FixedDepositAccount getAccount(Long accountNum) {
        FixedDepositAccount accountA = new FixedDepositAccount();
        Query m = em.createQuery("SELECT b FROM FixedDepositAccount b WHERE b.accountNumber = :fixedDepositAccountNum");
        m.setParameter("fixedDepositAccountNum", accountNum);
        accountA = (FixedDepositAccount) m.getResultList().get(0);
        return accountA;
    }

    //withdraw to Merlion saving account
    @Override
    public BigDecimal earlyWithdraw(Long fixedAccountNum, Long savingAccountNum) {
        account = this.getAccount(fixedAccountNum);
        //interest rate for early withdraw
        rateIdInt = 5;
        rateIdLong = rateIdInt.longValue();
        rate = em.find(FixedDepositRate.class, rateIdLong);
        interestRate = rate.getInterestRate();
        interest = this.calculateInterestEarly(fixedAccountNum, interestRate);
        BigDecimal totalAmount = interest.add(account.getBalance());

        //credit principal and interest to savingaccount
        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNum");
        q.setParameter("savingAccountNum", savingAccountNum);
        SavingAccount savingAccount = (SavingAccount) q.getResultList().get(0);
        System.out.print(savingAccount.getAvailableBalance().toString());
        BigDecimal newBalance = savingAccount.getAvailableBalance().add(interest);
        newBalance = newBalance.add(account.getBalance());
        savingAccount.setAvailableBalance(newBalance);
        System.out.print(savingAccount.getAvailableBalance().toString());
        //close fixed deposit
        account.setBalance(BigDecimal.valueOf(0));
        account.setStatus("terminated");
        em.flush();
        //create a transaction record for saving account
        String description1 = "Withdraw fixed deposit " + fixedAccountNum;
        Date currentTime = Calendar.getInstance().getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());
        TransferRecord transferRecord = new TransferRecord("TFF", totalAmount, "settled", description1, currentTimestamp, null, savingAccountNum, "intraTransfer", "MerlionBank", "MerlionBank");
        em.persist(transferRecord);
        em.flush();
        System.out.println("Fixed Deposit account transferred successfullly");
        return interest;
    }
    
    @Override
    public List<BigDecimal> earlyWithdrawCounter(Long fixedAccountNum){
      account = this.getAccount(fixedAccountNum);
        //interest rate for early withdraw
       List<BigDecimal> amountsToDisplay = new ArrayList<>();
       
        rateIdInt = 5;
        rateIdLong = rateIdInt.longValue();
        rate = em.find(FixedDepositRate.class, rateIdLong);
        interestRate = rate.getInterestRate();
        interest = this.calculateInterestEarly(fixedAccountNum, interestRate);
        amountsToDisplay.add(0, account.getBalance());
        amountsToDisplay.add(1,interest);
        BigDecimal totalAmount = interest.add(account.getBalance());
        amountsToDisplay.add(2,totalAmount);       
//        //close fixed deposit
        account.setBalance(BigDecimal.valueOf(0));
        account.setStatus("terminated");
        em.flush();
        return amountsToDisplay;
    }

    @Override
    public void normalWithdrawTakeEffect(Long fixedAccountNum, Long savingAccountNum) {
        account = this.getAccount(fixedAccountNum);
        interest = this.calculateInterestNormal(fixedAccountNum);
        BigDecimal totalAmount = interest.add(account.getBalance());
        //credit principal and interest to saving account
        Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccountNum");
        q.setParameter("savingAccountNum", savingAccountNum);
        SavingAccount savingAccount = (SavingAccount) q.getResultList().get(0);
        System.out.print(savingAccount.getAvailableBalance().toString());
        BigDecimal newBalance = savingAccount.getAvailableBalance().add(interest);
        newBalance = newBalance.add(account.getBalance());
        savingAccount.setAvailableBalance(newBalance);
        System.out.print(savingAccount.getAvailableBalance().toString());
        //close fixed deposit
        account.setBalance(BigDecimal.valueOf(0));
        System.out.print("set balance to 0");
        account.setStatus("terminated");
        System.out.print("set status to termianted");
        em.flush();
        //create transaction record
        String description1 = "Withdraw fixed deposit " + fixedAccountNum;
        Date currentTime = Calendar.getInstance().getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());
        TransferRecord transferRecord = new TransferRecord("TFF", totalAmount, "settled", description1, currentTimestamp, null, savingAccountNum, "intraTransfer", "MerlionBank", "MerlionBank");
        em.persist(transferRecord);
        em.flush();
    }
    
    @Override
    public List<BigDecimal> normalWithdrawCounter(Long fixedAccountNum){
        account = this.getAccount(fixedAccountNum);
        interest = this.calculateInterestNormal(fixedAccountNum);
        BigDecimal totalAmount = interest.add(account.getBalance());
        List<BigDecimal> amountsToDisplay = new ArrayList<>();
        amountsToDisplay.add(0,account.getBalance());
        amountsToDisplay.add(1,interest);
        amountsToDisplay.add(2,totalAmount);
        
        //close fixed deposit
        account.setBalance(BigDecimal.valueOf(0));
        System.out.print("set balance to 0");
        account.setStatus("terminated");
        System.out.print("set status to termianted");
        em.flush();
        return amountsToDisplay;
    }

    @Override
    public void normalWithdrawMark(Long fixedAccountNum, Long savingAccountNum) {
        account = this.getAccount(fixedAccountNum);
        String temp = "normalWithdraw," + savingAccountNum.toString();
        account.setStatus(temp);
    }

    @Override
    public BigDecimal calculateInterestEarly(Long accountNum, Double interestRate1) {
        account = this.getAccount(accountNum);
        BigDecimal principal = account.getBalance();
        //compute how many days has passed
        Calendar cal = GregorianCalendar.getInstance();
        DateTime today = new DateTime(cal.getTime());
        DateTime startDay = new DateTime(account.getStartDate());
        Days inBetween = Days.daysBetween(startDay, today);
        int numOfDays = inBetween.getDays();
        System.out.print("number of days = " + numOfDays);
        BigDecimal interestAmount = principal.multiply(new BigDecimal((numOfDays / (double) 365) * interestRate1));
        System.out.print("interest amount =" + interestAmount);
        interestAmount.setScale(4, RoundingMode.HALF_UP);
        return interestAmount;
    }

    @Override
    public List<Long> getWithdrawable(Long customerId) {
        customer = em.find(Customer.class, customerId);
        List<Long> fixedDepositsAcctNum = new ArrayList<>();
        for (int i = 0; i < customer.getFixedDepositeAccounts().size(); i++) {
            account = customer.getFixedDepositeAccounts().get(i);
            //active and renew and normal withdraw accounts can be withdraw
            if (account.getStatus().equalsIgnoreCase("active") || account.getStatus().equalsIgnoreCase("renew") || account.getStatus().contains(",")) {
                fixedDepositsAcctNum.add(account.getAccountNumber());
            }
        }
        return fixedDepositsAcctNum;
    }
    
    @Override
    public List<FixedDepositAccount> getWithdrawableAccount(Long customerId){
    customer = em.find(Customer.class, customerId);
        List<FixedDepositAccount> withdrawable = new ArrayList<>();
        for (int i = 0; i < customer.getFixedDepositeAccounts().size(); i++) {
            account = customer.getFixedDepositeAccounts().get(i);
            //active and renew and normal withdraw accounts can be withdraw
            if (account.getStatus().equalsIgnoreCase("active") || account.getStatus().equalsIgnoreCase("renew") || account.getStatus().contains(",")) {
                withdrawable.add(account);
            }
        }
        return withdrawable;    
    }

    @Override
    public List<Long> getRenewable(Long customerId) {
        customer = em.find(Customer.class, customerId);
        List<Long> fixedDepositsAcctNum = new ArrayList<>();
        for (int i = 0; i < customer.getFixedDepositeAccounts().size(); i++) {
            account = customer.getFixedDepositeAccounts().get(i);
            //active and normal withdraw accts can be renewed
            if (account.getStatus().equalsIgnoreCase("active") || account.getStatus().contains(",")) {
                fixedDepositsAcctNum.add(account.getAccountNumber());
            }
        }
        return fixedDepositsAcctNum;
    }

    @Override
    public void renewFixed(Long accountNumber) {
        account = this.getAccount(accountNumber);
        account.setStatus("renew");
    }

    @Override
    public List<String> getRenewDates(Long accountNumber) {
        account = this.getAccount(accountNumber);
        String duration = account.getDuration();
        Date endOld = account.getEndDate();
        DateTime endOldTemp = new DateTime(endOld);
        DateTime startNewTemp = endOldTemp.plus(Period.days(1));
        DateTime endNewTemp = new DateTime();
        if (duration.equals("3")) {
            endNewTemp = startNewTemp.plus(Period.months(3));
        } else if (duration.equals("6")) {
            endNewTemp = startNewTemp.plus(Period.months(6));
        } else if (duration.equals("12")) {
            endNewTemp = startNewTemp.plus(Period.months(12));
        } else {
            endNewTemp = startNewTemp.plus(Period.months(24));
        }
        Date startNew = startNewTemp.toDate();
        Date endNew = endNewTemp.toDate();
        List<String> dates = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        dates.add(0, this.formatDate(accountNumber).get(0));
        dates.add(1, this.formatDate(accountNumber).get(1));
        dates.add(2, df.format(startNew));
        dates.add(3, df.format(endNew));
        return dates;
    }
    @Override
    public BigDecimal calculateInterestNormal(Long accountNum) {
        account = this.getAccount(accountNum);
        interestRate = account.getInterestRate();
        BigDecimal principal = account.getBalance();
        Integer durationInt = Integer.valueOf(account.getDuration());
        BigDecimal interestAmount = principal.multiply(new BigDecimal((durationInt / (double) 12) * interestRate));
        interestAmount.setScale(4, RoundingMode.HALF_UP);
        return interestAmount;
    }
    @Override
    public void checkFixedDepositAccountStatus() {

        Calendar sevenDayToStart = GregorianCalendar.getInstance(); //current date
        Calendar threeDayToEnd = GregorianCalendar.getInstance();
        Calendar dayOfMature = GregorianCalendar.getInstance();
        //for testing purpose
        //+7 for testing purpose     
        sevenDayToStart.add(Calendar.DATE, 0);
        Date dateToday1 = sevenDayToStart.getTime();
        String newstring1 = new SimpleDateFormat("yyyy-MM-dd").format(dateToday1);
        System.out.println(newstring1);

        //3 days before account mature date
        //to send email
        threeDayToEnd.add(Calendar.DATE, 3);
        Date dateToday2 = threeDayToEnd.getTime();
        String newstring2 = new SimpleDateFormat("yyyy-MM-dd").format(dateToday2);
        System.out.println(newstring2);

        //date of mature
        //check date, and status
        dayOfMature.add(Calendar.DATE, 0);
        Date dateToday3 = dayOfMature.getTime();
        String newstring3 = new SimpleDateFormat("yyyy-MM-dd").format(dateToday3);
        System.out.println(newstring3);

        Query query1 = em.createQuery("SELECT a FROM FixedDepositAccount a");
        List<FixedDepositAccount> fixedDepositAccounts = new ArrayList(query1.getResultList());

        for (int i = 0; i < fixedDepositAccounts.size(); i++) {
            Date startDate = fixedDepositAccounts.get(i).getStartDate();
            String accountStartDay = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            Date endDate = fixedDepositAccounts.get(i).getEndDate();
            String accountEndDay = new SimpleDateFormat("yyyy-MM-dd").format(endDate);

            //check either to activate / terminate account 
            if (accountStartDay.equals(newstring1)) {
                BigDecimal balance = fixedDepositAccounts.get(i).getBalance();
                BigDecimal amount = fixedDepositAccounts.get(i).getAmount();
                //balance >= amount
                BigDecimal difference = balance.subtract(amount);
                if (difference.compareTo(BigDecimal.ZERO) >= 0) {
                    fixedDepositAccounts.get(i).setStatus("active");
                    em.flush();
                } else {
                    fixedDepositAccounts.get(i).setStatus("terminated");
                    em.flush();
                }
            } //3 days before, check whether to send the email to notify renew
            else if (accountEndDay.equals(newstring2) && fixedDepositAccounts.get(i).getStatus().equals("active")) {
                System.out.println("hey, you are here !!!!");
                String customerName = fixedDepositAccounts.get(i).getCustomer().getName();
                String email = fixedDepositAccounts.get(i).getCustomer().getEmail();
                Long fixedDepositAccountNum = fixedDepositAccounts.get(i).getAccountNumber();
                Date accountMatureDate = fixedDepositAccounts.get(i).getEndDate();

                try {
                    SendFixedDepositAccountMatureEmail(customerName, email, fixedDepositAccountNum, accountMatureDate);
                } catch (MessagingException ex) {
                    System.out.println("Error sending email.");
                    try {
                        throw new EmailNotSendException("Error sending email.");
                    } catch (EmailNotSendException ex1) {
                        Logger.getLogger(FixedDepositAccountSessionBean.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
                // at the end day, "active","renew","withdrawl,account number"
            } else if (accountEndDay.equals(newstring3)) {
                if (fixedDepositAccounts.get(i).getStatus().equals("renew") || fixedDepositAccounts.get(i).getStatus().equals("active")) {
                    System.out.println("get it renew pls !!!!");
     
                    String duration = fixedDepositAccounts.get(i).getDuration();
                    Date endOld = fixedDepositAccounts.get(i).getEndDate();
                    DateTime endOldTemp = new DateTime(endOld);
                    DateTime startNewTemp = endOldTemp.plus(Period.days(1));
                    DateTime endNewTemp = new DateTime();
                    if (duration.equals("3")) {
                        endNewTemp = startNewTemp.plus(Period.months(3));
                    } else if (duration.equals("6")) {
                        endNewTemp = startNewTemp.plus(Period.months(6));
                    } else if (duration.equals("12")) {
                        endNewTemp = startNewTemp.plus(Period.months(12));
                    } else {
                        endNewTemp = startNewTemp.plus(Period.months(24));
                    }
                    System.out.print("Dates calculated");
                    BigDecimal interestEnd = calculateInterestNormal(fixedDepositAccounts.get(i).getAccountNumber());
                    BigDecimal newBalance = fixedDepositAccounts.get(i).getBalance().add(interestEnd);
                    fixedDepositAccounts.get(i).setBalance(newBalance);
                    System.out.print("new balance is **************" + newBalance);
                    em.flush();
                    System.out.print("balance set");
                    fixedDepositAccounts.get(i).setStartDate(startNewTemp.toDate()); //set new start and end date
                    em.flush();
                    fixedDepositAccounts.get(i).setEndDate(endNewTemp.toDate());
                    em.flush();
                    fixedDepositAccounts.get(i).setStatus("active");
                    em.flush();
                    System.out.print("new dates set");
                } else if(fixedDepositAccounts.get(i).getStatus().contains(",")){

                    //"withdrawl,account number"
                    //terminate the fixed deposit account 
                    //deposit the saving account
                    String status = fixedDepositAccounts.get(i).getStatus();
                    System.out.println("****** status" + status);
                    String[] statusAccountNum = status.split(",");
                    String accountNumber = statusAccountNum[1]; //account number
                    System.out.println("************************** split split !!!");
                    System.out.println(accountNumber);
                    normalWithdrawTakeEffect(fixedDepositAccounts.get(i).getAccountNumber(), Long.parseLong(accountNumber));
                }
            } else {
                System.out.println("no thing to set !!!!!");
            }
        }
    }

    // haha 
    private void SendFixedDepositAccountMatureEmail(String name, String email, Long accountNum, Date matureDate) throws MessagingException {
        String subject = "Merlion Bank - " + name + "Fixed Deposit Account Maturity Notification";
        System.out.println("Inside send Fixed Deposit Account Maturity email");
        System.out.println("the email address is " + email);
        String content = "<h2>Dear " + name
                + ",</h2><br /><h1> Your fixed deposit account will be matured in 3 days.</h1><br />"
                + "<h1>Fixed Deposit Account Number :" + accountNum + "</h1>"
                + "<br />This email is to notify you that your Fixed Deposit Account" + accountNum + "</h2><br />"
                + "<br /><p>will be matured on " + matureDate + " Please be noted that the account will be auto renew with the same duration</p >"
                + "<br /><p>if no further instruction is given.</p >"
                + "<p>Thank you.</p ><br /><br /><p>Regards,</p ><p>MerLION Platform User Support</p >";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

////    @Override
//    public List<FixedDepositRate> getFixedDepositRate() {
//        Double fixedDepositRate;
//        List<String> fixedDepositRateString = new ArrayList<String>();
////         Query q = em.createNamedQuery("SELECT a FROM FixedDepositRate a");
////         List<FixedDepositRate> fixedDepositRates = q.getResultList();
////         
////         if(fixedDepositRates.isEmpty()){
////            System.out.print("The accountType Table is Empty");
////    }else {
////             System.out.println(fixedDepositRates.size());
////             for(int i = 0; i < fixedDepositRates.size(); i ++){
////                 fixedDepositRate = fixedDepositRates.get(i).getInterestRate();
////                 System.out.print(fixedDepositRates.get(i).getInterestRate());
////                 fixedDepositRateString.add(fixedDepositRate.toString());
////             }
////         }
//         return fixedDeositRateString;
////    }
}
