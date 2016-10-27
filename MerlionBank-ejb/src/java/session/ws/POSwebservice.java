/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.ws;

import CardEntity.Session.DebitCardSessionBeanLocal;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author Bella
 */
@WebService(serviceName = "POSwebservice")
@Stateless()
public class POSwebservice {

    @EJB
    DebitCardSessionBeanLocal dcsbl;
    /**
     * Web service operation
     */
    @WebMethod(operationName = "authorizeDebitCard")
    public String authorizeDebitCard(@WebParam(name = "cardNumber") String cardNumber, @WebParam(name = "cardHolder") String cardHolder, @WebParam(name = "cvv") String cvv, @WebParam(name = "merchant") String merchant, @WebParam(name = "amount") String amount) {
        //TODO write your implementation code here:
        boolean checkBalance = dcsbl.checkDebitCardBalance(cardNumber, cvv, cardHolder, amount,merchant);
        String message;
        if(checkBalance){
            message = "Debit Card "+cardNumber+" is authorized!";
            return message;
        }else{
            message = "Debit Card"+cardNumber+"is not valid!";
           return message;
        }
    }
}
