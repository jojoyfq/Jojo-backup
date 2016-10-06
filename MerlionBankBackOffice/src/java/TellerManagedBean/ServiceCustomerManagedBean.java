/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TellerManagedBean;

import CommonEntity.Customer;
import CustomerRelationshipEntity.Session.OperationalCRMSessionBeanLocal;
import Exception.UserNotExistException;
import StaffManagement.staffLogInManagedBean;
import TellerServeCustomer.Session.ServiceCustomerSessionBeanLocal;
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
@Named(value = "serviceCustomerManagedBean")
@SessionScoped
public class ServiceCustomerManagedBean implements Serializable {

    @EJB
    ServiceCustomerSessionBeanLocal scsb;
    @EJB
    OperationalCRMSessionBeanLocal ocsbl;
    @Inject
    staffLogInManagedBean slimb;

    /**
     * Creates a new instance of serviceCustomerManagedBean
     */
    public ServiceCustomerManagedBean() {
    }

    private Customer customer;
    private String customerIc;
    private Long staffId;
    private Long customerId;
    private Date birthDate;
    private String customerName;
   private String address;
    private String email;
    private String phoneNumber;
    private String occupation;
    private String familyInfo;
    private String financialGoal;
    private String gender;

    @PostConstruct
    public void init() {
        customer = new Customer();
        staffId = slimb.getStaffId();
   //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }
public void goToModifyCustomerProfile(){
try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/StaffVerifyCustomer/modifyCustomerProfile.xhtml");
        } catch (Exception ex) {
            System.out.print("Redirect to Home Page Encounter Error!");
        }
}
//    public void selectCustomer(ActionEvent event) throws IOException {
//        if (customerIc != null) {
//            if (!scsb.selectCustomer(customerIc).equals(false)) {
//                this.customer = (Customer) scsb.selectCustomer(customerIc);
//                System.out.print(customerIc);
//                System.out.print("customer set!");
//                FacesContext.getCurrentInstance().getExternalContext()
//                        .redirect("/MerlionBankBackOffice/TellerManagement/serviceCustomerSuccess.xhtml");
//
//            } else {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Customer not found. Please enter correct IC.");
//                RequestContext.getCurrentInstance().showMessageInDialog(message);
//            }
//        } else {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please enter customer IC");
//            RequestContext.getCurrentInstance().showMessageInDialog(message);
//        }
//    }

    public void deselectCustomer(ActionEvent event) throws IOException {
        this.customer = null;
        System.out.print("customer cleared!");
        //redirect to homepage
    }

    public void goToServiceCustomerPage(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/TellerManagement/ServiceCustomer.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to ServiceCustomer page fails");
        }
    }

    public void searchCustomer(ActionEvent event) throws UserNotExistException, IOException {
        try {
            System.out.println("**********Searched Customer ic is " + customerIc);
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

            System.out.println("*******Searched customer id is " + customerId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/TellerManagement/serviceCustomerSuccess.xhtml");

        } catch (UserNotExistException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void staffUpdateCustomerProfile(ActionEvent event) {

        ocsbl.updateProfile(staffId, customerId, customerIc, customerName, birthDate,
                address, email, phoneNumber, occupation, familyInfo, financialGoal);
        FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Customer profile updated successfully!");
        RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
    }
    public ServiceCustomerSessionBeanLocal getScsb() {
        return scsb;
    }

    public void setScsb(ServiceCustomerSessionBeanLocal scsb) {
        this.scsb = scsb;
    }

    public OperationalCRMSessionBeanLocal getOcsbl() {
        return ocsbl;
    }

    public void setOcsbl(OperationalCRMSessionBeanLocal ocsbl) {
        this.ocsbl = ocsbl;
    }

    public staffLogInManagedBean getSlimb() {
        return slimb;
    }

    public void setSlimb(staffLogInManagedBean slimb) {
        this.slimb = slimb;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

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

}
