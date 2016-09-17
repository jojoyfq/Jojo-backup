/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.CustomerMessage;
import CommonEntity.MessageEntity;
import CommonEntity.Session.InboxManagementSessionBeanLocal;
import CommonEntity.Staff;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author apple
 */
@Named(value = "messageManagedBean")
@SessionScoped
public class MessageManagedBean implements Serializable {

    @EJB
    InboxManagementSessionBeanLocal imsbl;

    //maintain the log in function from LogInManagedBean
    @Inject
    private LogInManagedBean logInManagedBean;
    @Inject
    private StaffMessageManagedBean staffMessageManagedBean;
//setter for LogInManagedBean

    public void setStaffMessageManagedBean(StaffMessageManagedBean staffMessageManagedBean) {
        this.staffMessageManagedBean = staffMessageManagedBean;
    }

    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
        this.logInManagedBean = logInManagedBean;
    }

    private String customerIc = "S4444";
    private Long staffId;
    private String messageSubject;
    private String content;
    private Customer customer;
    private Long customerId = 2L;
    private Staff staff;
    private List<MessageEntity> messages;
    private Long messageId;
    private MessageEntity message;
    private String customerName;
    private String status;
    private String customerReplyContent;
    private String customerReplyMsgStatus = "new";
    private int customerUnreadMsg;
    private CustomerMessage msgAddToStaff;

    public CustomerMessage getMsgAddToStaff() {
        return msgAddToStaff;
    }

    public void setMsgAddToStaff(CustomerMessage msgAddTostaff) {
        this.msgAddToStaff = msgAddTostaff;
    }

    public int getCustomerUnreadMsg() {
        return customerUnreadMsg;
    }

    public void setCustomerUnreadMsg(int customerUnreadMsg) {
        this.customerUnreadMsg = customerUnreadMsg;
    }

    public String getCustomerReplyMsgStatus() {
        return customerReplyMsgStatus;
    }

    public void setCustomerReplyMsgStatus(String customerReplyMsgStatus) {
        this.customerReplyMsgStatus = customerReplyMsgStatus;
    }

    public String getCustomerReplyContent() {
        return customerReplyContent;
    }

    public void setCustomerReplyContent(String customerReplyContent) {
        this.customerReplyContent = customerReplyContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

        System.err.println("************ ic: " + logInManagedBean.getIc());

        staff = new Staff();
        customer = new Customer();
//        customerId = customer.getId();
        staffId = staff.getId();
        message = new MessageEntity();
        messages = new ArrayList<>();
        //customerId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
//      *******This is going to be used in the future!!!**********
//        customerId = logInManagedBean.getCustomerId();
//        customerIc = logInManagedBean.getIc();
//        customerName = logInManagedBean.getCustomerName();

        System.out.println("Logged in customer IC is : " + customerId);
        //       messages = imsbl.viewAllMessage(customerId);
//        System.out.println("message size is: "+messages.size());
        customerUnreadMsg = imsbl.countNewMessage(customerId);

    }

    public void customerViewAllMessages(ActionEvent event) throws IOException, ListEmptyException {

        try {
            messages = imsbl.viewAllMessage(customerId);
        } catch (ListEmptyException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
        //  FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/customerViewMessage.xhtml");
    }

    public MessageEntity customerReadMessage(ActionEvent event) throws IOException {
        message = (MessageEntity) event.getComponent().getAttributes().get("selectedMessage");
        System.err.println("********** message.getId(): " + message.getId());
        //messageId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("MessageID");
        //  FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/displaySingleMessage.xhtml");
//        messageId = 6L;
        message = imsbl.readMessage(message.getId());
        messageId = message.getId();
        messageSubject = message.getSubject();
        content = message.getContent();
        staffId = message.getStaff().getId();
        status = message.getStatus();
        return message;
    }

    public void customerDeleteMessage(Long msgID) throws IOException, ListEmptyException {

        try {
            boolean checkDelete = imsbl.deleteMessage(msgID);

            if (checkDelete == false) {
                System.out.println("Message deleted unsuccessfully!!!!");
            }
            System.out.println("delete message check: " + customerId);
            messages = imsbl.viewAllMessage(customerId);
        // FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/customerViewMessage.xhtml");
        } catch (ListEmptyException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void customerReplyMessage(ActionEvent event) throws EmailNotSendException {
        try {
            //    message = (MessageEntity) event.getComponent().getAttributes().get("selectedMessage");
            System.err.println("********** message.getId(): " + messageId);
            System.err.println("********** message.getMessageSubject(): " + messageSubject);
            System.err.println("********** message.getContent: " + content);
            System.err.println("********** message.getStaffId(): " + staffId);
            msgAddToStaff = imsbl.customerSendMessage(message.getSubject(), customerReplyContent, message.getStatus(), staffId, customerId);
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your reply has been successfully sent!");
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            staffMessageManagedBean.getCustomerMessages().add(msgAddToStaff);

        } catch (EmailNotSendException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public MessageEntity replyMessage(ActionEvent event) {
        message = (MessageEntity) event.getComponent().getAttributes().get("selectedMessage");
        System.err.println("********** message.getId(): " + message.getId());
        System.out.println("***********Message from Managed Bean: ");
        //   replyID = msg.getSender().getId();
        System.out.println("Subject is " + message.getSubject());
        // sendID = msg.getReceiverId();
        System.out.println("Staff ID is  " + message.getStaff().getId());
        //   replyTitle = "RE: " + msg.getTitle();
        return message;
    }

    public void countCusotmerUnreadEmail() {

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
}
