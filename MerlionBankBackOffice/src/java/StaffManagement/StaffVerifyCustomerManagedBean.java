/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StaffManagement;

import CommonEntity.Customer;
import CommonEntity.MessageEntity;
import CommonEntity.Session.StaffVerifyCustomerAccountSessionBeanLocal;
import CommonEntity.Staff;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;

import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import org.primefaces.context.RequestContext;

/**
 *
 * @author apple
 */
@Named(value = "staffVerifyCustomerManagedBean")
@SessionScoped
public class StaffVerifyCustomerManagedBean implements Serializable {

    @EJB
    StaffVerifyCustomerAccountSessionBeanLocal svcasbl;

    private Staff staff;
    private Customer customer;
    private Long staffId = 4L;
    private Long customerId;
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    private String result;
    private Long savingAccountId;
    private Long accountId;
    private List<Customer> pendingCustomers;
    private Long fixedDepositId;

    public Long getFixedDepositId() {
        return fixedDepositId;
    }

    public void setFixedDepositId(Long fixedDepositId) {
        this.fixedDepositId = fixedDepositId;
    }

    /**
     * Creates a new instance of StaffVerifyCustomerManagedBean
     */
    public StaffVerifyCustomerManagedBean() {
    }
@PostConstruct
    public void init() {
        System.out.println("###############lalalalala");
        customer = new Customer();
        staff = new Staff();
        pendingCustomers = new ArrayList<>();
        pendingCustomers = svcasbl.viewPendingVerificationList();
        System.out.println("**************Message from managed bean -- size of the pending customers: " + pendingCustomers.size());
    }

//    public void viewPendingCustomer()  {
//        try {
//
//            pendingCustomers = svcasbl.viewPendingVerificationList();
//            System.out.println("**************Message from managed bean -- size of the pending customers: "+pendingCustomers.size());
//
//      
//    }
    public Customer verifyCustomerBankingAccount(ActionEvent event) throws IOException, EmailNotSendException {
        try {
            customer = (Customer) event.getComponent().getAttributes().get("selectedCustomer");
            customerId = customer.getId();
            
            System.err.println("********** customer.getId(): " + customer.getId());
            result = Boolean.toString(check);
            if (!customer.getSavingAccounts().isEmpty()) {
                savingAccountId = customer.getSavingAccounts().get(0).getId();
                 System.out.println("***********Saving account ID is "+savingAccountId);
                System.out.println(result);
                if(result.equals("true")){
                    result = "approve";
                }else{
                    result = "inactive";
                }
                  
                svcasbl.verifySavingAccountCustomer(staffId, customerId, result, savingAccountId);
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Cusotmer" + customerId + " has been+ " + result);
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

            } else if (!customer.getFixedDepositeAccounts().isEmpty()) {
                System.out.println("*******go in to fixed account verification");
                 if(result.equals("true")){
                    result = "approve";
                }else{
                    result = "inactive";
                }
                                 fixedDepositId = customer.getFixedDepositeAccounts().get(0).getId();

                                  System.out.println("***********Fixed account ID is "+fixedDepositId);

                svcasbl.verifyFixedDepositAccountCustomer(staffId, customerId, result, fixedDepositId);
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Cusotmer" + customerId + " has been+ " + result);
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

            }

        } catch (EmailNotSendException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
        return customer;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getSavingAccountId() {
        return savingAccountId;
    }

    public void setSavingAccountId(Long savingAccountId) {
        this.savingAccountId = savingAccountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the pendingCustomers
     */
    public List<Customer> getPendingCustomers() {
        return pendingCustomers;
    }

    /**
     * @param pendingCustomers the pendingCustomers to set
     */
    public void setPendingCustomers(List<Customer> pendingCustomers) {
        this.pendingCustomers = pendingCustomers;
    }
}
