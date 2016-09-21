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
import Exception.UserHasNoInactiveSavingAccountException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserHasPendingTransactionException;
import Exception.UserNotEnoughBalanceException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Bella
 */
@Named(value = "savingAccountManagedBean")
@SessionScoped
public class SavingAccountManagedBean implements Serializable {

    @EJB
    SavingAccountSessionBeanLocal sasb;
    @EJB
    AccountManagementSessionBeanLocal amsb;
    private Long customerID = Long.parseLong("2");
    private List<SavingAccount> savingAccounts;
    private String savingAccountName;
    private List<String> savingAccountTypeList;
    private boolean createSavingAccountStatus;
    private List<Long> savingAccountNumberList;
    private List<Long> inactiveSavingAccountNumberList;
    private Long savingAccountSelected;
    private Long inactiveSavingAccountSelected;
    private List<List> transactionRecordList;
    private List<SavingAccount> savingAccountForCloseAccount;

    @PostConstruct
    public void init() {
        try {
            this.getSavingAccountType();
            savingAccounts = sasb.getSavingAccount(customerID);
            this.getSavingAccountNumbers();
            this.getInactiveSavingAccountNumbers();
        } catch (Exception e) {
            System.out.print("Init encounter error");
        }
    }

    public SavingAccountManagedBean() {
    }

    public void createSavingAccountExistingCustomer(ActionEvent event) throws UserAlreadyHasSavingAccountException, EmailNotSendException, IOException, UserHasNoSavingAccountException {
        try {
            System.out.print(customerID);
            System.out.print(savingAccountName);
            amsb.createSavingAccountExistingCustomer(customerID, savingAccountName);
            //get data from database and assign the new value to the variable after createSavingAccount success!
            getSavingAccountNumbers();
            savingAccounts = sasb.getSavingAccount(customerID);

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/createSavingAccountECsuccess.xhtml");

        } catch (UserAlreadyHasSavingAccountException ex) {
            System.out.println(ex.getMessage());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void getSavingAccountNumbers() throws UserHasNoSavingAccountException {
        try {
            System.out.print("inside the getSavingAccountNumbers()");
            savingAccountNumberList = sasb.getSavingAccountNumbers(customerID);
        } catch (UserHasNoSavingAccountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void getInactiveSavingAccountNumbers() throws UserHasNoInactiveSavingAccountException {
        try {
            inactiveSavingAccountNumberList = sasb.getInactiveSavingAccountNumbers(customerID);
        } catch (UserHasNoInactiveSavingAccountException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void getSavingAccountType() {
        if (sasb.getSavingAccountType().get(0).equals("false")) {
            System.out.print("get saving account type from database got problem!");
        } else {
            savingAccountTypeList = sasb.getSavingAccountType();
        }
    }

    public void getTransactionRecords() {
        System.out.print("inside the transaction Managedbean");
        transactionRecordList = sasb.getTransactionRecord(savingAccountSelected);
        //System.out.print(transactionRecordList);
    }

    public void goToViewTransactionRecord(ActionEvent event) {
        try {
            if (savingAccountSelected != null) {
                this.getTransactionRecords();
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/DepositManagement/viewTransactionRecords.xhtml");
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Saving Account!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (Exception e) {
            System.out.print("Redirect to transferByPayee page fails");
        }
    }

    public void goToCloseSavingAccount(ActionEvent event) {
        try {
            if (savingAccountSelected != null) {
                savingAccountForCloseAccount = sasb.getSavingAccountForCloseAccount(savingAccountSelected);
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/DepositManagement/closeSavingAccount.xhtml");
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Saving Account!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (Exception e) {
            System.out.print("go to close saving account encounter error");
        }
    }

    public void goToActivateSavingAccount(ActionEvent event) throws UserNotEnoughBalanceException, UserHasNoSavingAccountException, IOException {
        try {
            if (inactiveSavingAccountSelected != null) {
                sasb.checkInactiveSavingAccount(inactiveSavingAccountSelected);
                this.getSavingAccountNumbers();
                savingAccounts = sasb.getSavingAccount(customerID);
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/DepositManagement/activateSavingAccountSuccess.xhtml");
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Saving Account to activate!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (UserNotEnoughBalanceException ex) {
            System.out.print("User Has Not Enough Balance!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void goBackToHomePage(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/dashboard.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to Home Page Encounter Error!");
        }
    }

    public void checkPendingTransaction(ActionEvent event) throws UserHasPendingTransactionException, IOException, UserHasNoSavingAccountException {
        try {
            sasb.checkPendingTransaction(savingAccountSelected);
            this.getSavingAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/closeSavingAccountSuccess.xhtml");
        } catch (UserHasPendingTransactionException ex) {
            System.out.print("User Has Pending Transaction!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
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

    public List getSavingAccountForCloseAccount() {
        return savingAccountForCloseAccount;
    }

    public void setSavingAccountForCloseAccount(List savingAccountForCloseAccount) {
        this.savingAccountForCloseAccount = savingAccountForCloseAccount;
    }

    public List<Long> getInactiveSavingAccountNumberList() {
        return inactiveSavingAccountNumberList;
    }

    public void setInactiveSavingAccountNumberList(List<Long> inactiveSavingAccountNumberList) {
        this.inactiveSavingAccountNumberList = inactiveSavingAccountNumberList;
    }

    public Long getInactiveSavingAccountSelected() {
        return inactiveSavingAccountSelected;
    }

    public void setInactiveSavingAccountSelected(Long inactiveSavingAccountSelected) {
        this.inactiveSavingAccountSelected = inactiveSavingAccountSelected;
    }

}
