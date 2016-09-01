/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.OnlineAccount;
import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import Exception.PasswordTooSimpleException;
//import Exception.AccountTypeNotExistException;
//import Exception.PasswordTooSimpleException;
import Exception.UserExistException;
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
//    private GoogleMail gm;
    @Override
    public void createSavingAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialAsset, String financialGoal){// throws UserExistException {
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        System.out.println("Inside createAccount");
        
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (!temp.isEmpty()) {
            System.out.println("User " + name + " exists!");
           //throw new UserExistException("User " + name + " exists!");
        }
         System.out.println("Username passes check!");
         
         for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }
        String password = GeneratePassword.createPassword();
        String tempPassword = password;
        
        try {
            SendEmail(name, email, tempPassword);
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            Logger.getLogger(AccountManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        password = passwordHash(password + salt);
        System.out.println("Password after hash&salt:" + password);
        
        
     System.out.println("In Creating debit account");
      OnlineAccount onlineAccount= new OnlineAccount (ic,"inactive",salt,password);
        em.persist(onlineAccount);
            Customer customer = new Customer(ic,name,gender,dateOfBirth,address,email,phoneNumber,occupation,familyInfo, null,financialGoal, "0.0000", onlineAccount);          
            em.persist(customer);
            System.out.println("Create Customer successfully");
            long savingAccoutNumber= Math.round(Math.random()*100000000);;
            SavingAccount savingAccount= new SavingAccount(savingAccoutNumber, null, null, "inactive", customer);
            em.persist(savingAccount);
            System.out.println("Debit Account successfully created");

    }
    
    @Override
    public void updateProfile(String ic, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialGoal) throws UserExistException {
       Query q = em.createQuery("SELECT b FROM Customer b WHERE b.ic=:ic");
            q.setParameter("ic", ic);
            Customer customer = (Customer)q.getSingleResult();
            customer.setDateOfBirth(dateOfBirth);
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            customer.setOccupation(occupation);
            customer.setFamilyInfo(familyInfo);
            customer.setFinancialGoal(financialGoal);
            em.merge(customer);
            em.flush();
    }
//            
//            }
//            CompanyAdmin companyAdmin = companyAdminAccount.getCompanyAdmin();
//            if (firstName != null && !firstName.equals(companyAdmin.getFirstName())) {
//                companyAdmin.setFirstName(firstName);
//            }
//            if (lastName != null && !lastName.equals(companyAdmin.getLastName())) {
//                companyAdmin.setLastName(lastName);
//            }
//            if (email != null && !email.equals(companyAdmin.getEmail())) {
//                companyAdmin.setEmail(email);
//            }
//            if (contactNo != null && !contactNo.equals(companyAdminAccount.getContactNo())) {
//                companyAdminAccount.setContactNo(contactNo);
//            }
//
//            em.merge(companyAdminAccount);
//            em.merge(companyAdmin);
//            em.flush();
//            return true;
//        } else if (accountType.equals("User")) {
//            System.out.println("Inside session bean");
//            CompanyUserAccount companyUserAccount = em.find(CompanyUserAccount.class, accountId);
//            if (companyUserAccount == null) {
//                return false;//cannot find account
//            }
//            if (username != null && !username.equals(companyUserAccount.getUsername())) {
//                Query query = em.createQuery("SELECT c FROM CompanyUserAccount c WHERE c.username = :user");
//                query.setParameter("user", username);
//                List<CompanyUserAccount> companyUserAccountList = query.getResultList();
//                if (!companyUserAccountList.isEmpty()) {
//                    throw new UserExistException("Username already exists!");
//                }
//                companyUserAccount.setUsername(username);
//            }
//            CompanyUser companyUser = companyUserAccount.getCompanyUser();
//            if (firstName != null && !firstName.equals(companyUser.getFirstName())) {
//                companyUser.setFirstName(firstName);
//            }
//            if (lastName != null && !lastName.equals(companyUser.getLastName())) {
//                companyUser.setLastName(lastName);
//            }
//            if (email != null && !email.equals(companyUser.getEmail())) {
//                companyUser.setEmail(email);
//            }
//            if (contactNo != null && !contactNo.equals(companyUserAccount.getContactNo())) {
//                companyUserAccount.setContactNo(contactNo);
//            }
//
//            em.merge(companyUserAccount);
//            em.merge(companyUser);
//            em.flush();
//            return true;
//        } else if (accountType.equals("SystemAdmin")) {
//            SystemAdminAccount systemAdminAccount = em.find(SystemAdminAccount.class, accountId);
//            if (systemAdminAccount == null) {
//                return false;//cannot find account
//            }
//            if (username != null && !username.equals(systemAdminAccount.getUsername())) {
//                Query query = em.createQuery("SELECT c FROM SystemAdminAccount c WHERE c.username = :user");
//                query.setParameter("user", username);
//                List<SystemAdminAccount> systemAdminAccountList = query.getResultList();
//                if (!systemAdminAccountList.isEmpty()) {
//                    throw new UserExistException("Username already exists!");
//                }
//                systemAdminAccount.setUsername(username);
//            }
//            SystemAdmin systemAdmin = systemAdminAccount.getSystemAdmin();
//            if (firstName != null && !firstName.equals(systemAdmin.getFirstName())) {
//                systemAdmin.setFirstName(firstName);
//            }
//            if (lastName != null && !lastName.equals(systemAdmin.getLastName())) {
//                systemAdmin.setLastName(lastName);
//            }
//            if (email != null && !email.equals(systemAdmin.getEmail())) {
//                systemAdmin.setEmail(email);
//            }
//            if (contactNo != null && !contactNo.equals(systemAdminAccount.getContactNo())) {
//                systemAdminAccount.setContactNo(contactNo);
//            }
//
//            em.merge(systemAdminAccount);
//            em.merge(systemAdmin);
//            em.flush();
//            return true;
//        } else {
//            return false;
//        }
//    }

    
    
    @Override
    public Customer diaplayCustomer(String ic) {

            Query q = em.createQuery("SELECT b FROM Customer b WHERE b.ic=:ic");
            q.setParameter("ic", ic);
            Customer customer = (Customer)q.getSingleResult();
            return customer;
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private void SendEmail(String name, String email, String password) throws MessagingException {
        String subject = "Merlion Bank - Online Banking Account \"" + name + "\" Created - Pending Activation";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Congratulations! You have successfully registered a Merlion Online Banking Account!</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Username: " + name
                + "<br />Temporary Password: " + password + "<br />Please activate your account through this link: " + "</h2><br />" 
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to transfer minimum SG$500 to your account in order to activate your saving account. Thank you.</p>"
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
    public String activateAccountVerifyDetail(String ic, String fullName, Date dateOfBirth,String phoneNumber){
      Query q = em.createQuery("SELECT b FROM Customer b WHERE b.ic=:ic");
            q.setParameter("ic", ic);
            Customer customer = (Customer)q.getSingleResult();
            if (customer == null) {
                return "invalid account";//cannot find account
            }
            if (!fullName.equals(customer.getName())){
                return "invalid account";
            }
            else if(!dateOfBirth.equals(customer.getDateOfBirth())){
                return "invalid account";
            }
            else if (!phoneNumber.equals(customer.getPhoneNumber())){
                return "invalid account";
            }
            else 
                return ic;             
    }
    
    //Activate account - 2nd step verify account balance
    @Override
    public String verifyAccountBalance(String ic){
       Query q = em.createQuery("SELECT b FROM Customer b WHERE b.ic=:ic");
            q.setParameter("ic", ic);
            Customer customer = (Customer)q.getSingleResult();
            int amount=customer.getSavingAccount().getBalance().intValueExact();
            if (amount>=500) {
                return ic;
            }
            else return "invalid amount";
    }
    
    //Activate account - 3rd intial password reset
    private String updatePassword (String ic, String oldPassword, String newPassword,String confirmPassword)throws PasswordTooSimpleException{
    Query q = em.createQuery("SELECT b FROM Customer b WHERE b.ic=:ic");
            q.setParameter("ic", ic);
            Customer customer = (Customer)q.getSingleResult();
            if (!passwordHash(oldPassword + customer.getOnlineAccount().getSalt()).equals(customer.getOnlineAccount().getPassword())){
                return "invalid old password";
            }
            else if (!oldPassword.equals(confirmPassword)){
                return "Does not match with new password";
            }
           else if (passwordHash(oldPassword + customer.getOnlineAccount().getSalt()).equals(customer.getOnlineAccount().getPassword())&& oldPassword.equals(confirmPassword)){
           if (!checkPasswordComplexity(newPassword)) {
                    throw new PasswordTooSimpleException("password is too simple");
                }
                String resetPassword = passwordHash(newPassword + customer.getOnlineAccount().getSalt());
                customer.getOnlineAccount().setPassword(resetPassword);
                em.flush();
                return ic;
           }
           else return "invalid details";
    } 

   
    //Password complexity check
    @Override
    public boolean checkPasswordComplexity(String password) {
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
    private boolean updateAccountStatus(String ic){
        Query q = em.createQuery("SELECT b FROM Customer b WHERE b.ic=:ic");
            q.setParameter("ic", ic);
            Customer customer = (Customer)q.getSingleResult();
            customer.getOnlineAccount().setAccountStatus("active");
            em.flush();
            customer.getSavingAccount().setStatus("active");
            em.flush();
            return true;
            
    }
    
    
    //Change password
//  @Override
//    public boolean resetPasswordVerifyAccount(String IC, String name, Date dateOfBirth,String oldPassword) throws PasswordTooSimpleException {
//       Customer customer = em.find(Customer.class, IC);
//            if (customer == null) {
//                return false;//cannot find account
//            }
//            
//            if (oldPassword != null && newPassword != null && passwordHash(oldPassword + companyAdminAccount.getSalt()).equals(companyAdminAccount.getPassword())) {
//                if (!checkPasswordComplexity(newPassword)) {
//                    throw new PasswordTooSimpleException("password is too simple");
//                }
//                newPassword = passwordHash(newPassword + companyAdminAccount.getSalt());
//                companyAdminAccount.setPassword(newPassword);
//            } else {
//                return false;//invalid input
//            }
//
//            em.merge(companyAdminAccount);
//            em.flush();
//            return true;
//        } else if (accountType.equals("User")) {
//            CompanyUserAccount companyUserAccount = em.find(CompanyUserAccount.class, id);
//            if (companyUserAccount == null) {
//                return false;//cannot find account
//            }
//            if (oldPassword != null && newPassword != null && passwordHash(oldPassword + companyUserAccount.getSalt()).equals(companyUserAccount.getPassword())) {
//                if (!checkPasswordComplexity(newPassword)) {
//                    throw new PasswordTooSimpleException("password is too simple");
//                }
//                newPassword = passwordHash(newPassword + companyUserAccount.getSalt());
//                companyUserAccount.setPassword(newPassword);
//            } else {
//                return false;//invalid input
//            }
//
//            em.merge(companyUserAccount);
//            em.flush();
//            return true;
//        } else if (accountType.equals("SystemAdmin")) {
//            SystemAdminAccount systemAdminAccount = em.find(SystemAdminAccount.class, id);
//            if (systemAdminAccount == null) {
//                return false;//cannot find account
//            }
//            if (oldPassword != null && newPassword != null && passwordHash(oldPassword + systemAdminAccount.getSalt()).equals(systemAdminAccount.getPassword())) {
//                if (!checkPasswordComplexity(newPassword)) {
//                    throw new PasswordTooSimpleException("password is too simple");
//                }
//                newPassword = passwordHash(newPassword + systemAdminAccount.getSalt());
//                systemAdminAccount.setPassword(newPassword);
//            } else {
//                return false;//invalid input
//            }
//
//            em.merge(systemAdminAccount);
//            em.flush();
//            return true;
//        } else {
//            throw new AccountTypeNotExistException("Account type " + accountType + " is incorrect, please check");
//        }
//    }
}


