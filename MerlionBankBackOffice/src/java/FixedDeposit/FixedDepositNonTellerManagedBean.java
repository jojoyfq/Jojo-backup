/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FixedDeposit;

import DepositEntity.FixedDepositRate;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author shuyunhuang
 */
@Named(value = "fixedDepositNonTellerManagedBean")
@SessionScoped
public class FixedDepositNonTellerManagedBean implements Serializable {

   
    private List<FixedDepositRate> fixedDepositRates;
    private FixedDepositRate fixedRateSelected;
    private Double newInterestRate;
    private Integer duration;
    
    @EJB
    FixedDepositAccountSessionBeanLocal fda;

    @PostConstruct
    public void init() {
            System.out.print("inside the init method");
            fixedDepositRates = fda.getFixedDepositRate();
            System.out.println(fixedDepositRates);

    }
    
    public void changeInterestRate(ActionEvent event)throws IOException{
        if(fixedRateSelected != null){
            //call session bean to set the interest rate
            duration = fixedRateSelected.getDuration();
            fda.changeFixedInterestRate(duration, newInterestRate);
            
        }else{
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please choose account");
        RequestContext.getCurrentInstance().showMessageInDialog(message);    
        }
    }
    public FixedDepositNonTellerManagedBean() {
    }

    public List<FixedDepositRate> getFixedDepositRates() {
        return fixedDepositRates;
    }

    public void setFixedDepositRates(List<FixedDepositRate> fixedDepositRates) {
        this.fixedDepositRates = fixedDepositRates;
    }

    public FixedDepositRate getFixedRateSelected() {
        return fixedRateSelected;
    }

    public void setFixedRateSelected(FixedDepositRate fixedRateSelected) {
        this.fixedRateSelected = fixedRateSelected;
    }

    public Double getNewInterestRate() {
        return newInterestRate;
    }

    public void setNewInterestRate(Double newInterestRate) {
        this.newInterestRate = newInterestRate;
    }
    
}
