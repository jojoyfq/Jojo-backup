/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardManagement;

import CardEntity.DebitCardType;
import CardEntity.DebitChargeback;
import CardEntity.Session.DebitCardSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
    private List<DebitChargeback> pendingDebitChargebackList = new ArrayList();
    private DebitChargeback selectedChargeback;
    //view debitcard scheme
    private List<DebitCardType> debitCardTypes = new ArrayList();
    //change debitcard scheme
    private List<String> debitCardTypeList = new ArrayList();
    private String debitCardTypeSelected;
    private double rebateRate;

    /**
     * Creates a new instance of DebitCardManagedBean
     */
    @PostConstruct
    public void init() {
        try {
            pendingDebitChargebackList = dcsb.getPendingDebitChargeback();
            debitCardTypes = dcsb.getDebitCardTypes();
        } catch (Exception e) {
            System.out.print("init encounter error!");
        }
    }
    
    public DebitCardManagedBean() {
    }
    
    public void dashboardToViewDebitChargeback() throws IOException {
        pendingDebitChargebackList = dcsb.getPendingDebitChargeback();
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/MerlionBankBackOffice/CardManagement/staffViewChargeback.xhtml");
    }
    
    public void dashboardToViewDebitCardScheme() {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/CardManagement/viewDebitCardScheme.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to view debit card scheme encounter error!");
        }
    }
    
    public void dashboardToChangeDebitScheme() {
        try {
            debitCardTypeList = dcsb.getDebitCardTypeList();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/CardManagement/changeDebitCardScheme_selectCardType.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to change debit card scheme encounter error!");
        }
    }
    
    public void verifyDebitChargebackApprove() {
        try {
            if (selectedChargeback != null) {
                dcsb.setChargebackStatus(selectedChargeback, "staff approved");
                pendingDebitChargebackList = dcsb.getPendingDebitChargeback();
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBankBackOffice/CardManagement/staffViewChargeback.xhtml");
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select a chargeback to verify! ");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (Exception e) {
            System.out.print("Verify Debit Chargeback approve encounter error");
        }
    }
    
    public void verifyDebitChargebackReject() {
        try {
            if (selectedChargeback != null) {
                dcsb.setChargebackStatus(selectedChargeback, "staff rejected");
                pendingDebitChargebackList = dcsb.getPendingDebitChargeback();
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBankBackOffice/CardManagement/staffViewChargeback.xhtml");
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select a chargeback to verify! ");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (Exception e) {
            System.out.print("Verify Debit Chargeback reject encounter error");
        }
    }
    
    public void showDebitCardTypeDetail(ActionEvent event) throws IOException {
        if (debitCardTypeSelected != null) {
            rebateRate = dcsb.getRebateRate(debitCardTypeSelected);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/CardManagement/changeDebitCardScheme.xhtml");
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select a debit card type! ");
            RequestContext.getCurrentInstance().showMessageInDialog(message);            
        }
    }
    
    public void changeRebateRate(ActionEvent event) {
        try {
            dcsb.changeRebateRate(debitCardTypeSelected, rebateRate);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/CardManagement/changeDebitCardSchemeSuccess.xhtml");
        } catch (Exception ex) {
            System.out.println("change rebate rate encounter error!");
        }
    }
    
    public List<DebitChargeback> getPendingDebitChargebackList() {
        return pendingDebitChargebackList;
    }
    
    public void setPendingDebitChargebackList(List<DebitChargeback> pendingDebitChargebackList) {
        this.pendingDebitChargebackList = pendingDebitChargebackList;
    }
    
    public DebitChargeback getSelectedChargeback() {
        return selectedChargeback;
    }
    
    public void setSelectedChargeback(DebitChargeback selectedChargeback) {
        this.selectedChargeback = selectedChargeback;
    }
    
    public List<DebitCardType> getDebitCardTypes() {
        return debitCardTypes;
    }
    
    public void setDebitCardTypes(List<DebitCardType> debitCardTypes) {
        this.debitCardTypes = debitCardTypes;
    }
    
    public List<String> getDebitCardTypeList() {
        return debitCardTypeList;
    }
    
    public void setDebitCardTypeList(List<String> debitCardTypeList) {
        this.debitCardTypeList = debitCardTypeList;
    }
    
    public String getDebitCardTypeSelected() {
        return debitCardTypeSelected;
    }
    
    public void setDebitCardTypeSelected(String debitCardTypeSelected) {
        this.debitCardTypeSelected = debitCardTypeSelected;
    }
    
    public double getRebateRate() {
        return rebateRate;
    }
    
    public void setRebateRate(double rebateRate) {
        this.rebateRate = rebateRate;
    }
    
}
