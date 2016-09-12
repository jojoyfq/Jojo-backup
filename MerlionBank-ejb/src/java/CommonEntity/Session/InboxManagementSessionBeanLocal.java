/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;

import CommonEntity.CustomerMessage;
import Exception.EmailNotSendException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import java.util.List;
import javax.ejb.Local;
import javax.mail.MessagingException;

/**
 *
 * @author a0113893
 */
@Local
public interface InboxManagementSessionBeanLocal {
   
 //1st - staff choose customer before send message
 public Long verifyCustomer(String customerIc) throws UserNotExistException, UserNotActivatedException;
 
 // 2nd- staff send customer message
 public boolean sendMessage(Long customerId,Long staffID, String subject,String content)throws EmailNotSendException;

  // customer view list of message
 public List viewAllMessage(Long customerId);
 
 // customer update status from new to read
 public boolean readMessage(Long messageID);
 
 //customer delete message
 public boolean deleteMessage(Long messageID);
 
 //system display number of new messages
 public int countNewMessage(Long customerID);
 
 //customer reply staff message 
 public boolean customerSendMessage(String subject, String content, String status, Long staffID, Long customerID)throws EmailNotSendException;
 
 // staff view list of message
 public List<CustomerMessage> StaffViewAllMessage(Long staffId);
 
 // staff update status from new to read
 public boolean readCustomerMessage(Long messageID);
 
  //system display number of new messages for staff
 public int countStaffNewMessage(Long staffID);
 
}
