/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import java.math.BigDecimal;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface WealthManagementSessionBeanLocal {
    public Long topUpAccount(Long staffId,Long accountId,BigDecimal amount);
}