/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerRelationshipEntity;

import CommonEntity.Customer;
import CommonEntity.CustomerMessage;
import CommonEntity.Staff;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author apple
 */
@Entity
public class CaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date caseCreatedTime;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date caseClosedTime;
    
    @ManyToOne
    private Customer customer;

  
    @OneToMany(cascade={CascadeType.ALL},mappedBy="caseEntity")
    private List<Issue> issues;
    
    @ManyToOne
    private Staff staff;
    
     @OneToOne(cascade={CascadeType.ALL}) 
   private CustomerMessage customerMessage;

    public CustomerMessage getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(CustomerMessage customerMessage) {
        this.customerMessage = customerMessage;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public CaseEntity(String status, Date caseCreatedTime, Customer customer,Staff staff) {
        this.status = status;
        this.caseCreatedTime = caseCreatedTime;
        this.customer = customer;
        this.staff = staff;
    }
    
    
    
    public Date getCaseClosedTime() {
        return caseClosedTime;
    }

    // @ManyToOne
    //private CaseStaff caseStaff;
    public void setCaseClosedTime(Date caseClosedTime) {    
        this.caseClosedTime = caseClosedTime;
    }

    public Date getCaseCreatedTime() {
        return caseCreatedTime;
    }

    public void setCaseCreatedTime(Date caseCreatedTime) {
        this.caseCreatedTime = caseCreatedTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
   
//    public Case (){
  //  }
  public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }
    
//    public CaseStaff getCaseStaff() {
//        return caseStaff;
//    }
//
//    public void setCaseStaff(CaseStaff caseStaff) {
//        this.caseStaff = caseStaff;
//    }
    public CaseEntity(){
     //   this.issues = new ArrayList<Issue>();
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
        if (!(object instanceof CaseEntity)) {
            return false;
        }
        CaseEntity other = (CaseEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CommonEntity.Case[ id=" + id + " ]";
    }
    
}
