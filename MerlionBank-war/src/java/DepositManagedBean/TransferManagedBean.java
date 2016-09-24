/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositManagedBean;

import CommonManagedBean.LogInManagedBean;
import DepositEntity.Session.TransferSessionBeanLocal;
import Exception.PayeeNotFoundException;
import Exception.TransferException;
import Exception.UserHasNoSavingAccountException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author liyanmeng
 */
@Named(value = "transferManagedBean")
@SessionScoped
public class TransferManagedBean implements Serializable {

    @EJB
    TransferSessionBeanLocal tfsb;
    @Inject
    LogInManagedBean logInManagedBean;
    private List savingAccountList;
    private String recipientName;
    private String amountString;
    private BigDecimal amountBD;
    private String transferLimitString;
    private BigDecimal transferLimitBD;
    private Long recipientAccountNumLong;
    private String recipientAccountNumString;
    private Long giverAccountNumLong;
    private Long payeeAccount;
    private String payeeAccountString;
    private String payeeName;
    private Long customerID;
    private List payeeList;
    private Long payeeTransferAccount;
    private Long payeeDelete;
    private BigDecimal transferLimitDatabase;

   

    

    @PostConstruct
    public void init() {
        try {
            this.setCustomerID(logInManagedBean.getCustomerId());
            this.getSavingAccountNumbers();
            this.getPayeeListfromDatabase();
        } catch (Exception e) {
            System.out.print("Get PayeeList encounter error");
        }
    }

    public TransferManagedBean() {
    }

    public void dashboardIntraTransfer(ActionEvent event) {
        try {
            this.init();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/TransferManagement/selectSavingAccount.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard Transfer Encounter Error!");
        }
    }
    
    public void dashboardChangeTransferLimit(ActionEvent event) {
        try {
            this.getTransferLimit();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/TransferManagement/changeTransferLimit.xhtml");
        } catch (Exception e) {
            System.out.print("Dashboard Change Transfer Limit Encounter Error!");
        }
    }


