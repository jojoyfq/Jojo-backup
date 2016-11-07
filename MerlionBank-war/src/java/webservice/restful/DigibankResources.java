/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import DepositEntity.Session.TransferSessionBeanLocal;
import Exception.PasswordNotMatchException;
import Exception.TransferException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import PayMeEntity.Session.PayMeSessionBeanLocal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    @EJB
    TransferSessionBeanLocal transferSessionBeanLocal;

    IsExistingCustomerResponse isExistingCustomerResponse;
    static String merlionBankIC;
    static String savingAccountString;

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

    @POST
    @Path(value = "saveSavingAccountString")
    @Produces(MediaType.APPLICATION_JSON)
    public SaveSavingAccountStringResponse saveSavingAccountString(@FormParam("savingAccountStr") String savingAccStr) {
        savingAccountString = savingAccStr;
        System.out.println("saving account is " + savingAccountString + "*********");
        if (savingAccountString.isEmpty() == false) {
            return new SaveSavingAccountStringResponse(0, "", true);
        } else {
            return new SaveSavingAccountStringResponse(1, "Does Not Get Saving Account", false);
        }
    }

    
    @POST
    @Path(value = "oneTimeTransfer")
    @Produces(MediaType.APPLICATION_JSON)
    public OneTimeTransferResponse oneTimeTransfer(@FormParam("recipientAccountStr") String recipientAccStr,
            @FormParam("amount") String amountStr){
        
        boolean isTransferSuccess;
        String giverBankAccountString = savingAccountString.split("-")[0].trim();
        try {
            isTransferSuccess = transferSessionBeanLocal.intraOneTimeTransferCheckMobile(merlionBankIC, giverBankAccountString, recipientAccStr, amountStr);
            System.out.println("success: " + isTransferSuccess);
            return new OneTimeTransferResponse(0, "", true);
        } catch (TransferException ex) {
            if(ex.getMessage().contains("Transfer Limit")){
                return new OneTimeTransferResponse(1, "You have exceeded your daily transfer limit", false);
            }else if(ex.getMessage().contains("enough fund")){
                return new OneTimeTransferResponse(1, "Your account does not have enough balance", false);
            }else if(ex.getMessage().contains("incorrect")){
                return new OneTimeTransferResponse(1, "invalid recipient account number", false);
            }else{
                return new OneTimeTransferResponse(1, "error", false);
            }
        }        
    }
    
    
    
}
