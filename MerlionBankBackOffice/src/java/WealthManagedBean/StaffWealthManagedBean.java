/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.Staff;
import DepositEntity.SavingAccount;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.NotEnoughAmountException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.Session.LoanApplicationSessionBeanLocal;
import StaffManagement.staffLogInManagedBean;
import WealthEntity.DiscretionaryAccount;
import WealthEntity.Portfolio;
import WealthEntity.Session.WealthApplicationSessionBeanLocal;
import WealthEntity.Session.WealthManagementSessionBeanLocal;
import WealthEntity.Session.WealthSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author apple
 */
@Named(value = "staffWealthManagedBean")
@SessionScoped
public class StaffWealthManagedBean implements Serializable {

    /**
     * Creates a new instance of StaffWealthManagedBean
     */
    public StaffWealthManagedBean() {
    }

    private Customer customer;
    private Customer searchedCustomer;
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
    private Long staffId;
    private Long customerId;
    private Long portfolioId;
    private Long searchedCustomerId;
    private Double exepectedRateOfReturn;
    private Double foreignExchange;
    private Double equity;
    private Double bond;
    private String searchedCustomerIc;

    private List<Portfolio> pendingApprovedTailoredPlans;
    private List<Portfolio> oneCustomerAllPortfolios;
    private List<Portfolio> pendingActivatedPlans;
    private List<DiscretionaryAccount> allWealthAccounts;
    private DiscretionaryAccount selectedWealth;
    UploadedFile file;
    private List<DiscretionaryAccount> pendingDisAccount;
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    Map<String, String> map = new HashMap<String, String>();

    private Map<String, String> wealthProducts;
    private Map<String, String> wealthTypes;
    private String type;
    private Portfolio selectedPort;
    private BigDecimal initialAmount;
    private int term;
    private List<SavingAccount> oneCustomerAllSavingAccts;
    private SavingAccount selectedSavingAccout;
    private BigDecimal amount;
    private BigDecimal withdrawAmount;
    private Portfolio selecteWealthToActivate;
    private Date startDate;

    private Map<String, String> availableRoleNames;
    private Map<String, String> availableStaffs;
    private String selectedRoleName;
    private List<Staff> relatedStaffs;
   private String selectedStaffName;
   private List roleNames;
   private Long portId;

    @EJB
    private WealthApplicationSessionBeanLocal wasbl;
    @EJB
    AccountManagementSessionBeanLocal amsbl;
    @EJB
    WealthSessionBeanLocal wsbl;
    @EJB
    WealthManagementSessionBeanLocal wmsbl;
    @Inject
    private staffLogInManagedBean slimb;
    @EJB
    LoanApplicationSessionBeanLocal lasbl;
    @EJB
    StaffManagementSessionBeanLocal smsbl;

    @PostConstruct
    public void init() {
        staffId = slimb.getStaffId();
        pendingDisAccount = new ArrayList<>();
        pendingApprovedTailoredPlans = new ArrayList<>();
        selectedWealth = new DiscretionaryAccount();
        selectedPort = new Portfolio();
        oneCustomerAllPortfolios = new ArrayList<>();
        allWealthAccounts = new ArrayList<>();
        oneCustomerAllSavingAccts = new ArrayList<>();
        selectedSavingAccout = new SavingAccount();
        pendingActivatedPlans = new ArrayList<>();
        selecteWealthToActivate = new Portfolio();
        relatedStaffs = new ArrayList<>();
        roleNames = new ArrayList<>();
                availableRoleNames = new HashMap<String, String>();

//        wealthProducts.put("Predefined Products", "Predefined Products");
//        wealthProducts.put("Tailored Product", "Tailored Product");
//
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("Education Planning", "Education Planning");
//        map.put("Retirement Planning", "Retirement Planning");
//        data.put("Predefined Products", map);
//
//        map = new HashMap<String, String>();
//        map.put("Tailored Financial Management Plan", "Tailored Financial Management Plan");
//
//        data.put("Tailored Product", map);
    }

