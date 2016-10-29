/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffManagement;

import CommonEntity.CustomerMessage;
import CommonEntity.MessageEntity;
import CommonEntity.Session.InboxManagementSessionBeanLocal;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import CustomerRelationshipEntity.CaseEntity;
import CustomerRelationshipEntity.Issue;
import CustomerRelationshipEntity.Session.CollaborativeCRMSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.IssueSolvedException;
import Exception.ListEmptyException;
import Exception.UserNotExistException;
import java.io.IOException;
import java.io.Serializable;
//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author apple
 */
@Named(value = "staffCaseManagedBean")
@SessionScoped
public class StaffCaseManagedBean implements Serializable {

    @EJB
    CollaborativeCRMSessionBeanLocal ccsbl;
    @EJB
    InboxManagementSessionBeanLocal imsbl;
    @Inject
    staffLogInManagedBean slimb;
   
    
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private List<StaffRole> staffRoles;
    private List<Staff> relatedStaff;
    private List<CaseEntity> allCases;
    private String issueType;
    private Date caseCreatedTime;
    private String customerIc;
    private Long caseStaffId;
    private Long customerId;
    private Long bankStaffId;
    private Long justCreatedCaseId;

    public Long getJustCreatedCaseId() {
        return justCreatedCaseId;
    }

    public void setJustCreatedCaseId(Long justCreatedCaseId) {
        this.justCreatedCaseId = justCreatedCaseId;
    }

    // private List availableRoleNames;
    private Map<String, String> availableRoleNames;
    private Map<String, String> availableStaffs;
    private String selectedRoleName;
    private CustomerMessage selectedMsg;
    private List<CustomerMessage> allCaseMessages;
    private List<Issue> allIssues;
    private String issueContent;
    Map<String, String> map = new HashMap<String, String>();
    private CaseEntity caseEntity;
    private CaseEntity selectedCase;
    private String selectedStaffName;
    private List<Issue> oneBankStaffAllIssues;

    private Issue selectedIssue;
    private String solution;
    private List<Issue> oneCaseAllIssues;

    public List<Issue> getOneCaseAllIssues() {
        return oneCaseAllIssues;
    }

