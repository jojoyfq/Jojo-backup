/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import java.util.Date;
import javax.ejb.Stateless;

/**
 *
 * @author shuyunhuang
 */
@Stateless
public class TimerDemoSessionBean implements TimerDemoSessionBeanLocal {

    @Override
    public void createTimers(Date startTime) {
        
        System.out.println("hihihihihihihihihi"+startTime);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelTimers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
