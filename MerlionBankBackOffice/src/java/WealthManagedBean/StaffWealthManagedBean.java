/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.Session.LoanApplicationSessionBeanLocal;
import StaffManagement.staffLogInManagedBean;
import WealthEntity.DiscretionaryAccount;
import WealthEntity.Session.WealthApplicationSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.model.UploadedFile;

/**
 *
 * @author apple
 */
@Named(value = "staffWealthManagedBean")
@SessionScoped
public class StaffWealthManagedBean implements Serializable{

    /**
     * Creates a new instance of StaffWealthManagedBean
     */
    public StaffWealthManagedBean() {
    }

    private Customer customer;
    private Customer searchedCustomer;
    private String customerIc;
    private String customerName;
    private String customerGender;
    private Date customerDateOfBirth;
    private String customerAddress;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerOccupation;
    private String customerFamilyInfo;
    private String password;
    private Long staffId;
    private Long customerId;
    UploadedFile file;
    private List<DiscretionaryAccount> pendingDisAccount;
    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    @EJB
    private WealthApplicationSessionBeanLocal wasbl;
    @EJB
    AccountManagementSessionBeanLocal amsbl;
    @Inject
    private staffLogInManagedBean slimb;
    @EJB
    LoanApplicationSessionBeanLocal lasbl;

    @PostConstruct
    public void init() {
        staffId = slimb.getStaffId();
        pendingDisAccount = new ArrayList<>();
    }

    public void goToApplyWealthAccountExistCustomer(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/WealthManagement/createWealthForExistingCustomer.xhtml");

    }
     public void goToApplyWealthAccountNewCustomer(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/WealthManagement/createWealthForNewCustomer.xhtml");

    }

    public void staffCreateWealthForNewCustomer(ActionEvent event) throws UserExistException, EmailNotSendException, IOException {
        try {
            System.out.println("**********Staff id is "+staffId);
            customer = wasbl.tellerCreateDiscretionaryAccount(staffId, customerIc, customerName, customerGender, customerDateOfBirth, password, customerEmail, customerName, customerOccupation, customerFamilyInfo, password);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Discretionary Account has been successfully created for customer " + customer.getIc() + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

            //   FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/LoanManagement/displayLoanTypes.xhtml");
        } catch (UserExistException | EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void searchCustomerByIc(ActionEvent event) throws UserNotExistException, UserNotActivatedException {
        try {
            searchedCustomer = lasbl.searchCustomer(customerIc);
        } catch (UserNotExistException | UserNotActivatedException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void existingCustomerCreateWealthAcct(ActionEvent event) throws EmailNotSendException {
        try {
            System.out.println("********Customer Id from search result is " + searchedCustomer.getId());
            wasbl.staffCreateDiscretionaryAccountExistingCustomer(staffId, searchedCustomer.getId());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Discretionary Account has been successfully created for customer " + customerIc + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
// public void staffViewPendingWealthApplications(ActionEvent event) throws IOException {
//        System.out.println("**** go to view pending loans alr!");
//        pendingDisAccount = wasbl.();
//        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/viewPendingLoans.xhtml");
//
//    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCustomerIc() {
        return customerIc;
    }

    public void setCustomerIc(String customerIc) {
        this.customerIc = customerIc;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public Date getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(Date customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerOccupation() {
        return customerOccupation;
    }

    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }

    public String getCustomerFamilyInfo() {
        return customerFamilyInfo;
    }

    public void setCustomerFamilyInfo(String customerFamilyInfo) {
        this.customerFamilyInfo = customerFamilyInfo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public WealthApplicationSessionBeanLocal getWasbl() {
        return wasbl;
    }

    public void setWasbl(WealthApplicationSessionBeanLocal wasbl) {
        this.wasbl = wasbl;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public AccountManagementSessionBeanLocal getAmsbl() {
        return amsbl;
    }

    public void setAmsbl(AccountManagementSessionBeanLocal amsbl) {
        this.amsbl = amsbl;
    }

    public staffLogInManagedBean getSlimb() {
        return slimb;
    }

    public void setSlimb(staffLogInManagedBean slimb) {
        this.slimb = slimb;
    }

    public Customer getSearchedCustomer() {
        return searchedCustomer;
    }

    public void setSearchedCustomer(Customer searchedCustomer) {
        this.searchedCustomer = searchedCustomer;
    }

    public LoanApplicationSessionBeanLocal getLasbl() {
        return lasbl;
    }

    public void setLasbl(LoanApplicationSessionBeanLocal lasbl) {
        this.lasbl = lasbl;
    }

    

}
