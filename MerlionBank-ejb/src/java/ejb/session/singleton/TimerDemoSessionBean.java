/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import BillEntity.Session.BillSessionBeanLocal;
import BillEntity.Session.BillTimerSessionBeanLocal;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.NotEnoughAmountException;
import LoanEntity.Session.LoanTimerSessionBeanLocal;
import static com.sun.faces.facelets.util.Path.context;
import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import org.joda.time.DateTime;
import static org.joda.time.format.ISODateTimeFormat.date;

/**
 *
 * @author shuyunhuang
 */
@Stateless
public class TimerDemoSessionBean implements TimerDemoSessionBeanLocal {

    @Resource
    private SessionContext context;

    @Resource
    TimerService timerService;

    @EJB
    FixedDepositAccountSessionBeanLocal fdasbl;

    @EJB
    AccountManagementSessionBeanLocal amsbl;

    @EJB
    SavingAccountSessionBeanLocal sasbl;

    @EJB
    LoanTimerSessionBeanLocal ltsbl;
    
    @EJB
    BillTimerSessionBeanLocal btsbl;

    @Override
    public void createTimers(Date startTime, String timerInfo) {
        timerService = context.getTimerService();

        if ((timerInfo.equals("FixedDeposit-TIMER"))) {
            Timer fixedDepositAccountTimer = timerService.createTimer(startTime, 1 * 60 * 1000, "FixedDeposit-TIMER");
            System.err.println("********** FixedDeposit-TIMER TIMER CREATED");

        } else if ((timerInfo.equals("OnlineBankingAccount-TIMER"))) {
            Timer accountClosureTimer = timerService.createTimer(startTime, 1 * 60 * 1000, "OnlineBankingAccount-TIMER");
            System.err.println("********** OnlineBankingAccount-TIMER TIMER CREATED");
        } //for interest crediting 
        else if (timerInfo.equals("DailyInterestAccrued-TIMER")) {
            Timer dailyInterestAccrued = timerService.createTimer(startTime, 1 * 60 * 1000, "DailyInterestAccrued-TIMER");
            System.err.println("********** DailyInterestAccrued-TIMER TIMER CREATED");
        } else if (timerInfo.equals("MonthlyInterest-TIMER")) {
            Timer monthlyInterest = timerService.createTimer(startTime, 1 * 60 * 1000, "MonthlyInterest-TIMER");
            System.err.println("********** MonthlyInterest-TIMER TIMER CREATED");
        }  else if (timerInfo.equals("checkCustomerAge-TIMER")) {
            Timer loanStatusTimer = timerService.createTimer(startTime, 1 * 60 * 1000, "checkCustomerAge-TIMER");
            System.err.println("********** checkCustomerAge-TIMER TIMER CREATED");
        }else if (timerInfo.equals("LoanAccountStatus-TIMER")) {
            Timer loanAccountTimer = timerService.createTimer(startTime, 1 * 60 * 1000, "LoanAccountStatus-TIMER");
            System.err.println("********** LoanAccountStatus-TIMER");
        }else if (timerInfo.equals("LoanAutoBadDebt-TIMER")){
            Timer loanAutoBadDebt = timerService.createTimer(startTime, 1 * 60 * 1000, "LoanAutoBadDebt-TIMER");
            System.err.println("********** LoanAutoBadDebt-TIMER");
        }else if(timerInfo.equals("LoanLatePayment-TIMER")){
            Timer loanLatePayment = timerService.createTimer(startTime, 1* 60 * 1000, "LoanLatePayment-TIMER");
            System.err.println("********** LoanLatePayment-TIMER");
        }else if(timerInfo.equals("LoanUpdateMonthlyPayment-TIMER")){
            Timer loanUpdateMonthlyPayment = timerService.createTimer(startTime, 1 * 60 * 1000, "LoanUpdateMonthlyPayment-TIMER");
            System.out.println("********** LoanUpdateMonthlyPayment-TIMER");
        }else if(timerInfo.equals("LoanPayByGIRO-TIMER")){
            Timer loanPayByGIRO = timerService.createTimer(startTime, 1 * 60 * 1000, "LoanPayByGIRO-TIMER");
        }else if(timerInfo.equals("RecurrentBill-TIMER")){
            Timer recurrentBill = timerService.createTimer(startTime, 1 * 60 * 1000, "RecurrentBill-TIMER");
        }
    }

    public void cancelTimers(String timerInfo) {
        if (timerService.getTimers() != null) {
            for (Timer timer : timerService.getTimers()) {
                if (timer.getInfo().equals(timerInfo)) {
                    timer.cancel();
                    System.out.println(timerInfo + "stopped succesfully **********");
                }
            }
        }
    }

    @Timeout
    public void timeout(Timer timer) throws EmailNotSendException, NotEnoughAmountException {
        System.err.println("********** get in timeout here!!!!");
        Date currentDate = new Date();
        if (timer.getInfo().toString().equals("FixedDeposit-TIMER")) {
            System.err.println("********** FixedDeposit-TIMER go to fixed deposit session bean here!!!!");
            fdasbl.checkFixedDepositAccountStatus();
        } else if (timer.getInfo().toString().equals("OnlineBankingAccount-TIMER")) {
            System.err.println("********** OnlineBankingAccount-TIMER go to Common Entity Session bean now!!!!");
//            amsbl.checkOnlineBankingAccountStatus()
        } else if (timer.getInfo().toString().equals("DailyInterestAccrued-TIMER")) {
            System.err.println("********** DailyInterestAccrued-TIMER go to saving account session bean here!!!!");
            sasbl.dailyInterestAccrued();
        } else if (timer.getInfo().toString().equals("MonthlyInterest-TIMER")) {
            sasbl.monthlyInterestCrediting();
        } else if (timer.getInfo().toString().equals("checkCustomerAge-TIMER")) {
            System.err.println("********** checkCustomerAge-TIMER go to saving account session bean here!!!!");
            sasbl.checkCustomerAge();
        } else if (timer.getInfo().toString().equals("LoanAccountStatus-TIMER")) {
            ltsbl.closeAccounts();
        } else if (timer.getInfo().toString().equals("LoanAutoBadDebt-TIMER")) {
            ltsbl.autoBadDebt();
        } else if (timer.getInfo().toString().equals("LoanLatePayment-TIMER")) {
            ltsbl.calculateLatePayment();
        } else if (timer.getInfo().toString().equals("LoanUpdateMonthlyPayment-TIMER")) {
            ltsbl.updateMonthlyPayment();
        } else if (timer.getInfo().toString().equals("LoanPayByGIRO-TIMER")){
            ltsbl.loanPayByGIRO();
        } else if (timer.getInfo().toString().equals("RecurrentBill-TIMER"))
            btsbl.recurrentBillDeduction();
    }

    public void createTimers(Date startTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
