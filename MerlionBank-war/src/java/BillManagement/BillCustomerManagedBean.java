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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
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
    private Long savingAcctNum;
    private BigDecimal limit;
    private Long customerId;
    private List<String> boNames;
    private String customerName;
    private String customerIc;
    private String input2FA;
    private String otpSuccessRedirect;
    private Customer customer;
    
    @Inject
    private LogInManagedBean logInManagedBean;
    @Inject
    private SavingAccountManagedBean savingAccountManagedBean;

    @PostConstruct
    public void init() {
        customerId = logInManagedBean.getCustomerId();
    }

    public void dashboardAddGIROArrangement(ActionEvent event) throws IOException {
        customer = logInManagedBean.getSelectedCustomer();
        customerName = customer.getName();
        customerId = logInManagedBean.getCustomerId();
        customerIc = logInManagedBean.getIc();
        boNames = bsbl.viewBOName();
        savingAccountManagedBean.init();
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/MerlionBank-war/BillManagement/addGIRO.xhtml");
        otpSuccessRedirect = "/MerlionBank-war/BillManagement/addGIROSuccess.xhtml" ;
    }

    public void invokeOTP(ActionEvent event) throws IOException, TwilioRestException {
        amsbl.sendTwoFactorAuthentication(customerIc);
        System.out.print("2FA SMS sent!!!!!!!");
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/BillManagement/OTP.xhtml");
    }

    private boolean verify2FA(String input2FA) {

        String result = amsbl.verifyTwoFactorAuthentication(customerIc, input2FA);
        if (result.equalsIgnoreCase(customerIc)) {
            System.out.print("2FA entered correct");
            return true;

        } else {
            System.out.print("2FA wrong");
            return false;
        }
    }

    public void addGIRO(ActionEvent event) throws IOException {
        if (this.verify2FA(input2FA) == true) {
            if (bsbl.addGIROArrangement(customerName, boName, limit, savingAcctNum, billReference) == true) {
                System.out.print("add giro success");
                FacesContext.getCurrentInstance().getExternalContext().redirect(otpSuccessRedirect);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Unsuccessful! You have an existing GIRO arrangement with same BO and same reference.");
                RequestContext.getCurrentInstance().showMessageInDialog(message);

            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Security code is wrong, please try again.");
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

    /**
     * Creates a new instance of BillCustomerManagedBean
     */
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

    public Long getSavingAcctNum() {
        return savingAcctNum;
    }

    public void setSavingAcctNum(Long savingAcctNum) {
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
