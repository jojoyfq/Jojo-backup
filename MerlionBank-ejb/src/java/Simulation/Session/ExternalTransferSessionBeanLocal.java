/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation.Session;

import Exception.AccountNotExistedException;
import java.math.BigDecimal;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface ExternalTransferSessionBeanLocal {
    public boolean transferSavingAccount(Long accountNumber, BigDecimal amount)throws AccountNotExistedException;
    public boolean transferFixedDepositAccount(Long accountNumber, BigDecimal amount)throws AccountNotExistedException;
    
}
