/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffManagement;

import CommonEntity.Customer;
import CustomerRelationshipEntity.Session.OperationalCRMSessionBeanLocal;
import Exception.UserNotExistException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
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
 * @author a0113893
 */
@Named(value = "staffModifyCustomerManagedBean")
@SessionScoped
public class StaffModifyCustomerManagedBean implements Serializable {

    private String customerIc;
    private Long staffId;
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    private Customer customer;
    private Date birthDate;

    private String customerName;
    private String address;
    private String email;
    private String phoneNumber;
    private String occupation;
    private String familyInfo;
    private String financialGoal;
    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
          

    @EJB
    OperationalCRMSessionBeanLocal ocsbl;
    @Inject
    staffLogInManagedBean slimb;

    /**
     * Creates a new instance of StaffModifyCustomerManagedBean
     */
    public StaffModifyCustomerManagedBean() {
    }
    
    
@PostConstruct
    public void init() {
        customer = new Customer();
        staffId = slimb.getStaffId();

    }

    public void searchCustomer(ActionEvent event) throws UserNotExistException, IOException {
        try {
System.out.println("**********Searched Customer ic is "+customerIc);
            customer = ocsbl.searchCustomer(customerIc);
            customerId = customer.getId();
            customerIc = customer.getIc();
            customerName = customer.getName();
            birthDate = customer.getDateOfBirth();
            address = customer.getAddress();
            email = customer.getEmail();
            phoneNumber = customer.getPhoneNumber();
            occupation = customer.getOccupation();
            familyInfo = customer.getFamilyInfo();
            financialGoal = customer.getFinancialGoal();
            gender = customer.getGender();
                    
            System.out.println("*******Searched customer id is "+customerId);
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffVerifyCustomer/modifyCustomerProfile.xhtml");

        } catch (UserNotExistException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }
    public void staffUpdateCustomerProfile(ActionEvent event){
        
        ocsbl.updateProfile(staffId, customerId, customerIc, email, birthDate, 
                address, email, phoneNumber, occupation, familyInfo, financialGoal);
       FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Customer profile updated successfully!");
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getFamilyInfo() {
        return familyInfo;
    }

    public void setFamilyInfo(String familyInfo) {
        this.familyInfo = familyInfo;
    }

    public String getFinancialGoal() {
        return financialGoal;
    }

    public void setFinancialGoal(String financialGoal) {
        this.financialGoal = financialGoal;
    }

   
    public staffLogInManagedBean getSlimb() {
        return slimb;
    }

    public void setSlimb(staffLogInManagedBean slimb) {
        this.slimb = slimb;
    }
}
