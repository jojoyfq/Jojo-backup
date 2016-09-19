/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import CommonEntity.Session.AccountManagementSessionBeanLocal;
import DepositEntity.Session.FixedDepositAccountSessionBeanLocal;
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
    @PostConstruct
    public void init() {
        Date date = new Date(2016 - 1900, 8, 18, 16, 20, 0);
        timerService = context.getTimerService();
        //Timer fixedDepositAccountTimer = timerService.createTimer(date, 86400000, "FixedDeposit-TIMER");
        //
        Timer fixedDepositAccountTimer = timerService.createTimer(date, 2 * 60 * 1000, "FixedDeposit-TIMER");
        System.err.println("********** FixedDeposit-TIMER TIMER CREATED");
//        **********for dummy timer************ 
//        Date date= Calendar.getInstance().getTime(); //get current time
//        timerService = context.getTimerService();
//        Timer timer = timerService.createTimer(30*1000, 24*60*60*1000 , "FixedDeposit-TIMER");

        Timer accountClosureTimer = timerService.createTimer(date, 2 * 60 * 1000, "OnlineBankingAccount-TIMER");
        System.err.println("********** OnlineBankingAccount-TIMER TIMER CREATED");
    }

    @Timeout
    public void timeout(Timer timer) {
        System.err.println("********** get in timeout here!!!!");
        if (timer.getInfo().toString().equals("FixedDeposit-TIMER")) {
            System.err.println("********** go to session bean here!!!!");
            fdasbl.checkFixedDepositAccountStatus();
        } else if (timer.getInfo().toString().equals("OnlineBankingAccount-TIMER")) {
            System.err.println("********** go to Common Entity Session bean now!!!!");
            amsbl.checkOnlineBankingAccountStatus();
        }
    }

    public void timeout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
