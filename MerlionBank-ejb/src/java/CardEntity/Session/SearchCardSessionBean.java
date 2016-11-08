/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.CreditCard;
import CardEntity.CreditCardTransaction;
import CardEntity.DebitCard;
import CardEntity.DebitCardTransaction;
import Exception.SearchException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Bella
 */
@Stateless
public class SearchCardSessionBean implements SearchCardSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<String> searchByCardNo(String cardNo) throws SearchException {
        List<String> cardInfo = new ArrayList();
        Long cardNoL = Long.parseLong(cardNo);

        Query q = em.createQuery("SELECT a FROM DebitCard a WHERE a.cardNumber = :cardNoL");
        q.setParameter("cardNoL", cardNoL);
        List<DebitCard> debitCards = new ArrayList(q.getResultList());

        if (debitCards.isEmpty()) {
            Query m = em.createQuery("SELECT a FROM CreditCard a WHERE a.cardNumber = :cardNoL");
            m.setParameter("cardNoL", cardNoL);
            List<CreditCard> creditCards = new ArrayList(m.getResultList());
            if (creditCards.isEmpty()) {
                throw new SearchException("The card number you entered is not valid! Please try again!");
            } else {
                CreditCard creditCard = creditCards.get(0);
                cardInfo.add(creditCard.getCardHolder());
                cardInfo.add(creditCard.getCardNumber().toString());
                cardInfo.add(creditCard.getCreditCardType().getCreditCardType());
                cardInfo.add("credit card");
                cardInfo.add(creditCard.getStatus());
                return cardInfo;
            }
        } else {
            DebitCard debitCard = debitCards.get(0);
            cardInfo.add(debitCard.getCardHolder());
            cardInfo.add(debitCard.getCardNumber().toString());
            cardInfo.add(debitCard.getDebitCardType().getDebitCardType());
            cardInfo.add("debit card");
            cardInfo.add(debitCard.getStatus());
            return cardInfo;
        }
    }
    
    @Override
    public List<DebitCardTransaction> getDebitCardTransaction(String cardNo){
        Long cardNoL = Long.parseLong(cardNo);

        Query q = em.createQuery("SELECT a FROM DebitCard a WHERE a.cardNumber = :cardNoL");
        q.setParameter("cardNoL", cardNoL);
        DebitCard debitCard = (DebitCard)q.getSingleResult();
        
        return debitCard.getDebitCardTransaction();
    } 
    
    @Override
    public List<CreditCardTransaction> getCreditCardTransaction(String cardNo){
        Long cardNoL = Long.parseLong(cardNo);

        Query q = em.createQuery("SELECT a FROM CreditCard a WHERE a.cardNumber = :cardNoL");
        q.setParameter("cardNoL", cardNoL);
        CreditCard creditCard = (CreditCard)q.getSingleResult();
        
        return creditCard.getCreditCardTransactions();
    } 

}
