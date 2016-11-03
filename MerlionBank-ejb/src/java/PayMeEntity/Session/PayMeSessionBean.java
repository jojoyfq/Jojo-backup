/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PayMeEntity.Session;

import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
import Exception.PasswordNotMatchException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import PayMeEntity.PayMe;
import PayMeEntity.PayMeTransaction;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Bella
 */
@Stateless
public class PayMeSessionBean implements PayMeSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private static final Random RANDOM = new SecureRandom();
    public static final int SALT_LENGTH = 32;

    @Override
    public boolean checkLogin(String ic, String password) throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (temp.isEmpty()) {
            System.out.println("Username " + ic + " does not exist!");
            throw new UserNotExistException("Username " + ic + " does not exist, please try again");
        }

        int size = temp.size();
        Customer customer = temp.get(size - 1);
        if (customer.getStatus().equals("terminated")) {
            System.out.println("Username " + ic + " does not exist!");
            throw new UserNotExistException("Username " + ic + " does not exist, please try again");
        } else if (customer.getStatus().equals("inactive")) {
            System.out.println("Username " + ic + " please activate your account!");

            throw new UserNotActivatedException("Username " + ic + " please activate your account!");

        } else if (customer.getOnlineAccount().getAccountStatus().equals("locked")) {
            System.out.println("Username " + ic + " Account locked! Please Reset Password!");
            throw new UserNotExistException("Username " + ic + " Account locked! Please Reset Password!");

        } else if (customer.getStatus().equals("unverified")) {
            System.out.println("Username " + ic + "Please wait for your account to be verified!");
            throw new UserNotExistException("Username " + ic + "Please wait for your account to be verified!");

        } else {
            System.out.println("Username " + ic + " IC check pass!");
        }

        System.out.println("inside session bean: " + password);
        if (!passwordHash(password + customer.getOnlineAccount().getSalt()).equals(customer.getOnlineAccount().getPassword())) {
            System.out.println("Go in password not match");
            throw new PasswordNotMatchException("password does not match!");
        }
        return true;

    }

    @Override
    public boolean checkPayMeLogin(String phoneNumber, String password) {
        String phone;
        if (phoneNumber.substring(0, 1).equals("+")) {
            phone = phoneNumber;
        } else {
            phone = "+" + phoneNumber;
        }
        Query q = em.createQuery("SELECT a FROM PayMe a WHERE a.phoneNumber = :phoneNumber");
        q.setParameter("phoneNumber", phone);
        PayMe payme = (PayMe) q.getSingleResult();
        if (passwordHash(password + payme.getSalt()).equals(payme.getPaymePassword())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean sendTwoFactorAuthentication(String ic) throws TwilioRestException {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        Customer customer = temp.get(temp.size() - 1);
        String ACCOUNT_SID = "AC0607da3843c85473703f3f5078a21a52";
        String AUTH_TOKEN = "79993c87540fe09ba96d8f1990d78eed";
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        Account account = client.getAccount();

        MessageFactory messageFactory = account.getMessageFactory();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", customer.getPhoneNumber())); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("From", "+12013451118")); // Replace with a valid phone number for your account.
        Long number = Math.round(Math.random() * 1000000);
        String oTP = "" + number;
        customer.getOnlineAccount().setAuthenticationCode(oTP);
        em.flush();
        params.add(new BasicNameValuePair("Body", oTP));

        try {
            Message message = messageFactory.create(params);
        } catch (TwilioRestException e) {
            System.out.println(e.getErrorMessage());
        }
        //Message sms = messageFactory.create(params);
        return true;
    }

    @Override
    public boolean verifyTwoFactorAuthentication(String ic, String inputCode) {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        Customer customer = (Customer) q.getSingleResult();

        if (customer.getOnlineAccount().getAuthenticationCode().equals(inputCode)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean createPayMe(String ic, String savingAccountNo, String phoneNumber, String paymePassword) {
        //get Customer Entity      
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        Customer customer = (Customer) q.getSingleResult();

        if (customer.getPayMe() == null) {

            //get Saving Account Entity
            Long savingAccount = Long.valueOf(savingAccountNo);
            System.out.println("Saving Account No is " + savingAccount);
            Query m = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :savingAccount");
            m.setParameter("savingAccount", savingAccount);
            SavingAccount temp = (SavingAccount) m.getSingleResult();

            //password salt and hash
            String salt = "";
            String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789@#$%^&*!?+-";
            for (int i = 0; i < SALT_LENGTH; i++) {
                int index = (int) (RANDOM.nextDouble() * letters.length());
                salt += letters.substring(index, index + 1);
            }
            String passwordDatabase = passwordHash(paymePassword + salt);
            System.out.println("PayMePassword after hash&salt:" + passwordDatabase);
            //set initial balance to 0
            BigDecimal balance = new BigDecimal("0.00");

            //Create New PayMe Account
            PayMe payMe = new PayMe(phoneNumber, customer, temp, balance, passwordDatabase, salt);
            em.persist(payMe);
            customer.setPayMe(payMe);
            em.persist(customer);
            temp.setPayMe(payMe);
            em.persist(temp);
            em.flush();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean topUp(String phoneNumber, String amount) {
        if (phoneNumber.substring(0, 1).equals("+") == false) {
            phoneNumber = "+" + phoneNumber;
        }
        Query q = em.createQuery("SELECT a FROM PayMe a WHERE a.phoneNumber = :phoneNumber");
        q.setParameter("phoneNumber", phoneNumber);
        PayMe payme = (PayMe) q.getSingleResult();

        System.out.println("the amount string is: " + amount + "*************");

        BigDecimal amountBD = new BigDecimal(amount);
        System.out.println("the BigDecimal amount string is: " + amountBD + "*********");

        if (payme.getSavingAccount().getAvailableBalance().compareTo(amountBD) == -1
                || (!payme.getSavingAccount().getStatus().equals("active"))) {
            return false;
        } else {
            //update the balance and available balance of saving account
            BigDecimal updatedAvailAmount = payme.getSavingAccount().getAvailableBalance().subtract(amountBD);
            payme.getSavingAccount().setAvailableBalance(updatedAvailAmount);
            BigDecimal updatedBalance = payme.getSavingAccount().getBalance().subtract(amountBD);
            payme.getSavingAccount().setBalance(updatedBalance);
            //update the payme balance
            payme.setBalance(payme.getBalance().add(amountBD));
            //get current time
            Date currentTime = Calendar.getInstance().getTime();
            PayMeTransaction paymeTransaction = new PayMeTransaction("TopUp", null, amountBD, currentTime, null,
                    payme.getSavingAccount().getAccountNumber(), payme);
            TransactionRecord transactionRecord = new TransactionRecord("PayMe", amountBD, null,
                    "settled", "TopUp PayMe Account", currentTime, payme.getSavingAccount().getAccountNumber(), null, payme.getSavingAccount(), "MerlionBank", "MerlionBank");
            payme.getPaymeTransaction().add(paymeTransaction);

            em.persist(paymeTransaction);
            em.persist(transactionRecord);
            em.persist(payme);
            em.flush();
            return true;
        }
    }

    @Override
    public boolean sendToMyAccount(String phoneNumber, String amount) {
        System.out.println("amount is: " + amount + "**********************");
        System.out.println("phoneNumber is: " + phoneNumber + "**********************");
        if (phoneNumber.substring(0, 1).equals("+") == false) {
            phoneNumber = "+" + phoneNumber;
        }

        Query q = em.createQuery("SELECT a FROM PayMe a WHERE a.phoneNumber = :phoneNumber");
        q.setParameter("phoneNumber", phoneNumber);
        PayMe payme = (PayMe) q.getSingleResult();
        BigDecimal amountBD = new BigDecimal(amount);

        Long savingAccountID = payme.getSavingAccount().getId();
        SavingAccount savingAccount = em.find(SavingAccount.class, savingAccountID);
        //if the saving account linked with payme is no longer valid
        if (savingAccount == null) {
            return false;
        } else if (payme.getBalance().compareTo(amountBD) == -1) {
            return false;
        } else {
            //update the balance and available balance of saving account
            BigDecimal updatedAvailAmount = payme.getSavingAccount().getAvailableBalance().add(amountBD);
            payme.getSavingAccount().setAvailableBalance(updatedAvailAmount);
            BigDecimal updatedBalance = payme.getSavingAccount().getBalance().add(amountBD);
            payme.getSavingAccount().setBalance(updatedBalance);
            //update the payme balance
            payme.setBalance(payme.getBalance().subtract(amountBD));
            //get current time
            Date currentTime = Calendar.getInstance().getTime();
            PayMeTransaction paymeTransaction = new PayMeTransaction("Send to My A/C", amountBD, null, currentTime, null,
                    payme.getSavingAccount().getAccountNumber(), payme);
            TransactionRecord transactionRecord = new TransactionRecord("PayMe", null, amountBD,
                    "settled", "PayMe Send to Account", currentTime, payme.getSavingAccount().getAccountNumber(), null, payme.getSavingAccount(), "MerlionBank", "MerlionBank");
            payme.getPaymeTransaction().add(paymeTransaction);
            em.persist(paymeTransaction);
            em.persist(transactionRecord);
            em.persist(payme);
            em.flush();
            return true;
        }
    }

    @Override
    public String payMeSent(String phoneNumber, String otherPhone, String amount) {
        if (phoneNumber.substring(0, 1).equals("+") == false) {
            phoneNumber = "+" + phoneNumber;
        }
        if (otherPhone.substring(0, 1).equals("+") == false) {
            otherPhone = "+" + otherPhone;
        }

        System.out.println("PhoneNumber is " + phoneNumber + "**********");
        System.out.println("otherPhone is " + otherPhone + "**********");

        Query q = em.createQuery("SELECT a FROM PayMe a WHERE a.phoneNumber = :phoneNumber");
        q.setParameter("phoneNumber", phoneNumber);
        PayMe payme = (PayMe) q.getSingleResult();
        BigDecimal amountBD = new BigDecimal(amount);

        Query m = em.createQuery("SELECT a FROM PayMe a WHERE a.phoneNumber = :otherPhone");
        m.setParameter("otherPhone", otherPhone);

        //if other payme number is not valid
        if (m.getResultList().isEmpty()) {
            return "Recipient does not exist";
        } else if (payme.getBalance().compareTo(amountBD) == -1) {
            return "Insufficient funds"; //payme account does not have enough balance
        } else {
            PayMe otherPayMe = (PayMe) m.getSingleResult();
            payme.setBalance(payme.getBalance().subtract(amountBD));
            em.persist(payme);
            otherPayMe.setBalance(otherPayMe.getBalance().add(amountBD));
            em.persist(otherPayMe);
            //get current time
            Date currentTime = Calendar.getInstance().getTime();
            PayMeTransaction paymeTransaction1 = new PayMeTransaction("Sent", amountBD, null, currentTime, otherPayMe.getPhoneNumber(),
                    null, payme);
            PayMeTransaction paymeTransaction2 = new PayMeTransaction("Received", null, amountBD, currentTime, payme.getPhoneNumber(),
                    null, otherPayMe);
            payme.getPaymeTransaction().add(paymeTransaction1);
            otherPayMe.getPaymeTransaction().add(paymeTransaction2);
            em.persist(paymeTransaction1);
            em.persist(paymeTransaction2);

            em.flush();
            return "Send Successful";
        }
    }

    @Override
    public List<List<String>> getPayMeTransaction(String phoneNumber) {
        List<List<String>> transactionRecords = new ArrayList<List<String>>();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String date;
        List<String> list = new ArrayList();

        if (phoneNumber.substring(0, 1).equals("+") == false) {
            phoneNumber = "+" + phoneNumber;
        }

        //find the corresponding payme from database
        Query q = em.createQuery("SELECT a FROM PayMe a WHERE a.phoneNumber = :phoneNumber");
        q.setParameter("phoneNumber", phoneNumber);
        PayMe payme = (PayMe) q.getSingleResult();
        List<PayMeTransaction> transactions = payme.getPaymeTransaction();

        if (transactions == null) {
            return null;
        }

        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getDescription().equals("TopUp")) {
                list.add("TopUp,");
                date = df.format(transactions.get(i).getTransactionTime());
                list.add(date + ",");
                list.add(payme.getPhoneNumber() + ",");
                list.add(transactions.get(i).getCredit().toString());
            } else if (transactions.get(i).getDescription().equals("Send to My A/C")) {
                list.add("Send to My A/C,");
                date = df.format(transactions.get(i).getTransactionTime());
                list.add(date + ",");
                list.add(payme.getPhoneNumber() + ",");
                list.add(transactions.get(i).getDebit().toString());
            } else if (transactions.get(i).getDescription().equals("Sent")) {
                list.add("Sent,");
                date = df.format(transactions.get(i).getTransactionTime());
                list.add(date + ",");
                list.add(transactions.get(i).getPhoneNumber() + ",");
                list.add(transactions.get(i).getDebit().toString());
            } else if (transactions.get(i).getDescription().equals("Received")) {
                list.add("Received,");
                date = df.format(transactions.get(i).getTransactionTime());
                list.add(date + ",");
                list.add(transactions.get(i).getPhoneNumber() + ",");
                list.add(transactions.get(i).getCredit().toString());
            } else {
                System.out.println("Error");
            }
            transactionRecords.add(list);
            list = new ArrayList();
        }
        System.out.print("How many Records: " + transactionRecords.size());
        return transactionRecords;
    }

    @Override
    public List<String> getSavingAccountString(String ic) throws UserHasNoSavingAccountException {
        List<String> savingAccountString = new ArrayList<String>();
        String savingAccountNo;
        String accountType;

        System.out.println("Customer ic is: " + ic + "******");

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        Customer customer = (Customer) q.getSingleResult();

        List<SavingAccount> savingAccounts = customer.getSavingAccounts();
        if (savingAccounts.isEmpty()) {
            throw new UserHasNoSavingAccountException("No Saving Account Found!");
        } else {
            for (int i = 0; i < savingAccounts.size(); i++) {
                savingAccountNo = Long.toString(savingAccounts.get(i).getAccountNumber());
                accountType = savingAccounts.get(i).getSavingAccountType().getAccountType();
                savingAccountString.add(savingAccountNo + " - " + accountType);
            }
            return savingAccountString;
        }
    }

    @Override
    public String getSavingAccountStringByPhone(String phone) {
        if (phone.substring(0, 1).equals("+") == false) {
            phone = "+" + phone;
        }
        System.out.println("Customer phoneNumber is: " + phone + "******");

        Query q = em.createQuery("SELECT a FROM PayMe a WHERE a.phoneNumber = :phone");
        q.setParameter("phone", phone);
        PayMe payme = (PayMe) q.getSingleResult();

        String accountNo = payme.getSavingAccount().getAccountNumber().toString();
        accountNo = accountNo + " - " + payme.getSavingAccount().getSavingAccountType().getAccountType();
        return accountNo;
    }

    @Override
    public String getPhoneNumber(String ic) {
        System.err.println("get phone number: ic = " + ic);

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        Customer customer = (Customer) q.getSingleResult();
        return customer.getPhoneNumber();
    }

    @Override
    public String getBalance(String phoneNumber) {

        String phone;
        if (phoneNumber.substring(0, 1).equals("+")) {
            phone = phoneNumber;
        } else {
            phone = "+" + phoneNumber;
        }
        Query q = em.createQuery("SELECT a FROM PayMe a WHERE a.phoneNumber = :phone");
        q.setParameter("phone", phone);
        PayMe payme = (PayMe) q.getSingleResult();
        return payme.getBalance().toString();
    }

    @Override
    public boolean checkTopUpLimit(String phoneNumber, String amount) {
        if (phoneNumber.substring(0, 1).equals("+") == false) {
            phoneNumber = "+" + phoneNumber;
        }
        Query q = em.createQuery("SELECT a FROM PayMe a WHERE a.phoneNumber = :phoneNumber");
        q.setParameter("phoneNumber", phoneNumber);
        PayMe payme = (PayMe) q.getSingleResult();

        BigDecimal amountBD = new BigDecimal(amount);
        BigDecimal totalAmount = amountBD.add(payme.getBalance());
        BigDecimal limit = new BigDecimal("999");

        if (totalAmount.compareTo(limit) == 1) {
            return false;
        } else {
            return true;
        }
    }

    private String passwordHash(String pass) {
        String md5 = null;
        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(pass.getBytes(), 0, pass.length());

            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
}
