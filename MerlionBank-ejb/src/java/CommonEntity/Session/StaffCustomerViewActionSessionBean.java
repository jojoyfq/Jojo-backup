/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CommonEntity.Staff;
import CustomerRelationshipEntity.StaffAction;
import Exception.ListEmptyException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0113893
 */
@Stateless
public class StaffCustomerViewActionSessionBean implements StaffCustomerViewActionSessionBeanLocal {
@PersistenceContext
    private EntityManager em;

@Override
public List<CustomerAction>viewCustomerAction(Long customerId) throws ListEmptyException{
    
Customer customer=em.find(Customer.class,customerId);
List<CustomerAction> actions=customer.getCustomerActions();

if (actions.isEmpty())
    throw new ListEmptyException("Customer has no action record");

return actions;

}

@Override
public List<StaffAction>viewStaffAction(Long staffId) throws ListEmptyException{
    
Staff staff=em.find(Staff.class,staffId);
List<StaffAction> actions= staff.getStaffActions();

if (actions.isEmpty())
    throw new ListEmptyException("Customer has no action record");

return actions;

}

}
