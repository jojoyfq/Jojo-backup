/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardManagement;

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
    List<DebitChargeback> pendingDebitChargebackList = new ArrayList();
    DebitChargeback selectedChargeback;

    /**
     * Creates a new instance of DebitCardManagedBean
     */
    @PostConstruct
    public void init() {
        try {
            pendingDebitChargebackList = dcsb.getPendingDebitChargeback();
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

}
