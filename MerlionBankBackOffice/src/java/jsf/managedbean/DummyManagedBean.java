/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.PhaseEvent;
import javax.xml.ws.WebServiceRef;
import webservice.soap.client.TestWebService_Service;

/**
 *
 * @author Bella
 */
@Named(value = "dummyManagedBean")
@RequestScoped
public class DummyManagedBean {
    @WebServiceRef(wsdlLocation = "http://127.0.0.1:8080/TestWebService/TestWebService?wsdl")
    private TestWebService_Service service;

    
    public DummyManagedBean() {
    }
    
    
    @PostConstruct
    public void init()
    {
        System.err.println("*********************** DummyMnagedBean: " + hello("World!"));
    }
    
    
    public void beforePhaseListener(PhaseEvent event)
    {
    }

    private String hello(java.lang.String name) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        webservice.soap.client.TestWebService port = service.getTestWebServicePort();
        return port.hello(name);
    }
    
}
