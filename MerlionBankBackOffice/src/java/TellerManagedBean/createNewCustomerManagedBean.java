/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TellerManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import CommonEntity.Staff;
import Exception.EmailNotSendException;
import Exception.UserExistException;
import StaffManagement.staffLogInManagedBean;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ruijia
 */
@Named(value = "createNewCustomerManagedBean")
@SessionScoped
public class createNewCustomerManagedBean implements Serializable {

    @EJB
    AccountManagementSessionBeanLocal amsb;

    private Customer customer;
    private String ic;
    private String customerName;
    private String customerGender;
    private Date customerDateOfBirth;
    private String customerAddress;
    private String customerEmail;
    private String customerPhoneNumber;

    private String customerOccupation;
    private String customerFamilyInfo;
    private String customerFinancialAsset;
    private String customerFinancialGoal;
    private Staff staff;
    private String password;

    @Inject
    private ServiceCustomerManagedBean serviceCustomerManagedBean;

    @Inject
    private staffLogInManagedBean staffLogInManagedBean;

    /**
     * Creates a new instance of createNewCustomerManagedBean
     */
    
    

    @PostConstruct
    public void init(){
    staff = staffLogInManagedBean.getStaff();
    }
    
    public createNewCustomerManagedBean() {
    }

    public void createNewCustomer(ActionEvent event) throws UserExistException, EmailNotSendException, IOException {

        if (ic != null && customerName != null && customerGender != null && customerDateOfBirth != null && customerAddress != null && customerEmail != null && customerPhoneNumber != null && customerOccupation != null && customerFamilyInfo != null) {
            if (FacesContext.getCurrentInstance().getResponseComplete()) {
                System.out.println("lala");
                return;
            }
            try {
                System.out.println("ahdhdhdhdaad ");
                amsb.tellerCreateFixedDepositAccount(ic, customerName, customerGender, customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo, password);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Customer created Successfully");

                RequestContext.getCurrentInstance().showMessageInDialog(message);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffDashboard.xhtml");

            } catch (UserExistException ex) {
                System.out.println(ex.getMessage());
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());

                RequestContext.getCurrentInstance().showMessageInDialog(message);

            } catch (EmailNotSendException ex1) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex1.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {
            System.out.println("Message from managed bean: please do not leave blanks!");
        }
        //log a staff action
        
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
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

    public String getCustomerFinancialAsset() {
        return customerFinancialAsset;
    }

    public void setCustomerFinancialAsset(String customerFinancialAsset) {
        this.customerFinancialAsset = customerFinancialAsset;
    }

    public String getCustomerFinancialGoal() {
        return customerFinancialGoal;
    }

    public void setCustomerFinancialGoal(String customerFinancialGoal) {
        this.customerFinancialGoal = customerFinancialGoal;
    }

}
