/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity.Session;

import BillEntity.BillRecord;
import BillEntity.BillingOrganization;
import BillEntity.GIROArrangement;
import BillEntity.OtherBank;
import BillEntity.RecurrentBillArrangement;
import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CommonEntity.Staff;
import CustomerRelationshipEntity.StaffAction;
import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joda.time.DateTime;

/**
 *
 * @author ruijia
 */
@Stateless
public class BillSessionBean implements BillSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public boolean addBank(String bankName, String swiftCode, String UEN, String address) {
        List<OtherBank> existingBanks = this.viewBank();

        for (int i = 0; i < existingBanks.size(); i++) {
            if (existingBanks.get(i).getName().equalsIgnoreCase(bankName) || existingBanks.get(i).getSwiftCode().equalsIgnoreCase(swiftCode) || existingBanks.get(i).getUEN().equalsIgnoreCase(UEN)) {
                System.out.print("Existing name, SWIFT or UEN");
                return false;
            }
        }
        OtherBank bank = new OtherBank(bankName, swiftCode, UEN, address);
        em.persist(bank);
        return true;
    }

    @Override
    public List<OtherBank> viewBank() {
        List<OtherBank> otherBanks = new ArrayList<>();
        Query query = em.createQuery("SELECT a FROM OtherBank a");
        otherBanks = new ArrayList(query.getResultList());
        return otherBanks;
    }

    @Override
    public List<String> viewBankNames() {
        List<OtherBank> otherBanks = this.viewBank();
        List<String> bankNames = new ArrayList<>();
        for (int i = 0; i < otherBanks.size(); i++) {
            if (otherBanks.get(i).getStatus().equalsIgnoreCase("active")) {
                bankNames.add(otherBanks.get(i).getName());
            }
        }
        return bankNames;
    }

