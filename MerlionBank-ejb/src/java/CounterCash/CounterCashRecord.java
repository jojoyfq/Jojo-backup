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
import javax.persistence.Temporal;

/**
 *
 * @author shuyunhuang
 */
@Entity
public class CounterCashRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal initialAmount;
    private BigDecimal finalAmount;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endTime;
    private Long staffId;
    private String status;
    
    public CounterCashRecord(){       
        
    }
    
    public CounterCashRecord(BigDecimal startAmt, Date startTime, Long staffId){
        this.initialAmount = startAmt;
        this.startTime = startTime;
        this.staffId = staffId;
        this.status = "initial"; //status can be either initial, matched amount, dismatched amount 
        this.finalAmount = new BigDecimal("0");
        this.endTime = null;
    }

    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(BigDecimal initialAmount) {
        this.initialAmount = initialAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
    public BigDecimal getStartAmt() {
        return initialAmount;
    }

    public void setStartAmt(BigDecimal startAmt) {
        this.initialAmount = startAmt;
    }

    public BigDecimal getEndAmt() {
        return finalAmount;
    }

    public void setEndAmt(BigDecimal endAmt) {
        this.finalAmount = endAmt;
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
        if (!(object instanceof CounterCashRecord)) {
            return false;
        }
        CounterCashRecord other = (CounterCashRecord) object;
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
