/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import CommonEntity.Permission;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import CustomerRelationshipEntity.StaffAction;
import Exception.RoleAlreadyExistedException;
import Exception.RoleHasStaffException;
import java.util.ArrayList;
import java.util.Calendar;
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
public class StaffManagementSessionBean implements StaffManagementSessionBeanLocal {

@PersistenceContext
    private EntityManager em;

@Override
public Long createRole(Long staffId,String roleName) throws RoleAlreadyExistedException{

    Query q = em.createQuery("SELECT a FROM StaffRole a WHERE a.roleName = :roleName");
        q.setParameter("roleName", roleName);
        List<StaffRole> temp = new ArrayList(q.getResultList());
        
        if (temp.isEmpty()){
            throw new RoleAlreadyExistedException("This role already existed in the system");
        }
        List<Staff> staffList = new ArrayList<Staff>();
        List<Permission>permissions=new ArrayList<Permission>();       
        StaffRole staffRole=new StaffRole(roleName, staffList,permissions);     
        em.persist(staffRole);
        em.flush();  
        
        Query queryStaff = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        queryStaff.setParameter("id", staffId);
        Staff staff = (Staff)queryStaff.getSingleResult();
        
        StaffAction action=new StaffAction(Calendar.getInstance().getTime(),"Create new role",null, staff);
            em.persist(action);
            List<StaffAction> staffActions=staff.getStaffActions();
            staffActions.add(action);
            staff.setStaffActions(staffActions);
            em.persist(staff);
            em.flush();
            
        return staffRole.getId();
        
}

@Override
public boolean grantRole(Long staffId,Long staffRoleId,boolean systemUserWorkspace,boolean systemUserAccount,boolean operationalCRM, 
        boolean collaborativeCRM,boolean fixedDeposit,boolean savingAccount, 
        boolean counterCash, boolean debitCard,boolean creditCard,boolean secureLoan,boolean unsecureLoan, 
        boolean billModule,boolean transferModule,boolean customerPlan,boolean executedPlan,boolean finalcialInstrument,
        boolean customerPortfolio,boolean staffPerformance,boolean customerProductRecommendation) {

     Query queryStaff = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        queryStaff.setParameter("id", staffId);
        Staff staff = (Staff)queryStaff.getSingleResult();
        
         Query queryRole = em.createQuery("SELECT c FROM StaffRole c WHERE c.id = :id");
        queryRole.setParameter("id", staffRoleId);
        StaffRole staffRole = (StaffRole)queryRole.getSingleResult();
        
    
    
        Query query = em.createQuery("SELECT a FROM Permission a");
        List<Permission> currentTable = new ArrayList(query.getResultList());
        List<Permission> permissions = new ArrayList<Permission>();
        
        //1
        if (systemUserWorkspace==true)
            permissions.add(currentTable.get(0));
        else
            permissions.add(currentTable.get(1));
        //2
        if (systemUserAccount==true)
            permissions.add(currentTable.get(2));
        else
            permissions.add(currentTable.get(3));
        //3
        if (operationalCRM==true)
            permissions.add(currentTable.get(4));
        else
            permissions.add(currentTable.get(5));
        //4
        if (collaborativeCRM==true)
            permissions.add(currentTable.get(6));
        else
            permissions.add(currentTable.get(7));
        //5
        if (fixedDeposit==true)
            permissions.add(currentTable.get(8));
        else
            permissions.add(currentTable.get(9));
        //6
        if (savingAccount==true)
            permissions.add(currentTable.get(10));
        else
            permissions.add(currentTable.get(11));
        //7
        if (counterCash==true)
            permissions.add(currentTable.get(12));
        else
            permissions.add(currentTable.get(13));
        //8
        if (debitCard==true)
            permissions.add(currentTable.get(14));
        else
            permissions.add(currentTable.get(15));
        //9
        if (creditCard==true)
            permissions.add(currentTable.get(16));
        else
            permissions.add(currentTable.get(17));
        //10
        if (secureLoan==true)
            permissions.add(currentTable.get(18));
        else
            permissions.add(currentTable.get(19));
        //11
        if (unsecureLoan==true)
            permissions.add(currentTable.get(20));
        else
            permissions.add(currentTable.get(21));
        //12
        if (billModule==true)
            permissions.add(currentTable.get(22));
        else
            permissions.add(currentTable.get(23));
        //13
        if (transferModule==true)
            permissions.add(currentTable.get(24));
        else
            permissions.add(currentTable.get(25));
        //14
        if (customerPlan==true)
            permissions.add(currentTable.get(26));
        else
            permissions.add(currentTable.get(27));
        //15
        if (executedPlan==true)
            permissions.add(currentTable.get(28));
        else
            permissions.add(currentTable.get(29));
        //16
        if (finalcialInstrument==true)
            permissions.add(currentTable.get(30));
        else
            permissions.add(currentTable.get(31));
        //17
        if (customerPortfolio==true)
            permissions.add(currentTable.get(32));
        else
            permissions.add(currentTable.get(33));
        //18
        if (staffPerformance==true)
            permissions.add(currentTable.get(34));
        else
            permissions.add(currentTable.get(35));
        //19
        if (customerProductRecommendation==true)
            permissions.add(currentTable.get(36));
        else
            permissions.add(currentTable.get(37));
        
       staffRole.setPermissions(permissions);
       em.flush();
        
        StaffAction action=new StaffAction(Calendar.getInstance().getTime(),"grant role's permission",null, staff);
            em.persist(action);
            List<StaffAction> staffActions=staff.getStaffActions();
            staffActions.add(action);
            staff.setStaffActions(staffActions);
            em.persist(staff);
            em.flush();
             
 return true;   
}

@Override
public List<StaffRole> viewRoles (){ 
        Query query = em.createQuery("SELECT a FROM StaffRole a");
        List<StaffRole> roleList = new ArrayList(query.getResultList());
        return roleList;
}

@Override
public boolean deleteRole(Long staffRoleId,Long staffId) throws RoleHasStaffException {
   Query q = em.createQuery("SELECT a FROM StaffRole a WHERE a.id = :id");
        q.setParameter("id", staffRoleId);
        StaffRole staffRole = (StaffRole)q.getSingleResult(); 
        
        if (!staffRole.getStaffList().isEmpty()){
          throw new RoleHasStaffException("This role has existing staff");  
        }
        
        em.remove(staffRole);
        em.flush();
        
        Query queryStaff = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        queryStaff.setParameter("id", staffId);
        Staff staff = (Staff)queryStaff.getSingleResult();
        
        StaffAction action=new StaffAction(Calendar.getInstance().getTime(),"Create new role",null, staff);
            em.persist(action);
            List<StaffAction> staffActions=staff.getStaffActions();
            staffActions.add(action);
            staff.setStaffActions(staffActions);
            em.persist(staff);
            em.flush();
              
        return true;
    
}
}
