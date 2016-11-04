/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CounterCashEntity.Session;

import CounterCash.CounterCash;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author shuyunhuang
 */
@Stateless
public class CounterCashManagement implements CounterCashManagementLocal {
    @PersistenceContext 
    private EntityManager em;
    private BigDecimal startAmount;
    private BigDecimal endAmount;
    private Date startTime;
    private Date endTime;
    private String staffName;
    
    @Override
    public void recordAmount(BigDecimal amount, Date time,Long staffName){
        
        Query q = em.createQuery("SELECT a FROM CounterCash");
        List<CounterCash> counterCashLists = new ArrayList(q.getResultList());
       
        for (int i = 0; i < counterCashLists.size(); i ++){
//            Date startTime = counterCashLists.get(i).;
            String name = counterCashLists.get(i).getStaffName();
            if(staffName.equals(name)){ //the staff have a initial entry 
                counterCashLists.get(i).setEndAmt(amount); //set the end amount
                counterCashLists.get(i).setEndTime(time); //set the end time
                
            }
        }
    }
         
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private Boolean checkCounterCashAmount(){
        return true;
    }
}
