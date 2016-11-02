/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CommonEntity.OnlineAccount;
import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.Staff;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.LoanTermInvalidException;
import Exception.UserExistException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.Loan;
import LoanEntity.LoanType;
import Other.Session.GeneratePassword;
import Other.Session.sendEmail;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joda.time.DateTime;

/**
 *
 * @author a0113893
 */
@Stateless
public class LoanApplicationSessionBean implements LoanApplicationSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    private static final Random RANDOM = new SecureRandom();

    
    public static final int SALT_LENGTH = 8;

    
    @EJB
    private StaffManagementSessionBeanLocal smsbl;

    //Create loan Account - 1st page - create online banking account
    @Override
    public Customer createNewLoanAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo) throws UserExistException, EmailNotSendException {
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        System.out.println("Inside createAccount");

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (!temp.isEmpty()) {
            System.out.println("User " + ic + " exists!");
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getStatus().equals("active")) {
                    throw new UserExistException("User " + ic + " exists! Please go and login");
                } else if (temp.get(i).getStatus().equals("unverified")) {
                    throw new UserExistException("User " + ic + "has not been verified by MerlionBank!");

                } else if (temp.get(i).getStatus().equals("locked")) {
                    throw new UserExistException("User " + ic + " account has been locked. Please unlock your account!");
                } else if (temp.get(i).getStatus().equals("inactive")) {
                    throw new UserExistException("User " + ic + " has an inavtive account. Please proceed to activation.");
                }
            }

        }
        System.out.println("customer does not exist!");

        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }
        String password = GeneratePassword.createPassword();
        String tempPassword = password;

        password = passwordHash(password + salt);
        System.out.println("Password after hash&salt:" + password);

        System.out.println("In Creating  account");
        OnlineAccount onlineAccount = new OnlineAccount(ic, "inactive", salt, password);
        em.persist(onlineAccount);
        BigDecimal intraTransferLimit = new BigDecimal(1000);
        Customer customer = new Customer(ic, name, gender, dateOfBirth, address, email, phoneNumber, occupation, familyInfo, null, "0.0000", onlineAccount, "unverified", intraTransferLimit);
        em.persist(customer);
        em.flush();
        System.out.println("Create Customer successfully");

        return customer;
    }

    private String passwordHash(String pass) {
        String md5 = null;

        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(pass.getBytes(), 0, pass.length());

            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    //Create Loan Account - 2nd page - display different type 
    //Teller Create Loan Account - 2nd page - display different type 
    @Override
    public List<LoanType> displayLoanType(String type) throws ListEmptyException {
        Query q = em.createQuery("SELECT a FROM LoanType a WHERE a.type = :type");
        q.setParameter("type", type);
        List<LoanType> temp = new ArrayList(q.getResultList());
        if (temp.isEmpty()) {
            throw new ListEmptyException("There are no available service under this type of loan");
        }
        return temp;
    }

    @Override
    public String displayPackageDetail(String name) {
        Query q = em.createQuery("SELECT a FROM LoanType a WHERE a.name = :name");
        q.setParameter("name", name);
        LoanType temp = (LoanType) q.getSingleResult();
        return temp.getDescription();
    }

    @Override
    public BigDecimal fixedCalculator(BigDecimal amount, Integer loanTerm, Double rate1, Double rate2) {
        BigDecimal baseRate = new BigDecimal(rate1);
        BigDecimal baseRate2 = new BigDecimal(rate2);
        BigDecimal month = new BigDecimal("12");
        BigDecimal temp = new BigDecimal("1");
        BigDecimal term = new BigDecimal(loanTerm);
        BigDecimal trueRate = baseRate.add(baseRate2).divide(month,MathContext.DECIMAL128);
        trueRate.setScale(2, RoundingMode.HALF_UP);
        BigDecimal temp2 = trueRate.add(temp).pow(loanTerm);
        BigDecimal temp3 = amount.multiply(trueRate).multiply(temp2,MathContext.DECIMAL128);
        temp3.setScale(2, RoundingMode.HALF_UP);
        BigDecimal temp4 = temp2.subtract(temp);
        BigDecimal monthlyPayment = temp3.divide(temp4,MathContext.DECIMAL128);
        monthlyPayment.setScale(2, RoundingMode.HALF_UP);
        System.out.println("monthly payment is" + monthlyPayment);
        return monthlyPayment;

    }

    //Create Loan Account - 3rd page - configure home loan 
    @Override
    public Long createLoanAccount(Long customerId, BigDecimal monthlyIncome, Long loanTypeId, BigDecimal principal, BigDecimal downpayment, Integer loanTerm, String homeType,String homeAddress,Long postCode,String carModel,BigDecimal existingDebit,Long postalCode,String carMode,String institution,String major,Date graduationDate) throws EmailNotSendException, LoanTermInvalidException {
        Customer customer=em.find(Customer.class,customerId);
        LoanType loanType = em.find(LoanType.class, loanTypeId);
        BigDecimal monthlyPayment = new BigDecimal("0");
        Double temp = 0.0;
        Long accountNum = this.generateLoanAccountNumber();
        BigDecimal TDSR=new BigDecimal(0);
        BigDecimal MSR=new BigDecimal(0);

        if (loanType.getType().equals("Home")) {
            if (loanTerm > 420) {
                throw new LoanTermInvalidException("Home repayment period can only be max 35 years");
            }

            if (principal.multiply(new BigDecimal("0.2")).compareTo(downpayment) == 1) {
                throw new LoanTermInvalidException("Down Payment must be greater than 20% of your total asset value");
            }
            
            
            if (loanType.getName().equals("SIBOR Package")) {
                monthlyPayment = fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getSIBOR(), loanType.getSIBORrate1());
               
            } else if (loanType.getName().equals("Fixed Interest Package")) {
                monthlyPayment = fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getFixedRate(), temp);
            }
            
             if (homeType.equals("HDB flat")){
                TDSR=calculateTDSR(monthlyIncome,existingDebit);
                MSR=calculateMSR(monthlyIncome,existingDebit);
                if (monthlyIncome.compareTo(TDSR)==-1){
                    throw new LoanTermInvalidException("Does not fulfill TDSR requirement");
                }
                if (monthlyIncome.compareTo(MSR)==-1){
                    throw new LoanTermInvalidException("Does not fulfill MSR requirement");
                }
                
            }else if (homeType.equals("Executive Condominium")){
                TDSR=calculateTDSR(monthlyIncome,existingDebit);
                if (monthlyIncome.compareTo(TDSR)==-1){
                    throw new LoanTermInvalidException("Does not fulfill MSR requirement");
                }
            }
        } else if (loanType.getType().equals("Car")) {
            if (loanTerm > 84) {
                throw new LoanTermInvalidException("Car repayment period can only be max 7 years");
            }

            monthlyPayment = fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getInterestRate(), temp);
            BigDecimal temp2=new BigDecimal(20000);
            BigDecimal temp3=new BigDecimal(0.7);
            BigDecimal temp4=new BigDecimal(0.6);
            
            if (principal.compareTo(temp2)==-1){
                if ((principal.subtract(downpayment)).compareTo(principal.multiply(temp3))==1)
                        throw new LoanTermInvalidException("According to MAS,for cars with an open market value (OMV) of $20,000 or less, buyers can borrow up to 70 per cent of the purchase price");

            }else {
                if ((principal.subtract(downpayment)).compareTo(principal.multiply(temp4))==1)
                        throw new LoanTermInvalidException("According to MAS,for cars with an open market value (OMV) more than $20,000 , buyers can borrow up to 60 per cent of the purchase price");
            }
        } else if (loanType.getType().equals("Education")) {
            if (loanTerm > 96) {
                throw new LoanTermInvalidException("Education repayment period can only be max 8 years");
            }
            monthlyPayment = fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getEducationRate(), temp);

        }

        Loan loan = new Loan(accountNum, principal, downpayment, loanTerm, "inactive", monthlyPayment, 0, principal.subtract(downpayment), customer);
        em.persist(loan);
        em.flush();
