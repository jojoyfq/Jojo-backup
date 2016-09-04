/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.UserExistException;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author apple
 */
@Named(value = "commonInfraManagedBean")
@SessionScoped
public class CommonInfraManagedBean implements Serializable {

    @EJB
    AccountManagementSessionBeanLocal amsbl;
    
    private Customer customer;
    private String ic;
    private String customerName;
    private String customerGender; 
    private Date customerDateOfBirth;
    private String customerAddress;
    private String customerEmail;
    private String customerPhoneNumber;
    private String savingAccountType;


    private String customerOccupation;
    private String customerFamilyInfo;
    private String customerFinancialAsset;
    private String customerFinancialGoal;
    //private Customer selectedCustomer;

    
    
  //  private final static Logger LOGGER = Logger.getLogger(CommonInfraManagedBean.class.getName());
    /**
     * Creates a new instance of CommonInfraManaged
     */
   
   //  public CommonInfraManagedBean() {
   //     try {
   //         MyLogger.setup();
   //     } catch (IOException e) {
   //         e.printStackTrace();
   //         throw new RuntimeException("Problems with creating the log files");
   //     }
   //     LOGGER.setLevel(Level.INFO);
   // }
    @PostConstruct
     public void init() {
         customer = new Customer();
         
     }
       public void setAllVariables(ActionEvent event) throws UserExistException, EmailNotSendException,IOException{
            if (FacesContext.getCurrentInstance().getResponseComplete()) {
            System.out.println("lala");
            return; 
        }
//            ic = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("IC");
//            customerName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerName"); 
//            customerGender = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerGender");
//            customerDateOfBirth = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerDateOfBirth");
//            customerAddress = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerAddress");
//            customerEmail = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEmail");
//            customerPhoneNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerPhoneNumber");
//            customerOccupation = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerOccupation");
//            customerFamilyInfo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFamilyInfo");
//            customerFinancialAsset = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFinancialAsset") ;
//            customerFinancialGoal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFinancialGoal");
             //   String phoneNumber = Integer.toString(customerPhoneNumber) ;
                amsbl.createSavingAccount(ic, customerName, customerGender,  customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo, savingAccountType);//throws UserExistException;
       
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin");
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", "user1");
//            
//            ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
//            
            
       }
           public String getSavingAccountType() {
        return savingAccountType;
    }

    public void setSavingAccountType(String savingAccountType) {
        this.savingAccountType = savingAccountType;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }
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
//        public void viewOneCustomer() throws IOException {
//        //this.ic = selectedCustomer.getIc();
//        selectedCustomer = amsbl.diaplayCustomer(ic);
//        System.out.println("Username is " + selectedCustomer.getName());
//        
//        this.ic = selectedCustomer.getIc();
//        this.customerName = selectedCustomer.getName();
//        this.customerGender = selectedCustomer.getGender();
//        this.customerDateOfBirth = selectedCustomer.getDateOfBirth();
//        this.customerAddress = selectedCustomer.getEmail();
//        this.customerPhoneNumber = selectedCustomer.getPhoneNumber();
//        this.customerOccupation = selectedCustomer.getOccupation();
//        this.customerFamilyInfo = selectedCustomer.getFamilyInfo();
//        this.customerFinancialGoal = selectedCustomer.getFinancialGoal();
//        
//
//       // FacesContext.getCurrentInstance().getExternalContext().redirect("/MerLION-war/GRNSWeb/admin/updateUser.xhtml");
//    }

//       public void modifyProfile(ActionEvent event){
//           try{ if (FacesContext.getCurrentInstance().getResponseComplete()) {
//            System.out.println("lala");
//            return;
//            }
////            ic = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("IC");
////            customerName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerName"); 
////            customerGender = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerGender");
////            customerDateOfBirth = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerDateOfBirth");
////            customerAddress = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerAddress");
////            customerEmail = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEmail");
////            customerPhoneNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerPhoneNumber");
////            customerOccupation = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerOccupation");
////            customerFamilyInfo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFamilyInfo");
////            customerFinancialAsset = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFinancialAsset") ;
////            customerFinancialGoal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFinancialGoal");
//            amsbl.updateProfile(ic,customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation,  customerFamilyInfo, customerFinancialAsset, customerFinancialGoal);
//           }catch (UserExistException ex) {
//            System.out.println("Username already exists");
//        }
//        }

    /**
     * @param customerPhoneNumber the customerPhoneNumber to set
     */
    
  }

