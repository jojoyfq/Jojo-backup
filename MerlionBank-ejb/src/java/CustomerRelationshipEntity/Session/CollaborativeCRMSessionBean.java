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
import Exception.ListEmptyException;
import java.util.ArrayList;
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
public List<Staff> retrieveStaffsAccordingToRole(String issueType){
    
}
   
public Issue addIssue(String content, String issueType, String status, Long assignedStaff, CaseEntity caseEntity){
   
    Issue issue=new Issue()
}

}
