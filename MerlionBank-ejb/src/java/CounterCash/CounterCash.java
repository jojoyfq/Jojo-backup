/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CounterCash;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author shuyunhuang
 */
@Entity
public class CounterCash implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal startAmt;
    private BigDecimal endAmt;
    private Date startTime;
    private Date endTime;
    private String staffName;
    private String status;
    
    public CounterCash(){       
        
    }
    
    public CounterCash(BigDecimal startAmt, Date startTime, String StaffName){
        this.startAmt = startAmt;
        this.startTime = startTime;
        this.staffName = staffName;
        this.status = "initial"; //status can be either initial, matched amount, dismatched amount 
        this.endAmt = null;
        this.endTime = null;
    }
    public BigDecimal getStartAmt() {
        return startAmt;
    }

    public void setStartAmt(BigDecimal startAmt) {
        this.startAmt = startAmt;
    }

    public BigDecimal getEndAmt() {
        return endAmt;
    }

    public void setEndAmt(BigDecimal endAmt) {
        this.endAmt = endAmt;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof CounterCash)) {
            return false;
        }
        CounterCash other = (CounterCash) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CounterCash.CounterCash[ id=" + id + " ]";
    }
    
}
