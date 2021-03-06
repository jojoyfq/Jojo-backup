/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity.Session;

import BillEntity.BillingOrganization;
import BillEntity.OtherBank;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ruijia
 */
@Local
public interface BillSessionBeanLocal {


    public void logStaffAction(String description, Long customerId, Long staffId);


    public boolean addBank(String bankName, String swiftCode, String UEN, String address);

    public List<OtherBank> viewBank();

    public List<String> viewBankNames();

    public OtherBank findBank(String bankName);

    public void modifyBank(String bankName, String address, String SWIFT, String UEN, Long bankId);

    public boolean addBO(String bOName, String bankName, Long accountNum, String UEN, String address);

    public BillingOrganization findBO(String bOName);

    public List<BillingOrganization> viewBO();

    public List<String> viewBOName();

    public void modifyBO(String boName, String boAddress, String boBankName, String boUEN, Long accountNumber, Long boId);

    public int deleteBO(String bOName);

    public int deleteBank(String bankName);

    
}
