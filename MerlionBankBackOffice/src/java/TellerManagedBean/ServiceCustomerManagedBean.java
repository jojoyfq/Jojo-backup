/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TellerManagedBean;

import CommonEntity.Customer;
import TellerServeCustomer.Session.ServiceCustomerSessionBeanLocal;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ruijia
 */
@Named(value = "serviceCustomerManagedBean")
@SessionScoped
public class ServiceCustomerManagedBean implements Serializable {

    @EJB
    ServiceCustomerSessionBeanLocal scsb;
    
    
    /**
     * Creates a new instance of serviceCustomerManagedBean
     */
    public ServiceCustomerManagedBean() {
    }
    
    private Customer customer;
    private String customerIc;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCustomerIc() {
        return customerIc;
    }

    public void setCustomerIc(String customerIc) {
        this.customerIc = customerIc;
    }
    
    public void selectCustomer(ActionEvent event) throws IOException{
        if(customerIc != null){
            if(!scsb.selectCustomer(customerIc).equals(false)){
        this.customer = (Customer) scsb.selectCustomer(customerIc);
        System.out.print(customerIc);
        System.out.print("customer set!");
       // redirect to homepage
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Customer selected");
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBankBackOffice/StaffDashboard.xhtml");

            }else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Customer not found. Please enter correct IC.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        }else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please enter customer IC");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    public void deselectCustomer(ActionEvent event) throws IOException{
        this.customer = null;
        System.out.print("customer cleared!");
        //redirect to homepage
    }

    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void goToServiceCustomerPage(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/TellerManagement/ServiceCustomer.xhtml");
        } catch (Exception e) {
            System.out.print("Redirect to ServiceCustomer page fails");
        }
    }


    
}
