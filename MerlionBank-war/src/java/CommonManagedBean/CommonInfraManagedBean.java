/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.AccountManagementSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import Logger.MyLogger;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

/**
 *
 * @author apple
 */
@Named(value = "commonInfraManaged")
@Dependent
public class CommonInfraManagedBean implements Serializable {

    @EJB
    AccountManagementSessionBeanLocal amsbl;
    
    private Customer customer;
    private String IC;
    private String customerName;
    private String customerGender; 
    private Date customerDateOfBirth;
    private String customerAddresss;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerOccupation;
    private String customerFamilyInfo;
    private String customerFinancialAsset;
    private String customerFinancialGoal;
    
  //  private final static Logger LOGGER = Logger.getLogger(CommonInfraManagedBean.class.getName());
    /**
     * Creates a new instance of CommonInfraManaged
     */
   
   //  public CommonInfraManagedBean() {
   //     try {
   //         MyLogger.setup();
   //     } catch (IOException e) {
   //         e.printStackTrace();
   //         throw new RuntimeException("Problems with creating the log files");
   //     }
   //     LOGGER.setLevel(Level.INFO);
   // }
    @PostConstruct
     public void init() {
         customer = new Customer();
         
     }
       public void setAllVariables() throws IOException{
            if (FacesContext.getCurrentInstance().getResponseComplete()) {
            System.out.println("lala");
            return;
        }
            IC = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("IC");
            customerName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerName"); 
            customerGender = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerGender");
            customerDateOfBirth = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerDateOfBirth");
            customerAddresss = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerAddress");
            customerEmail = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerEmail");
            customerPhoneNumber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerPhoneNumber");
            customerOccupation = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerOccupation");
            customerFamilyInfo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFamilyInfo");
            customerFinancialAsset = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFinancialAsset");
            customerFinancialGoal = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerFinancialGoal");
             
            amsbl.createAccount(IC, customerName,customerName,customerDateOfBirth,customerAddresss,customerEmail, customerPhoneNumber, customerOccupation,customerFamilyInfo, customerFinancialAsset, customerFinancialGoal);
       
       }
  }

