/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PermissionManagedBean;

import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.StaffRole;
import StaffManagement.staffLogInManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author liyanmeng
 */
@Named(value = "rolePermissionManagedBean")
@SessionScoped
public class RolePermissionManagedBean implements Serializable {

    @Inject
    staffLogInManagedBean staffLogInManagedBean1;
    @EJB
    StaffManagementSessionBeanLocal smsbl;

    private String roleName;
    private StaffRole role;
    private List<Boolean> permissionList;

    /**
     * Creates a new instance of RolePermissionManagedBean
     */
    public RolePermissionManagedBean() {

    }

    @PostConstruct
    public void init() {
        role = new StaffRole();
        permissionList = new ArrayList<>();
        permissionList = this.getPermissions();
    }

    public List<Boolean> getPermissions() {

        roleName = staffLogInManagedBean1.getRoleName();
        System.out.println("Role name is: " + roleName);
        role = smsbl.getRoleByRoleName(roleName);
        System.out.println("permission size: " + role.getPermissions().size());
        for (int i = 0; i < role.getPermissions().size(); i++) {
            permissionList.add(role.getPermissions().get(i).isValidity());
            System.out.println("boolean value " + i + " is " + permissionList.get(i));
        }
        return permissionList;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Boolean> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Boolean> permissionList) {
        this.permissionList = permissionList;
    }

}
