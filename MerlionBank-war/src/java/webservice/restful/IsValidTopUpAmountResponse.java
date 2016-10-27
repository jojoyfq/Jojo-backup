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
    "checkTopUpAmountValidity"
})

public class IsValidTopUpAmountResponse extends Response implements Serializable{
 
    public boolean checkTopUpAmountValidity;

    public IsValidTopUpAmountResponse() {
        super();
    }
    
    public IsValidTopUpAmountResponse(int status, String errorMessage, boolean checkTopUpAmountValidity) {
        super(status, errorMessage);
        this.checkTopUpAmountValidity = checkTopUpAmountValidity;
    } 
        
    public boolean getCheckTopUpAmountValidity() {
        return checkTopUpAmountValidity;
    }

    public void setCheckTopUpAmountValidity(boolean checkTopUpAmountValidity) {
        this.checkTopUpAmountValidity = checkTopUpAmountValidity;
    }
       
    
}
