/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillManagement;

import BillEntity.OtherBank;
import BillEntity.Session.BillSessionBeanLocal;
import CommonEntity.Staff;
import StaffManagement.staffLogInManagedBean;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
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

    @Inject
    private staffLogInManagedBean staffLogInManagedBean;

    @PostConstruct
    public void init() {
        System.out.print("inside the init method");
            staff = staffLogInManagedBean.getStaff();
            staffId = staffLogInManagedBean.getStaffId();
        // System.out.print("************************");
        //System.out.print(customerId);
    }

    public void addBank(ActionEvent event) throws IOException {
        System.out.print("UEN:"+UEN);
        if (bsbl.addBank(bankName,swiftCode,UEN,address) == true) {
            String description = "Staff " + staffLogInManagedBean.getStaffIc() + " add Bank " + bankName;
            bsbl.logStaffAction(description, null, staffId);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/BillManagement/addBankSuccess.xhtml");
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Bank already exist. Please confirm bank information");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    public void goToDashboard(ActionEvent event) throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("/MerlionBankBackOffice/StaffDashboard.xhtml");
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
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
    
    
}
