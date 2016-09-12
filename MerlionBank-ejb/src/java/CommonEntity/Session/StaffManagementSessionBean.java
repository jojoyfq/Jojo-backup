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
        if (systemUserWorkspace==true){
            currentTable.get(0).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(0));
        }
        else{
            currentTable.get(1).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(1));
        }
        //2
        if (systemUserAccount==true){
            currentTable.get(2).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(2));
        }
        else{
            currentTable.get(3).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(3));
        }
        //3
        if (operationalCRM==true){
            currentTable.get(4).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(4));
        }
        else{
            currentTable.get(5).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(5));
        }
        //4
        if (collaborativeCRM==true){
            currentTable.get(6).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(6));
        }
        else{
            currentTable.get(7).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(7));
        }
        //5
        if (fixedDeposit==true){
            currentTable.get(8).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(8));
        }
        else{
            currentTable.get(9).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(9));
        }
        //6
        if (savingAccount==true){
            currentTable.get(10).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(10));
        }
        else{
            currentTable.get(11).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(11));
        }
        //7
        if (counterCash==true){
            currentTable.get(12).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(12));
        }
        else{
            currentTable.get(13).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(13));
        }
        //8
        if (debitCard==true){
            currentTable.get(14).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(14));
        }
        else{
            currentTable.get(15).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(15));
        }
        //9
        if (creditCard==true){
            currentTable.get(16).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(16));
        }
        else{
            currentTable.get(17).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(17));
        }
        //10
        if (secureLoan==true){
            currentTable.get(18).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(18));
        }
        else{
            currentTable.get(19).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(19));
        }
        //11
        if (unsecureLoan==true){
            currentTable.get(20).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(20));
        }
        else{
            currentTable.get(21).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(21));
        }
        //12
        if (billModule==true){
            currentTable.get(22).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(22));
        }
        else{
            currentTable.get(23).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(23));
        }
        //13
        if (transferModule==true){
            currentTable.get(24).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(24));
        }
        else{
            currentTable.get(25).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(25));
        }
        //14
        if (customerPlan==true){
            currentTable.get(26).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(26));
        }
        else{
            currentTable.get(27).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(27));
        }
        //15
        if (executedPlan==true){
            currentTable.get(28).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(28));
        }
        else{
            currentTable.get(29).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(29));
        }
        //16
        if (finalcialInstrument==true){
            currentTable.get(30).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(30));
        }
        else{
            currentTable.get(31).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(31));
        }
        //17
        if (customerPortfolio==true){
            currentTable.get(32).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(32));
        }
        else{
            currentTable.get(33).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(33));
        }
        //18
        if (staffPerformance==true){
            currentTable.get(34).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(34));
        }
        else{
            currentTable.get(35).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(35));
        }
        //19
        if (customerProductRecommendation==true)
           {
            currentTable.get(36).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(36));
        }
        else{
            currentTable.get(37).getStaffRoles().add(staffRole);
            em.flush();
            permissions.add(currentTable.get(37));
        }
        
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

@Override
public List<Permission> viewPermission (){
    Query query = em.createQuery("SELECT a FROM Permission a");
        List<Permission> currentPermission = new ArrayList(query.getResultList());
        List<Permission>permissionDisplay=new ArrayList<Permission>();
        permissionDisplay.add(currentPermission.get(0));
        permissionDisplay.add(currentPermission.get(2));
        permissionDisplay.add(currentPermission.get(4));
        permissionDisplay.add(currentPermission.get(6));
        permissionDisplay.add(currentPermission.get(8));
        permissionDisplay.add(currentPermission.get(10));
        permissionDisplay.add(currentPermission.get(12));
        permissionDisplay.add(currentPermission.get(14));
        permissionDisplay.add(currentPermission.get(16));
        permissionDisplay.add(currentPermission.get(18));
        permissionDisplay.add(currentPermission.get(20));
        permissionDisplay.add(currentPermission.get(22));
        permissionDisplay.add(currentPermission.get(24));
        permissionDisplay.add(currentPermission.get(26));
        permissionDisplay.add(currentPermission.get(28));
        permissionDisplay.add(currentPermission.get(30));
        permissionDisplay.add(currentPermission.get(32));
        permissionDisplay.add(currentPermission.get(34));
        permissionDisplay.add(currentPermission.get(36));
        return permissionDisplay;             
}

