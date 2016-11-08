/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.CreditCardTransaction;
import CardEntity.DebitCardTransaction;
import Exception.SearchException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Bella
 */
@Local
public interface SearchCardSessionBeanLocal {
     public List<String> searchByCardNo(String cardNo) throws SearchException ;
     public List<DebitCardTransaction> getDebitCardTransaction(String cardNo);
     public List<CreditCardTransaction> getCreditCardTransaction(String cardNo);
}
