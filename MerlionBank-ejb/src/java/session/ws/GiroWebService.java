/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.ws;

import BillEntity.GIROArrangement;
import BillEntity.Session.BillSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import org.joda.time.DateTime;

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
     * @param deductDay
     * @param boName
     */
    
    @WebMethod(operationName = "approveGIRO")
    public void approveGIRO(@WebParam(name = "status")String status, @WebParam(name = "id") Long id, @WebParam(name="deductDay") DateTime deductDay){
        bsbl.approveGIRO(id, status, deductDay);
    }
    
    @WebMethod(operationName = "displayGIRO")
    public List<GIROArrangement> displayGIRO(@WebParam(name = "boName")String boName){
        List<GIROArrangement> giro = bsbl.getPendingGIRO(boName);
        return giro;
    }
    
    
}
