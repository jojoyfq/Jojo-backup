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
    
    public List<OtherBank> viewBank(){
        List<OtherBank> otherBanks = new ArrayList<>();
        Query query = em.createQuery("SELECT a FROM OtherBank a");
        otherBanks = new ArrayList(query.getResultList());
        return otherBanks;
    }
    
//    public boolean deleteBank (String id){
//        
//    } 
    public void modifyBank(String newName, String oldName){
        OtherBank bank = this.findBank(oldName);
        bank.setName(newName);
        System.out.print("bank name changed");
        em.flush();       
    }
    
    public void addBO(String bOName, String bankName, Long accountNum){
      OtherBank bank = this.findBank(bankName);
      BillingOrganization bO = new BillingOrganization(bOName,bank,accountNum); 
    }
    
    public List<BillingOrganization> viewBO(){
        List<BillingOrganization> bOs = new ArrayList<>();
        Query q = em.createQuery("SELECT a FROM BillingOrganization a");
        bOs = new ArrayList(q.getResultList());
        return bOs;
    }
    
    public void modifyBO(String oldBOName, String newName, String newBankName, Long newAcctNum){
        BillingOrganization bO = this.findBO(oldBOName);
        if(!oldBOName.equalsIgnoreCase(newName)){
            bO.setName(newName);
            System.out.print("name changed");
        }
        if(!newBankName.equalsIgnoreCase(bO.getBank().getName())){
            OtherBank oldBank = bO.getBank();
            OtherBank newBank = this.findBank(newBankName);
            bO.setBank(newBank);
            oldBank.getBillingOrganization().remove(bO);
            newBank.getBillingOrganization().add(bO);
            System.out.print("bank changed");
        }
        if(!newAcctNum.equals(bO.getAccountNumber())){
            bO.setAccountNumber(newAcctNum);
            System.out.print("account number changed");
        }
        em.flush();
         
    }
    
    public int deleteBO(String bOName){
     BillingOrganization bO = this.findBO(bOName);
     if(bO.getGIROArrangement().isEmpty()&& bO.getRecurrentBillArrangement().isEmpty()){
         for(int i =0;i<bO.getBillRecord().size();i++){
             if(bO.getBillRecord().get(i).getStatus().equalsIgnoreCase("pending")){
                 System.out.print("have pending bill");
                 return 1;           
             }
         }
     bO.setStatus("deleted");
     OtherBank bank = bO.getBank();
     bank.getBillingOrganization().remove(bO);
     System.out.print("successfully removed");
     return 3;
     }else{
         System.out.print("have giro and recurrent arrangement");
         return 2;
     }
     
    }
    
    public BillingOrganization findBO(String bOName){
        Query q = em.createQuery("SELECT a FROM BillingOrganization a WHERE a.name = :bOName");
        q.setParameter("bOName", bOName);
        BillingOrganization bO = (BillingOrganization) q.getResultList().get(0);
        return bO;  
    }
    
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
