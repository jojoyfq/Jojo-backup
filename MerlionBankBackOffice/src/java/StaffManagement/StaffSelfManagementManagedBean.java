/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffManagement;

import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.Staff;
import Exception.PasswordNotMatchException;
import Exception.PasswordTooSimpleException;
import Exception.UnexpectedErrorException;
import Exception.UserAlreadyActivatedException;
import Exception.UserNotExistException;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author apple
 */
@Named(value = "staffSelfManagementManagedBean")
@SessionScoped
public class StaffSelfManagementManagedBean implements Serializable {

    /**
     * Creates a new instance of StaffSelfManagement
     */
    @EJB
    StaffManagementSessionBeanLocal smsbl;

    private String staffIc;
    private String name;
    private String email;
    private Staff staff;
    private Long staffId;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public String getStaffIc() {
        return staffIc;
    }

    public void setStaffIc(String staffIc) {
        this.staffIc = staffIc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StaffSelfManagementManagedBean() {
    }

    @PostConstruct
    public void init() {
        staff = new Staff();

    }

    public void acctActivationVerifyDetails(ActionEvent event) throws UserNotExistException, UserAlreadyActivatedException, IOException {
        try {
            if (staffIc != null && name != null && email != null) {
                staffId = smsbl.activateAccountVerifyDetail(staffIc, name, email);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffSelfManagement/staffChangePassword.xhtml");

            } else {
                System.out.println("Please do not leave blanks");
            }
        } catch (UserNotExistException | UserAlreadyActivatedException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }

    }

    public void updatePassword(ActionEvent event) throws PasswordTooSimpleException, PasswordNotMatchException, UserNotExistException, IOException {
        try {

            System.out.println("***********Message from managed bean staff Id is " + staffId);
            if (staffId != null && oldPassword != null && newPassword != null && confirmPassword != null) {
                smsbl.updatePassword(staffId, oldPassword, newPassword, confirmPassword);
                smsbl.updateAccountStatus(staffId);
//                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your password has been updated successfully!");
//                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
//                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffSelfManagement/staffChangePassword.xhtml");

//                FacesContext facesContext = FacesContext.getCurrentInstance();
//                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "System message", "Your Account has been activated!"));
//                Flash flash = facesContext.getExternalContext().getFlash();
//                flash.setKeepMessages(true);
//                flash.setRedirect(true);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/staffSuccessPageWOLogIn.xhtml");

            } else {
                System.out.println("Please do not leave blanks!");
            }

        } catch (PasswordTooSimpleException | PasswordNotMatchException | UserNotExistException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
        //   return "staffLogInHome";
    }

}
