/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffManagement;

import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import Exception.PasswordNotMatchException;
import Exception.PasswordTooSimpleException;
import Exception.UnexpectedErrorException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author apple
 */
@Named(value = "staffLogInManagedBean")
@SessionScoped
public class staffLogInManagedBean implements Serializable {

    @EJB
    StaffManagementSessionBeanLocal smsbl;

    /**
     * Creates a new instance of staffLogInManagedBean
     */
    public staffLogInManagedBean() {
    }

    private String staffIc;

    public int getLogInAttempts() {
        return logInAttempts;
    }

    public void setLogInAttempts(int logInAttempts) {
        this.logInAttempts = logInAttempts;
    }

    public String getStaffIc() {
        return staffIc;
    }

    public void setStaffIc(String staffIc) {
        this.staffIc = staffIc;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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
    private Long staffId;
    private String name;
    private String email;
    private Staff staff;
    private StaffRole staffRole;
    private String password;
    private String oldPassword;
    private String newPassword;
    private List roleNames;
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List roleNames) {
        this.roleNames = roleNames;
    }

    private int logInAttempts;
    private final int max_attempts = 6;

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
    private String confirmPassword;

    public StaffManagementSessionBeanLocal getSmsbl() {
        return smsbl;
    }

    public void setSmsbl(StaffManagementSessionBeanLocal smsbl) {
        this.smsbl = smsbl;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public StaffRole getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(StaffRole staffRole) {
        this.staffRole = staffRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PostConstruct
    public void init() {
        staff = new Staff();
        roleNames = new ArrayList<>();

    }

    public void staffLogIn(ActionEvent event) throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException {
        try {
            if (staffIc != null && password != null && staffRole != null) {
                staffId = smsbl.checkLogin(staffIc, password, roleName);
                staff = smsbl.viewStaff(staffId);

                for (int i = 0; i < staff.getStaffRoles().size(); i++) {
                    roleNames.add(staff.getStaffRoles().get(i).getRoleName());
                }
                if (staffId.toString().equals("1")) {
                    System.out.println("Password does not match");
                    logInAttempts++;
                    System.out.println("number attempts:" + logInAttempts);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your password does not correct! Please try again!");

                    RequestContext.getCurrentInstance().showMessageInDialog(message);
                    if (logInAttempts >= max_attempts) {
                        System.out.println("Your account has been locked out.");
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your account has been locked out.");

                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                        System.out.println(smsbl.lockAccount(staffId));
                    }
                } else {
                    //  selectedCustomer = amsbl.diaplayCustomerId(customerId);
                    logInAttempts = 0;
//                    System.out.println("Log In Successful!");
//                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ic", ic);
//                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("name", selectedCustomer.getName());

                    //  FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/staffInputMessage.xhtml");
                }

                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Logged in successfully!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            } else {
                System.out.println("Please do not leave blanks!");
            }
        } catch (UserNotExistException | PasswordNotMatchException | UserNotActivatedException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void updateForgetPasswordVerifyDetail(ActionEvent event) throws UserNotExistException, UserNotActivatedException, IOException {
        try {
            if (staffIc != null && name != null && email != null) {
                staff = smsbl.forgetPasswordVerifyDetail(staffIc, name, email);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffSelfManagement/resetPassword.xhtml");

            }
        } catch (UserNotExistException | UserNotActivatedException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void staffUpdatePassword(ActionEvent event) throws PasswordTooSimpleException, PasswordNotMatchException, UnexpectedErrorException {
        try {
            if (staffId != null && oldPassword != null && newPassword != null && confirmPassword != null) {
                smsbl.updateForgetPassword(staff, newPassword, confirmPassword);
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Passwrod was updated successfully!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

            }
        } catch (PasswordTooSimpleException | PasswordNotMatchException | UnexpectedErrorException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

        }
    }

}
