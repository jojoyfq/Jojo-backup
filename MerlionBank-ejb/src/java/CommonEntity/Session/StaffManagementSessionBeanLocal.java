/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.StaffRole;
import Exception.RoleAlreadyExistedException;
import Exception.RoleHasStaffException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface StaffManagementSessionBeanLocal {
    
public Long createRole(Long staffId,String roleName) throws RoleAlreadyExistedException;

public boolean grantRole(Long staffId,Long staffRoleId,boolean systemUserWorkspace,boolean systemUserAccount,boolean operationalCRM, 
        boolean collaborativeCRM,boolean fixedDeposit,boolean savingAccount, 
        boolean counterCash, boolean debitCard,boolean creditCard,boolean secureLoan,boolean unsecureLoan, 
        boolean billModule,boolean transferModule,boolean customerPlan,boolean executedPlan,boolean finalcialInstrument,
        boolean customerPortfolio,boolean staffPerformance,boolean customerProductRecommendation);

public List<StaffRole> viewRoles ();

public boolean deleteRole(Long staffRoleId,Long staffId) throws RoleHasStaffException;


    
}
