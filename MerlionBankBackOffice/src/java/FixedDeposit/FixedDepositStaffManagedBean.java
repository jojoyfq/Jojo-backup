/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FixedDeposit;

import CommonEntity.Customer;
import CommonEntity.Staff;
import DepositEntity.FixedDepositAccount;
import DepositEntity.FixedDepositRate;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
import StaffManagement.staffLogInManagedBean;
import TellerManagedBean.ServiceCustomerManagedBean;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
/**
 *
 * @author ruijia
 */
@Named(value = "fixedDepositStaffManagedBean")
@SessionScoped
public class FixedDepositStaffManagedBean implements Serializable {

    @EJB
    FixedDepositAccountSessionBeanLocal fda;
    private Customer customer;
    private Long customerId;
    private Date startDate;
    private Date endDate;
    private String amountString;
    private BigDecimal amountBD;
    private String duration;
    private String startDateString;
    private String endDateString;
    private Long accountNumber;
    private List<FixedDepositAccount> fixedDepositAccounts;
    private List<FixedDepositRate> fixedDepositRates;
    private FixedDepositRate fixedRateSelected;
    private Double newInterestRate;
    
    public List<FixedDepositRate> getFixedDepositRates() {
        return fixedDepositRates;
    }

    public void setFixedDepositRates(List<FixedDepositRate> fixedDepositRates) {
        this.fixedDepositRates = fixedDepositRates;
    }
    private List<FixedDepositAccount> withdrawableFixedDeposit;
    private FixedDepositAccount selectedFixedDeposit;
    private String withdrawType;
    private BigDecimal interest;
    private BigDecimal total;
    private Staff staff;
    private Long staffId;
    
    @Inject
    private ServiceCustomerManagedBean serviceCustomerManagedBean;

    @Inject
    private staffLogInManagedBean staffLogInManagedBean;

    /**
     * Creates a new instance of FixedDepositStaffManagedBean
     */
    @PostConstruct
    public void init() {
            System.out.print("inside the init method");
            //serviceCustomerManagedBean.init();
            staff = staffLogInManagedBean.getStaff();
            staffId = staffLogInManagedBean.getStaffId();
            customerId = serviceCustomerManagedBean.getCustomer().getId();
            withdrawableFixedDeposit = fda.getWithdrawableAccount(customerId);
            customer = serviceCustomerManagedBean.getCustomer();
            System.out.print("************************");
            System.out.print(customerId);
            fixedDepositAccounts = fda.getFixedDepositAccounts(customerId);

    }

    public FixedDepositStaffManagedBean() {
    }

    public void createFixedDepositAcct(ActionEvent event) throws IOException {
        customerId = serviceCustomerManagedBean.getCustomer().getId();
        System.out.print(customerId);
        System.out.print(amountString);
        System.out.print(duration);
        if (amountString != null) {
            amountBD = new BigDecimal(amountString);

            //teller created acct should start on the day
            Calendar calS = GregorianCalendar.getInstance();
            startDate = calS.getTime();
            //compute end date
            Calendar calE = GregorianCalendar.getInstance();
            calE.setTime(getStartDate());
            if (getDuration().equalsIgnoreCase("3")) {
                calE.add(GregorianCalendar.MONTH, 3);
            } else if (getDuration().equalsIgnoreCase("6")) {
                calE.add(GregorianCalendar.MONTH, 6);
            } else if (getDuration().equalsIgnoreCase("12")) {
                calE.add(GregorianCalendar.MONTH, 12);
            } else {
                calE.add(GregorianCalendar.MONTH, 24);
            }
            setEndDate(calE.getTime());

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            endDateString = df.format(endDate);
            startDateString = df.format(startDate);
            accountNumber = fda.createFixedDepositCounter(customerId, amountBD, startDate, endDate, duration);
            this.updateList(customerId);
            String description = "Staff " +staffLogInManagedBean.getStaffIc()+" create fixed deposit "+accountNumber+" for customer "+customer.getIc();
            staffId = staffLogInManagedBean.getStaffId();
            fda.logStaffAction(description, customerId, staffId);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("/MerlionBankBackOffice/FixedDepositManagement/createFixedDepositSuccess.xhtml");
             
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please enter amount");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    public void goToHomePage(ActionEvent event)throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/StaffDashboard.xhtml");
    }
    
