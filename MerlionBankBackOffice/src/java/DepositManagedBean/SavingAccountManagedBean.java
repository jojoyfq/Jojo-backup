/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositManagedBean;

import CommonEntity.Session.AccountManagementSessionBeanLocal;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import DepositEntity.SavingAccount;
import DepositEntity.SavingAccountType;
import Exception.EmailNotSendException;
import Exception.UserAlreadyHasSavingAccountException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserHasPendingTransactionException;
import Exception.UserNotEnoughBalanceException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javafx.scene.control.TableColumn.CellEditEvent;
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
    private List<Long> notTerminAccountNumList;
    private Long savingAccountSelected;
    private Long notTerminAccountSelected;
    private List<List> transactionRecordList;
    private List<SavingAccount> savingAccountForCloseAccount;
    private List<SavingAccountType> savingAccountTypes;
    private BigDecimal withdrawAmount;
    private String withdrawAmountString;
    private BigDecimal depositAmount;
    private String depositAmountString;

    @PostConstruct
    public void init() {
        try {
            this.getSavingAccountType();
            savingAccounts = sasb.getSavingAccount(customerID);
            this.getSavingAccountNumbers();
            this.getNotTerminatedAccountNumbers();
            savingAccountTypes = sasb.getSavingAccountTypeList();
            System.out.print(savingAccountTypes);
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
            this.getSavingAccountNumbers();
            this.getNotTerminatedAccountNumbers();
            savingAccounts = sasb.getSavingAccount(customerID);

            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Account created Successfully");
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

        } catch (UserAlreadyHasSavingAccountException ex) {
            System.out.println(ex.getMessage());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void getSavingAccountNumbers() throws UserHasNoSavingAccountException {
        System.out.print("inside the getSavingAccountNumbers()");
        savingAccountNumberList = sasb.getSavingAccountNumbers(customerID);
    }
    
    public void getNotTerminatedAccountNumbers() throws UserHasNoSavingAccountException{
        try{
            notTerminAccountNumList = sasb.getNotTerminatedAccountNumbers(customerID);
        }catch(UserHasNoSavingAccountException ex){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
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

    public void cashWithdraw(ActionEvent event) throws UserNotEnoughBalanceException {
        try {
            if (savingAccountSelected != null) {
                withdrawAmount = new BigDecimal(withdrawAmountString);
                sasb.cashWithdraw(savingAccountSelected, withdrawAmount);
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Cash Withdraw Success!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }else{
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Saving Account!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (UserNotEnoughBalanceException ex) {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }
    
    public void cashDeposit(ActionEvent event) {
        try {
            if (notTerminAccountSelected != null) {
                depositAmount = new BigDecimal(depositAmountString);
                sasb.cashDeposit(notTerminAccountSelected, depositAmount);
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Cash Deposit Success!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }else{
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Saving Account!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (Exception ex) {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }
    

    public void goToViewTransactionRecord(ActionEvent event) {
        try {
            if (savingAccountSelected != null) {
                System.out.print("inside the goToTransaction Record method!");
                this.getTransactionRecords();
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBankBackOffice/DepositManagement/viewTransactionRecords.xhtml");
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
                        .redirect("/MerlionBankBackOffice/DepositManagement/closeSavingAccount.xhtml");
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Saving Account!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (Exception e) {
            System.out.print("go to close saving account encounter error");
        }
    }

    public void checkPendingTransaction(ActionEvent event) throws UserHasPendingTransactionException, IOException, UserHasNoSavingAccountException {
        try {
            sasb.checkPendingTransaction(savingAccountSelected);
            this.getSavingAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/closeSavingAccountSuccess.xhtml");
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

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getWithdrawAmountString() {
        return withdrawAmountString;
    }

    public void setWithdrawAmountString(String withdrawAmountString) {
        this.withdrawAmountString = withdrawAmountString;
    }
    
    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getDepositAmountString() {
        return depositAmountString;
    }

    public void setDepositAmountString(String depositAmountString) {
        this.depositAmountString = depositAmountString;
    }
    
     public List<Long> getNotTerminAccountNumList() {
        return notTerminAccountNumList;
    }

    public void setNotTerminAccountNumList(List<Long> notTerminAccountNumList) {
        this.notTerminAccountNumList = notTerminAccountNumList;
    }
    
    public Long getNotTerminAccountSelected() {
        return notTerminAccountSelected;
    }

    public void setNotTerminAccountSelected(Long notTerminAccountSelected) {
        this.notTerminAccountSelected = notTerminAccountSelected;
    }
    
     public List<SavingAccountType> getSavingAccountTypes() {
        return savingAccountTypes;
    }

    public void setSavingAccountTypes(List<SavingAccountType> savingAccountTypes) {
        this.savingAccountTypes = savingAccountTypes;
    }

}
