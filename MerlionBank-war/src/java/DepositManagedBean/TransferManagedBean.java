/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositManagedBean;

import DepositEntity.Session.TransferSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

/**
 *
 * @author liyanmeng
 */
@Named(value = "transferManagedBean")
@SessionScoped
public class TransferManagedBean implements Serializable{

    @EJB
    TransferSessionBeanLocal tfsb;
    private String recipientName;
    private String amountString;
    private BigDecimal amountBD;
    private Long recipientAccountNumLong;
    private String recipientAccountNumString;
    private Long giverAccountNumLong;
    private String giverAccountNumString;
    private boolean checkStatus;
    private Long payeeAccount;
    private String payeeAccountString;
    private String payeeName;
    private Long customerID;

   
    

    public TransferManagedBean() {
    }
    
    
    public void goToOneTimeTransferPage(ActionEvent event){
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/transferOneTime.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to OneTimeTransfer page fails");
        }
    }
    
    public void oneTimeTransfer(ActionEvent event) throws IOException {
        try{
            amountBD = new BigDecimal(amountString);
            recipientAccountNumLong = Long.parseLong(recipientAccountNumString);  
            setGiverAccountNumString("892951459");
            setGiverAccountNumLong(Long.parseLong(giverAccountNumString));
            System.out.print(amountBD);
            System.out.print(recipientAccountNumLong);
            System.out.print(giverAccountNumString);
            setCheckStatus(tfsb.intraOneTimeTransferCheck(giverAccountNumLong,recipientAccountNumLong,amountBD));

            if(checkStatus){ //if return true, go to success page
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/DepositManagement/transferOneTimeSuccess.xhtml");
            }else{ //if return false, stay at the same page, display error message
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/DepositManagement/transferOneTime.xhtml");
            }
            
        } catch (Exception e){
            System.out.print("OneTimeTransfer Encounter Error");
        }
    }
    
    public void addPayee(ActionEvent event)throws IOException{
        boolean checkAddPayeeStatus;
        customerID = Long.parseLong("2");
        try{
            payeeAccount = Long.parseLong(payeeAccountString);
            checkAddPayeeStatus = tfsb.addPayee(payeeAccount, payeeName,customerID);
            if(checkAddPayeeStatus) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/DepositManagement/addNewPayeeSuccess.xhtml");
            }else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/DepositManagement/addNewPayee.xhtml");
            }
            
        } catch(Exception e){
            System.out.print("Add Payee Encounter Error");
        }
    }
    
    public void goToTransferByPayeeListPage(ActionEvent event){
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/transferByPayeeList.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to TransferByPayeeList page fails");
        }       
    }
    
    public void goToAddNewPayeePage(ActionEvent event){
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/DepositManagement/addNewPayee.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to AddNewPayee page fails");
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

    public String getGiverAccountNumString() {
        return giverAccountNumString;
    }

    public void setGiverAccountNumString(String giverAccountNumString) {
        this.giverAccountNumString = giverAccountNumString;
    }
    
    public boolean isCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
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
}
