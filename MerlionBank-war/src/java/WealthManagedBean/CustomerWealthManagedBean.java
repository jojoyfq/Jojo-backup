/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import CommonManagedBean.LogInManagedBean;
import DepositEntity.SavingAccount;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.NotEnoughAmountException;
import Exception.UserExistException;
import WealthEntity.DiscretionaryAccount;
import WealthEntity.Session.WealthApplicationSessionBeanLocal;
import WealthEntity.Session.WealthSessionBeanLocal;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author apple
 */
@Named(value = "customerWealthManagedBean")
@SessionScoped
public class CustomerWealthManagedBean implements Serializable {

    private Customer customer;
    private String customerIc;
    private String customerName;
    private String customerGender;
    private Date customerDateOfBirth;
    private String customerAddress;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerOccupation;
    private String customerFamilyInfo;
    private String password;
    private List<DiscretionaryAccount> allWealthAccounts;
    private List<SavingAccount> oneCustomerAllSavingAccts;
    private DiscretionaryAccount selectedWealth;
    private SavingAccount selectedSavingAccout;
    private Long transferAmount;
    private BigDecimal amount;
    UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    @EJB
    private WealthApplicationSessionBeanLocal wasbl;
    @EJB
    AccountManagementSessionBeanLocal amsbl;
    @EJB
    WealthSessionBeanLocal wsbl;
    @Inject
    private LogInManagedBean logInManagedBean;

    /**
     * Creates a new instance of CustomerWealthManagedBean
     */
    public CustomerWealthManagedBean() {
    }

    @PostConstruct
    public void init() {
        allWealthAccounts = new ArrayList<>();
        oneCustomerAllSavingAccts = new ArrayList<>();
        selectedWealth = new DiscretionaryAccount();
        selectedSavingAccout = new SavingAccount();
    }

