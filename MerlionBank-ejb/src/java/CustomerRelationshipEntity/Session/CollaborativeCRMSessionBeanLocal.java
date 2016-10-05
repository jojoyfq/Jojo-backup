/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerRelationshipEntity.Session;

import CommonEntity.Staff;
import CommonEntity.StaffRole;
import CustomerRelationshipEntity.CaseEntity;
import CustomerRelationshipEntity.Issue;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.UserNotExistException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface CollaborativeCRMSessionBeanLocal {
    public List<StaffRole> displayListOfRoles() throws ListEmptyException;
    public List<Staff> retrieveStaffsAccordingToRole(String issueType)throws ListEmptyException;
    public CaseEntity createCase(Date caseCreatedTime, String customerIc,Long caseStaffID) throws UserNotExistException;
    public List<Issue> addIssue(String content, String issueType, String status, String solution, String assignedStaffName, CaseEntity newCase);
    public List<CaseEntity> viewAllCase()throws ListEmptyException;
    public List<Issue> viewAssignedCase(Long staffId) throws ListEmptyException;
    public List<Issue> deleteIssue(Long staffId,Long issueId);
    public List<CaseEntity> deleteCase(Long staffId,Long caseId);
    public Issue staffModifyIssue(Long staffId, Long issueId,String solution)throws EmailNotSendException;
    public Issue staffSolveIssue(Long staffId, Long issueId,String solution);
    public void closeCase() throws EmailNotSendException;
    
    //Customer views status of the case processing 
public List<CaseEntity> customerViewCases(Long customerId)throws ListEmptyException;

//Customer view case
public CaseEntity customerViewCase(Long caseId);

//Customer modifies the details of the case filed
public boolean customerModifyIssue(Long customerId, Long issueId,String content);

//Customer rate issue
public boolean customerRateIssue(Long customerId,Long issueId,Integer rating);


public boolean checkStatus(Long issueId);

}
