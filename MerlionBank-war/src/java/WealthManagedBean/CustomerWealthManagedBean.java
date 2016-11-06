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
import WealthEntity.Portfolio;
import WealthEntity.PortfolioTransaction;
import WealthEntity.Product;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.primefaces.event.RowEditEvent;
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
    private BigDecimal initialAmount;
    private Long customerId;
    private int term;
    private Long portfolioId;
    private String typeName;
    UploadedFile file;

    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

    private Map<String, String> wealthProducts;
    private Map<String, String> wealthTypes;
    private String type;
    private Portfolio selectedPort;
    private Double exepectedRateOfReturn;
    private Double foreignExchange;
    private Double equity;
    private Double bond;
    private BigDecimal investAmount;
    private List<Portfolio> oneCustomerAllPortfolios;
    private List<DiscretionaryAccount> oneCusotmerWealthAccounts;
    private List<Product> onePortAllProducts;
    private BigDecimal withdrawAmount;
    private Long selectedSavingAcctId;
    private List<Long> allSavingId;
    
    private Date viewStartDate;
    private Date viewEndDate;
    private List<PortfolioTransaction> onePortfolioAllTransactions;

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
        oneCustomerAllPortfolios = new ArrayList<>();
        selectedPort = new Portfolio();
        oneCusotmerWealthAccounts = new ArrayList<>();
        wealthProducts = new HashMap<String, String>();
        wealthTypes = new HashMap<String, String>();
        onePortAllProducts = new ArrayList<>();
        allSavingId = new ArrayList<>();
