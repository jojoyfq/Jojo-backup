/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositManagedBean;

import CommonEntity.Customer;
import CommonManagedBean.LogInManagedBean;
import DepositEntity.FixedDepositAccount;
import DepositEntity.SavingAccount;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ruijia
 */
@Named(value = "fixedDepositManagedBean")
@SessionScoped
public class FixedDepositManagedBean implements Serializable {

    /**
     * Creates a new instance of FixedDepositManagedBean
     */
    @EJB
    FixedDepositAccountSessionBeanLocal fda;
    @EJB
    SavingAccountSessionBeanLocal sas;
    private String amountString;
    private BigDecimal amountBD;
    private Date startDate;
    private Calendar calE;
    private Calendar calS;
    private String duration;
    private Date endDate;
    private Double interestRate;
    private Long customerId = 2L;
    private Boolean response;

    private Long accountNumber;
    private String accountNumberStr;
    private String endDateString;
    private String startDateString;
    private List<SavingAccount> savingAccounts;
    private Long savingAcctSelected;  
    private Long savingAcctNumber;
    private Customer customer;
    private List<FixedDepositAccount> fixedDepositAccounts;
    private List<Long> fixedDepositNoMoney;
    private Long fixedDepositSelected;
    private BigDecimal amountLessBalance;
    private BigDecimal amountToTransfer;
    private String amountToTransferStr;
    private List<Long> withdrawable;
    private List<Long> renewable;



    @Inject
    private LogInManagedBean logInManagedBean;
//setter for LogInManagedBean
    @Inject
    private SavingAccountManagedBean savingAccountManagedBean;

    
    public SavingAccountManagedBean getSavingAccountManagedBean() {
        return savingAccountManagedBean;
    }