//    public boolean deleteBank (String id){
//        
//    } 
    @Override
    public void modifyBank(String bankName, String address, String SWIFT, String UEN, Long bankId) {
        OtherBank bank = em.find(OtherBank.class, bankId);
        bank.setAddress(address);
        bank.setName(bankName);
        bank.setUEN(UEN);
        bank.setSwiftCode(SWIFT);
        em.flush();
    }

    @Override
    public boolean addBO(String bOName, String bankName, Long accountNum, String UEN, String address) {
        List<BillingOrganization> bOs = this.viewBO();
        OtherBank bank = this.findBank(bankName);
        for (int i = 0; i < bOs.size(); i++) {
            if (bOs.get(i).getName().equalsIgnoreCase(bOName) || bOs.get(i).getUEN().equals(UEN) || (bOs.get(i).getBank().equals(bank) && bOs.get(i).getAccountNumber().equals(accountNum))) {
                System.out.print("Error! Existing BO name/acct/uen");
                return false;
            }
        }

        BillingOrganization bO = new BillingOrganization(bOName, bank, accountNum, UEN, address);
        bank.getBillingOrganization().add(bO);
        em.persist(bO);
        em.flush();
        return true;
    }

    @Override
    public List<BillingOrganization> viewBO() {
        List<BillingOrganization> bOs = new ArrayList<>();
        Query q = em.createQuery("SELECT a FROM BillingOrganization a");
        bOs = new ArrayList(q.getResultList());
        return bOs;
    }

    @Override
    public List<String> viewBOName() {
        List<BillingOrganization> bos = this.viewBO();
        List<String> boNames = new ArrayList<>();
        for (int i = 0; i < bos.size(); i++) {
            if (bos.get(i).getStatus().equalsIgnoreCase("active")) {
                boNames.add(bos.get(i).getName());
            }
        }
        return boNames;
    }

    @Override
    public void modifyBO(String boName, String boAddress, String boBankName, String boUEN, Long accountNumber, Long boId) {
        BillingOrganization bo = em.find(BillingOrganization.class, boId);
        bo.setAccountNumber(accountNumber);
        bo.setAddress(boAddress);
        bo.setName(boName);
        bo.setUEN(boUEN);
        if (!bo.getBank().getName().equalsIgnoreCase(boBankName)) {
            OtherBank oldBank = bo.getBank();
            oldBank.getBillingOrganization().remove(bo);
            OtherBank newBank = this.findBank(boBankName);
            bo.setBank(newBank);
            newBank.getBillingOrganization().add(bo);
        }
        em.flush();

    }

    @Override
    public int deleteBO(String bOName) {
        BillingOrganization bO = this.findBO(bOName);
        //when giro and recurrent ends, disconnect them with BO
        if (bO.getGIROArrangement().isEmpty() && bO.getRecurrentBillArrangement().isEmpty()) {
            for (int i = 0; i < bO.getBillRecord().size(); i++) {
                if (bO.getBillRecord().get(i).getStatus().equalsIgnoreCase("pending")) {
                    System.out.print("have pending bill");
                    return 1;  //have pending bill         
                }
            }
            bO.setStatus("terminated");
            OtherBank bank = bO.getBank();
            bank.getBillingOrganization().remove(bO);
            System.out.print("successfully removed");
            em.flush();
            return 3; //successful
        } else {
            System.out.print("have giro and recurrent arrangement");
            return 2; //have arrangement
        }
    }

    @Override
    public int deleteBank(String bankName) {
        OtherBank bank = this.findBank(bankName);
        if (bank.getBillingOrganization().isEmpty()) {
            for (int i = 0; i < bank.getTransactionRecord().size(); i++) {
                if (bank.getTransactionRecord().get(i).getStatus().equalsIgnoreCase("pending")) {
                    System.out.print("have pending transaction!");
                    return 2;
                }
            }
            bank.setStatus("terminated");
            System.out.print("successfully removed");
            em.flush();
            return 3;
        } else {
            System.out.print("have associated BO");
            return 1;
        }
    }

    @Override
    public BillingOrganization findBO(String bOName) {
        Query q = em.createQuery("SELECT a FROM BillingOrganization a WHERE a.name = :bOName");
        q.setParameter("bOName", bOName);
        BillingOrganization bO = (BillingOrganization) q.getResultList().get(0);
        return bO;
    }

    @Override
    public OtherBank findBank(String bankName) {
        Query q = em.createQuery("SELECT a FROM OtherBank a WHERE a.name = :bankName");
        q.setParameter("bankName", bankName);
        OtherBank bank = (OtherBank) q.getResultList().get(0);
        return bank;
    }

    @Override
    public void logStaffAction(String description, Long customerId, Long staffId) {
        List<StaffAction> actions = new ArrayList<>();
        System.out.print(customerId);
        System.out.print(staffId);

        Staff staff = em.find(Staff.class, staffId);
        StaffAction action = new StaffAction(Calendar.getInstance().getTime(), description, customerId, staff);
        em.persist(action);
        System.out.print(action.getDescription());
        if (staff.getStaffActions() == null) {
            actions.add(action);
            staff.setStaffActions(actions);
            em.persist(actions);
        } else {
            staff.getStaffActions().add(action);
        }
        em.flush();
    }

    @Override
    public boolean addGIROArrangement(String customerName, String boName, BigDecimal limit, Long savingAcctNum, String billReference) {
        //check if got existing arrangement with same bo and reference
        SavingAccount savingAcct = this.findSavingAccount(savingAcctNum);
        BillingOrganization bo = this.findBO(boName);
        List<GIROArrangement> existingGIRO = savingAcct.getGiroArrangement();
        if (!existingGIRO.isEmpty()) {
            for (int i = 0; i < existingGIRO.size(); i++) {
                if (existingGIRO.get(i).getBillReference().equalsIgnoreCase(billReference) && existingGIRO.get(i).getBillingOrganization().getName().equalsIgnoreCase(boName)) {
                    System.out.print("same BO and same reference alr exist!");
                    return false;
                }
            }
            GIROArrangement giroArrangement = new GIROArrangement(customerName, limit, bo, billReference, savingAcct);
            em.persist(giroArrangement);
            bo.getGIROArrangement().add(giroArrangement);
            savingAcct.getGiroArrangement().add(giroArrangement);
            String description = "Add giro arrangement" + giroArrangement.getId();
            this.logAction(description, savingAcct.getCustomer().getId());
            em.flush();
            return true;
        } else {
            GIROArrangement giroArrangement = new GIROArrangement(customerName, limit, bo, billReference, savingAcct);
            em.persist(giroArrangement);
            bo.getGIROArrangement().add(giroArrangement);
            savingAcct.getGiroArrangement().add(giroArrangement);
            String description = "Add giro arrangement" + giroArrangement.getId();
            this.logAction(description, savingAcct.getCustomer().getId());
            em.flush();
            return true;
        }
    }

    @Override
    public boolean addRecurrentArrangement(String boName, BigDecimal amount, Long savingAccountNumber, String billReference, Integer times, Integer interval, Date StartDate) {
        SavingAccount savingAcct = this.findSavingAccount(savingAccountNumber);
        BillingOrganization bo = this.findBO(boName);
        RecurrentBillArrangement recurrent = new RecurrentBillArrangement(amount, bo, billReference, savingAcct, times, StartDate, interval, times);
        em.persist(recurrent);
        savingAcct.getRecurrentBillArrangement().add(recurrent);
        bo.getRecurrentBillArrangement().add(recurrent);
        String description = "Add recurrent arrangement" + recurrent.getId();
        this.logAction(description, savingAcct.getCustomer().getId());
        em.flush();
        return true;
    }

    private SavingAccount findSavingAccount(Long accountNum) {
        SavingAccount account = new SavingAccount();
        Query m = em.createQuery("SELECT b FROM SavingAccount b WHERE b.accountNumber = :savingAccountNumber");
        m.setParameter("savingAccountNumber", accountNum);
        account = (SavingAccount) m.getResultList().get(0);
        return account;
    }

    private void logAction(String description, Long customerId) {
        List<CustomerAction> actions = new ArrayList<>();
        Customer customer = em.find(Customer.class, customerId);
        CustomerAction action = new CustomerAction((Calendar.getInstance().getTime()), description, customer);
        em.persist(action);
        System.out.print(action.getDescription());
        if (customer.getCustomerActions() == null) {
            actions.add(action);
            customer.setCustomerActions(actions);
            em.persist(actions);
        } else {
            customer.getCustomerActions().add(action);
        }
        em.flush();
    }

    @Override
    public boolean adHocBill(String boName, Long accountNumber, String billReference, BigDecimal amount) {
        SavingAccount savingAccount = this.findSavingAccount(accountNumber);
        BillingOrganization bo = this.findBO(boName);
        System.out.print("balance before payment " + savingAccount.getAvailableBalance());
        if (savingAccount.getAvailableBalance().compareTo(amount) != -1) {
            savingAccount.setAvailableBalance(savingAccount.getAvailableBalance().subtract(amount));
            savingAccount.setBalance(savingAccount.getBalance().subtract(amount));
            System.out.print("balance after payment " + savingAccount.getAvailableBalance());
            Date todayDate = new Date();
            BillRecord bill = new BillRecord(bo, billReference, "BI", amount, null, "settled", "Bill payment to " + boName, todayDate, accountNumber, bo.getAccountNumber(), savingAccount, "Merlion", bo.getBank().getName());
            em.persist(bill);
            savingAccount.getTransactionRecord().add(bill);
            //invoke webservice to send bill
            String description = "Bill payment to " + boName;
            this.logAction(description, savingAccount.getCustomer().getId());
            em.flush();
            return true;
        } else {
            return false;
        }

    }
