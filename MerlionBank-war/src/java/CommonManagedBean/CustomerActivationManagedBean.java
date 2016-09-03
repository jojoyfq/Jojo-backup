/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
//import Logger.MyLogger;
import java.io.IOException;
//import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
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
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author apple
 */
@Named(value = "customerActivationManagedBean")
@SessionScoped
public class CustomerActivationManagedBean implements Serializable {

    @EJB
    AccountManagementSessionBeanLocal amsbl;
    public CustomerActivationManagedBean() {
    
    }
    /**
     * Creates a new instance of CustomerActivationManagedBean
     */
    private String customerIc;
    private String customerName;
    private Date customerDateOfBirth;
    private String customerPhoneNumber;
    private Customer customer;

    private final static Logger LOGGER = Logger.getLogger(CustomerActivationManagedBean.class.getName());
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

    public Date getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(Date customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @PostConstruct
    public void init() {
        customer = new Customer();

    }

    
    
     private void warnMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
        LOGGER.info("MESSAGE INFO: " + message);
    }
      private void faceMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
        LOGGER.info("MESSAGE INFO: " + message);
    }
        private void errorMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
        LOGGER.info("MESSAGE INFO: " + message);
    }
    public void checkVerificationDetails(ActionEvent event) {
        String result = amsbl.activateAccountVerifyDetail(customerIc, customerName, customerDateOfBirth, customerPhoneNumber);
        if (result.equals("invalid account")) {
            this.errorMsg("Please enter the correct information!");
        }else{
        // FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerManagement/companyAdminHome.xhtml");
        }
    }

}
