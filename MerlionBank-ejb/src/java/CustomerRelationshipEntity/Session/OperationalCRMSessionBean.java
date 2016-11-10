/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerRelationshipEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CommonEntity.Staff;
import CustomerRelationshipEntity.StaffAction;
import Exception.UserAlreadyActivatedException;
import Exception.UserNotExistException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author a0113893
 */
@Stateless
public class OperationalCRMSessionBean implements OperationalCRMSessionBeanLocal,OperationalCRMSessionBeanRemote {
    @PersistenceContext
 private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Customer searchCustomer(String ic) throws UserNotExistException{
        System.out.println("Inside CRM session bean, IC is: "+ic);
        
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
  
        if (temp.isEmpty()) {
            System.out.println("Username " + ic + " does not exist!");
            throw new UserNotExistException("Username " + ic + " does not exist, please try again");
        }
        
            int size=temp.size();
            
             System.out.println("Inside CRM session bean, size is: "+size);
            Customer customer=temp.get(size-1);
            if (customer.getStatus().equals("terminated")){
                 System.out.println("Username " + ic + " does not exist!");
            throw new UserNotExistException("Username " + ic + " does not exist, please try again");    
            }
            else {
              System.out.println("Username " + ic + " IC check pass!");  
            }               
            return customer;
    }
    
    @Override
    public void updateProfile(Long staffID, Long customerID, String ic, String name, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialGoal){
        System.out.println("Inside update profile session bean");
        Query query = em.createQuery("SELECT a FROM Staff a WHERE a.id = :id");
        query.setParameter("id", staffID);
        Staff staff = (Staff)query.getSingleResult(); 
        
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :id");
        q.setParameter("id", customerID);
        Customer customer = (Customer)q.getSingleResult();
        
        System.out.println("customer name: "+customer.getName());
         System.out.println("customer noe name: "+name);
        customer.setIc(ic);
        customer.setName(name);
        customer.setDateOfBirth(dateOfBirth);
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            customer.setOccupation(occupation);
            customer.setFamilyInfo(familyInfo);
            customer.setFinancialGoal(financialGoal);
            em.merge(customer);
            em.persist(customer);
            em.flush();
            
            StaffAction action=new StaffAction(Calendar.getInstance().getTime(),"update Customer profile",customerID, staff);
            em.persist(action);
            List<StaffAction> staffActions=staff.getStaffActions();
            staffActions.add(action);
            staff.setStaffActions(staffActions);
            em.persist(staff);
            em.flush();
            
      System.out.println("End update profile session bean");      
    }
}
