/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillManagement;

import BillEntity.Session.BillSessionBeanLocal;
import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import CommonManagedBean.LogInManagedBean;
import DepositManagedBean.SavingAccountManagedBean;
import com.twilio.sdk.TwilioRestException;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ruijia
 */
@Named(value = "billCustomerManagedBean")
@SessionScoped
public class BillCustomerManagedBean implements Serializable {

    @EJB
    BillSessionBeanLocal bsbl;
    @EJB
    AccountManagementSessionBeanLocal amsbl;

    private String boName;
    private String billReference;
    private String savingAcctNum;
    private BigDecimal limit;
    private Long customerId;
    private List<String> boNames;
    private String customerName;
    private String customerIc;
    private String input2FA;
    private String otpSuccessRedirect;
    private Customer customer;
    private BigDecimal amount;
    private Date startDate;
    private Integer times;
    private Integer frequency;

    @Inject
    private LogInManagedBean logInManagedBean;
    @Inject
    private SavingAccountManagedBean savingAccountManagedBean;

    @PostConstruct
    public void init() {
        customerId = logInManagedBean.getCustomerId();
    }

    public void dashboardAddGIROArrangement(ActionEvent event) throws IOException {
        customerName = null; 
        boName = null;
        limit = null;
        savingAcctNum = null;
        billReference = null;
        customer = logInManagedBean.getSelectedCustomer();
        customerName = customer.getName();
        customerId = logInManagedBean.getCustomerId();
        customerIc = logInManagedBean.getIc();
        boNames = bsbl.viewBOName();
        savingAccountManagedBean.init();
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/MerlionBank-war/BillManagement/addGIRO.xhtml");
        otpSuccessRedirect = "/MerlionBank-war/BillManagement/addGIROSuccess.xhtml";
        
    }

    public void dashboardAddRecurrentArrangement(ActionEvent event) throws IOException {
        boName = null;
        amount = null;
        savingAcctNum = null;
        billReference = null;
        times = null;
        frequency = null;
        startDate = null;
        customerId = logInManagedBean.getCustomerId();
        customerIc = logInManagedBean.getIc();
        boNames = bsbl.viewBOName();
        savingAccountManagedBean.init();
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/MerlionBank-war/BillManagement/addRecurrent.xhtml");
        otpSuccessRedirect = "/MerlionBank-war/BillManagement/addRecurrentSuccess.xhtml";
    }
    
    public void dashoboardAdhocBill(ActionEvent event)throws IOException{
        boName = null;
        amount = null;
        savingAcctNum = null;
        billReference = null;
        customerId = logInManagedBean.getCustomerId();
        customerIc = logInManagedBean.getIc();
        boNames = bsbl.viewBOName();
        savingAccountManagedBean.init();
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/MerlionBank-war/BillManagement/adhocBill.xhtml");  
        otpSuccessRedirect = "/MerlionBank-war/BillManagement/adhocBillSuccess.xhtml";
    }

    public void invokeOTP(ActionEvent event) throws IOException, TwilioRestException {
        //check recurrent date before proceed
        if (otpSuccessRedirect.equalsIgnoreCase("/MerlionBank-war/BillManagement/addRecurrentSuccess.xhtml")) {
            DateTime today = new DateTime().withTimeAtStartOfDay();
            System.out.print("today time ..." + today);
            DateTime chosenDate = new DateTime(startDate);
            System.out.print("start date ..."+startDate);
            System.out.print("chosenDate ..."+chosenDate);
            if (!chosenDate.isBefore(today)) {
                System.out.print("start date checked");
                amsbl.sendTwoFactorAuthentication(customerIc);
                System.out.print("2FA SMS sent!!!!!!!");
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/BillManagement/OTP.xhtml");
            } else {
                System.out.print("start date cannot be before today");
                  FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Start date cannot be before today. Please try again.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            }

        } else {
            amsbl.sendTwoFactorAuthentication(customerIc);
            System.out.print("2FA SMS sent!!!!!!!");
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/BillManagement/OTP.xhtml");
        }
    }