loan.setHomeType(homeType);
loan.setHomeAddress(homeAddress);
loan.setPostalCode(postalCode);
loan.setCarMode(carMode);
loan.setInstitution(institution);
loan.setMajor(major);
loan.setGraduationDate(graduationDate);

        loan.setLoanType(loanType);

        if (loanType.getName().equals("SIBOR Package")) {
            BigDecimal term = new BigDecimal(loanTerm);
            loan.setOutstandingBalance(monthlyPayment.multiply(term));
        } else if (loanType.getName().equals("Fixed Interest Package")) {
            BigDecimal monthly2 = fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getSIBOR(), loanType.getFixedRate2());
            BigDecimal temp2 = new BigDecimal(36);
            BigDecimal temp3 = new BigDecimal(loanTerm - 36);
            loan.setInterestRate1(loanType.getFixedRate());
            loan.setInterestRate2(loanType.getFixedRate2());
            loan.setOutstandingBalance(monthlyPayment.multiply(temp2).add(monthly2.multiply(temp3)));
        }else if (loanType.getType().equals("Car")) {
            BigDecimal term = new BigDecimal(loanTerm);
            loan.setInterestRate1(loanType.getInterestRate());
            loan.setOutstandingBalance(monthlyPayment.multiply(term));
        }else if (loanType.getType().equals("Education")) {
            BigDecimal term = new BigDecimal(loanTerm);
            loan.setInterestRate1(loanType.getEducationRate());
            loan.setOutstandingBalance(monthlyPayment.multiply(term));
        }

        Date currentTime = Calendar.getInstance().getTime();
        DateTime payDate = new DateTime(currentTime);
        DateTime currentTime1 = payDate.plusMonths(1);
        Date currentTimestamp = currentTime1.toDate();
        loan.setStartDate(currentTimestamp);

        List<Loan> loans = new ArrayList<Loan>();
        //customer may alr have fixed acct
        if (customer.getLoans() == null) {
            loans.add(loan);
            customer.setLoans(loans);
        } else {//alr have fixed acct
            customer.getLoans().add(loan);
        }

        String password = GeneratePassword.createPassword();
        String tempPassword = password;

        password = passwordHash(password + customer.getOnlineAccount().getSalt());
        customer.getOnlineAccount().setPassword(password);
        System.out.println("Password after hash&salt:" + password);

        try {
            // reminder: remove password
            SendPendingVerificationEmail(customer.getName(), customer.getEmail(), tempPassword);

        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
        customer.setMonthlyIncome(monthlyIncome);
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Create new Loan Account", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);

        return accountNum;
    }
    
    @Override
    public BigDecimal calculateTDSR(BigDecimal monthlyIncome,BigDecimal existingDebit){
        BigDecimal temp=new BigDecimal(0.6);
        BigDecimal returningValue=monthlyIncome.subtract(existingDebit);
        returningValue=returningValue.multiply(temp);
        return returningValue;
    }
    
    @Override
     public BigDecimal calculateMSR(BigDecimal monthlyIncome,BigDecimal existingDebit){
        BigDecimal temp=new BigDecimal(0.3);
        BigDecimal returningValue=monthlyIncome.subtract(existingDebit);
        returningValue=returningValue.multiply(temp);
        return returningValue;
    }

    private void SendPendingVerificationEmail(String name, String email, String password) throws MessagingException {
        String subject = "Merlion Bank - Online Banking Account \"" + name + "\" Created - Pending Verification";

        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Congratulations! You have successfully registered a Merlion Online Banking Account!</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                // + "<br />Temporary Password: " + password + "<br />Please activate your account through this link: " + "</h2><br />"
                + "<p style=\"color: #ff0000;\">Please kindly wait for 1 to 2 working days for staff to verify you account. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>MerLION Platform User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

    private long generateLoanAccountNumber() {
        int a = 1;
        Random rnd = new Random();
        int number = 10000000 + rnd.nextInt(90000000);
        Long accountNumber = Long.valueOf(number);
        Query q2 = em.createQuery("SELECT c.accountNumber FROM Loan c");
        List<Long> existingAcctNum = new ArrayList(q2.getResultList());
        while (a == 1) {

            if ((existingAcctNum.contains(accountNumber)) || (number / 10000000 == 0)) {
                number = 10000000 + rnd.nextInt(90000000);
                accountNumber = Long.valueOf(number);
                a = 1;
            } else {
                a = 0;
            }
        }

        return accountNumber;
    }
    
    @Override
    public Customer tellerCreateLoanAccount(String ic, String name, String gender, Date dateOfBirth, String address, String email, String phoneNumber, String occupation, String familyInfo, String enterPassword) throws UserExistException, EmailNotSendException {
        String salt = "";
        String letters = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
        System.out.println("Inside createAccount");

        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", ic);
        List<Customer> temp = new ArrayList(q.getResultList());
        if (!temp.isEmpty()) {
            System.out.println("User " + ic + " exists!");
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getStatus().equals("active")) {
                    throw new UserExistException("User " + ic + " exists! Please go and login");
                } else if (temp.get(i).getStatus().equals("unverified")) {
                    throw new UserExistException("User " + ic + "has not been verified by MerlionBank!");

                } else if (temp.get(i).getStatus().equals("locked")) {
                    throw new UserExistException("User " + ic + " account has been locked. Please unlock your account!");
                } else if (temp.get(i).getStatus().equals("inactive")) {
                    throw new UserExistException("User " + ic + " has an inavtive account. Please proceed to activation.");
                }
            }

        }
        System.out.println("customer does not exist!");

        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            salt += letters.substring(index, index + 1);
        }

        String tempPassword = enterPassword;

        enterPassword = passwordHash(enterPassword + salt);
        System.out.println("Password after hash&salt:" + enterPassword);

        System.out.println("In Creating  account");
        OnlineAccount onlineAccount = new OnlineAccount(ic, "active", salt, enterPassword);
        em.persist(onlineAccount);
        BigDecimal intraTransferLimit = new BigDecimal(1000);
        Customer customer = new Customer(ic, name, gender, dateOfBirth, address, email, phoneNumber, occupation, familyInfo, null, "0.0000", onlineAccount, "active", intraTransferLimit);
        em.persist(customer);
        em.flush();
        System.out.println("Create Customer successfully");
        return customer;
    }

    //Teller Create Loan Account - 3nd page - configure loan
    @Override
    public Long StaffCreateLoanAccount(Long staffId,Long customerId, BigDecimal monthlyIncome, Long loanTypeId, BigDecimal principal, BigDecimal downpayment, Integer loanTerm,String homeType,String homeAddress,Long postCode,String carModel,BigDecimal existingDebit,Long postalCode,String carMode,String institution,String major,Date graduationDate) throws LoanTermInvalidException {
        Staff staff=em.find(Staff.class,staffId);
        Customer customer = em.find(Customer.class, customerId);
        Long accountNum=0L;
       
        try{
        accountNum=createLoanAccount(customer.getId(),monthlyIncome,loanTypeId,principal,downpayment,loanTerm,homeType,homeAddress,postCode,carModel,existingDebit,postalCode,carMode,institution,major,graduationDate);
        }catch (LoanTermInvalidException|EmailNotSendException ex){
            throw new LoanTermInvalidException(ex.getMessage());
        }
        
        smsbl.recordStaffAction(staffId, "Create new loan account " + accountNum, customerId);
        return accountNum;
    }
    
    @Override
    public Long findTypeIdByName(String name){
          Query query = em.createQuery("SELECT a FROM LoanType a WHERE a.name = :name");
        query.setParameter("name", name);
        LoanType loanType = (LoanType) query.getSingleResult();
        return loanType.getId();
    }

    @Override
     public LoanType findTypeByName(String name){
          Query query = em.createQuery("SELECT a FROM LoanType a WHERE a.name = :name");
        query.setParameter("name", name);
        LoanType loanType = (LoanType) query.getSingleResult();
        return loanType;
    }
     @Override
    public Customer searchCustomer(String customerIc) throws UserNotExistException, UserNotActivatedException {
        System.out.println("testing: " + customerIc);
        Query q = em.createQuery("SELECT a FROM Customer a WHERE a.ic = :ic");
        q.setParameter("ic", customerIc);
        List<Customer> temp = new ArrayList(q.getResultList());
        System.out.println("testing: " + temp.size());
        if (temp.isEmpty()) {
            System.out.println("Username " + customerIc + " does not exist!");
            throw new UserNotExistException("Username " + customerIc + " does not exist, please try again");
        }

        int size = temp.size();
        Customer customer = temp.get(size - 1);
        //System.out.println("testing: "+customer.getIc());
        if (customer.getStatus().equals("terminated")) {
            System.out.println("Username " + customerIc + " does not exist!");
            throw new UserNotExistException("Username " + customerIc + " does not exist, please try again");
        } else if (customer.getStatus().equals("inactive")) {
            System.out.println("Username " + customerIc + "Customer has not activated his or her account!");
            throw new UserNotActivatedException("Username " + customerIc + "Customer has not activated his or her account!");
        } else {
            System.out.println("Username " + customerIc + " IC check pass!");
        }
        return customer;

    }
    
    //Create Loan Account - 3rd page - configure home loan 
    @Override
    public Long createLoanAccountExisting(Long customerId, BigDecimal monthlyIncome, Long loanTypeId, BigDecimal principal, BigDecimal downpayment, Integer loanTerm,String homeType,String homeAddress,Long postCode,String carModel,BigDecimal existingDebit,Long postalCode,String carMode,String institution,String major,Date graduationDate) throws EmailNotSendException, LoanTermInvalidException {
        Customer customer=em.find(Customer.class,customerId);
        LoanType loanType = em.find(LoanType.class, loanTypeId);
        BigDecimal monthlyPayment = new BigDecimal("0");
        Double temp = 0.0;
        Long accountNum = this.generateLoanAccountNumber();
         BigDecimal TDSR=new BigDecimal(0);
        BigDecimal MSR=new BigDecimal(0);

        if (loanType.getType().equals("Home")) {
            if (loanTerm > 420) {
                throw new LoanTermInvalidException("Home repayment period can only be max 35 years");
            }

            if (principal.multiply(new BigDecimal("0.2")).compareTo(downpayment) == 1) {
                throw new LoanTermInvalidException("Down Payment must be greater than 20% of your total asset value");
            }

            if (loanType.getName().equals("SIBOR Package")) {
                monthlyPayment = fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getSIBOR(), loanType.getSIBORrate1());
            } else if (loanType.getName().equals("Fixed Interest Package")) {
                monthlyPayment = fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getFixedRate(), temp);
            }
             if (homeType.equals("HDB flat")){
                TDSR=calculateTDSR(monthlyIncome,existingDebit);
                MSR=calculateMSR(monthlyIncome,existingDebit);
                if (monthlyIncome.compareTo(TDSR)==-1){
                    throw new LoanTermInvalidException("Does not fulfill TDSR requirement");
                }
                if (monthlyIncome.compareTo(MSR)==-1){
                    throw new LoanTermInvalidException("Does not fulfill MSR requirement");
                }
                
            }else if (homeType.equals("Executive Condominium")){
                TDSR=calculateTDSR(monthlyIncome,existingDebit);
                if (monthlyIncome.compareTo(TDSR)==-1){
                    throw new LoanTermInvalidException("Does not fulfill MSR requirement");
                }
            }
        } else if (loanType.getType().equals("Car")) {
            if (loanTerm > 84) {
                throw new LoanTermInvalidException("Car repayment period can only be max 7 years");
            }

            monthlyPayment = fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getInterestRate(), temp);
        
         BigDecimal temp2=new BigDecimal(20000);
            BigDecimal temp3=new BigDecimal(0.7);
            BigDecimal temp4=new BigDecimal(0.6);
            
            if (principal.compareTo(temp2)==-1){
                if ((principal.subtract(downpayment)).compareTo(principal.multiply(temp3))==1)
                        throw new LoanTermInvalidException("According to MAS,for cars with an open market value (OMV) of $20,000 or less, buyers can borrow up to 70 per cent of the purchase price");

            }else {
                if ((principal.subtract(downpayment)).compareTo(principal.multiply(temp4))==1)
                        throw new LoanTermInvalidException("According to MAS,for cars with an open market value (OMV) more than $20,000 , buyers can borrow up to 60 per cent of the purchase price");
            }
        } else if (loanType.getType().equals("Education")) {
            if (loanTerm > 96) {
                throw new LoanTermInvalidException("Education repayment period can only be max 8 years");
            }
            monthlyPayment = fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getEducationRate(), temp);

        }

        Loan loan = new Loan(accountNum, principal, downpayment, loanTerm, "inactive", monthlyPayment, 0, principal.subtract(downpayment), customer);
        em.persist(loan);
        em.flush();

        loan.setHomeType(homeType);
