/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import DepositEntity.FixedDepositAccount;
import DepositEntity.FixedDepositRate;
import DepositEntity.SavingAccount;
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
    private BigDecimal interest;

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public Long createFixedAccount(Long customerId, BigDecimal amount, Date dateOfStart, Date dateOfEnd, String duration) {

        customer = em.find(Customer.class, customerId);
        //generate and check account number
        accountNumber = Math.round(Math.random() * 99999999);
        int a = 1;
        while (a == 1) {
            Query q2 = em.createQuery("SELECT c.accountNumber FROM FixedDepositAccount c");
            List<Long> existingAcctNum = new ArrayList(q2.getResultList());

            if (!existingAcctNum.isEmpty()) {
                if (existingAcctNum.contains(accountNumber)) {
                    accountNumber = Math.round(Math.random() * 999999999);
                } else {
                    a = 0;
                }
            }
            a = 0;
        }

        BigDecimal balance = new BigDecimal("0.0000"); //initial balance 
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
        account = new FixedDepositAccount(accountNumber, amount, balance, dateOfStart, dateOfEnd, duration, "inactive", interestRate);

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

        //log an action
        String description = "Create a new Fixed deposit account " + accountNumber;
        this.logAction(description, customerId);
        em.persist(customer);
        em.flush();
        System.out.print("Demi" + account.getStartDate());
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
        savingAccounts.setAvailableBalance(BigDecimal.valueOf(10000));
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
            String description = "Transfer from saving account " + savingAccountNum + " to fixed deposit " + fixedDepositAccountNum;
            this.logAction(description, customerId);
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
        em.persist(customer);
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
        if ((account.getAmount().subtract(account.getBalance())).compareTo(BigDecimal.valueOf(0)) == 1) {
            return account.getAmount().subtract(account.getBalance());
        } else {
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
    public void earlyWithdraw(Long fixedAccountNum, Long savingAccountNum) {
        account = this.getAccount(fixedAccountNum);
        //interest rate for early withdraw
        rateIdInt = 5;
        rateIdLong = rateIdInt.longValue();
        rate = em.find(FixedDepositRate.class, rateIdLong);
        interestRate = rate.getInterestRate();
        //no interest if account is not active
        if (account.getStatus().equals("active") || account.getStatus().equals("renew")) {
            interest = this.calculateInterestEarly(fixedAccountNum, interestRate);
        } else {
            interest = BigDecimal.valueOf(0);
        }

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
        return interestAmount;
    }

    @Override
    public List<Long> getWithdrawable(Long customerId) {
        customer = em.find(Customer.class, customerId);
        List<Long> fixedDepositsAcctNum = new ArrayList<>();
        for (int i = 0; i < customer.getFixedDepositeAccounts().size(); i++) {
            account = customer.getFixedDepositeAccounts().get(i);
            if (account.getStatus().equals("inactive") || account.getStatus().equals("active")) {
                fixedDepositsAcctNum.add(account.getAccountNumber());
            }
        }
        return fixedDepositsAcctNum;
    }

    @Override
    public List<Long> getRenewable(Long customerId) {
        customer = em.find(Customer.class, customerId);
        List<Long> fixedDepositsAcctNum = new ArrayList<>();
        for (int i = 0; i < customer.getFixedDepositeAccounts().size(); i++) {
            account = customer.getFixedDepositeAccounts().get(i);
            if (account.getStatus().equals("active")) {
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

    public BigDecimal calculateInterestNormal(Long accountNum) {
        account = this.getAccount(accountNum);
        interestRate = account.getInterestRate();
        BigDecimal principal = account.getBalance();
        Integer durationInt = Integer.valueOf(account.getDuration());
        BigDecimal interestAmount = principal.multiply(new BigDecimal((durationInt / (double) 12) * interestRate));
        return interestAmount;
    }

    @Override
    public void checkFixedDepositAccountStatus() {

        Calendar sevenDayToStart = GregorianCalendar.getInstance(); //current date
        Calendar threeDayToEnd = GregorianCalendar.getInstance();
        Calendar dayOfMature = GregorianCalendar.getInstance();
        //for testing purpose
        //+7 for testing purpose     
        sevenDayToStart.add(Calendar.DATE, 5);
        Date dateToday1 = sevenDayToStart.getTime();
        String newstring1 = new SimpleDateFormat("yyyy-MM-dd").format(dateToday1);
        System.out.println(newstring1);

        //3 days before account mature date
        //to send email
        threeDayToEnd.add(Calendar.DATE, 95);
        Date dateToday2 = threeDayToEnd.getTime();
        String newstring2 = new SimpleDateFormat("yyyy-MM-dd").format(dateToday2);
        System.out.println(newstring2);

        //date of mature
        //check date, and status
        dayOfMature.add(Calendar.DATE, 98);
        Date dateToday3 = threeDayToEnd.getTime();
        String newstring3 = new SimpleDateFormat("yyyy-MM-dd").format(dateToday3);
        System.out.println(newstring3);

        Query query1 = em.createQuery("SELECT a FROM FixedDepositAccount a");
        List<FixedDepositAccount> fixedDepositAccounts = new ArrayList(query1.getResultList());

        for (int i = 0; i < fixedDepositAccounts.size(); i++) {
            Date startDate = fixedDepositAccounts.get(i).getStartDate();
            String accountStartDay = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            Date endDate = fixedDepositAccounts.get(i).getEndDate();
            String accountEndDay = new SimpleDateFormat("yyyy-MM-dd").format(endDate);

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
            } else if (accountEndDay.equals(newstring2) && fixedDepositAccounts.get(i).getStatus().equals("active")) {
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
            } else if (accountEndDay.equals(newstring3) && fixedDepositAccounts.get(i).getStatus().equals("renew")) {
                System.out.println("get it renew pls !!!!");
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
                //set new start and end date
                fixedDepositAccounts.get(i).setStartDate(startNewTemp.toDate());
                em.flush();
                fixedDepositAccounts.get(i).setEndDate(endNewTemp.toDate());
                em.flush();
                // BigDecimal interest = 
            } else {
                System.out.println("no thing to set !!!!!");
            }
        }
    }

    private void SendFixedDepositAccountMatureEmail(String name, String email, Long accountNum, Date matureDate) throws MessagingException {
        String subject = "Merlion Bank - " + name + "Fixed Deposit Account Maturity Notification";
        System.out.println("Inside send Fixed Deposit Account Maturity email");
        System.out.println("the email address is " + email);
        String content = "<h2>Dear " + name
                + ",</h2><br /><h1> Your fixed deposit account will be matured in 3 days " + name + " Saving Account!</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<br />This email is to notify you that your Fixed Deposit Account" + accountNum + "</h2><br />"
                + "<br /><p>will be matured on," + matureDate + " Please be noted that the account will be auto renew with the same duration</p >"
                + "<br /><p>if no further instruction is given.</p >"
                + "<p>Thank you.</p ><br /><br /><p>Regards,</p ><p>MerLION Platform User Support</p >";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

}


