/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation.Session;

import CommonEntity.Staff;
import DepositEntity.FixedDepositAccount;
import DepositEntity.SavingAccount;
import DepositEntity.TransferRecord;
import Exception.AccountNotExistedException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author a0113893
 */
@Stateless
public class ExternalTransferSessionBean implements ExternalTransferSessionBeanLocal {
@PersistenceContext
    private EntityManager em;

@Override
public boolean transferSavingAccount(Long accountNumber, BigDecimal amount,Long giverBankAccountNum, String giverBankAccountName)throws AccountNotExistedException{
   Query q = em.createQuery("SELECT a FROM SavingAccount a WHERE a.accountNumber = :accountNumber");
        q.setParameter("accountNumber", accountNumber);
        List<SavingAccount> savingAccounts = new ArrayList(q.getResultList()); 
        if (savingAccounts.isEmpty()) {
            throw new AccountNotExistedException ("Account is invalid");
        }     
            SavingAccount savingAccount=savingAccounts.get(0);
        BigDecimal targetBalance=savingAccount.getBalance().add(amount);
        BigDecimal targetAvailableBalance=savingAccount.getAvailableBalance().add(amount);
        savingAccount.setBalance(targetBalance);
        savingAccount.setAvailableBalance(targetAvailableBalance);
        em.persist(savingAccount);
        em.flush();
        
        Date currentTime = Calendar.getInstance().getTime();
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());
                
                TransferRecord transferRecord = new TransferRecord("TF", amount, "settled", "Interbank Transfer",currentTimestamp,giverBankAccountNum,accountNumber, "interTransfer",giverBankAccountName,"MerlionBank");
                em.persist(transferRecord);
                em.flush();
        return true;
}

@Override
public boolean transferFixedDepositAccount(Long accountNumber, BigDecimal amount)throws AccountNotExistedException{
   Query q = em.createQuery("SELECT a FROM FixedDepositAccount a WHERE a.accountNumber = :accountNumber");
        q.setParameter("accountNumber", accountNumber);
        List<FixedDepositAccount> fixedDepositAccounts = new ArrayList(q.getResultList()); 
        if (fixedDepositAccounts.isEmpty()) {
            throw new AccountNotExistedException ("Account is invalid");
        }     
            FixedDepositAccount fixedDepositAccount=fixedDepositAccounts.get(0);
        BigDecimal targetBalance=fixedDepositAccount.getBalance().add(amount);
        fixedDepositAccount.setBalance(targetBalance);
        em.persist(fixedDepositAccount);
        em.flush();
        return true;
}


}
