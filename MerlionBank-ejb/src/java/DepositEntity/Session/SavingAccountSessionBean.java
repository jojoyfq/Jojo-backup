/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Bella
 */
@Stateless
public class SavingAccountSessionBean implements SavingAccountSessionBeanLocal{
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<SavingAccount> getSavingAccount(Long customerID){
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID",customerID );
        List<Customer> customers = q.getResultList();
        Customer customer = customers.get(0);
        return customer.getSavingAccounts();
    }
    
    
}
