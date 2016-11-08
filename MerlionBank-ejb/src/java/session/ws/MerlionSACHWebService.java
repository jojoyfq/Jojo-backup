/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.ws;

import BillEntity.Session.BillSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author ruijia
 */
@WebService(serviceName = "MerlionSACHWebService")
@Stateless()
public class MerlionSACHWebService {
    @EJB
    BillSessionBeanLocal bsbl;
    
    /**
     * Web service operation
     * @param transactions
     * @return 
     */
    @WebMethod(operationName = "checkTransactions")
    public List<String> checkTransactions(@WebParam(name = "transactions") List<String> transactions) {
        //TODO write your implementation code here:
        return bsbl.checkReceivedTransactions(transactions);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "receiveTransactionsFromSACH")
    public boolean receiveTransactionsFromSACH(@WebParam(name = "transactions") List<String> transactions) {
        //TODO write your implementation code here:
        bsbl.processReceivedTransactions(transactions);
        return true;
    }
    
    
 
}
