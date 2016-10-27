/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffLoanManagement;

import CommonEntity.Customer;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.LoanTermInvalidException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.Loan;
import LoanEntity.LoanType;
import LoanEntity.Session.LoanApplicationSessionBeanLocal;
import LoanEntity.Session.LoanManagementSessionBeanLocal;
import StaffManagement.staffLogInManagedBean;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.primefaces.event.SelectEvent;

/**
 *
 * @author apple
 */
@Named(value = "staffLoanManagedBean")
@SessionScoped
public class StaffLoanManagedBean implements Serializable {

    @EJB
    LoanManagementSessionBeanLocal lmsbl;
    @EJB
    LoanApplicationSessionBeanLocal lasbl;

    @Inject
    private staffLogInManagedBean staffLogInManagedBean;

    /**
     * Creates a new instance of StaffLoanManagedBean
     */
    public StaffLoanManagedBean() {
    }
    private String searchedCustomerIc;
    private String customerIc;
    private BigDecimal principal;
    private BigDecimal monthlyIncome;
    private BigDecimal downpayment;
    private Integer loanTerm;
    private Date startDate;
    private List loanTypes;
    private String loanTypeName;
    private String loanName;
    private String customerName;
    private String customerGender;
    private Date customerDateOfBirth;
    private String customerAddress;
    private String customerEmail;
    private String customerPhoneNumber;
    private LoanType loanToCal;
    private Customer searchedCustomer;
    private String customerOccupation;
    private String customerFamilyInfo;
    private Customer customer;
    private Long loanTypeId;
    private Long customerId;
    private Long loanId;
    private Long staffId;
    private Loan selectedLoan;
    private BigDecimal loanAmount;
    private Integer calLoanTerm;
    private List<Loan> pendingLoans;
    private List<Loan> oneCustomerAllLoans;
    private List<LoanType> allLoanTypes;
    private String detail;
    private BigDecimal monthlyRepayment;
    private Date loanDate;
    private Double interest1;

    private Double interest2;
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

    private Map<String, String> loanCategories;
    private Map<String, String> loanNames;

    private String password;
    private LoanType selectedLoanType;
    private double risk;
    private String path;

    @PostConstruct
    public void init() {
        customer = new Customer();
        searchedCustomer = new Customer();
        selectedLoan = new Loan();
        loanToCal = new LoanType();
        selectedLoanType = new LoanType();
        staffId = staffLogInManagedBean.getStaffId();
        monthlyRepayment = new BigDecimal("0.00000");
        monthlyRepayment.setScale(2, BigDecimal.ROUND_DOWN);
        pendingLoans = new ArrayList<>();
        oneCustomerAllLoans = new ArrayList<>();
        allLoanTypes = new ArrayList<>();
        loanCategories = new HashMap<String, String>();
        loanNames = new HashMap<String, String>();
        loanCategories.put("Home", "Home");
        loanCategories.put("Car", "Car");
        loanCategories.put("Education", "Education");

        Map<String, String> map = new HashMap<String, String>();
        map.put("SIBOR Package", "SIBOR Package");
        map.put("Fixed Interest Package", "Fixed Interest Package");
        data.put("Home", map);

        map = new HashMap<String, String>();
        map.put("Car Loan", "Car Loan");

        data.put("Car", map);

        map = new HashMap<String, String>();
        map.put("NUS Education Loan", "NUS Education Loan");

        data.put("Education", map);
    }

    public void onLoanChange() {
        if (loanTypeName != null && !loanTypeName.equals("")) {
            loanNames = data.get(loanTypeName);

        } else {
            loanNames = new HashMap<String, String>();
        }
    }

