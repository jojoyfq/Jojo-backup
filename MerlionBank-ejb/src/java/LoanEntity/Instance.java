/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity;


import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author a0113893
 */
@Entity
public class Instance implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
     private int label;
   
    
     @OneToMany(cascade={CascadeType.ALL},mappedBy="instance")
    private List<InstanceValue> instanceValues;

    public Instance() {
    }

    public Instance(int label, List<InstanceValue> instanceValues) {
        this.label = label;
        this.instanceValues = instanceValues;
    }

    
    public List<InstanceValue> getInstanceValues() {
        return instanceValues;
    }

    public void setInstanceValues(List<InstanceValue> instanceValues) {
        this.instanceValues = instanceValues;
    }

    
   
   

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
      
    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
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
        if (!(object instanceof Instance)) {
            return false;
        }
        Instance other = (Instance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LoanEntity.Instance[ id=" + id + " ]";
    }
    
}
