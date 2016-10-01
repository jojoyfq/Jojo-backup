/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerRelationshipEntity.Session;

import CommonEntity.Customer;
import CommonEntity.Session.InboxManagementSessionBeanLocal;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import CustomerRelationshipEntity.CaseEntity;
import CustomerRelationshipEntity.Issue;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author a0113893
 */
@Stateless
public class CollaborativeCRMSessionBean implements CollaborativeCRMSessionBeanLocal {
@PersistenceContext
    private EntityManager em;
private InboxManagementSessionBeanLocal imsbl;

@Override
public List<StaffRole> displayListOfRoles() throws ListEmptyException{
   Query query = em.createQuery("SELECT a FROM StaffRole a");
        List<StaffRole> staffRoles = new ArrayList(query.getResultList());
         if (staffRoles.isEmpty()){
            throw new ListEmptyException("There are no available staffRoles! Please create a new one first");
         }
        return staffRoles;  
}

@Override
public List<Staff> retrieveStaffsAccordingToRole(String issueType)throws ListEmptyException{
     Query q = em.createQuery("SELECT a FROM StaffRole a WHERE a.roleName = :roleName");
        q.setParameter("roleName", issueType);
        StaffRole staffRole=(StaffRole)q.getSingleResult();
        List<Staff> staffList = staffRole.getStaffList();
        
        if (staffList.isEmpty()){
            throw new ListEmptyException("There are no available staff. Please choose another department.");
         }
       return staffList; 
    
    
}

@Override
public CaseEntity createCase(Date caseCreatedTime, String customerIc,Long caseStaffID) throws UserNotExistException{
    Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", customerIc);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (temp.isEmpty()) {
            System.out.println("Username " + customerIc + " does not exist!");
            throw new UserNotExistException("Username " + customerIc + " does not exist, please try again");
        }
        
        Query query = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        query.setParameter("id", caseStaffID);
        Staff staff = (Staff) query.getSingleResult();

        int size = temp.size();
        Customer customer = temp.get(size - 1);
    CaseEntity newCase=new CaseEntity("unresolved",Calendar.getInstance().getTime(),customer,staff);
    List<Issue>issueList=new ArrayList<Issue>();
    newCase.setIssues(issueList);
    em.persist(newCase);
    em.flush();
    return newCase;
}

@Override
public void addIssue(String content, String issueType, String status, String solution, Staff assignedStaff, CaseEntity newCase){
   
    Issue issue=new Issue(content,issueType,status,solution,newCase,assignedStaff);
    em.persist(issue);
    em.flush();
    
    List<Issue> issueList=newCase.getIssues();
    issueList.add(issue);
    newCase.setIssues(issueList);
    em.persist(newCase);
    em.flush();
    
}

@Override
public List<CaseEntity> viewAllCase()throws ListEmptyException{
    Query query = em.createQuery("SELECT a FROM CaseEntity a");
        List<CaseEntity> caseList = new ArrayList(query.getResultList());
        if (caseList.isEmpty()){
        throw new ListEmptyException("No case records.");
        }
        return caseList;
}

@Override
public List<Issue> viewAssignedCase(Long staffId) throws ListEmptyException{
    Query q = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        q.setParameter("id", staffId);
        Staff staff = (Staff) q.getSingleResult();
        
        List<Issue> issueList=staff.getIssues();
        
        if (issueList.isEmpty()){
        throw new ListEmptyException("No issue records.");
        }
        
        return issueList;
    
}

@Override
public boolean deleteIssue(Long staffId,Long issueId){
    Query q = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        q.setParameter("id", staffId);
        Staff staff = (Staff) q.getSingleResult();
        
        Query query = em.createQuery("SELECT a FROM Issue a WHERE a.id = :id");
        query.setParameter("id", issueId);
        Issue issue = (Issue) query.getSingleResult();
        
        issue.setStatus("falseEntry");
        em.persist(issue);
        em.flush();
        
        return true;
    
    
}

@Override 
public boolean deleteCase(Long staffId,Long caseId){
    Query q = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        q.setParameter("id", staffId);
        Staff staff = (Staff) q.getSingleResult();
        
        Query query = em.createQuery("SELECT a FROM CaseEntity a WHERE a.id = :id");
        query.setParameter("id", caseId);
        CaseEntity caseEntity = (CaseEntity) query.getSingleResult();
        
        List<Issue> issueList=new ArrayList<Issue>();
        issueList=caseEntity.getIssues();
        for (int i=0;i<issueList.size();i++){
            issueList.get(i).setStatus("falseEntry");
            em.persist(issueList.get(i));
               }
        
        caseEntity.setStatus("falseEntry");
        em.persist(caseEntity);
        em.flush();
        
        return true;   
}

//Staff request for more information from customer
@Override
public boolean staffModifyIssue(Long staffId, Long issueId,String solution)throws EmailNotSendException{
    Query query = em.createQuery("SELECT a FROM Issue a WHERE a.id = :id");
        query.setParameter("id", issueId);
        Issue issue = (Issue) query.getSingleResult();
        
        issue.setStatus("moreInfo");
        issue.setSolution(solution);
        issue.getCaseEntity().setStatus("moreInfo");
        
        em.persist(issue);
        em.flush();
        
        try{
        imsbl.sendMessage(issue.getCaseEntity().getCustomer().getId(),staffId, "Merlion Bank - Require more information for your issue",solution);
                }catch (EmailNotSendException ex){
                    throw new EmailNotSendException ("Emails not sent");
                }
        
   return true;
}

//Staff solve an issue
@Override 
public boolean staffSolveIssue(Long staffId, Long issueId,String solution){
    Query query = em.createQuery("SELECT a FROM Issue a WHERE a.id = :id");
        query.setParameter("id", issueId);
        Issue issue = (Issue) query.getSingleResult();
        
         issue.setStatus("solved");
        issue.setSolution(solution);
        
        em.persist(issue);
        em.flush();
        
   return true;
    
}

//Timer
@Override
public void closeCase() throws EmailNotSendException{
    Query query = em.createQuery("SELECT a FROM CaseEntity a");
        List<CaseEntity> caseList = new ArrayList(query.getResultList());
        for (int i=0;i<caseList.size();i++){
    CaseEntity caseEntity=caseList.get(i);
    List<Issue>issueList=caseEntity.getIssues();
    boolean flag=false;
    for (int j=0;j<issueList.size();j++){
        if (!issueList.get(j).getStatus().equals("solved"))
            flag=true;
    }
    if (flag==false){
       caseList.get(i).setStatus("solved");
       caseList.get(i).setCaseClosedTime(Calendar.getInstance().getTime());
       em.flush();
     try{
        imsbl.sendMessage(caseList.get(i).getCustomer().getId(),caseList.get(i).getStaff().getId(), "Merlion Bank - All your problems have been solved","Please go to case management page to view all replies from our respective staff.");
                }catch (EmailNotSendException ex){
                    throw new EmailNotSendException ("Emails not sent");
                }  
       
        
    }
        
}
    
}


}
