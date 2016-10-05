/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import java.util.Date;
import javax.ejb.Local;
import org.joda.time.DateTime;

/**
 *
 * @author shuyunhuang
 */
@Local
public interface TimerDemoSessionBeanLocal {

    public void createTimers(Date startTime);

    public void cancelTimers(String schedulerId);
    
}
