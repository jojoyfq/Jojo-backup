/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonManagedBean;

import CommonEntity.Customer;
import CommonEntity.Session.InboxManagementSessionBeanLocal;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author apple
 */
@Named(value = "messageManagedBean")
@SessionScoped
public class MessageManagedBean implements Serializable {

    @EJB
    InboxManagementSessionBeanLocal imsbl;

    private String customerIc;
    private String staffId;
    private String messageSubject;
    private String content;
    private Customer customer;
    private Long customerId;

    /**
     * Creates a new instance of MessageManagedBean
     */
    public MessageManagedBean() {
    }

    @PostConstruct
    public void init() {
        Customer customer = new Customer();
        customerId = customer.getId();

    }

    public void verifyCustomerDetails(ActionEvent event) throws UserNotExistException, UserNotActivatedException {
        if (customerIc != null) {
            try {
                Long msg = imsbl.verifyCustomer(customerIc);

                System.out.println(msg+"Customer verification is successful!");

            } catch (UserNotExistException | UserNotActivatedException ex) {
                System.out.println(ex.getMessage());
            }
        } else {

            System.out.println("Please do not leave any blanks");
        }
    }

    public String getCustomerIc() {
        return customerIc;
    }

    public void setCustomerIc(String customerIc) {
        this.customerIc = customerIc;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
