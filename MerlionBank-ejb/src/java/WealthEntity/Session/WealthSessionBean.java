/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import CommonEntity.Customer;
import CommonEntity.CustomerAction;
import CommonEntity.Session.StaffManagementSessionBeanLocal;
import DepositEntity.SavingAccount;
import DepositEntity.TransactionRecord;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.NotEnoughAmountException;
import LoanEntity.Loan;
import Other.Session.sendEmail;
import WealthEntity.DiscretionaryAccount;
import WealthEntity.Portfolio;
import WealthEntity.PortfolioTransaction;
import WealthEntity.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.joda.time.DateTime;

/**
 *
 * @author a0113893
 */
@Stateless
public class WealthSessionBean implements WealthSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    StaffManagementSessionBeanLocal smsbl;

    @Override
    public Long existingCustomerActivateAccount(Long customerId, Long accountId) throws NotEnoughAmountException {
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, accountId);
        Customer customer = em.find(Customer.class, customerId);
        BigDecimal amount = discretionaryAccount.getBalance();
        BigDecimal currentAmount = new BigDecimal(200000);
        int res = amount.compareTo(currentAmount);
        if (res == 0 || res == 1) {
            discretionaryAccount.setStatus("active");
            return accountId;
        } else {
            throw new NotEnoughAmountException("This account has not met the minimum SG$200000 requirement");
        }
    }

    @Override
    public List<DiscretionaryAccount> displayAvailableDiscretionaryAccounts(Long customerId) throws ListEmptyException {
        Customer customer = em.find(Customer.class, customerId);
        List<DiscretionaryAccount> discretionaryAccounts = customer.getDiscretionaryAccounts();
        List<DiscretionaryAccount> temp = new ArrayList<DiscretionaryAccount>();

        for (int i = 0; i < discretionaryAccounts.size(); i++) {
            if (!discretionaryAccounts.get(i).getStatus().equals("terminated")) {
                temp.add(discretionaryAccounts.get(i));
            }
        }

        if (temp.isEmpty()) {
            throw new ListEmptyException("You do not have any available discretionary accounts.");
        }
        return temp;
    }

    @Override
    public List<SavingAccount> displaySavingAccounts(Long customerId) throws ListEmptyException {
        Customer customer = em.find(Customer.class, customerId);
        List<SavingAccount> currentSavingAccounts = customer.getSavingAccounts();
        List<SavingAccount> savingAccounts = new ArrayList<SavingAccount>();

        for (int i = 0; i < currentSavingAccounts.size(); i++) {
            if (currentSavingAccounts.get(i).getStatus().equals("active")) {
                savingAccounts.add(currentSavingAccounts.get(i));
            }
        }
        if (savingAccounts.isEmpty()) {
            throw new ListEmptyException("You do not have any active saving accounts.");
        }
        return savingAccounts;
    }

    @Override
    public Long topUpBySaving(Long customerId, Long savingAccountId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException {
        Customer customer = em.find(Customer.class, customerId);
        SavingAccount savingAccount = em.find(SavingAccount.class, savingAccountId);
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);

        if (amount.compareTo(savingAccount.getAvailableBalance()) == 1) {
            throw new NotEnoughAmountException("There is not enough amount of money in this savingAccount");
        }

        discretionaryAccount.setBalance(discretionaryAccount.getBalance().add(amount));
        discretionaryAccount.getTotalBalance().add(amount);

        savingAccount.setAvailableBalance(savingAccount.getAvailableBalance().subtract(amount));
        savingAccount.setBalance(savingAccount.getBalance().subtract(amount));

        Date currentTime = Calendar.getInstance().getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

        TransactionRecord transactionRecord = new TransactionRecord("WT", amount, null, "settled", "Discretionary Account Top up", currentTimestamp, savingAccount.getAccountNumber(), discretionaryAccount.getAccountNumber(), savingAccount, "MerlionBank", "MerlionBank");
        savingAccount.getTransactionRecord().add(transactionRecord);
        em.persist(transactionRecord);
        em.flush();

        return discretionaryAccountId;
    }

    @Override
    public Boolean compareAmount(Long customerId, long discretionaryAccountId, BigDecimal amount) {
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);
        BigDecimal totalBalance = discretionaryAccount.getTotalBalance();
        BigDecimal temp = new BigDecimal(200000);
        if ((totalBalance.subtract(amount)).compareTo(temp) == -1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Long transferBackToSavingWithEnoughBalance(Long customerId, Long savingAccountId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException {
        Customer customer = em.find(Customer.class, customerId);
        SavingAccount savingAccount = em.find(SavingAccount.class, savingAccountId);
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);

        if (amount.compareTo(discretionaryAccount.getBalance()) == 1) {
            throw new NotEnoughAmountException("There is not enough amount of money in this Discretionary Account");
        }

        savingAccount.setBalance(savingAccount.getBalance().add(amount));
        savingAccount.setBalance(savingAccount.getAvailableBalance().add(amount));

        discretionaryAccount.setBalance(discretionaryAccount.getBalance().subtract(amount));
        discretionaryAccount.setTotalBalance(discretionaryAccount.getTotalBalance().subtract(amount));

        Date currentTime = Calendar.getInstance().getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

        TransactionRecord transactionRecord = new TransactionRecord("WT", amount, null, "settled", "Discretionary Account Transfer Back", currentTimestamp, discretionaryAccount.getAccountNumber(), savingAccount.getAccountNumber(), savingAccount, "MerlionBank", "MerlionBank");
        savingAccount.getTransactionRecord().add(transactionRecord);
        em.persist(transactionRecord);
        em.flush();

        return discretionaryAccountId;
    }

    @Override
    public Long transferBackToSavingWithNotEnoughBalance(Long customerId, Long savingAccountId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException {
        Customer customer = em.find(Customer.class, customerId);
        SavingAccount savingAccount = em.find(SavingAccount.class, savingAccountId);
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);

        if (amount.compareTo(discretionaryAccount.getBalance()) == 1) {
            throw new NotEnoughAmountException("There is not enough amount of money in this Discretionary Account");
        }
        BigDecimal cutline = new BigDecimal(200000);
        BigDecimal processingFee = new BigDecimal(1.15);

        savingAccount.setBalance(savingAccount.getBalance().add(amount));
        savingAccount.setBalance(savingAccount.getAvailableBalance().add(amount));

        if (discretionaryAccount.getTotalBalance().compareTo(cutline) == 1 || discretionaryAccount.getTotalBalance().compareTo(cutline) == 0) {
            BigDecimal totalBalance = discretionaryAccount.getTotalBalance().subtract(amount);
            BigDecimal tier1 = cutline.subtract(totalBalance);
            amount = amount.subtract(tier1);
            amount = amount.add(tier1.multiply(processingFee));
        } else {
            amount = amount.multiply(processingFee);
        }

        discretionaryAccount.setBalance(discretionaryAccount.getBalance().subtract(amount));
        discretionaryAccount.setTotalBalance(discretionaryAccount.getTotalBalance().subtract(amount));

        Date currentTime = Calendar.getInstance().getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(currentTime.getTime());

        TransactionRecord transactionRecord = new TransactionRecord("WT", amount, null, "settled", "Discretionary Account Transfer Back", currentTimestamp, discretionaryAccount.getAccountNumber(), savingAccount.getAccountNumber(), savingAccount, "MerlionBank", "MerlionBank");
        savingAccount.getTransactionRecord().add(transactionRecord);
        em.persist(transactionRecord);
        em.flush();

        return discretionaryAccountId;
    }

    @Override
    public List<DiscretionaryAccount> displayAllDiscretionaryAccounts(Long customerId) throws ListEmptyException {
        Customer customer = em.find(Customer.class, customerId);
        List<DiscretionaryAccount> discretionaryAccounts = customer.getDiscretionaryAccounts();

        if (discretionaryAccounts.isEmpty()) {
            throw new ListEmptyException("You do not have any available discretionary accounts.");
        }
        return discretionaryAccounts;
    }

    @Override
    public Long createPrefefinedPlan(Long customerId, Long accountId, BigDecimal initialAmount, String type, int term) throws NotEnoughAmountException {
        Customer customer = em.find(Customer.class, customerId);
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, accountId);
        BigDecimal minAmount = new BigDecimal(250000);
        if (initialAmount.compareTo(minAmount) == -1) {
            throw new NotEnoughAmountException("Minimum amount for " + type + " is SGD$250000");
        }
        if (initialAmount.compareTo(discretionaryAccount.getBalance()) == 1) {
            throw new NotEnoughAmountException("There is not enough money in the selected discretionary account. Please top up or choose another discretionary account.");
        }
        List<PortfolioTransaction> portfolioTransactions = new ArrayList<PortfolioTransaction>();
        List<Product> products = new ArrayList<Product>();

        Date currentTime = Calendar.getInstance().getTime();
        DateTime start = new DateTime(currentTime);
        DateTime currentTime1 = start.plusMonths(term);
        Date currentTimestamp = currentTime1.toDate();

        Portfolio portfolio = new Portfolio(type, initialAmount, initialAmount, 3.25, 3.25, Calendar.getInstance().getTime(), currentTimestamp, discretionaryAccount, portfolioTransactions, products, "active");
        em.persist(portfolio);
        em.flush();

        portfolio.setTerm(term);

        List<Portfolio> portfolios = new ArrayList<Portfolio>();
        if (discretionaryAccount.getPortfolios() == null) {
            portfolios.add(portfolio);
            discretionaryAccount.setPortfolios(portfolios);
        } else {//alr have fixed acct
            discretionaryAccount.getPortfolios().add(portfolio);
        }

        discretionaryAccount.setBalance(discretionaryAccount.getBalance().subtract(initialAmount));

        //log an action
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Buy a new wealth plan", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();
        return portfolio.getId();
    }

    @Override
    public Long staffCreatePrefefinedPlan(Long staffId, Long customerId, Long accountId, BigDecimal initialAmount, String type, int term) throws NotEnoughAmountException {
        Long accountNum = 0L;
        try {
            accountNum = createPrefefinedPlan(customerId, accountId, initialAmount, type, term);
        } catch (NotEnoughAmountException ex) {
            throw new NotEnoughAmountException(ex.getMessage());
        }
        smsbl.recordStaffAction(staffId, "apply a new plan" + accountNum, customerId);
        return accountNum;
    }

    @Override
    public Long createTailoredPortfolio(Long customerId, Long discretionaryAccountId, BigDecimal investAmount, Double expectedRateOfReturn, Double foreignExchange, Double equity, Double bond, int term) throws NotEnoughAmountException {
        Customer customer = em.find(Customer.class, customerId);
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);
        BigDecimal minAmount = new BigDecimal(3250000);
        if (investAmount.compareTo(minAmount) == -1) {
            throw new NotEnoughAmountException("Minimum amount for tailored plan is SGD$3,250,000");
        }
        if (investAmount.compareTo(discretionaryAccount.getBalance()) == 1) {
            throw new NotEnoughAmountException("There is not enough money in the selected discretionary account. Please top up or choose another discretionary account.");
        }

        List<PortfolioTransaction> portfolioTransactions = new ArrayList<PortfolioTransaction>();
        List<Product> products = new ArrayList<Product>();

        BigDecimal rate = new BigDecimal(foreignExchange);
        BigDecimal purchaseAmount = investAmount.multiply(rate);
        Product foreignExchangeProduct = new Product("Foreign Exchange", purchaseAmount, foreignExchange);
        em.persist(foreignExchangeProduct);

        rate = new BigDecimal(equity);
        purchaseAmount = investAmount.multiply(rate);
        Product equityProduct = new Product("Equity", purchaseAmount, equity);
        em.persist(equityProduct);

        rate = new BigDecimal(bond);
        purchaseAmount = investAmount.multiply(rate);
        Product stockProduct = new Product("Bond", purchaseAmount, bond);
        em.persist(equityProduct);

        products.add(foreignExchangeProduct);
        products.add(equityProduct);
        products.add(stockProduct);

        BigDecimal temp = new BigDecimal(0);
        Double temp2 = 0.0;
        Date currentTime = Calendar.getInstance().getTime();
        DateTime start = new DateTime(currentTime);
        DateTime currentTime1 = start.plusMonths(term);
        Date currentTimestamp = currentTime1.toDate();

        Portfolio portfolio = new Portfolio("Tailored plan", investAmount, investAmount, 0.0, expectedRateOfReturn, currentTime, currentTimestamp, discretionaryAccount, portfolioTransactions, products, "inactive");
        em.persist(portfolio);
        em.flush();

        portfolio.setTerm(term);

        foreignExchangeProduct.setPortfolio(portfolio);
        equityProduct.setPortfolio(portfolio);
        stockProduct.setPortfolio(portfolio);

        List<Portfolio> portfolios = new ArrayList<Portfolio>();
        if (discretionaryAccount.getPortfolios() == null) {
            portfolios.add(portfolio);
            discretionaryAccount.setPortfolios(portfolios);
        } else {//alr have fixed acct
            discretionaryAccount.getPortfolios().add(portfolio);
        }

        discretionaryAccount.setBalance(discretionaryAccount.getBalance().subtract(investAmount));

        //log an action
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Buy a new wealth plan", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();
        return portfolio.getId();
    }

    @Override
    public List<Portfolio> displayAllPortfolios(Long discretionaryAccountId) {
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);
        return discretionaryAccount.getPortfolios();
    }

    @Override
    public List<Portfolio> customerCancelPortfolios(Long portfolioId) {
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        portfolio.setStatus("terminated");
        return portfolio.getDiscretionaryAccount().getPortfolios();
    }

    @Override
    public List<Portfolio> customerAcceptPlan(Long customerId, Long portfolioId) throws EmailNotSendException {
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        Customer customer = em.find(Customer.class, customerId);
        portfolio.setStatus("customerVerified");

//      Date currentTime = Calendar.getInstance().getTime();
//           DateTime payDate = new DateTime(currentTime);
//           DateTime currentTime1 = payDate.plusMonths(1);
//           Date currentTimestamp=currentTime1.toDate();
//           
//           loan.setStartDate(currentTimestamp);
        try {
            sendApproveEmail(customer.getName(), customer.getEmail(), portfolio.getId());
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }

        //log an action
        CustomerAction action = new CustomerAction(Calendar.getInstance().getTime(), "Accept staff modified Wealth Tailored Plan", customer);
        em.persist(action);
        List<CustomerAction> customerActions = customer.getCustomerActions();
        customerActions.add(action);
        customer.setCustomerActions(customerActions);
        em.persist(customer);
        em.flush();
        return portfolio.getDiscretionaryAccount().getPortfolios();
    }

    private void sendApproveEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Loan Application Approved";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  You have successfully approve your portfolio plan.</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Portfolio Id: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to go to our counter to sign the wealth plan contract. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

    @Override
    public List<Portfolio> ModifyPortfolios(Long portfolioId, Double expectedRateOfReturn, Double foreignExchange, Double equity, Double bond, int term) throws EmailNotSendException {
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        BigDecimal investAmount = portfolio.getInvestAmount();
        DiscretionaryAccount discretionaryAccount = portfolio.getDiscretionaryAccount();

        List<Product> products = new ArrayList<Product>();

        BigDecimal rate = new BigDecimal(foreignExchange);
        BigDecimal purchaseAmount = investAmount.multiply(rate);
        Product foreignExchangeProduct = new Product("Foreign Exchange", purchaseAmount, foreignExchange);
        em.persist(foreignExchangeProduct);

        rate = new BigDecimal(equity);
        purchaseAmount = investAmount.multiply(rate);
        Product equityProduct = new Product("Equity", purchaseAmount, equity);
        em.persist(equityProduct);

        rate = new BigDecimal(bond);
        purchaseAmount = investAmount.multiply(rate);
        Product stockProduct = new Product("Bond", purchaseAmount, bond);
        em.persist(equityProduct);

        products.add(foreignExchangeProduct);
        products.add(equityProduct);
        products.add(stockProduct);

        portfolio.setProducts(products);

        portfolio.setTerm(term);

        Date currentTime = portfolio.getStartDate();
        DateTime start = new DateTime(currentTime);
        DateTime currentTime1 = start.plusMonths(term);
        Date currentTimestamp = currentTime1.toDate();

        portfolio.setEndDate(currentTimestamp);

        portfolio.setStatus("inactive");

        List<Portfolio> portfolios = portfolio.getDiscretionaryAccount().getPortfolios();

        return portfolios;
    }

    @Override
    public List<Portfolio> portfolioEarlyWithdraw(Long portfolioId) {
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        BigDecimal presentValue = portfolio.getPresentValue();
        BigDecimal temp = new BigDecimal(0.85);
        presentValue = presentValue.multiply(temp);
        portfolio.setStatus("Early Withdraw");

        DiscretionaryAccount discretionaryAccount = portfolio.getDiscretionaryAccount();
        discretionaryAccount.setBalance(discretionaryAccount.getBalance().add(presentValue));
        em.flush();
        return discretionaryAccount.getPortfolios();
    }

    @Override
    public Long payCommissionFee(Long customerId, Long discretionaryAccountId) throws NotEnoughAmountException {
        Customer customer = em.find(Customer.class, customerId);
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);
        BigDecimal amount = discretionaryAccount.getCommission();

        if (amount.compareTo(discretionaryAccount.getBalance()) == 1) {
            throw new NotEnoughAmountException("There is not enough amount of money in this Discretionary Account");
        }

        discretionaryAccount.setBalance(discretionaryAccount.getBalance().subtract(amount));
        discretionaryAccount.setTotalBalance(discretionaryAccount.getTotalBalance().subtract(amount));

        BigDecimal temp = new BigDecimal(0);
        discretionaryAccount.setCommission(temp);
        return discretionaryAccountId;
    }

}
