/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffManagement;

import CommonEntity.Customer;
import CommonEntity.CustomerMessage;
import CommonEntity.MessageEntity;
import CommonEntity.Session.InboxManagementSessionBeanLocal;
import CommonEntity.Staff;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.UnexpectedErrorException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
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
@Named(value = "staffMessageManagedBean")
@SessionScoped
public class StaffMessageManagedBean implements Serializable {

    @EJB
    InboxManagementSessionBeanLocal imsbl;

    //maintain the log in function from LogInManagedBean
    // @Inject
    //  private LogInManagedBean logInManagedBean;
    @Inject
    private staffLogInManagedBean slimb;
  //  @Inject
    //  private MessageManagedBean messageManagedBean;
//setter for LogInManagedBean

    public staffLogInManagedBean getSlimb() {
        return slimb;
    }

    public void setSlimb(staffLogInManagedBean slimb) {
        this.slimb = slimb;
    }

//    public void setMessageManagedBean(MessageManagedBean messageManagedBean) {
//        this.messageManagedBean = messageManagedBean;
//    }
//
    /**
     * Creates a new instance of StaffMessageManagedBean
     */
    public StaffMessageManagedBean() {
    }

//    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
//        this.logInManagedBean = logInManagedBean;
//    }
    private String customerIc;
    private Long staffId;
    private String messageSubject;
    private String content;
    private Customer customer;
    private Staff staff;
    private Long customerId;
    private List<CustomerMessage> customerMessages;
    private Long messageId;
    private CustomerMessage customerMessage;
    private String customerName;
    private String status;
    private String customerReplyContent;
    private String customerReplyMsgStatus = "new";
    private int staffUnreadMsg;
    private String staffReplyContent;
    private MessageEntity msgAddToCustomer;
    private Long selectedCustomerId;

    public Long getSelectedCustomerId() {
        return selectedCustomerId;
    }

    public void setSelectedCustomerId(Long selectedCustomerId) {
        this.selectedCustomerId = selectedCustomerId;
    }

    public MessageEntity getMsgAddToCustomer() {
        return msgAddToCustomer;
    }

