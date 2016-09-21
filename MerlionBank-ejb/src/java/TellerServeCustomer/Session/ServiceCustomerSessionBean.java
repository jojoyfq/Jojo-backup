/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TellerServeCustomer.Session;

import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ruijia
 */
@Stateless
public class ServiceCustomerSessionBean implements ServiceCustomerSessionBeanLocal {
    @PersistenceContext
    EntityManager em;
    
    @Override
    public Object selectCustomer(String customerIc){
        
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :customerIc");
        q.setParameter("customerIc", customerIc);
        List<Customer> customerList = q.getResultList();
        if(!customerList.isEmpty()){
        Customer customer = customerList.get(0);
        return customer;}
        else{
            return false;
        }
}
}
