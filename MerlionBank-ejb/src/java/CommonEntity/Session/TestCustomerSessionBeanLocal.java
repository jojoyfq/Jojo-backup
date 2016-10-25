/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import java.math.BigDecimal;
import javax.ejb.Local;

/**
 *
 * @author ruijia
 */
@Local
public interface TestCustomerSessionBeanLocal {

    public void setStatus(Long customerId, BigDecimal balance);

    public void setPassword(Long customerId, String password);
    
}
