/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity.Session;

import javax.ejb.Local;

/**
 *
 * @author ruijia
 */
@Local
public interface BillSessionBeanLocal {


    public void logStaffAction(String description, Long customerId, Long staffId);


    public boolean addBank(String bankName, String swiftCode, String UEN, String address);
    
}
