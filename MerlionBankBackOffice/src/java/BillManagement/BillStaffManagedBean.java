/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillManagement;

import BillEntity.BillingOrganization;
import BillEntity.OtherBank;
import BillEntity.Session.BillSessionBeanLocal;
import CommonEntity.Staff;
import StaffManagement.staffLogInManagedBean;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
@Named(value = "billStaffManagedBean")
@SessionScoped
public class BillStaffManagedBean implements Serializable {

    /**
     * Creates a new instance of BillStaffManagedBean
     */
    public BillStaffManagedBean() {
    }

    @EJB
    BillSessionBeanLocal bsbl;
    private Long staffId;
    private Staff staff;
    private String bankName;
    private List<OtherBank> bankList;
    private String swiftCode;
    private String address;
    private String UEN;
    private List<String> bankNames;
    private String bankNameSelected;
    private String bankSWIFTSelected;
    private String bankUENSelected;
    private String bankAddressSelected;
    private OtherBank bankSelected;
    private Long bankIdSelected;
    private String boName;
    private String boUEN;
    private String boAddress;
    private String boBankName;
    private Long boAcctNum;
    private List<BillingOrganization> bos;
    private List<String> boNames;
    private BillingOrganization boSelected;
    private Long boIdSelected;
    private BigDecimal adhocAmount;
    private String billReference;

    @Inject
    private staffLogInManagedBean staffLogInManagedBean;

    @PostConstruct
    public void init() {
        System.out.print("inside the init method");
        staffId = staffLogInManagedBean.getStaffId();
        // System.out.print("************************");
        //System.out.print(customerId);
    }

    public void addBank(ActionEvent event) throws IOException {
        if (bankName != null && swiftCode != null && UEN != null && address != null) {
            System.out.print("** inside if");
            if (bsbl.addBank(bankName, swiftCode, UEN, address) == true) {
                String description = "Staff " + staffLogInManagedBean.getStaffIc() + " add Bank " + bankName;
                bsbl.logStaffAction(description, null, staffId);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/BillManagement/addBankSuccess.xhtml");
            } else {
                System.out.print("** inside else");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Bank already exist. Please confirm bank information");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please fill in all information");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }

    public void goToDashboard(ActionEvent event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/StaffDashboard.xhtml");
    }

    public void dashboardAddBank(ActionEvent event) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/addBank.xhtml");
    }

    public void dashboardViewBank(ActionEvent event) throws IOException {
        bankList = bsbl.viewBank();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/viewBank.xhtml");
    }

    public void dashboardModifyBank(ActionEvent event) throws IOException {
        bankNames = bsbl.viewBankNames();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/modifyBankSelect.xhtml");
    }

    public void dashboardAddBO(ActionEvent event) throws IOException {
        bankNames = bsbl.viewBankNames();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/addBO.xhtml");
    }

    public void dashboardViewBO(ActionEvent event) throws IOException {
        bos = bsbl.viewBO();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/viewBO.xhtml");
    }

    public void dashboardModifyBO(ActionEvent event) throws IOException {
        boNames = bsbl.viewBOName();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/modifyBOSelect.xhtml");
    }

    public void dashboardDeleteBO(ActionEvent event) throws IOException {
        boNames = bsbl.viewBOName();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/deleteBOSelect.xhtml");
    }

    public void dashboardDeleteBank(ActionEvent event) throws IOException {
        bankNames = bsbl.viewBankNames();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/deleteBankSelect.xhtml");
    }

