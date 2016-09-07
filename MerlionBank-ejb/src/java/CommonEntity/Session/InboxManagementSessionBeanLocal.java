/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import Exception.EmailNotSendException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import javax.ejb.Local;
import javax.mail.MessagingException;

/**
 *
 * @author a0113893
 */
@Local
public interface InboxManagementSessionBeanLocal {
   public Long verifyCustomer(String customerIc) throws UserNotExistException, UserNotActivatedException;
    public boolean sendMessage(Long customerId,String staffID, String subject,String content)throws EmailNotSendException;
}