    private boolean verify2FA(String input2FA) {

        String result = amsbl.verifyTwoFactorAuthentication(customerIc, input2FA);
        if (result.equalsIgnoreCase(customerIc)) {
            System.out.print("2FA entered correct");
            input2FA = null;
            return true;
        } else {
            System.out.print("2FA wrong");
            input2FA = null;
            return false;
        }
    }

    public void submitOTP(ActionEvent event) throws IOException {
        if (this.verify2FA(input2FA) == true) {
            if (otpSuccessRedirect.equalsIgnoreCase("/MerlionBank-war/BillManagement/addGIROSuccess.xhtml")) {
                this.addGIRO();
            }else if(otpSuccessRedirect.equalsIgnoreCase("/MerlionBank-war/BillManagement/addRecurrentSuccess.xhtml")){
                this.addRecurrent();
            }else if(otpSuccessRedirect.equalsIgnoreCase("/MerlionBank-war/BillManagement/adhocBillSuccess.xhtml")){
                this.adhocBill();
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Security code is wrong, please try again.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }

    private void addRecurrent() throws IOException {
        bsbl.addRecurrentArrangement(boName, amount, Long.parseLong((savingAcctNum.split(","))[0]), billReference, times, frequency, startDate);
        System.out.print("add recurrent success");
        FacesContext.getCurrentInstance().getExternalContext().redirect(otpSuccessRedirect);
    }
    
    private void adhocBill() throws IOException {
       if(bsbl.adHocBill(boName, Long.parseLong((savingAcctNum.split(","))[0]), billReference, amount)==true){
          FacesContext.getCurrentInstance().getExternalContext().redirect(otpSuccessRedirect);  
       }
       else{
          FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Saving account does not have enough balance.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);  
       }
       
    }

    private void addGIRO() throws IOException {
        if (bsbl.addGIROArrangement(customerName, boName, limit, Long.parseLong((savingAcctNum.split(","))[0]), billReference) == true) {
            System.out.print("add giro success");
            FacesContext.getCurrentInstance().getExternalContext().redirect(otpSuccessRedirect);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Unsuccessful! You have an existing GIRO arrangement with same BO and same reference.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }

    public void goToDashboard(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBank-war/dashboard.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to Home page fails");
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    /**
     * Creates a new instance of BillCustomerManagedBean
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BillCustomerManagedBean() {
    }

    public String getInput2FA() {
        return input2FA;
    }

    public void setInput2FA(String input2FA) {
        this.input2FA = input2FA;
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

    public BillSessionBeanLocal getBsbl() {
        return bsbl;
    }

    public List<String> getBoNames() {
        return boNames;
    }

    public void setBoNames(List<String> boNames) {
        this.boNames = boNames;
    }

    public void setBsbl(BillSessionBeanLocal bsbl) {
        this.bsbl = bsbl;
    }

    public String getBoName() {
        return boName;
    }

    public void setBoName(String boName) {
        this.boName = boName;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public String getSavingAcctNum() {
        return savingAcctNum;
    }

    public void setSavingAcctNum(String savingAcctNum) {
        this.savingAcctNum = savingAcctNum;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LogInManagedBean getLogInManagedBean() {
        return logInManagedBean;
    }

    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
        this.logInManagedBean = logInManagedBean;
    }

    public SavingAccountManagedBean getSavingAccountManagedBean() {
        return savingAccountManagedBean;
    }

    public void setSavingAccountManagedBean(SavingAccountManagedBean savingAccountManagedBean) {
        this.savingAccountManagedBean = savingAccountManagedBean;
    }

    public String getOtpSuccessRedirect() {
        return otpSuccessRedirect;
    }

    public void setOtpSuccessRedirect(String otpSuccessRedirect) {
        this.otpSuccessRedirect = otpSuccessRedirect;
    }

}
