/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardManagedBean;

import CardEntity.CreditCard;
import CardEntity.Session.CreditCardSessionBeanLocal;
import CommonManagedBean.LogInManagedBean;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Bella
 */
@Named(value = "creditCardManagedBean")
@SessionScoped
public class CreditCardManagedBean implements Serializable {

    @EJB
    CreditCardSessionBeanLocal ccsb;
    @Inject
    LogInManagedBean logInManagedBean;

    private Long customerID;
    private List<String> creditCardTypeList;
    private String creditCardTypeSelected;
    private CreditCard creditCard;
    //create credit card
    private List<String> identityList = new ArrayList<>();
    private String identitySelected;
    //upload file
    private UploadedFile employmentPass;
    private UploadedFile cpfContribution;
    private UploadedFile paySlip;
    private UploadedFile salaryApprove;

    @PostConstruct
    public void init() {
        try {
            this.setCustomerID(logInManagedBean.getCustomerId());
            identityList.add(0, "Singaporean");
            identityList.add(1, "Permanent Resident");
            identityList.add(2, "Foreigner");
            System.out.print(identityList);
        } catch (Exception e) {
            System.out.print("init encounter error!");
        }
    }

    public CreditCardManagedBean() {
    }

    public void dashboardToCreateCreditCard(ActionEvent event) {
        try {

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardApply_SelectIdentity.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to create credit card encounter error!");
        }
    }

    public void creditApplyEnterDetail(ActionEvent event) throws IOException {
        if (identitySelected == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select your citizenship!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } else if (identitySelected.equals("Singaporean") || identitySelected.equals("Permanent Resident")) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardApply_UploadFileSingaporean.xhtml");
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardApply_UploadFileForeigner.xhtml");
        }
    }

    public void handleCPFUpload(FileUploadEvent event) {
        cpfContribution = event.getFile();
        System.out.print(cpfContribution.getFileName());
        String fileName = cpfContribution.getFileName();
        String contentType = cpfContribution.getContentType();
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "File uploaded successfully!");
//        RequestContext.getCurrentInstance().showMessageInDialog(message);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(String.format("File '%s' of type '%s' successfully uploaded!", fileName, contentType)));
    }

    public void handlePaySlipUpload(FileUploadEvent event) {
        paySlip = event.getFile();
        System.out.print(paySlip.getFileName());
        String fileName = paySlip.getFileName();
        String contentType = paySlip.getContentType();
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "File uploaded successfully!");
//        RequestContext.getCurrentInstance().showMessageInDialog(message);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(String.format("File '%s' of type '%s' successfully uploaded!", fileName, contentType)));

    }

    public void uploadFileForeigner(ActionEvent event) {
        if (employmentPass != null && salaryApprove != null) {
            FacesMessage message = new FacesMessage("Succesful", employmentPass.getFileName() + " and " + salaryApprove.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please upload all the file!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void uploadFileSingaporean(ActionEvent event) {
        if (cpfContribution != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "CPF Contribution uploaded!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please upload all the file!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public List<String> getCreditCardTypeList() {
        return creditCardTypeList;
    }

    public void setCreditCardTypeList(List<String> creditCardTypeList) {
        this.creditCardTypeList = creditCardTypeList;
    }

    public String getCreditCardTypeSelected() {
        return creditCardTypeSelected;
    }

    public void setCreditCardTypeSelected(String creditCardTypeSelected) {
        this.creditCardTypeSelected = creditCardTypeSelected;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public List<String> getIdentityList() {
        return identityList;
    }

    public void setIdentityList(List<String> identityList) {
        this.identityList = identityList;
    }

    public String getIdentitySelected() {
        return identitySelected;
    }

    public void setIdentitySelected(String identitySelected) {
        this.identitySelected = identitySelected;
    }

    public UploadedFile getEmploymentPass() {
        return employmentPass;
    }

    public void setEmploymentPass(UploadedFile employmentPass) {
        this.employmentPass = employmentPass;
    }

    public UploadedFile getCpfContribution() {
        return cpfContribution;
    }

    public void setCpfContribution(UploadedFile cpfContribution) {
        this.cpfContribution = cpfContribution;
    }

    public UploadedFile getPaySlip() {
        return paySlip;
    }

    public void setPaySlip(UploadedFile paySlip) {
        this.paySlip = paySlip;
    }

    public UploadedFile getSalaryApprove() {
        return salaryApprove;
    }

    public void setSalaryApprove(UploadedFile salaryApprove) {
        this.salaryApprove = salaryApprove;
    }

}
