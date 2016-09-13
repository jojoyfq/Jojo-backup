/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import CommonEntity.Permission;
import static CommonEntity.Session.AccountManagementSessionBean.SALT_LENGTH;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import CustomerRelationshipEntity.StaffAction;
import Exception.EmailNotSendException;
import Exception.RoleAlreadyExistedException;
import Exception.RoleHasStaffException;
import Exception.StaffRoleExistException;
import Exception.UserExistException;
import Other.Session.GeneratePassword;
import Other.Session.sendEmail;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author a0113893
 */
@Stateless
public class StaffManagementSessionBean implements StaffManagementSessionBeanLocal {
private static final Random RANDOM = new SecureRandom();

    public static final int SALT_LENGTH = 8;
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

//Super Admin create staff accounts
@Override
public boolean createStaff(Long staffID, String staffIc,String staffName, String staffEmail, String mobileNumber, String status) throws UserExistException{
    
  String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        System.out.println("Inside createAccount");

        Query q = em.createQuery("SELECT a FROM Staff a WHERE a.staffIc = :staffIc");
        q.setParameter("staffIc", staffIc);
        List<Staff> temp = new ArrayList(q.getResultList());
        if (!temp.isEmpty()) {
            System.out.println("User " + staffIc + " exists!");
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getStatus().equals("active")) {
                    throw new UserExistException("Staff " + staffIc + " exists!");
                }
                else if (temp.get(i).getStatus().equals("locked"))                
                    throw new UserExistException("Staff " + staffIc + " Account has been locked!");
                else if (temp.get(i).getStatus().equals("inactive"))
                    throw new UserExistException("Staff " + staffIc + " has an inavtive account. Please proceed to activation.");    
            }           

        }
        System.out.println("staff does not exist!");

        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }
        String password = GeneratePassword.createPassword();
        String tempPassword = password;

        password = passwordHash(password + salt);
        System.out.println("Password after hash&salt:" + password);  
        
        List<StaffRole> staffRoles = new ArrayList<StaffRole>();
        Staff staff=new Staff(staffIc, staffName, password, staffEmail, mobileNumber, "inactive", staffRoles);
        em.persist(staff);
        em.flush();
        
        recordStaffAction(staffID,"create a new staff"+staffIc,null);
        
        return true;
        
}

private void recordStaffAction(Long staffId, String actionDescription,Long customerId){
    
        Query queryStaff = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        queryStaff.setParameter("id", staffId);
        Staff staff = (Staff)queryStaff.getSingleResult();
        
        StaffAction action=new StaffAction(Calendar.getInstance().getTime(),actionDescription,customerId, staff);
            em.persist(action);
            List<StaffAction> staffActions=staff.getStaffActions();
            staffActions.add(action);
            staff.setStaffActions(staffActions);
            em.persist(staff);
            em.flush();
    
}

private String passwordHash(String pass) {
        String md5 = null;

        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(pass.getBytes(), 0, pass.length());

            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

@Override
public List<StaffRole> displayListOfRole(){
    Query query = em.createQuery("SELECT a FROM StaffRole a");
        List<StaffRole> staffRoles = new ArrayList(query.getResultList());
return staffRoles;
}

@Override
public Long assignStaffRole(Long staffId,Long newStaffId,Long staffRoleId) throws StaffRoleExistException,EmailNotSendException{

    Query queryStaff = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        queryStaff.setParameter("id", newStaffId);
        Staff newStaff = (Staff)queryStaff.getSingleResult();
        
    Query queryRole = em.createQuery("SELECT b FROM StaffRole b WHERE b.id = :id");
        queryRole.setParameter("id", staffRoleId);
        StaffRole staffRole = (StaffRole)queryRole.getSingleResult();
        
        List<StaffRole>staffRoles=newStaff.getStaffRoles();
        
        for (int i=0;i<staffRoles.size();i++){
            if (staffRoles.get(i).getId()==staffRoleId)
                throw new StaffRoleExistException("staff already has this role");
        }
        staffRoles.add(staffRole);
        newStaff.setStaffRoles(staffRoles);
        em.persist(newStaff);
        em.flush();
        
        List<Staff>staffList=new ArrayList<Staff>(staffRole.getStaffList());
        staffList.add(newStaff);
        staffRole.setStaffList(staffList);
        em.persist(staffRole);
        em.flush();
        
        recordStaffAction(staffId,"Assign Staff Roles"+newStaff.getStaffIc(),null);
        
        try {
            sendEmail(newStaff.getStaffName(),newStaff.getStaffEmail(),newStaff.getPassword());

        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
        return newStaffId;
        
}

private void sendEmail(String name, String email, String password) throws MessagingException {
        String subject = "Merlion Bank - Online Banking Staff Account \"" + name + "\" Created - Pending Activation";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Congratulations! System Administrators have successfully registered a Merlion Online Banking Staff Account for you!</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Temporary password: " + password
                + "<br />Please activate your account through this link: " + "</h2><br />" 
                //+ "<p style=\"color: #ff0000;\">Please noted that that you are required to transfer minimum SG$500 to your account in order to activate your saving account. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }


}
