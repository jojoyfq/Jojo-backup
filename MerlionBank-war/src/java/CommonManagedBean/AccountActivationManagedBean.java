/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import Exception.PasswordTooSimpleException;
import Exception.UserAlreadyActivatedException;
import Exception.UserNotExistException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author apple
 */
@Named(value = "accountActivationManagedBean")
@SessionScoped
public class AccountActivationManagedBean implements Serializable {

    @EJB
    AccountManagementSessionBeanLocal amsbl;

    private Customer customer;
    private Date dateOfBirth;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    private String customerIc;
    private String customerName;
    private String phoneNumber;
    private String initialPassword;
    private String newPassword;
    private String confirmedPassword;

    public void init() {
        customer = new Customer();

    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public AccountActivationManagedBean() {
    }

    public void goToLogInPage(ActionEvent event) throws IOException {
        System.out.println("Go into GoToLogInHome");
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/LogInHome.xhtml");

    }

    public void activateAccount(ActionEvent event) throws UserNotExistException, UserAlreadyActivatedException, IOException {
//        this.customerIc = customerIc;
//        this.customerName = customerName;
//        this.dateOfBirth = dateOfBirth;
        try {
            if (customerIc != null && customerName != null && dateOfBirth != null && phoneNumber != null) {
                customer = amsbl.activateAccountVerifyDetail(customerIc, customerName, dateOfBirth, phoneNumber);
                // System.out.println("GAO MEI REN:" + msg);

                System.out.println("lala");

                String msg2 = amsbl.verifyAccountBalance(customerIc);
                System.out.print("verifyAccountBalance status is" + msg2);

                if (msg2.equals("invalid amount")) {
                    System.out.print("inside the if statement!.......");
                    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have not reached the minimum top up amount!!");
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
                } else {
                    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Account Activated Successfully!");
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerManagement/ResetInitialPassword.xhtml");

                }

            } else {
                System.out.println("Please dont leave blanks!");
            }
        } catch (UserNotExistException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        } catch (UserAlreadyActivatedException ex1) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex1.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public String resetInitialPassword() throws PasswordTooSimpleException {
        try {
            if (customerIc != null && initialPassword != null && newPassword != null && confirmedPassword != null) {
                String msg = msg = amsbl.updatePassword(customerIc, initialPassword, newPassword, confirmedPassword);

                System.out.println(msg);

                if (msg.equals(customerIc)) {
//                    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your password has been successfully changed!");
//                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "System message", "Your password has been successfully changed!"));
                    Flash flash = facesContext.getExternalContext().getFlash();
                    flash.setKeepMessages(true);
                    flash.setRedirect(true);
                    boolean msg2 = amsbl.updateAccountStatus(msg);
                    if (msg2 == true) {
                        System.out.println("Status has been updated!");
                    } else {
                        System.out.println("Status has NOT been updated!");

                    }
                } else if (msg.equals("Does not match with new password")) {
                    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your password does not match with new password!");
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

                } else {
                    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your password has not been  changed!");
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

                    System.out.println("password has not been changed!");
                }
            } else {
                System.out.println("Please do not leave blanks!");
            }
        } catch (PasswordTooSimpleException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
        return "LogInHome";

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Creates a new instance of AccountActivationManagedBean
     */
}
