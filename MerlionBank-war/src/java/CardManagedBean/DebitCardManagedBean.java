/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardManagedBean;

import CardEntity.DebitChargeback;
import CardEntity.DebitCard;
import CardEntity.DebitCardTransaction;
import CardEntity.Session.DebitCardSessionBeanLocal;
import CommonManagedBean.LogInManagedBean;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import DepositEntity.TransactionRecord;
import Exception.ChargebackException;
import Exception.DebitCardException;
import Exception.UserHasDebitCardException;
import Exception.UserHasNoSavingAccountException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
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
@Named(value = "debitCardManagedBean")
@SessionScoped
public class DebitCardManagedBean implements Serializable {

    @EJB
    DebitCardSessionBeanLocal dcsb;
    @EJB
    SavingAccountSessionBeanLocal sasb;
    @Inject
    LogInManagedBean logInManagedBean;
    private Long customerID;
    private List<String> savingAccountNumberList;
    private String savingAccountSelected;
    private Long savingAccountSelectedLong;
    private List<String> debitCardTypeList;
    private String debitCardTypeSelected;
    private DebitCard debitCard;
    //for debit card activation
    private String cardHolder;
    private String cardNo;
    private Date expiryDate;
    private String cvv;
    //set debitCard password
    private String newPassword;
    private String confirmedPassword;
    //view debit card summary
    private List<String> debitCardList;
    private String debitCardSelected;
    //view Debit Card
    private List<DebitCard> debitCards;
    //debit card chargeback
    private String merchantName;
    private Date transactionDate;
    private String transactionAmount;
    private String chargebackDescription;
    private String debitCardNo;
    //cancel debit card
    private String cancelReason;
    private DebitCard debitCardForClose;
    //get debitcard transaction record
    List<DebitCardTransaction> debitCardTransaction = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            this.setCustomerID(logInManagedBean.getCustomerId());
        } catch (Exception e) {
            System.out.print("init encounter error!");
        }
    }

    public DebitCardManagedBean() {
    }

    public void dashboardToCreateDebitCard(ActionEvent event) throws UserHasNoSavingAccountException {
        try {
            this.getSavingAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/debitCardApply_selectAccounts.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to create debit card encounter error!");
        }
    }

    public void dashboardToActivateDebitCard(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/debitCardActivate_EnterDetail.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to activate debit card encounter error!");
        }
    }

    public void dashboardToViewDebitSummary(ActionEvent event) {
        try {
//            dcsb.insertDebitCardTransactionForTesting(customerID);
            debitCardList = dcsb.getDebitCardString(customerID);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/viewDebitCardSummary_selectCard.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to view Debit Card Summary encounter error!");
        }
    }

    public void dashboardToViewDebitCard(ActionEvent event) {
        try {
            debitCards = dcsb.getDebitCard(customerID);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/viewDebitCard.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to view Debit Card encounter error!");
        }
    }

    public void dashboardToChargeback(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/debitCardChargeback.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to Debit Card Chargeback encounter error!");
        }
    }

    public void dashboardToCancelDebitCard(ActionEvent event) {
        try {
            debitCardList = dcsb.getDebitCardStringForClose(customerID);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/debitCardCancel_selectCard.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to Cancel Debit Card encounter error!");
        }
    }

    public void verifyDebitCard(ActionEvent event) throws DebitCardException, IOException {
        try {
            Long cardNoL = Long.parseLong(cardNo);
            Long cvvL = Long.parseLong(cvv);
            if (dcsb.verifyDebitCard(cardHolder, cardNoL, expiryDate, cvvL)) {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/CardManagement/debitCardActivate_setPassword.xhtml");
            } else {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/CardManagement/debitCardActivate_EnterDetail.xhtml");
            }

        } catch (DebitCardException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void setDebitCardPassword(ActionEvent event) throws IOException {
        if (!newPassword.equals(confirmedPassword)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Confirmed Password does not match!! ");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } else {
            Long cardNoL = Long.parseLong(cardNo);
            dcsb.setPassword(cardNoL, newPassword);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/debitCardActivateSuccess.xhtml");
        }
    }

    public void viewDebitCardSummary(ActionEvent event) throws IOException {
        if (debitCardSelected != null) {
            String[] split = debitCardSelected.split(",");
            Long debitCardNo = Long.parseLong(split[0]);
//            dcsb.insertDebitCardTransactionForTesting(customerID);
            debitCardTransaction = dcsb.getDebitCardTransaction(debitCardNo);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/viewDebitCardSummary.xhtml");
            
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select a debit card!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void goToCreateDebitCard(ActionEvent event) throws IOException {
        debitCardTypeList = dcsb.getDebitCardType();
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/MerlionBank-war/CardManagement/debitCardApply.xhtml");
    }

    public void chargeback(ActionEvent event) throws ChargebackException, IOException {
        if ((merchantName != null) && (transactionDate != null) && (transactionAmount != null) && (chargebackDescription != null) && (debitCardNo != null)) {
            BigDecimal amount = new BigDecimal(transactionAmount);
            try {
                dcsb.createChargeback(merchantName, transactionDate, amount, chargebackDescription, debitCardNo);
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/CardManagement/debitCardChargebackSuccess.xhtml");
            } catch (ChargebackException e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", e.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please fill in the blank box!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void createDebitCard(ActionEvent event) throws IOException, UserHasDebitCardException {
        try {
            savingAccountSelectedLong = Long.parseLong(savingAccountSelected.split(",")[0]);
            debitCard = dcsb.createDebitCard(savingAccountSelectedLong, customerID, debitCardTypeSelected);
            if (debitCard != null) {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/CardManagement/debitCardApplySuccess.xhtml");
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Debit Card Application Encounter Error!");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (UserHasDebitCardException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void getSavingAccountNumbers() throws UserHasNoSavingAccountException {
        try {
            System.out.print("inside the getSavingAccountNumbers()");
            System.out.print("inside getsavingAccountNumbers" + customerID);
            savingAccountNumberList = sasb.getSavingAccountNumbers(customerID);
        } catch (UserHasNoSavingAccountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void showDebitCardDetail(ActionEvent event) throws IOException {
        if (debitCardSelected != null && cancelReason != null) {
            debitCardForClose = dcsb.getDebitCardForClose(debitCardSelected);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/debitCardCancel_showDetail.xhtml");
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select the required field!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void cancelDebitCard(ActionEvent event) throws DebitCardException, IOException {
        try {
            dcsb.cancelDebitCard(debitCardSelected);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/debitCardCancelSuccess.xhtml");
        } catch (DebitCardException ex) {
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

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public List<String> getSavingAccountNumberList() {
        return savingAccountNumberList;
    }

    public void setSavingAccountNumberList(List<String> savingAccountNumberList) {
        this.savingAccountNumberList = savingAccountNumberList;
    }

    public String getSavingAccountSelected() {
        return savingAccountSelected;
    }

    public void setSavingAccountSelected(String savingAccountSelected) {
        this.savingAccountSelected = savingAccountSelected;
    }

    public String getDebitCardTypeSelected() {
        return debitCardTypeSelected;
    }

    public void setDebitCardTypeSelected(String debitCardTypeSelected) {
        this.debitCardTypeSelected = debitCardTypeSelected;
    }

    public List<String> getDebitCardTypeList() {
        return debitCardTypeList;
    }

    public void setDebitCardTypeList(List<String> debitCardTypeList) {
        this.debitCardTypeList = debitCardTypeList;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public List<String> getDebitCardList() {
        return debitCardList;
    }

    public void setDebitCardList(List<String> debitCardList) {
        this.debitCardList = debitCardList;
    }

    public String getDebitCardSelected() {
        return debitCardSelected;
    }

    public void setDebitCardSelected(String debitCardSelected) {
        this.debitCardSelected = debitCardSelected;
    }

    public List<DebitCard> getDebitCards() {
        return debitCards;
    }

    public void setDebitCards(List<DebitCard> debitCards) {
        this.debitCards = debitCards;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getChargebackDescription() {
        return chargebackDescription;
    }

    public void setChargebackDescription(String chargebackDescription) {
        this.chargebackDescription = chargebackDescription;
    }

    public String getDebitCardNo() {
        return debitCardNo;
    }

    public void setDebitCardNo(String debitCardNo) {
        this.debitCardNo = debitCardNo;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public DebitCard getDebitCardForClose() {
        return debitCardForClose;
    }

    public void setDebitCardForClose(DebitCard debitCardForClose) {
        this.debitCardForClose = debitCardForClose;
    }

    public List<DebitCardTransaction> getDebitCardTransaction() {
        return debitCardTransaction;
    }

    public void setDebitCardTransaction(List<DebitCardTransaction> debitCardTransaction) {
        this.debitCardTransaction = debitCardTransaction;
    }

    public Long getSavingAccountSelectedLong() {
        return savingAccountSelectedLong;
    }

    public void setSavingAccountSelectedLong(Long savingAccountSelectedLong) {
        this.savingAccountSelectedLong = savingAccountSelectedLong;
    }
    
    

}
