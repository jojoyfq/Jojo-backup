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
    @Inject
    private LogInManagedBean logInManagedBean;
//setter for LogInManagedBean

    /**
     * Creates a new instance of StaffMessageManagedBean
     */
    public StaffMessageManagedBean() {
    }

    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
        this.logInManagedBean = logInManagedBean;
    }

    private String customerIc = "S4444";
    private Long staffId = 4L;
    private String messageSubject;
    private String content;
    private Customer customer;
    private Staff staff;
    private Long customerId = 2L;

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
    private List<CustomerMessage> customerMessages;
    private Long messageId;
    private CustomerMessage customerMessage;
    private String customerName;
    private String status;
    private String customerReplyContent;
    private String customerReplyMsgStatus = "new";
    private int customerUnreadMsg;
    private String staffReplyContent;

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
    @PostConstruct
    public void init() {

        System.err.println("************ ic: " + logInManagedBean.getIc());

        staff = new Staff();
        customer = new Customer();
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
        customerMessages = imsbl.StaffViewAllMessage(staffId);
//        System.out.println("message size is: "+messages.size());
        customerUnreadMsg = imsbl.countStaffNewMessage(staffId);

    }

//    public void customerViewAllMessages(ActionEvent event) throws IOException {
//
//        messages = imsbl.viewAllMessage(customerId);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/customerViewMessage.xhtml");
//    }
    public void verifyCustomerDetails(ActionEvent event) throws UserNotExistException, UserNotActivatedException, IOException {
        if (customerIc != null) {
            try {
                Long msg = imsbl.verifyCustomer(customerIc);

                System.out.println(msg + "Customer verification is successful!");

                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/staffInputMessage.xhtml");

            } catch (UserNotExistException | UserNotActivatedException ex) {
              FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message",ex.getMessage());

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
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message",ex.getMessage());

                RequestContext.getCurrentInstance().showMessageInDialog(message);
                System.out.println(ex.getMessage());
            }
        }
    }

    public CustomerMessage staffReadMessage(ActionEvent event) throws IOException {

        customerMessage = (CustomerMessage) event.getComponent().getAttributes().get("selectedMessage");
        System.err.println("********** message.getId(): " + customerMessage.getId());

        boolean check = imsbl.readCustomerMessage(customerMessage.getId());

    }

    public void staffReplyMessage(ActionEvent event) throws EmailNotSendException {
        try {
            customerMessage = (CustomerMessage) event.getComponent().getAttributes().get("selectedMessage");
            System.err.println("********** customer message.getId(): " + customerMessage.getId());
            imsbl.sendMessage(customerId, staffId, customerMessage.getSubject(), staffReplyContent);
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

}
