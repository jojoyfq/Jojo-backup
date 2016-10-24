/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import DepositEntity.SavingAccount;
import Exception.ListEmptyException;
import Exception.NotEnoughAmountException;
import WealthEntity.DiscretionaryAccount;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface WealthSessionBeanLocal {
    public Long existingCustomerActivateAccount(Long customerId,Long accountId)throws NotEnoughAmountException;
    public List<DiscretionaryAccount> displayAvailableDiscretionaryAccounts(Long customerId) throws ListEmptyException;
    public List<SavingAccount> displaySavingAccounts(Long customerId) throws ListEmptyException;
    public Long topUpBySaving(Long customerId, Long savingAccountId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException;
     public Long transferBackToSaving(Long customerId, Long savingAccountId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException;
     public List<DiscretionaryAccount> displayAllDiscretionaryAccounts(Long customerId) throws ListEmptyException;
}
