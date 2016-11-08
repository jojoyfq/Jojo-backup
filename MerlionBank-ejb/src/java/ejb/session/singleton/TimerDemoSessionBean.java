/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import BillEntity.Session.BillSessionBeanLocal;
import BillEntity.Session.BillTimerSessionBeanLocal;
import BusinessIntelligenceEntity.Session.SpendingSegmentationTimerSessionBeanLocal;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.NotEnoughAmountException;
import LoanEntity.Session.LoanTimerSessionBeanLocal;
import WealthEntity.Session.WealthTimerSessionBeanLocal;
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

    @EJB
    WealthTimerSessionBeanLocal wtsbl;
    
    @EJB
    SpendingSegmentationTimerSessionBeanLocal sstsbl;

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
        } else if (timerInfo.equals("checkCustomerAge-TIMER")) {
            Timer loanStatusTimer = timerService.createTimer(startTime, 1 * 60 * 1000, "checkCustomerAge-TIMER");
            System.err.println("********** checkCustomerAge-TIMER TIMER CREATED");
        } else if (timerInfo.equals("LoanAccountStatus-TIMER")) {
            Timer loanAccountTimer = timerService.createTimer(startTime, 1 * 60 * 1000, "LoanAccountStatus-TIMER");
            System.err.println("********** LoanAccountStatus-TIMER");
        } else if (timerInfo.equals("LoanAutoBadDebt-TIMER")) {
            Timer loanAutoBadDebt = timerService.createTimer(startTime, 1 * 60 * 1000, "LoanAutoBadDebt-TIMER");
            System.err.println("********** LoanAutoBadDebt-TIMER");
        } else if (timerInfo.equals("LoanLatePayment-TIMER")) {
            Timer loanLatePayment = timerService.createTimer(startTime, 1 * 60 * 1000, "LoanLatePayment-TIMER");
            System.err.println("********** LoanLatePayment-TIMER");
        } else if (timerInfo.equals("LoanUpdateMonthlyPayment-TIMER")) {
            Timer loanUpdateMonthlyPayment = timerService.createTimer(startTime, 1 * 60 * 1000, "LoanUpdateMonthlyPayment-TIMER");
            System.out.println("********** LoanUpdateMonthlyPayment-TIMER");
        } else if (timerInfo.equals("LoanPayByGIRO-TIMER")) {
            Timer loanPayByGIRO = timerService.createTimer(startTime, 1 * 60 * 1000, "LoanPayByGIRO-TIMER");
            System.out.println("********** LoanPayByGIRO-TIMER");
        } else if (timerInfo.equals("RecurrentBill-TIMER")) {
            Timer recurrentBill = timerService.createTimer(startTime, 1 * 60 * 1000, "RecurrentBill-TIMER");
            System.out.println("********** RecurrentBill-TIMER");
        } else if (timerInfo.equals("CloseWealthAccount-TIMER")) {
            Timer closeWealthAccount = timerService.createTimer(startTime, 1 * 60 * 1000, "CloseWealthAccount-TIMER");
            System.out.println("********** CloseWealthAccount-TIMER");
        } else if (timerInfo.equals("WealthAccountInterest-TIMER")) {
            Timer wealthAccountInterest = timerService.createTimer(startTime, 1 * 60 * 1000, "WealthAccountInterest-TIMER");
            System.out.println("********** WealthAccountInterest-TIMER");
        } else if (timerInfo.equals("WealthCommissionFee-TIMER")) {
            Timer wealthCommission = timerService.createTimer(startTime, 1 * 60 * 1000, "WealthCommissionFee-TIMER");
            System.out.println("********** WealthCommissionFee-TIMER");
        } else if (timerInfo.equals("ClosePortfolio-TIMER")) {
            Timer closePortfolio = timerService.createTimer(startTime, 1 * 60 * 1000, "ClosePortfolio-TIMER");
            System.out.println("********** ClosePortfolio-TIMER");
        } else if (timerInfo.equals("UpdateDiscretionaryAccountInterestRate-TIMER")) {
            Timer updateDiscretionaryAccountInterestRate = timerService.createTimer(startTime, 1 * 60 * 1000, "UpdateDiscretionaryAccountInterestRate-TIMER");
            System.out.println("********** UpdateDiscretionaryAccountInterestRate-TIMER");
        } else if (timerInfo.equals("PreDefinedPlanInterestCrediting-TIMER")) {
            Timer preDefinedPlanInterestCrediting = timerService.createTimer(startTime, 1 * 60 * 1000, "PreDefinedPlanInterestCrediting-TIMER");
            System.out.println("********** PreDefinedPlanInterestCrediting-TIMER");
        } else if (timerInfo.equals("DebitCardSegmentation-TIMER")) {
            Timer preDefinedPlanInterestCrediting = timerService.createTimer(startTime, 1 * 60 * 1000, "DebitCardSegmentation-TIMER");
            System.out.println("********** DebitCardSegmentation-TIMER");
        } else if (timerInfo.equals("CreditCardSegmentation-TIMER")) {
            Timer preDefinedPlanInterestCrediting = timerService.createTimer(startTime, 1 * 60 * 1000, "CreditCardSegmentation-TIMER");
            System.out.println("********** CreditCardSegmentation-TIMER");
        } else if (timerInfo.equals("SendBirthdayCoupon-TIMER")) {
            System.out.println("********** SendBirthdayCoupon-TIMER");
            Timer preDefinedPlanInterestCrediting = timerService.createTimer(startTime, 1 * 60 * 1000, "SendBirthdayCoupon-TIMER");
            
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
        } else if (timer.getInfo().toString().equals("LoanPayByGIRO-TIMER")) {
            ltsbl.loanPayByGIRO();
        } else if (timer.getInfo().toString().equals("RecurrentBill-TIMER")) {
            btsbl.recurrentBillDeduction();
        } else if (timer.getInfo().toString().equals("CloseWealthAccount-TIMER")) {
            wtsbl.closeAccount();
        } else if (timer.getInfo().toString().equals("WealthAccountInterest-TIMER")) {
            wtsbl.interestCrediting();
        } else if (timer.getInfo().toString().equals("WealthCommissionFee-TIMER")) {
            wtsbl.commissionFeeCalculation();
        } else if (timer.getInfo().toString().equals("ClosePortfolio-TIMER")) {
            wtsbl.closePortfolio();
        } else if (timer.getInfo().toString().equals("UpdateDiscretionaryAccountInterestRate-TIMER")) {
            wtsbl.updateDiscretionaryAccountInterestRate();
        } else if (timer.getInfo().toString().equals("PreDefinedPlanInterestCrediting-TIMER")) {
            wtsbl.preDefinedPlanInterestCrediting();
        } else if (timer.getInfo().toString().equals("DebitCardSegmentation-TIMER")){
            System.err.println("********** DebitCardSegmentation-TIME go to session bean here!!!!");
            sstsbl.debitCardSegmentation();
        } else if (timer.getInfo().toString().equals("CreditCardSegmentation-TIMER")){
            System.err.println("********** CreditCardSegmentation-TIMER go to session bean here!!!!");
            sstsbl.creditCardSegmentation();
        } else if (timer.getInfo().toString().equals("SendBirthdayCoupon-TIMER")){
            System.err.println("********** SendBirthdayCoupon-TIMER go to session bean here!!!!");
            sstsbl.sendBirthdayCoupon();
        }
    }

    @Override
    public void createTimers(Date startTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