    public void searchCustomerId(ActionEvent event) throws UserNotExistException, UserNotActivatedException {
        try {
            System.out.println("searched customer is " + searchedCustomerIc);
            searchedCustomer = lasbl.searchCustomer(searchedCustomerIc);
            searchedCustomerId = searchedCustomer.getId();

        } catch (UserNotExistException | UserNotActivatedException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }

    public void searchCustomerIdToActivate(ActionEvent event) throws UserNotExistException, UserNotActivatedException {
        try {
            System.out.println("searched customer is " + searchedCustomerIc);
            searchedCustomer = lasbl.searchCustomer(searchedCustomerIc);
            searchedCustomerId = searchedCustomer.getId();
            System.out.println("**** go to view pending loans alr!");
            pendingActivatedPlans = wmsbl.viewAllPendingAcivationTailoredPlan(searchedCustomerId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/WealthManagement/viewPendingActivatedPlans.xhtml");

        } catch (UserNotExistException | UserNotActivatedException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        } catch (IOException ex) {
            Logger.getLogger(StaffWealthManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onLoanChange() {
        if (type != null && !type.equals("")) {
            wealthTypes = data.get(type);

        } else {
            wealthTypes = new HashMap<String, String>();
        }
    }

    public void goToApplyWealthAccountExistCustomer(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/WealthManagement/createWealthForExistingCustomer.xhtml");

    }

    public void goToApplyWealthAccountNewCustomer(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/WealthManagement/createWealthForNewCustomer.xhtml");

    }

    public void staffCreateWealthForNewCustomer(ActionEvent event) throws UserExistException, EmailNotSendException, IOException {
        try {
            System.out.println("**********Staff id is " + staffId);
            customer = wasbl.tellerCreateDiscretionaryAccount(staffId, customerIc, customerName, customerGender, customerDateOfBirth, password, customerEmail, customerName, customerOccupation, customerFamilyInfo, password);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Discretionary Account has been successfully created for customer " + customer.getIc() + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

            //   FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/LoanManagement/displayLoanTypes.xhtml");
        } catch (UserExistException | EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

//    public void searchCustomerByIc(ActionEvent event) throws UserNotExistException, UserNotActivatedException {
//        try {
//            searchedCustomer = lasbl.searchCustomer(customerIc);
//        } catch (UserNotExistException | UserNotActivatedException ex) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
//            RequestContext.getCurrentInstance().showMessageInDialog(message);
//        }
//    }
    public void existingCustomerCreateWealthAcct(ActionEvent event) throws EmailNotSendException {
        try {
            System.out.println("********Customer Id from search result is " + searchedCustomer.getId());
            wasbl.staffCreateDiscretionaryAccountExistingCustomer(staffId, searchedCustomer.getId());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Discretionary Account has been successfully created for customer " + customerIc + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void staffCreatePredefinedPlan(ActionEvent event) throws NotEnoughAmountException {
        selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");

        System.out.println("********Selected discretinonary account to create plan is " + selectedWealth.getId());
        System.out.println("********Selected discretinonary account to create plan, the initial amount is  " + initialAmount);
        System.out.println("********Selected discretinonary account to create plan, the type is  " + type);
        System.out.println("********Selected discretinonary account to create plan, the term is  " + term);

        try {
            portfolioId = wsbl.staffCreatePrefefinedPlan(staffId, searchedCustomer.getId(), selectedWealth.getId(), initialAmount, type, term);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Discretionary Account has been successfully created for customer " + customerIc + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (NotEnoughAmountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void displayPending(ActionEvent event) throws IOException {
        System.out.println("*****Go in to view all pending tailored plans!");
        pendingApprovedTailoredPlans = wmsbl.viewAllPendingApproveTailoredPlan();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/WealthManagement/viewPendingPlans.xhtml");

    }

    public void staffAcceptPlan(ActionEvent event) {
        selectedPort = (Portfolio) event.getComponent().getAttributes().get("selectedPort");
        try {
            pendingApprovedTailoredPlans = wmsbl.staffApprovePortfolios(customerId, selectedPort.getId());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully accepted the plan " + selectedPort.getType() + "(" + selectedPort.getId() + ")");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void staffCancelPlan(ActionEvent event) throws EmailNotSendException {
        try {
            selectedPort = (Portfolio) event.getComponent().getAttributes().get("selectedPort");
            pendingApprovedTailoredPlans = wmsbl.staffRejectPortfolios(staffId, selectedPort.getId());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully rejected the plan " + selectedPort.getType() + "(" + selectedPort.getId() + ")");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public void displayAllDiscretionaryAccount(ActionEvent event) throws ListEmptyException {
        try {
            System.out.println("*****Customer ID to view all accounts is " + searchedCustomer.getId());

            allWealthAccounts = wsbl.displayAllDiscretionaryAccounts(searchedCustomer.getId());
        } catch (ListEmptyException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void customerDisplayPortfolios(ActionEvent event) {
        selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");
        oneCustomerAllPortfolios = wsbl.displayAllPortfolios(selectedWealth.getId());
        System.out.println("*****Display portfolio now!");

    }

//    public void staffModifyPortfolio(RowEditEvent event) throws EmailNotSendException {
//        try {
//            selectedPort = (Portfolio) event.getObject();
//            System.out.println("******Selected Portfolio to edit is " + selectedPort.getId());
//            exepectedRateOfReturn = selectedPort.getExpectedRateOfReturn();
//            foreignExchange = selectedPort.getProducts().get(0).getPercentage();
//            equity = selectedPort.getProducts().get(1).getPercentage();
//            bond = selectedPort.getProducts().get(2).getPercentage();
//            term = selectedPort.getTerm();
//            oneCustomerAllPortfolios = wmsbl.staffModifyPortfolios(staffId, selectedPort.getId(), exepectedRateOfReturn, foreignExchange, equity, bond, term);
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Modified Successfully!");
//            RequestContext.getCurrentInstance().showMessageInDialog(message);
//
//        } catch (EmailNotSendException ex) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
//            RequestContext.getCurrentInstance().showMessageInDialog(message);
//        }
//    }

    public void customerDisplaySavingAccounts(ActionEvent event) throws ListEmptyException, IOException {
        try {

            selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");
            // customerId = logInManagedBean.getCustomerId();
            oneCustomerAllSavingAccts = wsbl.displaySavingAccounts(searchedCustomerId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/WealthManagement/displayOneCustomerAllSavingAccounts.xhtml");
        } catch (ListEmptyException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void customerPayBySaving(ActionEvent event) {

        //  customerId = logInManagedBean.getCustomerId();
        selectedSavingAccout = (SavingAccount) event.getComponent().getAttributes().get("selectedSavingAccout");
        System.out.println("******Selected saving account id " + selectedSavingAccout.getId());
        System.out.println("******Selected wealth account id " + selectedWealth.getId());

        wmsbl.topUpAccount(staffId, selectedSavingAccout.getId(), amount);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have transfered S$" + amount + " successfully!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    public void selectWealthAccountToWithdraw(ActionEvent event) {
        selectedWealth = (DiscretionaryAccount) event.getComponent().getAttributes().get("selectedWealth");

    }

    public void withdrawFromDiscretionaryAccount(ActionEvent event) {
        System.out.println("******Selected discretionary Account to withdraw is " + selectedWealth.getId());
        boolean result = wmsbl.compareAmount(selectedWealth.getId(), withdrawAmount);
        if (result = true) {
            try {
                wmsbl.discreationaryAccountMoneyWithdrawWithEnoughBalance(staffId, selectedWealth.getCustomer().getId(), selectedWealth.getId(), withdrawAmount);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully withdrawed " + withdrawAmount + "!");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            } catch (NotEnoughAmountException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {
            try {
                wmsbl.transferBackToSavingWithNotEnoughBalance(staffId, selectedWealth.getCustomer().getId(), selectedWealth.getId(), withdrawAmount);
            } catch (NotEnoughAmountException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        }
    }

    public void selectPortToActivate(ActionEvent event) {
        selecteWealthToActivate = (Portfolio) event.getComponent().getAttributes().get("selecteWealthToActivate");

    }

    public void staffActivatePlan(ActionEvent event) {
        System.out.println("******Selected portfolio to activate is " + selecteWealthToActivate.getId());

        wmsbl.staffActivateLoan(staffId, selecteWealthToActivate.getId(), startDate);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully activate plan " + selecteWealthToActivate.getType() + "(" + selecteWealthToActivate.getId() + ")");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    public void onStaffChange() {
        if (availableRoleNames != null && !availableRoleNames.equals("")) {
            availableStaffs = data.get(selectedRoleName);
        } else {
            availableStaffs = new HashMap<String, String>();
        }
    }

    public void assignRM(ActionEvent event) {
         selectedPort = (Portfolio) event.getComponent().getAttributes().get("selectedPort");
         System.out.println("****Selected port to assign rm is "+selectedPort.getId());
         portId = selectedPort.getId();
         System.out.println("***********Role size is " + smsbl.viewRoles().size());
        for (int i = 0; i < smsbl.viewRoles().size(); i++) {
            System.out.println("********role name is "+smsbl.viewRoles().get(i).getRoleName());
            roleNames.add(smsbl.viewRoles().get(i).getRoleName());
        }
        for (int i = 0; i <roleNames.size(); i++) {
            availableRoleNames.put(roleNames.get(i).toString(), roleNames.get(i).toString());
            System.out.println("************** " + availableRoleNames);
            try {
                // issueType = slimb.getRoleNames().get(i).toString();
                relatedStaffs = wmsbl.retrieveStaffsAccordingToRole(roleNames.get(i).toString());
            } catch (ListEmptyException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
            for (int j = 0; j < relatedStaffs.size(); j++) {
                map.put(relatedStaffs.get(j).getStaffName(), relatedStaffs.get(j).getStaffName());
            }
            data.put(roleNames.get(i).toString(), map);
            map = new HashMap<String, String>();
        }
        
        

    }
    public void assignRM2(ActionEvent event){
//                    selectedPort = (Portfolio) event.getComponent().getAttributes().get("selectedPort");
System.out.println("******Selected port is "+portId);
System.out.println("******Selected port staff ID is "+staffId);
        wmsbl.assignRM(portId, staffId);
         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully assigned "+selectedStaffName+"!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public Portfolio getSelecteWealthToActivate() {
        return selecteWealthToActivate;
    }

    public void setSelecteWealthToActivate(Portfolio selecteWealthToActivate) {
        this.selecteWealthToActivate = selecteWealthToActivate;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSearchedCustomerIc() {
        return searchedCustomerIc;
    }

    public void setSearchedCustomerIc(String searchedCustomerIc) {
        this.searchedCustomerIc = searchedCustomerIc;
    }

    public List<Portfolio> getPendingActivatedPlans() {
        return pendingActivatedPlans;
    }

    public void setPendingActivatedPlans(List<Portfolio> pendingActivatedPlans) {
        this.pendingActivatedPlans = pendingActivatedPlans;
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

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public AccountManagementSessionBeanLocal getAmsbl() {
        return amsbl;
    }

    public void setAmsbl(AccountManagementSessionBeanLocal amsbl) {
        this.amsbl = amsbl;
    }

    public staffLogInManagedBean getSlimb() {
        return slimb;
    }

    public void setSlimb(staffLogInManagedBean slimb) {
        this.slimb = slimb;
    }

    public Customer getSearchedCustomer() {
        return searchedCustomer;
    }

    public void setSearchedCustomer(Customer searchedCustomer) {
        this.searchedCustomer = searchedCustomer;
    }

    public LoanApplicationSessionBeanLocal getLasbl() {
        return lasbl;
    }

    public void setLasbl(LoanApplicationSessionBeanLocal lasbl) {
        this.lasbl = lasbl;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Long getSearchedCustomerId() {
        return searchedCustomerId;
    }

    public void setSearchedCustomerId(Long searchedCustomerId) {
        this.searchedCustomerId = searchedCustomerId;
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

    public List<Portfolio> getPendingApprovedTailoredPlans() {
        return pendingApprovedTailoredPlans;
    }

    public void setPendingApprovedTailoredPlans(List<Portfolio> pendingApprovedTailoredPlans) {
        this.pendingApprovedTailoredPlans = pendingApprovedTailoredPlans;
    }

    public List<Portfolio> getOneCustomerAllPortfolios() {
        return oneCustomerAllPortfolios;
    }

    public void setOneCustomerAllPortfolios(List<Portfolio> oneCustomerAllPortfolios) {
        this.oneCustomerAllPortfolios = oneCustomerAllPortfolios;
    }

    public List<DiscretionaryAccount> getAllWealthAccounts() {
        return allWealthAccounts;
    }

    public void setAllWealthAccounts(List<DiscretionaryAccount> allWealthAccounts) {
        this.allWealthAccounts = allWealthAccounts;
    }

    public DiscretionaryAccount getSelectedWealth() {
        return selectedWealth;
    }

    public void setSelectedWealth(DiscretionaryAccount selectedWealth) {
        this.selectedWealth = selectedWealth;
    }

    public List<DiscretionaryAccount> getPendingDisAccount() {
        return pendingDisAccount;
    }

    public void setPendingDisAccount(List<DiscretionaryAccount> pendingDisAccount) {
        this.pendingDisAccount = pendingDisAccount;
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

    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(BigDecimal initialAmount) {
        this.initialAmount = initialAmount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public List<SavingAccount> getOneCustomerAllSavingAccts() {
        return oneCustomerAllSavingAccts;
    }

    public void setOneCustomerAllSavingAccts(List<SavingAccount> oneCustomerAllSavingAccts) {
        this.oneCustomerAllSavingAccts = oneCustomerAllSavingAccts;
    }

    public SavingAccount getSelectedSavingAccout() {
        return selectedSavingAccout;
    }

    public void setSelectedSavingAccout(SavingAccount selectedSavingAccout) {
        this.selectedSavingAccout = selectedSavingAccout;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public WealthSessionBeanLocal getWsbl() {
        return wsbl;
    }

    public void setWsbl(WealthSessionBeanLocal wsbl) {
        this.wsbl = wsbl;
    }

    public WealthManagementSessionBeanLocal getWmsbl() {
        return wmsbl;
    }

    public void setWmsbl(WealthManagementSessionBeanLocal wmsbl) {
        this.wmsbl = wmsbl;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> getAvailableRoleNames() {
        return availableRoleNames;
    }

    public void setAvailableRoleNames(Map<String, String> availableRoleNames) {
        this.availableRoleNames = availableRoleNames;
    }

    public Map<String, String> getAvailableStaffs() {
        return availableStaffs;
    }

    public void setAvailableStaffs(Map<String, String> availableStaffs) {
        this.availableStaffs = availableStaffs;
    }

    public String getSelectedRoleName() {
        return selectedRoleName;
    }

    public void setSelectedRoleName(String selectedRoleName) {
        this.selectedRoleName = selectedRoleName;
    }

    public List<Staff> getRelatedStaffs() {
        return relatedStaffs;
    }

    public void setRelatedStaffs(List<Staff> relatedStaffs) {
        this.relatedStaffs = relatedStaffs;
    }

    public String getSelectedStaffName() {
        return selectedStaffName;
    }

    public void setSelectedStaffName(String selectedStaffName) {
        this.selectedStaffName = selectedStaffName;
    }

    public List getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List roleNames) {
        this.roleNames = roleNames;
    }

    public StaffManagementSessionBeanLocal getSmsbl() {
        return smsbl;
    }

    public void setSmsbl(StaffManagementSessionBeanLocal smsbl) {
        this.smsbl = smsbl;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

}
