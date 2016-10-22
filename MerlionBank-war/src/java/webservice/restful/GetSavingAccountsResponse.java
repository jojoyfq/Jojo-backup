/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author liyanmeng
 */

@XmlRootElement
@XmlType(propOrder = {
    "savingAccounts"
})
public class GetSavingAccountsResponse extends Response implements Serializable{
 
    public List<String> savingAccounts;
        
    public GetSavingAccountsResponse() {
        super();
    }
    
    public GetSavingAccountsResponse(int status, String errorMessage, List<String> savingAccounts) {
        super(status, errorMessage);
        this.savingAccounts = savingAccounts;
    }
    
    public List<String> getSavingAccounts() {
        return savingAccounts;
    }

    public void setSavingAccounts(List<String> savingAccounts) {
        this.savingAccounts = savingAccounts;
    }
    
    
    
}
