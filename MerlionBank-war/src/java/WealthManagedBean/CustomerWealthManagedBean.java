/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthManagedBean;

import CommonEntity.Customer;
import CommonManagedBean.LogInManagedBean;
import Exception.EmailNotSendException;
import Exception.UserExistException;
import WealthEntity.Session.WealthApplicationSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
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
@Named(value = "customerWealthManagedBean")
@SessionScoped
public class CustomerWealthManagedBean implements Serializable {
    
    private Customer customer;
    private String customerIc;
    private String customerName;
    private String customerGender;
    private Date customerDateOfBirth;
    private String customerAddress;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerOccupation;
    private String customerFamilyInfo;
    private String password;
    
    
    
      @EJB
    private WealthApplicationSessionBeanLocal wasbl;
    @Inject
    private LogInManagedBean logInManagedBean;
    /**
     * Creates a new instance of CustomerWealthManagedBean
     */
    public CustomerWealthManagedBean() {
    }
     public void customerCreateLoanAcct(ActionEvent event) throws UserExistException, EmailNotSendException, IOException {
        try {
            customer = wasbl.createDiscretionaryAccount(customerIc, customerName, customerGender, customerDateOfBirth, password, customerEmail, customerName, customerOccupation, customerFamilyInfo);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Discretionary Account has been successfully created! Detailed informaiton has been sent to your email!");
           RequestContext.getCurrentInstance().showMessageInDialog(message);
         //   FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/LoanManagement/displayLoanTypes.xhtml");

        } catch (UserExistException | EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
}
