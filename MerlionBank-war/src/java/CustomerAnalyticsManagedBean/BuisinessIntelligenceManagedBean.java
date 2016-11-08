package CustomerAnalyticsManagedBean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import BusinessIntelligenceEntity.Interest;
import BusinessIntelligenceEntity.Session.SpendingSegmentationManagementSessionBeanLocal;
import CommonManagedBean.LogInManagedBean;
import WealthEntity.Session.WealthApplicationSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author apple
 */
@Named(value = "buisinessIntelligenceManagedBean")
@SessionScoped
public class BuisinessIntelligenceManagedBean implements Serializable{
  @EJB
    SpendingSegmentationManagementSessionBeanLocal ssmsbl;
    @EJB
    private WealthApplicationSessionBeanLocal wasbl;
    @Inject
    LogInManagedBean logInManagedBean;
    
    
   private Boolean searchResult;
    private String customerIc;
    private Interest selectedInterest;
   private Long selectedInterestId;   
   private List<Interest>  oneCustomerAllInterest;
   private Long customerId;
    private BarChartModel barModel;

    /**
     * Creates a new instance of BuisinessIntelligenceManagedBean
     */
    public BuisinessIntelligenceManagedBean() {
    }
     @PostConstruct
    public void init() {
          oneCustomerAllInterest = new ArrayList<>();
        selectedInterest = new Interest();
    }
     public void searchCustomer(ActionEvent event) throws IOException{
       
            customerId = logInManagedBean.getCustomerId();
            System.out.println("Searched customer ID is "+customerId);
            searchResult = true;
    
       oneCustomerAllInterest = ssmsbl.viewCustomerSegmentation(customerId);
        createBarModels();
      FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerAnalytics/viewAllInterests.xhtml");

    }
     
     private BarChartModel initBarModel() {
       BarChartModel model = new BarChartModel();
 
        ChartSeries boys = new ChartSeries();
        for(int i=0;i<oneCustomerAllInterest.size();i++){
           // String legend = oneCustomerAllInterest.get(i).getCategory().getName();
            boys.setLabel("Spending Interest");
            boys.set(""+oneCustomerAllInterest.get(i).getCategory().getName(),oneCustomerAllInterest.get(i).getAmount());
       
        }
            
          
        model.addSeries(boys);
        return model;
    }
     private void createBarModel() {
        barModel = initBarModel();
         
        barModel.setTitle("Bar Chart");
         
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Category");
         
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Amount Spent S($)");
        yAxis.setMin(0);
        yAxis.setMax(20000);
    }
    private void createBarModels() {
        createBarModel();
    }

    public SpendingSegmentationManagementSessionBeanLocal getSsmsbl() {
        return ssmsbl;
    }

    public void setSsmsbl(SpendingSegmentationManagementSessionBeanLocal ssmsbl) {
        this.ssmsbl = ssmsbl;
    }

    public WealthApplicationSessionBeanLocal getWasbl() {
        return wasbl;
    }

    public void setWasbl(WealthApplicationSessionBeanLocal wasbl) {
        this.wasbl = wasbl;
    }

    public LogInManagedBean getLogInManagedBean() {
        return logInManagedBean;
    }

    public void setLogInManagedBean(LogInManagedBean logInManagedBean) {
        this.logInManagedBean = logInManagedBean;
    }

    public Boolean getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(Boolean searchResult) {
        this.searchResult = searchResult;
    }

    public String getCustomerIc() {
        return customerIc;
    }

    public void setCustomerIc(String customerIc) {
        this.customerIc = customerIc;
    }

    public Interest getSelectedInterest() {
        return selectedInterest;
    }

    public void setSelectedInterest(Interest selectedInterest) {
        this.selectedInterest = selectedInterest;
    }

    public Long getSelectedInterestId() {
        return selectedInterestId;
    }

    public void setSelectedInterestId(Long selectedInterestId) {
        this.selectedInterestId = selectedInterestId;
    }

    public List<Interest> getOneCustomerAllInterest() {
        return oneCustomerAllInterest;
    }

    public void setOneCustomerAllInterest(List<Interest> oneCustomerAllInterest) {
        this.oneCustomerAllInterest = oneCustomerAllInterest;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }
    
}
