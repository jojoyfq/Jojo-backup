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
    "checkPasswordValidity"
})

public class IsValidPasswordResponse extends Response implements Serializable{
    
    public boolean checkPasswordValidity;

       
    public IsValidPasswordResponse() {
        super();
    }
    
    public IsValidPasswordResponse(int status, String errorMessage, boolean checkPasswordValidity) {
        super(status, errorMessage);
        this.checkPasswordValidity = checkPasswordValidity;
    }  
    
    
    public boolean getCheckPasswordValidity() {
        return checkPasswordValidity;
    }

    public void setCheckPasswordValidity(boolean checkPasswordValidity) {
        this.checkPasswordValidity = checkPasswordValidity;
    }
    
    
}
