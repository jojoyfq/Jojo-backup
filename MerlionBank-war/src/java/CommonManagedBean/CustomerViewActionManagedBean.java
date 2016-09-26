/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.CustomerAction;
import CommonEntity.Session.StaffCustomerViewActionSessionBeanLocal;
//import CustomerRelationshipEntity.StaffAction;
import Exception.ListEmptyException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author a0113893
 */
@Named(value = "customerViewActionManagedBean")
@SessionScoped
public class CustomerViewActionManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerViewActionManagedBean
     */
    public CustomerViewActionManagedBean() {
    }
    @EJB
    StaffCustomerViewActionSessionBeanLocal scvasbl;
    @Inject
    LogInManagedBean limbl;
    private Long customerId;

    public StaffCustomerViewActionSessionBeanLocal getScvasbl() {
        return scvasbl;
    }

    public void setScvasbl(StaffCustomerViewActionSessionBeanLocal scvasbl) {
        this.scvasbl = scvasbl;
    }

    public LogInManagedBean getLimbl() {
        return limbl;
    }

    public void setLimbl(LogInManagedBean limbl) {
        this.limbl = limbl;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<CustomerAction> getActions() {
        return actions;
    }

    public void setActions(List<CustomerAction> actions) {
        this.actions = actions;
    }
    private List<CustomerAction> actions;

    /**
     * Creates a new instance of StaffLoggingActionManagedBean
     */
    

    @PostConstruct
    public void init() {
        customerId = limbl.getCustomerId();
        actions = new ArrayList<CustomerAction>();
       // actions = this.viewLoggingAction;
    }

    public List<CustomerAction> viewLoggingAction(ActionEvent event) throws ListEmptyException {
        try {
            customerId = limbl.getCustomerId();
            System.out.println("***********customer Id is "+customerId);
            actions = scvasbl.viewCustomerAction(customerId);
        } catch (ListEmptyException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Log In Message", ex.getMessage());

            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        return actions;
    }
    
}
