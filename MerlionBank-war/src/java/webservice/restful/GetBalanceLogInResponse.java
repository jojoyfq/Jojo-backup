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
    "balanceLogIn"
})

public class GetBalanceLogInResponse extends Response implements Serializable {

    public String balanceLogIn;

    public GetBalanceLogInResponse() {
        super();
    }

    public GetBalanceLogInResponse(int status, String errorMessage, String balanceLogIn) {
        super(status, errorMessage);
        this.balanceLogIn = balanceLogIn;
    }

    public String getBalanceLogIn() {
        return balanceLogIn;
    }

    public void setBalanceLogIn(String balanceLogIn) {
        this.balanceLogIn = balanceLogIn;
    }

}
