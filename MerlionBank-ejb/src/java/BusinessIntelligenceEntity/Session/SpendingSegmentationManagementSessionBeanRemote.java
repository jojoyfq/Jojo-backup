/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessIntelligenceEntity.Session;

import BusinessIntelligenceEntity.Category;
import BusinessIntelligenceEntity.Coupon;
import BusinessIntelligenceEntity.Interest;
import BusinessIntelligenceEntity.Shop;
import Exception.ShopAlreadyExistedException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author a0113893
 */
@Remote
public interface SpendingSegmentationManagementSessionBeanRemote {
     public List<Category> displayAllCategory();
     public List<Shop> displayShop(Long categoryId);
     public List<Shop> addShop(Long categoryId, String shopName) throws ShopAlreadyExistedException;
     public List<Shop> deleteShop(Long categoryId, Long shopId);
     public List<Shop> modifyShop(Long categoryId, Long shopId,String shopName, String newCategoryName);
     
     public List<Shop> viewAllUnverifiedShop();
     //Display List of current category to let user choose
      public List<Shop> assignUnverifiedShopCategory(String categoryName,Long shopId);
      
       public List<Coupon> viewAllCoupons(Long categoryId);
        public List<Coupon> addCoupon(Long categoryId, String shopName,String description, String couponCode);
        public List<Coupon> deleteCoupon(Long categoryId, Long couponId);
        
        //For both customer and staff
        public List<Interest> viewCustomerSegmentation(Long customerId);
}