loan.setHomeAddress(homeAddress);
loan.setPostalCode(postalCode);
loan.setCarMode(carMode);
loan.setInstitution(institution);
loan.setMajor(major);
loan.setGraduationDate(graduationDate);
        loan.setLoanType(loanType);

        if (loanType.getName().equals("SIBOR Package")) {
            BigDecimal term = new BigDecimal(loanTerm);
            loan.setOutstandingBalance(monthlyPayment.multiply(term));
        } else if (loanType.getName().equals("Fixed Interest Package")) {
            BigDecimal monthly2 = fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getSIBOR(), loanType.getFixedRate2());
            BigDecimal temp2 = new BigDecimal(36);
            BigDecimal temp3 = new BigDecimal(loanTerm - 36);
            loan.setInterestRate1(loanType.getFixedRate());
            loan.setInterestRate2(loanType.getFixedRate2());
            loan.setOutstandingBalance(monthlyPayment.multiply(temp2).add(monthly2.multiply(temp3)));
        }else if (loanType.getType().equals("Car")) {
            BigDecimal term = new BigDecimal(loanTerm);
            loan.setInterestRate1(loanType.getInterestRate());
            loan.setOutstandingBalance(monthlyPayment.multiply(term));
        }else if (loanType.getType().equals("Education")) {
            BigDecimal term = new BigDecimal(loanTerm);
            loan.setInterestRate1(loanType.getEducationRate());
            loan.setOutstandingBalance(monthlyPayment.multiply(term));
        }

        Date currentTime = Calendar.getInstance().getTime();
        DateTime payDate = new DateTime(currentTime);
        DateTime currentTime1 = payDate.plusMonths(1);
        Date currentTimestamp = currentTime1.toDate();
        loan.setStartDate(currentTimestamp);

        List<Loan> loans = new ArrayList<Loan>();
        //customer may alr have fixed acct
        if (customer.getLoans() == null) {
            loans.add(loan);
            customer.setLoans(loans);
        } else {//alr have fixed acct
            customer.getLoans().add(loan);
        }

       
        customer.setMonthlyIncome(monthlyIncome);
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Create new Loan Account", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);

        return accountNum;
    }
    
     //Teller Create Loan Account - 3nd page - configure loan
    @Override
    public Long StaffCreateLoanAccountExisting(Long staffId,Long customerId, BigDecimal monthlyIncome, Long loanTypeId, BigDecimal principal, BigDecimal downpayment, Integer loanTerm,String homeType,String homeAddress,Long postCode,String carModel,BigDecimal existingDebit,Long postalCode,String carMode,String institution,String major,Date graduationDate) throws LoanTermInvalidException {
        Staff staff=em.find(Staff.class,staffId);
        Customer customer = em.find(Customer.class, customerId);
        Long accountNum=0L;
       
        try{
        accountNum=createLoanAccountExisting(customer.getId(),monthlyIncome,loanTypeId,principal,downpayment,loanTerm,homeType,homeAddress,postCode,carModel,existingDebit,postalCode,carMode,institution,major,graduationDate);
        }catch (LoanTermInvalidException|EmailNotSendException ex){
            throw new LoanTermInvalidException(ex.getMessage());
        }
        
        smsbl.recordStaffAction(staffId, "Create new loan account " + accountNum, customerId);
        return accountNum;
    }

}
