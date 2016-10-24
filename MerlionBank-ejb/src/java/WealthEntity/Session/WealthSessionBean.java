/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import CommonEntity.Customer;
import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
import Exception.ListEmptyException;
import Exception.NotEnoughAmountException;
import LoanEntity.Loan;
import WealthEntity.DiscretionaryAccount;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author a0113893
 */
@Stateless
public class WealthSessionBean implements WealthSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long existingCustomerActivateAccount(Long customerId, Long accountId) throws NotEnoughAmountException {
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, accountId);
        Customer customer = em.find(Customer.class, customerId);
        BigDecimal amount = discretionaryAccount.getBalance();
        BigDecimal currentAmount = new BigDecimal(200000);
        int res = amount.compareTo(currentAmount);
        if (res == 0 || res == 1) {
            discretionaryAccount.setStatus("active");
            return accountId;
        } else {
            throw new NotEnoughAmountException("This account has not met the minimum SG$200000 requirement");
        }
    }

    @Override
    public List<DiscretionaryAccount> displayAvailableDiscretionaryAccounts(Long customerId) throws ListEmptyException {
        Customer customer = em.find(Customer.class, customerId);
        List<DiscretionaryAccount> discretionaryAccounts = customer.getDiscretionaryAccounts();
        List<DiscretionaryAccount> temp = new ArrayList<DiscretionaryAccount>();

        for (int i = 0; i < discretionaryAccounts.size(); i++) {
            if (!discretionaryAccounts.get(i).getStatus().equals("terminated")) {
                temp.add(discretionaryAccounts.get(i));
            }
        }

        if (temp.isEmpty()) {
            throw new ListEmptyException("You do not have any available discretionary accounts.");
        }
        return temp;
    }

    @Override
    public List<SavingAccount> displaySavingAccounts(Long customerId) throws ListEmptyException {
        Customer customer = em.find(Customer.class, customerId);
        List<SavingAccount> currentSavingAccounts = customer.getSavingAccounts();
        List<SavingAccount> savingAccounts = new ArrayList<SavingAccount>();

        for (int i = 0; i < currentSavingAccounts.size(); i++) {
            if (currentSavingAccounts.get(i).getStatus().equals("active")) {
                savingAccounts.add(currentSavingAccounts.get(i));
            }
        }
        if (savingAccounts.isEmpty()) {
            throw new ListEmptyException("You do not have any active saving accounts.");
        }
        return savingAccounts;
    }

    @Override
    public Long topUpBySaving(Long customerId, Long savingAccountId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException {
        Customer customer = em.find(Customer.class, customerId);
        SavingAccount savingAccount = em.find(SavingAccount.class, savingAccountId);
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);

        if (amount.compareTo(savingAccount.getAvailableBalance()) == 1) {
            throw new NotEnoughAmountException("There is not enough amount of money in this savingAccount");
        }

        discretionaryAccount.setBalance(discretionaryAccount.getBalance().add(amount));

        savingAccount.setAvailableBalance(savingAccount.getAvailableBalance().subtract(amount));
        savingAccount.setBalance(savingAccount.getBalance().subtract(amount));

        Date currentTime = Calendar.getInstance().getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

        TransactionRecord transactionRecord = new TransactionRecord("WT", amount, null, "settled", "Discretionary Account Top up", currentTimestamp, savingAccount.getAccountNumber(), discretionaryAccount.getAccountNumber(), savingAccount, "MerlionBank", "MerlionBank");
        savingAccount.getTransactionRecord().add(transactionRecord);
        em.persist(transactionRecord);
        em.flush();

        return discretionaryAccountId;
    }
    
    @Override
    public Long transferBackToSaving(Long customerId, Long savingAccountId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException {
        Customer customer = em.find(Customer.class, customerId);
        SavingAccount savingAccount = em.find(SavingAccount.class, savingAccountId);
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);

        if (amount.compareTo(discretionaryAccount.getBalance()) == 1) {
            throw new NotEnoughAmountException("There is not enough amount of money in this Discretionary Account");
        }

        savingAccount.setBalance(savingAccount.getBalance().add(amount));
        savingAccount.setBalance(savingAccount.getAvailableBalance().add(amount));

        discretionaryAccount.setBalance(discretionaryAccount.getBalance().subtract(amount));

        Date currentTime = Calendar.getInstance().getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

        TransactionRecord transactionRecord = new TransactionRecord("WT", amount, null, "settled", "Discretionary Account Transfer Back", currentTimestamp, discretionaryAccount.getAccountNumber(), savingAccount.getAccountNumber(), savingAccount, "MerlionBank", "MerlionBank");
        savingAccount.getTransactionRecord().add(transactionRecord);
        em.persist(transactionRecord);
        em.flush();

        return discretionaryAccountId;
    }
    
   @Override
    public List<DiscretionaryAccount> displayAllDiscretionaryAccounts(Long customerId) throws ListEmptyException {
        Customer customer = em.find(Customer.class, customerId);
        List<DiscretionaryAccount> discretionaryAccounts = customer.getDiscretionaryAccounts();
        
        if (discretionaryAccounts.isEmpty()) {
            throw new ListEmptyException("You do not have any available discretionary accounts.");
        }
        return discretionaryAccounts;
    }
    
//    @Override 
//  public Long createTailoredPortfolio(Long customerId,BigDecimal investAmount, Double expectedRateOfReturn,Double foreignExchange,Double equity,Double bond){
//    Customer customer = em.find(Customer.class, customerId);
//      
//    }
}
