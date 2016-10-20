/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import CommonEntity.Session.StaffManagementSessionBeanLocal;
import WealthEntity.DiscretionaryAccount;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0113893
 */
@Stateless
public class WealthManagementSessionBean implements WealthManagementSessionBeanLocal {
 @PersistenceContext
    private EntityManager em;
 @EJB
    StaffManagementSessionBeanLocal smsbl;
 
 @Override
 public Long topUpAccount(Long staffId,Long accountId,BigDecimal amount){
     DiscretionaryAccount discretionaryAccount=em.find(DiscretionaryAccount.class,accountId);
     discretionaryAccount.getBalance().add(amount);
     smsbl.recordStaffAction(staffId, "create existing customer discretionary account", discretionaryAccount.getCustomer().getId());
 return accountId;
 }
 
 
}