    public void deleteBO(ActionEvent event) throws IOException {
        if (boName != null) {
            int result = bsbl.deleteBO(boName);
            if (result == 1) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Unsuccessful! Billing organization have associated pending bill.");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            } else if (result == 2) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Unsuccessful! Billing organization have associated GIRO or recurrent arrangements.");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            } else {
                System.out.print("BO deleted");
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("/MerlionBankBackOffice/BillManagement/deleteBOSuccess.xhtml");
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select billing organization.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void deleteBank(ActionEvent event) throws IOException {
        if (bankNameSelected != null) {
            int result = bsbl.deleteBank(bankNameSelected);
            if (result == 1) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Unsuccessful! Bank has associated billing organization.");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            } else if (result == 2) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Unsuccessful! Bank has associated pending transaction.");
                RequestContext.getCurrentInstance().showMessageInDialog(message);

            } else {
                System.out.print("Bank deleted");
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect("/MerlionBankBackOffice/BillManagement/deleteBankSuccess.xhtml");
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please select bank.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void displayModifyInfo() throws IOException {
        bankSelected = bsbl.findBank(bankNameSelected);
        bankIdSelected = bankSelected.getId();
        bankSWIFTSelected = bankSelected.getSwiftCode();
        bankUENSelected = bankSelected.getUEN();
        bankAddressSelected = bankSelected.getAddress();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/modifyBank.xhtml");

    }

    public void displayBOModifyInfo() throws IOException {
        boSelected = bsbl.findBO(boName);
        boIdSelected = boSelected.getId();
        boUEN = boSelected.getUEN();
        boAddress = boSelected.getAddress();
        boBankName = boSelected.getBank().getName();
        boAcctNum = boSelected.getAccountNumber();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/modifyBO.xhtml");
    }

    public void modifyBank(ActionEvent event) throws IOException {
        if (bankNameSelected != null && bankAddressSelected != null && bankSWIFTSelected != null && bankUENSelected != null) {
            System.out.print("new bank name:" + bankNameSelected);
            bsbl.modifyBank(bankNameSelected, bankAddressSelected, bankSWIFTSelected, bankUENSelected, bankIdSelected);
            String description = "Staff " + staffLogInManagedBean.getStaffIc() + " modified bank information for " + bankNameSelected;
            bsbl.logStaffAction(description, null, staffId);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("/MerlionBankBackOffice/BillManagement/modifyBankSuccess.xhtml");
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please fill in all information.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }

    public void modifyBO(ActionEvent event) throws IOException {
        bsbl.modifyBO(boName, boAddress, boBankName, boUEN, boAcctNum, boIdSelected);
        String description = "staff " + staffLogInManagedBean.getStaffIc() + " modified BO information for " + boName;
        bsbl.logStaffAction(description, null, staffId);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/BillManagement/modifyBOSuccess.xhtml");
    }

    public void addBO(ActionEvent event) throws IOException {
        if (boName != null && boAddress != null && boAcctNum != null && boBankName != null && boUEN != null) {
            if (bsbl.addBO(boName, boBankName, boAcctNum, boUEN, boAddress) == true) {
                String description = "Staff " + staffLogInManagedBean.getStaffIc() + " add BO " + boName;
                bsbl.logStaffAction(description, null, staffId);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/BillManagement/addBOSuccess.xhtml");
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "BO already exist. Please confirm BO information");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please fill in all information");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
    }
public void adHocBillCounter(ActionEvent event){
    System.out.println("Adhoc bill counter--boName "+boName);
            System.out.println("Adhoc bill counter--billReference "+billReference);
                System.out.println("Adhoc bill counter--adhocAmount "+adhocAmount);

    boolean result = bsbl.adhocBillCounter(boName,billReference, adhocAmount);
   ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect("/MerlionBankBackOffice/BillManagement/adhocBillCounterSuccess.xhtml");
        } catch (IOException ex) {
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        }
}
 public void dashboardToAdhocOTC(ActionEvent event) throws IOException {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/TransferManagement/adhocBillCounter.xhtml");
        } catch (IOException ex) {
            FacesMessage sysMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(sysMessage);
        }
    }

    public String getBoAddress() {
        return boAddress;
    }

    public void setBoAddress(String boAddress) {
        this.boAddress = boAddress;
    }

    public String getBoBankName() {
        return boBankName;
    }

    public void setBoBankName(String boBankName) {
        this.boBankName = boBankName;
    }

    public Long getBoAcctNum() {
        return boAcctNum;
    }

    public void setBoAcctNum(Long boAcctNum) {
        this.boAcctNum = boAcctNum;
    }

    public Long getBankIdSelected() {
        return bankIdSelected;
    }

    public void setBankIdSelected(Long bankIdSelected) {
        this.bankIdSelected = bankIdSelected;
    }

    public Long getBoIdSelected() {
        return boIdSelected;
    }

    public void setBoIdSelected(Long boIdSelected) {
        this.boIdSelected = boIdSelected;
    }

    public OtherBank getBankSelected() {
        return bankSelected;
    }

    public void setBankSelected(OtherBank bankSelected) {
        this.bankSelected = bankSelected;
    }

    public String getBankSWIFTSelected() {
        return bankSWIFTSelected;
    }

    public void setBankSWIFTSelected(String bankSWIFTSelected) {
        this.bankSWIFTSelected = bankSWIFTSelected;
    }

    public String getBankUENSelected() {
        return bankUENSelected;
    }

    public void setBankUENSelected(String bankUENSelected) {
        this.bankUENSelected = bankUENSelected;
    }

    public String getBankAddressSelected() {
        return bankAddressSelected;
    }

    public List<BillingOrganization> getBos() {
        return bos;
    }

    public void setBos(List<BillingOrganization> bos) {
        this.bos = bos;
    }

    public BillingOrganization getBoSelected() {
        return boSelected;
    }

    public void setBoSelected(BillingOrganization boSelected) {
        this.boSelected = boSelected;
    }

    public List<String> getBoNames() {
        return boNames;
    }

    public void setBoNames(List<String> boNames) {
        this.boNames = boNames;
    }

    public void setBankAddressSelected(String bankAddressSelected) {
        this.bankAddressSelected = bankAddressSelected;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public List<String> getBankNames() {
        return bankNames;
    }

    public void setBankNames(List<String> bankNames) {
        this.bankNames = bankNames;
    }

    public String getBankNameSelected() {
        return bankNameSelected;
    }

    public void setBankNameSelected(String bankNameSelected) {
        this.bankNameSelected = bankNameSelected;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUEN() {
        return UEN;
    }

    public void setUEN(String UEN) {
        this.UEN = UEN;
    }

    public BillSessionBeanLocal getBsbl() {
        return bsbl;
    }

    public void setBsbl(BillSessionBeanLocal bsbl) {
        this.bsbl = bsbl;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<OtherBank> getBankList() {
        return bankList;
    }

    public void setBankList(List<OtherBank> bankList) {
        this.bankList = bankList;
    }

    public staffLogInManagedBean getStaffLogInManagedBean() {
        return staffLogInManagedBean;
    }

    public void setStaffLogInManagedBean(staffLogInManagedBean staffLogInManagedBean) {
        this.staffLogInManagedBean = staffLogInManagedBean;
    }

    public String getBoName() {
        return boName;
    }

    public void setBoName(String boName) {
        this.boName = boName;
    }

    public String getBoUEN() {
        return boUEN;
    }

    public void setBoUEN(String boUEN) {
        this.boUEN = boUEN;
    }

    public BigDecimal getAdhocAmount() {
        return adhocAmount;
    }

    public void setAdhocAmount(BigDecimal adhocAmount) {
        this.adhocAmount = adhocAmount;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

}
