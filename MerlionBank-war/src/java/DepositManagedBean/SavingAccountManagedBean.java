/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositManagedBean;

import DepositEntity.Session.SavingAccountSessionBeanLocal;
import DepositEntity.SavingAccount;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Bella
 */
@Named(value = "savingAccountManagedBean")
@SessionScoped
public class SavingAccountManagedBean implements Serializable{

    @EJB
    SavingAccountSessionBeanLocal sasb;
    private Long customerID=Long.parseLong("2");
    private List<SavingAccount> savingAccounts;

    
    
    @PostConstruct
    public void init() {
        try{
            savingAccounts = sasb.getSavingAccount(customerID);
        }catch(Exception e){
            System.out.print("Init encounter error");
        }
    }
    
    public SavingAccountManagedBean() {
    }
    
    public List<SavingAccount> getSavingAccounts() {
        return savingAccounts;
    }

    public void setSavingAccounts(List<SavingAccount> savingAccounts) {
        this.savingAccounts = savingAccounts;
    }
    
}
