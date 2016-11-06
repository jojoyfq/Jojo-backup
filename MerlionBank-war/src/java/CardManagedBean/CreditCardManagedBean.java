/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardManagedBean;

import CardEntity.CreditCard;
import CardEntity.Session.CreditCardSessionBeanLocal;
import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import CommonManagedBean.LogInManagedBean;
import Exception.ChargebackException;
import Exception.CreditCardException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
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
    @EJB
    AccountManagementSessionBeanLocal amsbl;

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
    private UploadedFile file;
    private UploadedFile employmentPass;
    private UploadedFile cpfContribution;
    private UploadedFile paySlip;
    private UploadedFile salaryApprove;
    //for credit card activation
    private String cardNo;
    private String cardHolder;
    private Date expiryDate;
    private String cvv;
    private String newPassword;
    private String confirmedPassword;
    //cancel credit card
    private List<String> creditCardList;
    private String creditCardSelected;
    private Long creditCardSelectedLong;
    private String cancelReason;
    private CreditCard creditCardForClose;
    //for chargeback requests
    private String creditCardNo;
    private String merchantName;
    private Date transactionDate;
    private String transactionAmount;
    private String chargebackDescription;
            

    @PostConstruct
    public void init() {
        try {
            this.setCustomerID(logInManagedBean.getCustomerId());
            identityList.add(0, "Singaporean");
            identityList.add(1, "Permanent Resident");
            identityList.add(2, "Foreigner");
            System.out.print(identityList);
            creditCardList = ccsb.getCreditCardNumbers(customerID);
        } catch (Exception e) {
            System.out.print("init encounter error!");
        }
    }

    public CreditCardManagedBean() {
    }
    
    public void goBackToHomePage(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/dashboard.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to Home Page Encounter Error!");
        }
    }

    public void dashboardToCreateCreditCard(ActionEvent event) {
        try {

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardApply_SelectIdentity.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to create credit card encounter error!");
        }
    }
    
    public void dashboardToActivateCreditCard(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardActivate_EnterDetail.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to activate Credit card encounter error!");
        }
    }
    
    public void dashboardToCancelCreditCard(ActionEvent event) {
        try {
            creditCardList = ccsb.getCreditCardNumbers(customerID);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardCancel_selectCard.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to cancel Credit card encounter error!");
        }
    }
    
    public void dashboardToChargeback(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardChargeback.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to Credit Card Chargeback encounter error!");
        }
    }
    
    public void dashboardToPayCreditCard(ActionEvent event) {
        try {
            creditCardList = ccsb.getCreditCardNumbers(customerID);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardPay_selectCard.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to pay credit card encounter error!");
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

    public void sendCreditCardApplication(ActionEvent event) throws IOException {
        if (creditCardTypeSelected != null) {
            Customer customer = ccsb.getCustomer(customerID);
            ccsb.newCreditCardApplication(customer, identitySelected, creditCardTypeSelected);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardApplySuccess.xhtml");
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select a credit card type!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void dashboardToCreateLoanAccount(ActionEvent event) {
        try {

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/LoanManagement/createLoanAccount.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to loan page error!");
        }

    }

//    public void handleCPFUpload(FileUploadEvent event) throws IOException {
//        cpfContribution = event.getFile();
//        InputStream input = event.getFile().getInputstream();
//        System.out.print(cpfContribution.getFileName());
//        System.out.println("Uploade file Customer ID: " + customerID);
//        String fileName = cpfContribution.getFileName();
//        String contentType = cpfContribution.getContentType();
//
//        System.out.println("Uploaded File Name Is :: " + cpfContribution.getFileName() + " :: Uploaded File Size :: " + cpfContribution.getSize());
//        System.out.println("Uploade file Customer ID is: " + customerID);
//
//        FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage(String.format("File '%s' of type '%s' successfully uploaded!", fileName, contentType)));
//        //specify the storage path
//
//    }
//    public void handlePaySlipUpload(FileUploadEvent event) {
//        paySlip = event.getFile();
//        System.out.print(paySlip.getFileName());
//        String fileName = paySlip.getFileName();
//        String contentType = paySlip.getContentType();
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "File uploaded successfully!");
//        RequestContext.getCurrentInstance().showMessageInDialog(message);
//        FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage(String.format("File '%s' of type '%s' successfully uploaded!", fileName, contentType)));
//    }
//    public void handleSAUpload(FileUploadEvent event) {
//        salaryApprove = event.getFile();
//        System.out.print(salaryApprove.getFileName());
//        String fileName = salaryApprove.getFileName();
//        String contentType = salaryApprove.getContentType();
//
//        FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage(String.format("File '%s' of type '%s' successfully uploaded!", fileName, contentType)));
//    }
//    public void handleEPUpload(FileUploadEvent event) {
//        employmentPass = event.getFile();
//        System.out.print(employmentPass.getFileName());
//        String fileName = employmentPass.getFileName();
//        String contentType = employmentPass.getContentType();
//
//        FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage(String.format("File '%s' of type '%s' successfully uploaded!", fileName, contentType)));
//    }
    public void uploadFileForeigner(ActionEvent event) throws IOException {
        if (file != null) {
            creditCardTypeList = ccsb.getCreditCardType();

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardApply_selectCard.xhtml");
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please upload all the file!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void fileUploadListener(FileUploadEvent e) throws IOException {
        Customer customer = ccsb.getCustomer(customerID);
        // Get uploaded file from the FileUploadEvent
        this.file = e.getFile();

        // Print out the information of the file
        System.out.println("Uploaded File Name Is :: " + file.getFileName() + " :: Uploaded File Size :: " + file.getSize());

        System.out.println("Uploade file Customer Ic: " + customer.getIc());
        // String destPath = "C:\\Users\\apple\\AppData\\Roaming\\NetBeans\\8.0.2\\config\\GF_4.1\\domain1\\docroot\\" + "\\"+ic + "\\"+file.getFileName();
        String destPath = "C:\\Users\\apple\\AppData\\Roaming\\NetBeans\\8.0.2\\config\\GF_4.1\\domain1\\docroot\\" + customer.getIc() + "\\" + file.getFileName();
        // String savedFileName = path + "/" + uploadedFile.getFileName();
        //    File fileToSave = new File(savedFileName);
        File fileToSave = new File(destPath);

        fileToSave.getParentFile().mkdirs();
        fileToSave.delete();
        //Generate path file to copy file
        Path folder = Paths.get(destPath);
        Path fileToSavePath = Files.createFile(folder);
        //Copy file to server
        InputStream input = e.getFile().getInputstream();
        Files.copy(input, fileToSavePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File uploaded successfully!");
        System.out.println("File path: " + fileToSave.getAbsoluteFile().getName());
        System.out.println("File path: " + fileToSave.getCanonicalFile().getName());

        ccsb.setFileDestination(customer.getId(), fileToSave.getParentFile().getName() + "/" + fileToSave.getAbsoluteFile().getName());

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "File " + file.getFileName() + " uploaded successfully!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public void uploadFileSingaporean(ActionEvent event) throws IOException {
        if (file != null) {
            creditCardTypeList = ccsb.getCreditCardType();

            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardApply_selectCard.xhtml");
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please upload all the file!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void verifyCreditCard(ActionEvent event) throws IOException,CreditCardException {
        try {
            Long cardNoL = Long.parseLong(cardNo);
            Long cvvL = Long.parseLong(cvv);
            if (ccsb.verifyCreditCard(cardHolder, cardNoL, expiryDate, cvvL)) {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/CardManagement/creditCardActivate_setPassword.xhtml");
            } else {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/CardManagement/creditCardActivate_EnterDetail.xhtml");
            }

        } catch (CreditCardException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    public void setCreditCardPassword(ActionEvent event) throws IOException {
        if (!newPassword.equals(confirmedPassword)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Confirmed Password does not match!! ");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } else {
            Long cardNoL = Long.parseLong(cardNo);
            ccsb.setPassword(cardNoL, newPassword);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardActivateSuccess.xhtml");
        }
    }
    
    public void showCreditCardDetail(ActionEvent event) throws IOException {
        if (creditCardSelected != null && cancelReason != null) {
            creditCardForClose = ccsb.getCreditCardForClose(creditCardSelected);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardCancel_showDetail.xhtml");
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select the required field!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    
    public void cancelDebitCard(ActionEvent event) throws CreditCardException, IOException {
        try {
            ccsb.cancelCreditCard(creditCardSelected);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/CardManagement/creditCardCancelSuccess.xhtml");
        } catch (CreditCardException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    public void chargeback(ActionEvent event) throws ChargebackException, IOException {
        if ((merchantName != null) && (transactionDate != null) && (transactionAmount != null) && (chargebackDescription != null) && (creditCardNo != null)) {
            BigDecimal amount = new BigDecimal(transactionAmount);
            try {
                ccsb.createChargeback(merchantName, transactionDate, amount, chargebackDescription, creditCardNo);
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/MerlionBank-war/CardManagement/creditCardChargebackSuccess.xhtml");
            } catch (ChargebackException e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", e.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please fill in the blank box!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    public void showOutstandAmount(ActionEvent event){
        
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
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

    public List<String> getCreditCardList() {
        return creditCardList;
    }

    public void setCreditCardList(List<String> creditCardList) {
        this.creditCardList = creditCardList;
    }

    public Long getCreditCardSelectedLong() {
        return creditCardSelectedLong;
    }

    public void setCreditCardSelectedLong(Long creditCardSelectedLong) {
        this.creditCardSelectedLong = creditCardSelectedLong;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCreditCardSelected() {
        return creditCardSelected;
    }

    public void setCreditCardSelected(String creditCardSelected) {
        this.creditCardSelected = creditCardSelected;
    }

    public CreditCard getCreditCardForClose() {
        return creditCardForClose;
    }

    public void setCreditCardForClose(CreditCard creditCardForClose) {
        this.creditCardForClose = creditCardForClose;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
       
}
