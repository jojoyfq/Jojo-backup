/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CounterCashEntity.Session;

import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author shuyunhuang
 */
@Local
public interface CounterCashManagementLocal {


    public int recordAmount(BigDecimal amount, Date time, Long staffId);
    
}
