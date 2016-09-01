/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.Customer;
import Exception.UserExistException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface AccountManagementSessionBeanLocal  {
     public void createSavingAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialAsset, String financialGoal);//throws UserExistException;
     public Customer diaplayCustomer(String ic); 
     public void updateProfile(String ic, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialGoal) throws UserExistException;
    public String activateAccountVerifyDetail(String ic, String fullName, Date dateOfBirth,String phoneNumber);
public String verifyAccountBalance(String ic);
public boolean checkPasswordComplexity(String password);
}

