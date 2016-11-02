/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity.Session;

import BillEntity.BillRecord;
import BillEntity.BillingOrganization;
import BillEntity.RecurrentBillArrangement;
import DepositEntity.SavingAccount;
import Exception.EmailNotSendException;
import Exception.NotEnoughAmountException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author shuyunhuang
 */
@Stateless
public class BillTimerSessionBean implements BillTimerSessionBeanLocal {
    @PersistenceContext
    private EntityManager em;
               
    @Override
    public void recurrentBillDeduction() throws NotEnoughAmountException {
        Query query = em.createQuery("SELECT a FROM RecurrentBillArrangement a");
        List<RecurrentBillArrangement> recurrentBillArrangements = new ArrayList(query.getResultList());

        //times remaining is greater than 0 
        for (int i = 0; i < recurrentBillArrangements.size(); i++) {
            if (recurrentBillArrangements.get(i).getTimesRemaining() > 0 && recurrentBillArrangements.get(i).getStatus().equals("active")) {
                System.out.println("**********the recurrent bill ID is " + recurrentBillArrangements.get(i).getId());

                Date startDate = recurrentBillArrangements.get(i).getStartDate();
                Date todayDate = Calendar.getInstance().getTime();

                DateTime dateToStart = new DateTime(startDate);
                DateTime dateToday = new DateTime(todayDate);
                DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
                String startTime = dtf.print(dateToStart);
                String today = dtf.print(dateToday);
                System.out.println("********** start day is " + startTime);
                System.out.println("********** today is " + today);

                Integer remainingTimes = recurrentBillArrangements.get(i).getTimesRemaining(); // get remaining times
                System.out.println("********** remaining number of time is " + remainingTimes);
                Integer totalTimes = recurrentBillArrangements.get(i).getBillTimes();
                System.out.println("********** total number of time is " + totalTimes);

                //if it is the first time for the recurrent, remainingTimes = totalTimes
                if (remainingTimes == totalTimes) {
                    if (startTime.equals(today)) {
                        System.out.println("********** inside first dedcution checking");
                        SavingAccount savingAccount = recurrentBillArrangements.get(i).getSavingAccount();
                        BigDecimal recurrentAmt = recurrentBillArrangements.get(i).getAmount();
//                        System.out.println("the t");

                        //the saving account has not enough balance
                        if (recurrentAmt.compareTo(savingAccount.getAvailableBalance()) == 1) {
                            throw new NotEnoughAmountException("There is not enough amount of money in this savingAccount");
                        } else {
                            //balance is enough
                            System.out.println("balance before deduction is " + savingAccount.getAvailableBalance());
                            
                            recurrentBillArrangements.get(i).setTimesRemaining(remainingTimes - 1);
                            em.flush();
                            savingAccount.setAvailableBalance(savingAccount.getAvailableBalance().subtract(recurrentAmt));
                            System.out.println("balance after deduction is " + savingAccount.getAvailableBalance());
                            
                            
                            BillingOrganization billingOrganization = recurrentBillArrangements.get(i).getBillingOrganization();
                            String billReference = recurrentBillArrangements.get(i).getBillReference();
                            String boName =  recurrentBillArrangements.get(i).getBillingOrganization().getName();
                            Long accountNumber = savingAccount.getAccountNumber();
                            Long boSavingAccount = recurrentBillArrangements.get(i).getBillingOrganization().getAccountNumber();
                            
                            //set the bo account number here
                            BillRecord bill = new BillRecord(billingOrganization, billReference, "BI", recurrentAmt, null, "settled", "Bill payment to " + boName, 
                                    todayDate, accountNumber, null, null, null, null);
                            em.persist(bill);
                            savingAccount.getTransactionRecord().add(bill);
                            em.flush();
                            
                            if (recurrentBillArrangements.get(i).getTimesRemaining() == 0) {
                                recurrentBillArrangements.get(i).setStatus("terminated");
                            }
                        }
                    }
                } else { //it is not the first time of deduction 
                    System.out.println("********** inside not first time recurrent checking");
                    Integer interval = recurrentBillArrangements.get(i).getBillInterval();
                    Integer timePassed = (totalTimes - remainingTimes) * interval;
                    DateTime nextDeductionDate = dateToStart.plusWeeks(timePassed); //calculate the next dedection date
                    System.out.println("******** Next deduction date is " + nextDeductionDate);
                    String nextDeduction = dtf.print(nextDeductionDate);
                    System.out.println("******** Next deduction date is " + nextDeduction);
                        
                    if (today.equals(nextDeduction)) {
                        System.out.println("********** inside remaining dedcution checking");
                        SavingAccount savingAccount = recurrentBillArrangements.get(i).getSavingAccount();
                        BigDecimal recurrentAmt = recurrentBillArrangements.get(i).getAmount();

                        //the saving account has not enough balance
                        if (recurrentAmt.compareTo(savingAccount.getAvailableBalance()) == 1) {
                            throw new NotEnoughAmountException("There is not enough amount of money in this savingAccount");
                        } else {
                            //balance is enough
                            recurrentBillArrangements.get(i).setTimesRemaining(remainingTimes - 1);
                            savingAccount.setAvailableBalance(savingAccount.getAvailableBalance().subtract(recurrentAmt));
                            
                                                        BillingOrganization billingOrganization = recurrentBillArrangements.get(i).getBillingOrganization();
                            String billReference = recurrentBillArrangements.get(i).getBillReference();
                            String boName =  recurrentBillArrangements.get(i).getBillingOrganization().getName();
                            Long accountNumber = savingAccount.getAccountNumber();
                            Long boSavingAccount = recurrentBillArrangements.get(i).getBillingOrganization().getAccountNumber();
                            
                            //set the bo account number here
                            BillRecord bill = new BillRecord(billingOrganization, billReference, "BI", recurrentAmt, null, "settled", "Bill payment to " + boName, 
                                    todayDate, accountNumber, null, null, null, null);
                            em.persist(bill);
                            savingAccount.getTransactionRecord().add(bill);
                            em.flush();
                            if (recurrentBillArrangements.get(i).getTimesRemaining() == 0) {
                                recurrentBillArrangements.get(i).setStatus("terminated");
                            }
                        }
                    }
                }
            }
        }
    }
}