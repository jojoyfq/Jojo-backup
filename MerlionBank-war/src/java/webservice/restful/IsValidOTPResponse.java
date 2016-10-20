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
    "checkOTPValidity"
})
public class IsValidOTPResponse extends Response implements Serializable{
    
    public boolean checkOTPValidity;

    public IsValidOTPResponse() {
        super();
    }
    
    public IsValidOTPResponse(int status, String errorMessage, boolean checkOTPValidity) {
        super(status, errorMessage);
        this.checkOTPValidity = checkOTPValidity;
    }  
        
    
    public boolean getCheckOTPValidity() {
        return checkOTPValidity;
    }

    public void setCheckOTPValidity(boolean checkOTPValidity) {
        this.checkOTPValidity = checkOTPValidity;
    }
        
}
