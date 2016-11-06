/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CounterCashEntity.Session;

import CounterCash.CashServiceRecord;
import CounterCash.CounterCashRecord;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import static org.joda.time.format.ISODateTimeFormat.date;

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
    private Long staffId;
    private CounterCashRecord counterCashRecord;

    @Override
    public void recordAmount(BigDecimal amount, Date time, Long staffId) {

        Query q = em.createQuery("SELECT a FROM CounterCashRecord a");
        List<CounterCashRecord> counterCashLists = new ArrayList(q.getResultList());

        if (counterCashLists.size() == 0) {
            System.out.println("********** staff id is ********** " + staffId);
            counterCashRecord = new CounterCashRecord(amount, time, staffId);
            em.persist(counterCashRecord);
        } else {
            for (int i = 0; i < counterCashLists.size(); i++) {
//            Date startTime = counterCashLists.get(i).;
                System.out.println("********** staff id is ********** " + staffId);
                Long idOfStaff = counterCashLists.get(i).getStaffId();

                //if the entry is completed and it is the last entry, make a new entry 
                if (!counterCashLists.get(i).getStatus().equals("initial") && (counterCashLists.size() == i + 1)) {
                    System.out.println("********** make a new entry ********** ");
                    counterCashRecord = new CounterCashRecord(amount, time, staffId);
                    em.persist(counterCashRecord);
                    break;
                } else if (!counterCashLists.get(i).getStatus().equals("initial") && (counterCashLists.size() != i + 1)) {
                    //the entry is completetd, and the next is not null, exit and proceed to next checking

                } else { //the entry is not completed, check the satff id
                    //check satff id matched, if matched, make change, else make a new entry
                    if (counterCashLists.get(i).getStaffId().equals(staffId)) {
                        System.out.println("********** you have previous entry **********");
                        counterCashLists.get(i).setEndAmt(amount); //set the end amount
                        counterCashLists.get(i).setEndTime(time); //set the end time
                        BigDecimal initialAmount = counterCashLists.get(i).getStartAmt();
                        //check wether the amount matched
                        Boolean checkAmount = checkCounterCashAmount(initialAmount, amount);
                        if (checkAmount == true) {
                            System.out.println("********** the amout entered match with the previous amount **********");
                            counterCashLists.get(i).setStatus("cleared");
                            break;
                        } else {
                            System.out.println("********** 66666%%%%%% the amount doesnt matched **********");
                            counterCashLists.get(i).setStatus("pending for investigation");
                            break;
                        }
                    } else { // a new entry 
                        System.out.println("********** ##### the list is not empty, but the entry is new ********** ");
                        counterCashRecord = new CounterCashRecord(amount, time, staffId);
                        em.persist(counterCashRecord);
                    }
                }
            }
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private Boolean checkCounterCashAmount(BigDecimal initialAmount, BigDecimal endAmount) {
        //how to check for all the cash transaction at the counter??
        //from cashServiceRecord table
        BigDecimal totalAmount = new BigDecimal("0");
//        BigDecimal amtDifference = initialAmount.subtract(endAmount); //abs value of amt difference

//        System.out.println("********** the amout difference of initial entry and final entry is " + amtDifference);
        Calendar today = GregorianCalendar.getInstance();
        Date date = today.getTime();
        String currentDay = new SimpleDateFormat("dd-MM-yyyy").format(date);
        System.out.println("********** current day is " + currentDay);

        Query q = em.createQuery("SELECT a FROM CashServiceRecord a");
        List<CashServiceRecord> cashServiceRecords = new ArrayList(q.getResultList());

        if (cashServiceRecords.size() == 0) {
            return true;
        } else {
            System.out.println("inside the cash records checking ********** size is " + cashServiceRecords.size());
            for (int i = 0; i < cashServiceRecords.size(); i++) {
                String recordTime = new SimpleDateFormat("dd-MM-yyyy").format(cashServiceRecords.get(i).getTime());
                System.out.println("**********what is the current day is ********** " + recordTime);
                if (recordTime.equals(currentDay)) {
                    if (cashServiceRecords.get(i).getIsCredit() == true) {
                        totalAmount = totalAmount.add(cashServiceRecords.get(i).getCashTransaction());
                        System.out.println("********** the total amount is" + totalAmount);
                    } else {
                        totalAmount = totalAmount.subtract(cashServiceRecords.get(i).getCashTransaction());
                        System.out.println("********** the total amount is" + totalAmount);
                    }
                }
            }
        }
        if (initialAmount.add(totalAmount).equals(endAmount)) {
            System.out.println("********** the amount difference is 0 **********");
            return true;
        } else {
            System.out.println("********** the amount difference is NOT 0 **********");
            return false;
        }
    }

    @Override
    public void recordAmount(BigDecimal amount, Date time, String staffName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
