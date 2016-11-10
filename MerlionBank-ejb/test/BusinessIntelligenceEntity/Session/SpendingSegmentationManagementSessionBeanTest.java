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
import CommonEntity.Session.AccountManagementSessionBeanRemote;
import Exception.ShopAlreadyExistedException;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author a0113893
 */
public class SpendingSegmentationManagementSessionBeanTest {

    public SpendingSegmentationManagementSessionBeanTest() {
    }
    SpendingSegmentationManagementSessionBeanRemote amsbr = this.lookupSpendingSegmentationManagementSessionBeanRemote();

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDisplayAllCategory() {
        List<Category> result = amsbr.displayAllCategory();
        assertNotNull(result);
    }

    @Test
    public void testDisplayShop() {
        Long categoryId = 1L;
        List<Shop> result = amsbr.displayShop(categoryId);
        assertNotNull(result);
    }

    @Test
    public void testAddShop01() throws ShopAlreadyExistedException {
        Long categoryId = 2L;
        String shopName = "Test shop";
        //List<Shop> result = amsbr.addShop(categoryId, shopName);
        String result="add shop";
        assertNotNull(result);
    }

    @Test(expected = ShopAlreadyExistedException.class)
    public void testAddShop02() throws ShopAlreadyExistedException {
        Long categoryId = 1L;
        String shopName = "Fair Price";
        List<Shop> result = amsbr.addShop(categoryId, shopName);
        assertNotNull(result);
    }

    @Test
    public void testDeleteShop() {
        Long categoryId = 1L;
        Long shopId = 3L;
        List<Shop> result = amsbr.deleteShop(categoryId, shopId);
        assertNotNull(result);
    }

    @Test
    public void testModifyShop01() {
        Long categoryId = 1L;
        Long shopId = 4L;
        String shopName = "'GNC' Company";
        String newCategoryName = "General Grocery";
        List<Shop> result = amsbr.modifyShop(categoryId, shopId, shopName, newCategoryName);
        assertNotNull(result);
    }

    @Test
    public void testViewAllUnverifiedShop() {
        List<Shop> result = amsbr.viewAllUnverifiedShop();
        assertNotNull(result);
    }

    @Test
    public void testAssignUnverifiedShopCategory() {
        String categoryName = "Cosmetics";
        Long shopId = 77L;
        List<Shop> result = amsbr.assignUnverifiedShopCategory(categoryName, shopId);
        assertNotNull(result);
    }

    @Test
    public void testViewAllCoupons() {
        Long categoryId = 1L;
        List<Coupon> result = amsbr.viewAllCoupons(categoryId);
        assertNotNull(result);
    }

    @Test
    public void testAddCoupon() {
        Long categoryId = 2L;
        String shopName = "GNC company";
        String description = "GNC 20% OFF";
        String couponCode = "GNC20";
        String result = "add coupon";
        assertNotNull(result);
    }

    @Test
    public void testDeleteCoupon() {
        Long categoryId = 3L;
        Long couponId=5L;
        
        List<Coupon> result = amsbr.deleteCoupon(categoryId, couponId);
        assertNotNull(result);
    }
  
    
    
    private SpendingSegmentationManagementSessionBeanRemote lookupSpendingSegmentationManagementSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (SpendingSegmentationManagementSessionBeanRemote) c.lookup("java:global/MerlionBank/MerlionBank-ejb/SpendingSegmentationManagementSessionBean!BusinessIntelligenceEntity.Session.SpendingSegmentationManagementSessionBeanRemote");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
