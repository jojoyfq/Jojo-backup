/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PortfolioManagedBean;

import Exception.NotEnoughAmountException;
import StaffManagement.staffLogInManagedBean;
import WealthEntity.DiscretionaryAccount;
import WealthEntity.Good;
import WealthEntity.Portfolio;
import WealthEntity.Product;
import WealthEntity.Session.WealthManagementSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author apple
 */
@Named(value = "portfolioManagedBean")
@SessionScoped
public class PortfolioManagedBean implements Serializable {

    /**
     * Creates a new instance of PortfolioManagedBean
     */
    public PortfolioManagedBean() {
    }

    @EJB
    WealthManagementSessionBeanLocal wmsbl;
    @Inject
    staffLogInManagedBean slimb;

    private Long staffId;
    private Long productId;
    private Long goodId;
    private BigDecimal unitPrice;
    private Integer numOfUnits;
    private List<Portfolio> oneStaffAllPortfolios;
    private List<Product> oneCustomerAllProducts;
    private List<Good> oneProductAllGoods;
    private Portfolio selectedPort;
    private Good selectedGood;
    private Product selectedProduct;
    private String productName;

    @PostConstruct
    public void init() {
        oneCustomerAllProducts = new ArrayList<>();
        oneStaffAllPortfolios = new ArrayList<>();
        oneProductAllGoods = new ArrayList<>();
        staffId = slimb.getStaffId();
        selectedPort = new Portfolio();
        selectedGood = new Good();
        selectedProduct = new Product();
    }

    public void goToViewAllPortfolios(ActionEvent event) {
        oneStaffAllPortfolios = wmsbl.displayPortfoliosUnderStaff(staffId);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/PortfolioManagement/portfolioManagement.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(PortfolioManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void viewProducts(ActionEvent event) {
        selectedPort = (Portfolio) event.getComponent().getAttributes().get("selectedPort");
        System.out.println("Selected portfolio to sell is " + selectedPort.getId());

        oneCustomerAllProducts = wmsbl.displayProduct(selectedPort.getId());
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/PortfolioManagement/productManagement.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(PortfolioManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void viewGoods(ActionEvent event) {
        selectedProduct = (Product) event.getComponent().getAttributes().get("selectedProduct");
        System.out.println("Selected product to view is " + selectedProduct.getId());

        oneProductAllGoods = wmsbl.displayGood(selectedProduct.getId());
        productId = selectedProduct.getId();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/PortfolioManagement/goodManagement.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(PortfolioManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void selectGood(ActionEvent event){
            selectedGood = (Good) event.getComponent().getAttributes().get("selectedGood");

    }

    public void staffBuyExistingGood(ActionEvent event) {
        System.out.println("Selected Good to buy is " + selectedGood.getName());

        try {
            oneProductAllGoods = wmsbl.buyExistingGood(staffId, productId, selectedGood.getId(), unitPrice, numOfUnits);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully bought  " + selectedGood.getName() + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (NotEnoughAmountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public void staffBuyNewGood(ActionEvent event) {
//        selectedGood = (Good) event.getComponent().getAttributes().get("selectedGood");
        try {
            oneProductAllGoods = wmsbl.buyNewGood(staffId, productId, productName, unitPrice, numOfUnits);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully bought  " + goodId + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);

        } catch (NotEnoughAmountException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public void staffSellGood(ActionEvent event) {
        System.out.println("Selected Good to sell is " + selectedGood.getName());
        try {
            oneProductAllGoods = wmsbl.sellGood(staffId, productId, selectedGood.getId(), unitPrice, numOfUnits);
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully sold  " + selectedGood.getName() + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (NotEnoughAmountException ex) {
 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);        }

    }

    public WealthManagementSessionBeanLocal getWmsbl() {
        return wmsbl;
    }

    public void setWmsbl(WealthManagementSessionBeanLocal wmsbl) {
        this.wmsbl = wmsbl;
    }

    public staffLogInManagedBean getSlimb() {
        return slimb;
    }

    public void setSlimb(staffLogInManagedBean slimb) {
        this.slimb = slimb;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getNumOfUnits() {
        return numOfUnits;
    }

    public void setNumOfUnits(Integer numOfUnits) {
        this.numOfUnits = numOfUnits;
    }

    public List<Portfolio> getOneStaffAllPortfolios() {
        return oneStaffAllPortfolios;
    }

    public void setOneStaffAllPortfolios(List<Portfolio> oneStaffAllPortfolios) {
        this.oneStaffAllPortfolios = oneStaffAllPortfolios;
    }

    public List<Product> getOneCustomerAllProducts() {
        return oneCustomerAllProducts;
    }

    public void setOneCustomerAllProducts(List<Product> oneCustomerAllProducts) {
        this.oneCustomerAllProducts = oneCustomerAllProducts;
    }

    public List<Good> getOneProductAllGoods() {
        return oneProductAllGoods;
    }

    public void setOneProductAllGoods(List<Good> oneProductAllGoods) {
        this.oneProductAllGoods = oneProductAllGoods;
    }

    public Portfolio getSelectedPort() {
        return selectedPort;
    }

    public void setSelectedPort(Portfolio selectedPort) {
        this.selectedPort = selectedPort;
    }

    public Good getSelectedGood() {
        return selectedGood;
    }

    public void setSelectedGood(Good selectedGood) {
        this.selectedGood = selectedGood;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
