/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositManagedBean;

import CommonEntity.Session.AccountManagementSessionBeanLocal;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import DepositEntity.SavingAccount;
import Exception.EmailNotSendException;
import Exception.UserAlreadyHasSavingAccountException;
import Exception.UserHasNoSavingAccountException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
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
    @EJB
    AccountManagementSessionBeanLocal amsb;
    private Long customerID=Long.parseLong("2");
    private List<SavingAccount> savingAccounts;
    private String savingAccountName;
    private List<String> savingAccountTypeList;
    private boolean createSavingAccountStatus;
    private List<Long> savingAccountNumberList;
    private Long savingAccountSelected;
    private List<List> transactionRecordList;

    
    
    @PostConstruct
    public void init() {
        try{
            getSavingAccountType();
            savingAccounts = sasb.getSavingAccount(customerID);
            getSavingAccountNumbers();
        }catch(Exception e){
            System.out.print("Init encounter error");
        }
    }
    
    public SavingAccountManagedBean() {
    }
    
    public void createSavingAccountExistingCustomer(ActionEvent event) throws UserAlreadyHasSavingAccountException, EmailNotSendException, IOException, UserHasNoSavingAccountException{
        try{
            System.out.print(customerID);
            System.out.print(savingAccountName);
            amsb.createSavingAccountExistingCustomer(customerID, savingAccountName);
            //get data from database and assign the new value to the variable after createSavingAccount success!
            getSavingAccountNumbers();
            savingAccounts = sasb.getSavingAccount(customerID);
                FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/createSavingAccountECsuccess.xhtml");
         
        }catch(UserAlreadyHasSavingAccountException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public void getSavingAccountNumbers() throws UserHasNoSavingAccountException{
        System.out.print("inside the getSavingAccountNumbers()");
        savingAccountNumberList = sasb.getSavingAccountNumbers(customerID);
    }
    
    public void getSavingAccountType(){
        if(sasb.getSavingAccountType().get(0).equals("false")){
            System.out.print("get saving account type from database got problem!");
        }else{
            savingAccountTypeList = sasb.getSavingAccountType();  
        }
    }
    
    public void getTransactionRecords() {
        System.out.print("inside the transaction Managedbean");
        transactionRecordList = sasb.getTransactionRecord(savingAccountSelected);
        //System.out.print(transactionRecordList);
    }
    
    public void goToViewTransactionRecord(ActionEvent event){
         try {
            this.getTransactionRecords();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/viewTransactionRecords.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to transferByPayee page fails");
        }
    }
    
    public List<SavingAccount> getSavingAccounts() {
        return savingAccounts;
    }

    public void setSavingAccounts(List<SavingAccount> savingAccounts) {
        this.savingAccounts = savingAccounts;
    }
    
    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }
    
    public String getSavingAccountName() {
        return savingAccountName;
    }

    public void setSavingAccountName(String savingAccountName) {
        this.savingAccountName = savingAccountName;
    }
    
    public List<String> getSavingAccountTypeList() {
        return savingAccountTypeList;
    }

    public void setSavingAccountTypeList(List<String> savingAccountTypeList) {
        this.savingAccountTypeList = savingAccountTypeList;
    }
    
    public boolean isCreateSavingAccountStatus() {
        return createSavingAccountStatus;
    }

    public void setCreateSavingAccountStatus(boolean createSavingAccountStatus) {
        this.createSavingAccountStatus = createSavingAccountStatus;
    }
    
    public List<Long> getSavingAccountNumberList() {
        return savingAccountNumberList;
    }

    public void setSavingAccountNumberList(List<Long> savingAccountNumList) {
        this.savingAccountNumberList = savingAccountNumList;
    }

    public Long getSavingAccountSelected() {
        return savingAccountSelected;
    }

    public void setSavingAccountSelected(Long savingAccountSelected) {
        this.savingAccountSelected = savingAccountSelected;
    }

    public List<List> getTransactionRecordList() {
        return transactionRecordList;
    }

    public void setTransactionRecordList(List<List> transactionRecordList) {
        this.transactionRecordList = transactionRecordList;
    }

    

    
}
