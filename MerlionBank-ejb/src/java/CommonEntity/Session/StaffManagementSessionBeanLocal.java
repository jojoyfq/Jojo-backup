/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import Exception.RoleAlreadyExistedException;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface StaffManagementSessionBeanLocal {
    
public boolean createRole(String roleName,boolean systemUserWorkspace,boolean systemUserAccount,boolean operationalCRM, 
        boolean collaborativeCRM,boolean fixedDeposit,boolean savingAccount, 
        boolean counterCash, boolean debitCard,boolean creditCard,boolean secureLoan,boolean unsecureLoan, 
        boolean billModule,boolean transferModule,boolean customerPlan,boolean executedPlan,boolean finalcialInstrument,
        boolean customerPortfolio,boolean staffPerformance,boolean customerProductRecommendation)throws RoleAlreadyExistedException;   
    
}