    public void setSavingAccountManagedBean(SavingAccountManagedBean savingAccountManagedBean) {
        this.savingAccountManagedBean = savingAccountManagedBean;
    }
    
    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
        this.logInManagedBean = logInManagedBean;
    }
    

    
    public FixedDepositManagedBean() {
    }

    @PostConstruct
    public void init() {     
        savingAccountManagedBean.init();
        fixedDepositAccounts = fda.getFixedDepositAccounts(customerId);
        fixedDepositNoMoney = fda.getNoMoneyFixedDeposit(customerId);
        withdrawable = fda.getWithdrawable(customerId);
        renewable = fda.getRenewable(customerId);
    }

    public void createFixedDepositAccount(ActionEvent event) throws IOException {
        if (amountString != null) {
            amountBD = new BigDecimal(amountString);

            //compute start date
            calS = GregorianCalendar.getInstance();
            calS.add(GregorianCalendar.DATE, 7);
            startDate = calS.getTime();
            //compute end date
            calE = GregorianCalendar.getInstance();
            calE.setTime(getStartDate());
            if (getDuration().equalsIgnoreCase("3")) {
                calE.add(GregorianCalendar.MONTH, 3);
            }        
            else if (getDuration().equalsIgnoreCase("6")) {
                calE.add(GregorianCalendar.MONTH, 6);
            } else if (getDuration().equalsIgnoreCase("12")) {
                calE.add(GregorianCalendar.MONTH, 12);
            } else {
                calE.add(GregorianCalendar.MONTH, 24);
            }

            setEndDate(getCalE().getTime());

        //setCustomerId(logInManagedBean.getCustomerId());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            endDateString = df.format(endDate);
            startDateString = df.format(startDate);

            accountNumber = fda.createFixedAccount(customerId, amountBD, startDate, endDate, duration);
            System.out.print(customerId);
            accountNumberStr = accountNumber.toString();
            //update acct list and no money acct list
            this.updateList(customerId);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("/MerlionBank-war/FixedDepositManagement/createFixedDepositAccountSuccess.xhtml");

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please enter amount");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }
    //to return the most recently created fixed deposit account info after successful creation
 /*   public ArrayList<String> getLastDepositAccount(){
     customerId = (String) FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
     ArrayList<String> result = fda.displayLastAccount(customerId);
     // accountNum, endDate, amount
     return result; 
     }
    
     public void withdraw(){
     customerId = (String) FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
     accountNumberStr = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("accountNumber");
     accountNumber = Long.valueOf(accountNumberStr);
     // havent finished  
           
     } **/

    public void transferToFixed(ActionEvent event) throws IOException {

        //check if customer have select any account

        if (savingAcctSelected != null&& amountToTransferStr !=null) {
            amountToTransfer = new BigDecimal(amountToTransferStr);
            response = fda.transferToFixedDeposit(savingAcctSelected, accountNumber, amountToTransfer, customerId);
            if (response == true) {
                fixedDepositSelected = accountNumber;
                amountLessBalance = fda.amountLessBalance(accountNumber);
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                this.updateList(customerId);
                ec.redirect("/MerlionBank-war/FixedDepositManagement/transferToFixedSuccess.xhtml");
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your saving account has insufficient balance");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please choose saving account and enter amount");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }
      
    public void transferToFixedSeparate(ActionEvent event) throws IOException{
        if(savingAcctSelected !=null && fixedDepositSelected != null&&amountToTransferStr != null){
          ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
          amountBD = fda.getAmount(fixedDepositSelected);
          amountLessBalance = fda.amountLessBalance(fixedDepositSelected);
          ec.redirect("/MerlionBank-war/FixedDepositManagement/transferToFixedSeparateNext.xhtml");
        }
        else{
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select accounts and enter amount");
             RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    public void transferToFixedSeparateNext(ActionEvent event) throws IOException {
        amountToTransfer = new BigDecimal(amountToTransferStr);
        response = fda.transferToFixedDeposit(savingAcctSelected, fixedDepositSelected,amountToTransfer, customerId);

        if (response == true) {
            //format date for display in success page
            startDateString = fda.formatDate(fixedDepositSelected).get(0);
            endDateString = fda.formatDate(fixedDepositSelected).get(1);
            amountLessBalance = fda.amountLessBalance(fixedDepositSelected);
               ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
               this.updateList(customerId);
                ec.redirect("/MerlionBank-war/FixedDepositManagement/transferToFixedSuccess.xhtml");
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your saving account has insufficient balance");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
    }
    
    public void earlyWithdraw(ActionEvent event) throws IOException{
         if (savingAcctSelected != null&& fixedDepositSelected !=null){
             fda.earlyWithdraw(fixedDepositSelected, savingAcctSelected);
             this.updateList(customerId);   
             ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
             ec.redirect("/MerlionBank-war/FixedDepositManagement/earlyWithdrawNext.xhtml");
         }
    }
    
    public void updateList(Long customerId){
            fixedDepositAccounts = fda.getFixedDepositAccounts(customerId);
            fixedDepositNoMoney = fda.getNoMoneyFixedDeposit(customerId);
            withdrawable = fda.getWithdrawable(customerId);
            renewable = fda.getRenewable(customerId);
    }
    
    public void renewFixed(ActionEvent event) throws IOException{
        fda.renewFixed(fixedDepositSelected);
    }
    public void displayRenewInfo(ActionEvent event) throws IOException{
        
    }
    public String getAmountString() {
        return amountString;
    }

    /**
     * @param amountString the amountString to set
     */
    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    /**
     * @return the amountBD
     */
    public BigDecimal getAmountBD() {
        return amountBD;
    }

    /**
     * @param amountBD the amountBD to set
     */
    public void setAmountBD(BigDecimal amountBD) {
        this.amountBD = amountBD;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        System.out.println(startDate);
        this.startDate = startDate;
    }

    /**
     * @return the duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the interestRate
     */
    public Double getInterestRate() {
        return interestRate;
    }

    /**
     * @param interestRate the interestRate to set
     */
    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * @return the customerId
     */
    /**
     * @return the accountNumber
     */
    public Long getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the accountNumberStr
     */
    public String getAccountNumberStr() {
        return accountNumberStr;
    }

    /**
     * @param accountNumberStr the accountNumberStr to set
     */
    public void setAccountNumberStr(String accountNumberStr) {
        this.accountNumberStr = accountNumberStr;
    }

    /**
     * @return the endDateString
     */
    public String getEndDateString() {
        return endDateString;
    }

    /**
     * @param endDateString the endDateString to set
     */
    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    /**
     * @return the calE
     */
    public Calendar getCalE() {
        return calE;
    }

    /**
     * @param calE the calE to set
     */
    public void setCalE(Calendar calE) {
        this.calE = calE;
    }

    /**
     * @return the calS
     */
    public Calendar getCalS() {
        return calS;
    }

    /**
     * @param calS the calS to set
     */
    public void setCalS(Calendar calS) {
        this.calS = calS;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<SavingAccount> getSavingAccounts() {
        return savingAccounts;
    }

    public void setSavingAccounts(List<SavingAccount> savingAccounts) {
        this.savingAccounts = savingAccounts;
    }
 public List<Long> getFixedDepositNoMoney() {
        return fixedDepositNoMoney;
    }

    public void setFixedDepositNoMoney(List<Long> fixedDepositNoMoney) {
        this.fixedDepositNoMoney = fixedDepositNoMoney;
    }

    public List<FixedDepositAccount> getFixedDepositAccounts() {
        return fixedDepositAccounts;
    }

    public void setFixedDepositAccounts(List<FixedDepositAccount> fixedDepositAccounts) {
        this.fixedDepositAccounts = fixedDepositAccounts;
    }
    
    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getSavingAcctNumber() {
        return savingAcctNumber;
    }

    public void setSavingAcctNumber(Long savingAcctNumber) {
        this.savingAcctNumber = savingAcctNumber;
    }
    
      public Long getSavingAcctSelected() {
        return savingAcctSelected;
    }

    public void setSavingAcctSelected(Long savingAcctSelected) {
        this.savingAcctSelected = savingAcctSelected;
    }
    
    public BigDecimal getAmountLessBalance() {
        return amountLessBalance;
    }

    public void setAmountLessBalance(BigDecimal amountLessBalance) {
        this.amountLessBalance = amountLessBalance;
    }

    public Long getFixedDepositSelected() {
        return fixedDepositSelected;
    }

    public void setFixedDepositSelected(Long fixedDepositSelected) {
        this.fixedDepositSelected = fixedDepositSelected;
    }
    
    public List<Long> getRenewable() {
        return renewable;
    }

    public void setRenewable(List<Long> renewable) {
        this.renewable = renewable;
    }

    public List<Long> getWithdrawable() {
        return withdrawable;
    }

    public void setWithdrawable(List<Long> withdrawable) {
        this.withdrawable = withdrawable;
    }

    public BigDecimal getAmountToTransfer() {
        return amountToTransfer;
    }

    public void setAmountToTransfer(BigDecimal amountToTransfer) {
        this.amountToTransfer = amountToTransfer;
    }

    public String getAmountToTransferStr() {
        return amountToTransferStr;
    }

    public void setAmountToTransferStr(String amountToTransferStr) {
        this.amountToTransferStr = amountToTransferStr;
    }

}
