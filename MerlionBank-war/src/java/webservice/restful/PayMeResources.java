/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import Exception.PasswordNotMatchException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import PayMeEntity.Session.PayMeSessionBeanLocal;
import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author liyanmeng
 */
@Path("PayMeResources")
public class PayMeResources {

    @EJB
    PayMeSessionBeanLocal payMeSessionBeanLocal;

    IsExistingCustomerResponse isExistingCustomerResponse;
    IsValidPasswordResponse isValidPasswordResponse;
    String merlionBankIC;

    public PayMeResources() {
    }

    @POST
    @Path(value = "isExistingCustomer")
    @Produces(MediaType.APPLICATION_JSON)
    public IsExistingCustomerResponse isExistingCustomer(@FormParam("customerIC") String customerIC,
            @FormParam("password") String password) {

        boolean checkExisting = false;

        System.err.println("isExistingCustomer: " + customerIC);

        try {

            checkExisting = payMeSessionBeanLocal.checkLogin(customerIC, password);
            isExistingCustomerResponse = new IsExistingCustomerResponse(0, "", checkExisting);
            merlionBankIC = customerIC;

        } catch (UserNotExistException ex) {
            isExistingCustomerResponse = new IsExistingCustomerResponse(1, "Invalid IC", false);

        } catch (PasswordNotMatchException ex) {
            isExistingCustomerResponse = new IsExistingCustomerResponse(1, "Invalid IC or Password", false);

        } catch (UserNotActivatedException ex) {
            isExistingCustomerResponse = new IsExistingCustomerResponse(1, "Please activate your account", false);

        }

        return isExistingCustomerResponse;

    }

    @POST
    @Path(value = "isValidPassword")
    @Produces(MediaType.APPLICATION_JSON)
    public IsValidPasswordResponse isValidPassword(@FormParam("password") String password) {

        boolean checkValidity = false;
        
        String phoneNumber = payMeSessionBeanLocal.getPhoneNumber(merlionBankIC);       
        checkValidity = payMeSessionBeanLocal.checkPayMeLogin(phoneNumber, password);        
        if(checkValidity == true){
            isValidPasswordResponse = new IsValidPasswordResponse(0, "", checkValidity);
        }else{
            isValidPasswordResponse = new IsValidPasswordResponse(1, "Invalid password", false);
        }

        return isValidPasswordResponse;
    }

}
