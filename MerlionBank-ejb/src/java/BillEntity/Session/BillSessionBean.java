/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity.Session;

import BillEntity.BillingOrganization;
import BillEntity.OtherBank;
import CommonEntity.Staff;
import CustomerRelationshipEntity.StaffAction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public boolean addBank(String bankName,String swiftCode,String UEN,String address){
        List<OtherBank> existingBanks = this.viewBank();
     
      for(int i=0;i<existingBanks.size();i++){
          if(existingBanks.get(i).getName().equalsIgnoreCase(bankName)||existingBanks.get(i).getSwiftCode().equalsIgnoreCase(swiftCode)||existingBanks.get(i).getUEN().equalsIgnoreCase(UEN)){
              System.out.print("Existing name, SWIFT or UEN");
              return false;
          }
      }
      OtherBank bank = new OtherBank(bankName,swiftCode,UEN,address);
      em.persist(bank);
      return true;
    }
    
        @Override
    public List<OtherBank> viewBank(){
        List<OtherBank> otherBanks = new ArrayList<>();
        Query query = em.createQuery("SELECT a FROM OtherBank a");
        otherBanks = new ArrayList(query.getResultList());
        return otherBanks;
    }
    
        @Override
    public List<String> viewBankNames(){
        List<OtherBank> otherBanks = this.viewBank();
        List<String> bankNames = new ArrayList<>();
        for(int i=0;i<otherBanks.size();i++ ){
            if(otherBanks.get(i).getStatus().equalsIgnoreCase("active")){
            bankNames.add(otherBanks.get(i).getName());}
        }
        return bankNames;
    }
    
//    public boolean deleteBank (String id){
//        
//    } 
        @Override
    public void modifyBank(String bankName, String address, String SWIFT, String UEN, Long bankId){
        OtherBank bank = em.find(OtherBank.class,bankId);
        bank.setAddress(address);
        bank.setName(bankName);
        bank.setUEN(UEN);
        bank.setSwiftCode(SWIFT);
        em.flush();
    }
    
        @Override
    public boolean addBO(String bOName, String bankName, Long accountNum,String UEN, String address){
        List<BillingOrganization> bOs = this.viewBO();
        OtherBank bank = this.findBank(bankName);
        for(int i=0;i<bOs.size();i++){
            if(bOs.get(i).getName().equalsIgnoreCase(bOName)||bOs.get(i).getUEN().equals(UEN)||(bOs.get(i).getBank().equals(bank)&&bOs.get(i).getAccountNumber().equals(accountNum))){
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
    public List<BillingOrganization> viewBO(){
        List<BillingOrganization> bOs = new ArrayList<>();
        Query q = em.createQuery("SELECT a FROM BillingOrganization a");
        bOs = new ArrayList(q.getResultList());
        return bOs;
    }
    
        @Override
    public List<String> viewBOName(){
        List<BillingOrganization> bos = this.viewBO();
        List<String> boNames = new ArrayList<>();
        for(int i =0;i<bos.size();i++){
            if(bos.get(i).getStatus().equalsIgnoreCase("active")){
               boNames.add(bos.get(i).getName());
            }
        }
        return boNames;
    }
    
        @Override
    public void modifyBO(String boName, String boAddress, String boBankName,String boUEN, Long accountNumber, Long boId){
        BillingOrganization bo = em.find(BillingOrganization.class, boId);
        bo.setAccountNumber(accountNumber);
        bo.setAddress(boAddress);
        bo.setName(boName);
        bo.setUEN(boUEN);
        if(!bo.getBank().getName().equalsIgnoreCase(boBankName)){
        OtherBank oldBank =  bo.getBank();
        oldBank.getBillingOrganization().remove(bo);
        OtherBank newBank = this.findBank(boBankName);
        bo.setBank(newBank);
        newBank.getBillingOrganization().add(bo);
        }
        em.flush();
         
    }
    
        @Override
    public int deleteBO(String bOName){
     BillingOrganization bO = this.findBO(bOName);
     //when giro and recurrent ends, disconnect them with BO
     if(bO.getGIROArrangement().isEmpty()&& bO.getRecurrentBillArrangement().isEmpty()){
         for(int i =0;i<bO.getBillRecord().size();i++){
             if(bO.getBillRecord().get(i).getStatus().equalsIgnoreCase("pending")){
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
     }else{
         System.out.print("have giro and recurrent arrangement");
         return 2; //have arrangement
     }     
    }
    
        @Override
    public int deleteBank(String bankName){
        OtherBank bank =  this.findBank(bankName);
        if(bank.getBillingOrganization().isEmpty()){
            for(int i=0;i<bank.getTransactionRecord().size();i++){
                if(bank.getTransactionRecord().get(i).getStatus().equalsIgnoreCase("pending")){
                    System.out.print("have pending transaction!");
                    return 2;
                }
            }
            bank.setStatus("terminated");
            System.out.print("successfully removed");
            em.flush();
            return 3;
        }else{
            System.out.print("have associated BO");
            return 1;
        }  
    }
    
        @Override
    public BillingOrganization findBO(String bOName){
        Query q = em.createQuery("SELECT a FROM BillingOrganization a WHERE a.name = :bOName");
        q.setParameter("bOName", bOName);
        BillingOrganization bO = (BillingOrganization) q.getResultList().get(0);
        return bO;  
    }
    
        @Override
    public OtherBank findBank(String bankName){
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
    
}
