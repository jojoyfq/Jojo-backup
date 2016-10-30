/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import DepositEntity.SavingAccount;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.NotEnoughAmountException;
import WealthEntity.DiscretionaryAccount;
import WealthEntity.Portfolio;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface WealthSessionBeanLocal {

    public Long existingCustomerActivateAccount(Long customerId, Long accountId) throws NotEnoughAmountException;

    public List<DiscretionaryAccount> displayAvailableDiscretionaryAccounts(Long customerId) throws ListEmptyException;

    public List<SavingAccount> displaySavingAccounts(Long customerId) throws ListEmptyException;

    public Long topUpBySaving(Long customerId, Long savingAccountId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException;

    public Boolean compareAmount(Long customerId,long discretionaryAccountId, BigDecimal amount);
    public Long transferBackToSavingWithEnoughBalance(Long customerId, Long savingAccountId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException;
     public Long transferBackToSavingWithNotEnoughBalance(Long customerId, Long savingAccountId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException ;

    public List<DiscretionaryAccount> displayAllDiscretionaryAccounts(Long customerId) throws ListEmptyException;

    public Long createPrefefinedPlan(Long customerId, Long accountId, BigDecimal initialAmount, String type, int term) throws NotEnoughAmountException;

    public Long staffCreatePrefefinedPlan(Long staffId, Long customerId, Long accountId, BigDecimal initialAmount, String type, int term) throws NotEnoughAmountException;

    public Long createTailoredPortfolio(Long customerId, Long discretionaryAccountId, BigDecimal investAmount, Double expectedRateOfReturn, Double foreignExchange, Double equity, Double bond, int term) throws NotEnoughAmountException;

    public List<Portfolio> displayAllPortfolios(Long discretionaryAccountId);

    public List<Portfolio> customerCancelPortfolios(Long portfolioId);

    public List<Portfolio> customerAcceptPlan(Long customerId, Long portfolioId) throws EmailNotSendException;

public List<Portfolio> ModifyPortfolios(Long portfolioId, Double expectedRateOfReturn,Double foreignExchange,Double equity,Double stock,int term) throws EmailNotSendException;

public List<Portfolio> portfolioEarlyWithdraw(Long portfolioId);
public Long payCommissionFee(Long customerId, Long discretionaryAccountId) throws NotEnoughAmountException;
}
