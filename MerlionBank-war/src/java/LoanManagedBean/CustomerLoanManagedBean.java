/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import CommonManagedBean.LogInManagedBean;
import Exception.EmailNotSendException;
import Exception.UserExistException;
import LoanEntity.Loan;
import LoanEntity.Session.LoanSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author apple
 */
@Named(value = "customerLoanManagedBean")
@SessionScoped
public class CustomerLoanManagedBean implements Serializable {

    @EJB
    private LoanSessionBeanLocal lsbl;
    @EJB
    private AccountManagementSessionBeanLocal amsbl;
    @Inject
    private LogInManagedBean logInManagedBean;
    /**
     * Creates a new instance of CustomerLoanManagedBean
     */
    private Customer customer;
    private Long loanTypeId;
    private Long customerId;
    private Long loanId;

    private String customerIc;
    private BigDecimal principal;
    private BigDecimal downpayment;
    private Integer loanTerm;
    private Date startDate = null;
    private String loanTypeName;
    private String loanName;
    private String customerName;
    private String customerGender;
    private Date customerDateOfBirth;
    private String customerAddress;
    private String customerEmail;
    private String customerPhoneNumber;

    private String customerOccupation;
    private String customerFamilyInfo;
    private String password;
    private List<Loan> oneCustomerAllLoans;
    private Loan selectedLoan;
    
    private BigDecimal calPrincipal;
     private BigDecimal calDownpayment;

    public BigDecimal getCalPrincipal() {
        return calPrincipal;
    }

    public void setCalPrincipal(BigDecimal calPrincipal) {
        this.calPrincipal = calPrincipal;
    }

    public BigDecimal getCalDownpayment() {
        return calDownpayment;
    }

    public void setCalDownpayment(BigDecimal calDownpayment) {
        this.calDownpayment = calDownpayment;
    }

    public Integer getCalLoanTerm() {
        return calLoanTerm;
    }

    public void setCalLoanTerm(Integer calLoanTerm) {
        this.calLoanTerm = calLoanTerm;
    }
       private Integer calLoanTerm;
       private BigDecimal monthlyRepayment;

    public BigDecimal getMonthlyRepayment() {
        return monthlyRepayment;
    }

    public void setMonthlyRepayment(BigDecimal monthlyRepayment) {
        this.monthlyRepayment = monthlyRepayment;
    }
     

    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

    private Map<String, String> loanCategories;
    private Map<String, String> loanNames;

    public CustomerLoanManagedBean() {
    }

    @PostConstruct
    public void init() {
        customer = new Customer();
       selectedLoan = new Loan();
       oneCustomerAllLoans = new ArrayList<>();
       loanCategories = new HashMap<String, String>();
       loanNames = new HashMap<String, String>(); 
       loanCategories.put("Home Loan", "Home Loan");
       loanCategories.put("Car Loan", "Car Loan");

        Map<String, String> map = new HashMap<String, String>();
        map.put("SIBOR Package", "SIBOR Package");
        map.put("Fixed Interest Package", "Fixed Interest Package");
        data.put("Home Loan", map);

        map = new HashMap<String, String>();
        map.put("Car Loan", "Car Loan");

       data.put("Car Loan", map);

       customerId = logInManagedBean.getCustomerId();

    }

