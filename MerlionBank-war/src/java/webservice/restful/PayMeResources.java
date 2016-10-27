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
    static String merlionBankIC;
    static String phoneNumStr;
    static String phoneNumLogInStr;
    static boolean isLoginPage;

    public PayMeResources() {
    }

    @POST
    @Path(value = "isExistingCustomer")
    @Produces(MediaType.APPLICATION_JSON)
    public IsExistingCustomerResponse isExistingCustomer(@FormParam("customerIC") String customerIC,
            @FormParam("password") String password) {

        boolean checkExisting = false;
        isLoginPage = false;
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
    @Path(value = "isValidPassword")
    @Produces(MediaType.APPLICATION_JSON)
    public IsValidPasswordResponse isValidPassword(@FormParam("customerpaymephonenumber") String phoneNum,
            @FormParam("customerpaymepassword") String password) {

        boolean checkValidity;

//        String phoneNumber = payMeSessionBeanLocal.getPhoneNumber("ruijia");
//        String phoneNumber = payMeSessionBeanLocal.getPhoneNumber(merlionBankIC);
        System.out.println("Phone Number is " + phoneNum);
        System.out.println("Password is " + password);
        phoneNumLogInStr = phoneNum;
        System.out.println("phoneNumLogInStr is " + phoneNumLogInStr);
        isLoginPage = true;
        checkValidity = payMeSessionBeanLocal.checkPayMeLogin(phoneNum, password);
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
//            payMeSessionBeanLocal.sendTwoFactorAuthentication(merlionBankIC);
            System.out.println("merlion bank ic is " + merlionBankIC);
            payMeSessionBeanLocal.sendTwoFactorAuthentication(merlionBankIC);
        } catch (TwilioRestException ex) {
            return new SendTFAResponse(1, "Send OTP failed", false);
        }
        return new SendTFAResponse(0, "Send OTP Successfully", true);
    }

    @POST
    @Path(value = "isValidOTP")
    @Produces(MediaType.APPLICATION_JSON)
    public IsValidOTPResponse isValidOTP(@FormParam("OneTimePassword") String OTPString) {

        boolean checkOTPValidity;
        isLoginPage = false;
        System.out.println("IC is " + merlionBankIC);
        System.out.println("OTP is " + OTPString);
        checkOTPValidity = payMeSessionBeanLocal.verifyTwoFactorAuthentication(merlionBankIC, OTPString);

//        checkOTPValidity = payMeSessionBeanLocal.verifyTwoFactorAuthentication("ruijia", OTPString);
        if (checkOTPValidity == true) {
            System.out.println("Check OTP validity successfully");
            return new IsValidOTPResponse(0, "", true);
        } else {
            return new IsValidOTPResponse(1, "Invalid OTP", false);
        }

    }

    @POST
    @Path(value = "getPhoneNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public GetPhoneNumberResponse getPhoneNumberString() {

//        phoneNumStr = payMeSessionBeanLocal.getPhoneNumber("ruijia");
        System.out.println("is log in page " + isLoginPage);       
        if (isLoginPage == false) {
            phoneNumStr = payMeSessionBeanLocal.getPhoneNumber(merlionBankIC);
            System.out.println("phoneNumStr is " + phoneNumStr);
            if (phoneNumStr.isEmpty()) {
                return new GetPhoneNumberResponse(1, "No Phone Number Available", "");
            } else {
                return new GetPhoneNumberResponse(0, "", phoneNumStr);
            }
        } else {
            System.out.println("phoneNumLogInStr is " + phoneNumLogInStr);
            if (phoneNumLogInStr.isEmpty()) {
                return new GetPhoneNumberResponse(1, "No Phone Number Available", "");
            } else {
                System.out.println("come here");
                return new GetPhoneNumberResponse(0, "", phoneNumLogInStr);
            }
        }

    }

    @POST
    @Path(value = "getSavingAccountList")
    @Produces(MediaType.APPLICATION_JSON)
    public GetSavingAccountsResponse getSavingAccountStringList() {

        List<String> savingAccountsList;
        try {
            savingAccountsList = payMeSessionBeanLocal.getSavingAccountString(merlionBankIC);
//            savingAccountsList = payMeSessionBeanLocal.getSavingAccountString("ruijia");
        } catch (UserHasNoSavingAccountException ex) {
            return new GetSavingAccountsResponse(1, "No Saving Account Found", Arrays.asList(""));
        }
        return new GetSavingAccountsResponse(0, "", savingAccountsList);
    }

    @POST
    @Path(value = "getSavingAccountByPhoneNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public GetSavingAccountByPhoneResponse getSavingAccountNoByPhoneNumber() {
        String savingAccountNo;
        if (isLoginPage == false) {
            savingAccountNo = payMeSessionBeanLocal.getSavingAccountStringByPhone(phoneNumStr);
            if (savingAccountNo.isEmpty()) {
                return new GetSavingAccountByPhoneResponse(1, "No Saving Account Found", "");
            } else {
                return new GetSavingAccountByPhoneResponse(0, "", savingAccountNo);
            }
        } else {
            savingAccountNo = payMeSessionBeanLocal.getSavingAccountStringByPhone(phoneNumLogInStr);
            if (savingAccountNo.isEmpty()) {
                return new GetSavingAccountByPhoneResponse(1, "No Saving Account Found", "");
            } else {
                return new GetSavingAccountByPhoneResponse(0, "", savingAccountNo);
            }
        }
    }

    @POST
    @Path(value = "getBalance")
    @Produces(MediaType.APPLICATION_JSON)
    public GetBalanceResponse getBalanceString() {
        if (isLoginPage == false) {
            String balance = payMeSessionBeanLocal.getBalance(phoneNumStr);
            if (balance.isEmpty()) {
                return new GetBalanceResponse(1, "No Balance Available", "");
            } else {
                return new GetBalanceResponse(0, "", balance);
            }
        } else {
            String balanceLogIn = payMeSessionBeanLocal.getBalance(phoneNumLogInStr);
            if (phoneNumLogInStr.isEmpty()) {
                return new GetBalanceResponse(1, "No Balance Available", "");
            } else {
                return new GetBalanceResponse(0, "", balanceLogIn);
            }
        }
    }

    @POST
    @Path(value = "createPayMeAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public CreatePayMeAccountResponse createPayMeAccount(@FormParam("savingAccountStr") String savingAccountStr,
            @FormParam("payMePassword") String payMePassword) {

        System.out.println("Saving Account String is " + savingAccountStr);
        System.out.println("PayMe password is " + payMePassword);
        isLoginPage = false;
        boolean success;
        String savingAccountNo = savingAccountStr.split("-")[0].trim();
        success = payMeSessionBeanLocal.createPayMe(merlionBankIC, savingAccountNo, phoneNumStr, payMePassword);
//        success = payMeSessionBeanLocal.createPayMe("ruijia", savingAccountNo, "+6584527086", payMePassword);

        if (success == true) {
            return new CreatePayMeAccountResponse(0, "", true);
        } else {
            return new CreatePayMeAccountResponse(1, "PayMe account exists", false);
        }

    }

}
