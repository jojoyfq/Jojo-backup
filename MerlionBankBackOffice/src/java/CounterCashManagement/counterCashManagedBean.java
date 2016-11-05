/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CounterCashManagement;

import CommonEntity.Customer;
import CommonEntity.Staff;
import CounterCashEntity.Session.CounterCashManagementLocal;
import StaffManagement.staffLogInManagedBean;
import TellerManagedBean.ServiceCustomerManagedBean;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import static jdk.nashorn.internal.objects.NativeFunction.function;
import org.primefaces.context.RequestContext;

/**
 *
 * @author shuyunhuang
 */
@Named(value = "counterCashManagedBean")
@SessionScoped
public class counterCashManagedBean implements Serializable {

    @EJB
    CounterCashManagementLocal ccml;
    
    private Staff staff;
    private Long staffId;
    private Long customerId;
    private Customer customer;
    private String amount;
    private Date date;
    private String staffName;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    
    @Inject 
    private staffLogInManagedBean staffLogInManagedBean;
    
    @Inject
    private ServiceCustomerManagedBean serviceCustomerManagedBean;
    
    @PostConstruct
    public void init(){
            System.out.print("inside the init method");
        //serviceCustomerManagedBean.init();
            staff = staffLogInManagedBean.getStaff();
            staffId = staffLogInManagedBean.getStaffId();
            //customerId = serviceCustomerManagedBean.getCustomer().getId();
            //customer = serviceCustomerManagedBean.getCustomer();
           System.out.print("************************");           
    }
    /**
     * Creates a new instance of counterCashManagedBean
     */
    
    /**
     * Creates a new instance of counterCashManagedBean
     * @param event
     */
    public void dashboardEnterAmount(ActionEvent event) {
        System.out.println("********** inside the dashboard enter amount method **********");
        staffId = staffLogInManagedBean.getStaffId();

        System.out.print(staffId);
        System.out.print(amount);
        
        if(amount != null){
        Calendar today = GregorianCalendar.getInstance();
        date = today.getTime();
        BigDecimal amountEnter=new BigDecimal(amount);
        ccml.recordAmount(amountEnter, date, staffId);
        System.out.println("entered the amount");
        amount="0";
        }else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please enter amount");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        
        
    }
    
    public counterCashManagedBean() {
    }
    
}
