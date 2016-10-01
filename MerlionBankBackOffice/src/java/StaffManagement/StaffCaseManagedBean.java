/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffManagement;

import CommonEntity.MessageEntity;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import CustomerRelationshipEntity.CaseEntity;
import CustomerRelationshipEntity.Session.CollaborativeCRMSessionBeanLocal;
import Exception.ListEmptyException;
import Exception.UserNotExistException;
import java.io.Serializable;
//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * @author apple
 */
@Named(value = "staffCaseManagedBean")
@SessionScoped
public class StaffCaseManagedBean implements Serializable {

    @EJB
    CollaborativeCRMSessionBeanLocal ccsbl;
    @Inject
    staffLogInManagedBean slimb;

    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private List<StaffRole> staffRoles;
    private List<Staff> relatedStaff;
    private List<CaseEntity> allCases;
    private String issueType;
    private Date caseCreatedTime;
    private String customerIc;
    private Long caseStaffId = slimb.getStaffId();
    private Long customerId;
    // private List availableRoleNames;
    private Map<String, String> availableRoleNames;
    private Map<String, String> availableStaffs;
    private String selectedRoleName;
    private MessageEntity selectedMsg;

    public MessageEntity getSelectedMsg() {
        return selectedMsg;
    }

    public void setSelectedMsg(MessageEntity selectedMsg) {
        this.selectedMsg = selectedMsg;
    }

    public String getSelectedRoleName() {
        return selectedRoleName;
    }

    public void setSelectedRoleName(String selectedRoleName) {
        this.selectedRoleName = selectedRoleName;
    }

    public Map<String, String> getAvailableStaffs() {
        return availableStaffs;
    }

    public void setAvailableStaffs(Map<String, String> availableStaffs) {
        this.availableStaffs = availableStaffs;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
    Map<String, String> map = new HashMap<String, String>();

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public Map<String, String> getAvailableRoleNames() {
        return availableRoleNames;
    }

    public void setAvailableRoleNames(Map<String, String> availableRoleNames) {
        this.availableRoleNames = availableRoleNames;
    }
    private CaseEntity caseEntity;
    private CaseEntity selectedCase;
    private String selectedStaffName;

    public String getSelectedStaffName() {
        return selectedStaffName;
    }

    public void setSelectedStaffName(String selectedStaffName) {
        this.selectedStaffName = selectedStaffName;
    }

    public StaffCaseManagedBean() {
    }

    @PostConstruct
    public void init() {

        staffRoles = new ArrayList<StaffRole>();
        //availableRoleNames = new ArrayList<>();
        availableRoleNames = new HashMap<String, String>();
        // availableRoleNames = slimb.getRoleNames();

        relatedStaff = new ArrayList<>();
        allCases = new ArrayList<>();
        caseEntity = new CaseEntity();
        selectedCase = new CaseEntity();

    }
    public void caseStaffViewAllCaseMsg(ActionEvent event){
        
    }

    public void staffViewAllCases(ActionEvent event) throws ListEmptyException {

        allCases = ccsbl.viewAllCase();
    }

    public void onCountryChange() {
        if (availableRoleNames != null && !availableRoleNames.equals("")) {
            availableStaffs = data.get(issueType);
        } else {
            availableStaffs = new HashMap<String, String>();
        }
    }

    public void staffCreateCase(ActionEvent event) throws ListEmptyException, UserNotExistException {
        try {
            for (int i = 0; i < slimb.getRoleNames().size(); i++) {
                availableRoleNames.put(slimb.getRoleNames().get(i).toString(), slimb.getRoleNames().get(i).toString());
                // issueType = slimb.getRoleNames().get(i).toString();
                relatedStaff = ccsbl.retrieveStaffsAccordingToRole(slimb.getRoleNames().get(i).toString());
                for (int j = 0; j < relatedStaff.size(); j++) {
                    map.put(relatedStaff.get(j).getStaffName(), relatedStaff.get(j).getStaffName());
                }
                data.put(slimb.getRoleNames().get(i).toString(), map);
            }
            selectedCase = (CaseEntity) event.getComponent().getAttributes().get("selectedCase");
            customerId = selectedCase.getCustomer().getId();
            customerIc = selectedCase.getCustomer().getIc();
            System.out.println("**********Selected case: the customer ic is " + customerIc);
           // issueType = (String) event.getComponent().getAttributes().get("selectedRoleName");

            // relatedStaff = ccsbl.retrieveStaffsAccordingToRole(issueType);
            selectedCase = ccsbl.createCase(caseCreatedTime, customerIc, caseStaffId);
        } catch (ListEmptyException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

        }
    }

    public void caseStaffCreateIssue(ActionEvent event) {
        selectedMsg = (MessageEntity) event.getComponent().getAttributes().get("selectedCaseMessage");
        ccsbl.addIssue(selectedMsg.getContent(), issueType, null, null, selectedStaffName, selectedCase);
    }

    public CollaborativeCRMSessionBeanLocal getCcsbl() {
        return ccsbl;
    }

    public void setCcsbl(CollaborativeCRMSessionBeanLocal ccsbl) {
        this.ccsbl = ccsbl;
    }

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

    public List<Staff> getRelatedStaff() {
        return relatedStaff;
    }

    public void setRelatedStaff(List<Staff> relatedStaff) {
        this.relatedStaff = relatedStaff;
    }

    public List<CaseEntity> getAllCases() {
        return allCases;
    }

    public void setAllCases(List<CaseEntity> allCases) {
        this.allCases = allCases;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public Date getCaseCreatedTime() {
        return caseCreatedTime;
    }

    public void setCaseCreatedTime(Date caseCreatedTime) {
        this.caseCreatedTime = caseCreatedTime;
    }

    public String getCustomerIc() {
        return customerIc;
    }

    public void setCustomerIc(String customerIc) {
        this.customerIc = customerIc;
    }

    public Long getCaseStaffId() {
        return caseStaffId;
    }

    public void setCaseStaffId(Long caseStaffId) {
        this.caseStaffId = caseStaffId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public CaseEntity getCaseEntity() {
        return caseEntity;
    }

    public void setCaseEntity(CaseEntity caseEntity) {
        this.caseEntity = caseEntity;
    }

    public CaseEntity getSelectedCase() {
        return selectedCase;
    }

    public void setSelectedCase(CaseEntity selectedCase) {
        this.selectedCase = selectedCase;
    }
}
