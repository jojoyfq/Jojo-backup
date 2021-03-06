/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TransferManagement;

import DepositEntity.Session.TransferSessionBeanLocal;
import Exception.TransferException;
import Exception.UserHasNoSavingAccountException;
import TellerManagedBean.ServiceCustomerManagedBean;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Named(value = "transferManagedBean")
@SessionScoped
public class TransferManagedBean implements Serializable {

    @EJB
    TransferSessionBeanLocal tfsb;
    @Inject
    private ServiceCustomerManagedBean serviceCustomerManagedBean;

    private List<Long> savingAccountList;
    private String recipientName;
    private String amountString;
    private BigDecimal amountBD;
    private Long recipientAccountNumLong;
    private String recipientAccountNumString;
    private Long giverAccountNumLong;
    private Long customerID;

    @PostConstruct
    public void init() {
    }

    public TransferManagedBean() {
    }

    public void dashboardToIntraTransfer(ActionEvent event) {
        try {
            customerID = serviceCustomerManagedBean.getCustomer().getId();
            if (customerID != null) {
                setCustomerID(serviceCustomerManagedBean.getCustomer().getId());
                this.getSavingAccountNumbers();
                 FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBankBackOffice/TransferManagement/intraTransfer.xhtml");
            } else {
              FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select a customer first!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }

        } catch (Exception e) {
            System.out.print("Dashboard go to intra transfer encounter error!");
        }
    }

    public void getSavingAccountNumbers() throws IOException, UserHasNoSavingAccountException {
        try {
            savingAccountList = tfsb.getSavingAccountNumbers(customerID);
        } catch (UserHasNoSavingAccountException ex) {
            System.out.print("User Has No Saving Account");
        }
    }

    public void oneTimeTransfer(ActionEvent event) throws TransferException, IOException {
        try {
            if (giverAccountNumLong != null) {
                amountBD = new BigDecimal(amountString);
                recipientAccountNumLong = Long.parseLong(recipientAccountNumString);
                System.out.print(amountBD);
                System.out.print(recipientAccountNumLong);
                tfsb.intraOneTimeTransferCheck(customerID, giverAccountNumLong, recipientAccountNumLong, amountBD);

                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBankBackOffice/TransferManagement/intraTransferSuccess.xhtml");
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please Select a Saving Account!");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }

        } catch (TransferException ex) {
            System.out.print("OneTimeTransfer Encounter Error");
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
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

    public List getSavingAccountList() {
        return savingAccountList;
    }

    public void setSavingAccountList(List savingAccountList) {
        this.savingAccountList = savingAccountList;
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

    public String getRecipientAccountNumString() {
        return recipientAccountNumString;
    }

    public void setRecipientAccountNumString(String recipientAccountNumString) {
        this.recipientAccountNumString = recipientAccountNumString;
    }

    public Long getGiverAccountNumLong() {
        return giverAccountNumLong;
    }

    public void setGiverAccountNumLong(Long giverAccountNumLong) {
        this.giverAccountNumLong = giverAccountNumLong;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }
}
