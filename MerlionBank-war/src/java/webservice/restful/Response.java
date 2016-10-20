/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import com.twilio.sdk.auth.IpMessagingGrant;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author liyanmeng
 */

@XmlRootElement

@XmlSeeAlso({
    IpMessagingGrant.class
})

@XmlType(propOrder = {
    "status",
    "errorMessage"
})


public class Response implements Serializable {
    
    public int status = 0;
    public String errorMessage = null;

    public Response() {
    }
    
    

    
    public Response(int status, String errorMessage){
        
        this();
        
        this.status = status;
        this.errorMessage = errorMessage;
    }
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    
    
}
