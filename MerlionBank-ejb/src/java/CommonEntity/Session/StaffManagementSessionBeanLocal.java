/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Permission;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import Exception.EmailNotSendException;
import Exception.PasswordNotMatchException;
import Exception.PasswordTooSimpleException;
import Exception.RoleAlreadyExistedException;
import Exception.RoleHasStaffException;
import Exception.StaffAlreadyHasRoleException;
import Exception.StaffRoleExistException;
import Exception.UnexpectedErrorException;
import Exception.UserAlreadyActivatedException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface StaffManagementSessionBeanLocal {

    // create role without assigning permissions
    public StaffRole createRole(Long staffId, String roleName) throws RoleAlreadyExistedException;

// System administrators grant permissions for different roles
    public boolean grantRole(Long staffId, String roleName, boolean systemUserWorkspace, boolean systemUserAccount, boolean operationalCRM,
            boolean collaborativeCRM, boolean fixedDeposit, boolean savingAccount,
            boolean counterCash, boolean debitCard, boolean creditCard, boolean secureLoan, boolean unsecureLoan,
            boolean billModule, boolean transferModule, boolean customerPlan, boolean executedPlan, boolean finalcialInstrument,
            boolean customerPortfolio, boolean staffPerformance, boolean customerProductRecommendation);

// Display system roles
    public List<StaffRole> viewRoles();

//System administrator delete system roles
    public boolean deleteRole(Long staffRoleId, Long staffId) throws RoleHasStaffException;

//Display permissions and associated roles
    public List<Permission> viewPermission();

//Modify role
    public boolean addPermission(Long staffId, Long staffRoleId, Long permissionId);

    public boolean deletePermission(Long staffId, Long staffRoleId, Long permissionId);

// Create staff, at the same time assign staff roles
    public Staff createStaff(Long staffID, String staffIc, String staffName, String staffEmail, String mobileNumber, String status) throws UserExistException, EmailNotSendException;

    public List<StaffRole> displayListOfRole();

    public Long assignStaffRole(Long staffId, Long newStaffId, String staffRoleName) throws StaffRoleExistException;

//Activate account- 1st step verify account details
    public Long activateAccountVerifyDetail(String staffIc, String fullName, String email) throws UserNotExistException, UserAlreadyActivatedException;

    //Activate account - 2nd intial password reset
    public Long updatePassword(Long staffId, String oldPassword, String newPassword, String confirmPassword) throws PasswordTooSimpleException, PasswordNotMatchException, UserNotExistException;

    //Activate account - 3rd step update account status
    public boolean updateAccountStatus(Long staffId);

    //view system users
    public List<Staff> displayListOfStaff();

// delete staff, 1st delete staff, 2nd delete staff from role
    public boolean deleteStaff(Long staffId, Long adminId);

    public boolean deleteStaffFromRole(Staff staff, Long roleId);

// modify staff- update staff personal info
public Long updateStaffInfo(Long adminId, Long staffId, String staffIc, String staffName, String staffEmail, String mobileNumber) ;
//modify staff- modify staff roles
    public boolean staffAddRole(Long staffId, String roleName)throws StaffAlreadyHasRoleException;

   public boolean staffDeleteRole(Long staffId, String roleName);

//forget password/unlock account- 1st step verify account details, 2nd update new password and status
    public Staff forgetPasswordVerifyDetail(String ic, String fullName, String email) throws UserNotExistException, UserNotActivatedException;

    public boolean updateForgetPassword(Staff staff, String newPassword, String confirmPassword) throws PasswordTooSimpleException, PasswordNotMatchException, UnexpectedErrorException;

    //log in
    public Long checkLogin(String ic, String password, String staffRoleName) throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException;

    public Staff viewStaff(Long staffID) throws UserNotExistException;

    // invalid log in - acccount lock

    public Long lockAccount(Long staffId);
    
    public StaffRole getRoleByRoleName(String roleName);
    
}
