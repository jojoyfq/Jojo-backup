/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author apple
 */
@Named(value = "accountTypeManagedBean")
@SessionScoped
public class AccountTypeManagedBean implements Serializable {

    private List accountTypes;
    private String accountType;
    //private List data = new ArrayList();

    public List getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(List accountTypes) {
        this.accountTypes = accountTypes;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * Creates a new instance of AccountTypeManagedBean
     */
    public AccountTypeManagedBean() {
    }

    @PostConstruct
    public void init() {

        try {
            accountTypes = new ArrayList();
            accountTypes.add("Saving Account");
            accountTypes.add("Fixed Deposit Account");
            accountTypes.add("Credit Account");
            accountTypes.add("Loan Account");
            accountTypes.add("Wealth Managment Account");
            System.out.println(accountTypes);

            //    System.out.println("Account Type chosen is " + accountType);
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public void chooseAccountType(ActionEvent event) throws IOException {

//        FacesMessage msg;
//        msg = new FacesMessage("Selected" + accountType);
        System.out.println("Account Type chosen is ");
        try {
            switch (accountType) {
                case "Saving Account":
                    System.out.println("Saving account has been selected");
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerManagement/createSavingAccount.xhtml");
                    break;
                case "Fixed Deposit Account":
                    System.out.println("Fixed deposit account has been selected");
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/CustomerManagement/createFixedDepositAccount.xhtml");
                    break;
                case "Credit Account":
                    System.out.println("This service is currently not available!");
                    break;
                case "Loan Account":
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/MerlionBank-war/LoanManagement/createLoanAccount.xhtml");
                    break;
                case "Wealth Management Account":
                    System.out.println("This service is currently not available!");
                    break;
                default:
                    System.out.println("Please indicate one choice");
                    break;
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }
}
