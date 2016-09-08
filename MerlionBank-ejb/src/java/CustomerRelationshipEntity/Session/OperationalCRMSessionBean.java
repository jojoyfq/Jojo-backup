/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerRelationshipEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import Exception.UserAlreadyActivatedException;
import Exception.UserNotExistException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author a0113893
 */
@Stateless
public class OperationalCRMSessionBean implements OperationalCRMSessionBeanLocal {
 private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    /*
    @Override
    public Customer searchCustomer(String ic) {
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (temp.isEmpty()) {
            System.out.println("Username " + ic + " does not exist!");
            throw new UserNotExistException("Username " + ic + " does not exist, please try again");
        }
        
            int size=temp.size();
            Customer customer=temp.get(size-1);
            if (customer.getStatus().equals("terminated")){
                 System.out.println("Username " + ic + " does not exist!");
            throw new UserNotExistException("Username " + ic + " does not exist, please try again");    
            }
            else if (customer.getStatus().equals("active")){
                 System.out.println("Username " + ic + "You have already activated your account!"); 
             throw new UserAlreadyActivatedException("You have already activated your account!");
            }
            else if (customer.getOnlineAccount().getAccountStatus().equals("locked")){
                System.out.println("Username " + ic + "Acount locked"); 
            throw new UserAlreadyActivatedException("You have already activated your account!");
            }
            else {
              System.out.println("Username " + ic + " IC check pass!");  
            }               
        
        
        
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
            Customer customer = temp.get(temp.size()-1);
            return customer;
    }
    
    @Override
    public void updateProfile(String ic, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialGoal){
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
            Customer customer = temp.get(temp.size()-1);
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            customer.setOccupation(occupation);
            customer.setFamilyInfo(familyInfo);
            customer.setFinancialGoal(financialGoal);
            em.merge(customer);
            em.flush();
            
            CustomerAction action=new CustomerAction(Calendar.getInstance().getTime(),"Update Profile",customer);
            em.persist(action);
            List<CustomerAction> customerActions=customer.getCustomerActions();
            customerActions.add(action);
            customer.setCustomerActions(customerActions);
            em.persist(customer);
            em.flush();
    }*/
}
