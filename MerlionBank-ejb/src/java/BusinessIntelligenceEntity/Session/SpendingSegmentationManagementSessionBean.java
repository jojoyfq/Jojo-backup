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
import CommonEntity.Customer;
import Exception.ShopAlreadyExistedException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author a0113893
 */
@Stateless
public class SpendingSegmentationManagementSessionBean implements SpendingSegmentationManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Category> displayAllCategory() {
        Query q = em.createQuery("SELECT a FROM Category a");
        List<Category> categories = new ArrayList(q.getResultList());
        return categories;
    }

    @Override
    public List<Shop> displayShop(Long categoryId) {
        Category category = em.find(Category.class, categoryId);
        List<Shop> shops = category.getShops();
        List<Shop> result = new ArrayList<Shop>();
        for (int i = 0; i < shops.size(); i++) {
            if (shops.get(i).getStatus().equals("active")) {
                result.add(shops.get(i));
            }
        }
        System.out.println("In session bean"+result.size());
        return result;
    }

    @Override
    public List<Shop> addShop(Long categoryId, String shopName) throws ShopAlreadyExistedException {
        Category category = em.find(Category.class, categoryId);
        List<Shop> shops=category.getShops();
        for (int i=0;i<shops.size();i++){
            if (shops.get(i).getName().equals(shopName))
                throw new ShopAlreadyExistedException("A shop with same name is already existed in the selected category");
        }
        Shop shop = new Shop(shopName, "active", category);
        em.persist(shop);
        em.flush();
       
        List<Shop> current=category.getShops();
        current.add(shop);
        category.setShops(current);
        

        return displayShop(categoryId);

    }

    @Override
    public List<Shop> deleteShop(Long categoryId, Long shopId) {
        Category category = em.find(Category.class, categoryId);
        Shop shop = em.find(Shop.class, shopId);
        shop.setStatus("terminated");
        return displayShop(categoryId);
    }
    
       
    @Override
    public List<Shop> modifyShop(Long categoryId, Long shopId,String shopName, String newCategoryName){
        Shop shop = em.find(Shop.class, shopId);
        Category oldCategory=em.find(Category.class, categoryId);
        
        if (!oldCategory.getName().equals(newCategoryName)){
        List<Shop> oldShops=oldCategory.getShops();
        List<Shop> result=new ArrayList<Shop>();
        
        for (int i=0;i<oldShops.size();i++){
            if (!oldShops.get(i).getName().equals(shop.getName()))
                result.add(oldShops.get(i));
        }
        oldCategory.setShops(result);
        
        shop.setStatus("active");
        shop.setName(shopName);
        
        String name=newCategoryName;
        
         Query q = em.createQuery("SELECT a FROM Category a WHERE a.name = :name");
        q.setParameter("name", name);
        Category category=(Category)q.getSingleResult();
        shop.setCategory(category);
        
        List<Shop> shops=category.getShops();
        shops.add(shop);
        category.setShops(shops);
        em.flush();
        }
        
        return viewAllUnverifiedShop();
    }

    @Override
    public List<Shop> viewAllUnverifiedShop() {
        Query q = em.createQuery("SELECT a FROM Shop a");
        List<Shop> shops = new ArrayList(q.getResultList());
        List<Shop> result = new ArrayList<Shop>();

        for (int i = 0; i < shops.size(); i++) {
            if (shops.get(i).getStatus().equals("unverified")) {
                result.add(shops.get(i));
            }
        }

        return result;
    }
 
    

    @Override
    public List<Shop> assignUnverifiedShopCategory(String categoryName,Long shopId){
        Shop shop = em.find(Shop.class, shopId);
        shop.setStatus("active");
        String name=categoryName;
        
         Query q = em.createQuery("SELECT a FROM Category a WHERE a.name = :name");
        q.setParameter("name", name);
        Category category=(Category)q.getSingleResult();
        shop.setCategory(category);
        
        List<Shop> shops=category.getShops();
        shops.add(shop);
        category.setShops(shops);
        em.flush();
        
        return viewAllUnverifiedShop();
        
    }
    
    @Override
    public List<Coupon> viewAllCoupons(Long categoryId){
      Category category = em.find(Category.class, categoryId);
        List<Coupon> coupons = category.getCoupons();
        List<Coupon> result = new ArrayList<Coupon>();
        for (int i = 0; i < coupons.size(); i++) {
            if (coupons.get(i).getStatus().equals("active")) {
                result.add(coupons.get(i));
            }
        }
        return result;  
    }
    
    @Override
    public List<Coupon> addCoupon(Long categoryId, String shopName,String description, String couponCode) {
        Category category = em.find(Category.class, categoryId);
        Coupon coupon = new Coupon(shopName, description, couponCode, "active", category);
        em.persist(coupon);
        em.flush();
        
        List<Coupon> current=category.getCoupons();
        current.add(coupon);
        category.setCoupons(current);
        em.flush();

        return viewAllCoupons(categoryId);

    }

    @Override
    public List<Coupon> deleteCoupon(Long categoryId, Long couponId) {
        Category category = em.find(Category.class, categoryId);
        Coupon coupon = em.find(Coupon.class, couponId);
        coupon.setStatus("terminated");
        return viewAllCoupons(categoryId);
    }
    
    @Override
    public List<Interest> viewCustomerSegmentation(Long customerId){
       Customer customer=em.find(Customer.class,customerId);
       List<Interest> interests=new ArrayList<Interest>();
       
       try {
                interests = customer.getInterests();
            } catch (NullPointerException ex) {
                interests=addInterests(customer);
            }
       return interests;
    }
    
   private List<Interest> addInterests(Customer customer) {
Query q = em.createQuery("SELECT a FROM Category a");
            List<Category> categories = new ArrayList(q.getResultList());
            BigDecimal temp=new BigDecimal(0);
            List<Interest> interests=new ArrayList<Interest>();
            
            for (int i=0;i<categories.size();i++){
                Category category=categories.get(i);
                Interest interest=new Interest(temp,category,customer);
                em.persist(interest);
                em.flush();
                interests.add(interest);
            }
            
            customer.setInterests(interests);
            em.flush();
            return customer.getInterests();
    } 
}
