/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositManagedBean;

import CommonEntity.Session.AccountManagementSessionBeanLocal;
import CommonManagedBean.LogInManagedBean;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
import Exception.EmailNotSendException;
import Exception.UserAlreadyHasSavingAccountException;
import Exception.UserCloseAccountException;
import Exception.UserHasNoInactiveSavingAccountException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserHasPendingTransactionException;
import Exception.UserNotEnoughBalanceException;
import java.io.IOException;
import java.io.Serializable;
import java.math.RoundingMode;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
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
    @Inject
    LogInManagedBean logInManagedBean;
    @Inject
    TransferManagedBean transferManagedBean;
    private Long customerID;
    private List<SavingAccount> savingAccounts;
    private String savingAccountName;
    private List<String> savingAccountTypeList;
    private boolean createSavingAccountStatus;
    private List<String> savingAccountNumberList;
    private List<Long> inactiveSavingAccountNumberList;
    private String savingAccountSelected;
    private Long savingAccountSelectedLong;
    private Long inactiveSavingAccountSelected;
    private List<TransactionRecord> transactionRecordList;
    private List<SavingAccount> savingAccountForCloseAccount;

    @PostConstruct
    public void init() {
        try {
            this.setCustomerID(logInManagedBean.getCustomerId());
            this.getSavingAccountType();
            savingAccounts = sasb.getSavingAccount(customerID);
            System.out.print(savingAccounts.get(0).getAvailableBalance().setScale(2, RoundingMode.HALF_UP));

            this.getSavingAccountNumbers();
            this.getInactiveSavingAccountNumbers();
        } catch (Exception e) {
            System.out.print("Init encounter error");
        }
    }

    public SavingAccountManagedBean() {
    }

    public void dashboardCloseAccount(ActionEvent event) {
        try {
            this.getSavingAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/closeSavingAccount_selectAccounts.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard Close Account Encounter Error!");
        }
    }

    public void dashboardTransactionRecord(ActionEvent event) {
        try {
            this.getSavingAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/viewTransactionRecords_selectAccounts.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard View Transaction History Encounter Error!");
        }
    }

    public void dashboardDisplaySavingAccount(ActionEvent event) {
        try {
            savingAccounts = sasb.getSavingAccount(customerID);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/displaySavingAccountDetail.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard View Saving Account Encounter Error!");
        }
    }

    public void dashboardActivateAccount(ActionEvent event) {
        try {
            this.getInactiveSavingAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/activateSavingAccount_selectAccounts.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard Activate Saving Account Encounter Error!");
        }
    }

    public void dashboardCreateAccount(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/createSavingAccountEC.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard Create Saving Account Encounter Error!");
        }
    }

    public void createSavingAccountExistingCustomer(ActionEvent event) throws UserAlreadyHasSavingAccountException, EmailNotSendException, IOException, UserHasNoSavingAccountException {
        try {
            if (savingAccountName != null) {
                System.out.print(customerID);
                System.out.print(savingAccountName);
                amsb.createSavingAccountExistingCustomer(customerID, savingAccountName);
                //get data from database and assign the new value to the variable after createSavingAccount success!
                getSavingAccountNumbers();
                savingAccounts = sasb.getSavingAccount(customerID);

                //Log the customer action into database
                String description = "Create " + savingAccountName + " saving account ";
                sasb.logAction(description, customerID);

                //Redirect to successful page
                if (savingAccountName.equals("MerLion Youth Saving Account")) {
                    FacesContext.getCurrentInstance().getExternalContext()
                            .redirect("/MerlionBank-war/DepositManagement/createSavingAccountECsuccessY.xhtml");
                } else {
                    FacesContext.getCurrentInstance().getExternalContext()
                            .redirect("/MerlionBank-war/DepositManagement/createSavingAccountECsuccess.xhtml");
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select a saving account type!");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }

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
        savingAccountSelectedLong = Long.parseLong(savingAccountSelected.split(",")[0]);
        transactionRecordList = sasb.getTransactionRecord(savingAccountSelectedLong);
        //System.out.print(transactionRecordList);
    }

    public void goToViewTransactionRecord(ActionEvent event) {
        try {
            if (savingAccountSelected != null) {
                this.getTransactionRecords();

                //Log the customer action into database
                String description = "View Transaction Records";
                sasb.logAction(description, customerID);

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
                savingAccountSelectedLong = Long.parseLong(savingAccountSelected.split(",")[0]);
                savingAccountForCloseAccount = sasb.getSavingAccountForCloseAccount(savingAccountSelectedLong);
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

                String description = "Activate " + inactiveSavingAccountSelected + " saving account ";
                sasb.logAction(description, customerID);

                transferManagedBean.init();

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

    public void checkPendingTransaction(ActionEvent event) throws UserHasPendingTransactionException, IOException, UserHasNoSavingAccountException, UserCloseAccountException {
        try {
            savingAccountSelectedLong = Long.parseLong(savingAccountSelected.split(",")[0]);
            sasb.checkPendingTransaction(savingAccountSelectedLong);
            this.getSavingAccountNumbers();

            String description = "Close " + savingAccountSelectedLong + " saving account ";
            sasb.logAction(description, customerID);

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/closeSavingAccountSuccess.xhtml");
        } catch (UserHasPendingTransactionException ex) {
            System.out.print("User Has Pending Transaction!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (UserCloseAccountException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", e.getMessage());
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

    public List<String> getSavingAccountNumberList() {
        return savingAccountNumberList;
    }

    public void setSavingAccountNumberList(List<String> savingAccountNumList) {
        this.savingAccountNumberList = savingAccountNumList;
    }

    public String getSavingAccountSelected() {
        return savingAccountSelected;
    }

    public void setSavingAccountSelected(String savingAccountSelected) {
        this.savingAccountSelected = savingAccountSelected;
    }

    public List<TransactionRecord> getTransactionRecordList() {
        return transactionRecordList;
    }

    public void setTransactionRecordList(List<TransactionRecord> transactionRecordList) {
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

    public Long getSavingAccountSelectedLong() {
        return savingAccountSelectedLong;
    }

    public void setSavingAccountSelectedLong(Long savingAccountSelectedLong) {
        this.savingAccountSelectedLong = savingAccountSelectedLong;
    }
    
    

}
