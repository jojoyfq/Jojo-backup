/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerRelationshipEntity.Session;

import CommonEntity.Customer;
import Exception.UserNotExistException;
import java.util.Date;
import javax.ejb.Remote;

/**
 *
 * @author a0113893
 */
@Remote
public interface OperationalCRMSessionBeanRemote {
  public Customer searchCustomer(String ic) throws UserNotExistException;
   public void updateProfile(Long staffID, Long customerID, String ic, String name, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String financialGoal);
}
