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
import com.twilio.sdk.TwilioRestException;
import java.util.Arrays;
import java.util.List;
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
    SendTFAResponse sendTFAResponse;
    IsValidOTPResponse isValidOTPResponse;
    GetPhoneNumberResponse getPhoneNumberResponse;
    String merlionBankIC;
    String phoneNumStr;

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
    public IsValidPasswordResponse isValidPassword(@FormParam("customerpaymepassword") String password) {

        boolean checkValidity;

        String phoneNumber = payMeSessionBeanLocal.getPhoneNumber("ruijia");
//        String phoneNumber = payMeSessionBeanLocal.getPhoneNumber(merlionBankIC);

        checkValidity = payMeSessionBeanLocal.checkPayMeLogin(phoneNumber, password);
        if (checkValidity == true) {
            isValidPasswordResponse = new IsValidPasswordResponse(0, "", checkValidity);
        } else {
            isValidPasswordResponse = new IsValidPasswordResponse(1, "Invalid password", false);
        }

        return isValidPasswordResponse;
    }

    @POST
    @Path(value = "getOneTimePassword")
    @Produces(MediaType.APPLICATION_JSON)
    public SendTFAResponse getOneTimePassword() {
        try {
            //        payMeSessionBeanLocal.sendTwoFactorAuthentication(merlionBankIC);
            payMeSessionBeanLocal.sendTwoFactorAuthentication("ruijia");
        } catch (TwilioRestException ex) {
            return new SendTFAResponse(1, "Send OTP failed", false);
        }
        return new SendTFAResponse(0, "", true);
    }

    @POST
    @Path(value = "isValidOTP")
    @Produces(MediaType.APPLICATION_JSON)
    public IsValidOTPResponse isValidOTP(@FormParam("OneTimePassword") String OTPString) {
//        payMeSessionBeanLocal.verifyTwoFactorAuthentication(merlionBankIC, OTPString);
        boolean checkOTPValidity;

        checkOTPValidity = payMeSessionBeanLocal.verifyTwoFactorAuthentication("ruijia", OTPString);
        if (checkOTPValidity == true) {
            return new IsValidOTPResponse(0, "", true);
        } else {
            return new IsValidOTPResponse(1, "Invalid OTP", false);
        }

    }

    @POST
    @Path(value = "getPhoneNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public GetPhoneNumberResponse getPhoneNumberString() {

//        String phoneNumStr = payMeSessionBeanLocal.getPhoneNumber(merlionBankIC);
        phoneNumStr = payMeSessionBeanLocal.getPhoneNumber("ruijia");
        if (phoneNumStr.isEmpty()) {
            return new GetPhoneNumberResponse(1, "Empty Phone Number String", "");
        } else {
            return new GetPhoneNumberResponse(0, "", phoneNumStr);
        }

    }

    @POST
    @Path(value = "getSavingAccountList")
    @Produces(MediaType.APPLICATION_JSON)
    public GetSavingAccountsResponse getSavingAccountStringList() {

        List<String> savingAccountsList;
        try {
//        List<String> savingAccountsList = payMeSessionBeanLocal.getSavingAccountString(merlionBankIC);
            savingAccountsList = payMeSessionBeanLocal.getSavingAccountString("ruijia");
        } catch (UserHasNoSavingAccountException ex) {
            return new GetSavingAccountsResponse(1, "No Saving Account Found", Arrays.asList(""));
        }
        return new GetSavingAccountsResponse(0, "", savingAccountsList);
    }

    @POST
    @Path(value = "createPayMeAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public CreatePayMeAccountResponse createPayMeAccount(@FormParam("savingAccountStr") String savingAccountStr,
            @FormParam("payMePassword") String payMePassword) {

        boolean success;
        String savingAccountNo = savingAccountStr.split("-")[0].trim();
//        success = payMeSessionBeanLocal.createPayMe(merlionBankIC, savingAccountNo, phoneNumStr, payMePassword);
        success = payMeSessionBeanLocal.createPayMe("ruijia", savingAccountNo, "+6584527086", payMePassword);

        if (success == true) {
            return new CreatePayMeAccountResponse(0, "", true);
        }else{
            return new CreatePayMeAccountResponse(1, "PayMe account exists", false);
        }

    }

}
