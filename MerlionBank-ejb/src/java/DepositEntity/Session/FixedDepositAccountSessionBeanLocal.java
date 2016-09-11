/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepositEntity.Session;

import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author ruijia
 */
@Local
public interface FixedDepositAccountSessionBeanLocal {

    public Boolean createFixedAccount(String ic, BigDecimal amount, Date dateOfStart, Date dateOfEnd, String duration, Double interest);
    
}
