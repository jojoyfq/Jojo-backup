

import ejb.session.singleton.TimerDemoSessionBeanLocal;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;


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
        if(startTime != null){
        System.out.println(":::::::::::::::::::pppppp");
        System.out.println(startTime);
        timerDemoSessionBeanLocal.createTimers(startTime);     
        }
        else{
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please choose time.");
        RequestContext.getCurrentInstance().showMessageInDialog(message);   
        }
    }
       
    public void cancelTimers(ActionEvent event)
    {
        timerDemoSessionBeanLocal.cancelTimers();        
    }
}