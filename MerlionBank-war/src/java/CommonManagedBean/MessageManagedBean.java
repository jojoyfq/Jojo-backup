/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.MessageEntity;
import CommonEntity.Session.InboxManagementSessionBeanLocal;
import CommonEntity.Staff;
import Exception.EmailNotSendException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author apple
 */
@Named(value = "messageManagedBean")
@SessionScoped
public class MessageManagedBean implements Serializable {

    @EJB
    InboxManagementSessionBeanLocal imsbl;

    private String customerIc;
    private Long staffId;
    private String messageSubject;
    private String content;
    private Customer customer;
    private Long customerId;
    private Staff staff;
    private List<MessageEntity> messages;
    private Long messageId;
    private MessageEntity message;
    private String customerName;

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
//    public List getMessages() {
//        return messages;
//    }
    /**
     * Creates a new instance of MessageManagedBean
     */
    public MessageManagedBean() {
    }

    @PostConstruct
    public void init() {
        staff = new Staff();
        customer = new Customer();
//        customerId = customer.getId();
        staffId = staff.getId();
        message = new MessageEntity();
        messages = new ArrayList<>();
        //customerId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
        customerId = 2L;
        customerIc = "S9276";
        customerName = "gao";

        System.out.println("Logged in customer IC is : " + customerId);
       messages = imsbl.viewAllMessage(customerId);
//        System.out.println("message size is: "+messages.size());

    }
    public void customerViewAllMessages(ActionEvent event) throws IOException{
          
          messages = imsbl.viewAllMessage(customerId);
          FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/customerViewMessage.xhtml");
    }

    public void verifyCustomerDetails(ActionEvent event) throws UserNotExistException, UserNotActivatedException, IOException {
        if (customerIc != null) {
            try {
                Long msg = imsbl.verifyCustomer(customerIc);

                System.out.println(msg + "Customer verification is successful!");

                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/staffInputMessage.xhtml");

            } catch (UserNotExistException | UserNotActivatedException ex) {
                System.out.println(ex.getMessage());
            }
        } else {

            System.out.println("Please do not leave any blanks");
        }
    }

    public void sendMessageToCustomer(ActionEvent event) throws EmailNotSendException {
        if (messageSubject != null && content != null) {
            try {
                imsbl.sendMessage(customerId, staffId, messageSubject, content);
            } catch (EmailNotSendException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public MessageEntity customerReadMessage(ActionEvent event) throws IOException {
        //messageId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("MessageID");
      //  FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/displaySingleMessage.xhtml");
        messageId = 6L;
        message = imsbl.readMessage(messageId);
        return message;
    }
    public void customerDeleteMessage(Long msgID) throws IOException {
    
           boolean checkDelete = imsbl.deleteMessage(msgID);
          
           if (checkDelete == false) {
            System.out.println("Message deleted unsuccessfully!!!!");
        }
           System.out.println("delete message check: "+customerId);
              messages = imsbl.viewAllMessage(customerId);
             // FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/customerViewMessage.xhtml");
        
    }

//    public void customerViewAllMessage(ActionEvent event){
//       //customerId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
//       
//      //  messages.addAll(imsbl.viewAllMessage(customerId));
//    }
////    public void customerReadOneMessage(ActionEvent event){
////        messageId=(Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get('message').getMessageId();
////        
////    }
//    public void goToHomeMessage(ActionEvent event) throws IOException{
//         try{ FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/customerViewMessage.xhtml");
//         }catch(IOException ex){
//             System.out.println(ex.getMessage());
//         }
//         
//    }
    public String getCustomerIc() {
        return customerIc;
    }

    public void setCustomerIc(String customerIc) {
        this.customerIc = customerIc;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

}
