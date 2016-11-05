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
    "checkAmountValidity"
})

public class IsValidSendAmountResponse extends Response implements Serializable {

    public boolean checkAmountValidity;

    public IsValidSendAmountResponse() {
        super();
    }

    public IsValidSendAmountResponse(int status, String errorMessage, boolean checkAmountValidity) {
        super(status, errorMessage);
        this.checkAmountValidity = checkAmountValidity;
    }

    public boolean getCheckAmountValidity() {
        return checkAmountValidity;
    }

    public void setCheckAmountValidity(boolean checkAmountValidity) {
        this.checkAmountValidity = checkAmountValidity;
    }

}
