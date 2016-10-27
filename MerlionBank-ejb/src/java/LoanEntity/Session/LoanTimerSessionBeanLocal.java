/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import Exception.EmailNotSendException;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface LoanTimerSessionBeanLocal {
   //timer 
    // close redundant account
    public void closeAccounts();
    
    //run daily- check all loan accounts
   
    
    public void updateMonthlyPayment(Date currentDate) throws EmailNotSendException;

    public void autoBadDebt() throws EmailNotSendException;

    public void updateMonthlyPayment() throws EmailNotSendException;

    public void calculateLatePayment() throws EmailNotSendException;
}
