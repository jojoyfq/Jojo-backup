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
    public boolean deleteIssue(Long staffId,Long issueId);
    public boolean deleteCase(Long staffId,Long caseId);
    public boolean staffModifyIssue(Long staffId, Long issueId,String solution)throws EmailNotSendException;
    public boolean staffSolveIssue(Long staffId, Long issueId,String solution);
    public void closeCase() throws EmailNotSendException;
    
    
    
}