    public void searchCustomerId(ActionEvent event) throws UserNotExistException, UserNotActivatedException {
        try {
                        System.out.println("searched customer is "+searchedCustomerIc);
            searchedCustomer = lasbl.searchCustomer(searchedCustomerIc);
        } catch (UserNotExistException | UserNotActivatedException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }

    public void staffViewLoanTypeList(ActionEvent event) throws IOException {
        allLoanTypes = lmsbl.viewLoanTypeList();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/staffDisplayAllLoanTypes.xhtml");
    }

   
    public void staffUpdateLoanType(RowEditEvent event) {
        selectedLoanType = (LoanType) (event.getObject());
        System.out.println("********Loan Type selected to be updated: " + selectedLoanType.getName());
        if(selectedLoanType.getName().equals("SIBOR Package")){
        interest1 = selectedLoanType.getSIBOR();
        interest2 = selectedLoanType.getSIBORrate1();
        }else if(selectedLoanType.getName().equals("NUS Education Loan")){
        interest1=selectedLoanType.getEducationRate();
        interest2=null;
        }else if(selectedLoanType.getName().equals("Fixed Interest Package")){
        interest1=selectedLoanType.getFixedRate();
        interest2=selectedLoanType.getFixedRate2();
        }else{
         interest1=selectedLoanType.getInterestRate();
        interest2=null;
        }
        //   selectedLoanType = (LoanType) event.getComponent().getAttributes().get("selectedLoanType");
        allLoanTypes = lmsbl.updateLoanType(selectedLoanType.getId(), interest1, interest2);
         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message","Interest Rate for "+selectedLoanType.getName()+" has been changed!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        
    }
public void goToDisplayLoanTypes(ActionEvent event) throws IOException{
             FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/staffDisplayLoanTypes.xhtml");
   
}   
    public void goToLoanPage(ActionEvent event) throws IOException {
        loanToCal = lasbl.findTypeByName(loanName);
        if (loanName.equals("Fixed Interest Package")) {
            interest1 = loanToCal.getFixedRate();
            interest2 = loanToCal.getFixedRate2();
        } else if (loanName.equals("SIBOR Package")) {
            interest1 = loanToCal.getSIBOR();
            interest2 = loanToCal.getSIBORrate1();
        } else if (loanName.equals("NUS Education Loan")) {
            interest1 = loanToCal.getEducationRate();
            interest2 = null;
        } else {
            interest1 = loanToCal.getInterestRate();
            interest2 = null;
        }
        if (customer.getId() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/staffCreateLoanAccountForExistCustomer.xhtml");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/staffApplyLoanForCustomer.xhtml");
        }

    }

    public void goToUpdateCustomerLoanPage(ActionEvent event) throws IOException {

        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/staffUpdateLoanForCustomer.xhtml");

    }

