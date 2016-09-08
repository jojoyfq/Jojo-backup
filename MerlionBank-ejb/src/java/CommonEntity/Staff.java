/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author apple
 */
@Entity
public class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    @OneToMany(cascade={CascadeType.ALL},mappedBy="staff")
//    private Collection<MessageEntity> messages =new ArrayList<MessageEntity>();
    
    //@OneToMany(cascade={CascadeType.ALL},mappedBy="staff")
    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="Staff_StaffRole")
    private ArrayList<StaffRole> staffRoles = new ArrayList<StaffRole>();
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

//    public Collection<MessageEntity> getMessages() {
//        return messages;
//    }
//
//    public void setMessages(Collection<MessageEntity> messages) {
//        this.messages = messages;
//    }

    public ArrayList<StaffRole> getStaffRoles() {
        return staffRoles;
    }

    public void setStaffRoles(ArrayList<StaffRole> staffRoles) {
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
    private String staffName;
    private String password;
   
   
    
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
