/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffLoanManagement;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.UserExistException;
import LoanEntity.Loan;
import LoanEntity.Session.LoanManagementSessionBeanLocal;
import StaffManagement.staffLogInManagedBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

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
    AccountManagementSessionBeanLocal amsbl;
    @Inject
    private staffLogInManagedBean staffLogInManagedBean;

    /**
     * Creates a new instance of StaffLoanManagedBean
     */
    public StaffLoanManagedBean() {
    }
    private String customerIc;
    private BigDecimal principal;

    public LoanManagementSessionBeanLocal getLmsbl() {
        return lmsbl;
    }

    public void setLmsbl(LoanManagementSessionBeanLocal lmsbl) {
        this.lmsbl = lmsbl;
    }

    public AccountManagementSessionBeanLocal getAmsbl() {
        return amsbl;
    }

    public void setAmsbl(AccountManagementSessionBeanLocal amsbl) {
        this.amsbl = amsbl;
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
    private BigDecimal downpayment;
    private Integer loanTerm;
    private Date startDate;
    private List loanTypes;
    private String loanTypeName;
    private String customerName;
    private String customerGender;
    private Date customerDateOfBirth;
    private String customerAddress;
    private String customerEmail;
    private String customerPhoneNumber;

    private String customerOccupation;
    private String customerFamilyInfo;
    private Customer customer;
    private Long loanTypeId;
    private Long customerId;
    private Long loanId;
    private Long staffId;
    private Loan selectedLoan;
    private List<Loan> pendingLoans;

    @PostConstruct
    public void init() {
        customer = new Customer();
        selectedLoan = new Loan();
        staffId = staffLogInManagedBean.getStaffId();
        pendingLoans = new ArrayList<>();
    }

    public void staffViewPendingLoans(ActionEvent event) {
        System.out.println("**** go to view pending loans alr!");
        pendingLoans = lmsbl.staffViewPendingLoans();

    }
    
    public void staffRejectPending(ActionEvent event) throws EmailNotSendException {
        try {
            selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");
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
            selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");
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
     public void satffUpdateLoan(RowEditEvent event) throws EmailNotSendException {
         try{
        selectedLoan = (Loan) event.getComponent().getAttributes().get("selectedLoan");
        loanId = selectedLoan.getId();
        System.out.println("*************Selected loan to update - loan ID is " + selectedLoan.getId());
        System.out.println("*************Selected loan to update - loan downpayment is " + downpayment);
        System.out.println("*************Selected loan to update - loan loanTerm is " + loanTerm);
        System.out.println("*************Selected loan to update - loan startDate is " + startDate);

        pendingLoans = lmsbl.staffUpdateLoan(staffId, loanId, downpayment, loanTerm, startDate);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Loan has been updated successfully!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }catch(EmailNotSendException ex){
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        
    }
}
}