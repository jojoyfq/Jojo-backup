/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import CommonEntity.Staff;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import DepositEntity.SavingAccount;
import DepositEntity.SavingAccountType;
import Exception.EmailNotSendException;
import Exception.UserAlreadyHasSavingAccountException;
import Exception.UserCloseAccountException;
import Exception.UserExistException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserHasPendingTransactionException;
import Exception.UserNotEnoughBalanceException;
import StaffManagement.staffLogInManagedBean;
import TellerManagedBean.ServiceCustomerManagedBean;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
    private ServiceCustomerManagedBean serviceCustomerManagedBean;
    @Inject
    private staffLogInManagedBean staffLogInManagedBean;

    private Long customerID;
    private List<SavingAccount> savingAccounts;
    private String changeIRsavingAccountType;
    private String savingAccountName;
    private List<String> savingAccountTypeList;
    private boolean createSavingAccountStatus;
    private List<Long> savingAccountNumberList;
    private List<Long> notTerminAccountNumList;
    private Long savingAccountSelected;
    private Long notTerminAccountSelected;
    private List<List> transactionRecordList;
    private List<SavingAccount> savingAccountForCloseAccount;
    private BigDecimal withdrawAmount;
    private String withdrawAmountString;
    private BigDecimal depositAmount;
    private String depositAmountString;
    private Double interestRate1;
    private Double interestRate2;
    private Double interestRate3;
    private Staff staff;

   
    @PostConstruct
    public void init() {
        try {
            System.out.print("inside the init() method");
            staff = staffLogInManagedBean.getStaff(); //Get staff who is login
            setCustomerID(serviceCustomerManagedBean.getCustomer().getId()); // Get the customerID 
            System.out.print("customer is" + customerID);
            this.getSavingAccountType();
            savingAccounts = sasb.getSavingAccount(customerID);
            this.getSavingAccountNumbers();
            this.getNotTerminatedAccountNumbers();

        } catch (Exception e) {
            System.out.print("Init encounter error");
        }
    }

    public SavingAccountManagedBean() {
    }

    public void dashboardToCreateSavingAccountEC(ActionEvent event) {
        try {
            setCustomerID(serviceCustomerManagedBean.getCustomer().getId());
            this.getSavingAccountType();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/createSavingAccountEC.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard to Create Saving Account Existing Customer encounter error!");
        }
    }

    public void dashboardToCashDeposit(ActionEvent event) {
        try {
            setCustomerID(serviceCustomerManagedBean.getCustomer().getId());
            this.getNotTerminatedAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/cashDeposit.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard to Cash Deposit encounter error!");
        }
    }

    public void dashboardToCashWithdraw(ActionEvent event) {
        try {
            setCustomerID(serviceCustomerManagedBean.getCustomer().getId());
            this.getSavingAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/cashWithdraw.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard to Cash Withdraw encounter error!");
        }
    }

    public void dashboardToViewAccounts(ActionEvent event) {
        try {
            setCustomerID(serviceCustomerManagedBean.getCustomer().getId());
            savingAccounts = sasb.getSavingAccount(customerID);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/displaySavingAccountDetail.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard to View Saving Accounts Encounter error!");
        }
    }

    public void dashboardToViewTransactions(ActionEvent event) {
        try {
            setCustomerID(serviceCustomerManagedBean.getCustomer().getId());
            this.getSavingAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/viewTransactionRecords_selectAccounts.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard to View Transaction History Encounter error!");
        }
    }

    public void dashboardToCloseSavingAccount(ActionEvent event) {
        try {
            setCustomerID(serviceCustomerManagedBean.getCustomer().getId());
            this.getSavingAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/closeSavingAccount_selectAccounts.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard to Close Saving Account Encounter error!");
        }
    }

    public void dashboardToChangeInterestRate(ActionEvent event) {
        try {
            this.getSavingAccountType();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/changeInterestRate_selectType.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard to Change Interest Rate Encounter error!");
        }
    }

    //******************************Main Functions*************************************************************
    public void createSavingAccountExistingCustomer(ActionEvent event) throws UserAlreadyHasSavingAccountException, EmailNotSendException, IOException, UserHasNoSavingAccountException {
        try {
            System.out.print(customerID);
            System.out.print(savingAccountName);
            amsb.createSavingAccountExistingCustomer(customerID, savingAccountName);
            //get data from database and assign the new value to the variable after createSavingAccount success!
            this.getSavingAccountNumbers();
            this.getNotTerminatedAccountNumbers();
            savingAccounts = sasb.getSavingAccount(customerID);

            //Log Staff Action into database
//            String description = "Staff " +staff.getStaffIc()+" create "+savingAccountName+" saving account for customer "+customerID;
//            sasb.logStaffAction(description, customerID, staff);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/createSavingAccountECsuccess.xhtml");

        } catch (UserAlreadyHasSavingAccountException ex) {
            System.out.println(ex.getMessage());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void getInterestRate() {
        interestRate1 = sasb.getInterestRate(changeIRsavingAccountType, "InterestRate1");
        interestRate2 = sasb.getInterestRate(changeIRsavingAccountType, "InterestRate2");
        interestRate3 = sasb.getInterestRate(changeIRsavingAccountType, "InterestRate3");
    }

    public void getSavingAccountNumbers() throws UserHasNoSavingAccountException {
        System.out.print("inside the getSavingAccountNumbers()");
        savingAccountNumberList = sasb.getSavingAccountNumbers(customerID);
    }

    public void getNotTerminatedAccountNumbers() throws UserHasNoSavingAccountException {
        try {
            notTerminAccountNumList = sasb.getNotTerminatedAccountNumbers(customerID);
        } catch (UserHasNoSavingAccountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void getSavingAccountType() {
        System.out.print("inside the getSavingAccountType method");
        if (sasb.getSavingAccountType().get(0).equals("false")) {
            System.out.print("get saving account type from database got problem!");
        } else {
            savingAccountTypeList = sasb.getSavingAccountType();
            System.out.print(savingAccountTypeList);
        }
    }

    public void getTransactionRecords() {
        System.out.print("inside the transaction Managedbean");
        transactionRecordList = sasb.getTransactionRecord(savingAccountSelected);
        //System.out.print(transactionRecordList);
    }

    public void cashWithdraw(ActionEvent event) throws UserNotEnoughBalanceException, IOException {
        try {
            if (savingAccountSelected != null) {
                withdrawAmount = new BigDecimal(withdrawAmountString);
                sasb.cashWithdraw(savingAccountSelected, withdrawAmount);

//                String description = "Staff " +staff.getStaffIc()+" perform cash withdraw from "+savingAccountSelected+" saving account for customer "+customerID;
//                sasb.logStaffAction(description, customerID, staff);
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBankBackOffice/DepositManagement/cashWithdrawSuccess.xhtml");
            } else {
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

//                String description = "Staff " + staff.getStaffIc() + " perform cash deposit from " + notTerminAccountSelected + " saving account for customer " + customerID;
//                sasb.logStaffAction(description, customerID, staff);
                
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBankBackOffice/DepositManagement/cashDepositSuccess.xhtml");
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Saving Account!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (Exception ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void changeInterestRate(ActionEvent event) {
        try {
            if (changeIRsavingAccountType != null) {
                this.getInterestRate();
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBankBackOffice/DepositManagement/changeInterestRate.xhtml");
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Saving Account Type!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (Exception e) {
            System.out.print("ChangeInterestRate Encounter some error!");
        }
    }

    public void setInterestRate(ActionEvent event) {
        try {
            sasb.setInterestRate(changeIRsavingAccountType, interestRate1, interestRate2, interestRate3);
            
//          String description = "Staff " + staff.getStaffIc() + " perform change saving account interest rate";
//          sasb.logStaffAction(description, null, staff);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/changeInterestRateSuccess.xhtml");
        } catch (Exception e) {
            System.out.print("set interest rate encounter error!");
        }
    }

    public void goToViewTransactionRecord(ActionEvent event) {
        try {
            if (savingAccountSelected != null) {
                System.out.print("inside the goToTransaction Record method!");
                this.getTransactionRecords();
                
//              String description = "Staff " + staff.getStaffIc() + " perform change saving account interest rate";
//              sasb.logStaffAction(description, null, staff);
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

    public void goBackToHomePage(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/StaffDashboard.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to Home Page Encounter Error!");
        }
    }

    public void checkPendingTransaction(ActionEvent event) throws UserHasPendingTransactionException, IOException, UserHasNoSavingAccountException, UserCloseAccountException {
        try {
            sasb.checkPendingTransaction(savingAccountSelected);
            this.getSavingAccountNumbers();
            
//          String description = "Staff " + staff.getStaffIc() + " perform close saving account "+savingAccountSelected+"for customer"+customerID;
//          sasb.logStaffAction(description, customerID, staff);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/DepositManagement/closeSavingAccountSuccess.xhtml");
        } catch (UserHasPendingTransactionException ex) {
            System.out.print("User Has Pending Transaction!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }catch(UserCloseAccountException e){
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

    public Double getInterestRate1() {
        return interestRate1;
    }

    public void setInterestRate1(Double interestRate1) {
        this.interestRate1 = interestRate1;
    }

    public Double getInterestRate2() {
        return interestRate2;
    }

    public void setInterestRate2(Double interestRate2) {
        this.interestRate2 = interestRate2;
    }

    public Double getInterestRate3() {
        return interestRate3;
    }

    public void setInterestRate3(Double interestRate3) {
        this.interestRate3 = interestRate3;
    }
    
     public String getChangeIRsavingAccountType() {
        return changeIRsavingAccountType;
    }

    public void setChangeIRsavingAccountType(String changeIRsavingAccountType) {
        this.changeIRsavingAccountType = changeIRsavingAccountType;
    }
    
     public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

}
