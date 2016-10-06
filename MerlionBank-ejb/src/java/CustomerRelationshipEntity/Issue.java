
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerRelationshipEntity;

import CommonEntity.Staff;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
 
/**
 *
 * @author apple
 */
@Entity
public class Issue implements Serializable {
    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    private String issueType; 
    private String status;
    private int rating;
    private String solution;
  
    @ManyToOne
    private CaseEntity caseEntity;
    
    @ManyToOne
    private Staff staff;

    public CaseEntity getCaseEntity() {
        return caseEntity;
    }

    public void setCaseEntity(CaseEntity caseEntity) {
        this.caseEntity = caseEntity;
    }
    
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
   
    
   
    
    public Issue(){
    }

    public Issue(String content, String issueType, String status, String solution, CaseEntity caseEntity, Staff staff) {
        this.content = content;
        this.issueType = issueType;
        this.status = status;
        this.solution = solution;
        this.caseEntity = caseEntity;
        this.staff = staff;
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Issue)) {
            return false;
        }
        Issue other = (Issue) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CommonEntity.Issue[ id=" + getId() + " ]";
    }

    /**
     * @return the issueType
     */
    public String getIssueType() {
        return issueType;
    }

    /**
     * @param issueType the issueType to set
     */
    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }


    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }
    
}
