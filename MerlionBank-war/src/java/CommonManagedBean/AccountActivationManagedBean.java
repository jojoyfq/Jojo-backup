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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

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
    private String customerIc = "S9276";
    private String customerName;
    private String phoneNumber;
    private String initialPassword;
    private String newPassword;
    private String confirmedPassword;

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public AccountActivationManagedBean() {
    }

    public void activateAccount(ActionEvent event) throws UserNotExistException, UserAlreadyActivatedException, IOException {
//        this.customerIc = customerIc;
//        this.customerName = customerName;
//        this.dateOfBirth = dateOfBirth;
        try {
            if (customerIc != null && customerName != null && dateOfBirth != null && phoneNumber != null) {
                String msg = amsbl.activateAccountVerifyDetail(customerIc, customerName, dateOfBirth, phoneNumber);
                System.out.println("GAO MEI REN:" + msg);
                if (msg.equals(customerIc)) {
                    System.out.println("lala");
                    String msg2 = amsbl.verifyAccountBalance(customerIc);

                    if (msg2.equals(customerIc)) {
                        System.out.println("Account activated successfully!");
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerManagement/ResetInitialPassword.xhtml");
                    } else {
                        System.out.println(msg2);
                    }
                } else {
                    System.out.println(msg);
                }
            } else {
                System.out.println("Please dont leave blanks!");
            }
        } catch (UserNotExistException ex) {
            System.out.println("User not existed!" + ex.getMessage());
        } catch (UserAlreadyActivatedException ex1) {
            System.out.println("Problem with activation");
        }
    }

    public void resetInitialPassword(ActionEvent event) throws PasswordTooSimpleException {
        if(customerIc!=null && initialPassword!=null && newPassword!=null && confirmedPassword != null){
        String msg = msg = amsbl.updatePassword(customerIc, initialPassword, newPassword, confirmedPassword);

        System.out.println(msg);

        if (msg.equals(customerIc)) {
            System.out.println("Your password has been successfully changed!");
            boolean msg2 = amsbl.updateAccountStatus(msg);
            if (msg2 == true) {
                System.out.println("Status has been updated!");
            } else {
                System.out.println("Status has NOT been updated!");

            }
        } else {
            System.out.println("password has not been changed!");
        }
        }else{
            System.out.println("Please do not leave blanks!");
        }

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
    public void init() {
        customer = new Customer();

    }

}
