package StaffManagement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import CommonEntity.MessageEntity;
import CommonEntity.Permission;
import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import Exception.EmailNotSendException;
import Exception.RoleAlreadyExistedException;
import Exception.StaffAlreadyHasRoleException;
import Exception.StaffRoleExistException;
import Exception.UnexpectedErrorException;
import Exception.UserExistException;
import Exception.UserNotExistException;
import java.io.IOException;
import java.io.Serializable;
//import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author apple
 */
@Named(value = "staffManagementManagedBean")
@SessionScoped
public class StaffManagementManagedBean implements Serializable {

    @EJB
    StaffManagementSessionBeanLocal smsbl;
    @Inject
    staffLogInManagedBean slimb;
    /**
     * Creates a new instance of StaffManagementManagedBean
     */
    public StaffManagementManagedBean() {
    }
    private String roleName;
    private Long staffId = 4L;
    private Long staffRoleId;
    private Staff staff;
    private List<Staff> staffs;

    public List<Staff> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<Staff> staffs) {
        this.staffs = staffs;
    }
    private List<StaffRole> staffRoles;

    public staffLogInManagedBean getSlimb() {
        return slimb;
    }

    public void setSlimb(staffLogInManagedBean slimb) {
        this.slimb = slimb;
    }

    public List<StaffRole> getStaffRoles() {
        return staffRoles;
    }

    public void setStaffRoles(List<StaffRole> staffRoles) {
        this.staffRoles = staffRoles;
    }
    private Permission permission;
    private Long bankStaffId;

    public Long getBankStaffId() {
        return bankStaffId;
    }

    public void setBankStaffId(Long bankStaffId) {
        this.bankStaffId = bankStaffId;
    }
    private boolean systemUserWorkspace = false;
    private boolean systemUserAccount = false;
    private boolean operationalCRM = false;
    private boolean collaborativeCRM = false;
    private boolean fixedDeposit = false;
    private boolean savingAccount = false;
    private boolean counterCash = false;
    private boolean debitCard = false;
    private boolean creditCard = false;
    private boolean secureLoan = false;
    private boolean unsecureLoan = false;
    private boolean billModule = false;
    private boolean transferModule = false;
    private boolean customerPlan = false;
    private boolean executedPlan = false;
    private boolean finalcialInstrument = false;
    private boolean customerPortfolio = false;
    private boolean staffPerformance = false;
    private boolean customerProductRecommendation = false;
    private List<Permission> availablePermissions;
    private List<Permission> selectedPermissions;
    private List availablePermissionNames;
    private List selectedPermissionNames;
    private DualListModel<String> permissionNames;
    private String staffIc;
    private String staffName;
    private String staffEmail;
    private String mobileNumber;
    private String status;
    private List allRoles;
    private String selectedRoleName;
    private String selectedRoleNameForPermission;
    private List roleNamesStaffHas;
    private List oneStaffRoleNames;
    private Staff selectedStaff;
    private String roleNameToDelete;
    private List<StaffRole> roles;
    private Long editedStaffId;
    private Long roleId;
    private String selectedRoleNameToAdd;

    public String getSelectedRoleNameToAdd() {
        return selectedRoleNameToAdd;
    }

    public void setSelectedRoleNameToAdd(String selectedRoleNameToAdd) {
        this.selectedRoleNameToAdd = selectedRoleNameToAdd;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getEditedStaffId() {
        return editedStaffId;
    }

    public void setEditedStaffId(Long editedStaffId) {
        this.editedStaffId = editedStaffId;
    }

    public List<StaffRole> getRoles() {
        return roles;
    }

    public void setRoles(List<StaffRole> roles) {
        this.roles = roles;
    }

    public String getRoleNameToDelete() {
        return roleNameToDelete;
    }

    public void setRoleNameToDelete(String roleNameToDelete) {
        this.roleNameToDelete = roleNameToDelete;
    }

    public Staff getSelectedStaff() {
        return selectedStaff;
    }

    public void setSelectedStaff(Staff selectedStaff) {
        this.selectedStaff = selectedStaff;
    }

    public List getOneStaffRoleNames() {
        return oneStaffRoleNames;
    }

    public void setOneStaffRoleNames(List oneStaffRoleNames) {
        this.oneStaffRoleNames = oneStaffRoleNames;
    }

    public List getRoleNamesStaffHas() {
        return roleNamesStaffHas;
    }

    public void setRoleNamesStaffHas(List roleNamesStaffHas) {
        this.roleNamesStaffHas = roleNamesStaffHas;
    }

    public String getSelectedRoleNameForPermission() {
        return selectedRoleNameForPermission;
    }

    public void setSelectedRoleNameForPermission(String selectedRoleNameForPermission) {
        this.selectedRoleNameForPermission = selectedRoleNameForPermission;
    }

    public String getSelectedRoleName() {
        return selectedRoleName;
    }

    public void setSelectedRoleName(String selectedRoleName) {
        this.selectedRoleName = selectedRoleName;
    }

    public List getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(List allRoles) {
        this.allRoles = allRoles;
    }

    @PostConstruct

    public void init() {
        System.err.println("********************Message from managed bean: login staff id is " + staffId);

        staff = new Staff();
        staffs = new ArrayList<>();
        staffs = this.adminDisplayStaffs();
        staffRoles = new ArrayList<StaffRole>();
        permission = new Permission();
        availablePermissions = new ArrayList<>();
        //      availablePermissions = smsbl.viewRoles();
        availablePermissions = new ArrayList<>();
        availablePermissionNames = new ArrayList<>();
        selectedPermissionNames = new ArrayList<>();
        availablePermissionNames = this.displayPermissions();
        selectedStaff = new Staff();
        permissionNames = new DualListModel<String>(availablePermissionNames, selectedPermissionNames);
        allRoles = new ArrayList<>();

        allRoles = this.adminDisplayRole();
        roleNamesStaffHas = new ArrayList<>();
        oneStaffRoleNames = new ArrayList<>();
        roles = new ArrayList<>();

    }

    public List adminDisplayRole() {
        for (int i = 0; i < smsbl.viewRoles().size(); i++) {
            allRoles.add(smsbl.viewRoles().get(i).getRoleName());

        }
        return allRoles;

    }

    public List adminDisplayStaffs() {
        staffs = smsbl.displayListOfStaff();

        return staffs;
    }

//    public List rolesOneStaffHas() {
//        for (int i = 0; i < staffs.size(); i++) {
//            oneStaffRoleNames.add(staffs.get(i).getStaffName());
//        }
//        return oneStaffRoleNames;
//    }
    public void adminCreateStaffRole(ActionEvent event) throws RoleAlreadyExistedException, IOException {
        try {
            if (roleName != null && staffId != null) {
                staffRoles = smsbl.createRole(staffId, roleName);
              //  allRoles.add(staffRoles.getRoleName());
                List temp = new ArrayList<>();
                for(int i=0;i<staffRoles.size();i++){
               temp.add(staffRoles.get(i).getRoleName());
                
                }
                slimb.setRoleNames(temp);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/SuperAdminManagement/adminGrantRole.xhtml");

            } else {
                System.out.println("Please do not leave blanks!");
            }
        } catch (RoleAlreadyExistedException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

        }
    }

    public void adminGrantPermissionToRole(ActionEvent event) {
//        if (staffId != null && staffRoleId != null && systemUserWorkspace && systemUserAccount != null && operationalCRM != null && collaborativeCRM != null && fixedDeposit != null && savingAccount != null && counterCash != null && debitCard
//                != null && creditCard != null && secureLoan != null && unsecureLoan != null && billModule != null && transferModule != null && customerPlan != null && executedPlan != null && finalcialInstrument != null && customerPortfolio != null && staffPerformance != null && customerProductRecommendation != null) {
        this.updateRolesToTrue();
        System.out.println("******************hi lalallalalal!");
        if (staffId != null && selectedRoleNameForPermission != null) {
            smsbl.grantRole(staffId, selectedRoleNameForPermission, systemUserWorkspace, systemUserAccount, operationalCRM, collaborativeCRM, fixedDeposit, savingAccount,
                    counterCash, debitCard, creditCard, secureLoan, unsecureLoan, billModule,
                    transferModule, customerPlan, executedPlan, finalcialInstrument, customerPortfolio, staffPerformance, customerProductRecommendation);
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Role Assigned Successfully!!!");
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        } else {
            System.out.println("Please do not leave blanks!");

        }
    }

    public List displayPermissions() {
        availablePermissions = smsbl.viewPermission();
        for (int i = 0; i < availablePermissions.size(); i++) {
            availablePermissionNames.add(i, availablePermissions.get(i).getModuleName());
        }
        return availablePermissionNames;

    }

    public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder();
        System.out.println("*****************Selected Item size is " + event.getItems());
        for (int i = 0; i < event.getItems().size(); i++) {
            selectedPermissionNames.add(event.getItems().get(i));

        }

        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");
        msg.setDetail(builder.toString());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void updateRolesToTrue() {
        //selectedPermissionNames = (List) event.getComponent().getAttributes().get("selectedPermissionNames");
        System.out.println("************" + selectedPermissionNames.size());
        for (int i = 0; i < selectedPermissionNames.size(); i++) {
            if (selectedPermissionNames.get(i).equals("systemUserWorkspace")) {
                systemUserWorkspace = true;
            } else if (selectedPermissionNames.get(i).equals("systemUserAccount")) {
                systemUserAccount = true;
            } else if (selectedPermissionNames.get(i).equals("operationalCRM")) {
                operationalCRM = true;
            } else if (selectedPermissionNames.get(i).equals("collaborativeCRM")) {
                collaborativeCRM = true;
            } else if (selectedPermissionNames.get(i).equals("fixedDeposit")) {
                fixedDeposit = true;
            } else if (selectedPermissionNames.get(i).equals("savingAccount")) {
                savingAccount = true;
            } else if (selectedPermissionNames.get(i).equals("counterCash")) {
                counterCash = true;
            } else if (selectedPermissionNames.get(i).equals("debitCard")) {
                debitCard = true;
            } else if (selectedPermissionNames.get(i).equals("creditCard")) {
                creditCard = true;
            } else if (selectedPermissionNames.get(i).equals("secureLoan")) {
                secureLoan = true;
            } else if (selectedPermissionNames.get(i).equals("unsecureLoan")) {
                unsecureLoan = true;
            } else if (selectedPermissionNames.get(i).equals("billModule")) {
                billModule = true;
            } else if (selectedPermissionNames.get(i).equals("transferModule")) {
                transferModule = true;
            } else if (selectedPermissionNames.get(i).equals("customerPlan")) {
                customerPlan = true;
            } else if (selectedPermissionNames.get(i).equals("executedPlan")) {
                executedPlan = true;
            } else if (selectedPermissionNames.get(i).equals("finalcialInstrument")) {
                finalcialInstrument = true;
            } else if (selectedPermissionNames.get(i).equals("customerPortfolio")) {
                customerPortfolio = true;
            } else if (selectedPermissionNames.get(i).equals("staffPerformance")) {
                staffPerformance = true;
            } else if (selectedPermissionNames.get(i).equals("customerProductRecommendation")) {
                customerProductRecommendation = true;
            }

        }
    }

    public void adminCreateStaff(ActionEvent event) throws UserExistException, EmailNotSendException, IOException {
        try {
            status = "inactive";
            if (staffId != null && staffIc != null && staffName != null
                    && staffEmail != null && mobileNumber != null && status != null) {

                staff = smsbl.createStaff(staffId, staffIc, staffName, staffEmail, mobileNumber, status);
                staffs.add(staff);
                System.out.println("Staff created successfully!");
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/SuperAdminManagement/assignRoleToStaff.xhtml");

            } else {
                System.out.println(staffId + " " + staffIc + " " + staffName + " " + mobileNumber + " " + status + " ************************");
                System.out.println("***************Please do not leave blanks!");

            }

        } catch (UserExistException | EmailNotSendException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void adminAssignStaffRole(ActionEvent event) throws StaffRoleExistException {
        try {
            System.out.println("***********Message from managed bean is staffId is: " + staffId + " newStaffId is " + staff.getId() + " roleName is " + selectedRoleName);
            roles.add(smsbl.assignStaffRole(staffId, staff.getId(), selectedRoleName));
            System.out.println("Assigned Role Successfully!!");
        } catch (StaffRoleExistException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public List adminViewOneStaff(ActionEvent event) throws UserNotExistException {
//        try {
        oneStaffRoleNames = new ArrayList<String>();
        staff = (Staff) event.getComponent().getAttributes().get("selectedStaff");
        System.out.println("************Message from managed bean-staffId is " + staff.getId());
        //    staff = smsbl.viewStaff(staffId);
        System.out.println("************Message from managed bean-staffId is " + staff.getStaffRoles().size());
        for (int i = 0; i < staff.getStaffRoles().size(); i++) {
            oneStaffRoleNames.add(staff.getStaffRoles().get(i).getRoleName());
        }

//        } catch (UserNotExistException ex) {
//            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
//            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
//        }
        return oneStaffRoleNames;
    }

    public void adminDeleteStaff(Long bankStaffID) throws UserNotExistException {
    //    try {
            boolean check = smsbl.deleteStaff(bankStaffID, staffId);
            staffs = smsbl.displayListOfStaff();
            if (check = true) {
                System.out.println("**********Message from managed bean: Staff is successfully deleted");
               // staff = smsbl.viewStaff(bankStaffID);
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Staff Deleted Successfully!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

            } else {
                System.out.println("**********Message from managed bean: Staff is unsuccessfully deleted");

            }
//        } catch (UserNotExistException ex) {
//            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
//            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
//        }
    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("lalalalalalalala");
        // if(bankStaffId != null && staffId != null && staffIc != null && staffName != null && staffEmail !=null && mobileNumber!=null){
        FacesMessage msg = new FacesMessage("Car Edited", ((Staff) event.getObject()).getStaffIc());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        bankStaffId = ((Staff) event.getObject()).getId();
        staffIc = ((Staff) event.getObject()).getStaffIc();
        staffName = ((Staff) event.getObject()).getStaffName();
        staffEmail = ((Staff) event.getObject()).getStaffEmail();
        mobileNumber = ((Staff) event.getObject()).getMobileNumber();

        System.out.println("************Message from managed bean staffid is ; " + bankStaffId);
        System.out.println("************Message from managed bean staffIC is ; " + staffIc);
        System.out.println("************Message from managed bean staffName is ; " + staffName);
        System.out.println("************Message from managed bean staffEmail is ; " + staffEmail);
        System.out.println("************Message from managed bean staffid is ; " + mobileNumber);

        smsbl.updateStaffInfo(staffId, bankStaffId, staffIc, staffName, staffEmail, mobileNumber);
        FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Updated Successfully");
        RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        smsbl.displayListOfStaff();
        // }else{
        //     FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Don't leave blanks!");
        //        RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        // }
    }
//    public void updateStaffProfile(ActionEvent event){
//        staff = (Staff) event.getComponent().getAttributes().get("selectedStaff");
//        smsbl.updateStaffInfo(staffId, staff, staffIc, staffName, staffEmail, mobileNumber)
//    }

    public void adminViewAllStaffs() {
        staffs = smsbl.displayListOfStaff();
    }

    public void editRole(Staff staff) throws IOException {
        
        roles = smsbl.viewOneStaffRole(staff);
        editedStaffId = staff.getId();

        System.out.println("***** Selected Staff ID is " + editedStaffId);
        System.out.println("*********Role size is " + roles.size());
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/SuperAdminManagement/editRoleForOneStaff.xhtml");

        //      smsbl.staffDeleteRole(bankStaffId, roleName);
    }

    public void editRoleDelete(ActionEvent event) throws IOException, UnexpectedErrorException {
     try{
         roleId = ((StaffRole) event.getComponent().getAttributes().get("selectedRole")).getId();
     
        roleName = ((StaffRole) event.getComponent().getAttributes().get("selectedRole")).getRoleName();
        System.out.println("***** Selected Staff ID is " + roleName);

       roles = smsbl.staffDeleteRole(editedStaffId, roleName);
       System.out.println("***********************After delete, no of roles: "+roles.size());
        //roles = staff.getStaffRoles();
       // System.out.println("**************Message after pressing delete role: " + msg);
        
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Deleted successfully");
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
      }catch( UnexpectedErrorException ex) {
      FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
      } 

    }

    public void addOneRoleForStaff(ActionEvent event) throws IOException,StaffAlreadyHasRoleException {
        try{
        roles.add(smsbl.staffAddRole(editedStaffId, selectedRoleNameToAdd));
        
      
            System.out.println("***************Added one role successfully!");
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/SuperAdminManagement/editRoleForOneStaff.xhtml");

        
        }catch (StaffAlreadyHasRoleException ex){
             FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            
        }
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public boolean isSystemUserWorkspace() {
        return systemUserWorkspace;
    }

    public void setSystemUserWorkspace(boolean systemUserWorkspace) {
        this.systemUserWorkspace = systemUserWorkspace;
    }

    public boolean isSystemUserAccount() {
        return systemUserAccount;
    }

    public void setSystemUserAccount(boolean systemUserAccount) {
        this.systemUserAccount = systemUserAccount;
    }

    public boolean isOperationalCRM() {
        return operationalCRM;
    }

    public void setOperationalCRM(boolean operationalCRM) {
        this.operationalCRM = operationalCRM;
    }

    public boolean isCollaborativeCRM() {
        return collaborativeCRM;
    }

    public void setCollaborativeCRM(boolean collaborativeCRM) {
        this.collaborativeCRM = collaborativeCRM;
    }

    public boolean isFixedDeposit() {
        return fixedDeposit;
    }

    public void setFixedDeposit(boolean fixedDeposit) {
        this.fixedDeposit = fixedDeposit;
    }

    public boolean isSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(boolean savingAccount) {
        this.savingAccount = savingAccount;
    }

    public boolean isCounterCash() {
        return counterCash;
    }

    public void setCounterCash(boolean counterCash) {
        this.counterCash = counterCash;
    }

    public boolean isDebitCard() {
        return debitCard;
    }

    public void setDebitCard(boolean debitCard) {
        this.debitCard = debitCard;
    }

    public boolean isCreditCard() {
        return creditCard;
    }

    public void setCreditCard(boolean creditCard) {
        this.creditCard = creditCard;
    }

    public boolean isSecureLoan() {
        return secureLoan;
    }

    public void setSecureLoan(boolean secureLoan) {
        this.secureLoan = secureLoan;
    }

    public boolean isUnsecureLoan() {
        return unsecureLoan;
    }

    public void setUnsecureLoan(boolean unsecureLoan) {
        this.unsecureLoan = unsecureLoan;
    }

    public boolean isBillModule() {
        return billModule;
    }

    public void setBillModule(boolean billModule) {
        this.billModule = billModule;
    }

    public boolean isTransferModule() {
        return transferModule;
    }

    public void setTransferModule(boolean transferModule) {
        this.transferModule = transferModule;
    }

    public boolean isCustomerPlan() {
        return customerPlan;
    }

    public void setCustomerPlan(boolean customerPlan) {
        this.customerPlan = customerPlan;
    }

    public boolean isExecutedPlan() {
        return executedPlan;
    }

    public void setExecutedPlan(boolean executedPlan) {
        this.executedPlan = executedPlan;
    }

    public boolean isFinalcialInstrument() {
        return finalcialInstrument;
    }

    public void setFinalcialInstrument(boolean finalcialInstrument) {
        this.finalcialInstrument = finalcialInstrument;
    }

    public boolean isCustomerPortfolio() {
        return customerPortfolio;
    }

    public void setCustomerPortfolio(boolean customerPortfolio) {
        this.customerPortfolio = customerPortfolio;
    }

    public boolean isStaffPerformance() {
        return staffPerformance;
    }

    public void setStaffPerformance(boolean staffPerformance) {
        this.staffPerformance = staffPerformance;
    }

    public boolean isCustomerProductRecommendation() {
        return customerProductRecommendation;
    }

    public void setCustomerProductRecommendation(boolean customerProductRecommendation) {
        this.customerProductRecommendation = customerProductRecommendation;
    }

    public Long getStaffRoleId() {
        return staffRoleId;
    }

    public void setStaffRoleId(Long staffRoleId) {
        this.staffRoleId = staffRoleId;
    }

    public List<Permission> getAvailablePermissions() {
        return availablePermissions;
    }

    public List<Permission> getSelectedPermissions() {
        return selectedPermissions;
    }

    public void setSelectedPermissions(List<Permission> selectedPermissions) {
        this.selectedPermissions = selectedPermissions;
    }

    public String getStaffIc() {
        return staffIc;
    }

    public void setStaffIc(String staffIc) {
        this.staffIc = staffIc;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DualListModel<String> getPermissionNames() {
        return permissionNames;
    }

    public void setPermissionNames(DualListModel<String> permissionNames) {
        this.permissionNames = permissionNames;
    }

    public List getSelectedPermissionNames() {
        return selectedPermissionNames;
    }

    public void setSelectedPermissionNames(List selectedPermissionNames) {
        this.selectedPermissionNames = selectedPermissionNames;
    }

    public List getAvailablePermissionNames() {
        return availablePermissionNames;
    }

    public void setAvailablePermissionNames(List availablePermissionNames) {
        this.availablePermissionNames = availablePermissionNames;
    }

    public void setAvailableRoles(List<Permission> availablePermissions) {
        this.availablePermissions = availablePermissions;
    }
}
