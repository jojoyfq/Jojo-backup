/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositManagedBean;

import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
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
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

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
    private String amountString;
    private BigDecimal amountBD;
    private Date startDate;
    private Calendar calE;
    private Calendar calS;
    private String duration;
    private Date endDate;
    private Double interestRate;
    private String customerIC;
    private Boolean createResponse;
    private Long accountNumber;
    private String accountNumberStr;
    private String endDateString;


    public FixedDepositManagedBean() {
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
         if (getDuration().equalsIgnoreCase("3"))
         calE.add(GregorianCalendar.MONTH, 3);
               
         else if(getDuration().equalsIgnoreCase("6"))
         calE.add(GregorianCalendar.MONTH, 6);
       
         else if(getDuration().equalsIgnoreCase("12"))
         calE.add(GregorianCalendar.MONTH, 12);
          
         else
         calE.add(GregorianCalendar.MONTH,24);
     
         setEndDate(getCalE().getTime());
         
        //for testing
        setCustomerIC("123a");
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        endDateString = df.format(endDate);
       
        createResponse = fda.createFixedAccount(customerIC, amountBD, startDate, endDate, duration);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        
      if (getCreateResponse() == true) {
            ec.redirect("/MerlionBank-war/DepositManagement/createFixedDepositAccountSuccess.xhtml");
        } else {
            ec.redirect("/MerlionBank-war/DepositManagement/createFixedDepositAccountFail.xhtml");
        }
    }

    //to return the most recently created fixed deposit account info after successful creation
 /*   public ArrayList<String> getLastDepositAccount(){
     customerIC = (String) FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
     ArrayList<String> result = fda.displayLastAccount(customerIC);
     // accountNum, endDate, amount
     return result; 
     }
    
     public void withdraw(){
     customerIC = (String) FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
     accountNumberStr = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("accountNumber");
     accountNumber = Long.valueOf(accountNumberStr);
     // havent finished  
           
     }
     */
    /**
     * @return the amountString
     */
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
     * @return the customerIC
     */
    public String getCustomerIC() {
        return customerIC;
    }

    /**
     * @param customerIC the customerIC to set
     */
    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
    }

    /**
     * @return the createResponse
     */
    public Boolean getCreateResponse() {
        return createResponse;
    }

    /**
     * @param createResponse the createResponse to set
     */
    public void setCreateResponse(Boolean createResponse) {
        this.createResponse = createResponse;
    }

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

}
