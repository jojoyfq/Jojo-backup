/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffManagement;

import CommonEntity.Permission;
import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import Exception.RoleHasStaffException;
import PermissionManagedBean.PermissionDataTableRow;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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
@Named(value = "roleManagementManagedBean")
@SessionScoped
public class RoleManagementManagedBean implements Serializable {

    @EJB
    StaffManagementSessionBeanLocal smsbl;
@Inject
staffLogInManagedBean slimbl;
    /**
     * Creates a new instance of RoleManagementManagedBean
     */
    public RoleManagementManagedBean() {
    }
    private Long adminId = 4L;
    private Long staffRoleId;
    private Long permissionId;
    private List<StaffRole> staffRoles;
    private StaffRole role;
    private boolean newValidility;
    private boolean oldValidility;
    private String moduleName;
    private Permission selectedPermission;
    private Long roleId;

    private String newValidilityStr;


    private List<PermissionDataTableRow> permissionDataTableRows;
    
    
    
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Permission getSelectedPermission() {
        return selectedPermission;
    }

    public void setSelectedPermission(Permission selectedPermission) {
        this.selectedPermission = selectedPermission;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public boolean isOldValidility() {
        return oldValidility;
    }

    public void setOldValidility(boolean oldValidility) {
        this.oldValidility = oldValidility;
    }

    public boolean isNewValidility() {
        return newValidility;
    }

    public void setNewValidility(boolean newValidility) {
        this.newValidility = newValidility;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getStaffRoleId() {
        return staffRoleId;
    }

    public void setStaffRoleId(Long staffRoleId) {
        this.staffRoleId = staffRoleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public List<StaffRole> getStaffRoles() {
        return staffRoles;
    }

    public void setStaffRoles(List<StaffRole> staffRoles) {
        this.staffRoles = staffRoles;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
    private Staff staff;
    private List<Permission> permissions;

    @PostConstruct
    public void init() {
        staffRoles = new ArrayList<>();
        staff = new Staff();
        permissions = new ArrayList<>();
        staffRoles = this.displayAllRoles();
        role = new StaffRole();
        selectedPermission = new Permission();
    }

    public List<StaffRole> displayAllRoles() {

        staffRoles = smsbl.viewRoles();
        return staffRoles;
    }
    
    public void goToViewAllRolePage(ActionEvent event){
        try{
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/SuperAdminManagement/viewAllRoles.xhtml");
        }catch(Exception e){
           System.out.print("Redirect to View One Role Page Error!");
        }
    }
    
    public void deleteRole(ActionEvent event) throws RoleHasStaffException, IOException {
        try {
             role = (StaffRole) event.getComponent().getAttributes().get("selectedRole");
             staffRoleId = role.getId();
 
             staffRoles = smsbl.deleteRole(staffRoleId, adminId);
             
             List <String>temp=new ArrayList<String>();
             System.out.println("In session bean delete role!!!!!!"+staffRoles.size());
             for(int i=0;i<staffRoles.size();i++){
             temp.add(staffRoles.get(i).getRoleName());
             }
             slimbl.setRoleNames(temp);
             //redirect to Success Page
              FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/SuccessPage.xhtml");
         } catch (RoleHasStaffException ex) {
             FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
             RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
 
         }
     }

    public void displayOneRolePermissions(ActionEvent event) {

        role = (StaffRole) event.getComponent().getAttributes().get("selectedRole");
        System.out.println("********Selected Role is " + role.getRoleName() + " " + role.getId());
        permissions = role.getPermissions();

        

        System.out.println("**********size of permission is " + permissions.size());

        for (int i = 0; i < permissions.size(); i++) {
            System.out.println("" + permissions.get(i).getModuleName());
        }
//                return permissions;
    }
    public void editPermission(StaffRole role) throws IOException {
        // permissionId = ((Permission) event.getComponent().getAttributes().get("selectedPermission")).getId();

        permissions = role.getPermissions();
        
        setPermissionDataTableRows(new ArrayList<>());
        
        for(Permission p:permissions)
            getPermissionDataTableRows().add(new PermissionDataTableRow(p));
        roleId = role.getId();
        System.out.println("**********size of permission is " + permissions.size());

        for (int i = 0; i < permissions.size(); i++) {
            System.out.println("" + permissions.get(i).getModuleName());
        }
        System.out.println("***** permission size " + permissions.size());
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/SuperAdminManagement/viewOneStaffRolePermissions.xhtml");

    }

    public void editPermissionNext(ActionEvent event)
    {    
        PermissionDataTableRow permissionDataTableRow = ((PermissionDataTableRow) event.getComponent().getAttributes().get("selectedPermission"));
        
        permissionId = permissionDataTableRow.getPermission().getId();
        moduleName = permissionDataTableRow.getPermission().getModuleName();
        oldValidility = permissionDataTableRow.getPermission().isValidity();
        newValidility = permissionDataTableRow.getValidity();

        if (oldValidility != newValidility) 
        {
            if (newValidility == false) 
            {
                boolean msg = smsbl.deletePermission(adminId, roleId, permissionId);
            } 
            else 
            {
                smsbl.addPermission(adminId, roleId, permissionId);
            }
            
            staffRoles = displayAllRoles();
            
            for(StaffRole sr:staffRoles)
            {
                if(sr.getId().equals(roleId))
                {
                    role = sr;
                    break;
                }
            }
                        
            permissions = role.getPermissions();            
            setPermissionDataTableRows(new ArrayList<>());
        
            for(Permission p:permissions)
            {
                getPermissionDataTableRows().add(new PermissionDataTableRow(p));
            }
        }
    }

    public List<PermissionDataTableRow> getPermissionDataTableRows() {
        return permissionDataTableRows;
    }

    public void setPermissionDataTableRows(List<PermissionDataTableRow> permissionDataTableRows) {
        this.permissionDataTableRows = permissionDataTableRows;
    }


}
