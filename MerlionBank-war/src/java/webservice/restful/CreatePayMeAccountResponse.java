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
    "success"
})


public class CreatePayMeAccountResponse extends Response implements Serializable{
    
    boolean success;

    public CreatePayMeAccountResponse() {
        super();
    }
    
    public CreatePayMeAccountResponse(int status, String errorMessage, boolean success) {
        super(status, errorMessage);
        this.success = success;
    } 
    
    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    
    
}
