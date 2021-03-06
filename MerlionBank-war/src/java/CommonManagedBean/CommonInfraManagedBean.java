/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import CommonEntity.Session.StaffVerifyCustomerAccountSessionBeanLocal;
import DepositEntity.SavingAccountType;
import Exception.EmailNotSendException;
import Exception.UserExistException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author apple
 */
@Named(value = "commonInfraManagedBean")
@SessionScoped
public class CommonInfraManagedBean implements Serializable {

    @EJB
    AccountManagementSessionBeanLocal amsbl;

    @EJB
    StaffVerifyCustomerAccountSessionBeanLocal svcasbl;

    private Customer customer;
    private String ic;
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
    //private Customer selectedCustomer;
    private List savingAccountTypes;

    private String savingAccountType;
    private Long savingAccountID;
    private BigDecimal amount;
    private Date dateOfStart;
    private Date dateOfEnd;
    private String duration;
    private Long depositAccountNumber;
    
    UploadedFile file;

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

//	public void fileUploadListener(FileUploadEvent e){
//		// Get uploaded file from the FileUploadEvent
//		this.file = e.getFile();
////                customer.
////                System.out.println("");
//		// Print out the information of the file
//		System.out.println("Uploaded File Name Is :: "+file.getFileName()+" :: Uploaded File Size :: "+file.getSize());
//	}

    public Long getDepositAccountNumber() {
        return depositAccountNumber;

    }

    public void setDepositAccountNumber(Long depositAccountNumber) {
        this.depositAccountNumber = depositAccountNumber;
    }

    //  private final static Logger LOGGER = Logger.getLogger(CommonInfraManagedBean.class.getName());
    /**
     * Creates a new instance of CommonInfraManaged
     */
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

