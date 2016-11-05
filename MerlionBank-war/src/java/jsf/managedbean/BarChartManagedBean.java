/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import DepositManagedBean.SavingAccountManagedBean;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author liyanmeng
 */
@Named(value = "barChartManagedBean")
@SessionScoped
public class BarChartManagedBean implements Serializable {

    @Inject
    SavingAccountManagedBean savingAccountManagedBean;
    private BarChartModel barModel;
    /**
     * Creates a new instance of BarChartManagedBean
     */
    
    @PostConstruct
    public void init() {
        createBarModel();
    }
    
    public BarChartManagedBean() {
    }
    
    public BarChartModel getBarModel() {
        return barModel;
    }

    private void createBarModel() {
       barModel = initBarModel();
       
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Cash & Investment");
        
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Amount");
        yAxis.setMin(0);
    }
    
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
        ChartSeries cash = new ChartSeries();
        Long accountNo = Long.parseLong(savingAccountManagedBean.getSavingAccountNumberList().get(0).split(",")[0]);
        String accountStr = accountNo + "";
        BigDecimal balance = savingAccountManagedBean.getSavingAccounts().get(0).getAvailableBalance();
        cash.set(accountStr, balance);
//        cash.set("2nd account", 1385);
        
        model.addSeries(cash);
        return model;
    }
    
    
    
    
}