//for webservice

    @Override
    public List<String> getPendingGIRO(String boName) {
        System.out.print("GIRO Organization name is: " + boName);

        Query m = em.createQuery("SELECT b FROM GIROArrangement b WHERE b.status = :status");
        m.setParameter("status", "pending");
        List<GIROArrangement> allGIRO = m.getResultList();
        List<String> giroInfo = new ArrayList<>();
        String oneGIRO = new String();
        for (int i = 0; i < allGIRO.size(); i++) {
            if (allGIRO.get(i).getBillingOrganization().getName().equalsIgnoreCase(boName)) {
                String concat = oneGIRO.concat(allGIRO.get(i).getId().toString() + "," + allGIRO.get(i).getBillReference() + "," + allGIRO.get(i).getCustomerName() + "," + allGIRO.get(i).getDeductionLimit().toString());
                giroInfo.add(concat);
            }
        }

        return giroInfo;
    }

    public String deductGIRO(Long id, Double amount) {
        System.out.print("giro id is" + id);
        GIROArrangement giro = em.find(GIROArrangement.class, id);
        BigDecimal deductAmount = BigDecimal.valueOf(amount);
        if (giro.getDeductionLimit().compareTo(deductAmount) != -1) {
            if (giro.getSavingAccount().getAvailableBalance().compareTo(deductAmount) != -1) {
                giro.getSavingAccount().setAvailableBalance(giro.getSavingAccount().getAvailableBalance().subtract(deductAmount));
                giro.getSavingAccount().setBalance(giro.getSavingAccount().getBalance().subtract(deductAmount));
                return "Deduction success!";
            } else {
                return "Deduction fail! not enough balance";
            }
        } else {
            return "Deduction fail! amount more than limit.";
        }
    }
