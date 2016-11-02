/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BillEntity.Session;

import Exception.NotEnoughAmountException;
import javax.ejb.Local;

/**
 *
 * @author shuyunhuang
 */
@Local
public interface BillTimerSessionBeanLocal {

    public void recurrentBillDeduction() throws NotEnoughAmountException;
    
}
