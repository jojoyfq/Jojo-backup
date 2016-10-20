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

/**
 *
 * @author a0113893
 */
@Entity
public class Logistic implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    /** the learning rate */
	private double rate;

	/** the weight to learn */
	private double[] weights;

	/** the number of iterations */
	private int ITERATIONS = 3000;

	public Logistic(int n) {
		this.rate = 0.0001;
		weights = new double[n];
        }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public int getITERATIONS() {
        return ITERATIONS;
    }

    public void setITERATIONS(int ITERATIONS) {
        this.ITERATIONS = ITERATIONS;
    }

    public Logistic() {
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
        if (!(object instanceof Logistic)) {
            return false;
        }
        Logistic other = (Logistic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LoanEntity.Logistic[ id=" + id + " ]";
    }
    
    
}
