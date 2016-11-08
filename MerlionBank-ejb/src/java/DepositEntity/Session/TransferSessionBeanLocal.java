/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import BillEntity.OtherBank;
import DepositEntity.TransactionRecord;
import Exception.PayeeNotFoundException;
import Exception.TransferException;
import Exception.UserHasNoSavingAccountException;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Bella
 */
@Local
public interface TransferSessionBeanLocal {

    public void intraOneTimeTransferCheck(Long customerID, Long giverBankAccountNum, Long recipientBankAccountNum, BigDecimal transferAmount) throws TransferException;

    public void addPayee(Long payeeAccount, String payeeName, Long customerID) throws PayeeNotFoundException;

    public List getPayeeList(Long customerID);

    public String searchPayeeName(Long payeeAccount);

    public List<String> getSavingAccountNumbers(Long customerID) throws UserHasNoSavingAccountException;

    public void changeTransferLimit(Long customerID, BigDecimal transferLimit);

    public boolean checkTransferLimit(Long customerID, Long savingAccountNum, BigDecimal transferAmount);

    public void deletePayee(Long customerID, Long accountNum);

    public BigDecimal getTransferLimit(Long customerID);

    public List<TransactionRecord> getTransactionRecord(Long savingAccountNumber);


    public void logAction(String description, Long customerId);

    public void payeeTransferCheck(Long customerID, Long giverBankAccountNum, Long recipientBankAccountNum, BigDecimal transferAmount) throws TransferException;

    public boolean checkPayeeValidity(Long payeeAccount);

    public void interOneTimeTransferCheck(Long customerID, Long giverBankAccountNum, Long recipientBankAccountNum, String recipientBankAccountName, BigDecimal transferAmount) throws TransferException;

    public List<OtherBank> viewOtherBank();

    public boolean intraOneTimeTransferCheckMobile(String customerIC, String giverBankAccountString, String recipientBankAccountString, String transferAmountStr) throws TransferException;

    }
