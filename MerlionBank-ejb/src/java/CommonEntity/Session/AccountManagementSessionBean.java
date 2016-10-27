/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.OnlineAccount;
import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CommonEntity.Permission;
import DepositEntity.FixedDepositAccount;
import DepositEntity.FixedDepositRate;
import DepositEntity.SavingAccount;
import DepositEntity.SavingAccountType;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.LoanTermInvalidException;
import Exception.PasswordNotMatchException;
import Exception.PasswordTooSimpleException;
import Exception.UserAlreadyActivatedException;
import Exception.UserAlreadyHasSavingAccountException;
//import Exception.AccountTypeNotExistException;
//import Exception.PasswordTooSimpleException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.LoanType;
import LoanEntity.Session.LoanSessionBeanLocal;
import Other.Session.sendEmail;
import Other.Session.GeneratePassword;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Map;
import java.util.HashMap;

import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Collections.list;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 *
 * @author a0113893
 */
@Stateless
public class AccountManagementSessionBean implements AccountManagementSessionBeanLocal {

    private static final Random RANDOM = new SecureRandom();

    public static final int SALT_LENGTH = 8;
    @PersistenceContext
    private EntityManager em;
    @EJB
    FixedDepositAccountSessionBeanLocal fdasbl;
    @EJB
    StaffManagementSessionBeanLocal smsbl;
    @EJB
    LoanSessionBeanLocal hlsbl;
//    private GoogleMail gm;

