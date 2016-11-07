/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import Exception.PasswordNotMatchException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import PayMeEntity.Session.PayMeSessionBeanLocal;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author liyanmeng
 */
@Path("DigibankResources")
public class DigibankResources {

    @EJB
    PayMeSessionBeanLocal payMeSessionBeanLocal;

    IsExistingCustomerResponse isExistingCustomerResponse;
    static String merlionBankIC;

    public DigibankResources() {
    }

    @POST
    @Path(value = "isExistingCustomer")
    @Produces(MediaType.APPLICATION_JSON)
    public IsExistingCustomerResponse isExistingCustomer(@FormParam("customerIC") String customerIC,
            @FormParam("password") String password) {

        boolean checkExisting = false;
        System.err.println("customer IC is " + customerIC);
        System.out.println("password is " + password);

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
    @Path(value = "/getMerlionBankIC")
    @Produces(MediaType.APPLICATION_JSON)
    public GetMerlionBankICResponse getMerlionBankICString() {
        if (merlionBankIC.isEmpty() == false) {
            return new GetMerlionBankICResponse(0, "", merlionBankIC);
        } else {
            return new GetMerlionBankICResponse(1, "No IC Available", "");
        }
    }
    
    
    @POST
    @Path(value = "getSavingAccountList")
    @Produces(MediaType.APPLICATION_JSON)
    public GetSavingAccountsResponse getSavingAccountStringList() {

        List<String> savingAccountsList;
        try {
            savingAccountsList = payMeSessionBeanLocal.getSavingAccountString(merlionBankIC);
        } catch (UserHasNoSavingAccountException ex) {
            return new GetSavingAccountsResponse(1, "No Saving Account Found", Arrays.asList(""));
        }
        return new GetSavingAccountsResponse(0, "", savingAccountsList);
    }
    
    

}