    public void setMsgAddToCustomer(MessageEntity msgAddToCustomer) {
        this.msgAddToCustomer = msgAddToCustomer;
    }

//    public List getMessages() {
//        return messages;
//    }
    @PostConstruct
    public void init() {

        System.err.println("************Logged in staff id is: " + slimb.getStaffId());
        staffId = slimb.getStaffId();
        staff = new Staff();
        customer = new Customer();
        msgAddToCustomer = new MessageEntity();
//        customerId = customer.getId();
        // staffId = staff.getId();
        customerMessage = new CustomerMessage();
        customerMessages = new ArrayList<>();
        //customerId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
//      *******This is going to be used in the future!!!**********
//        customerId = logInManagedBean.getCustomerId();
//        customerIc = logInManagedBean.getIc();
//        customerName = logInManagedBean.getCustomerName();

        System.out.println("Logged in Staff IC is : " + staffId);
        //       customerMessages = imsbl.StaffViewAllMessage(staffId);
//        System.out.println("message size is: "+messages.size());
        staffUnreadMsg = imsbl.countStaffNewMessage(staffId);
        System.out.println("*************Number of Unread Message is "+staffUnreadMsg);
       // customerMessages = this.staffViewAllMessages();
    }
public void goIntoStaffVerifyCustomerBeforeSendingMsg(ActionEvent event){
 try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/StaffSelfManagement/verifyCustomer.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to ActivateAccount page fails");
        }

}
    public void staffViewAllMessages(ActionEvent event) throws IOException {
        //  try {
       // customerMessages = this.staffViewAllMessages();
        
        staffId = slimb.getStaffId();
        System.out.println("*******Staff now is "+staffId);
        customerMessages = imsbl.StaffViewAllMessage(staffId);
//        } catch (ListEmptyException ex) {
//            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
//            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
//        }
     //   return customerMessages;
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffSelfManagement/StaffViewAllMessages.xhtml");

        
    }

    public void verifyCustomerDetails(ActionEvent event) throws UserNotExistException, UserNotActivatedException, IOException {
        if (customerIc != null) {
            try {
                customerId = imsbl.verifyCustomer(customerIc);

                System.out.println(customerId + "Customer verification is successful!");

                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffSelfManagement/staffInputMessage.xhtml");

            } catch (UserNotExistException | UserNotActivatedException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());

                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {

            System.out.println("Please do not leave any blanks");
        }
    }
//     public void customerViewAllMessages(ActionEvent event) throws IOException {
//
//        customerMessages = imsbl.StaffViewAllMessage(staffId);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/customerViewMessage.xhtml");
//    }

    public void sendMessageToCustomer(ActionEvent event) throws EmailNotSendException {
        if (messageSubject != null && content != null) {
            try {
                imsbl.sendMessage(customerId, staffId, messageSubject, content);
            } catch (EmailNotSendException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());

                RequestContext.getCurrentInstance().showMessageInDialog(message);
                System.out.println(ex.getMessage());
            }
        }
    }

    public CustomerMessage staffReadMessage(ActionEvent event) throws IOException {

        customerMessage = (CustomerMessage) event.getComponent().getAttributes().get("selectedMessage");

        System.out.println("********customer message content is " + customerMessage.getContent());
        System.err.println("********** message.getId(): " + customerMessage.getId());

        customerMessage = imsbl.readCustomerMessage(customerMessage.getId());
        messageId = customerMessage.getId();
        messageSubject = customerMessage.getSubject();
        content = customerMessage.getContent();
        status = customerMessage.getStatus();

        return customerMessage;

    }

    public void staffReplyMessage(ActionEvent event) throws EmailNotSendException {
        try {
            selectedCustomerId = customerMessage.getCustomer().getId();
            //customerMessage = (CustomerMessage) event.getComponent().getAttributes().get("selectedMessage");
            System.err.println("********** customer message.getId(): " + customerMessage.getId());
            System.err.println("********** customer Id: " + selectedCustomerId);
            System.err.println("********** customer message subject: " + customerMessage.getSubject());

            System.err.println("********** staff reply content: " + staffReplyContent);

            msgAddToCustomer = imsbl.sendMessage(selectedCustomerId, staffId, customerMessage.getSubject(), staffReplyContent);
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your reply has been successfully sent!");
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            //   messageManagedBean.getMessages().add(msgAddToCustomer);
        } catch (EmailNotSendException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void replyMessage(ActionEvent event) {
        customerMessage = (CustomerMessage) event.getComponent().getAttributes().get("selectedMessage");
        System.err.println("********** customer message.getId(): " + customerMessage.getId());
        System.out.println("***********Message from Managed Bean: ");
        //   replyID = msg.getSender().getId();
        System.out.println("Subject is " + customerMessage.getSubject());
        // sendID = msg.getReceiverId();
        System.out.println("Customer ID is  " + customerMessage.getCustomer().getId());
        //   replyTitle = "RE: " + msg.getTitle();
    }

    public void staffDeleteMessage(Long messageId) throws UnexpectedErrorException {

        try {
            customerMessages = imsbl.deleteCustomerMessage(messageId, staffId);

            //   customerMessages = imsbl.StaffViewAllMessage(staffId);
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Message deleted successfully");
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

        } catch (UnexpectedErrorException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }
    public int countStaffUnreadEmail() {
        staffUnreadMsg = imsbl.countStaffNewMessage(staffId);

        return staffUnreadMsg;
    }

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

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<CustomerMessage> getCustomerMessages() {
        return customerMessages;
    }

    public void setCustomerMessages(List<CustomerMessage> customerMessages) {
        this.customerMessages = customerMessages;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public CustomerMessage getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(CustomerMessage customerMessage) {
        this.customerMessage = customerMessage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStaffReplyContent() {
        return staffReplyContent;
    }

    public void setStaffReplyContent(String staffReplyContent) {
        this.staffReplyContent = staffReplyContent;
    }

    public int getStaffUnreadMsg() {
        return staffUnreadMsg;
    }

    public void setStaffUnreadMsg(int customerUnreadMsg) {
        this.staffUnreadMsg = customerUnreadMsg;
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

}
