/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import Exception.PasswordNotMatchException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
//import Logger.MyLogger;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
//import javax.faces.bean.SessionScoped;
import javax.inject.Named;
//import javax.enterprise.context.Dependent;
//import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import org.primefaces.context.RequestContext;

/**
 *
 * @author apple
 */
@Named(value = "logInManagedBean")
@SessionScoped
public class LogInManagedBean implements Serializable {

    @EJB
    AccountManagementSessionBeanLocal amsbl;

    private Customer customer;
    private String ic = "S9276";
    private String customerName;
    private String customerGender;
    private Date customerDateOfBirth;
    private String customerAddress;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerOccupation;
    private String customerFamilyInfo;
    private String customerFinancialAsset;
    private String customerFinancialGoal;
    private Customer selectedCustomer;
    private String birthdate;
    private String customerPassword;
    private final int max_attempts = 5;
    private int logInAttempts;
//    private String accountType = "Saving Account";

    private List data = new ArrayList();
    private List accountTypes;
    
          
    
//    public List getAccountTypes() {
//        return accountTypes;
//    }
//
//    public void setAccountTypes(List accountTypes) {
//        this.accountTypes = accountTypes;
//    }

    

//    public String getAccountType() {
//        return accountType;
//    }
//
//    public void setAccountType(String accountType) {
//        this.accountType = accountType;
//    }

   
    /**
     * Creates a new instance of LogInManagedBean
     */
    public LogInManagedBean() {
//        try {
//            MyLogger.setup();
//        } catch (IOException e) {
//            e.printStackTrace();
//     //       throw new RuntimeException("Problems with creating the log files");
//        }
//        LOGGER.setLevel(Level.INFO);
    }

