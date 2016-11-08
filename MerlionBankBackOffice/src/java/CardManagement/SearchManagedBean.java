/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardManagement;

import CardEntity.CreditCardTransaction;
import CardEntity.DebitCardTransaction;
import CardEntity.Session.SearchCardSessionBeanLocal;
import Exception.SearchException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Bella
 */
@Named(value = "searchManagedBean")
@SessionScoped
public class SearchManagedBean implements Serializable {

    @EJB
    SearchCardSessionBeanLocal scsbl;
    
    private String cardNo;
    private List<String> cardInfo;
    List<List<String>> cardInfos;
    private List<DebitCardTransaction> debitTrans;
    private List<CreditCardTransaction> creditTrans;

    public SearchManagedBean() {
    }

    public void dashboardToSearchByCardNo(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/CardManagement/searchByCardNo_EnterCard.xhtml");
        } catch (Exception e) {
            System.out.print("dashboard to search by card number encounter error!");
        }
    }

    public void showCardDetail(ActionEvent event) throws IOException {
        try {
            if (cardNo != null) {
                cardInfo = scsbl.searchByCardNo(cardNo);
                cardInfos = new ArrayList();
                cardInfos.add(cardInfo);
                System.out.print(cardInfo);
                FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/CardManagement/searchByCardNo_displayDetail.xhtml");
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", "Please enter card number first! ");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (SearchException e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Message", e.getMessage());
                RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    public void viewTransHistory(ActionEvent event) throws IOException{
        if(cardInfo.get(3).equals("debit card")){
            debitTrans = scsbl.getDebitCardTransaction(cardNo); 
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/CardManagement/searchByCardNo_displayDebitTrans.xhtml");            
        }else if(cardInfo.get(3).equals("credit card")){
            creditTrans = scsbl.getCreditCardTransaction(cardNo);
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/MerlionBankBackOffice/CardManagement/searchByCardNo_displayCreditTrans.xhtml");  
        }
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public List<String> getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(List<String> cardInfo) {
        this.cardInfo = cardInfo;
    }

    public List<DebitCardTransaction> getDebitTrans() {
        return debitTrans;
    }

    public void setDebitTrans(List<DebitCardTransaction> debitTrans) {
        this.debitTrans = debitTrans;
    }

    public List<CreditCardTransaction> getCreditTrans() {
        return creditTrans;
    }

    public void setCreditTrans(List<CreditCardTransaction> creditTrans) {
        this.creditTrans = creditTrans;
    }

    public List<List<String>> getCardInfos() {
        return cardInfos;
    }

    public void setCardInfos(List<List<String>> cardInfos) {
        this.cardInfos = cardInfos;
    }
    
    

}