    public void goToUpdateCustomerLoanPage1() throws IOException, UserNotExistException, UserNotActivatedException, ListEmptyException {
        try {
            System.out.println("*****Searched Customer IC: " + customerIc);
            oneCustomerAllLoans = lmsbl.searchLoan(customerIc);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/viewCustomerLoan.xhtml");
        } catch (IOException | UserNotExistException | UserNotActivatedException | ListEmptyException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
public void openDocs(ActionEvent event) {

           selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");

           System.out.println("********Selected customer to view documents is " + selectedLoan.getCustomer().getIc());
        //File openFile = new File(selectedCustomer.getFileDestination());
//         if(!Desktop.isDesktopSupported()){
//            System.out.println("Desktop is not supported");
//            return;
//        }
//        
//        Desktop desktop = Desktop.getDesktop();
//        if(openFile.exists()) desktop.open(openFile);
//        
        
             path = "http://localhost:8080/"+selectedLoan.getCustomer().getFileDestination();
                    System.out.println("File Path From customer is "+selectedLoan.getCustomer().getFileDestination());
        System.out.println("File Path is "+path);

//            Runtime runtime = Runtime.getRuntime();
//            runtime.exec("explorer.exe C:\\Users\\apple\\AppData\\Roaming\\NetBeans\\8.0.2\\config\\GF_4.1\\domain1\\docroot\\"+path);
//            System.out.println("open");
   
       }

    public void goToApplyLoanForNewCustomerPage(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/staffCreateLoanAccountForNewCustomer.xhtml");

    }

    public void goToActivateCustomerLoanPage() throws IOException, UserNotExistException, ListEmptyException, UserNotActivatedException {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/staffActivateLoanForCustomer.xhtml");
            oneCustomerAllLoans = lmsbl.searchLoan(customerIc);
        } catch (IOException | UserNotExistException | ListEmptyException | UserNotActivatedException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public BigDecimal calculateMonthlyPayment(ActionEvent event) {
        System.out.println("*********Caculator: loan name " + loanName);

        if (loanName.equals("NUS Education Loan")) {
            interest2 = 0.00;
        } else if (loanName.equals("Car Loan")) {
            interest2 = 0.00;
        }
        System.out.println("*********Caculator: loan amount " + loanAmount);
        System.out.println("*********Caculator: loan term " + calLoanTerm);
        System.out.println("*********Caculator: interest1 " + interest1);
        System.out.println("*********Caculator: interest2 " + interest2);

        monthlyRepayment = lasbl.fixedCalculator(loanAmount, calLoanTerm, interest1, interest2);
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your Monthly Payment: " + monthlyRepayment);
//        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return monthlyRepayment;
    }

    public void staffCreateLoanAcct(ActionEvent event) throws UserExistException, EmailNotSendException, IOException {
        try {
            customer = lasbl.tellerCreateLoanAccount(customerIc, customerName, customerGender, customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo, password);
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan Account has been successfully created! Detailed informaiton has been sent to your email!");
//            RequestContext.getCurrentInstance().showMessageInDialog(message);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/staffDisplayLoanTypes.xhtml");

        } catch (UserExistException | EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public String displayLoanPackageDetail(String loanName) {
        System.out.println("****************Selected loan name to display " + loanName);

        detail = lasbl.displayPackageDetail(loanName);
        System.out.println("Loan package detailed " + detail);

        return detail;
    }

    public void staffApplyLoanForCustomer(ActionEvent event) throws LoanTermInvalidException {

        //System.out.println("*************Customer create loan details - customerId " + customer.getIc());
        System.out.println("*************Customer create loan details - loanTypeName " + loanTypeName);
        System.out.println("*************Customer create loan details - loanName " + loanName);
        System.out.println("*************Customer create loan details - principal " + principal);
        System.out.println("*************Customer create loan details - downpayment " + downpayment);
        System.out.println("*************Customer create loan details - loanTerm " + loanTerm);
        System.out.println("*************Customer create loan details - monthly income " + monthlyIncome);

        try {

            loanTypeId = lasbl.findTypeIdByName(loanName);
            if (customer.getId() == null) {
                System.out.println("Create loan account for EXISTING customer");
                searchedCustomer.getId();
                customerId=lasbl.StaffCreateLoanAccountExisting(staffId, customerId, monthlyIncome, loanTypeId, principal, downpayment, loanTerm);
                 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message","Loan Account has been created for customer "+searchedCustomer.getIc()+"!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            } else {
                System.out.println("Create loan account for NEW customer customerID " + customer.getId());

                lasbl.StaffCreateLoanAccount(staffId, customer.getId(), monthlyIncome, loanTypeId, principal, downpayment, loanTerm);
 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan Account has been created for customer "+customer.getIc()+"!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (LoanTermInvalidException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }
    public void staffViewPendingLoans(ActionEvent event) throws IOException {
        System.out.println("**** go to view pending loans alr!");
        pendingLoans = lmsbl.staffViewPendingLoans();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/LoanManagement/viewPendingLoans.xhtml");

    }

    public void staffViewCustomer(SelectEvent event) {
        selectedLoan=(Loan) event.getObject();
        System.out.println("*****Selected Loan to view customer is "+selectedLoan.getLoanType().getName());
       // selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");

    }

    public void staffRejectPending(ActionEvent event) throws EmailNotSendException {
        try {
        //    selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");
            loanId = selectedLoan.getId();
            System.out.println("*************Selected pending loan to view - loan ID is " + selectedLoan.getId());
            System.out.println("**************Selected pending loan to view - customer ic" + selectedLoan.getCustomer().getName());
            pendingLoans = lmsbl.staffRejectLoans(staffId, loanId);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan has been rejected for customer " + selectedLoan.getCustomer().getName() + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public void staffApprovePending(ActionEvent event) throws EmailNotSendException {
        try {
            System.out.println("go into approve customer!");
            //    selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");
            loanId = selectedLoan.getId();
            System.out.println("*************Selected pending loan to view - loan ID is " + selectedLoan.getId());
            System.out.println("**************Selected pending loan to view - customer ic" + selectedLoan.getCustomer().getName());
            pendingLoans = lmsbl.staffApproveLoans(staffId, loanId);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan has been approved for customer " + selectedLoan.getCustomer().getName() + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public void staffUpdateLoan(ActionEvent event) throws EmailNotSendException {
        try {
            //  selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");
            loanId = selectedLoan.getId();
            System.out.println("*************Selected loan to update - loan ID is " + selectedLoan.getId());
            System.out.println("*************Selected loan to update - loan downpayment is " + selectedLoan.getDownpayment());
            System.out.println("*************Selected loan to update - loan loanTerm is " + selectedLoan.getLoanTerm());

            selectedLoan = lmsbl.staffUpdateLoan(staffId, loanId, selectedLoan.getPrincipal(), selectedLoan.getDownpayment(), selectedLoan.getLoanTerm(), startDate);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan has been updated successfully!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }

    public void selecteLoanToActivate(ActionEvent event) {
        selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");

        System.out.println("Selected loan to activate is " + selectedLoan.getId());

        loanId = selectedLoan.getId();
    }

    public List<Loan> staffActivateLoan(ActionEvent event) throws UserNotExistException, UserNotActivatedException, ListEmptyException {
        try {
           // selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");
            // selectedLoan= (Loan) event.getObject();
            System.out.println("*****Loan ID to activate loan is " + loanId);
            lmsbl.staffActivateLoan(loanId, loanDate);
            System.out.println("*****Customer IC to activate loan is " + customerIc);
            oneCustomerAllLoans = lmsbl.searchLoan(customerIc);
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan has been activated successfully!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (UserNotExistException | ListEmptyException | UserNotActivatedException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        return oneCustomerAllLoans;

    }
public void calculateRisk(ActionEvent event){
    selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");
System.out.println("************Selected customer ID to calculate risk is "+selectedLoan.getCustomer().getId());
System.out.println("************Selected loan ID to calculate risk is "+selectedLoan.getId());

  risk=  lmsbl.calculateRisk(selectedLoan.getCustomer().getId(), selectedLoan.getId());

}
    public List<LoanType> getAllLoanTypes() {
        return allLoanTypes;
    }

    public void setAllLoanTypes(List<LoanType> allLoanTypes) {
        this.allLoanTypes = allLoanTypes;
    }

    public LoanType getSelectedLoanType() {
        return selectedLoanType;
    }

    public void setSelectedLoanType(LoanType selectedLoanType) {
        this.selectedLoanType = selectedLoanType;
    }

    public LoanManagementSessionBeanLocal getLmsbl() {
        return lmsbl;
    }

    public void setLmsbl(LoanManagementSessionBeanLocal lmsbl) {
        this.lmsbl = lmsbl;
    }

    public staffLogInManagedBean getStaffLogInManagedBean() {
        return staffLogInManagedBean;
    }

    public void setStaffLogInManagedBean(staffLogInManagedBean staffLogInManagedBean) {
        this.staffLogInManagedBean = staffLogInManagedBean;
    }

    public String getCustomerIc() {
        return customerIc;
    }

    public void setCustomerIc(String customerIc) {
        this.customerIc = customerIc;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getDownpayment() {
        return downpayment;
    }

    public void setDownpayment(BigDecimal downpayment) {
        this.downpayment = downpayment;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List getLoanTypes() {
        return loanTypes;
    }

    public void setLoanTypes(List loanTypes) {
        this.loanTypes = loanTypes;
    }

    public String getLoanTypeName() {
        return loanTypeName;
    }

    public void setLoanTypeName(String loanTypeName) {
        this.loanTypeName = loanTypeName;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getLoanTypeId() {
        return loanTypeId;
    }

    public void setLoanTypeId(Long loanTypeId) {
        this.loanTypeId = loanTypeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Loan getSelectedLoan() {
        return selectedLoan;
    }

    public void setSelectedLoan(Loan selectedLoan) {
        this.selectedLoan = selectedLoan;
    }

    public List<Loan> getPendingLoans() {
        return pendingLoans;
    }

    public void setPendingLoans(List<Loan> pendingLoans) {
        this.pendingLoans = pendingLoans;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public LoanType getLoanToCal() {
        return loanToCal;
    }

    public void setLoanToCal(LoanType loanToCal) {
        this.loanToCal = loanToCal;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getMonthlyRepayment() {
        return monthlyRepayment;
    }

    public void setMonthlyRepayment(BigDecimal monthlyRepayment) {
        this.monthlyRepayment = monthlyRepayment;
    }

    public Double getInterest1() {
        return interest1;
    }

    public void setInterest1(Double interest1) {
        this.interest1 = interest1;
    }

    public Double getInterest2() {
        return interest2;
    }

    public void setInterest2(Double interest2) {
        this.interest2 = interest2;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public Map<String, String> getLoanCategories() {
        return loanCategories;
    }

    public void setLoanCategories(Map<String, String> loanCategories) {
        this.loanCategories = loanCategories;
    }

    public Map<String, String> getLoanNames() {
        return loanNames;
    }

    public void setLoanNames(Map<String, String> loanNames) {
        this.loanNames = loanNames;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoanApplicationSessionBeanLocal getLasbl() {
        return lasbl;
    }

    public void setLasbl(LoanApplicationSessionBeanLocal lasbl) {
        this.lasbl = lasbl;
    }

    public Customer getSearchedCustomer() {
        return searchedCustomer;
    }

    public void setSearchedCustomer(Customer searchedCustomer) {
        this.searchedCustomer = searchedCustomer;
    }

    public List<Loan> getOneCustomerAllLoans() {
        return oneCustomerAllLoans;
    }

    public void setOneCustomerAllLoans(List<Loan> oneCustomerAllLoans) {
        this.oneCustomerAllLoans = oneCustomerAllLoans;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getCalLoanTerm() {
        return calLoanTerm;
    }

    public void setCalLoanTerm(Integer calLoanTerm) {
        this.calLoanTerm = calLoanTerm;
    }

    public String getSearchedCustomerIc() {
        return searchedCustomerIc;
    }

    public void setSearchedCustomerIc(String searchedCustomerIc) {
        this.searchedCustomerIc = searchedCustomerIc;
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    

}
