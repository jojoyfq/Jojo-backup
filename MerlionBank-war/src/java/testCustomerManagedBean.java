/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import CardEntity.DebitCard;
import CardEntity.Session.DebitCardSessionBeanLocal;
import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import CommonEntity.Session.TestCustomerSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.UserExistException;
import PayMeEntity.Session.PayMeSessionBeanLocal;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ruijia
 */
@Named(value = "testCustomerManagedBean")
@SessionScoped
public class testCustomerManagedBean implements Serializable {
    private Customer customer;
    private String ic = "ruijia";
    private String customerName = "Wang Ruijia";
    private String customerGender ="F";
    private Date customerDateOfBirth;
    private String customerAddress = "kent ridge" ;
    private String customerEmail= "ruijiaruijia@mailinator.com";
    private String customerPhoneNumber = "+6584527086";

    private String customerOccupation ="student";
    private String customerFamilyInfo = "single";
    private String savingAccountType = "MerLion Monthly Saving Account";
    private Long customerId;
    private BigDecimal balance = BigDecimal.valueOf(10000);
      
        @EJB
    AccountManagementSessionBeanLocal amsbl;
        @EJB
        TestCustomerSessionBeanLocal testl;
        @EJB
        PayMeSessionBeanLocal pmsbl;
    /**
     * Creates a new instance of testCustomerManagedBean
     */
    public testCustomerManagedBean() {
    }
    
    public void insertTest(ActionEvent event) throws UserExistException, EmailNotSendException, IOException{
          System.out.print("inside managed bean");
          customerDateOfBirth = Calendar.getInstance().getTime();
          customer = amsbl.createSavingAccount(ic, customerName, customerGender, customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo, savingAccountType);
          customerId = customer.getId();
          testl.setStatus(customerId, balance);
          System.out.print("******status set****");
          testl.setPassword(customerId);
          System.out.print("******password set****");
          
          //setPayMe
//          String savingAccountNo = customer.getSavingAccounts().get(0).getAccountNumber().toString();
//          pmsbl.createPayMe(ic, savingAccountNo, customerPhoneNumber, "ruijia123");
          
          FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/LogInHome.xhtml");
         
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
    
    
}
