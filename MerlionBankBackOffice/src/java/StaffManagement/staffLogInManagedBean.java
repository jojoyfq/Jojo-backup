/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffManagement;

import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import Exception.ListEmptyException;
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
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
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

    @Inject
    StaffLoggingActionManagedBean staffLoggingActionManagedBean;
    
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

        System.out.println("***********Role size is " + smsbl.viewRoles().size());
        for (int i = 0; i < smsbl.viewRoles().size(); i++) {
            roleNames.add(smsbl.viewRoles().get(i).getRoleName());
        }

    }

    public void goToLogInPage(ActionEvent event) throws IOException {
        System.out.println("Go into GoToStaffLogInHome");
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/staffLogInHome.xhtml");

    }
    
    public void viewStaffAction(ActionEvent event) throws IOException, ListEmptyException{
         staffLoggingActionManagedBean.viewLoggingAction(event);
         FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffSelfManagement/viewActionLog.xhtml");
    }

//    public void staffLogIn(ActionEvent event) throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException, IOException {
//        try {
//            if (staffIc != null && password != null && roleName != null) {
////                staffId = smsbl.checkLogin(staffIc, password, roleName);
//                // staff = smsbl.viewStaff(staffId);
//                name = staff.getStaffName();
//                System.out.println("*****************Staff id is " + staffId);
////                for (int i = 0; i < staff.getStaffRoles().size(); i++) {
////                    roleNames.add(staff.getStaffRoles().get(i).getRoleName());
////                }
//                if (staffId.toString().equals("1")) {
//                    System.out.println("Password does not match");
//                    logInAttempts++;
//                    System.out.println("number attempts:" + logInAttempts);
//                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your password does not correct! Please try again!");
//
//                    RequestContext.getCurrentInstance().showMessageInDialog(message);
//                    if (logInAttempts >= max_attempts) {
//                        System.out.println("Your account has been locked out.");
//                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your account has been locked out.");
//
//                        RequestContext.getCurrentInstance().showMessageInDialog(message);
//                        System.out.println(smsbl.lockAccount(staffIc));
//                    }
//                } else {
//                    //  selectedCustomer = amsbl.diaplayCustomerId(customerId);
//                    logInAttempts = 0;
//                    System.out.println("*********Log In Attempts");
////                    System.out.println("Log In Successful!");
////                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ic", ic);
////                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("name", selectedCustomer.getName());
//
//                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffDashboard.xhtml");
//                //    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Logged in successfully!");
//
//                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
//
//             //       RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
//                }
//
//            } else {
//                System.out.println("Please do not leave blanks!");
//            }
//        } catch (UserNotExistException | PasswordNotMatchException | UserNotActivatedException ex) {
//            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
//            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
//        }
//    }

    public void updateForgetPasswordVerifyDetail(ActionEvent event) throws UserNotExistException, UserNotActivatedException, IOException {
        try {
            if (staffIc != null && name != null && email != null) {
                staff = smsbl.forgetPasswordVerifyDetail(staffIc, name, email);
                staffId = staff.getId();
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffSelfManagement/resetPassword.xhtml");

            }
        } catch (UserNotExistException | UserNotActivatedException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void staffUpdatePassword(ActionEvent event) throws PasswordTooSimpleException, PasswordNotMatchException, UnexpectedErrorException, IOException {
        try {
            if (staffId != null && newPassword != null && confirmPassword != null) {
                System.out.println("*F************Reset Password staff id is  "+staffId);
                smsbl.updateForgetPassword(staffId, newPassword, confirmPassword);
//               FacesContext facesContext = FacesContext.getCurrentInstance();
//            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "System message", "Your password has been successfully changed! Please log in again!"));
//                Flash flash = facesContext.getExternalContext().getFlash();
//                flash.setKeepMessages(true);
//                flash.setRedirect(true);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/staffLogInHome.xhtml");

            }
        } catch (PasswordTooSimpleException | PasswordNotMatchException | UnexpectedErrorException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

        }
//        return "staffLogInHome";
    }

    public void logout() throws IOException {
        System.out.println("Inside logout");
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
//        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();        
        System.out.println("testtest");
        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "8080";
        FacesContext.getCurrentInstance().getExternalContext().redirect("http://" + serverName + ":" + serverPort + "/MerlionBankBackOffice/staffLogInHome.xhtml");
    }

    public void goToActivation(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/StaffSelfManagement/staffAccountActivationVerifyDetails.xhtml");
        } catch (Exception e) {
            System.out.print("go to activation page fails");
        }
    }

    public void goToResetPasswordVerifyDetails(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/StaffSelfManagement/resetPasswordVerifyDetails.xhtml");
        } catch (Exception e) {
            System.out.print("go to ResetPasswordVerifyDetails page fails");
        }
    }

}
