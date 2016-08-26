/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.OnlineAccount;
import CommonEntity.Customer;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Stateless;

/**
 *
 * @author a0113893
 */
@Stateless
public class AccountManagementSessionBean implements AccountManagementSessionBeanLocal {
    
    public Customer Create(String IC, String name, String gender, Date dateOfBirth, String addresss, String email, Long phoneNumber, String occupation, String familyInfo, BigDecimal financialAsset, String financialGoal, Double riskRating, OnlineAccount onlineAccount) {
     create user 
             
        return null;  
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
