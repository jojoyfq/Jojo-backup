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
import Exception.UserNotActivatedException;
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
 
 //1st - staff choose customer before send message
 @Override
 public Long verifyCustomer(String customerIc) throws UserNotExistException, UserNotActivatedException{
     Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", customerIc);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (temp.isEmpty()) {
            System.out.println("Username " + customerIc + " does not exist!");
            throw new UserNotExistException("Username " + customerIc + " does not exist, please try again");
        }
        
            int size=temp.size();
            Customer customer=temp.get(size-1);
            if (customer.getStatus().equals("terminated")){
                 System.out.println("Username " + customerIc + " does not exist!");
            throw new UserNotExistException("Username " + customerIc + " does not exist, please try again");    
            }
            else if (customer.getStatus().equals("inactive")){
                 System.out.println("Username " + customerIc + "Customer has not activated his or her account!"); 
             throw new UserNotActivatedException("Username " + customerIc + "Customer has not activated his or her account!");
            }
            else {
              System.out.println("Username " + customerIc + " IC check pass!");  
            }
            return customer.getId();
            
 }
    // 2nd- staff send customer message
  @Override  
 public boolean sendMessage(Long customerId,String staffID, String subject,String content)throws EmailNotSendException{
      Query query = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        query.setParameter("id", staffID);
        Staff staff = (Staff)query.getSingleResult(); 
        
        
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", customerId);
        Customer customer = (Customer)q.getSingleResult();
        
        MessageEntity internalMessage = new MessageEntity (subject,content,staff,customer,"new");
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
    
 // customer view list of message
 @Override
 public List viewAllMessage(Long customerId){
    Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", customerId);
        Customer customer = (Customer)q.getSingleResult();      
        return customer.getMessages();
     
 }
 
// customer update status from new to read
 @Override
 public boolean readMessage(Long customerId, Long messageID){
     
 }
 
 @Override
 public boolean deleteMessage(Long customerID, Long messageID){
     
     
 }
 
 
 
}
