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
import CustomerRelationshipEntity.CaseEntity;
import CustomerRelationshipEntity.Issue;
import CustomerRelationshipEntity.Session.CollaborativeCRMSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
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
import org.primefaces.event.RateEvent;

/**
 *
 * @author apple
 */
@Named(value = "messageManagedBean")
@SessionScoped
public class MessageManagedBean implements Serializable {

    @EJB
    InboxManagementSessionBeanLocal imsbl;
    @EJB
    CollaborativeCRMSessionBeanLocal ccsbl;
    //maintain the log in function from LogInManagedBean
    @Inject
    private LogInManagedBean logInManagedBean;
//    @Inject
//    private StaffMessageManagedBean staffMessageManagedBean;
//setter for LogInManagedBean

//    public void setStaffMessageManagedBean(StaffMessageManagedBean staffMessageManagedBean) {
//        this.staffMessageManagedBean = staffMessageManagedBean;
//    }
    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
        this.logInManagedBean = logInManagedBean;
    }

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
    private String status;
    private String customerReplyContent;
    private String customerReplyMsgStatus = "new";
    private int customerUnreadMsg;
    private CustomerMessage msgAddToStaff;
    private String caseSubject;
    private String caseContent;
    private List<CaseEntity> allCases;
    private List<Issue> oneCaseAllIssues;
    private CaseEntity selectedCase;
    private Issue selectedIssue;
    private String issueContent;
    private boolean checkStatus;
    private String caseStatus;
    private CustomerMessage caseMessage;
    private Integer rating;

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
        caseMessage = new CustomerMessage();
        staff = new Staff();
        customer = new Customer();
//        customerId = customer.getId();
        staffId = staff.getId();
        message = new MessageEntity();
        messages = new ArrayList<>();
        allCases = new ArrayList<>();
        //customerId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
//      *******This is going to be used in the future!!!**********
        customerId = logInManagedBean.getCustomerId();
        customerIc = logInManagedBean.getIc();
        customerName = logInManagedBean.getCustomerName();
        oneCaseAllIssues = new ArrayList<>();
        selectedCase = new CaseEntity();
        selectedIssue = new Issue();

        //  customerUnreadMsg = this.customerUnreadMsg;
        System.out.println("Logged in customer IC is : " + customerId);
        //       messages = imsbl.viewAllMessage(customerId);
//        System.out.println("message size is: "+messages.size());
        //      customerUnreadMsg = imsbl.countNewMessage(customerId);

    }

//    public boolean checkIssueStatus(ActionEvent event){
//                selectedIssue = (Issue) event.getComponent().getAttributes().get("selectedIssue");
//                boolean check;
//       check = ccsbl.checkStatus(selectedIssue.getId());
//       return check;
//    }
    public List<Issue> issuesUnderOneCase(ActionEvent event) {
        System.out.println("*********** in issueUnderOneCase function alr!!!!");
        selectedCase = (CaseEntity) event.getComponent().getAttributes().get("selectedCase");
        System.out.println("**********Selected case to view all the issues, selectedCase.getId()" + selectedCase.getId());
        oneCaseAllIssues = ccsbl.retrieveIssues(selectedCase.getId());
        System.out.println("********issue size is " + oneCaseAllIssues.size());
        if (selectedCase.getStatus().equals("moreInfo")) {
            checkStatus = true;
        } else {
            checkStatus = false;
        }
        System.out.println("*************issue status is " + checkStatus);
        return oneCaseAllIssues;

    }

    public void customerViewAllMessages(ActionEvent event) throws IOException, ListEmptyException {

        try {
            messages = imsbl.viewAllMessage(customerId);
        } catch (ListEmptyException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/customerViewMessage.xhtml");
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
            //staffMessageManagedBean.getCustomerMessages().add(msgAddToStaff);

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

    public int countCusotmerUnreadEmail() {
        customerUnreadMsg = imsbl.countNewMessage(customerId);

        return customerUnreadMsg;
    }

    //customer read case message
    public void customerViewAllCaseMessages(ActionEvent event) throws IOException, ListEmptyException {

        try {

            System.out.println("*************Customer id " + customerId);
            allCases = ccsbl.customerViewCases(customerId);
            System.out.println("*************Case size is " + allCases.size());

        } catch (ListEmptyException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CaseManagement/viewAllCases.xhtml");
    }

    //customer create case message
    public CustomerMessage createCaseMessage(ActionEvent event) {
        caseStatus = null;
        caseMessage = imsbl.customerSendCaseMessage(caseSubject, caseContent, caseStatus, customerId);
        caseSubject = null;
        caseContent = null;

        System.out.println("Message Created Successfully!");
        FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Message Created Successfully!");
        RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        
        return caseMessage;
    }

    public void customerModifyIssue(ActionEvent event) {
        selectedIssue = (Issue) event.getComponent().getAttributes().get("selectedIssue");
        System.out.println("************customer Ic modified the content of the issue is " + customerIc);
        System.out.println("************Issue id modified the content of the issue is " + selectedIssue.getId());

        ccsbl.customerModifyIssue(customerId, selectedIssue.getId(), selectedIssue.getContent());

    }

    public void customerRate(RateEvent  event) {
        System.out.println("*********Customer ID to rate issue " + customerId);
                selectedIssue = (Issue) event.getComponent().getAttributes().get("selectedIssue");

        System.out.println("*********Issue ID to be rated  " + selectedIssue.getId());
        ccsbl.customerRateIssue(customerId, selectedIssue.getId(), rating);

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

    public boolean isCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getIssueContent() {
        return issueContent;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    public Issue getSelectedIssue() {
        return selectedIssue;
    }

    public void setSelectedIssue(Issue selectedIssue) {
        this.selectedIssue = selectedIssue;
    }

    public CaseEntity getSelectedCase() {
        return selectedCase;
    }

    public void setSelectedCase(CaseEntity selectedCase) {
        this.selectedCase = selectedCase;
    }

    public List<Issue> getOneCaseAllIssues() {
        return oneCaseAllIssues;
    }

    public void setOneCaseAllIssues(List<Issue> oneCaseAllIssues) {
        this.oneCaseAllIssues = oneCaseAllIssues;
    }

    public List<CaseEntity> getAllCases() {
        return allCases;
    }

    public void setAllCases(List<CaseEntity> allCases) {
        this.allCases = allCases;
    }

    public String getCaseSubject() {
        return caseSubject;
    }

    public void setCaseSubject(String caseSubject) {
        this.caseSubject = caseSubject;
    }

    public String getCaseContent() {
        return caseContent;
    }

    public void setCaseContent(String caseContent) {
        this.caseContent = caseContent;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public CustomerMessage getCaseMessage() {
        return caseMessage;
    }

    public void setCaseMessage(CustomerMessage caseMessage) {
        this.caseMessage = caseMessage;
    }

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

    public InboxManagementSessionBeanLocal getImsbl() {
        return imsbl;
    }

    public void setImsbl(InboxManagementSessionBeanLocal imsbl) {
        this.imsbl = imsbl;
    }

    public CollaborativeCRMSessionBeanLocal getCcsbl() {
        return ccsbl;
    }

    public void setCcsbl(CollaborativeCRMSessionBeanLocal ccsbl) {
        this.ccsbl = ccsbl;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

}
