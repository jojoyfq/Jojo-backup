/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity;

import CustomerRelationshipEntity.StaffAction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author apple
 */
@Entity
public class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String staffIc;
    private String staffName;
    private String password;
    private String staffEmail;
    private String mobileNumber;
    private String status;
    private String salt;
    
    @OneToMany(cascade={CascadeType.ALL},mappedBy="staff")
   private List<MessageEntity> messages;

    
    //@OneToMany(cascade={CascadeType.ALL},mappedBy="staff")
    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="Staff_StaffRole")
    private List<StaffRole> staffRoles;
    
      @OneToMany(cascade={CascadeType.ALL},mappedBy="staff")
    private List<StaffAction> staffActions;
      
@OneToMany(cascade={CascadeType.ALL},mappedBy="staff")
    private List<CustomerMessage> customerMessages;

    public Staff() {
    }

    public Staff(String staffIc, String staffName, String password, String staffEmail, String mobileNumber, String status, List<StaffRole> staffRoles) {
        this.staffIc = staffIc;
        this.staffName = staffName;
        this.password = password;
        this.staffEmail = staffEmail;
        this.mobileNumber = mobileNumber;
        this.status = status;
        this.staffRoles = staffRoles;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    
    public String getStaffIc() {
        return staffIc;
    }

    public void setStaffIc(String staffIc) {
        this.staffIc = staffIc;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<CustomerMessage> getCustomerMessages() {
        return customerMessages;
    }

    public void setCustomerMessages(List<CustomerMessage> customerMessages) {
        this.customerMessages = customerMessages;
    }

    public List<StaffAction> getStaffActions() {
        return staffActions;
    }

    public void setStaffActions(List<StaffAction> staffActions) {
        this.staffActions = staffActions;
    }
      

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }


    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }


    public List<StaffRole> getStaffRoles() {
        return staffRoles;
    }

    public void setStaffRoles(List<StaffRole> staffRoles) {
        this.staffRoles = staffRoles;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
 
   
   
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Staff)) {
            return false;
        }
        Staff other = (Staff) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CommonEntity.Staff[ id=" + id + " ]";
    }

   
}