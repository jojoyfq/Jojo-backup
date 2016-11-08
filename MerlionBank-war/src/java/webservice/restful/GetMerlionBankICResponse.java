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
    "merlionBankIC"
})

public class GetMerlionBankICResponse extends Response implements Serializable {

    public String merlionBankIC;

    
    public GetMerlionBankICResponse() {
        super();
    }

    public GetMerlionBankICResponse(int status, String errorMessage, String merlionBankIC) {
        super(status, errorMessage);
        this.merlionBankIC = merlionBankIC;
    }

    public String getMerlionBankIC() {
        return merlionBankIC;
    }

    public void setMerlionBankIC(String merlionBankIC) {
        this.merlionBankIC = merlionBankIC;
    }

}
