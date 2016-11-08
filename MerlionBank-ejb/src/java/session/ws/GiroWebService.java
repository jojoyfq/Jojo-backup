/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.ws;

import BillEntity.Session.BillSessionBeanLocal;
import java.text.ParseException;
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
@WebService(serviceName = "GiroWebService")
@Stateless()
public class GiroWebService {
    
     @EJB 
     BillSessionBeanLocal bsbl;
     
    /**
     * This is a sample web service operation
     * @param status
     * @param id
     * @param deductionDay
     * @throws java.text.ParseException
     */
    
    @WebMethod(operationName = "approveGIRO")
    public boolean approveGIRO(@WebParam(name = "status")String status, @WebParam(name = "id") Long id, @WebParam(name="deductDay") String deductionDay) throws ParseException{
            return bsbl.approveGIRO(id, status, deductionDay);
    }
    
    @WebMethod(operationName = "displayGIRO")
    public List<String> displayGIRO(@WebParam(name = "boName")String boName){
        return bsbl.getPendingGIRO(boName);
    }
    
    
}
