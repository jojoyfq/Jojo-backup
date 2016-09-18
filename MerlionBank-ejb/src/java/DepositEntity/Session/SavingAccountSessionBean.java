/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import DepositEntity.SavingAccountType;
import DepositEntity.TransactionRecord;
import DepositEntity.TransferRecord;
import Exception.UserHasNoSavingAccountException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public List<SavingAccount> getSavingAccount(Long customerID) throws UserHasNoSavingAccountException {
        List<SavingAccount> activeSavingAccounts = new ArrayList();

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = q.getResultList();
        Customer customer = customers.get(0);
        if (customer.getSavingAccounts().isEmpty()) {
            System.out.print("Customer has no saving account");
            throw new UserHasNoSavingAccountException("User has no saving account!");
        } else {
            for (int i = 0; i < customer.getSavingAccounts().size(); i++) {
                if (customer.getSavingAccounts().get(i).getStatus().equals("active")) {
                    activeSavingAccounts.add(customer.getSavingAccounts().get(i));
                }
            }
            return activeSavingAccounts;
        }
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

    @Override
    public List<Long> getSavingAccountNumbers(Long customerID) throws UserHasNoSavingAccountException {
        List<Long> savingAccountNumbers = new ArrayList();
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.id = :customerID");
        q.setParameter("customerID", customerID);
        List<Customer> customers = q.getResultList();
        Customer customer = customers.get(0);
        if (customer.getSavingAccounts().isEmpty()) {
            throw new UserHasNoSavingAccountException("User has no saving account!");
        } else {
            for (int i = 0; i < customer.getSavingAccounts().size(); i++) {
                if (customer.getSavingAccounts().get(i).getStatus().equals("active")) {
                    savingAccountNumbers.add(customer.getSavingAccounts().get(i).getAccountNumber());
                }
            }
            return savingAccountNumbers;
        }
    }

    @Override
    public List<List> getTransactionRecord(Long savingAccountNumber) {
        System.out.print("inside the getTransactionRecord SessionBean");
        
        List<List> displayList = new ArrayList();

        Query m = em.createQuery("SELECT a FROM TransactionRecord a WHERE a.giverAccountNum = :giverAccountNum");
        m.setParameter("giverAccountNum", savingAccountNumber);
        List<TransactionRecord> record1 = m.getResultList();
        System.out.print("record1 size"+record1.size());
        List<List>temp=new ArrayList();
        temp=addTransferList(record1, "debit");
        displayList.addAll(temp);
        
        System.out.print("in the middle of transaction session Bean"+ displayList.size());

        Query n = em.createQuery("SELECT a FROM TransactionRecord a WHERE a.recipientAccountNum = :recipientAccountNum");
        n.setParameter("recipientAccountNum", savingAccountNumber);
        List<TransactionRecord> record2 = n.getResultList();
        List<List>temp2=new ArrayList();
        temp2=addTransferList(record2, "credit");
        displayList.addAll(temp2);

        return displayList;

    }

  
    private List<List> addTransferList(List<TransactionRecord> record, String type) {
        
        List<List> list = new ArrayList();
        int count = 0;
        if (type.equals("credit")) {
            for (int i = 0; i < record.size(); i++) {
                if (record.get(i).getStatus().equals("settled")) {
                    list.add(count,new ArrayList<>());
                    list.get(count).add(0, record.get(i).getTransactionTime());
                    list.get(count).add(1, "TF");
                    list.get(count).add(2, record.get(i).getDescription());
                    list.get(count).add(3, null);
                    list.get(count).add(4, record.get(i).getAmount());
                    count = count + 1;
                }

            }

        } else if (type.equals("debit")) {
            System.out.print("Inside the addTransferList method");
            for (int i = 0; i < record.size(); i++) {
                if (record.get(i).getStatus().equals("settled")) {
                    list.add(count,new ArrayList<>());
                    list.get(count).add(0, record.get(i).getTransactionTime());
                    list.get(count).add(1, "TF");
                    list.get(count).add(2, record.get(i).getDescription());
                    list.get(count).add(3, record.get(i).getAmount());
                    list.get(count).add(4, null);
                    count = count + 1;
                }

            }

        }
        System.out.print("Inside the addTransferList method "+ list.size());
        return list;
    }
}
