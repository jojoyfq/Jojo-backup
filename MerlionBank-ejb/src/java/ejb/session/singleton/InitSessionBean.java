/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import BillEntity.Session.BillTimerSessionBeanLocal;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
import Exception.EmailNotSendException;
import Exception.NotEnoughAmountException;
import LoanEntity.Session.LoanTimerSessionBeanLocal;
import WealthEntity.Session.WealthTimerSessionBeanLocal;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author shuyunhuang
 */
@Singleton
@LocalBean
@Startup
//@ApplicationScoped
public class InitSessionBean {

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

    @PostConstruct
    public void init() {
        Date date = new Date(2017 - 1900, 9, 17, 0, 0, 0);

        timerService = context.getTimerService();

        Timer fixedDepositAccountTimer = timerService.createTimer(date, 24 * 60 * 60 * 1000, "FixedDeposit-TIMER");
        System.err.println("********** FixedDeposit-TIMER TIMER CREATED");

        Timer accountClosureTimer = timerService.createTimer(date, 24 * 60 * 60 * 1000, "OnlineBankingAccount-TIMER");
        System.err.println("********** OnlineBankingAccount-TIMER TIMER CREATED");

        Timer savingAccountDailyInterestTimer = timerService.createTimer(date, 24 * 60 * 60 * 1000, "SavingAccountDailyInterestTimer-TIMER");
        System.err.println("********** SavingAccountDailyInterestTimer-TIMER");

//        Timer monthlyInterest = timerService.createTimer(date, 24 * 60 * 60 * 1000, "MonthlyInterest-TIMER");
//        System.err.println("********** MonthlyInterest-TIMER TIMER CREATED");
        Timer loanStatusTimer = timerService.createTimer(date, 24 * 60 * 60 * 1000, "checkCustomerAge-TIMER");
        System.err.println("********** checkCustomerAge-TIMER TIMER CREATED");

        Timer loanAccountTimer = timerService.createTimer(date, 24 * 60 * 60 * 1000, "LoanAccountStatus-TIMER");
        System.err.println("********** LoanAccountStatus-TIMER");

        Timer loanAutoBadDebt = timerService.createTimer(date, 24 * 60 * 60 * 1000, "LoanAutoBadDebt-TIMER");
        System.err.println("********** LoanAutoBadDebt-TIMER");

        Timer loanLatePayment = timerService.createTimer(date, 24 * 60 * 60 * 1000, "LoanLatePayment-TIMER");
        System.err.println("********** LoanLatePayment-TIMER");

//        Timer loanUpdateMonthlyPayment = timerService.createTimer(date, 24 * 60 * 60 * 1000, "LoanUpdateMonthlyPayment-TIMER");
//        System.out.println("********** LoanUpdateMonthlyPayment-TIMER");
        Timer loanPayByGIRO = timerService.createTimer(date, 24 * 60 * 60 * 1000, "LoanPayByGIRO-TIMER");
        System.out.println("********** LoanPayByGIRO-TIMER");

        Timer recurrentBill = timerService.createTimer(date, 24 * 60 * 60 * 1000, "RecurrentBill-TIMER");
        System.out.println("********** RecurrentBill-TIMER");

        Timer closeWealthAccount = timerService.createTimer(date, 24 * 60 * 60 * 1000, "CloseWealthAccount-TIMER");
        System.out.println("********** CloseWealthAccount-TIMER");

        Timer wealthAccountInterest = timerService.createTimer(date, 24 * 60 * 60 * 1000, "WealthAccountInterest-TIMER");
        System.out.println("********** WealthAccountInterest-TIMER");

        Timer wealthCommission = timerService.createTimer(date, 24 * 60 * 60 * 1000, "WealthCommissionFee-TIMER");
        System.out.println("********** WealthCommissionFee-TIMER");

        Timer closePortfolio = timerService.createTimer(date, 24 * 60 * 60 * 1000, "ClosePortfolio-TIMER");
        System.out.println("********** ClosePortfolio-TIMER");

        Timer updateDiscretionaryAccountInterestRate = timerService.createTimer(date, 24 * 60 * 60 * 1000, "UpdateDiscretionaryAccountInterestRate-TIMER");
        System.out.println("********** UpdateDiscretionaryAccountInterestRate-TIMER");

        Timer preDefinedPlanInterestCrediting = timerService.createTimer(date, 24 * 60 * 60 * 1000, "PreDefinedPlanInterestCrediting-TIMER");
        System.out.println("********** PreDefinedPlanInterestCrediting-TIMER");
    }

    @Timeout
    public void timeout(Timer timer) throws EmailNotSendException, NotEnoughAmountException {
        System.err.println("********** get in timeout here!!!!");
        if (timer.getInfo().toString().equals("FixedDeposit-TIMER")) {
            System.err.println("********** FixedDeposit-TIMER go to session bean here!!!!");
            fdasbl.checkFixedDepositAccountStatus();
        } else if (timer.getInfo().toString().equals("OnlineBankingAccount-TIMER")) {
            System.err.println("********** OnlineBankingAccount-TIMER go to Common Entity Session bean now!!!!");
//            amsbl.checkOnlineBankingAccountStatus();
        } else if (timer.getInfo().toString().equals("SavingAccountDailyInterestTimer-TIMER")) {
            System.err.println("********** SavingAccountDailyInterestTimer-TIMER go to Saving Account Entity Session bean now!!!!");
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
        } else if (timer.getInfo().toString().equals("RecurrentBill-TIMER")){
            btsbl.recurrentBillDeduction();
        } else if (timer.getInfo().toString().equals("CloseWealthAccount-TIMER")){
            wtsbl.closeAccount();
        } else if (timer.getInfo().toString().equals("WealthAccountInterest-TIMER")){
            wtsbl.interestCrediting();
        } else if (timer.getInfo().toString().equals("WealthCommissionFee-TIMER")){
            wtsbl.commissionFeeCalculation();
        } else if (timer.getInfo().toString().equals("ClosePortfolio-TIMER")){
            wtsbl.closePortfolio();
        } else if (timer.getInfo().toString().equals("UpdateDiscretionaryAccountInterestRate-TIMER")){
            wtsbl.updateDiscretionaryAccountInterestRate();
        } else if (timer.getInfo().toString().equals("PreDefinedPlanInterestCrediting-TIMER")){
            wtsbl.preDefinedPlanInterestCrediting();
        }
    }

    public void timeout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
