/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerMessage;
import CommonEntity.MessageEntity;
import CommonEntity.Staff;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.UnexpectedErrorException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import Other.Session.sendEmail;
import java.util.ArrayList;
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
public class InboxManagementSessionBean implements InboxManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    //1st - staff choose customer before send message
    @Override
    public Long verifyCustomer(String customerIc) throws UserNotExistException, UserNotActivatedException {
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

    // 2nd- staff send customer message

    @Override
    public MessageEntity sendMessage(Long customerId, Long staffID, String subject, String content) throws EmailNotSendException {

        Query query = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        query.setParameter("id", staffID);
        Staff staff = (Staff) query.getSingleResult();
        System.out.println("@@@@inside:staff.getId() " + staffID);

        System.out.println("inside session bean, customer id: " + customerId);
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", customerId);
        Customer customer = (Customer) q.getSingleResult();
        System.out.println("@@@@inside:customer.getId() " + customerId);

        MessageEntity internalMessage = new MessageEntity(subject, content, staff, customer, "new");
        em.persist(internalMessage);

        List<MessageEntity> messages = customer.getMessages();
        messages.add(internalMessage);
        customer.setMessages(messages);
        em.flush();

        List<MessageEntity> staffMessages = staff.getMessages();
        staffMessages.add(internalMessage);
        staff.setMessages(staffMessages);
        em.flush();
        System.out.println("@@@@inside:case content() " + subject);

        try {
            SendEmail(customer.getName(), customer.getEmail());
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
        System.out.println("@@@@@inside:solution " + content);

        return internalMessage;
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
    public List viewAllMessage(Long customerId) throws ListEmptyException {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        // System.out.println("Customer Ic is "+customerName);
        q.setParameter("id", customerId);
        Customer customer = (Customer) q.getSingleResult();

        //Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        System.out.println("Ccccccccustomer Ic is " + customerId);
        //q.setParameter("id", customerName);
        //List<Customer> temp = new ArrayList(q.getResultList());
        // int size = temp.size();
        // Customer customer = temp.get(size - 1);
        List<MessageEntity> messages = customer.getMessages();
        List<MessageEntity> newMessages = new ArrayList<MessageEntity>();
        for (int i = 0; i < messages.size(); i++) {
            if (!messages.get(i).getStatus().equals("deleted")) {
                newMessages.add(messages.get(i));
            }
        }
        if (newMessages.size() == 0) {
            throw new ListEmptyException("There are no messages!");
        }
        return newMessages;

    }

// customer update status from new to read
    @Override

    public MessageEntity readMessage(Long messageID) {
        System.out.println("Selected Message ID is " + messageID);

        Query q = em.createQuery("SELECT a FROM MessageEntity a WHERE a.id = :id");
        q.setParameter("id", messageID);
        MessageEntity message = (MessageEntity) q.getSingleResult();
        message.setStatus("read");
        em.persist(message);
        return message;
    }

    //customer delete message
    @Override
    public boolean deleteMessage(Long messageID) {
        Query q = em.createQuery("SELECT a FROM MessageEntity a WHERE a.id = :id");
        q.setParameter("id", messageID);
        MessageEntity message = (MessageEntity) q.getSingleResult();
        message.setStatus("deleted");
        em.persist(message);
        em.flush();
        return true;

    }

    //system display number of new messages for customer
    @Override
    public int countNewMessage(Long customerID) {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", customerID);
        Customer customer = (Customer) q.getSingleResult();
        List<MessageEntity> messages = customer.getMessages();
        List<MessageEntity> newMessages = new ArrayList<MessageEntity>();
        int count = 0;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getStatus().equals("new")) {
                count = count + 1;
            }
        }
        return count;

    }

    //customer reply staff message
    @Override
    public CustomerMessage customerSendMessage(String subject, String content, String status, Long staffID, Long customerID) throws EmailNotSendException {
        Query query = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        query.setParameter("id", staffID);
        Staff staff = (Staff) query.getSingleResult();

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", customerID);
        Customer customer = (Customer) q.getSingleResult();

        CustomerMessage customerMessage = new CustomerMessage(subject, content, "new", staff, false, customer);
        em.persist(customerMessage);

        List<CustomerMessage> messages = customer.getCustomerMessages();
        messages.add(customerMessage);
        customer.setCustomerMessages(messages);
        em.flush();

        List<CustomerMessage> staffMessages = staff.getCustomerMessages();
        staffMessages.add(customerMessage);
        staff.setCustomerMessages(staffMessages);
        em.flush();

        try {
            SendStaffNotificationEmail(staff.getStaffName(), staff.getStaffEmail(), customer.getName());
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
        return customerMessage;

    }

    //send notification email to staff
    private void SendStaffNotificationEmail(String staffName, String email, String customerName) throws MessagingException {
        String subject = "Merlion Bank - New message has been sent to your MerlionBank Online Inbox";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + staffName
                + ",</h2><br /><h1>  A new message has been sent to your MerlionBank Online Inbox</h1><br />"
                + "<h1>This new mwssage comes from user: </h1>" + customerName
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

    // staff view list of message
    @Override
    public List<CustomerMessage> StaffViewAllMessage(Long staffId)/*throws ListEmptyException*/ {
        Query query = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        query.setParameter("id", staffId);
        Staff staff = (Staff) query.getSingleResult();
        List<CustomerMessage> messages = staff.getCustomerMessages();
        List<CustomerMessage> newMessages = new ArrayList<CustomerMessage>();
        for (int i = 0; i < messages.size(); i++) {
            if (!messages.get(i).getStatus().equals("deleted")) {
                newMessages.add(messages.get(i));
            }
        }
//        if (newMessages.size()==0)
//            throw new ListEmptyException("There are no messages!");
        return newMessages;

    }

    // staff update status from new to read
    @Override
    public CustomerMessage readCustomerMessage(Long messageID) {
        Query q = em.createQuery("SELECT a FROM CustomerMessage a WHERE a.id = :id");
        q.setParameter("id", messageID);
        CustomerMessage message = (CustomerMessage) q.getSingleResult();
        message.setStatus("read");
        em.persist(message);
        return message;

    }

    //system display number of new messages for staff
    @Override
    public int countStaffNewMessage(Long staffID) {
        Query query = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        query.setParameter("id", staffID);
        Staff staff = (Staff) query.getSingleResult();
        List<CustomerMessage> messages = staff.getCustomerMessages();
        List<CustomerMessage> newMessages = new ArrayList<CustomerMessage>();
        int count = 0;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getStatus().equals("new")) {
                count = count + 1;
            }
        }
        return count;

    }
    @Override
    public MessageEntity diaplayMessage(Long id) {
        Query q = em.createQuery("SELECT a FROM MessageEntity a WHERE a.id = :id");
        q.setParameter("id", id);
        MessageEntity message = (MessageEntity) q.getSingleResult();
        return message;
    }

    //staff delete message
    @Override
    public List<CustomerMessage> deleteCustomerMessage(Long messageID, Long staffId) throws UnexpectedErrorException {
        Query q = em.createQuery("SELECT a FROM CustomerMessage a WHERE a.id = :id");
        q.setParameter("id", messageID);
        CustomerMessage message = (CustomerMessage) q.getSingleResult();
        message.setStatus("deleted");
        em.persist(message);
        em.flush();

        Query query = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        query.setParameter("id", staffId);
        Staff staff = (Staff) query.getSingleResult();

        List<CustomerMessage> currentList = staff.getCustomerMessages();
        List<CustomerMessage> newList = new ArrayList<CustomerMessage>();

        for (int i = 0; i < currentList.size(); i++) {
            if (!currentList.get(i).getStatus().equals("deleted")) {
                newList.add(currentList.get(i));
            }
        }

        if (currentList.size() == newList.size()) {
            throw new UnexpectedErrorException("Staff deletes message fail.");
        }

        return newList;


    }

    //customer compose collaborative message
    @Override
    public CustomerMessage customerSendCaseMessage(String subject, String content, String status, Long customerID) {

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", customerID);
        Customer customer = (Customer) q.getSingleResult();

        CustomerMessage customerMessage = new CustomerMessage(subject, content, "new", null, false, customer);
        em.persist(customerMessage);

        List<CustomerMessage> messages = customer.getCustomerMessages();
        messages.add(customerMessage);
        customer.setCustomerMessages(messages);
        em.flush();

        return customerMessage;

    }

    // staff view list of case message
    @Override
    public List<CustomerMessage> StaffViewAllCaseMessage()/*throws ListEmptyException*/ {
        Query query = em.createQuery("SELECT a FROM CustomerMessage a");
        List<CustomerMessage> caseMessages = new ArrayList(query.getResultList());

        List<CustomerMessage> newMessages = new ArrayList<CustomerMessage>();

        System.out.println("Case message size is " + caseMessages.size());
        for (int i = 0; i < caseMessages.size(); i++) {
            if (!caseMessages.get(i).getStatus().equals("deleted") && (caseMessages.get(i).getStaff() == null)) {
                newMessages.add(caseMessages.get(i));
            }
        }
        System.out.println("New message size is " + newMessages.size());
//        if (newMessages.size()==0)
//            throw new ListEmptyException("There are no messages!");
        return newMessages;

    }

    //system display number of new case messages for staff
    @Override
    public int countStaffNewCaseMessage() {
        Query query = em.createQuery("SELECT a FROM CustomerMessage a WHERE a.staff = :staff");
        query.setParameter("staff", null);
        List<CustomerMessage> caseMessages = new ArrayList(query.getResultList());
        List<CustomerMessage> newMessages = new ArrayList<CustomerMessage>();
        int count = 0;
        for (int i = 0; i < caseMessages.size(); i++) {
            if (caseMessages.get(i).getStatus().equals("new")) {
                count = count + 1;
            }
        }
        return count;

    }


}
