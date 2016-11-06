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
@Named(value = "customerAnalyticsManagedBean")
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
    private Long selectedShopId;
    private Long selectedCouponId;

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

        allCategories = ssmsbl.displayAllCategory();
        for (int i = 0; i < allCategories.size(); i++) {
            categoryNames.add(allCategories.get(i).getId() + "," + allCategories.get(i).getName());

        }
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
        System.out.println("All Shop size "+allShops.size());
    }

    public void selectCategory(ActionEvent event) {
        selectedCategory = (Category) event.getComponent().getAttributes().get("selectedCategory");
        categoryId = selectedCategory.getId();

    }

    public void selectShop(ActionEvent event) {
        selectedShop = (Shop) event.getComponent().getAttributes().get("selectedShop");
        selectedShopId = selectedShop.getId();
        System.out.println("*****Selected shop id is " + selectedShopId);
    }

    public void selectCoupon(ActionEvent event) {
        selectedCoupon = (Coupon) event.getComponent().getAttributes().get("selectedCoupon");
        selectedCouponId = selectedCoupon.getId();
        System.out.println("*****Selected shop id is " + selectedCouponId);
    }

    public void selectShopToModify(ActionEvent event) throws IOException {
        selectedShop = (Shop) event.getComponent().getAttributes().get("selectedShop");
        selectedShopId = selectedShop.getId();
        System.out.println("*****Selected shop id is " + selectedShopId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/CustomerAnalytics/modifyShop.xhtml");

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

    public void deleteShop(Long shopId) {
        System.out.println("******Selected category to view is " + categoryId);
        // selectedShop = (Shop) event.getComponent().getAttributes().get("selectedShop");
        System.out.println("******Selected shop to view is " + shopId);

        allShops = ssmsbl.deleteShop(categoryId, selectedShop.getId());

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully deleted shop " + selectedShop.getName() + "from Category (" + categoryId + ")!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    public void modifyShop(ActionEvent event) {
        selectedShop = (Shop) event.getComponent().getAttributes().get("selectedShop");
        System.out.println("******Selected categoryID  is " + categoryId);
        System.out.println("******Selected shop to modify is " + selectedShop.getId());
        newCategoryName = (newCategoryName.split(","))[1];
        System.out.println("******New Category Name is " + newCategoryName);
        System.out.println("******Shop Name is " + shopName);

        allShops = ssmsbl.modifyShop(categoryId, selectedShop.getId(), shopName, newCategoryName);
    }

    public void viewAllUnverifiedShops(ActionEvent event) {
        unverifiedShops = ssmsbl.viewAllUnverifiedShop();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/CustomerAnalytics/viewUnverifiedShops.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(CustomerAnalyticsManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
//           allCategories = ssmsbl.displayAllCategory();
//        for(int i=0;i<allCategories.size();i++){
//        categoryNames.add(allCategories.get(i).getId()+","+allCategories.get(i).getName());
//        
//        }
        System.out.println("*****Category names's size is" + categoryNames.size());

    }

    public void assignUnverifiedShopCategory(ActionEvent event) {
//        for(int i=0;i<categoryNames.size();i++){
//           Long m = Long.parseLong((categoryNames.get(i).split(","))[0]);
//        splitedCategoryId.add(m);
//        }

        selectedShop = (Shop) event.getComponent().getAttributes().get("selectedShop");
        System.out.println("******Selected shop to add category is " + selectedShop.getId());
        categoryName = (categoryName.split(","))[1];
        System.out.println("****Select category name: " + categoryName);

        allShops = ssmsbl.assignUnverifiedShopCategory(categoryName, selectedShop.getId());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully assinged " + categoryName + " successfully!");

        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    public void viewAllCoupons(ActionEvent event) throws IOException {
        selectedCategory = (Category) event.getComponent().getAttributes().get("selectedCategory");
        categoryId = selectedCategory.getId();
        System.out.println("******Selected category to view coupon is " + categoryId);
        allCoupons = ssmsbl.viewAllCoupons(categoryId);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/CustomerAnalytics/viewAllCoupons.xhtml");

    }

    public void addCoupon(ActionEvent event) throws ShopAlreadyExistedException {
        System.out.println("******Selected category to view is " + categoryId);

        allCoupons = ssmsbl.addCoupon(categoryId, shopName, description, couponCode);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully added coupon " + couponCode + "to Category (" + categoryId + ")!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    public void deleteCoupon(Long couponId) {
        System.out.println("******Selected category to view coupon is " + categoryId);
        //  selectedCoupon = (Coupon) event.getComponent().getAttributes().get("selectedCoupon");
        System.out.println("******Selected coupon to delete is " + couponId);

        allCoupons = ssmsbl.deleteCoupon(categoryId, couponId);

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "You have successfully deleted coupon " + selectedCoupon.getId() + "from Category (" + categoryId + ")!");
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    public SpendingSegmentationManagementSessionBeanLocal getSsmsbl() {
        return ssmsbl;
    }

    public void setSsmsbl(SpendingSegmentationManagementSessionBeanLocal ssmsbl) {
        this.ssmsbl = ssmsbl;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getNewCategoryName() {
        return newCategoryName;
    }

    public void setNewCategoryName(String newCategoryName) {
        this.newCategoryName = newCategoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(Long selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public List<Category> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(List<Category> allCategories) {
        this.allCategories = allCategories;
    }

    public List<Shop> getAllShops() {
        return allShops;
    }

    public void setAllShops(List<Shop> allShops) {
        this.allShops = allShops;
    }

    public List<Shop> getUnverifiedShops() {
        return unverifiedShops;
    }

    public void setUnverifiedShops(List<Shop> unverifiedShops) {
        this.unverifiedShops = unverifiedShops;
    }

    public List<Coupon> getAllCoupons() {
        return allCoupons;
    }

    public void setAllCoupons(List<Coupon> allCoupons) {
        this.allCoupons = allCoupons;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public Shop getSelectedShop() {
        return selectedShop;
    }

    public void setSelectedShop(Shop selectedShop) {
        this.selectedShop = selectedShop;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public List<Long> getSplitedCategoryId() {
        return splitedCategoryId;
    }

    public void setSplitedCategoryId(List<Long> splitedCategoryId) {
        this.splitedCategoryId = splitedCategoryId;
    }

    public Coupon getSelectedCoupon() {
        return selectedCoupon;
    }

    public void setSelectedCoupon(Coupon selectedCoupon) {
        this.selectedCoupon = selectedCoupon;
    }

    public Long getSelectedShopId() {
        return selectedShopId;
    }

    public void setSelectedShopId(Long selectedShopId) {
        this.selectedShopId = selectedShopId;
    }

    public Long getSelectedCouponId() {
        return selectedCouponId;
    }

    public void setSelectedCouponId(Long selectedCouponId) {
        this.selectedCouponId = selectedCouponId;
    }

}
