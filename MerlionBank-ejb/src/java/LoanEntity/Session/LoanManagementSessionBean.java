/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoanEntity.Session;

import CommonEntity.Customer;
import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.LoanTermInvalidException;
import Exception.UserNotActivatedException;
import Exception.UserNotExistException;
import LoanEntity.Instance;
import LoanEntity.InstanceValue;
import LoanEntity.Loan;
import LoanEntity.LoanType;
import LoanEntity.Logistic;
import Other.Session.sendEmail;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 *
 * @author a0113893
 */
@Stateless
public class LoanManagementSessionBean implements LoanManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private StaffManagementSessionBeanLocal smsbl;

    @EJB

    private LoanApplicationSessionBeanLocal lasb;

    @Override
    public List<Loan> staffViewPendingLoans() {
        Query query = em.createQuery("SELECT a FROM Loan a");
        List<Loan> loans = new ArrayList(query.getResultList());
        List<Loan> newLoans = new ArrayList<Loan>();
        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i).getStatus().equals("inactive")) {
                newLoans.add(loans.get(i));
            }
        }
        return newLoans;
    }

    @Override
    public List<Loan> staffRejectLoans(Long staffId, Long loanId) throws EmailNotSendException {
        Loan loan = em.find(Loan.class, loanId);
        Staff staff = em.find(Staff.class, staffId);
        loan.setStatus("terminated");
        try {
            sendRejectVerificationEmail(loan.getCustomer().getName(), loan.getCustomer().getEmail(), loan.getAccountNumber());
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
        List<Loan> loans = staffViewPendingLoans();
        smsbl.recordStaffAction(staffId, "Reject loan account " + loan.getAccountNumber(), loan.getCustomer().getId());
        return loans;
    }

    @Override
    public List<Loan> staffApproveLoans(Long staffId, Long loanId) throws EmailNotSendException {
        Loan loan = em.find(Loan.class, loanId);
        Staff staff = em.find(Staff.class, staffId);
        loan.setStatus("customerVerified");
        Date currentTime = Calendar.getInstance().getTime();
        DateTime payDate = new DateTime(currentTime);
        DateTime currentTime1 = payDate.plusMonths(1);
        Date currentTimestamp = currentTime1.toDate();

        loan.setStartDate(currentTimestamp);
        try {
            sendApproveEmail(loan.getCustomer().getName(), loan.getCustomer().getEmail(), loan.getAccountNumber());
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }

        smsbl.recordStaffAction(staffId, "Approve loan account " + loan.getAccountNumber(), loan.getCustomer().getId());

        List<Loan> loans = staffViewPendingLoans();
        return loans;
    }

    private void sendApproveEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Loan Application Approved";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Your loan has been approved by our bank staff.</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Loan Account Number: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to go to our counter to sign the loan contract. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

    private void sendRejectVerificationEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Loan Application Rejected";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Sorry! You application of Loan " + accountNumber + "has been rejected.</h1><br />"
                + "<p style=\"color: #ff0000;\">For more enquiries, please kindly contact +65 8888 8888. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please log in and use case management page and submit your enquiry there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

    @Override
    public Loan staffUpdateLoan(Long staffId, Long loanId, BigDecimal principal, BigDecimal downpayment, Integer loanTerm, Date startDate) throws EmailNotSendException {
        Loan loan = em.find(Loan.class, loanId);
        Staff staff = em.find(Staff.class, staffId);

        LoanType loanType = loan.getLoanType();

        BigDecimal monthlyPayment2 = new BigDecimal(0);
        Double temp = 0.0;
        if (loanType.getName().equals("SIBOR Package")) {
            System.out.println("principal "+principal);
            System.out.println("downpayment "+downpayment);
            System.out.println("loanTerm "+loanTerm);
             System.out.println("loan.getInterestRate1()"+loan.getInterestRate1());
            System.out.println("loan.getInterestRate2()"+loan.getInterestRate2());
            monthlyPayment2 = lasb.fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getSIBOR(), loanType.getSIBORrate1());
        } else if (loanType.getName().equals("Fixed Interest Package")) {
            monthlyPayment2 = lasb.fixedCalculator(principal.subtract(downpayment), loanTerm, loan.getInterestRate1(), temp);
        } else if (loanType.getType().equals("Car")) {
            monthlyPayment2 = lasb.fixedCalculator(principal.subtract(downpayment), loanTerm, loan.getInterestRate1(), temp);
        } else if (loanType.getType().equals("Education")) {  
            System.out.println("principal "+principal);
            System.out.println("downpayment "+downpayment);
            System.out.println("loanTerm "+loanTerm);
             System.out.println("loan.getEduInterestRate1()"+loan.getInterestRate1());
            monthlyPayment2 = lasb.fixedCalculator(principal.subtract(downpayment), loanTerm, loan.getInterestRate1(), temp);

        }

        loan.setMonthlyPayment(monthlyPayment2);
        loan.setPrincipal(principal);
        loan.setDownpayment(downpayment);
        loan.setLoanTerm(loanTerm);

        if (loanType.getName().equals("SIBOR Package")) {
            BigDecimal term = new BigDecimal(loanTerm);
            loan.setOutstandingBalance(monthlyPayment2.multiply(term));
        } else if (loanType.getName().equals("Fixed Interest Package")) {
            BigDecimal monthly2 = lasb.fixedCalculator(principal.subtract(downpayment), loanTerm, loanType.getSIBOR(), loan.getInterestRate2());
            BigDecimal temp2 = new BigDecimal(36);
            BigDecimal temp3 = new BigDecimal(loanTerm - 36);
            loan.setInterestRate1(loanType.getFixedRate());
            loan.setInterestRate2(loanType.getFixedRate2());
            loan.setOutstandingBalance(monthlyPayment2.multiply(temp2).add(monthly2.multiply(temp3)));
        } else if (loanType.getType().equals("Car")) {
            BigDecimal term = new BigDecimal(loanTerm);
            loan.setInterestRate1(loanType.getInterestRate());
            loan.setOutstandingBalance(monthlyPayment2.multiply(term));
        } else if (loanType.getType().equals("Education")) {
            BigDecimal term = new BigDecimal(loanTerm);
            loan.setInterestRate1(loanType.getEducationRate());
            loan.setOutstandingBalance(monthlyPayment2.multiply(term));
        }

        loan.setStatus("staffVerified");

        Date currentTime = Calendar.getInstance().getTime();
        DateTime payDate = new DateTime(currentTime);
        DateTime currentTime1 = payDate.plusMonths(1);
        Date currentTimestamp = currentTime1.toDate();

        loan.setStartDate(currentTimestamp);

        try {
            sendMofifiedEmail(loan.getCustomer().getName(), loan.getCustomer().getEmail(), loan.getAccountNumber());
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }

        //List<Loan> loans = staffViewPendingLoans();
        smsbl.recordStaffAction(staffId, "Update loan account " + loan.getAccountNumber(), loan.getCustomer().getId());
        return loan;
    }

    private void sendMofifiedEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Your loan has been modified- Pending verification";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Cour loan has been modified by our bank staff.</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Loan Account Number: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to go to our counter to sign the loan contract. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

    @Override
    public Loan staffActivateLoan(Long loanId, Date loanDate) {
        Loan loan = em.find(Loan.class, loanId);
        loan.setStatus("active");
        loan.setStartDate(null);
        loan.setLoanDate(loanDate);
        BigDecimal temp=new BigDecimal(0);
        loan.setLatePayment(temp);
        loan.setLoanDate(Calendar.getInstance().getTime());
        em.flush();
        return loan;

    }

    @Override
    public List<Loan> searchLoan(String customerIc) throws UserNotExistException, UserNotActivatedException, ListEmptyException {
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
        List<Loan> loans = customer.getLoans();
        if (loans.isEmpty()) {
            throw new ListEmptyException("There are no loans under this customer");
        }

        return loans;

    }

    @Override
    public List<LoanType> viewLoanTypeList() {
        Query query = em.createQuery("SELECT a FROM LoanType a");
        List<LoanType> loanTypes = new ArrayList(query.getResultList());
        return loanTypes;
    }

    @Override
    public List<LoanType> updateLoanType(Long loanTypeId, Double interest1, Double interest2) {
        
        LoanType loanType = em.find(LoanType.class, loanTypeId);
        Query query = em.createQuery("SELECT a FROM Loan a");
        List<Loan> currentLoans = new ArrayList(query.getResultList());
        List<Loan> loans = new ArrayList<Loan>();

        for (int i = 0; i < currentLoans.size(); i++) {
            if (currentLoans.get(i).getStatus().equals("active")) {
                loans.add(currentLoans.get(i));
            }
        }

        if (loanType.getName().equals("SIBOR Package")) {
            loanType.setSIBOR(interest1);
            loanType.setSIBORrate1(interest2);
            for (int j = 0; j < loans.size(); j++) {
                if (loans.get(j).getLoanType().getName().equals("SIBOR Package")) {
                    loans.get(j).setInterestRate1(interest1);
                    loans.get(j).setInterestRate2(interest2);
                    em.flush();
                }
            }
        } else if (loanType.getName().equals("Fixed Interest Package")) {
            loanType.setFixedRate(interest1);
            loanType.setFixedRate2(interest2);
            for (int m = 0; m < loans.size(); m++) {
                if (loans.get(m).getLoanType().getName().equals("Fixed Interest Package")) {
                    loans.get(m).setInterestRate1(interest1);
                    loans.get(m).setInterestRate2(interest2);
                    em.flush();
                }
            }
        } else if (loanType.getName().equals("Car Loan")) {
            System.out.println("update car loan with interest"+interest1);
            loanType.setInterestRate(interest1);
        } else if (loanType.getName().equals("NUS Education Loan")) {
            loanType.setEducationRate(interest1);
        }
        
        Query q = em.createQuery("SELECT a FROM LoanType a");
        List<LoanType> loanTypes = new ArrayList(q.getResultList());
        return loanTypes;

    }

    private static double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }
    
    
    private void train(List<Instance> instances, Logistic logistic) {
        double[] weights = logistic.getWeights();

        for (int n = 0; n < 3000; n++) {
            double lik = 0.0;
            for (int i = 0; i < instances.size(); i++) {
                List <InstanceValue> trainingData=instances.get(i).getInstanceValues();
                int first=trainingData.get(0).getTrainVariable().intValue();
                 int second=trainingData.get(1).getTrainVariable().intValue();
                 int third=trainingData.get(2).getTrainVariable().intValue();
                int[] x={first,second,third};
               
                double predicted = classify(x, logistic);
                int label = instances.get(i).getLabel();
                for (int j = 0; j < 3; j++) {
                    weights[j] = weights[j] + logistic.getRate() * (label - predicted) * x[j];
                }
                logistic.setWeights(weights);
                // not necessary for learning
                lik = lik + (label * Math.log(classify(x, logistic)) + (1 - label) * Math.log(1 - classify(x, logistic)));
            }
            //System.out.println("iteration: " + n + " " + Arrays.toString(weights) + " mle: " + lik);
        }
    }
    

    private double classify(int[] x, Logistic logistic) {
        double logit = 0.0;
        double[] weights = logistic.getWeights();

        for (int i = 0; i < weights.length; i++) {
            logit += weights[i] * x[i];
        }
        return sigmoid(logit);
    }

    @Override
    public double calculateRisk(Long customerId, Long loanId) {
        Customer customer = em.find(Customer.class, customerId);
        Loan loan = em.find(Loan.class, loanId);
        Query query = em.createQuery("SELECT a FROM Instance a");
        List<Instance> instances = new ArrayList(query.getResultList());
        Logistic logistic = new Logistic(3);
        train(instances, logistic);
        int income=customer.getMonthlyIncome().intValueExact()-loan.getMonthlyPayment().intValueExact();
        
        int gender=0;
        if (customer.getGender().equals("Female"))
            gender=0;
        if (customer.getGender().equals("Male"))
            gender=1;
        
        int status=0;
        if(customer.getFamilyInfo().equals("Single"))
            status=0;
        if(customer.getFamilyInfo().equals("Married"))
            status=1;
        if(customer.getFamilyInfo().equals("Divorced"))
            status=2;
        
        int[] x = {income,gender,status};
        System.out.println("prob(1|x) = " + classify(x, logistic));

        return classify(x, logistic);

    }

}
