/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("TransferRecord")
public class TransferRecord extends TransactionRecord {
    private String transfer_Type; //Transfer can includes IntraTransfer and InterTransfer
    private String giverBankName;
    private String receipientBankName;  
    @ManyToOne
    private SavingAccount giverBankAccount; 
    @ManyToOne
    private SavingAccount receipientBankAccount; 
    private Long giverBankAccountNum;
    private Long receipientBankAccountNum;

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
        this.receipientBankName = receipientBankName;
    }

    public String getReceipientBankName() {
        return receipientBankName;
    }

     public void setGiverBankAccount(SavingAccount giverBankAccount) {
        this.giverBankAccount = giverBankAccount;
    }

    public SavingAccount getGiverBankAccount() {
        return giverBankAccount;
    }
    
    public SavingAccount getReceipientBankAccount() {
        return receipientBankAccount;
    }
    
    public void setReceipientBankAccount(SavingAccount receipientBankAccount) {
        this.receipientBankAccount = receipientBankAccount;
    }
    
    public void setGiverBankAccountNum(Long giverBankAccountNum) {
        this.giverBankAccountNum = giverBankAccountNum;
    }

    public void setReceipientBankAccountNum(Long receipientBankAccountNum) {
        this.receipientBankAccountNum = receipientBankAccountNum;
    }

    public Long getGiverBankAccountNum() {
        return giverBankAccountNum;
    }

    public Long getReceipientBankAccountNum() {
        return receipientBankAccountNum;
    }
    
}
