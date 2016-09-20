/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import CommonEntity.Session.AccountManagementSessionBeanLocal;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
import static com.sun.faces.facelets.util.Path.context;
import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
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

    @Override
    public void createTimers(Date startTime){
        timerService = context.getTimerService();
        System.out.println("hihihihihihihihihi"+startTime);
        Timer fixedDepositAccountTimer = timerService.createTimer(startTime, 8 * 60 * 60 * 1000, "FixedDeposit-TIMER");
        System.err.println("********** FixedDeposit-TIMER TIMER CREATED");
    
        Timer accountClosureTimer = timerService.createTimer(startTime, 8 * 60 * 60 * 1000, "OnlineBankingAccount-TIMER");
        System.err.println("********** OnlineBankingAccount-TIMER TIMER CREATED");
    }
    
    @Override
    public void cancelTimers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Timeout
    public void timeout(Timer timer) {
        System.err.println("********** get in timeout here!!!!");
        if (timer.getInfo().toString().equals("FixedDeposit-TIMER")) {
            System.err.println("********** FixedDeposit-TIMER go to session bean here!!!!");
            fdasbl.checkFixedDepositAccountStatus();
        } else if (timer.getInfo().toString().equals("OnlineBankingAccount-TIMER")) {
            System.err.println("********** OnlineBankingAccount-TIMER go to Common Entity Session bean now!!!!");
//            amsbl.checkOnlineBankingAccountStatus();
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
