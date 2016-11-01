/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CommonEntity.OnlineAccount;
import CommonEntity.Session.StaffManagementSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import Other.Session.GeneratePassword;
import Other.Session.sendEmail;
import WealthEntity.DiscretionaryAccount;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.mail.MessagingException;

/**
 *
 * @author a0113893
 */
@Stateless
public class WealthApplicationSessionBean implements WealthApplicationSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private static final Random RANDOM = new SecureRandom();
    public static final int SALT_LENGTH = 8;
    @EJB
    StaffManagementSessionBeanLocal smsbl;

    @Override
    public Customer createDiscretionaryAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo) throws UserExistException, EmailNotSendException {
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        System.out.println("Inside createAccount");

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (!temp.isEmpty()) {
            System.out.println("User " + ic + " exists!");
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getStatus().equals("active")) {
                    throw new UserExistException("User " + ic + " exists! Please go and login");
                } else if (temp.get(i).getStatus().equals("unverified")) {
                    throw new UserExistException("User " + ic + "has not been verified by MerlionBank!");

                } else if (temp.get(i).getStatus().equals("locked")) {
                    throw new UserExistException("User " + ic + " account has been locked. Please unlock your account!");
                } else if (temp.get(i).getStatus().equals("inactive")) {
                    throw new UserExistException("User " + ic + " has an inavtive account. Please proceed to activation.");
                }
            }

        }
        System.out.println("customer does not exist!");

        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }
        String password = GeneratePassword.createPassword();
        String tempPassword = password;

        password = passwordHash(password + salt);
        System.out.println("Password after hash&salt:" + password);

        System.out.println("In Creating saving account");
        OnlineAccount onlineAccount = new OnlineAccount(ic, "inactive", salt, password);
        em.persist(onlineAccount);
        BigDecimal intraTransferLimit = new BigDecimal(1000);
        Customer customer = new Customer(ic, name, gender, dateOfBirth, address, email, phoneNumber, occupation, familyInfo, null, "0.0000", onlineAccount, "unverified", intraTransferLimit);
        em.persist(customer);
        System.out.println("Create Customer successfully");

        System.out.println("Discretionary Account successfully created");

        try {
            // reminder: remove password
            SendPendingVerificationEmail(name, email, tempPassword);

        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException(ex.getMessage());
        }

        //create discretionary account
        long discretionaryAccoutNumber = generateDiscretionaryAccountNumber();
        BigDecimal initialValue = new BigDecimal(0);

        DiscretionaryAccount discretionaryAccount = new DiscretionaryAccount(discretionaryAccoutNumber, Calendar.getInstance().getTime(), null, initialValue, initialValue, "inactive", customer,initialValue);
        em.persist(discretionaryAccount);
        em.flush();
                 discretionaryAccount.setCommission(initialValue);
                 
        List<DiscretionaryAccount> discretionaryAccounts = new ArrayList<DiscretionaryAccount>();
        discretionaryAccounts.add(0, discretionaryAccount);
        customer.setDiscretionaryAccounts(discretionaryAccounts);
        em.persist(customer);
        em.flush();
        //log an action
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Create a new Discretionary Account", customer);
        em.persist(action);
        List<CustomerAction> customerActions = new ArrayList<CustomerAction>();
        customerActions.add(0, action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();

        return customer;

    }

    @Override
    public void createDiscretionaryAccountExistingCustomer(Long customerID) throws EmailNotSendException {
       
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = new ArrayList(q.getResultList());
        Customer customer = customers.get(0);

        long discretionaryAccoutNumber = generateDiscretionaryAccountNumber();
        BigDecimal initialValue = new BigDecimal(0);

        DiscretionaryAccount discretionaryAccount = new DiscretionaryAccount(discretionaryAccoutNumber, Calendar.getInstance().getTime(), null, initialValue, initialValue, "inactive", customer,initialValue);
        em.persist(discretionaryAccount);
        em.flush();
        
        discretionaryAccount.setCommission(initialValue);
        List<DiscretionaryAccount> discretionaryAccounts = new ArrayList<DiscretionaryAccount>();

        if (customer.getDiscretionaryAccounts() == null) {
            discretionaryAccounts.add(discretionaryAccount);
            customer.setDiscretionaryAccounts(discretionaryAccounts);
        } else {//alr have fixed acct
            customer.getDiscretionaryAccounts().add(discretionaryAccount);
        }
        em.persist(customer);
        em.flush();

        String email = customer.getEmail();
        //BigDecimal minAmount = new BigDecimal(200000.00);

        try {
            SendDiscretionaryAccountActivationEmail(customer.getName(), discretionaryAccount.getAccountNumber(), email);

        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }

        //log an action
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Create a new Discretionary Account", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();

        System.out.println("Discretionary Account successfully created");

    }

    private long generateDiscretionaryAccountNumber() {
        int a = 1;
        Random rnd = new Random();
        int number = 100000000 + rnd.nextInt(900000000);
        Long accountNumber = Long.valueOf(number);
        Query q2 = em.createQuery("SELECT c.accountNumber FROM DiscretionaryAccount c");
        List<Long> existingAcctNum = new ArrayList(q2.getResultList());
        while (a == 1) {

            if ((existingAcctNum.contains(accountNumber)) || (number / 100000000 == 0)) {
                number = 100000000 + rnd.nextInt(900000000);
                accountNumber = Long.valueOf(number);
                a = 1;
            } else {
                a = 0;
            }
        }

        return accountNumber;
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

    private void SendPendingVerificationEmail(String name, String email, String password) throws MessagingException {
        String subject = "Merlion Bank - Online Banking Account \"" + name + "\" Created - Pending Verification";

        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Congratulations! You have successfully registered a Merlion Online Banking Account!</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                // + "<br />Temporary Password: " + password + "<br />Please activate your account through this link: " + "</h2><br />"
                + "<p style=\"color: #ff0000;\">Please kindly wait for 1 to 2 working days for staff to verify you account. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>MerLION Platform User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

    private void SendDiscretionaryAccountActivationEmail(String name, Long accountNumber, String email) throws MessagingException {
        String subject = "Merlion Bank - Discretionary Account Activation";
        System.out.println("Inside send SavingAccount Activation email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Congratulations! You have successfully created a Merlion Discretionary Account!</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<br />Discretionary Account Number: " + accountNumber + "<br />Please activate your account through this link: " + "</h2><br />"
                + "<br />Please logins to your iBanking to activate your Discretionary Account</h2><br />"
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to transfer minimum SG$200,000 to your account in order to activate your Discretionary account. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>MerLION Platform User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }
    
    @Override
    public Customer tellerCreateDiscretionaryAccount(Long staffId,String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String enterPassword) throws UserExistException, EmailNotSendException {
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        System.out.println("Inside createAccount");

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (!temp.isEmpty()) {
            System.out.println("User " + ic + " exists!");
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getStatus().equals("active")) {
                    throw new UserExistException("User " + ic + " exists! Please go and login");
                } else if (temp.get(i).getStatus().equals("unverified")) {
                    throw new UserExistException("User " + ic + "has not been verified by MerlionBank!");

                } else if (temp.get(i).getStatus().equals("locked")) {
                    throw new UserExistException("User " + ic + " account has been locked. Please unlock your account!");
                } else if (temp.get(i).getStatus().equals("inactive")) {
                    throw new UserExistException("User " + ic + " has an inavtive account. Please proceed to activation.");
                }
            }

        }
        System.out.println("customer does not exist!");

        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }

        String tempPassword = enterPassword;

        enterPassword = passwordHash(enterPassword + salt);
        System.out.println("Password after hash&salt:" + enterPassword);

        System.out.println("In Creating  account");
        OnlineAccount onlineAccount = new OnlineAccount(ic, "active", salt, enterPassword);
        em.persist(onlineAccount);
        BigDecimal intraTransferLimit = new BigDecimal(1000);
        Customer customer = new Customer(ic, name, gender, dateOfBirth, address, email, phoneNumber, occupation, familyInfo, null, "0.0000", onlineAccount, "active", intraTransferLimit);
        em.persist(customer);
        em.flush();
        System.out.println("Create Customer successfully");
        
        long discretionaryAccoutNumber = generateDiscretionaryAccountNumber();
        BigDecimal initialValue = new BigDecimal(0);

        DiscretionaryAccount discretionaryAccount = new DiscretionaryAccount(discretionaryAccoutNumber, Calendar.getInstance().getTime(), null, initialValue, initialValue, "inactive", customer,initialValue);
        em.persist(discretionaryAccount);
         em.flush();
                  discretionaryAccount.setCommission(initialValue);
        List<DiscretionaryAccount> discretionaryAccounts = new ArrayList<DiscretionaryAccount>();
        discretionaryAccounts.add(0, discretionaryAccount);
        customer.setDiscretionaryAccounts(discretionaryAccounts);
        em.persist(customer);
        em.flush();
        
        smsbl.recordStaffAction(staffId, "create new customer discretionary account", customer.getId());
        return customer;
    }
    
    @Override
    public Long searchCustomer(String customerIc) throws UserNotExistException, UserNotActivatedException {
        System.out.println("testing: " + customerIc);
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", customerIc);
        List<Customer> temp = new ArrayList(q.getResultList());
        System.out.println("testing: " + temp.size());
        if (temp.isEmpty()) {
            System.out.println("Username " + customerIc + " does not exist!");
            throw new UserNotExistException("Username " + customerIc + " does not exist, please try again");
        }

        int size = temp.size();
        Customer customer = temp.get(size - 1);
        //System.out.println("testing: "+customer.getIc());
        if (customer.getStatus().equals("terminated")) {
            System.out.println("Username " + customerIc + " does not exist!");
            throw new UserNotExistException("Username " + customerIc + " does not exist, please try again");
        } else if (customer.getStatus().equals("inactive")) {
            System.out.println("Username " + customerIc + "Customer has not activated his or her account!");
            throw new UserNotActivatedException("Username " + customerIc + "Customer has not activated his or her account!");
        } else {
            System.out.println("Username " + customerIc + " IC check pass!");
        }
        return customer.getId();

    }
    
    @Override
    public void staffCreateDiscretionaryAccountExistingCustomer(Long staffId, Long customerID) throws EmailNotSendException {
       
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = new ArrayList(q.getResultList());
        Customer customer = customers.get(0);

        long discretionaryAccoutNumber = generateDiscretionaryAccountNumber();
        BigDecimal initialValue = new BigDecimal(0);

        DiscretionaryAccount discretionaryAccount = new DiscretionaryAccount(discretionaryAccoutNumber, Calendar.getInstance().getTime(), null, initialValue, initialValue, "inactive", customer,initialValue);
        em.persist(discretionaryAccount);     
        em.flush();
         discretionaryAccount.setCommission(initialValue);
        List<DiscretionaryAccount> discretionaryAccounts = new ArrayList<DiscretionaryAccount>();

        if (customer.getDiscretionaryAccounts() == null) {
            discretionaryAccounts.add(discretionaryAccount);
            customer.setDiscretionaryAccounts(discretionaryAccounts);
        } else {//alr have fixed acct
            customer.getDiscretionaryAccounts().add(discretionaryAccount);
        }

        em.persist(customer);
        em.flush();

        String email = customer.getEmail();
        //BigDecimal minAmount = new BigDecimal(200000.00);

        try {
            SendDiscretionaryAccountActivationEmail(customer.getName(), discretionaryAccount.getAccountNumber(), email);

        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }

        smsbl.recordStaffAction(staffId, "create existing customer discretionary account", customer.getId());

        System.out.println("Discretionary Account successfully created");

    }
    
    //Activate account - 2nd step verify account balance
    @Override
    public String verifyDiscretionaryAccountBalance(String ic) {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        Customer customer = temp.get(temp.size() - 1);
        BigDecimal amount = customer.getDiscretionaryAccounts().get(0).getBalance();
        BigDecimal currentAmount = new BigDecimal(200000);
        int res = amount.compareTo(currentAmount);
        if (res == 0 || res == 1) {
            return ic;
        } else {
            return "invalid amount";
        }
    }

     

}
