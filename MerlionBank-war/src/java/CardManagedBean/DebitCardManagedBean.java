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
import Exception.UserHasDebitCardException;
import Exception.UserHasNoSavingAccountException;
import java.io.IOException;
import java.io.Serializable;
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
    
    //For Activate Debit Card
    private String dCardNo;
    private String dCardHolder;
    private String dExpiryDate;
    private String dCVV;

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

    public void dashboardToCreateDebitCard() throws UserHasNoSavingAccountException {
        try {
            this.getSavingAccountNumbers();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/debitCardApply_selectAccounts.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to create debit card encounter error!");
        }
    }
    
    public void verifyDebitCard() {
        
    }

    public void goToCreateDebitCard() throws IOException {
        debitCardTypeList = dcsb.getDebitCardType();
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/MerlionBank-war/CardManagement/debitCardApply.xhtml");
    }

    public void createDebitCard() throws IOException, UserHasDebitCardException {
        try {
            debitCard = dcsb.createDebitCard(savingAccountSelected, customerID, debitCardTypeSelected);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/debitCardApplySuccess.xhtml");
        } catch (UserHasDebitCardException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void getSavingAccountNumbers() throws UserHasNoSavingAccountException {
        try {
            System.out.print("inside the getSavingAccountNumbers()");
            System.out.print("inside getsavingAccountNumbers"+customerID);
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

}
