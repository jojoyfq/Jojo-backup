/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardManagedBean;

import CardEntity.DebitCard;
import CardEntity.Session.DebitCardSessionBeanLocal;
import CommonManagedBean.LogInManagedBean;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import Exception.DebitCardException;
import Exception.UserHasDebitCardException;
import Exception.UserHasNoSavingAccountException;
import java.io.IOException;
import java.io.Serializable;
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
    private List<Long> savingAccountNumberList;
    private Long savingAccountSelected;
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
    
    public void dashboardToViewDebitSummary(ActionEvent event){
        try {
            debitCardList = dcsb.getDebitCardString(customerID);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/viewDebitCardSummary_selectCard.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to view Debit Card Summary encounter error!");
        }
    }
    
    public void dashboardToViewDebitCard(ActionEvent event){
        try {
            debitCards = dcsb.getDebitCard(customerID);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/viewDebitCard.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to view Debit Card encounter error!");
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
    
    public void viewDebitCardSummary(ActionEvent event){
        if(debitCardSelected==null){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select a debit card!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }else{
            String[] split = debitCardSelected.split(",");
            Long debitCardNo = Long.parseLong(split[0]); 
        }
    }

    public void goToCreateDebitCard(ActionEvent event) throws IOException {
        debitCardTypeList = dcsb.getDebitCardType();
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/MerlionBank-war/CardManagement/debitCardApply.xhtml");
    }

    public void createDebitCard(ActionEvent event) throws IOException, UserHasDebitCardException {
        try {
            debitCard = dcsb.createDebitCard(savingAccountSelected, customerID, debitCardTypeSelected);
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

    public List<Long> getSavingAccountNumberList() {
        return savingAccountNumberList;
    }

    public void setSavingAccountNumberList(List<Long> savingAccountNumberList) {
        this.savingAccountNumberList = savingAccountNumberList;
    }

    public Long getSavingAccountSelected() {
        return savingAccountSelected;
    }

    public void setSavingAccountSelected(Long savingAccountSelected) {
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

}
