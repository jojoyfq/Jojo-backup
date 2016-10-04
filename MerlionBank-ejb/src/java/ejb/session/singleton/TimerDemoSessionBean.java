/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import CommonEntity.Session.AccountManagementSessionBeanLocal;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
import DepositEntity.Session.SavingAccountSessionBeanLocal;
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

    @Override
    public void createTimers(Date startTime){
        timerService = context.getTimerService();
        System.out.println("hihihihihihihihihi"+startTime);
        Timer fixedDepositAccountTimer = timerService.createTimer(startTime, 1 * 60 * 1000, "FixedDeposit-TIMER");
        System.err.println("********** FixedDeposit-TIMER TIMER CREATED");
    
        Timer accountClosureTimer = timerService.createTimer(startTime, 1 * 60 * 1000, "OnlineBankingAccount-TIMER");
        System.err.println("********** OnlineBankingAccount-TIMER TIMER CREATED");
        
        //for interest crediting 
        Timer dailyInterestAccrued = timerService.createTimer(startTime, 1 * 60 * 1000, "DailyInterestAccrued-TIMER");
        System.err.println("********** DailyInterestAccrued-TIMER TIMER CREATED");
        
        Timer monthlyInterest = timerService.createTimer(5*60*1000,"MonthlyInterest-TIMER");
        System.err.println("********** MonthlyInterest-TIMER TIMER CREATED");
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
    public void timeout(Timer timer) {
        System.err.println("********** get in timeout here!!!!");
        if (timer.getInfo().toString().equals("FixedDeposit-TIMER")) {
            System.err.println("********** FixedDeposit-TIMER go to fixed deposit session bean here!!!!");
            fdasbl.checkFixedDepositAccountStatus();
        } else if (timer.getInfo().toString().equals("OnlineBankingAccount-TIMER")) {
            System.err.println("********** OnlineBankingAccount-TIMER go to Common Entity Session bean now!!!!");
//            amsbl.checkOnlineBankingAccountStatus()
        } else if (timer.getInfo().toString().equals("DailyInterestAccrued-TIMER")){
            System.err.println("********** DailyInterestAccrued-TIMER go to saving account session bean here!!!!");
            sasbl.dailyInterestAccrued();
        } else if (timer.getInfo().toString().equals("MonthlyInterest-TIMER")){
            sasbl.monthlyInterestCrediting();
        }
            
    }

}
