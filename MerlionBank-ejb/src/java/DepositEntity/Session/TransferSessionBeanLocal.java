/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import java.math.BigDecimal;
import javax.ejb.Local;

/**
 *
 * @author Bella
 */
@Local
public interface TransferSessionBeanLocal {
     public Boolean intraOneTimeTransferCheck(Long giverBankAccountNum, Long recipientBankAccountNum, BigDecimal transferAmount);
     public Boolean addPayee(Long payeeAccount, String payeeName, Long customerID);
}
