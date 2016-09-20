/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
import Exception.UserHasNoInactiveSavingAccountException;
import Exception.UserHasNoSavingAccountException;
import Exception.UserHasPendingTransactionException;
import Exception.UserNotEnoughBalanceException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Bella
 */
@Local
public interface SavingAccountSessionBeanLocal {

    public List<SavingAccount> getSavingAccount(Long customerID)throws UserHasNoSavingAccountException;
    public List<String> getSavingAccountType();
    public List<Long> getSavingAccountNumbers(Long customerID) throws UserHasNoSavingAccountException;
    public List<List> getTransactionRecord(Long savingAccountNumber);
    public List<SavingAccount> getSavingAccountForCloseAccount(Long savingAccountNum);
    public void checkPendingTransaction(Long savingAccountNum) throws UserHasPendingTransactionException;
    public List<Long> getInactiveSavingAccountNumbers(Long customerID) throws UserHasNoInactiveSavingAccountException;
    public void checkInactiveSavingAccount(Long inactiveSavingAccountNum) throws UserNotEnoughBalanceException;
}
