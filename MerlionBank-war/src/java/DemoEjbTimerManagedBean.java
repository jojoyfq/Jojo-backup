

import ejb.session.singleton.TimerDemoSessionBeanLocal;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.joda.time.DateTime;


@Named(value = "DemoEjbTimerManagedBean")
@ViewScoped



public class DemoEjbTimerManagedBean implements Serializable
{
    @EJB
    TimerDemoSessionBeanLocal timerDemoSessionBeanLocal;
    
    private Date startTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    public DemoEjbTimerManagedBean() {
    }
    
    //by clicking 'confirmed' 
    public void createTimers()
    {
        System.out.println(":::::::::::::::::::pppppp");
        System.out.println(startTime);
        timerDemoSessionBeanLocal.createTimers(startTime);        
    }  
       
    public void cancelTimers(ActionEvent event)
    {
        timerDemoSessionBeanLocal.cancelTimers();        
    }
}