    @Override
    public Customer createSavingAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String savingAccountName) throws UserExistException, EmailNotSendException {
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

        System.out.println("Debit Account successfully created");

        try {
            // reminder: remove password
            SendPendingVerificationEmail(name, email, tempPassword);

        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException(ex.getMessage());
        }

        //create saving account
        long savingAccoutNumber = generateSavingAccountNumber();
        BigDecimal initialValue = new BigDecimal("0.0000");

        System.out.println("Saving Account Type is: " + savingAccountName);
        Query queryType = em.createQuery("SELECT a FROM SavingAccountType a WHERE a.accountType = :accountType");
        queryType.setParameter("accountType", savingAccountName);
        SavingAccountType savingAccountType = (SavingAccountType) queryType.getSingleResult();
        SavingAccount savingAccount = new SavingAccount(savingAccoutNumber, initialValue, initialValue, "inactive", customer, savingAccountType);
        em.persist(savingAccount);
        List<SavingAccount> savingAccounts = new ArrayList<SavingAccount>();
        savingAccounts.add(0, savingAccount);
        customer.setSavingAccounts(savingAccounts);
        em.persist(customer);
        em.flush();
        //log an action
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Create a new Saving Account", customer);
        em.persist(action);
        List<CustomerAction> customerActions = new ArrayList<CustomerAction>();
        customerActions.add(0, action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();

        return customer;

    }

    @Override
    public void createSavingAccountExistingCustomer(Long customerID, String savingAccountName) throws UserAlreadyHasSavingAccountException, EmailNotSendException {
        System.out.println(savingAccountName);
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        System.out.println("Inside createSavingAccount FOR Existing customer");

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = new ArrayList(q.getResultList());
        Customer customer = customers.get(0);

        List<SavingAccount> savingAccounts = customer.getSavingAccounts();
        List<SavingAccount> temp = new ArrayList<SavingAccount>();
        for (int i = 0; i < savingAccounts.size(); i++) {
            System.out.print("hehheheheh");
            if (!savingAccounts.get(i).getStatus().equals("terminated")) {
                temp.add(savingAccounts.get(i));
            }
        }

        for (int i = 0; i < temp.size(); i++) {
            System.out.print("inside the for loop!");
            if (temp.get(i).getSavingAccountType().getAccountType().equals(savingAccountName)) {
                System.out.print("inside the if statement!");
                throw new UserAlreadyHasSavingAccountException("User Already has this type of saving account, cannot create more!");
            }
        }

//        if (savingAccounts.size() == 3) {
//            System.out.print("You already have 3 saving accounts!");
//            throw new UserAlreadyHasSavingAccountException("User Already has 3 saving accounts, cannot create more!");
//
//        } else {
//            for (int i = 0; i < savingAccounts.size(); i++) {
//                if (savingAccounts.get(i).getSavingAccountType().getAccountType().equals(savingAccountName)) {
//                    System.out.print("User already has the same saving account.");
//                    throw new UserAlreadyHasSavingAccountException("User already has the same saving account.");
//                }
//            }
        //create saving account
        //long savingAccoutNumber = Math.round(Math.random() * 1000000000);
        Long savingAccoutNumber = generateSavingAccountNumber();
        BigDecimal initialValue = new BigDecimal("0.0000");
        System.out.println("Saving Account Type is: " + savingAccountName);
        Query queryType = em.createQuery("SELECT a FROM SavingAccountType a WHERE a.accountType = :accountType");
        queryType.setParameter("accountType", savingAccountName);
        SavingAccountType savingAccountType = (SavingAccountType) queryType.getSingleResult();
        if (savingAccountName.equals("MerLion Youth Saving Account")) {
            SavingAccount savingAccount = new SavingAccount(savingAccoutNumber, initialValue, initialValue, "active", customer, savingAccountType);
            em.persist(savingAccount);
            savingAccounts.add(savingAccount);
            customer.setSavingAccounts(savingAccounts);
            em.persist(customer);
            em.flush();

        } else {
            SavingAccount savingAccount = new SavingAccount(savingAccoutNumber, initialValue, initialValue, "inactive", customer, savingAccountType);
            em.persist(savingAccount);
            savingAccounts.add(savingAccount);
            customer.setSavingAccounts(savingAccounts);
            em.persist(customer);
            em.flush();
            String email = customer.getEmail();
            BigDecimal minAmount = savingAccount.getSavingAccountType().getMinAmount();

            try {
                SendSavingAccountActivationEmail(savingAccountName, minAmount, email);

            } catch (MessagingException ex) {
                System.out.println("Error sending email.");
                throw new EmailNotSendException("Error sending email.");
            }
        }

        //log an action
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Create a new Saving Account", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();

        System.out.println("Saving Account successfully created");

    }

    private long generateSavingAccountNumber() {
        int a = 1;
        Random rnd = new Random();
        int number = 100000000 + rnd.nextInt(900000000);
        Long accountNumber = Long.valueOf(number);
        Query q2 = em.createQuery("SELECT c.accountNumber FROM SavingAccount c");
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

    @Override
    public void updateProfile(String ic, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialGoal) {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());

        Customer customer = temp.get(temp.size() - 1);
        customer.setAddress(address);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setOccupation(occupation);
        customer.setFamilyInfo(familyInfo);
        customer.setFinancialGoal(financialGoal);
        em.merge(customer);
        em.flush();

        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Update Profile", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();

    }

    @Override
    public Customer diaplayCustomerId(Long id) {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", id);
        Customer customer = (Customer) q.getSingleResult();
        return customer;
    }

    @Override
    public Customer diaplayCustomer(String ic) throws ListEmptyException {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        if (q.getResultList().isEmpty()) {
            throw new ListEmptyException("Customer does not exist!");
        } else {
            List<Customer> temp = new ArrayList(q.getResultList());
            Customer customer = temp.get(temp.size() - 1);

            return customer;
        }
    }

    private void SendSavingAccountActivationEmail(String name, BigDecimal minAmount, String email) throws MessagingException {
        String subject = "Merlion Bank - " + name + "Saving Account Activation";
        System.out.println("Inside send SavingAccount Activation email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Congratulations! You have successfully created a Merlion " + name + " Saving Account!</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<br />Please logins to your iBanking to activate your Saving Account</h2><br />"
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to transfer minimum SG$" + minAmount + " to your account in order to activate your saving account. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>MerLION Platform User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
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

    //Activate account- 1st step verify account details
    @Override
    public Customer activateAccountVerifyDetail(String ic, String fullName, Date dateOfBirth, String phoneNumber) throws UserNotExistException, UserAlreadyActivatedException {
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
        } else if (customer.getStatus().equals("unverified")) {
            System.out.println("Username " + ic + " does not exist!");
            throw new UserNotExistException("Username " + ic + "has not been verified");
        } else if (customer.getStatus().equals("active")) {
            System.out.println("Username " + ic + "You have already activated your account!");
            throw new UserAlreadyActivatedException("You have already activated your account!");
        } else if (customer.getOnlineAccount().getAccountStatus().equals("locked")) {
            System.out.println("Username " + ic + "Acount locked");

            throw new UserAlreadyActivatedException("You have already activated your account!");
        } else {
            System.out.println("Username " + ic + " IC check pass!");
        }

        System.out.println(customer.getDateOfBirth());
        if (!fullName.equals(customer.getName())) {
            System.out.println(fullName + "         " + customer.getName());
            throw new UserNotExistException("Username " + ic + "invaid account details");
            //return customer.getName();

        } else if (!dateOfBirth.equals(customer.getDateOfBirth())) {
            System.out.println(dateOfBirth + "         " + customer.getDateOfBirth());
            throw new UserNotExistException("Username " + ic + "invaid account details");
            //return "date";
        } else if (!phoneNumber.equals(customer.getPhoneNumber())) {
            System.out.println(phoneNumber + "         " + customer.getPhoneNumber());
            throw new UserNotExistException("Username " + ic + "invaid account details");
            //return customer.getPhoneNumber();
        } else {
            return customer;
        }

    }

    //Activate account - 2nd step verify account balance
    @Override
    public String verifyAccountBalance(String ic) {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        Customer customer = temp.get(temp.size() - 1);
        int amount = customer.getSavingAccounts().get(0).getBalance().intValueExact();
        BigDecimal currentAmount = new BigDecimal(amount);
        int res = currentAmount.compareTo(customer.getSavingAccounts().get(0).getSavingAccountType().getMinAmount());
        if (res == 0 || res == 1) {
            return ic;
        } else {
            return "invalid amount";
        }
    }

    //Activate account - 3rd intial password reset
    @Override
    public String updatePassword(String ic, String oldPassword, String newPassword, String confirmPassword) throws PasswordTooSimpleException {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        Customer customer = temp.get(temp.size() - 1);
        if (!passwordHash(oldPassword + customer.getOnlineAccount().getSalt()).equals(customer.getOnlineAccount().getPassword())) {
            return "invalid old password";
        } else if (!newPassword.equals(confirmPassword)) {
            System.out.println(newPassword);
            System.out.println(confirmPassword);
            return "Does not match with new password";
        } else if (passwordHash(oldPassword + customer.getOnlineAccount().getSalt()).equals(customer.getOnlineAccount().getPassword()) && newPassword.equals(confirmPassword)) {
            if (!checkPasswordComplexity(newPassword)) {
                throw new PasswordTooSimpleException("password is too simple");
            }
            String resetPassword = passwordHash(newPassword + customer.getOnlineAccount().getSalt());
            customer.getOnlineAccount().setPassword(resetPassword);
            em.flush();
            return ic;
        } else {
            return "invalid details";
        }
    }

    //Password complexity check
    private boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) {
            return false;
        }

        char pw[] = password.toCharArray();
        boolean alphabet = false;
        boolean digit = false;
        for (int i = 0; i < pw.length; i++) {
            if (Character.isLetter(pw[i])) {
                alphabet = true;
            }
            if (Character.isDigit(pw[i])) {
                digit = true;
            }
            if (alphabet && digit) {
                return true;
            }
        }
        return false;
    }

    //Activate account - 4th step update account status
    @Override

    public boolean updateAccountStatus(String ic) {

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());

        Customer customer = temp.get(temp.size() - 1);
        customer.setStatus("active");
        em.persist(customer);
        customer.getOnlineAccount().setAccountStatus("active");
        em.flush();
        if (!customer.getSavingAccounts().isEmpty()) {
            customer.getSavingAccounts().get(0).setStatus("active");
        } else if (!customer.getFixedDepositeAccounts().isEmpty()) {
            customer.getFixedDepositeAccounts().get(0).setStatus("active");
        }else if (!customer.getLoans().isEmpty()) {
            //customer.getLoans().get(0).setStatus("active");
        }else if (!customer.getDiscretionaryAccounts().isEmpty()) {
            customer.getDiscretionaryAccounts().get(0).setStatus("active");
        }
        em.flush();

        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Activate Account", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();

        return true;

    }

    //forget password- 1st step verify account details
    @Override
    public String forgetPasswordVerifyDetail(String ic, String fullName, Date dateOfBirth, String email) throws UserNotExistException, UserNotActivatedException {
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
            System.out.println("Username " + ic + "You have not activated your account!");
            throw new UserNotActivatedException("You have not activated your account!");
        } else {
            System.out.println("Username " + ic + " IC check pass!");
        }

        if (!fullName.equals(customer.getName())) {
            throw new UserNotExistException("Username " + ic + "invaid account details");
        } else if (!dateOfBirth.equals(customer.getDateOfBirth())) {
            throw new UserNotExistException("Username " + ic + "invaid account details");
        } else if (!email.equals(customer.getEmail())) {
            throw new UserNotExistException("Username " + ic + "invaid account details");
        } else {
            return ic;
        }
    }

    //send 2FA
    //forget password- 2nd send 2FA
    @Override
    public String sendTwoFactorAuthentication(String ic) throws TwilioRestException {
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

        return ic;
    }

    //verify 2FA
    //forget password- 3rd step verify 2FA
    @Override
    public String verifyTwoFactorAuthentication(String ic, String inputCode) {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        Customer customer = temp.get(temp.size() - 1);

        if (customer.getOnlineAccount().getAuthenticationCode().equals(inputCode)) {
            return ic;
        } else {
            return "invalid 2FA";
        }

    }

    @Override
    public String updateForgetPassword(String ic, String newPassword, String confirmPassword) throws PasswordTooSimpleException {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        Customer customer = temp.get(temp.size() - 1);

        if (newPassword.equals(confirmPassword)) {
            if (!checkPasswordComplexity(newPassword)) {
                throw new PasswordTooSimpleException("password is too simple");
            }
            String resetPassword = passwordHash(newPassword + customer.getOnlineAccount().getSalt());
            customer.getOnlineAccount().setPassword(resetPassword);
            em.flush();
            OnlineAccount onlineAccount = customer.getOnlineAccount();
            onlineAccount.setAccountStatus("active");
            em.persist(onlineAccount);
            em.flush();
            customer.setStatus("active");
            em.flush();
            customer.setOnlineAccount(onlineAccount);

            CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Reset Password", customer);
            em.persist(action);
            List<CustomerAction> customerActions = customer.getCustomerActions();
            customerActions.add(action);
            customer.setCustomerActions(customerActions);
            em.persist(customer);
            em.flush();

            return ic;
        } else if (!newPassword.equals(confirmPassword)) {
            return "Does not match with new password";
        } else {
            return "invalid details";
        }
    }

    //log in- 1st step verify details  
    @Override
    public Long checkLogin(String ic, String password) throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException {
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

        if (!passwordHash(password + customer.getOnlineAccount().getSalt()).equals(customer.getOnlineAccount().getPassword())) {

            Long i = Long.parseLong("1");
            return i;
        }

        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Successful Login", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();

        return customer.getId();
    }

    @Override
    public Long lockAccount(String customerIc) {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", customerIc);
        List<Customer> temp = new ArrayList(q.getResultList());
        Customer customer = temp.get(temp.size() - 1);
        customer.setStatus("locked");
        em.flush();
        OnlineAccount onlineAccount = customer.getOnlineAccount();
        onlineAccount.setAccountStatus("locked");
        em.persist(onlineAccount);
        em.flush();
        customer.setOnlineAccount(onlineAccount);
        return customer.getId();
    }

    //Create Fixed Deposit Account - 1st page - create online banking account
    @Override
    public Customer createFixedDepositAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo) throws UserExistException, EmailNotSendException {
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

        System.out.println("In Creating  account");
        OnlineAccount onlineAccount = new OnlineAccount(ic, "inactive", salt, password);
        em.persist(onlineAccount);
        BigDecimal intraTransferLimit = new BigDecimal(1000);
        Customer customer = new Customer(ic, name, gender, dateOfBirth, address, email, phoneNumber, occupation, familyInfo, null, "0.0000", onlineAccount, "unverified", intraTransferLimit);
        em.persist(customer);
        em.flush();
        System.out.println("Create Customer successfully");

        return customer;
    }

    //Create Fixed Deposit Account - 2nd page - configure fixed deposit account
    @Override

    public Long createFixedAccount(Customer customer, BigDecimal amount, String duration) throws EmailNotSendException {
        //compute start date
        Calendar calE;
        Calendar calS;

        calS = GregorianCalendar.getInstance();
        calS.add(GregorianCalendar.DATE, 7);
        Date dateOfStart = calS.getTime();

        calE = Calendar.getInstance();
        calE.setTime(dateOfStart);

        Date dateOfEnd;
        //compute end date
        if (duration.equals("3")) {
            calE.add(Calendar.MONTH, 3);
        } else if (duration.equals("6")) {
            calE.add(Calendar.MONTH, 6);
        } else if (duration.equals("12")) {
            calE.add(Calendar.MONTH, 12);
        } else if (duration.equals("24")) {
            calE.add(Calendar.MONTH, 24);
        }

        dateOfEnd = calE.getTime();

        System.out.println("@@@@@@@" + customer.getIc() + "@@@@@@" + amount + "@@@@@" + dateOfStart + "@@@@" + dateOfEnd + "@@@@@@@@@@" + duration);
        Long accountNumber = fdasbl.createFixedAccount(customer.getId(), amount, dateOfStart, dateOfEnd, duration);
        System.out.println("accountNumber" + accountNumber);
        String password = GeneratePassword.createPassword();

        String tempPassword = password;

        password = passwordHash(password + customer.getOnlineAccount().getSalt());
        customer.getOnlineAccount().setPassword(password);
        System.out.println("Password after hash&salt:" + password);

        try {
            // reminder: remove password
            SendPendingVerificationEmail(customer.getName(), customer.getEmail(), tempPassword);

        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }

        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Create new Fixed Deposit Account", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);

        return customer.getId();
    }

    //Teller Create Fixed Deposit Account - 1st page - create online banking account
    //Teller Create Loan Account - 1st page - create online banking account
    @Override
    public Customer tellerCreateFixedDepositAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String enterPassword) throws UserExistException, EmailNotSendException {
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
        return customer;
    }

    //Teller Create saving Account 
    @Override
    public void tellerCreateSavingAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String savingAccountName, String enterPassword) throws UserExistException, EmailNotSendException {
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
        String password = enterPassword;
        String tempPassword = password;

        password = passwordHash(password + salt);
        System.out.println("Password after hash&salt:" + password);

        System.out.println("In Creating saving account");
        OnlineAccount onlineAccount = new OnlineAccount(ic, "active", salt, password);
        em.persist(onlineAccount);
        BigDecimal intraTransferLimit = new BigDecimal(1000);
        Customer customer = new Customer(ic, name, gender, dateOfBirth, address, email, phoneNumber, occupation, familyInfo, null, "0.0000", onlineAccount, "active", intraTransferLimit);
        em.persist(customer);
        System.out.println("Create Customer successfully");

        //create saving account
        long savingAccoutNumber = generateSavingAccountNumber();
        BigDecimal initialValue = new BigDecimal("0.0000");

        System.out.println("Saving Account Type is: " + savingAccountName);
        Query queryType = em.createQuery("SELECT a FROM SavingAccountType a WHERE a.accountType = :accountType");
        queryType.setParameter("accountType", savingAccountName);
        SavingAccountType savingAccountType = (SavingAccountType) queryType.getSingleResult();
        SavingAccount savingAccount = new SavingAccount(savingAccoutNumber, initialValue, initialValue, "active", customer, savingAccountType);
        em.persist(savingAccount);
        List<SavingAccount> savingAccounts = new ArrayList<SavingAccount>();
        savingAccounts.add(0, savingAccount);
        customer.setSavingAccounts(savingAccounts);
        em.persist(customer);
        em.flush();
        //log an action
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Create a new Saving Account", customer);
        em.persist(action);
        List<CustomerAction> customerActions = new ArrayList<CustomerAction>();
        customerActions.add(0, action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();

        System.out.println("Debit Account successfully created");

    }
    
    @Override
    public void setFileDestination(Long customerId,String fileDestination){
        Customer customer=em.find(Customer.class,customerId);
        customer.setFileDestination(fileDestination);
        em.flush();
        
    }
    
   
    
}