//for webservice

    @Override
    public boolean approveGIRO(Long id, String status, String deductDay) throws ParseException {
        System.out.print("giro id is " + id);

        GIROArrangement giro = em.find(GIROArrangement.class, id);
        giro.setStatus(status);
        if (status.equalsIgnoreCase("active")) {
            SimpleDateFormat dateFormator = new SimpleDateFormat("dd-MM-yyyy");
            DateTime deductDate = new DateTime(dateFormator.parse(deductDay));
            System.out.print(deductDate);
            giro.setDeductionDay(deductDate);
        }
        return true;
    }

    @Override
    public List<GIROArrangement> viewableGIRO(Long customerId) {
        Customer customer = em.find(Customer.class, customerId);
        List<SavingAccount> savingAccounts = customer.getSavingAccounts();
        List<GIROArrangement> viewable = new ArrayList<GIROArrangement>();
        for (int i = 0; i < savingAccounts.size(); i++) {
            for (int j = 0; j < savingAccounts.get(i).getGiroArrangement().size(); j++) {
                if (!savingAccounts.get(i).getGiroArrangement().get(j).getStatus().equalsIgnoreCase("terminated")) {
                    viewable.add(savingAccounts.get(i).getGiroArrangement().get(j));
                }
            }
        }
        return viewable;
    }

    //send today's interbank transactions to SACH
    @Override
    public List<TransactionRecord> sendInterbankTransactions() {
        Date todayDate = new Date();
        SimpleDateFormat dateFormator = new SimpleDateFormat("dd-MM-yyyy");
        String todayDateStr = dateFormator.format(todayDate);
        List<TransactionRecord> records = new ArrayList<>();
        Query m = em.createQuery("SELECT a FROM TransactionRecord a");
        List<TransactionRecord> allRecords = m.getResultList();
        for (int i = 0; i < allRecords.size(); i++) {
            if (dateFormator.format(allRecords.get(i).getTransactionTime()).equalsIgnoreCase(todayDateStr)) {
                if (allRecords.get(i).getCode().equalsIgnoreCase("INTF") || allRecords.get(i).getCode().equalsIgnoreCase("BI")) {
                   records.add(allRecords.get(i));
                }
            }
        }
        return records;
    }
    
   
    
    

}
