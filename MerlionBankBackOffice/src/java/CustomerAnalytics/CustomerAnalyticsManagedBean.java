/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerAnalytics;

import BusinessIntelligenceEntity.Category;
import BusinessIntelligenceEntity.Coupon;
import BusinessIntelligenceEntity.Session.SpendingSegmentationManagementSessionBeanLocal;
import BusinessIntelligenceEntity.Shop;
import Exception.ShopAlreadyExistedException;
import java.io.IOException;
import java.io.Serializable;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author apple
 */
@Named(value = "customerAnalytics")
@SessionScoped
public class CustomerAnalyticsManagedBean implements Serializable {

    @EJB
    SpendingSegmentationManagementSessionBeanLocal ssmsbl;

    /**
     * Creates a new instance of CustomerAnalytics
     */
    public CustomerAnalyticsManagedBean() {
    }
    private String shopName;
    private String newCategoryName;
    private Long categoryId;
    private Long shopId;
    private String categoryName;
    private String description;
    private String couponCode;
    private Long customerId;
    private Long selectedCategoryId;
    private List<Category> allCategories;
    private List<Shop> allShops;
    private List<Shop> unverifiedShops;
    private List<Coupon> allCoupons;
    private Category selectedCategory;
    private Shop selectedShop;
    private List<String> categoryNames;
    private List<Long> splitedCategoryId;
    private Coupon selectedCoupon;

    @PostConstruct
    public void init() {
        allCategories = new ArrayList<>();
        allShops = new ArrayList<>();
        unverifiedShops = new ArrayList<>();
        allCoupons = new ArrayList<>();
        selectedCategory = new Category();
        selectedShop = new Shop();
        categoryNames = new ArrayList<>();
        splitedCategoryId = new ArrayList<>();
        selectedCoupon = new Coupon();
    }

    public void viewAllCategory(ActionEvent event) {
        allCategories = ssmsbl.displayAllCategory();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/CustomerAnalytics/viewAllCategories.xhtml");
        } catch (IOException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

    public void viewAllShops(ActionEvent event) {
        selectedCategory = (Category) event.getComponent().getAttributes().get("selectedCategory");
        System.out.println("******Selected category to view is " + selectedCategory.getId());
        allShops = ssmsbl.displayShop(selectedCategory.getId());
    }

    public void selectCategory(ActionEvent event) {
        selectedCategory = (Category) event.getComponent().getAttributes().get("selectedCategory");
        categoryId = selectedCategory.getId();

    }
    public void addShop(ActionEvent event) throws ShopAlreadyExistedException {
        try {
            System.out.println("******Selected category to view is " + categoryId);

            allShops = ssmsbl.addShop(categoryId, shopName);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully added shop " + shopName + "to Category (" + categoryId + ")!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        } catch (ShopAlreadyExistedException ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", ex.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void deleteShop(ActionEvent event) {
        System.out.println("******Selected category to view is " + categoryId);
        selectedShop = (Shop) event.getComponent().getAttributes().get("selectedShop");
        System.out.println("******Selected shop to view is " + selectedShop.getId());

        allShops = ssmsbl.deleteShop(categoryId, selectedShop.getId());

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully deleted shop " + selectedShop.getName() + "from Category (" + categoryId + ")!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }
    public void modifyShop(ActionEvent event){
                selectedShop = (Shop) event.getComponent().getAttributes().get("selectedShop");

            System.out.println("******Selected shop to modify is " + selectedShop.getId());
            allShops = ssmsbl.modifyShop(categoryId, selectedShop.getId(), shopName, newCategoryName);
    }
    public void viewAllUnverifiedShops(ActionEvent event){
        unverifiedShops = ssmsbl.viewAllUnverifiedShop();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/CustomerAnalytics/viewAllCategories.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(CustomerAnalyticsManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
           allCategories = ssmsbl.displayAllCategory();
        for(int i=0;i<allCategories.size();i++){
        categoryNames.add(allCategories.get(i).getId()+","+allCategories.get(i).getName());
        
        }
        System.out.println("*****Category names's size is"+categoryNames.size());

    }
    public void assignUnverifiedShopCategory(ActionEvent event){
//        for(int i=0;i<categoryNames.size();i++){
//           Long m = Long.parseLong((categoryNames.get(i).split(","))[0]);
//        splitedCategoryId.add(m);
//        }
        
                selectedShop = (Shop) event.getComponent().getAttributes().get("selectedShop");
            System.out.println("******Selected shop to add category is " + selectedShop.getId());

        for(int j=0;j<categoryNames.size();j++){
            if((categoryNames.get(j).split(","))[1] .equals(categoryName) ){
            
            allShops = ssmsbl.assignUnverifiedShopCategory(categoryName, selectedShop.getId());
                     FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully assinged "+categoryName+" successfully!");

                    RequestContext.getCurrentInstance().showMessageInDialog(message);

            }
        }
    }
    public void viewAllCoupons(ActionEvent event){
        System.out.println("******Selected category to view coupon is " + categoryId);
    allCoupons = ssmsbl.viewAllCoupons(categoryId);
    
    }
        public void addCoupon(ActionEvent event) throws ShopAlreadyExistedException {
            System.out.println("******Selected category to view is " + categoryId);

            allCoupons = ssmsbl.addCoupon(categoryId, shopName,description,couponCode);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully added coupon " + couponCode + "to Category (" + categoryId + ")!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
       
    }

    public void deleteCoupon(ActionEvent event) {
        System.out.println("******Selected category to view coupon is " + categoryId);
                selectedCoupon = (Coupon) event.getComponent().getAttributes().get("selectedCoupon");
        System.out.println("******Selected coupon to delete is " + selectedCoupon.getId());

        allCoupons = ssmsbl.deleteCoupon(categoryId, selectedCoupon.getId());

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully deleted coupon " + selectedCoupon.getId() + "from Category (" + categoryId + ")!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }
    
}
