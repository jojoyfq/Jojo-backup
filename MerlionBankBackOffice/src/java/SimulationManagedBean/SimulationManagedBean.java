package SimulationManagedBean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Exception.AccountNotExistedException;
import Simulation.Session.ExternalTransferSessionBeanLocal;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author apple
 */
@Named(value = "simulationManagedBean")
@SessionScoped
public class SimulationManagedBean implements Serializable {

    @EJB
    ExternalTransferSessionBeanLocal etsbl;

    /**
     * Creates a new instance of SimulationManagedBean
     */
    public SimulationManagedBean() {
    }
    private Long accountNumber;
    private BigDecimal amount;
    private Long giverBankActNum;
    private String giverBankActName;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getGiverBankActNum() {
        return giverBankActNum;
    }

    public void setGiverBankActNum(Long giverBankActNum) {
        this.giverBankActNum = giverBankActNum;
    }

    public String getGiverBankActName() {
        return giverBankActName;
    }

    public void setGiverBankActName(String giverBankActName) {
        this.giverBankActName = giverBankActName;
    }

    @PostConstruct
    public void init() {
        //   amount = new BigDecimal();
    }

    public void transferToFixed(ActionEvent event) throws AccountNotExistedException {
        try {
            boolean msg = etsbl.transferFixedDepositAccount(accountNumber, amount);

            if (msg = false) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Simulation Message", "Transferred unsuccessfully!");

                RequestContext.getCurrentInstance().showMessageInDialog(message);

            }
            else{
            System.out.println("**************Transferred successfully!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Transfer Successful!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (AccountNotExistedException ex) {

        }
    }
public void transerToSaving(ActionEvent event) throws AccountNotExistedException{
        try {
            boolean msg = etsbl.transferSavingAccount(accountNumber, amount, giverBankActNum, giverBankActName);

            if (msg = false) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Simulation Message", "Transferred unsuccessfully!");

                RequestContext.getCurrentInstance().showMessageInDialog(message);

            }
            else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Transfer successful!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            System.out.println("**************Transferred successfully!");
            }
        } catch (AccountNotExistedException ex) {

        }
}


}
