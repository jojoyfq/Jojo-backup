/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface LoanSessionBeanLocal {
   public Long createHomeLoan(Customer customer,Long loanTypeId,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate); 
   public Long createCarLoan(Customer customer,Long loanTypeId,BigDecimal principal,BigDecimal downpayment,Integer loanTerm,Date startDate);

}
