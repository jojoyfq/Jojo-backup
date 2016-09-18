/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;

@Entity
public class TransferRecord extends TransactionRecord {
    private String transfer_Type; //Transfer can includes IntraTransfer and InterTransfer
    private String giverBankName;
    private String recipientBankName;  

    public TransferRecord(){}
    
    public TransferRecord(String code, BigDecimal amount,String status, String description,Date transactionTime,Long giverAccountNum,Long recipientAccountNum, String transfer_Type,String giverBankName,String recipientBankName){
        super(code,amount,status,description,transactionTime,giverAccountNum,recipientAccountNum);
        this.transfer_Type = transfer_Type;
        this.giverBankName = giverBankName;
        this.recipientBankName = recipientBankName;
    }
    
    public void setTransfer_Type(String transfer_Type) {
        this.transfer_Type = transfer_Type;
    }
    

    public String getTransfer_Type() {
        return transfer_Type;
    }
    
    public void setGiverBankName(String giverBankName) {
        this.giverBankName = giverBankName;
    }

    public String getGiverBankName() {
        return giverBankName;
    }
    
    public void setReceipientBankName(String receipientBankName) {
        this.recipientBankName = receipientBankName;
    }

    public String getReceipientBankName() {
        return recipientBankName;
    }

}