    public void goToOneTimeTransferPage(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/TransferManagement/transferOneTime.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to OneTimeTransfer page fails");
        }
    }

    public void goToTransferByPayee(ActionEvent event) {
        try {
            if(payeeTransferAccount != null) {
                payeeName = tfsb.searchPayeeName(payeeTransferAccount);
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/TransferManagement/transferByPayee.xhtml");
            }else{
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Payee from the Payee List!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);    
                 }
        } catch (Exception e) {
            System.out.print("Redirect to transfer By Payee page fails");
        }
    }


    public void changeTransferLimit(ActionEvent event) {
        try {
            this.init();
            if (transferLimitString != null) {
                transferLimitBD = new BigDecimal(transferLimitString);
                tfsb.changeTransferLimit(customerID, transferLimitBD);
                
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/TransferManagement/changeTransferLimitSuccess.xhtml");
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Enter your transfer limit amount!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (Exception e) {
            System.out.print("Change Transfer Limit encounter Error!");
        }
    }

    
    public void goToDeletePayeePage(ActionEvent event) {
        try{
            this.getPayeeListfromDatabase();
             FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/TransferManagement/deletePayee.xhtml");
        }catch(Exception e){
            System.out.print("Redirect to delete Payee page fails");
        }
    }

    public void transferByPayee(ActionEvent event) throws TransferException, IOException {
        try {
            if (payeeTransferAccount != null) {
                amountBD = new BigDecimal(amountString);
                tfsb.intraOneTimeTransferCheck(customerID,giverAccountNumLong, payeeTransferAccount, amountBD);

                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/TransferManagement/transferSuccess.xhtml");
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Payee from the Payee List!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (TransferException ex) {
            System.out.print("Transfer By Payee Encounter Error");
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void oneTimeTransfer(ActionEvent event) throws TransferException, IOException {
        try {
            amountBD = new BigDecimal(amountString);
            recipientAccountNumLong = Long.parseLong(recipientAccountNumString);
            System.out.print(amountBD);
            System.out.print(recipientAccountNumLong);
            tfsb.intraOneTimeTransferCheck(customerID, giverAccountNumLong, recipientAccountNumLong, amountBD);

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/TransferManagement/transferSuccess.xhtml");

        } catch (TransferException ex) {
            System.out.print("OneTimeTransfer Encounter Error");
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void addPayee(ActionEvent event) throws PayeeNotFoundException, IOException {
        try {
            payeeAccount = Long.parseLong(payeeAccountString);
            tfsb.addPayee(payeeAccount, payeeName, customerID);
            this.getPayeeListfromDatabase();

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/TransferManagement/addNewPayeeSuccess.xhtml");

        } catch (PayeeNotFoundException ex) {
            System.out.print("Add Payee Encounter Error");
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    
    public void deletePayee(ActionEvent event) throws IOException {
        if(payeeDelete != null){
            tfsb.deletePayee(customerID, payeeDelete);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/TransferManagement/deletePayeeSuccess.xhtml");
        }else{
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Payee to Delete!");
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }
    
    public void getTransferLimit(){
        try{
            transferLimitDatabase = tfsb.getTransferLimit(customerID);
        }catch(Exception e){
            System.out.print("Get Intra-Transfer Limit encounter Error!");
        }
    }

    public List getPayeeListfromDatabase() {
        payeeList = tfsb.getPayeeList(customerID);
        System.out.print(payeeList);
        return payeeList;
    }

    public void getSavingAccountNumbers() throws IOException, UserHasNoSavingAccountException {
        try {
            savingAccountList = tfsb.getSavingAccountNumbers(customerID);
        } catch (UserHasNoSavingAccountException ex) {
            System.out.print("User Has No Saving Account");
        }
    }

    public void goToIntraTransferPage(ActionEvent event) {
        try {
            if (giverAccountNumLong != null) {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/TransferManagement/intraTransfer.xhtml");
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have no saving account!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }
        } catch (Exception e) {
            System.out.print("Redirect to intraTransfer Page fails");
        }
    }

    public void goToTransferByPayeeListPage(ActionEvent event) {
        try {
            this.getPayeeListfromDatabase();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/TransferManagement/transferByPayeeList.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to TransferByPayeeList page fails");
        }
    }

    public void goToAddNewPayeePage(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/TransferManagement/addNewPayee.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to AddNewPayee page fails");
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

    public String getRecipientAccountNumString() {
        return recipientAccountNumString;
    }

    public void setRecipientAccountNumString(String recipientAccountNumString) {
        this.recipientAccountNumString = recipientAccountNumString;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public BigDecimal getAmountBD() {
        return amountBD;
    }

    public void setAmountBD(BigDecimal amountBD) {
        this.amountBD = amountBD;
    }

    public Long getRecipientAccountNumLong() {
        return recipientAccountNumLong;
    }

    public void setRecipientAccountNumLong(Long recipientAccountNumLong) {
        this.recipientAccountNumLong = recipientAccountNumLong;
    }

    public Long getGiverAccountNumLong() {
        return giverAccountNumLong;
    }

    public void setGiverAccountNumLong(Long giverAccountNumLong) {
        this.giverAccountNumLong = giverAccountNumLong;
    }

    public Long getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(Long payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeAccountString() {
        return payeeAccountString;
    }

    public void setPayeeAccountString(String payeeAccountString) {
        this.payeeAccountString = payeeAccountString;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public List getPayeeList() {
        return payeeList;
    }

    public void setPayeeList(List payeeList) {
        this.payeeList = payeeList;
    }

    public Long getPayeeTransferAccount() {
        return payeeTransferAccount;
    }

    public void setPayeeTransferAccount(Long payeeTransferAccount) {
        this.payeeTransferAccount = payeeTransferAccount;
    }

    public List getSavingAccountList() {
        return savingAccountList;
    }

    public void setSavingAccountList(List savingAccountList) {
        this.savingAccountList = savingAccountList;
    }

    public String getTransferLimitString() {
        return transferLimitString;
    }

    public void setTransferLimitString(String transferLimitString) {
        this.transferLimitString = transferLimitString;
    }

    public BigDecimal getTransferLimitBD() {
        return transferLimitBD;
    }

    public void setTransferLimitBD(BigDecimal transferLimitBD) {
        this.transferLimitBD = transferLimitBD;
    }

    public Long getPayeeDelete() {
        return payeeDelete;
    }

    public void setPayeeDelete(Long payeeDelete) {
        this.payeeDelete = payeeDelete;
    }
    
     public BigDecimal getTransferLimitDatabase() {
        return transferLimitDatabase;
    }

    public void setTransferLimitDatabase(BigDecimal transferLimitDatabase) {
        this.transferLimitDatabase = transferLimitDatabase;
    }
}
