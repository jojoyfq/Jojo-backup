/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity.Session;

import BillEntity.BillingOrganization;
import BillEntity.GIROArrangement;
import BillEntity.OtherBank;
import DepositEntity.TransactionRecord;
import Exception.NotEnoughAmountException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.joda.time.DateTime;

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

    public boolean addGIROArrangement(String customerName, String boName, BigDecimal limit, Long savingAcctNum, String billReference);

    public boolean addRecurrentArrangement(String boName, BigDecimal amount, Long savingAccountNumber, String billReference, Integer times, Integer interval, Date StartDate);

    public boolean adHocBill(String boName, Long accountNumber, String billReference, BigDecimal amount);

    public List<GIROArrangement> viewableGIRO(Long customerId);  

    public List<String> getPendingGIRO(String boName);

    public boolean approveGIRO(Long id, String boName, String deductDay) throws ParseException;

    public List<TransactionRecord> sendInterbankTransactions();

}
