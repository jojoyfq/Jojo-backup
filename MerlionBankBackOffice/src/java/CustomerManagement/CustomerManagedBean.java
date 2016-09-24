/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerManagement;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.UserExistException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Bella
 */
@Named(value = "customerManagedBean")
@SessionScoped
public class CustomerManagedBean implements Serializable{

    @EJB
    AccountManagementSessionBeanLocal amsb;
    @EJB
    SavingAccountSessionBeanLocal sasb;
    
    private String savingAccountName;
    private List<String> savingAccountTypeList;
    //for Teller create saving account for new customer
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
    private String newPassword;

    
    
    @PostConstruct
    public void init() {
        try {
            System.out.print("inside the init() method");
            this.getSavingAccountType();
        } catch (Exception e) {
            System.out.print("Init encounter error");
        }
    }
    
    public CustomerManagedBean() {
    }
    
    public void createSavingAccountNewCustomer(ActionEvent event)throws UserExistException, EmailNotSendException, IOException {
        try{
            amsb.tellerCreateSavingAccount(ic, customerName, customerGender, customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo, savingAccountName, newPassword);
             FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/CustomerManagement/createSavingAccountNCsuccess.xhtml");
        }catch(UserExistException | EmailNotSendException ex){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    public void getSavingAccountType() {
        System.out.print("inside the getSavingAccountType method");
        if (sasb.getSavingAccountType().get(0).equals("false")) {
            System.out.print("get saving account type from database got problem!");
        } else {
            savingAccountTypeList = sasb.getSavingAccountType();
            System.out.print(savingAccountTypeList);
        }
    }
    
    public String getSavingAccountName() {
        return savingAccountName;
    }

    public void setSavingAccountName(String savingAccountName) {
        this.savingAccountName = savingAccountName;
    }

    public List<String> getSavingAccountTypeList() {
        return savingAccountTypeList;
    }

    public void setSavingAccountTypeList(List<String> savingAccountTypeList) {
        this.savingAccountTypeList = savingAccountTypeList;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
}
