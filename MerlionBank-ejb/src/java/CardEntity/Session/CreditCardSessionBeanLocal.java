/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Bella
 */
@Local
public interface CreditCardSessionBeanLocal {
    public List<String> getCreditCardType();
    
}
