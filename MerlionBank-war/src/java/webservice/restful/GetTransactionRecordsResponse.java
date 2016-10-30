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
    "transactionRecords"
})

public class GetTransactionRecordsResponse extends Response implements Serializable {

    public List<List<String>> transactionRecords;

    public GetTransactionRecordsResponse() {
        super();
    }

    public GetTransactionRecordsResponse(int status, String errorMessage, List<List<String>> transactionRecords) {
        super(status, errorMessage);
        this.transactionRecords = transactionRecords;
    }

    public List<List<String>> getTransactionRecords() {
        return transactionRecords;
    }

    public void setTransactionRecords(List<List<String>> transactionRecords) {
        this.transactionRecords = transactionRecords;
    }

}
