/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TellerServeCustomer.Session;

import CommonEntity.Customer;
import javax.ejb.Local;

/**
 *
 * @author ruijia
 */
@Local
public interface ServiceCustomerSessionBeanLocal {

    public Customer selectCustomer(String customerIc);
    
}