onePortfolioAllTransactions = new ArrayList<>();
        wealthProducts.put("Predefined Products", "Predefined Products");
        wealthProducts.put("Tailored Product", "Tailored Product");

        Map<String, String> map = new HashMap<String, String>();
        map.put("Education Planning", "Education Planning");
        map.put("Retirement Planning", "Retirement Planning");
        data.put("Predefined Products", map);

        map = new HashMap<String, String>();
        map.put("Tailored Financial Management Plan", "Tailored Financial Management Plan");
        data.put("Tailored Product", map);
    }

    public void onLoanChange() {
        System.out.println("Selected type name " + typeName);
        if (typeName != null && !typeName.equals("")) {
            wealthTypes = data.get(typeName);

        } else {
            wealthTypes = new HashMap<String, String>();
        }
    }

    public void goToApplyWealthAccountExistCustomer(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/existingCustomerCreateWealthAcct.xhtml");

    }

    public void customerDisplayProducts(ActionEvent event) throws IOException {
        selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");

        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/displayProducts.xhtml");

    }

    public void customerFillUpApplicationForm(ActionEvent event) throws IOException {
        System.out.println("*******Selected type to apply is " + type);
        if (type.equals("Education Planning") || type.equals("Retirement Planning")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/createPredefinedPlan.xhtml");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/createTailoredPlan.xhtml");
        }
    }

    public void goToDisplayAllMyWealthAccounts(ActionEvent event) throws IOException {
        try {
            allWealthAccounts = wsbl.displayAllDiscretionaryAccounts(logInManagedBean.getCustomerId());
            System.out.println("all wealth account size is " + allWealthAccounts.size());
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/viewAllDiscretionaryAccount.xhtml");

        } catch (ListEmptyException | IOException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }
     public void goToPayCommission(ActionEvent event) throws IOException {
        try {
            allWealthAccounts = wsbl.displayAllDiscretionaryAccounts(logInManagedBean.getCustomerId());
            System.out.println("all wealth account size is " + allWealthAccounts.size());
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/customerPayCommission.xhtml");

        } catch (ListEmptyException | IOException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }
    public void goToAcitivateWealthAccount(ActionEvent event){
      try {
            allWealthAccounts = wsbl.displayAllDiscretionaryAccounts(logInManagedBean.getCustomerId());
            System.out.println("all wealth account size is " + allWealthAccounts.size());
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/existingCustomerActivateWealthAccount.xhtml");

        } catch (ListEmptyException | IOException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void goToDisplayAllMyWealthAccountsForPortfolio(ActionEvent event) throws IOException {
        try {
            allWealthAccounts = wsbl.displayAllDiscretionaryAccounts(logInManagedBean.getCustomerId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/viewDiscretionaryAccountsPort.xhtml");

        } catch (ListEmptyException | IOException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public void goToDisplayAllMyWealthAccountsPortfolio(ActionEvent event) throws IOException {
        try {
            allWealthAccounts = wsbl.displayAllDiscretionaryAccounts(logInManagedBean.getCustomerId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/PortfolioManagement/viewAllDiscretionaryAccount.xhtml");

        } catch (ListEmptyException | IOException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public void goToDisplayAllMyWealthAccountsForBuyProduct(ActionEvent event) throws IOException {
        try {
            allWealthAccounts = wsbl.displayAllDiscretionaryAccounts(logInManagedBean.getCustomerId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/customerManageWealthAccounts.xhtml");

        } catch (ListEmptyException | IOException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }
    
     public void goToDisplayAllMyWealthAccountsForPortfolioSystem(ActionEvent event) throws IOException {
        try {
            allWealthAccounts = wsbl.displayAllDiscretionaryAccounts(logInManagedBean.getCustomerId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/PortfolioManagement/viewAllDiscretionaryAccount.xhtml");

        } catch (ListEmptyException | IOException ex) {
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
            customer = wasbl.createDiscretionaryAccount(customerIc, customerName, customerGender, customerDateOfBirth, password, customerEmail,customerPhoneNumber , customerOccupation, customerFamilyInfo);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Discretionary Account has been successfully created! Detailed informaiton has been sent to your email!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/uploadFileForWealth.xhtml");

            //   FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/LoanManagement/displayLoanTypes.xhtml");
        } catch (UserExistException | EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } finally {
            customerIc = null;
            customerName = null;
            customerGender = null;
            customerDateOfBirth = null;
            customerAddress = null;
            customerEmail = null;
            customerPhoneNumber = null;
            customerOccupation = null;
            customerFamilyInfo = null;
        }
    }
    public void customerPayCommission(ActionEvent event){
                    selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");
                    System.out.println("******Selected Wealth account to pay commission is "+selectedWealth.getId());
        try {
            wsbl.payCommissionFee(logInManagedBean.getCustomerId(), selectedWealth.getId());
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have succesfully paid the commission ");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (NotEnoughAmountException ex) {
 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);        }

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

            selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");
            // customerId = logInManagedBean.getCustomerId();
            oneCustomerAllSavingAccts = wsbl.displaySavingAccounts(logInManagedBean.getCustomerId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/customerDisplayAllSaving.xhtml");
        } catch (ListEmptyException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

//public void customerActivateWealthAccount(ActionEvent event){
//            selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");
//                System.out.println("selected wealth to update is "+selectedWealth.getId());

//        try {
//            allWealthAccounts = wsbl.existingCustomerActivateAccount(logInManagedBean.getCustomerId(),selectedWealth.getId() );
//        } catch (NotEnoughAmountException|ListEmptyException ex) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
//            RequestContext.getCurrentInstance().showMessageInDialog(message);
//        }

    public void selectSavingSet(ActionEvent event) {
        selectedSavingAccout = (SavingAccount) event.getComponent().getAttributes().get("selectedSavingAccout");

    }

    public void customerPayBySaving(ActionEvent event) throws NotEnoughAmountException {
        try {
            //  customerId = logInManagedBean.getCustomerId();
            System.out.println("******Selected saving account id " + selectedSavingAccout.getId());
            System.out.println("******Selected wealth account id " + selectedWealth.getId());
            System.out.println("******Top up amount is " + amount);

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
//    public void customerDisplayAllWealthAccounts(ActionEvent event) throws ListEmptyException{
//        try {
//            customerId = logInManagedBean.getCustomerId();
//            oneCusotmerWealthAccounts = wsbl.displayAllDiscretionaryAccounts(customerId);
//        } catch (ListEmptyException ex) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
//            RequestContext.getCurrentInstance().showMessageInDialog(message);
//        }
//    
//    }

    public void customerCreatePredefinedPlan(ActionEvent event) throws NotEnoughAmountException {
        customerId = logInManagedBean.getCustomerId();
        //selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");

        System.out.println("********Selected discretinonary account to create plan is " + selectedWealth.getId());
        System.out.println("********Selected discretinonary account to create plan, the initial amount is  " + initialAmount);
        System.out.println("********Selected discretinonary account to create plan, the type is  " + type);
        System.out.println("********Selected discretinonary account to create plan, the term is  " + term);

        try {
            portfolioId = wsbl.createPrefefinedPlan(customerId, selectedWealth.getId(), initialAmount, type, term);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully created plan for predefined product!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (NotEnoughAmountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void customerCreateTailoredPortfolio(ActionEvent event) throws NotEnoughAmountException {
        //   selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");
        exepectedRateOfReturn = exepectedRateOfReturn / 100;
        foreignExchange = foreignExchange / 100;
        equity = equity / 100;
        bond = bond / 100;
        System.out.println("*****Expected Rate of Return is " + exepectedRateOfReturn);
        System.out.println("*****FX is " + foreignExchange);
        System.out.println("*****Equity is " + equity);
        System.out.println("*****Bond is " + bond);
        if ((equity + bond + foreignExchange) > 1) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Proportion cannot not be greater than 100%");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return;
        }
        try {
            portfolioId = wsbl.createTailoredPortfolio(logInManagedBean.getCustomerId(), selectedWealth.getId(), investAmount, exepectedRateOfReturn, foreignExchange, equity, bond, term);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully created plan for tailored product!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (NotEnoughAmountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void customerDisplayPortfolios(ActionEvent event) {
        selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");

        oneCustomerAllPortfolios = wsbl.displayAllPortfolios(selectedWealth.getId());
        System.out.println("*****Display portfolio now!");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/WealthManagement/portfolioManagement.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(CustomerWealthManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
     public void customerDisplayPortfoliosForPortfolioSystem(ActionEvent event) {
        selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");

        oneCustomerAllPortfolios = wsbl.displayAllPortfolios(selectedWealth.getId());
        System.out.println("*****Display portfolio now!");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/PortfolioManagement/portfolioManagement.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(CustomerWealthManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void customerAcceptPlan(Long portfolioId) {
        //   selectedPort = (Portfolio) event.getComponent().getAttributes().get("selectedPort");
        try {
            oneCustomerAllPortfolios = wsbl.customerAcceptPlan(logInManagedBean.getCustomerId(), portfolioId);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully accepted the plan " + "(" + portfolioId + ")");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void customerCancelPlan(Long portfolioId) {

        oneCustomerAllPortfolios = wsbl.customerCancelPortfolios(portfolioId);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully rejected the plan " + "(" + portfolioId + ")");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }


    public void selectPortfolio(ActionEvent event) {
        selectedPort = (Portfolio) event.getComponent().getAttributes().get("selectedPort");
        onePortAllProducts = selectedPort.getProducts();
        System.out.println("fx proportion is" + onePortAllProducts.get(0).getPercentage());
        foreignExchange = onePortAllProducts.get(0).getPercentage();
        equity = onePortAllProducts.get(1).getPercentage();
        bond = onePortAllProducts.get(2).getPercentage();

    }
public void selectPorfolioToViewTransactions(ActionEvent event){
        selectedPort = (Portfolio) event.getComponent().getAttributes().get("selectedPort");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/PortfolioManagement/viewTransactions.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(CustomerWealthManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

}
    public void customerModifyPortfolio(RowEditEvent event) {
        selectedPort = (Portfolio) event.getObject();
        System.out.println("Selected Portfolio to edit - id: " + selectedPort.getId());
        System.out.println("Term to modify is " + term);

        oneCustomerAllPortfolios = wsbl.ModifyPortfolioRate(selectedPort.getId(), exepectedRateOfReturn, term);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully modifed your plan!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    public void customerModifyProduct(ActionEvent event) {
        //  selectedPort = (Portfolio) event.getObject();
        System.out.println("Selected Portfolio to edit - id: " + selectedPort.getId());
        System.out.println("edited proportion for FX is " + foreignExchange);

        oneCustomerAllPortfolios = wsbl.ModifyPortfolioProduct(selectedPort.getId(), foreignExchange, equity, bond);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully modifed your plan!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    public void selectWealthAccount(ActionEvent event) {
        selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");
        try {
            oneCustomerAllSavingAccts = wsbl.displaySavingAccounts(logInManagedBean.getCustomerId());
            for(int i=0;i<oneCustomerAllSavingAccts.size();i++){
            allSavingId.add(oneCustomerAllSavingAccts.get(i).getId());
            }
            
        } catch (ListEmptyException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }


    public void withdrawFromDiscretionaryAccount(ActionEvent event) {
        System.out.println("******Selected discretionary Account to withdraw is " + selectedWealth.getId());
                System.out.println("******Selected saving Account to debit is " + selectedSavingAcctId);

        boolean result = wsbl.compareAmount(logInManagedBean.getCustomerId(), selectedWealth.getId(), withdrawAmount);
        if (result = true) {
            try {
                wsbl.transferBackToSavingWithEnoughBalance(logInManagedBean.getCustomerId(),selectedSavingAcctId, selectedWealth.getId(), withdrawAmount);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully withdrawed " + withdrawAmount + "!");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            } catch (NotEnoughAmountException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {
            try {
                wsbl.transferBackToSavingWithNotEnoughBalance(logInManagedBean.getCustomerId(),selectedSavingAcctId, selectedWealth.getId(), withdrawAmount);
            } catch (NotEnoughAmountException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        }
    }
    
    public void customerViewAllTransactions(ActionEvent event){
         //   selectedPort = (Portfolio) event.getComponent().getAttributes().get("selectedPort");
            System.out.println("******Selected Discretionary Account to view transaction is "+selectedPort.getId());
            
onePortfolioAllTransactions = wsbl.viewtransactionHistory(selectedPort.getId(), viewStartDate, viewEndDate);
    
    }
    public void customerEarlyWithdrawPortfolio(ActionEvent event){
     selectedPort = (Portfolio) event.getComponent().getAttributes().get("selectedPort");
                 System.out.println("******Selected portfolio to withdraw is "+selectedPort.getId());
                 oneCustomerAllPortfolios = wsbl.portfolioEarlyWithdraw(selectedPort.getId());
                   FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully withdraw the portfolio!");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
                 


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

    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(BigDecimal initialAmount) {
        this.initialAmount = initialAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public Map<String, String> getWealthProducts() {
        return wealthProducts;
    }

    public void setWealthProducts(Map<String, String> wealthProducts) {
        this.wealthProducts = wealthProducts;
    }

    public Map<String, String> getWealthTypes() {
        return wealthTypes;
    }

    public void setWealthTypes(Map<String, String> wealthTypes) {
        this.wealthTypes = wealthTypes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Portfolio getSelectedPort() {
        return selectedPort;
    }

    public void setSelectedPort(Portfolio selectedPort) {
        this.selectedPort = selectedPort;
    }

    public Double getExepectedRateOfReturn() {
        return exepectedRateOfReturn;
    }

    public void setExepectedRateOfReturn(Double exepectedRateOfReturn) {
        this.exepectedRateOfReturn = exepectedRateOfReturn;
    }

    public Double getForeignExchange() {
        return foreignExchange;
    }

    public void setForeignExchange(Double foreignExchange) {
        this.foreignExchange = foreignExchange;
    }

    public Double getEquity() {
        return equity;
    }

    public void setEquity(Double equity) {
        this.equity = equity;
    }

    public Double getBond() {
        return bond;
    }

    public void setBond(Double bond) {
        this.bond = bond;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public List<Portfolio> getOneCustomerAllPortfolios() {
        return oneCustomerAllPortfolios;
    }

    public void setOneCustomerAllPortfolios(List<Portfolio> oneCustomerAllPortfolios) {
        this.oneCustomerAllPortfolios = oneCustomerAllPortfolios;
    }

    public List<DiscretionaryAccount> getOneCusotmerWealthAccounts() {
        return oneCusotmerWealthAccounts;
    }

    public void setOneCusotmerWealthAccounts(List<DiscretionaryAccount> oneCusotmerWealthAccounts) {
        this.oneCusotmerWealthAccounts = oneCusotmerWealthAccounts;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public List<Product> getOnePortAllProducts() {
        return onePortAllProducts;
    }

    public void setOnePortAllProducts(List<Product> onePortAllProducts) {
        this.onePortAllProducts = onePortAllProducts;
    }

    public Long getSelectedSavingAcctId() {
        return selectedSavingAcctId;
    }

    public void setSelectedSavingAcctId(Long selectedSavingAcctId) {
        this.selectedSavingAcctId = selectedSavingAcctId;
    }

    public List<Long> getAllSavingId() {
        return allSavingId;
    }

    public void setAllSavingId(List<Long> allSavingId) {
        this.allSavingId = allSavingId;
    }

    public Date getViewStartDate() {
        return viewStartDate;
    }

    public void setViewStartDate(Date viewStartDate) {
        this.viewStartDate = viewStartDate;
    }

    public Date getViewEndDate() {
        return viewEndDate;
    }

    public void setViewEndDate(Date viewEndDate) {
        this.viewEndDate = viewEndDate;
    }

    public List<PortfolioTransaction> getOnePortfolioAllTransactions() {
        return onePortfolioAllTransactions;
    }

    public void setOnePortfolioAllTransactions(List<PortfolioTransaction> onePortfolioAllTransactions) {
        this.onePortfolioAllTransactions = onePortfolioAllTransactions;
    }
    

}