    public void goToApplyWealthAccountExistCustomer(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/existingCustomerCreateWealthAcct.xhtml");

    }
    public void goToDisplayAllMyWealthAccounts(ActionEvent event) throws IOException {
        try {
            allWealthAccounts = wsbl.displayAllDiscretionaryAccounts(logInManagedBean.getCustomerId());
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/viewAllDiscretionaryAccount.xhtml");

        } catch (ListEmptyException|IOException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public void fileUploadListener(FileUploadEvent e) throws IOException {

        // Get uploaded file from the FileUploadEvent
        this.file = e.getFile();
     //    InputStream input = e.getFile().getInputstream();

        //   File inputFile = new File(e.getFile().getFileName());
        // Get uploaded file from the FileUploadEvent
        //     this.file = e.getFile();
        //                customer.
        //                System.out.println("");
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
// String destPath = "C:\\Users\\apple\\AppData\\Roaming\\NetBeans\\8.0.2\\config\\GF_4.1\\domain1\\docroot\\" + "Sxiaojing" ;d

//         File destFile = new File(destPath);
//         FileUtils.forceMkdir(destFile);
//         FileUtils.copyInputStreamToFile(input, destFile);
        //FileUtils.copyFileToDirectory(inputFile, destFile);
        System.out.println("File uploaded successfully!");
        amsbl.setFileDestination(customer.getId(), fileToSave.getParentFile().getName());
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/customerSuccessPageWOLogIn.xhtml");

    }

    public void customerCreateWealthAcct(ActionEvent event) throws UserExistException, EmailNotSendException, IOException {
        try {
            customer = wasbl.createDiscretionaryAccount(customerIc, customerName, customerGender, customerDateOfBirth, password, customerEmail, customerName, customerOccupation, customerFamilyInfo);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Discretionary Account has been successfully created! Detailed informaiton has been sent to your email!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/uploadFileForWealth.xhtml");

            //   FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/LoanManagement/displayLoanTypes.xhtml");
        } catch (UserExistException | EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void existingCustomerCreateWealthAcct(ActionEvent event) throws EmailNotSendException {
        try {
            System.out.println("********Customer to create wealth account is " + logInManagedBean.getCustomerId());
            wasbl.createDiscretionaryAccountExistingCustomer(logInManagedBean.getCustomerId());
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Discretionary Account has been successfully created! Detailed informaiton has been sent to your email!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void displayAllDiscretionaryAccount(ActionEvent event) throws ListEmptyException {
        try {
            System.out.println("*****Customer ID to view all accounts is " + logInManagedBean.getCustomerId());

            allWealthAccounts = wsbl.displayAllDiscretionaryAccounts(logInManagedBean.getCustomerId());
        } catch (ListEmptyException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void customerDisplaySavingAccounts(ActionEvent event) throws ListEmptyException, IOException {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/customerDisplayAllSaving.xhtml");
            selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");
            // customerId = logInManagedBean.getCustomerId();
            oneCustomerAllSavingAccts = wsbl.displaySavingAccounts(logInManagedBean.getCustomerId());

        } catch (ListEmptyException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void customerPayBySaving(ActionEvent event) throws NotEnoughAmountException {
        try {
            //  customerId = logInManagedBean.getCustomerId();
            selectedSavingAccout = (SavingAccount) event.getComponent().getAttributes().get("selectedSavingAccout");
            System.out.println("******Selected saving account id " + selectedSavingAccout.getId());
            System.out.println("******Selected wealth account id " + selectedWealth.getId());

            wsbl.topUpBySaving(logInManagedBean.getCustomerId(), selectedSavingAccout.getId(), selectedWealth.getId(), amount);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have transfered S$" + amount + " successfully!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (NotEnoughAmountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
     public void customerTransferToSaving(ActionEvent event) throws NotEnoughAmountException {
        try {
            //  customerId = logInManagedBean.getCustomerId();
            selectedSavingAccout = (SavingAccount) event.getComponent().getAttributes().get("selectedSavingAccout");
            System.out.println("******Selected saving account id " + selectedSavingAccout.getId());
            System.out.println("******Selected wealth account id " + selectedWealth.getId());

            wsbl.topUpBySaving(logInManagedBean.getCustomerId(), selectedSavingAccout.getId(), selectedWealth.getId(), amount);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have transfered S$" + amount + " successfully!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (NotEnoughAmountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void existingCustomerActivateAccount() {

    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCustomerIc() {
        return customerIc;
    }

    public void setCustomerIc(String customerIc) {
        this.customerIc = customerIc;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public Date getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(Date customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerOccupation() {
        return customerOccupation;
    }

    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }

    public String getCustomerFamilyInfo() {
        return customerFamilyInfo;
    }

    public void setCustomerFamilyInfo(String customerFamilyInfo) {
        this.customerFamilyInfo = customerFamilyInfo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public WealthApplicationSessionBeanLocal getWasbl() {
        return wasbl;
    }

    public void setWasbl(WealthApplicationSessionBeanLocal wasbl) {
        this.wasbl = wasbl;
    }

    public LogInManagedBean getLogInManagedBean() {
        return logInManagedBean;
    }

    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
        this.logInManagedBean = logInManagedBean;
    }

    public List<DiscretionaryAccount> getAllWealthAccounts() {
        return allWealthAccounts;
    }

    public void setAllWealthAccounts(List<DiscretionaryAccount> allWealthAccounts) {
        this.allWealthAccounts = allWealthAccounts;
    }

    public List<SavingAccount> getOneCustomerAllSavingAccts() {
        return oneCustomerAllSavingAccts;
    }

    public void setOneCustomerAllSavingAccts(List<SavingAccount> oneCustomerAllSavingAccts) {
        this.oneCustomerAllSavingAccts = oneCustomerAllSavingAccts;
    }

    public DiscretionaryAccount getSelectedWealth() {
        return selectedWealth;
    }

    public void setSelectedWealth(DiscretionaryAccount selectedWealth) {
        this.selectedWealth = selectedWealth;
    }

    public SavingAccount getSelectedSavingAccout() {
        return selectedSavingAccout;
    }

    public void setSelectedSavingAccout(SavingAccount selectedSavingAccout) {
        this.selectedSavingAccout = selectedSavingAccout;
    }

    public Long getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Long transferAmount) {
        this.transferAmount = transferAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AccountManagementSessionBeanLocal getAmsbl() {
        return amsbl;
    }

    public void setAmsbl(AccountManagementSessionBeanLocal amsbl) {
        this.amsbl = amsbl;
    }

    public WealthSessionBeanLocal getWsbl() {
        return wsbl;
    }

    public void setWsbl(WealthSessionBeanLocal wsbl) {
        this.wsbl = wsbl;
    }

}
