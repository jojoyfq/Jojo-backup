/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServiceClient;

import BillEntity.Session.BillSessionBeanLocal;
import DepositEntity.TransactionRecord;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author ruijia
 */
@ManagedBean
@RequestScoped
public class SACHManagedBean {

    @WebServiceRef(wsdlLocation = "http://localhost:8080/SACHWebService/SACHWebService?WSDL")
    private SACHWebService_Service service;

    @EJB
    BillSessionBeanLocal bsbl;

    private List<TransactionRecord> records;

    /**
     * Creates a new instance of SACHManagedBean
     */
    public SACHManagedBean() {
    }

    @PostConstruct
    public void init() {
    }

    public void sendAllTransaction(ActionEvent event) {
        System.out.print("inside sach managedbean");
        records = bsbl.sendInterbankTransactions();
        Date todayDate = new Date();
        SimpleDateFormat dateFormator = new SimpleDateFormat("dd-MM-yyyy");
        String todayDateStr = dateFormator.format(todayDate);
        for (int i = 0; i < records.size(); i++) {
          
            this.recordOneTransaction("Merlion", records.get(i).getRecipientBankName(), records.get(i).getCode(), records.get(i).getGiverAccountNum().toString(), records.get(i).getGiverAccountNum().toString(), records.get(i).getDebit().doubleValue(), todayDateStr);
            System.out.print("send to SACH success!");
        }
    }

    private boolean recordOneTransaction(java.lang.String giverBank, java.lang.String receiptBank, java.lang.String code, java.lang.String giverAccount, java.lang.String receiptAccount, double amount, java.lang.String transactionTime) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        WebServiceClient.SACHWebService port = service.getSACHWebServicePort();
        return port.recordOneTransaction(giverBank, receiptBank, code, giverAccount, receiptAccount, amount, transactionTime);
    }

}
