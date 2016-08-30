/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity;

//import DepositEntity.FixedDepositAccount;
import DepositEntity.SavingAccount;
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
    private String addresss;
    private String email;
    private String phoneNumber;
    private String occupation; //company info
    private String familyInfo;
    private BigDecimal financialAsset;
    private String financialGoal;
    private String riskRating;
  
    @OneToOne(cascade={CascadeType.ALL}) 
   private OnlineAccount onlineAccount;//same as ic
    
    @OneToOne(cascade = {CascadeType.ALL})
    private SavingAccount savingAccount;
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy="customer")
    private List<CaseEntity> cases= new ArrayList<CaseEntity>();
    
//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy="customer")
//    private List<FixedDepositAccount> fixedDepositeAccounts=new ArrayList<FixedDepositAccount>();

    public List<CaseEntity> getCases() {
        return cases;
    }

    public void setCases(List<CaseEntity> cases) {
        this.cases = cases;
    }

//    public List<FixedDepositAccount> getFixedDepositeAccounts() {
//        return fixedDepositeAccounts;
//    }

//    public void setFixedDepositeAccounts(List<FixedDepositAccount> fixedDepositeAccounts) {
//        this.fixedDepositeAccounts = fixedDepositeAccounts;
//    }
   
    
  //  @OneToMany(cascade = {CascadeType.ALL}, mappedBy="customer")
    public Customer (){
    };
    

    public Customer(String IC, String name, String gender, Date dateOfBirth, String addresss, String email, String phoneNumber, String occupation, String familyInfo, BigDecimal financialAsset, String financialGoal, String riskRating, OnlineAccount onlineAccount) {
        this.ic = IC;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.addresss = addresss;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.occupation = occupation;
        this.familyInfo = familyInfo;
        this.financialAsset = financialAsset;
        this.financialGoal = financialGoal;
        this.riskRating = riskRating;
        this.onlineAccount = onlineAccount;
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
     * @return the addresss
     */
    public String getAddresss() {
        return addresss;
    }

    /**
     * @param addresss the addresss to set
     */
    public void setAddresss(String addresss) {
        this.addresss = addresss;
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

    /**
     * @return the savingAccount
     */
    public SavingAccount getSavingAccount() {
        return savingAccount;
    }

    /**
     * @param savingAccount the savingAccount to set
     */
    public void setSavingAccount(SavingAccount savingAccount) {
        this.savingAccount = savingAccount;
    }

    /**
     * @return the fixedDepositeAccount
     */
   
    
}
