/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffManagement;

import CommonEntity.CustomerAction;
import CommonEntity.Session.StaffCustomerViewActionSessionBeanLocal;
import CustomerRelationshipEntity.StaffAction;
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
@Named(value = "staffLoggingActionManagedBean")
@SessionScoped
public class StaffLoggingActionManagedBean implements Serializable {

    @EJB
    StaffCustomerViewActionSessionBeanLocal scvasbl;
    @Inject
    staffLogInManagedBean limbl;
    private Long staffId;
    private List<StaffAction> actions;

    /**
     * Creates a new instance of StaffLoggingActionManagedBean
     */
    public StaffLoggingActionManagedBean() {
    }

    @PostConstruct
    public void init() {
        staffId = limbl.getStaffId();
        actions = new ArrayList<>();
    }
    
    public List<StaffAction> viewLoggingAction(ActionEvent event) throws ListEmptyException {
        try {
            staffId = limbl.getStaffId();
            System.out.println("***********customer Id is "+staffId);
            actions = scvasbl.viewStaffAction(staffId);
        } catch (ListEmptyException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Log In Message", ex.getMessage());

            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        return actions;
    }
    
    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public List<StaffAction> getActions() {
        return actions;
    }

    public void setActions(List<StaffAction> actions) {
        this.actions = actions;
    }
    
}