    public void withdraw(ActionEvent event)throws IOException{
        if(selectedFixedDeposit != null){
      DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
      String todayDate = df.format(Calendar.getInstance().getTime());
      String selectedEndDateStr = df.format(selectedFixedDeposit.getEndDate());
      if(todayDate.equals(selectedEndDateStr)){
          withdrawType = "Normal Withdraw";      
          List<BigDecimal> amountsToDisplay = fda.normalWithdrawCounter(selectedFixedDeposit.getAccountNumber());
          amountBD = amountsToDisplay.get(0);
          interest = amountsToDisplay.get(1).setScale(4, RoundingMode.HALF_UP);
          total = amountsToDisplay.get(2).setScale(4, RoundingMode.HALF_UP);
         String description = "Staff "+staffLogInManagedBean.getStaffIc()+" perform normal withdraw of fixed deposit "+selectedFixedDeposit.getAccountNumber()+" for customer "+customer.getIc();
          fda.logStaffAction(description, customerId, staffId);
      }else{
          withdrawType = "Premature Withdraw";
          List<BigDecimal> amountsToDisplay  = fda.earlyWithdrawCounter(selectedFixedDeposit.getAccountNumber());
          amountBD = amountsToDisplay.get(0);
          interest = amountsToDisplay.get(1).setScale(4, RoundingMode.HALF_UP);
          total = amountsToDisplay.get(2).setScale(4, RoundingMode.HALF_UP);
          String description = "Staff "+staffLogInManagedBean.getStaffIc()+" perform prematur withdraw of fixed deposit "+selectedFixedDeposit.getAccountNumber()+" for customer "+customer.getIc();
          fda.logStaffAction(description, customerId, staffId);
      }
      this.updateList(customerId);
      ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("/MerlionBankBackOffice/FixedDepositManagement/withdrawFixedDepositSuccess.xhtml");
        }
        else{
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please choose account");
        RequestContext.getCurrentInstance().showMessageInDialog(message);    
        }
      
    }
    


    public FixedDepositRate getFixedRateSelected() {
        return fixedRateSelected;
    }

    public void setFixedRateSelected(FixedDepositRate fixedRateSelected) {
        this.fixedRateSelected = fixedRateSelected;
    }

    public Double getNewInterestRate() {
        return newInterestRate;
    }

    public void setNewInterestRate(Double newInterestRate) {
        this.newInterestRate = newInterestRate;
    }
    
    private void updateList(Long customerId){
       withdrawableFixedDeposit = fda.getWithdrawableAccount(customerId); 
       fixedDepositAccounts = fda.getFixedDepositAccounts(customerId);
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<FixedDepositAccount> getWithdrawableFixedDeposit() {
        return withdrawableFixedDeposit;
    }

    public void setWithdrawableFixedDeposit(List<FixedDepositAccount> withdrawableFixedDeposit) {
        this.withdrawableFixedDeposit = withdrawableFixedDeposit;
    }

    public FixedDepositAccount getSelectedFixedDeposit() {
        return selectedFixedDeposit;
    }

    public void setSelectedFixedDeposit(FixedDepositAccount selectedFixedDeposit) {
        this.selectedFixedDeposit = selectedFixedDeposit;
    }

    public String getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(String withdrawType) {
        this.withdrawType = withdrawType;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNum) {
        this.accountNumber = accountNum;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    public List<FixedDepositAccount> getFixedDepositAccounts() {
        return fixedDepositAccounts;
    }

    public void setFixedDepositAccounts(List<FixedDepositAccount> fixedDepositAccounts) {
        this.fixedDepositAccounts = fixedDepositAccounts;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    
}