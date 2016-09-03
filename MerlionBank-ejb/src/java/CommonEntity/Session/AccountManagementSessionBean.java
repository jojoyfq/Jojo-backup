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

import java.util.Map;
import java.util.HashMap;

import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


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
    public void createSavingAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String savingAccountType)throws UserExistException {
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        System.out.println("Inside createAccount");
        
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (!temp.isEmpty()) {
            System.out.println("User " + name + " exists!");
            for (int i=0;i<temp.size();i++){
                if (!temp.get(i).getStatus().equals("terminated"))
                    throw new UserExistException("User " + name + " exists!");
            }
           
        }
         System.out.println("Username passes check!");
         
         for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }
        String password = GeneratePassword.createPassword();
        String tempPassword = password;
        
        
        password = passwordHash(password + salt);
        System.out.println("Password after hash&salt:" + password);
        
        
     System.out.println("In Creating saving account");
      OnlineAccount onlineAccount= new OnlineAccount (ic,"inactive",salt,password);
        em.persist(onlineAccount);
            Customer customer = new Customer(ic,name,gender,dateOfBirth,address,email,phoneNumber,occupation,familyInfo, null, "0.0000", onlineAccount,"inactive");          
            em.persist(customer);
            System.out.println("Create Customer successfully");
            long savingAccoutNumber= Math.round(Math.random()*1000000000);
            BigDecimal initialValue=new BigDecimal("0.0000");
            SavingAccount savingAccount= new SavingAccount(savingAccoutNumber, initialValue, initialValue, "inactive", customer,savingAccountType);
            em.persist(savingAccount);
            List<SavingAccount> savingAccounts=new ArrayList<SavingAccount>();
            savingAccounts.add(0,savingAccount);
            customer.setSavingAccounts(savingAccounts);
            em.persist(customer);
            em.flush();
            System.out.println("Debit Account successfully created");
            
            try {
            SendEmail(name, email, tempPassword);
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            Logger.getLogger(AccountManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    
    @Override
    public void updateProfile(String ic, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialGoal) throws UserExistException {
       Query q = em.createQuery("SELECT b FROM Customer b WHERE b.ic=:ic");
            q.setParameter("ic", ic);
            Customer customer = (Customer)q.getSingleResult();
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            customer.setOccupation(occupation);
            customer.setFamilyInfo(familyInfo);
            customer.setFinancialGoal(financialGoal);
            em.merge(customer);
            em.flush();
    }
   
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
            int amount=customer.getSavingAccounts().get(0).getBalance().intValueExact();
            if (amount>=500) {
                return ic;
            }
            else return "invalid amount";
    }
    
    //Activate account - 3rd intial password reset
    @Override
    public String updatePassword (String ic, String oldPassword, String newPassword,String confirmPassword)throws PasswordTooSimpleException{
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
            customer.setStatus("active");
            em.persist(customer);
            customer.getOnlineAccount().setAccountStatus("active");
            em.flush();
            customer.getSavingAccounts().get(0).setStatus("active");
            em.flush();
            return true;
            
    }
    
    //forget password- 1st step verify account details
    @Override
    public String forgetPasswordVerifyDetail(String ic, String fullName, Date dateOfBirth, String email){
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
            else if (!email.equals(customer.getEmail())){
                return "invalid account";
            }
            else 
                return ic;             
    }
    
    //send 2FA
    //forget password- 2nd send 2FA
    @Override
    public String sendTwoFactorAuthentication(String ic) throws TwilioRestException{
        Query q = em.createQuery("SELECT b FROM Customer b WHERE b.ic=:ic");
            q.setParameter("ic", ic);
            Customer customer = (Customer)q.getSingleResult();
        String ACCOUNT_SID = "AC0607da3843c85473703f3f5078a21a52";
        String AUTH_TOKEN = "79993c87540fe09ba96d8f1990d78eed";
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        Account account = client.getAccount();

        MessageFactory messageFactory = account.getMessageFactory();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", customer.getPhoneNumber())); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("From", "+12013451118")); // Replace with a valid phone number for your account.
        String oTP="Math.round(Math.random()*100000)";
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
    public String verifyTwoFactorAuthentication(String ic,String inputCode){
      Query q = em.createQuery("SELECT b FROM Customer b WHERE b.ic=:ic");
            q.setParameter("ic", ic);
            Customer customer = (Customer)q.getSingleResult(); 
            
            if (customer.getOnlineAccount().getAuthenticationCode().equals(inputCode)){
                return ic;
            }
            else return "invalid 2FA";
        
    }
    
    @Override
    public String updateForgetPassword (String ic, String newPassword,String confirmPassword)throws PasswordTooSimpleException{
    Query q = em.createQuery("SELECT b FROM Customer b WHERE b.ic=:ic");
            q.setParameter("ic", ic);
            Customer customer = (Customer)q.getSingleResult();
           if (newPassword.equals(confirmPassword)){
           if (!checkPasswordComplexity(newPassword)) {
                    throw new PasswordTooSimpleException("password is too simple");
                }
                String resetPassword = passwordHash(newPassword + customer.getOnlineAccount().getSalt());
                customer.getOnlineAccount().setPassword(resetPassword);
                em.flush();
                return ic;
           }
           else if (!newPassword.equals(confirmPassword)){
                return "Does not match with new password";
            }
           else return "invalid details";
    } 
    
    
    
    
    
    
}


