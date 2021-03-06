/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import Exception.PasswordTooSimpleException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import com.twilio.sdk.TwilioRestException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author apple
 */
@Named(value = "resetPasswordManagedBean")
@SessionScoped
public class ResetPasswordManagedBean implements Serializable {

    @Inject
    private LogInManagedBean logInManagedBean;

    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
        this.logInManagedBean = logInManagedBean;
    }
    /**
     * Creates a new instance of ResetPasswordManagedBean
     */
    private String customerIc ;
    private String customerName;
    private Date dateOfBirth;
    private String customerEmail;
    private Customer customer;
    private String inputCode;
    private String password;
    private String confirmPassword;

    @EJB
    AccountManagementSessionBeanLocal amsbl;

    public ResetPasswordManagedBean() {
    }

    @PostConstruct
    public void init() {
        customer = new Customer();
        customerIc  = logInManagedBean.getIc();
    }

    public void verifyCustomerDetails(ActionEvent event) throws UserNotExistException, UserNotActivatedException, IOException, TwilioRestException {

        //customerIc = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerIc");
       try{ System.out.println(customerIc);
        if (customerIc != null && customerName != null && dateOfBirth != null && customerEmail != null) {
            String msg = amsbl.forgetPasswordVerifyDetail(customerIc, customerName, dateOfBirth, customerEmail);
            System.out.println(msg);
            if (msg.equals(customerIc)) {

                try {
                    String msg2 = amsbl.sendTwoFactorAuthentication(customerIc);
                    System.out.println("Message displayed after 2fa  was sent: " + msg2);
                    if (msg2.equals(customerIc)) {

                        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerManagement/SubmitTwoFA.xhtml");

                    }
                } catch (TwilioRestException ex) {
//                    Logger.getLogger(ResetPasswordManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else {
            System.out.println("Don't leave blank please!");

        }
       }catch( UserNotExistException|UserNotActivatedException ex){
         FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
       }
    }

    public void submitTwoFA(ActionEvent event) throws IOException {
        String msg3 = amsbl.verifyTwoFactorAuthentication(customerIc, inputCode);
        this.customerIc = msg3;
        System.out.println("After customer enter 2fa code, the message is " + msg3);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerManagement/ResetPassword.xhtml");

    }

    public void resetForgetPassword(ActionEvent event) throws PasswordTooSimpleException,IOException {
        try {
           // customerIc = logInManagedBean.getIc();
            System.out.println("************customer Ic is "+customerIc);
            String errorMsg = amsbl.updateForgetPassword(customerIc, password, confirmPassword);
            System.out.println("after entring the confirmed password: " + errorMsg);
            amsbl.updateAccountStatus(customerIc);
            if (!errorMsg.equals(customerIc)) {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", errorMsg);
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            } else if (errorMsg.equals(customerIc)) {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully changed your password!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/customerSuccessPageWOLogIn.xhtml");

            }
        } catch (PasswordTooSimpleException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

        }
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public void goToResetPasswordVerifyCustomerDetailsPage(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CustomerManagement/ResetPasswordVerifyCustomerDetails.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to ResetPasswordVerifyCustomerDetails page fails");
        }
    }
}
