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
import java.awt.Desktop;
import java.io.File;

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
import org.primefaces.event.SelectEvent;

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
    private Customer selectedCustomer;
    private String path;

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

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
        selectedCustomer = new Customer();
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
         //   customer = ((Customer) event.getObject());
            // customer = (Customer) event.getComponent().getAttributes().get("selectedCustomer");
            //    customerId = ((Customer) event.getObject()).getId();
            if (selectedCustomer != null) {
                customerId = selectedCustomer.getId();
                System.err.println("********** customer.getId(): " + selectedCustomer.getId());
                //  result = Boolean.toString(check);
                if (!selectedCustomer.getSavingAccounts().isEmpty()) {
                    savingAccountId = selectedCustomer.getSavingAccounts().get(0).getId();
                    System.out.println("***********Saving account ID is " + savingAccountId);
                    System.out.println(result);
                    //   if(result.equals("true")){
                    result = "approve";
                //    }else{
                    //       result = "inactive";
                    //   }

                    svcasbl.verifySavingAccountCustomer(staffId, customerId, result, savingAccountId);
                    pendingCustomers = svcasbl.viewPendingVerificationList();
                    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Cusotmer" + customerId + " has been+ " + result);
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

                } else if (!selectedCustomer.getFixedDepositeAccounts().isEmpty()) {
                    //   if(result.equals("true")){
                    result = "approve";
//                }else{
//                    result = "inactive";
//                }
                    fixedDepositId = selectedCustomer.getFixedDepositeAccounts().get(0).getId();
                    System.out.println("***********Fixed account ID is " + fixedDepositId);

                    svcasbl.verifyFixedDepositAccountCustomer(staffId, customerId, result, fixedDepositId);
                    pendingCustomers = svcasbl.viewPendingVerificationList();

                    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Customer" + customerId + " has been " + result + "d");
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

                } else if (!selectedCustomer.getLoans().isEmpty()) {
                    result = "approve";

                    System.out.println("***********Loan account ID is " + selectedCustomer.getLoans().get(0).getId());

                    svcasbl.verifyLoanAccountCustomer(staffId, customerId, result, selectedCustomer.getLoans().get(0).getId());
                    pendingCustomers = svcasbl.viewPendingVerificationList();

                    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Customer" + customerId + " has been " + result + "d");
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

                }
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select a customer");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }

        } catch (EmailNotSendException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
        return customer;
    }

    public Customer verifyCustomerBankingAccountReject(ActionEvent event) throws IOException, EmailNotSendException {
        try {
         //   customer = ((Customer) event.getObject());
            // customer = (Customer) event.getComponent().getAttributes().get("selectedCustomer");
            //    customerId = ((Customer) event.getObject()).getId();
            if (selectedCustomer != null) {
                customerId = selectedCustomer.getId();
                System.err.println("********** customer.getId(): " + selectedCustomer.getId());
                //  result = Boolean.toString(check);
                if (!selectedCustomer.getSavingAccounts().isEmpty()) {
                    savingAccountId = selectedCustomer.getSavingAccounts().get(0).getId();
                    System.out.println("***********Saving account ID is " + savingAccountId);
                    System.out.println(result);
                    //   if(result.equals("true")){
                    result = "reject";
                //    }else{
                    //       result = "inactive";
                    //   }

                    svcasbl.verifySavingAccountCustomer(staffId, customerId, result, savingAccountId);
                    pendingCustomers = svcasbl.viewPendingVerificationList();

                    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Cusotmer" + customerId + " has been+ " + result);
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

                } else if (!selectedCustomer.getFixedDepositeAccounts().isEmpty()) {
                    //   if(result.equals("true")){
                    result = "reject";
//                }else{
//                    result = "inactive";
//                }
                    fixedDepositId = selectedCustomer.getFixedDepositeAccounts().get(0).getId();
                    System.out.println("***********Fixed account ID is " + fixedDepositId);

                    svcasbl.verifyFixedDepositAccountCustomer(staffId, customerId, result, fixedDepositId);
                    pendingCustomers = svcasbl.viewPendingVerificationList();

                    FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Customer" + customerId + " has been+ " + result);
                    RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);

                }
            } else {
                FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select a customer");
                RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
            }

        } catch (EmailNotSendException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
        return customer;
    }

    public void openDocs(ActionEvent event) throws IOException {
         selectedCustomer = (Customer) event.getComponent().getAttributes().get("selectedCustomer");

           System.out.println("********Selected customer to view documents is " + selectedCustomer.getIc());
        //File openFile = new File(selectedCustomer.getFileDestination());
//         if(!Desktop.isDesktopSupported()){
//            System.out.println("Desktop is not supported");
//            return;
//        }
//        
//        Desktop desktop = Desktop.getDesktop();
//        if(openFile.exists()) desktop.open(openFile);
//        
        
             path = "http://localhost:8080/"+selectedCustomer.getFileDestination();
                    System.out.println("File Path From customer is "+selectedCustomer.getFileDestination());
        System.out.println("File Path is "+path);

//            Runtime runtime = Runtime.getRuntime();
//            runtime.exec("explorer.exe C:\\Users\\apple\\AppData\\Roaming\\NetBeans\\8.0.2\\config\\GF_4.1\\domain1\\docroot\\"+path);
//            System.out.println("open");
   
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

    public StaffVerifyCustomerAccountSessionBeanLocal getSvcasbl() {
        return svcasbl;
    }

    public void setSvcasbl(StaffVerifyCustomerAccountSessionBeanLocal svcasbl) {
        this.svcasbl = svcasbl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
