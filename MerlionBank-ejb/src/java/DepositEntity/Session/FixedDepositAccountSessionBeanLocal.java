/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import DepositEntity.FixedDepositAccount;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ruijia
 */
@Local
public interface FixedDepositAccountSessionBeanLocal {

    /**
     *
     * @param ic
     * @param amount
     * @param dateOfStart
     * @param dateOfEnd
     * @param duration
     * @param status
     * @param interest
     * @return
     */


    public Long createFixedAccount(Long customerId, BigDecimal amount, Date dateOfStart, Date dateOfEnd, String duration);

    public boolean transferToFixedDeposit(Long savingAccountNum, Long fixedDepositAccountNum, BigDecimal amount, Long customerId);

    public List<FixedDepositAccount> getFixedDepositAccounts(Long customerId);

    public List<Long> getNoMoneyFixedDeposit(Long customerId);

    public BigDecimal amountLessBalance(Long accountNum);

    public List<String> formatDate(Long accountNum);

    public BigDecimal getAmount(Long accountNum);

    public FixedDepositAccount getAccount(Long accountNum);

    public Long createFixedAccount(String ic, BigDecimal amount, Date dateOfStart, Date dateOfEnd, String duration);

    public void logAction(String description, Long customerId);

    public void earlyWithdraw(Long fixedAccountNum, Long savingAccountNum);

    public BigDecimal calculateInterestEarly(Long accountNum, Double interestRate);

    public List<Long> getWithdrawable(Long customerId);

    public List<Long> getRenewable(Long customerId);

    public void renewFixed(Long accountNumber);

    public List<String> getRenewDates(Long accountNumber);

    public BigDecimal getBalance(Long accountNum);

    public void checkFixedDepositAccountStatus();
    
}
