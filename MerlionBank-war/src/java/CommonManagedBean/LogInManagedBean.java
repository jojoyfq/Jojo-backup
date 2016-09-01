/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import Exception.UserExistException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
//import javax.faces.bean.SessionScoped;
import javax.inject.Named;
//import javax.enterprise.context.Dependent;
//import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author apple
 */
@Named(value = "logInManagedBean")
@SessionScoped
public class LogInManagedBean implements Serializable {

    @EJB
    AccountManagementSessionBeanLocal amsbl;

    private Customer customer;
    private String ic = "S9276";
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
    private Customer selectedCustomer;

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

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    /**
     * Creates a new instance of LogInManagedBean
     */
    public LogInManagedBean() {
    }

    @PostConstruct
    public void init() {
        selectedCustomer = new Customer();
        try {
            this.viewOneCustomer();
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public void viewOneCustomer() throws IOException {
        //this.ic = selectedCustomer.getIc();
        selectedCustomer = amsbl.diaplayCustomer(ic);
        System.out.println("Username is " + selectedCustomer);

        // this.ic = selectedCustomer.getIc();
        this.customerName = selectedCustomer.getName();
        this.customerGender = selectedCustomer.getGender();
        this.customerDateOfBirth = selectedCustomer.getDateOfBirth();
        this.customerEmail = selectedCustomer.getEmail();
        this.customerAddress = selectedCustomer.getAddress();
        this.customerPhoneNumber = selectedCustomer.getPhoneNumber();
        this.customerOccupation = selectedCustomer.getOccupation();
        this.customerFamilyInfo = selectedCustomer.getFamilyInfo();
        this.customerFinancialGoal = selectedCustomer.getFinancialGoal();
        System.out.println(customerName);
        System.out.println(customerGender);
        System.out.println(customerDateOfBirth);
        System.out.println(customerAddress);
        System.out.println(customerPhoneNumber);
        System.out.println(customerOccupation);
        System.out.println(customerFamilyInfo);
        System.out.println(customerFinancialGoal);
        System.out.println(selectedCustomer.getId());
        System.out.println(selectedCustomer.getIc());

        // FacesContext.getCurrentInstance().getExternalContext().redirect("/MerLION-war/GRNSWeb/admin/updateUser.xhtml");
    }

    public void modifyProfile(ActionEvent event) {
        //try {
            if (FacesContext.getCurrentInstance().getResponseComplete()) {
                System.out.println("lala");
                return;
            }
//            ic = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("IC");
//            customerName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerName"); 
//            customerGender = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerGender");
//            customerDateOfBirth = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerDateOfBirth");
//            customerAddress = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerAddress");
//            customerEmail = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEmail");
//            customerPhoneNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerPhoneNumber");
//            customerOccupation = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerOccupation");
//            customerFamilyInfo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFamilyInfo");
//            customerFinancialAsset = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFinancialAsset") ;
//            customerFinancialGoal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFinancialGoal");
            System.out.println("Phone Number is: " + customerPhoneNumber);
            amsbl.updateProfile(ic, customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo, customerFinancialGoal);
        } //catch (UserExistException ex) {
      //      System.out.println("Username already exists");
        //}
  //  }

}
