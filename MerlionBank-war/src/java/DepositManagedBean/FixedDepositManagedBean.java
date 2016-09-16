/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositManagedBean;

import CommonManagedBean.LogInManagedBean;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

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


    private Long accountNumber;
    private String accountNumberStr;
    private String endDateString;
    private String startDateString;
    private List<SavingAccount> savingAccounts;
    private List<String> savingAcctDisplay;
    private String savingAcctSelected;
    private Long savingAcctNumber;
//    private Customer customer;
//
//    public Customer getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }
//    
//  
    
    

    public Long getSavingAcctNumber() {
        return savingAcctNumber;
    }

    public void setSavingAcctNumber(Long savingAcctNumber) {
        this.savingAcctNumber = savingAcctNumber;
    }



    public String getSavingAcctSelected() {
        return savingAcctSelected;
    }

    public void setSavingAcctSelected(String savingAcctSelected) {
        this.savingAcctSelected = savingAcctSelected;
    }

    public List<String> getSavingAcctDisplay() {
        return savingAcctDisplay;
    }

    public void setSavingAcctDisplay(List<String> savingAcctDisplay) {
        this.savingAcctDisplay = savingAcctDisplay;
    }

    
     @Inject
    private LogInManagedBean logInManagedBean;
//setter for LogInManagedBean
    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
        this.logInManagedBean = logInManagedBean;
    }


    public FixedDepositManagedBean() {
    }

    @PostConstruct
    public void init(){
        savingAcctDisplay = new ArrayList<String>();
       
      //  customer = new Customer();
        
    }
    
    public void createFixedDepositAccount(ActionEvent event) throws IOException {
    
        amountBD = new BigDecimal(amountString);
      
       //compute start date
         calS = GregorianCalendar.getInstance();
         calS.add(GregorianCalendar.DATE, 7);
         startDate = calS.getTime();
         
       //compute end date
         calE = GregorianCalendar.getInstance();
         calE.setTime(getStartDate());
         if (getDuration().equalsIgnoreCase("3")){
         calE.add(GregorianCalendar.MONTH, 3);}
//         try{
//             rate = em.getReference(FixedDepositRate.class,1);
//         }catch(EntityNotFoundException enf){         
//         }         
           
         else if(getDuration().equalsIgnoreCase("6")){
         calE.add(GregorianCalendar.MONTH, 6);}
     
       
         else if(getDuration().equalsIgnoreCase("12")){
         calE.add(GregorianCalendar.MONTH, 12);}

         else{
         calE.add(GregorianCalendar.MONTH,24);}
 
         setEndDate(getCalE().getTime());

        //setCustomerId(logInManagedBean.getCustomerId());
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        endDateString = df.format(endDate);
        startDateString = df.format(startDate);

       
        accountNumber = fda.createFixedAccount(customerId, amountBD, startDate, endDate, duration);
        System.out.print(customerId);
        accountNumberStr = accountNumber.toString();
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBank-war/FixedDepositManagement/createFixedDepositAccountSuccess.xhtml");
     
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
   
    public void transferToFixed( ActionEvent event) throws IOException{

        savingAcctNumber = Long.valueOf(savingAcctSelected);
        //check if customer have select any account
       // Boolean response = fda.transferToFixed(amountBD,savingAcctNumber,accountNumber);
     
        
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

    
}
