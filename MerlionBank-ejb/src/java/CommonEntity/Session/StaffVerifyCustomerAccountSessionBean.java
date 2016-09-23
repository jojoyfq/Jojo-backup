/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import CommonEntity.Staff;
import CustomerRelationshipEntity.StaffAction;
import DepositEntity.FixedDepositAccount;
import DepositEntity.SavingAccount;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Other.Session.GeneratePassword;
import Other.Session.sendEmail;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
public class StaffVerifyCustomerAccountSessionBean implements StaffVerifyCustomerAccountSessionBeanLocal {
 @PersistenceContext
    private EntityManager em;
    
//system retrieve list of pending verification customers
@Override
public List<Customer> viewPendingVerificationList()/*throws ListEmptyException*/{
   Query q = em.createQuery("SELECT a FROM Customer a WHERE a.status = :status");
        q.setParameter("status", "unverified");
        List<Customer> temp = new ArrayList(q.getResultList()); 
      //  if (temp.size()==0)
          //  throw new ListEmptyException("There are pending verifications!");
        return temp;
}

//staff verify customer and choose"reject" or "approve"
@Override
public boolean verifySavingAccountCustomer (Long staffID, Long customerID, String result,Long savingAccountId) throws EmailNotSendException{
    Query queryStaff = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        queryStaff.setParameter("id", staffID);
        Staff staff = (Staff)queryStaff.getSingleResult();
    
    Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", customerID);
        Customer customer = (Customer)q.getSingleResult();
        
        Query query = em.createQuery("SELECT a FROM SavingAccount a WHERE a.id = :id");
        query.setParameter("id", savingAccountId);
        System.out.println("@@@@@@@@@@@ Saving Account Id: "+savingAccountId);
        SavingAccount savingAccount = (SavingAccount)query.getSingleResult();
        
        String password = GeneratePassword.createPassword();
        String tempPassword = password;

        password = passwordHash(password + customer.getOnlineAccount().getSalt());
        customer.getOnlineAccount().setPassword(password);
        System.out.println("Password after hash&salt:" + password);
        
        if (result.equals("approve")){
            customer.setStatus("inactive");
            em.persist(customer);
            StaffAction action=new StaffAction(Calendar.getInstance().getTime(),"Approve saving account"+savingAccount.getAccountNumber(),customerID, staff);
            em.persist(action);
            List<StaffAction> staffActions=staff.getStaffActions();
            staffActions.add(action);
            staff.setStaffActions(staffActions);
            em.persist(staff);
            em.flush();
            
            try {
            sendEmail(customer.getName(),customer.getEmail(),tempPassword,savingAccount.getAccountNumber(),savingAccount.getSavingAccountType().getMinAmount());
            } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
            return true;
        }
        else if (result.equals("reject")){
            customer.setStatus("terminated");
            StaffAction action=new StaffAction(Calendar.getInstance().getTime(),"Reject saving account"+savingAccount.getAccountNumber(),customerID, staff);
            em.persist(action);
            List<StaffAction> staffActions=staff.getStaffActions();
            staffActions.add(action);
            staff.setStaffActions(staffActions);
            em.persist(staff);
            em.flush();
           try {
            sendRejectVerificationEmail(customer.getName(),customer.getEmail());
            } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
            return true;
        }
        else return false;
}

//staff verify customer and choose"reject" or "approve"
@Override
public boolean verifyFixedDepositAccountCustomer (Long staffID, Long customerID, String result,Long accountId) throws EmailNotSendException{
    Query queryStaff = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        queryStaff.setParameter("id", staffID);
        Staff staff = (Staff)queryStaff.getSingleResult();
    
    Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", customerID);
        Customer customer = (Customer)q.getSingleResult();
        
        Query query = em.createQuery("SELECT a FROM FixedDepositAccount a WHERE a.id = :id");
        query.setParameter("id", accountId);
        FixedDepositAccount fixedDepositAccount = (FixedDepositAccount)query.getSingleResult();
        
        String password = GeneratePassword.createPassword();
        String tempPassword = password;

        password = passwordHash(password + customer.getOnlineAccount().getSalt());
        customer.getOnlineAccount().setPassword(password);
        System.out.println("Password after hash&salt:" + password);
        
        if (result.equals("approve")){
            customer.setStatus("inactive");
            em.persist(customer);
            StaffAction action=new StaffAction(Calendar.getInstance().getTime(),"Approve fixed deposit account"+fixedDepositAccount.getAccountNumber(),customerID, staff);
            em.persist(action);
            List<StaffAction> staffActions=staff.getStaffActions();
            staffActions.add(action);
            staff.setStaffActions(staffActions);
            em.persist(staff);
            em.flush();
            
            try {
            sendFixedEmail(customer.getName(),customer.getEmail(),tempPassword,fixedDepositAccount.getAccountNumber(),fixedDepositAccount.getAmount());
            } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
            return true;
        }
        else if (result.equals("reject")){
            customer.setStatus("terminated");
            StaffAction action=new StaffAction(Calendar.getInstance().getTime(),"Reject fixed deposit account"+fixedDepositAccount.getAccountNumber(),customerID, staff);
            em.persist(action);
            List<StaffAction> staffActions=staff.getStaffActions();
            staffActions.add(action);
            staff.setStaffActions(staffActions);
            em.persist(staff);
            em.flush();
           try {
            sendRejectVerificationEmail(customer.getName(),customer.getEmail());
            } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
            return true;
        }
        else return false;
}

 private void sendEmail(String name, String email, String password,Long accountNumber,BigDecimal minAmount) throws MessagingException {
        String subject = "Merlion Bank - Online Banking Account \"" + name + "\" Created - Pending Activation";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Congratulations! You have successfully registered a Merlion Online Banking Account!</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Saving Account Number: " + accountNumber
                + "<br />Temporary Password: " + password + "<br />Please activate your account through activation link on the Login page " + "</h2><br />" 
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to transfer minimum"+minAmount+" to your account in order to activate your saving account. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }
 
 private void sendFixedEmail(String name, String email, String password,Long accountNumber,BigDecimal minAmount) throws MessagingException {
        String subject = "Merlion Bank - Online Banking Account \"" + name + "\" Created - Pending Activation";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Congratulations! You have successfully registered a Merlion Online Banking Account!</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Fixed Deposit Account Number: " + accountNumber
                + "<br />Temporary Password: " + password + "<br />Please activate your account through activation link on the Login page " + "</h2><br />" 
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to transfer minimum"+minAmount+" to your account within seven days. Otherwise your account will be terminated. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }


private void sendRejectVerificationEmail(String name, String email) throws MessagingException {
      String subject = "Merlion Bank - Online Banking Account \"" + name + "\" Application Rejected";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Sorry! You application of Merlion Bank account has been rejected.</h1><br />"
                + "<p style=\"color: #ff0000;\">For more enquiries, please kindly contact +65 8888 8888. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
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

}
