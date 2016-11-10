/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.CustomerAction;
import CustomerRelationshipEntity.StaffAction;
import Exception.ListEmptyException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author a0113893
 */
@Remote
public interface StaffCustomerViewActionSessionBeanRemote {
    public List<CustomerAction>viewCustomerAction(Long customerId) throws ListEmptyException;

    public List<StaffAction> viewStaffAction(Long staffId) throws ListEmptyException;

}
