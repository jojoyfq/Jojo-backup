/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import Exception.UserExistException;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface AccountManagementSessionBeanLocal  {
     public void createSavingAccount(String IC, String name, String gender, Date dateOfBirth, String addresss, String email, String phoneNumber, String occupation, String familyInfo, String financialAsset, String financialGoal);//throws UserExistException;
             }
