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
    "savingAccountNo"
})

public class GetSavingAccountByPhoneResponse extends Response implements Serializable{
    
    public String savingAccountNo;

    public GetSavingAccountByPhoneResponse() {
        super();
    }
    
    public GetSavingAccountByPhoneResponse(int status, String errorMessage, String savingAccountNo) {
        super(status, errorMessage);
        this.savingAccountNo = savingAccountNo;
    }
    
    public String getSavingAccountNo() {
        return savingAccountNo;
    }

    public void setSavingAccountNo(String savingAccountNo) {
        this.savingAccountNo = savingAccountNo;
    }
    
    
    
}