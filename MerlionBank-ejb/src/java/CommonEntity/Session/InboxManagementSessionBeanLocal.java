/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonEntity.Session;


import CommonEntity.CustomerMessage;
import CommonEntity.MessageEntity;

import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.UnexpectedErrorException;
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
 public MessageEntity sendMessage(Long customerId,Long staffID, String subject,String content)throws EmailNotSendException;

  // customer view list of message
 public List viewAllMessage(Long customerId)throws ListEmptyException;
 //public List viewAllMessage(String customerName);
 // customer update status from new to read
 public MessageEntity readMessage(Long messageID);
 
 //customer delete message
 public boolean deleteMessage(Long messageID);
 
 //system display number of new messages
 public int countNewMessage(Long customerID);
 
 //customer reply staff message 
 public CustomerMessage customerSendMessage(String subject, String content, String status, Long staffID, Long customerID)throws EmailNotSendException;
 
 // staff view list of message
 public List<CustomerMessage> StaffViewAllMessage(Long staffId);//throws ListEmptyException;
 
 // staff update status from new to read
 public CustomerMessage readCustomerMessage(Long messageID);
 
  //system display number of new messages for staff
 public int countStaffNewMessage(Long staffID);
 
 public MessageEntity diaplayMessage(Long id);
 
 //staff delete customer message
 public List<CustomerMessage> deleteCustomerMessage(Long messageID,Long staffId)throws UnexpectedErrorException;
 
 //customer compose collaborative message 
 public CustomerMessage customerSendCaseMessage(String subject, String content, String status, Long customerID);

// staff view list of case message
 public List<CustomerMessage> StaffViewAllCaseMessage();
 
  //system display number of new case messages for staff
 public int countStaffNewCaseMessage( );
 

}
