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
    "phoneNumberLogIn"
})


public class GetPhoneNumberLogInResponse extends Response implements Serializable{
    
    public String phoneNumberLogIn;

    public GetPhoneNumberLogInResponse() {
        super();
    }

    public GetPhoneNumberLogInResponse(int status, String errorMessage, String phoneNumberLogIn) {
        super(status, errorMessage);
        this.phoneNumberLogIn = phoneNumberLogIn;
    }
    
    public String getPhoneNumberLogIn() {
        return phoneNumberLogIn;
    }

    public void setPhoneNumberLogIn(String phoneNumberLogIn) {
        this.phoneNumberLogIn = phoneNumberLogIn;
    }
    
    
}
