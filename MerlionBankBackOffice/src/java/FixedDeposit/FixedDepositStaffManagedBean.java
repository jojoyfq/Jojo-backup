/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FixedDeposit;

import CommonEntity.Customer;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
import TellerManagedBean.ServiceCustomerManagedBean;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
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
public class FixedDepositStaffManagedBean implements Serializable{

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

    @Inject
    private ServiceCustomerManagedBean serviceCustomerManagedBean;

    /**
     * Creates a new instance of FixedDepositStaffManagedBean
     */

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
            accountNumber = fda.createFixedAccount(customerId, amountBD, startDate, endDate, duration);
            fda.getAccount(accountNumber).setStatus("active");
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("/MerlionBankBackOffice/FixedDepositManagement/createFixedDepositSuccess.xhtml");

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please enter amount");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }
    
    public void goToHomePage(ActionEvent event)throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/FixedDepositManagement/createFixedDeposit.xhtml");
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNum) {
        this.accountNumber = accountNumber;
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

}
