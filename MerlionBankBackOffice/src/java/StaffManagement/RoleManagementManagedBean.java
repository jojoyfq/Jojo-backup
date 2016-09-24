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
import PermissionManagedBean.PermissionDataTableRow;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author apple
 */
@Named(value = "roleManagementManagedBean")
@SessionScoped
public class RoleManagementManagedBean implements Serializable {

    @EJB
    StaffManagementSessionBeanLocal smsbl;

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

    public void editPermissionNext(ActionEvent event) {
        // permission = (Permission) event.getComponent().getAttributes().get("selectedPermission");

//        permissionId = ((Permission) event.getComponent().getAttributes().get("selectedPermission")).getId();
//        moduleName = ((Permission) event.getComponent().getAttributes().get("selectedPermission")).getModuleName();
//        oldValidility = ((Permission) event.getComponent().getAttributes().get("selectedPermission")).isValidity();
//        newValidilityStr = Boolean.toString(newValidility);

        
        PermissionDataTableRow permissionDataTableRow = ((PermissionDataTableRow) event.getComponent().getAttributes().get("selectedPermission"));
        
        permissionId = permissionDataTableRow.getPermission().getId();
        moduleName = permissionDataTableRow.getPermission().getModuleName();
        oldValidility = permissionDataTableRow.getPermission().isValidity();
        newValidility = permissionDataTableRow.getValidity();


//        permissionId = ((Permission) event.getObject()).getId();
//        moduleName = ((Permission) event.getObject()).getModuleName();
//       oldValidility= ((Permission) event.getObject()).isValidity();
        System.out.println("******Old validility: " + oldValidility);

        System.out.println("******New validility: " + newValidilityStr);

        System.out.println("******New validility: " + newValidility);

        System.out.println("***************Role Id is " + roleId);
        System.out.println("***************Permission Id is " + permissionId);

        if (oldValidility != newValidility) {
            if (oldValidility = true) {
                boolean msg = smsbl.deletePermission(adminId, roleId, permissionId);
                System.out.println("***********message from delete permission" + msg);
            } else {
                smsbl.addPermission(adminId, roleId, permissionId);
            }

            if (newValidility == false) {
                boolean msg = smsbl.deletePermission(adminId, roleId, permissionId);
                System.out.println("***********message from delete permission" + msg);
            } else {
                smsbl.addPermission(adminId, roleId, permissionId);
            }
            
            role = smsbl.viewRole(roleId);
            permissions = role.getPermissions();
            
            setPermissionDataTableRows(new ArrayList<>());
        
            for(Permission p:permissions)
                getPermissionDataTableRows().add(new PermissionDataTableRow(p));
            

        } else {
            System.out.println("No permission has been changed!");
        }
    }

    public List<PermissionDataTableRow> getPermissionDataTableRows() {
        return permissionDataTableRows;
    }

    public void setPermissionDataTableRows(List<PermissionDataTableRow> permissionDataTableRows) {
        this.permissionDataTableRows = permissionDataTableRows;
    }


}