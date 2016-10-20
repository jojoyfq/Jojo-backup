/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author liyanmeng
 */
@XmlRootElement
@XmlType(propOrder = {
    "checkExistingCustomer"
})

public class IsExistingCustomerResponse extends Response implements Serializable{
    
    public boolean checkExistingCustomer;

    public IsExistingCustomerResponse() {
        super();
    }
    
    

    public IsExistingCustomerResponse(int status, String errorMessage, boolean checkExistingCustomer) {
        super(status, errorMessage);
        this.checkExistingCustomer = checkExistingCustomer;
    }    
    
    public boolean getCheckExistingCustomer() {
        return checkExistingCustomer;
    }

    public void setCheckExistingCustomer(boolean checkExistingCustomer) {
        this.checkExistingCustomer = checkExistingCustomer;
    }
    
    
}