        savingAccountTypes = new ArrayList<>();
        savingAccountTypes.add("MerLion Monthly Saving Account");
        savingAccountTypes.add("MerLion Youth Saving Account");
        savingAccountTypes.add("MerLion Everyday Saving Account");
    }

    public void setAllVariables(ActionEvent event) throws UserExistException, EmailNotSendException, IOException {

        if (ic != null && customerName != null && customerGender != null && customerDateOfBirth != null && customerAddress != null && customerEmail != null && customerPhoneNumber != null && customerOccupation != null && customerFamilyInfo != null && savingAccountType != null) {

//            if (FacesContext.getCurrentInstance().getResponseComplete()) {
//                System.out.println("lala");
//                return;
//            }
            try {
                System.out.println("ahdhdhdhdaad ");

                //  try {
                //savingAccountType = (SavingAccountType) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("savingAccountType");
                //  savingAccountID = savingAccountType.getId();
                //          }catch()
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
                customer = amsbl.createSavingAccount(ic, customerName, customerGender, customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo, savingAccountType);

                svcasbl.viewPendingVerificationList().add(amsbl.createSavingAccount(ic, customerName, customerGender, customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo, savingAccountType));//throws UserExistException;

//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin");
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", "user1");
//            
//            ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
//                FacesContext facesContext = FacesContext.getCurrentInstance();
//                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "System message", "Account created Successfully"));
//                Flash flash = facesContext.getExternalContext().getFlash();
//                flash.setKeepMessages(true);
//                flash.setRedirect(true);

//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Account created Successfully");
//
//                RequestContext.getCurrentInstance().showMessageInDialog(message);
           FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerManagement/uploadFile.xhtml");
            } catch (UserExistException ex) {
                System.out.println(ex.getMessage());
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());

                RequestContext.getCurrentInstance().showMessageInDialog(message);

            } catch (EmailNotSendException ex1) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex1.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {
            System.out.println("Message from managed bean: please do not leave blanks!");
        }

      //  return "../LogInHome";
    }
      public void fileUploadListener(FileUploadEvent e) throws IOException {
  
 // Get uploaded file from the FileUploadEvent
         this.file = e.getFile();
     //    InputStream input = e.getFile().getInputstream();
         
      //   File inputFile = new File(e.getFile().getFileName());
 
         // Get uploaded file from the FileUploadEvent
    //     this.file = e.getFile();
 //                customer.
 //                System.out.println("");
         // Print out the information of the file
         System.out.println("Uploaded File Name Is :: " + file.getFileName() + " :: Uploaded File Size :: " + file.getSize());
         System.out.println("Uploade file Customer Ic: "+ic);
       // String destPath = "C:\\Users\\apple\\AppData\\Roaming\\NetBeans\\8.0.2\\config\\GF_4.1\\domain1\\docroot\\" + "\\"+ic + "\\"+file.getFileName();
        String destPath = "C:\\Users\\apple\\AppData\\Roaming\\NetBeans\\8.0.2\\config\\GF_4.1\\domain1\\docroot\\" +customer.getIc() +"\\"+file.getFileName();
       // String savedFileName = path + "/" + uploadedFile.getFileName();
    //    File fileToSave = new File(savedFileName);
        File fileToSave = new File(destPath);
        fileToSave.getParentFile().mkdirs();
        fileToSave.delete();
        //Generate path file to copy file
        Path folder = Paths.get(destPath);
        Path fileToSavePath = Files.createFile(folder);
        //Copy file to server
        InputStream input = e.getFile().getInputstream();
        Files.copy(input, fileToSavePath, StandardCopyOption.REPLACE_EXISTING);
// String destPath = "C:\\Users\\apple\\AppData\\Roaming\\NetBeans\\8.0.2\\config\\GF_4.1\\domain1\\docroot\\" + "Sxiaojing" ;d
 
//         File destFile = new File(destPath);
//         FileUtils.forceMkdir(destFile);
//         FileUtils.copyInputStreamToFile(input, destFile);
        //FileUtils.copyFileToDirectory(inputFile, destFile);
         System.out.println("File uploaded successfully!");
         amsbl.setFileDestination(customer.getId(),  fileToSave.getParentFile().getName());
           FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/customerSuccessPageWOLogIn.xhtml");
 
     }


    public void createFixedDepositAccount(ActionEvent event) throws UserExistException, EmailNotSendException, IOException {
        try {
            if (ic != null && customerName != null && customerGender != null && customerDateOfBirth != null && customerAddress != null && customerEmail != null && customerPhoneNumber != null && customerOccupation != null && customerFamilyInfo != null) {

                customer = amsbl.createFixedDepositAccount(ic, customerName, customerGender, customerDateOfBirth, customerAddress, customerEmail, customerPhoneNumber, customerOccupation, customerFamilyInfo);
                svcasbl.viewPendingVerificationList().add(customer);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerManagement/configureFixedDepositAccount.xhtml");

                //  depositAccountNumber = customer.getFixedDepositeAccounts().get(0).getId();
                //  amsbl.createFixedAccount(customer, amount, duration);
            } else {
                System.out.println("Message from managed bean: please do not leave blanks!");
            }
        } catch (UserExistException | EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public void configureFixedDeposit(ActionEvent event) throws EmailNotSendException, IOException {
        try {
            System.out.println("*******Customer IC " + customer.getIc());

            Long check = amsbl.createFixedAccount(customer, amount, duration);
            if (customer.getId().equals(check)) {
//                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Your Account created Successfully!");
//                RequestContext.getCurrentInstance().showMessageInDialog(message);
//                FacesContext facesContext = FacesContext.getCurrentInstance();
//                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "System message", "Account created Successfully"));
//                Flash flash = facesContext.getExternalContext().getFlash();
//                flash.setKeepMessages(true);
//                flash.setRedirect(true);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/customerSuccessPageWOLogIn.xhtml");

            }
        } catch (EmailNotSendException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
        //    return "LogInHome";

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

    public List getSavingAccountTypes() {
        return savingAccountTypes;
    }

    public void setSavingAccountTypes(List savingAccountTypes) {
        this.savingAccountTypes = savingAccountTypes;
    }

    public String getSavingAccountType() {
        return savingAccountType;
    }

    public void setSavingAccountType(String savingAccountType) {
        this.savingAccountType = savingAccountType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(Date dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public Date getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(Date dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getSavingAccountID() {
        return savingAccountID;
    }

    public void setSavingAccountID(Long savingAccountID) {
        this.savingAccountID = savingAccountID;
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