//modifyRole - add Permission
@Override
public boolean addPermission(Long staffId,Long staffRoleId,Long permissionId) {

     Query queryStaff = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        queryStaff.setParameter("id", staffId);
        Staff staff = (Staff)queryStaff.getSingleResult();
        
         Query queryRole = em.createQuery("SELECT c FROM StaffRole c WHERE c.id = :id");
        queryRole.setParameter("id", staffRoleId);
        StaffRole staffRole = (StaffRole)queryRole.getSingleResult();
        List<Permission> currentPermissions = new ArrayList<Permission>(staffRole.getPermissions());
        
         Query queryPermission = em.createQuery("SELECT b FROM Permission b WHERE b.id = :id");
        queryPermission.setParameter("id", permissionId);
        Permission falsePermission = (Permission)queryPermission.getSingleResult();
        
        Query queryPermission2 = em.createQuery("SELECT b FROM Permission b WHERE b.id = :id");
        queryPermission2.setParameter("id", permissionId-1L);
        Permission truePermission = (Permission)queryPermission2.getSingleResult();
        
        currentPermissions.add(truePermission);
        staffRole.setPermissions(currentPermissions);
        em.persist(staffRole);
        em.flush();
        
        List<StaffRole>trueStaffRoles=new ArrayList<StaffRole>(truePermission.getStaffRoles());
        trueStaffRoles.add(staffRole);
        truePermission.setStaffRoles(trueStaffRoles);
        em.persist(truePermission);
        em.flush();
        
        List<StaffRole>falseStaffRoles= new ArrayList<StaffRole>(falsePermission.getStaffRoles());
        List<StaffRole> updateStaffRoles = new ArrayList<StaffRole>();
        
        for (int i=0;i<falseStaffRoles.size();i++){
           if (falseStaffRoles.get(0).getId()!=staffRoleId) 
               updateStaffRoles.add(falseStaffRoles.get(i));
        }
        falsePermission.setStaffRoles(updateStaffRoles);
        em.persist(falsePermission);
        em.flush();
        
        return true; 
        
}

//modifyRole - delete Permission
@Override
public boolean deletePermission(Long staffId,Long staffRoleId,Long permissionId){
  Query queryStaff = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        queryStaff.setParameter("id", staffId);
        Staff staff = (Staff)queryStaff.getSingleResult();
        
         Query queryRole = em.createQuery("SELECT c FROM StaffRole c WHERE c.id = :id");
        queryRole.setParameter("id", staffRoleId);
        StaffRole staffRole = (StaffRole)queryRole.getSingleResult();
        List<Permission> currentPermissions = new ArrayList<Permission>(staffRole.getPermissions());
        
         Query queryPermission = em.createQuery("SELECT b FROM Permission b WHERE b.id = :id");
        queryPermission.setParameter("id", permissionId);
        Permission truePermission = (Permission)queryPermission.getSingleResult();
        
        Query queryPermission2 = em.createQuery("SELECT b FROM Permission b WHERE b.id = :id");
        queryPermission2.setParameter("id", permissionId+1L);
        Permission falsePermission = (Permission)queryPermission2.getSingleResult();  
        
        List<Permission> temp=new ArrayList<Permission>();
        for (int i=0;i<currentPermissions.size();i++){
           if (currentPermissions.get(0).getId()!=permissionId) 
               temp.add(currentPermissions.get(i));
        }
        staffRole.setPermissions(temp);
        em.persist(staffRole);
        em.flush();
        
        List<StaffRole> updateStaffRoles=new ArrayList<StaffRole>(falsePermission.getStaffRoles());
        updateStaffRoles.add(staffRole);
        falsePermission.setStaffRoles(updateStaffRoles);
        em.persist(falsePermission);
        em.flush();
        
        updateStaffRoles=truePermission.getStaffRoles();
        List<StaffRole> tempRole=new ArrayList<StaffRole>();
        for (int i=0;i<updateStaffRoles.size();i++){
           if (updateStaffRoles.get(0).getId()!=staffRoleId) 
               tempRole.add(updateStaffRoles.get(i));
        }
        truePermission.setStaffRoles(tempRole);
        em.persist(truePermission);
        em.flush();
        
        return true;  
} 
}
