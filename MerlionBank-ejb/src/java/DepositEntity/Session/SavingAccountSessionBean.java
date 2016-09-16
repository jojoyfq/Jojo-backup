/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import DepositEntity.SavingAccountType;
import java.util.ArrayList;
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
public class SavingAccountSessionBean implements SavingAccountSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SavingAccount> getSavingAccount(Long customerID) {
        List<SavingAccount> activeSavingAccounts = new ArrayList();
        
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = q.getResultList();
        Customer customer = customers.get(0);
        for(int i=0;i<customer.getSavingAccounts().size();i++){
            if(customer.getSavingAccounts().get(i).getStatus().equals("active")){
                activeSavingAccounts.add(customer.getSavingAccounts().get(i));
            }
        }
        return activeSavingAccounts;
    }

    @Override
    public List<String> getSavingAccountType() {
        String savingAccountType;
        List<String> savingAccountString = new ArrayList<String>();

        Query q = em.createQuery("SELECT a FROM SavingAccountType a");
        List<SavingAccountType> savingAccountTypes = q.getResultList();

        if (savingAccountTypes.isEmpty()) {
           System.out.print("The accountType Table is Empty");
           savingAccountString.add("false");
           return savingAccountString;
        } else {
            System.out.print(savingAccountTypes.size());
            for (int i = 0; i < savingAccountTypes.size(); i++) {
                savingAccountType = savingAccountTypes.get(i).getAccountType();
                System.out.print(savingAccountTypes.get(i).getAccountType());
                savingAccountString.add(savingAccountType);
            }
            return savingAccountString;
        }
    }

}