    public void setOneCaseAllIssues(List<Issue> oneCaseAllIssues) {
        this.oneCaseAllIssues = oneCaseAllIssues;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Issue getSelectedIssue() {
        return selectedIssue;
    }

    public void setSelectedIssue(Issue selectedIssue) {
        this.selectedIssue = selectedIssue;
    }

    public List<Issue> getOneBankStaffAllIssues() {
        return oneBankStaffAllIssues;
    }

    public void setOneBankStaffAllIssues(List<Issue> oneBankStaffAllIssues) {
        this.oneBankStaffAllIssues = oneBankStaffAllIssues;
    }

    public StaffCaseManagedBean() {
    }

    @PostConstruct
    public void init() {
        selectedIssue = new Issue();
        staffRoles = new ArrayList<StaffRole>();
        //availableRoleNames = new ArrayList<>();
        availableRoleNames = new HashMap<String, String>();
        // availableRoleNames = slimb.getRoleNames();

        relatedStaff = new ArrayList<>();
        allCases = new ArrayList<>();
        caseEntity = new CaseEntity();
        selectedCase = new CaseEntity();
        allCaseMessages = new ArrayList<>();
        allIssues = new ArrayList<>();
        // caseStaffId = slimb.getStaffId();
        caseStaffId = 4L;
        bankStaffId = slimb.getStaffId();
        oneBankStaffAllIssues = new ArrayList<>();
        oneCaseAllIssues = new ArrayList<>();

    }

    public List<CustomerMessage> caseStaffViewAllCaseMsg(ActionEvent event) throws IOException {

        allCaseMessages = imsbl.StaffViewAllCaseMessage();
        System.out.println("************Case message size is " + allCaseMessages.size());
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/CaseManagement/viewAllNewCaseMessages.xhtml");
        return allCaseMessages;

    }

    public List<Issue> issuesUnderOneCase(ActionEvent event) {
        System.out.println("*********** in issueUnderOneCase function alr!!!!");
        selectedCase = (CaseEntity) event.getComponent().getAttributes().get("selectedCase");
        System.out.println("**********Selected case to view all the issues, selectedCase.getId()" + selectedCase.getId());
        oneCaseAllIssues = selectedCase.getIssues();
        return oneCaseAllIssues;

    }

    public void staffViewAllCases(ActionEvent event) throws ListEmptyException, IOException {
try{
        allCases = ccsbl.viewAllCase();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/CaseManagement/viewAllCases.xhtml");
}catch(ListEmptyException| IOException ex ){
FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

}
    }

    public void onCountryChange() {
        if (availableRoleNames != null && !availableRoleNames.equals("")) {
            availableStaffs = data.get(issueType);
        } else {
            availableStaffs = new HashMap<String, String>();
        }
    }

    public void staffCreateCase(ActionEvent event) throws UserNotExistException, IOException, ListEmptyException {

//            for (int i = 0; i < slimb.getRoleNames().size(); i++) {
//                availableRoleNames.put(slimb.getRoleNames().get(i).toString(), slimb.getRoleNames().get(i).toString());
//                System.out.println("************** " + availableRoleNames);
//                // issueType = slimb.getRoleNames().get(i).toString();
//                relatedStaff = ccsbl.retrieveStaffsAccordingToRole(slimb.getRoleNames().get(i).toString());
//                for (int j = 0; j < relatedStaff.size(); j++) {
//                    map.put(relatedStaff.get(j).getStaffName(), relatedStaff.get(j).getStaffName());
//                }
//                data.put(slimb.getRoleNames().get(i).toString(), map);
//                map = new HashMap<String, String>();
//            }
        selectedMsg = (CustomerMessage) event.getComponent().getAttributes().get("selectedCaseMessage");
        customerId = selectedMsg.getCustomer().getId();
        customerIc = selectedMsg.getCustomer().getIc();
        
        System.out.println("**********Selected case message: the customer ic is " + customerIc);
           // issueType = (String) event.getComponent().getAttributes().get("selectedRoleName");

        // relatedStaff = ccsbl.retrieveStaffsAccordingToRole(issueType);
        imsbl.readCustomerMessage(selectedMsg.getId());
        selectedCase = ccsbl.createCase(null, customerIc, caseStaffId,selectedMsg.getId());
        
        allCases  = ccsbl.viewAllCase();
        justCreatedCaseId = selectedCase.getId();
        FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Case created successfully! The Case ID is " + justCreatedCaseId);
        RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
          //  FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/CaseManagement/createIssues.xhtml");

    }

    public void goToCreateIssuePage(ActionEvent event) throws IOException, ListEmptyException {
      try{  selectedCase = (CaseEntity) event.getComponent().getAttributes().get("selectedCase");
        allIssues = selectedCase.getIssues();
        System.out.println("********selected case id is " + selectedCase.getId());
        for (int i = 0; i < slimb.getRoleNames().size(); i++) {
            availableRoleNames.put(slimb.getRoleNames().get(i).toString(), slimb.getRoleNames().get(i).toString());
            System.out.println("************** " + availableRoleNames);
            // issueType = slimb.getRoleNames().get(i).toString();
            relatedStaff = ccsbl.retrieveStaffsAccordingToRole(slimb.getRoleNames().get(i).toString());
            for (int j = 0; j < relatedStaff.size(); j++) {
                map.put(relatedStaff.get(j).getStaffName(), relatedStaff.get(j).getStaffName());
            }
            data.put(slimb.getRoleNames().get(i).toString(), map);
            map = new HashMap<String, String>();
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/CaseManagement/createIssues.xhtml");
      }catch(IOException| ListEmptyException ex){
      FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
      }
    }

    public void caseStaffCreateIssue(ActionEvent event) {
        selectedMsg = (CustomerMessage) event.getComponent().getAttributes().get("selectedCaseMessage");
        System.out.println("************ issue content " + issueContent);
        System.out.println("************ issue type " + issueType);
        System.out.println("************ selectedStaffName " + selectedStaffName);
        System.out.println("************Selected case ID is " + selectedCase.getId());
        System.out.println("************");
        System.out.println("************");
        System.out.println("just now case created the id is " + justCreatedCaseId);

        System.out.println("************issuesize for this case " + allIssues.size());
        allIssues.add(ccsbl.addIssue(issueContent, issueType, null, null, selectedStaffName, selectedCase.getId()));
        FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Issue submitted successfully!!!");
        RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

    }

    public void openLevel1() {

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        RequestContext.getCurrentInstance().openDialog("/createIssuesDialog", options, null);
        System.out.println("************level 1 is opened");
    }

    public void onReturnFromLevel1(SelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data Returned", event.getObject().toString()));
    }

    public List<Issue> staffViewAssignedIssue(ActionEvent event) throws ListEmptyException, IOException {
        try {
            System.out.println("*************bank staff id " + bankStaffId);
            oneBankStaffAllIssues = ccsbl.viewAssignedCase(bankStaffId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/CaseManagement/viewAllAssignedIssues.xhtml");

        } catch (ListEmptyException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

        }
        return oneBankStaffAllIssues;
    }

    public List<Issue> staffDeleteAssingnedIssue(ActionEvent event) {
        selectedIssue = (Issue) event.getComponent().getAttributes().get("selectedIssue");

        System.out.println("**********staff id that deleted the issue is " + bankStaffId);
        System.out.println("**************selected issue id is " + selectedIssue.getId());
        oneBankStaffAllIssues = ccsbl.deleteIssue(bankStaffId, selectedIssue.getId());
        FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Issue deleted successfully!!");
        RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        return oneBankStaffAllIssues;
    }

    public Issue staffRequireMoreInfo(ActionEvent event) throws EmailNotSendException, IssueSolvedException {
        try {
            selectedIssue = (Issue) event.getComponent().getAttributes().get("selectedIssue");
            Long issueId = selectedIssue.getId();
            System.out.println("**********Staff id which required more info is " + bankStaffId);
            System.out.println("**************selected issue id is " + selectedIssue.getId());

            oneBankStaffAllIssues = ccsbl.staffModifyIssue(bankStaffId, issueId, solution);
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Message sent successfully!!");
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

        } catch (EmailNotSendException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
        return selectedIssue;
    }

    public Issue staffSolveIssue(ActionEvent event) {
        selectedIssue = (Issue) event.getComponent().getAttributes().get("selectedIssue");

        System.out.println("**********Staff id which solved issue is " + bankStaffId);
        System.out.println("**************selected issue id is " + selectedIssue.getId());
        Long issueId = selectedIssue.getId();
        selectedIssue = ccsbl.staffSolveIssue(bankStaffId, issueId, solution);
        return selectedIssue;
    }

    public List<CaseEntity> caseStaffDeleteCase(ActionEvent event) {
        selectedCase = (CaseEntity) event.getComponent().getAttributes().get("selectedCase");

        System.out.println("**********Case Staff id which solved issue is " + caseStaffId);
        System.out.println("**************selected case id is " + selectedCase.getId());
        allCases = ccsbl.deleteCase(caseStaffId, selectedCase.getId());
        return allCases;
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

    public String getIssueContent() {
        return issueContent;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    public List<Issue> getAllIssues() {
        return allIssues;
    }

    public void setAllIssues(List<Issue> allIssues) {
        this.allIssues = allIssues;
    }

    public InboxManagementSessionBeanLocal getImsbl() {
        return imsbl;
    }

    public void setImsbl(InboxManagementSessionBeanLocal imsbl) {
        this.imsbl = imsbl;
    }

    public List<CustomerMessage> getAllCaseMessages() {
        return allCaseMessages;
    }

    public void setAllCaseMessages(List<CustomerMessage> allCaseMessages) {
        this.allCaseMessages = allCaseMessages;
    }

    public CustomerMessage getSelectedMsg() {
        return selectedMsg;
    }

    public void setSelectedMsg(CustomerMessage selectedMsg) {
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

    public String getSelectedStaffName() {
        return selectedStaffName;
    }

    public void setSelectedStaffName(String selectedStaffName) {
        this.selectedStaffName = selectedStaffName;
    }

    public Long getBankStaffId() {
        return bankStaffId;
    }

    public void setBankStaffId(Long bankStaffId) {
        this.bankStaffId = bankStaffId;
    }

}
