/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardManagement;

import CardEntity.CreditCardApplication;
import CardEntity.Session.CreditCardSessionBeanLocal;
import CommonEntity.Customer;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Bella
 */
@Named(value = "creditCardManagedBean")
@SessionScoped
public class CreditCardManagedBean implements Serializable {

    @EJB
    CreditCardSessionBeanLocal ccsb;
    private List<CreditCardApplication> pendingCreditCardApplications;
    private CreditCardApplication selectedCreditApplication;
    private String path;
    private String fileSelected;

    @PostConstruct
    public void init() {
        this.getPendingApplication();
    }

    public CreditCardManagedBean() {
    }

    public void getPendingApplication() {
        try {
            pendingCreditCardApplications = ccsb.getPendingCreditCardApplication();
        } catch (Exception e) {
            System.out.print("get pending credit card application encounter error!");
        }
    }

    public void approveCardApplication(ActionEvent event) throws ParseException {
        if(selectedCreditApplication != null){
            ccsb.approveCreditCardApplication(selectedCreditApplication);
        }else{
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select an application to approve!");
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void rejectCardApplication(ActionEvent event) {
        if(selectedCreditApplication != null){
            ccsb.rejectCreditCardApplication(selectedCreditApplication);
        }else{
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select an application to reject!");
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public void openDocs(ActionEvent event) throws IOException {

        System.out.println("********Selected Application to view documents is " + selectedCreditApplication.getCustomerIC());

        path = "http://localhost:8080/" + fileSelected;
        System.out.println("File Path From customer is " + fileSelected);
        System.out.println("File Path is " + path);

    }

    public List<CreditCardApplication> getPendingCreditCardApplications() {
        return pendingCreditCardApplications;
    }

    public void setPendingCreditCardApplications(List<CreditCardApplication> pendingCreditCardApplications) {
        this.pendingCreditCardApplications = pendingCreditCardApplications;
    }

    public CreditCardApplication getSelectedCreditApplication() {
        return selectedCreditApplication;
    }

    public void setSelectedCreditApplication(CreditCardApplication selectedCreditApplication) {
        this.selectedCreditApplication = selectedCreditApplication;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileSelected() {
        return fileSelected;
    }

    public void setFileSelected(String fileSelected) {
        this.fileSelected = fileSelected;
    }

}
