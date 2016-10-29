/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author a0113893
 */
@Entity
public class InstanceValue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer trainVariable;
 
  @ManyToOne
    private Instance instance;

    public InstanceValue() {
    }

    public InstanceValue(Integer trainVariable, Instance instance) {
        this.trainVariable = trainVariable;
        this.instance = instance;
    }

    public Integer getTrainVariable() {
        return trainVariable;
    }

    public void setTrainVariable(Integer trainVariable) {
        this.trainVariable = trainVariable;
    }

   

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
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
        if (!(object instanceof InstanceValue)) {
            return false;
        }
        InstanceValue other = (InstanceValue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LoanEntity.InstanceValue[ id=" + id + " ]";
    }
    
}
