/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity;

//import DepositEntity.FixedDepositAccount;

import CardEntity.CreditCard;
import DepositEntity.Payee;
import CustomerRelationshipEntity.CaseEntity;
import DepositEntity.FixedDepositAccount;
import DepositEntity.SavingAccount;
import LoanEntity.Loan;
import PayMeEntity.PayMe;
import WealthEntity.DiscretionaryAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author apple
 */
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    private String ic;
    private String name;
    private String gender;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfBirth;
    private String address;

    
    private String email;
    private String phoneNumber;
    private String occupation; //company info
    private String familyInfo;
    private BigDecimal financialAsset;
    private String financialGoal;
    private String riskRating;
    private String status;
    private BigDecimal intraTransferLimit;
    private BigDecimal monthlyIncome;
    private String fileDestination;


    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getFileDestination() {
        return fileDestination;
    }

    public void setFileDestination(String fileDestination) {
        this.fileDestination = fileDestination;
    }

  
    @OneToOne(cascade={CascadeType.ALL}) 
   private OnlineAccount onlineAccount;//same as ic
    
    @OneToOne(cascade={CascadeType.ALL})
    private PayMe payMe; //one customer has one payme account

    @OneToMany(cascade={CascadeType.ALL},mappedBy="customer")
    private List<SavingAccount> savingAccounts;
    
    @OneToMany(cascade={CascadeType.PERSIST})
    private List<Payee> payees =new ArrayList<Payee>();
    

    @OneToMany(cascade={CascadeType.ALL},mappedBy="customer")
    private List<CustomerAction> customerActions;
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="customer")
    private List<CustomerMessage> customerMessages;
    
     @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "customer")

    private List<Loan> loans = new ArrayList<Loan>();
     
     @OneToMany (cascade = {CascadeType.ALL}, mappedBy = "customer")
     private List<CreditCard> creditCard = new ArrayList<CreditCard>();

    public List<CreditCard> getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(List<CreditCard> creditCard) {
        this.creditCard = creditCard;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

     
    public List<CustomerMessage> getCustomerMessages() {
        return customerMessages;
    }

    public void setCustomerMessages(List<CustomerMessage> customerMessages) {
        this.customerMessages = customerMessages;
    }

    public List<CustomerAction> getCustomerActions() {
        return customerActions;
    }

    public void setCustomerActions(List<CustomerAction> customerActions) {
        this.customerActions = customerActions;
    }


    public List<SavingAccount> getSavingAccounts() {
        return savingAccounts;
    }

    public void setSavingAccounts(List<SavingAccount> savingAccounts) {
        this.savingAccounts = savingAccounts;
    }
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy="customer")
    private List<CaseEntity> cases= new ArrayList<CaseEntity>();
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy="customer")
    private List<FixedDepositAccount> fixedDepositeAccounts;

    public List<FixedDepositAccount> getFixedDepositeAccounts() {
        return fixedDepositeAccounts;
    }

    public void setFixedDepositeAccounts(List<FixedDepositAccount> fixedDepositeAccounts) {
        this.fixedDepositeAccounts = fixedDepositeAccounts;
    }

    public List<CaseEntity> getCases() {
        return cases;
    }

    public void setCases(List<CaseEntity> cases) {
        this.cases = cases;
    }
    
    public List<Payee> getPayees(){
        return payees;
    }

    public void setPayees(List<Payee> payees) {
        this.payees = payees;
    }
    
    
    

    public BigDecimal getIntraTransferLimit() {
        return intraTransferLimit;
    }

    public void setIntraTransferLimit(BigDecimal intraTransferLimit) {
        this.intraTransferLimit = intraTransferLimit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
  @OneToMany(cascade={CascadeType.ALL},mappedBy="customer")
    private List<MessageEntity> messages;
  
 @OneToMany(cascade={CascadeType.ALL},mappedBy="customer")
    private List<UploadedFile> files;

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }
 
    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }
    
//    public List<FixedDepositAccount> getFixedDepositeAccounts() {
//        return fixedDepositeAccounts;
//    }

//    public void setFixedDepositeAccounts(List<FixedDepositAccount> fixedDepositeAccounts) {
//        this.fixedDepositeAccounts = fixedDepositeAccounts;
//    }
   
    
  //  @OneToMany(cascade = {CascadeType.ALL}, mappedBy="customer")
    public Customer (){
    }
    

    public Customer(String IC, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, BigDecimal financialAsset, String riskRating, OnlineAccount onlineAccount,String status,BigDecimal intraTransferLimit) {
        this.ic = IC;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.occupation = occupation;
        this.familyInfo = familyInfo;
        this.financialAsset = financialAsset;
        this.riskRating = riskRating;
        this.onlineAccount = onlineAccount;
        this.status=status;
        this.intraTransferLimit = intraTransferLimit;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }
 

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIc() != null ? getIc().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.getIc() == null && other.getIc() != null) || (this.getIc() != null && !this.ic.equals(other.ic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + getIc() + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * @param occupation the occupation to set
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * @return the familyInfo
     */
    public String getFamilyInfo() {
        return familyInfo;
    }

    /**
     * @param familyInfo the familyInfo to set
     */
    public void setFamilyInfo(String familyInfo) {
        this.familyInfo = familyInfo;
    }

    /**
     * @return the financialAsset
     */
    public BigDecimal getFinancialAsset() {
        return financialAsset;
    }

    /**
     * @param financialAsset the financialAsset to set
     */
    public void setFinancialAsset(BigDecimal financialAsset) {
        this.financialAsset = financialAsset;
    }

    /**
     * @return the financialGoal
     */
    public String getFinancialGoal() {
        return financialGoal;
    }

    /**
     * @param financialGoal the financialGoal to set
     */
    public void setFinancialGoal(String financialGoal) {
        this.financialGoal = financialGoal;
    }

    /**
     * @return the riskRating
     */
    public String getRiskRating() {
        return riskRating;
    }

    /**
     * @param riskRating the riskRating to set
     */
    public void setRiskRating(String riskRating) {
        this.riskRating = riskRating;
    }

    /**
     * @return the onlineAccount
     */
    public OnlineAccount getOnlineAccount() {
        return onlineAccount;
    }

    /**
     * @param onlineAccount the onlineAccount to set
     */
    public void setOnlineAccount(OnlineAccount onlineAccount) {
        this.onlineAccount = onlineAccount;
    }
    
    public PayMe getPayMe() {
        return payMe;
    }

    public void setPayMe(PayMe payMe) {
        this.payMe = payMe;
    }
       
}