    public void goToLoanPage(ActionEvent event) throws IOException {

        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/LoanManagement/customerViewMessage.xhtml");

    }
    public void calculateMonthlyPayment(ActionEvent event){
        monthlyRepayment = lsbl.calcultateMonthlyPayment(principal, downpayment, loanTerm, loanTypeId);
    }
public void onCountryChange() {
        if(loanTypeName !=null && !loanTypeName.equals(""))
            loanNames = data.get(loanTypeName);
        else
            loanNames = new HashMap<String, String>();
    }
    public void customerCreateLoanAcct(ActionEvent event) throws UserExistException, EmailNotSendException {
        try {
            customer= amsbl.createFixedDepositAccount(customerIc, customerName,customerGender, customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan Account has been successfully created! Detailed informaiton has been sent to your email!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (UserExistException | EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void customerApplyLoan(ActionEvent event) {

        System.out.println("*************Customer create home loan details - customerId " + customerId);
        System.out.println("*************Customer create home loan details - loanTypeName " + loanTypeName);
        System.out.println("*************Customer create home loan details - loanName " + loanName);
        System.out.println("*************Customer create home loan details - principal " + principal);
        System.out.println("*************Customer create home loan details - downpayment " + downpayment);
        System.out.println("*************Customer create home loan details - loanTerm " +loanTerm);
        System.out.println("*************Customer create home loan details - startDate " + startDate);

        if (loanTypeName.equals("Home Loan")) {
           lsbl.createHomeLoan(customer, loanName, principal, downpayment,loanTerm, startDate);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan account has been successfully created!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } else if (getLoanTypeName().equals("Car Loan")) {
           lsbl.createCarLoan(customer, loanName, principal, downpayment,loanTerm, startDate);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan account has been successfully created!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public List<Loan> customerViewAllLoans(ActionEvent event) {
        oneCustomerAllLoans=lsbl.customerViewListOfLoan(customerId);
        return getOneCustomerAllLoans();
    }

    public void customerViewOneLoan(ActionEvent event) {
        selectedLoan =(Loan) event.getComponent().getAttributes().get("selectedLoan");
        System.out.println("*************Selected loan to view - loan ID is " + getSelectedLoan().getId());
       loanId = selectedLoan.getId();
        lsbl.customerViewLoan(loanId);

    }

    public void customerUpdateLoan(RowEditEvent  event) {
              selectedLoan =(Loan) event.getComponent().getAttributes().get("selectedLoan");
        loanId = selectedLoan.getId();
        System.out.println("*************Selected loan to update - loan ID is " + loanId);
        System.out.println("*************Selected loan to update - loan downpayment is " + downpayment);
        System.out.println("*************Selected loan to update - loan loanTerm is " + loanTerm);
        System.out.println("*************Selected loan to update - loan startDate is " + startDate);

        oneCustomerAllLoans=lsbl.customerUpdateLoan(customerId, loanId, downpayment, loanTerm, startDate);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan has been updated successfully!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public void customerCancelLoan(Long loanId) {
     //   selectedLoan =(Loan) event.getComponent().getAttributes().get("selectedLoan");
        System.out.println("*************Selected loan to cancel - loan ID is " + loanId);
     // loanId = selectedLoan.getId();
        oneCustomerAllLoans=lsbl.customerCancelLoan(customerId, loanId);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan has been canceled successfully!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    public void customerAcceptLoan(Long loanId) throws EmailNotSendException {
        try {
          //    selectedLoan =(Loan) event.getComponent().getAttributes().get("selectedLoan");

        System.out.println("*************Selected loan to accept - loan ID is " + loanId);
     // loanId = selectedLoan.getId();
        oneCustomerAllLoans=lsbl.customerAcceptLoan(customerId, loanId);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully accepted the loan!  Detailed informaiton has been sent to your email!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }

    public LoanSessionBeanLocal getLsbl() {
        return lsbl;
    }

    public void setLsbl(LoanSessionBeanLocal lsbl) {
        this.lsbl = lsbl;
    }

    public AccountManagementSessionBeanLocal getAmsbl() {
        return amsbl;
    }

    public void setAmsbl(AccountManagementSessionBeanLocal amsbl) {
        this.amsbl = amsbl;
    }

    public LogInManagedBean getLogInManagedBean() {
        return logInManagedBean;
    }

    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
        this.logInManagedBean = logInManagedBean;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Loan> getOneCustomerAllLoans() {
        return oneCustomerAllLoans;
    }

    public void setOneCustomerAllLoans(List<Loan> oneCustomerAllLoans) {
        this.oneCustomerAllLoans = oneCustomerAllLoans;
    }

    public Loan getSelectedLoan() {
        return selectedLoan;
    }

    public void setSelectedLoan(Loan selectedLoan) {
        this.selectedLoan = selectedLoan;
    }

    /**
     * @return the loanName
     */
    public String getLoanName() {
        return loanName;
    }

    /**
     * @param loanName the loanName to set
     */
    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    /**
     * @return the data
     */
    public Map<String, Map<String, String>> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    /**
     * @return the loanCategories
     */
    public Map<String, String> getLoanCategories() {
        return loanCategories;
    }

    /**
     * @param loanCategories the loanCategories to set
     */
    public void setLoanCategories(Map<String, String> loanCategories) {
        this.loanCategories = loanCategories;
    }

    /**
     * @return the loanNames
     */
    public Map<String, String> getLoanNames() {
        return loanNames;
    }

    /**
     * @param loanNames the loanNames to set
     */
    public void setLoanNames(Map<String, String> loanNames) {
        this.loanNames = loanNames;
    }
}