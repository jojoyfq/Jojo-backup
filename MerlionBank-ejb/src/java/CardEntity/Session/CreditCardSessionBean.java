/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardEntity.Session;

import CardEntity.CreditCardType;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Bella
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanLocal{

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<String> getCreditCardType(){
        List<String> typeList = new ArrayList();
        Query m = em.createQuery("SELECT b FROM CreditCardType b");
        List<CreditCardType> types = m.getResultList();       
        for(int i=0; i<types.size();i++){
            typeList.add(types.get(i).getCreditCardType());
        }        
        return typeList;
    }
}
