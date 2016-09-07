/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import CommonEntity.MessageEntity;
import CommonEntity.Staff;
import Exception.EmailNotSendException;
import Exception.UserNotExistException;
import Other.Session.sendEmail;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author a0113893
 */
@Stateless
public class InboxManagementSessionBean implements InboxManagementSessionBeanLocal {
 private EntityManager em;
    // staff send customer message
  @Override  
 public boolean sendMessage(Long customerId,String staffID, String subject,String content)throws EmailNotSendException{
      Query query = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        query.setParameter("id", staffID);
        Staff staff = (Staff)query.getSingleResult(); 
        
        
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", customerId);
        Customer customer = (Customer)q.getSingleResult();
        
        MessageEntity internalMessage = new MessageEntity (subject,content,staff,customer);
        em.persist(internalMessage);
        
         try {
            SendEmail(customer.getName(), customer.getEmail());
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
        return true;    
}
 
 //send notification email to customer
 private void SendEmail(String name, String email) throws MessagingException {
        String subject = "Merlion Bank - New message has been sent to your MerlionBank Online Inbox";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  A new message has been sent to your MerlionBank Online Inbox</h1><br />"
                + "<h1>Please Log in to your account and check the new message.</h1>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }
    
}
