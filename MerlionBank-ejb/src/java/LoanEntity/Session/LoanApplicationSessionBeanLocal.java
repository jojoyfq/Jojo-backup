/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.LoanTermInvalidException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.LoanType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface LoanApplicationSessionBeanLocal {

    //Create loan Account - 1st page - create online banking account

    public Customer createNewLoanAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo) throws UserExistException, EmailNotSendException;

//Create Loan Account - 2nd page - display different type 
    //Teller Create Loan Account - 2nd page - display different type
    public List<LoanType> displayLoanType(String type) throws ListEmptyException;

    public String displayPackageDetail(String name);

    public BigDecimal fixedCalculator(BigDecimal amount, Integer loanTerm, Double rate1, Double rate2);

    //Create Loan Account - 3rd page - configure home loan 
    public Long createLoanAccount(Long customerId, BigDecimal monthlyIncome, Long loanTypeId, BigDecimal principal, BigDecimal downpayment, Integer loanTerm) throws EmailNotSendException, LoanTermInvalidException;

    public Customer tellerCreateLoanAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String enterPassword) throws UserExistException, EmailNotSendException;

    //Teller Create Loan Account - 3nd page - configure loan
    public Long StaffCreateLoanAccount(Long staffId, Long customerId, BigDecimal monthlyIncome, Long loanTypeId, BigDecimal principal, BigDecimal downpayment, Integer loanTerm) throws LoanTermInvalidException;
public Long createLoanAccountExisting(Long customerId, BigDecimal monthlyIncome, Long loanTypeId, BigDecimal principal, BigDecimal downpayment, Integer loanTerm) throws EmailNotSendException, LoanTermInvalidException;
 public Long StaffCreateLoanAccountExisting(Long staffId,Long customerId, BigDecimal monthlyIncome, Long loanTypeId, BigDecimal principal, BigDecimal downpayment, Integer loanTerm) throws LoanTermInvalidException;    

 public Long findTypeIdByName(String name);
public LoanType findTypeByName(String name);
 public Customer searchCustomer(String customerIc) throws UserNotExistException, UserNotActivatedException;

}