    @PostConstruct
    public void init() {
        selectedCustomer = new Customer();
        try {
//            accountTypes = new ArrayList();
//            accountTypes.add("Saving Account");
//            accountTypes.add("Credit Account");
//            accountTypes.add("Loan Account");
//            accountTypes.add("Wealth Managment Account");
            this.viewOneCustomer();
        //    System.out.println("Account Type chosen is " + accountType);
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

//   private void warnMsg(String message) {
//        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, message, "");
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, msg);
//        context.getExternalContext().getFlash().setKeepMessages(true);
//        LOGGER.info("MESSAGE INFO: " + message);
//    }
//      private void faceMsg(String message) {
//        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, "");
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, msg);
//        context.getExternalContext().getFlash().setKeepMessages(true);
//        LOGGER.info("MESSAGE INFO: " + message);
//    }
//        private void errorMsg(String message) {
//        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, msg);
//        context.getExternalContext().getFlash().setKeepMessages(true);
//        LOGGER.info("MESSAGE INFO: " + message);
//    }
 
//    public void chooseAccountType(ActionEvent event) throws IOException {
////        accountTypes.add("Saving Account");
////        accountTypes.add("Credit Account");
////        accountTypes.add("Loan Account");
////        accountTypes.add("Wealth Management Account");
////        FacesMessage msg;
////        msg = new FacesMessage("Selected" + accountType);
//        System.out.println("Account Type chosen is " );
//        try {
//            if (accountType.equals("Saving Account")) {
//                System.out.println("Saving account has been selected");
//                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerManagement/createSavingAccount.xhtml");
//            } else if (accountType.equals("Credit Account")) {
//                System.out.println("This service is currently not available!");
//            } else if (accountType.equals("Loan Account")) {
//                System.out.println("This service is currently not available!");
//            } else if (accountType.equals("Wealth Management Account")) {
//                System.out.println("This service is currently not available!");
//            } else {
//                System.out.println("Please indicate one choice");
//            }
//        } catch (IOException ex) {
//            System.out.println(ex);
//        }
//
//    }
//
    public void customerLogIn(ActionEvent event) throws UserNotExistException, PasswordNotMatchException, UserNotActivatedException {
        try {
            if (customerName != null && customerPassword != null) {
                Long logInId = amsbl.checkLogin(ic, customerPassword);
                //logInAttempts = 0;
                if (logInId.toString().equals("1")) {
                    System.out.println("Password does not match");
                    logInAttempts++;
                    System.out.println("number attempts:" + logInAttempts);
                    if (logInAttempts == max_attempts) {
                        System.out.println("Your account has been locked out.");
                    }
                } else {
                    logInAttempts = 0;
                    System.out.println("Log In Successful!");
                   //  FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/MessageManagement/staffInputMessage.xhtml");
                }
            } else {
                System.out.println("Please dont leave blanks!");
            }
        } catch (UserNotExistException ex) {
            System.out.println("acccccounnnt does not exist!!!!!!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Log In Message" ,ex.getMessage());

            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (PasswordNotMatchException | UserNotActivatedException ex1) {
            System.out.println(ex1.getMessage());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Log In Message" ,ex1.getMessage());

            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void viewOneCustomer() throws IOException {
        //this.ic = selectedCustomer.getIc();
        selectedCustomer = amsbl.diaplayCustomer(ic);
        System.out.println("Username is " + selectedCustomer);

        // this.ic = selectedCustomer.getIc();
        this.customerName = selectedCustomer.getName();
        this.customerGender = selectedCustomer.getGender();
        this.customerDateOfBirth = selectedCustomer.getDateOfBirth();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        this.birthdate = sdf.format(customerDateOfBirth);
        this.birthdate = customerDateOfBirth.toString();
        this.customerEmail = selectedCustomer.getEmail();
        this.customerAddress = selectedCustomer.getAddress();
        this.customerPhoneNumber = selectedCustomer.getPhoneNumber();
        this.customerOccupation = selectedCustomer.getOccupation();
        this.customerFamilyInfo = selectedCustomer.getFamilyInfo();
        this.customerFinancialGoal = selectedCustomer.getFinancialGoal();
        System.out.println(customerName);
        System.out.println(customerGender);
        System.out.println(customerDateOfBirth);
        System.out.println(customerAddress);
        System.out.println(customerPhoneNumber);
        System.out.println(customerOccupation);
        System.out.println(customerFamilyInfo);
        System.out.println(customerFinancialGoal);
        System.out.println(selectedCustomer.getId());
        System.out.println(selectedCustomer.getIc());

        // FacesContext.getCurrentInstance().getExternalContext().redirect("/MerLION-war/GRNSWeb/admin/updateUser.xhtml");
    }

    public void modifyProfile(ActionEvent event) throws UserExistException {
        try {
            if (FacesContext.getCurrentInstance().getResponseComplete()) {
                System.out.println("lala");
                return;
            }

            System.out.println("Phone Number is: " + customerPhoneNumber);
            if (ic != null && customerName != null && customerGender != null && customerDateOfBirth != null && customerAddress != null && customerEmail != null && customerPhoneNumber != null
                    && customerOccupation != null && customerFamilyInfo != null) {
                amsbl.updateProfile(ic, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo, customerFinancialGoal);
                
                 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"System Message" ,"Profile edited successfully!");

            RequestContext.getCurrentInstance().showMessageInDialog(message);
            } else {
                System.out.println("Please fill in correct information!");
                
            }

            //   amsbl.updateProfile(ic, customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo, customerFinancialGoal);
        } catch (UserExistException ex) {
            System.out.println("Username already exists");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Log In Message" ,ex.getMessage());

            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

   // private final static Logger LOGGER = Logger.getLogger(LogInManagedBean.class.getName());

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
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

    public String getCustomerFinancialAsset() {
        return customerFinancialAsset;
    }

    public void setCustomerFinancialAsset(String customerFinancialAsset) {
        this.customerFinancialAsset = customerFinancialAsset;
    }

    public String getCustomerFinancialGoal() {
        return customerFinancialGoal;
    }

    public void setCustomerFinancialGoal(String customerFinancialGoal) {
        this.customerFinancialGoal = customerFinancialGoal;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }
    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public List getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(List accountTypes) {
        this.accountTypes = accountTypes;
    }
     public int getLogInAttempts() {
        return logInAttempts;
    }

    public void setLogInAttempts(int logInAttempts) {
        this.logInAttempts = logInAttempts;
    }

    
}
