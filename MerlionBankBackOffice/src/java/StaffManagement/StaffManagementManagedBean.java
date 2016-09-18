package StaffManagement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import CommonEntity.Permission;
import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import java.awt.event.ActionEvent;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author apple
 */
@Named(value = "staffManagementManagedBean")
@Dependent
public class StaffManagementManagedBean {
  @EJB
  StaffManagementSessionBeanLocal smsbl;
    /**
     * Creates a new instance of StaffManagementManagedBean
     */
    public StaffManagementManagedBean() {
    }
    private String roleName;
    private Long staffId;
    private Staff staff;
    private StaffRole staffRole;
    private Permission permission; 
    
    @PostConstruct
    public void init(){
        System.err.println("********************Message from managed bean: login staff id is "+staffId);
        
        staff = new Staff();
        staffRole = new StaffRole();
        permission = new Permission();
        
    }
    public void adminCreateStaffRole(ActionEvent event){
        
    }
}
