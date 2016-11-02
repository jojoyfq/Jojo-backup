/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import CommonEntity.Customer;
import CommonEntity.Session.StaffManagementSessionBeanLocal;
import CommonEntity.Staff;
import CommonEntity.StaffRole;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.NotEnoughAmountException;
import LoanEntity.Loan;
import Other.Session.sendEmail;
import WealthEntity.DiscretionaryAccount;
import WealthEntity.Good;
import WealthEntity.Portfolio;
import WealthEntity.PortfolioTransaction;
import WealthEntity.Product;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
public class WealthManagementSessionBean implements WealthManagementSessionBeanLocal {
 @PersistenceContext
    private EntityManager em;
 @EJB
    StaffManagementSessionBeanLocal smsbl;
 
 @Override
 public Long topUpAccount(Long staffId,Long accountId,BigDecimal amount){
     DiscretionaryAccount discretionaryAccount=em.find(DiscretionaryAccount.class,accountId);
     discretionaryAccount.getBalance().add(amount);
     discretionaryAccount.getTotalBalance().add(amount);
     smsbl.recordStaffAction(staffId, "create existing customer discretionary account", discretionaryAccount.getCustomer().getId());
 return accountId;
 }
 
 @Override
 public List<Portfolio> viewAllPendingApproveTailoredPlan(){
    Query query = em.createQuery("SELECT a FROM Portfolio a");
        List<Portfolio> portfolios = new ArrayList(query.getResultList());
        List<Portfolio> pendingPortfolios = new ArrayList<Portfolio>();
        for (int i = 0; i < portfolios.size(); i++) {
            if (portfolios.get(i).getStatus().equals("inactive") && portfolios.get(i).getType().equals("Tailored plan") ) {
                pendingPortfolios.add(portfolios.get(i));
            }
        }
        return pendingPortfolios; 
 }
 
  @Override
    public List<Portfolio> staffRejectPortfolios(Long staffId, Long portfolioId) throws EmailNotSendException {
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        Staff staff = em.find(Staff.class, staffId);
        
        portfolio.setStatus("terminated");
        DiscretionaryAccount discretionaryAccount=portfolio.getDiscretionaryAccount();
        
        discretionaryAccount.setBalance(discretionaryAccount.getBalance().add(portfolio.getInvestAmount()));
        em.flush();
        
        try {
            sendRejectVerificationEmail(discretionaryAccount.getCustomer().getName(), discretionaryAccount.getCustomer().getEmail(), portfolio.getId());
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }
        List<Portfolio> portfolios = viewAllPendingApproveTailoredPlan();
        smsbl.recordStaffAction(staffId, "Reject portfolio account " + portfolio.getId(), discretionaryAccount.getCustomer().getId());
        return portfolios;
    }

    @Override
    public List<Portfolio> staffApprovePortfolios(Long staffId, Long portfolioId) throws EmailNotSendException {
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        Staff staff = em.find(Staff.class, staffId);
        
        portfolio.setStatus("customerVerified");
         DiscretionaryAccount discretionaryAccount=portfolio.getDiscretionaryAccount();
        
//        Date currentTime = Calendar.getInstance().getTime();
//        DateTime payDate = new DateTime(currentTime);
//        DateTime currentTime1 = payDate.plusMonths(1);
//        Date currentTimestamp = currentTime1.toDate();

        try {
            sendApproveEmail(discretionaryAccount.getCustomer().getName(), discretionaryAccount.getCustomer().getEmail(), portfolio.getId());
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }

        smsbl.recordStaffAction(staffId, "Approve portfolio account " + portfolio.getId(), discretionaryAccount.getCustomer().getId());

        List<Portfolio> portfolios = viewAllPendingApproveTailoredPlan();
        return portfolios;
    }
    
    @Override
public List<Staff> retrieveStaffsAccordingToRole(String roleName)throws ListEmptyException{
     Query q = em.createQuery("SELECT a FROM StaffRole a WHERE a.roleName = :roleName");
        q.setParameter("roleName", roleName);
        StaffRole staffRole=(StaffRole)q.getSingleResult();
        List<Staff> staffList = staffRole.getStaffList();
        
        if (staffList.isEmpty()){
            throw new ListEmptyException("There are no available staff. Please choose another department.");
         }
        
        List<Staff> newStaffList=new ArrayList<Staff>();
        for (int i=0;i<staffList.size();i++){
            if (!staffList.get(i).getStatus().equals("terminated") || !staffList.get(i).getStatus().equals("inactive"))
                newStaffList.add(staffList.get(i));
        }
       return staffList; 
    
    
}
    @Override
    public Long assignRM(Long portfolioId,Long staffId){
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        Staff staff=em.find(Staff.class,staffId);
        System.out.println("Inside Session Bean staff Id: "+staffId);
        portfolio.setStaff(staff);
        List<Portfolio> portfolios=staff.getPortfolios();
        portfolios.add(portfolio);
        staff.setPortfolios(portfolios);
        return portfolio.getId();
        
    }

    private void sendApproveEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Loan Application Approved";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Your loan has been approved by our bank staff.</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Portfolio Id: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to go to our counter to sign the portfolio contract.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

    private void sendRejectVerificationEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Loan Application Rejected";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Sorry! You application of Portfolio " + accountNumber + "has been rejected.</h1><br />"
                + "<p style=\"color: #ff0000;\">For more enquiries, please kindly contact +65 8888 8888. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please log in and use case management page and submit your enquiry there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }

    @Override
    public Portfolio displayPortfolio(Long portfolioId){
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        return portfolio;
    }
    
     @Override
    public List<Portfolio> staffModifyPortfolios(Long staffId, Long portfolioId, Double expectedRateOfReturn,Double foreignExchange,Double equity,Double bond,int term) throws EmailNotSendException{
Portfolio portfolio = em.find(Portfolio.class, portfolioId);
BigDecimal investAmount=portfolio.getInvestAmount();
DiscretionaryAccount discretionaryAccount=portfolio.getDiscretionaryAccount();

    List<Product> products=new ArrayList<Product>();
    List<Good> goods=new ArrayList<Good>();
        
        BigDecimal rate=new BigDecimal(foreignExchange);
        BigDecimal purchaseAmount=investAmount.multiply(rate);
        BigDecimal test=new BigDecimal(0);
        Product foreignExchangeProduct=new Product("Foreign Exchange", purchaseAmount, foreignExchange,goods,purchaseAmount,test);
        em.persist(foreignExchangeProduct);
        
        rate=new BigDecimal(equity);
        purchaseAmount=investAmount.multiply(rate);
        Product equityProduct=new Product("Equity", purchaseAmount, equity,goods,purchaseAmount,test);
        em.persist(equityProduct);
        
        rate=new BigDecimal(bond);
        purchaseAmount=investAmount.multiply(rate);
        Product stockProduct=new Product("Bond", purchaseAmount, bond,goods,purchaseAmount,test);
        em.persist(equityProduct);
        
         products.add(foreignExchangeProduct);
        products.add(equityProduct);
        products.add(stockProduct);
        
        portfolio.setProducts(products);
        portfolio.setStatus("staffVefified");
        
        try {
            sendMofifiedEmail(discretionaryAccount.getCustomer().getName(), discretionaryAccount.getCustomer().getEmail(), portfolio.getId());
        } catch (MessagingException ex) {
            System.out.println("Error sending email.");
            throw new EmailNotSendException("Error sending email.");
        }

        //List<Loan> loans = staffViewPendingLoans();
        smsbl.recordStaffAction(staffId, "Update portfolio id " + portfolio.getId(), discretionaryAccount.getCustomer().getId());
    
        List<Portfolio> portfolios = viewAllPendingApproveTailoredPlan();
        return portfolios;
}
    
    private void sendMofifiedEmail(String name, String email, Long accountNumber) throws MessagingException {
        String subject = "Merlion Bank - Your wealth plan has been modified- Pending verification";
        System.out.println("Inside send email");

        String content = "<h2>Dear " + name
                + ",</h2><br /><h1>  Cour loan has been modified by our bank staff.</h1><br />"
                + "<h1>Welcome to Merlion Bank.</h1>"
                + "<h2 align=\"center\">Portfolio Id: " + accountNumber
                + "<p style=\"color: #ff0000;\">Please noted that that you are required to log in to view your newly edited wealth plan. Thank you.</p>"
                + "<br /><p>Note: Please do not reply this email. If you have further questions, please go to the contact form page and submit there.</p>"
                + "<p>Thank you.</p><br /><br /><p>Regards,</p><p>Merlion Bank User Support</p>";
        System.out.println(content);
        sendEmail.run(email, subject, content);
    }
    
   @Override 
   public List<Portfolio> viewAllPendingAcivationTailoredPlan(Long customerId){
    Customer customer=em.find(Customer.class,customerId);
    List<DiscretionaryAccount> discretionaryAccounts=customer.getDiscretionaryAccounts();
    List<Portfolio> portfolios=new ArrayList<Portfolio>();
    for (int i=0;i<discretionaryAccounts.size();i++){
        DiscretionaryAccount discretionaryAccount=discretionaryAccounts.get(i);
        List<Portfolio> temp=discretionaryAccount.getPortfolios();
        for (int j=0;j<temp.size();j++){
            portfolios.add(temp.get(j));
        }
    }
    return portfolios;
   }
    
    @Override
    public Portfolio staffActivateLoan(Long staffId, Long portfolioId, Date startDate) {
       
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        portfolio.setStatus("active");
        Date currentTime = startDate;
        DateTime start = new DateTime(currentTime);
        DateTime currentTime1 = start.plusMonths(portfolio.getTerm());
        Date currentTimestamp = currentTime1.toDate();
        
        portfolio.setStartDate(startDate);
        portfolio.setEndDate(currentTimestamp);
      
        em.flush();
         smsbl.recordStaffAction(staffId, "Activate portfolio id " + portfolio.getId(), portfolio.getDiscretionaryAccount().getCustomer().getId());
        return portfolio;

    }

 @Override 
    public Boolean compareAmount(long discretionaryAccountId, BigDecimal amount){
       DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);
      BigDecimal totalBalance=discretionaryAccount.getTotalBalance();
      BigDecimal temp = new BigDecimal(200000);
      if ((totalBalance.subtract(amount)).compareTo(temp)==-1)
          return false;
                  else 
          return true;
    }
    
    @Override
    public Long discreationaryAccountMoneyWithdrawWithEnoughBalance(Long staffId, Long customerId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException {
        Customer customer = em.find(Customer.class, customerId);
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);

        if (amount.compareTo(discretionaryAccount.getBalance()) == 1) {
            throw new NotEnoughAmountException("There is not enough amount of money in this Discretionary Account");
        }

        discretionaryAccount.setBalance(discretionaryAccount.getBalance().subtract(amount));
       discretionaryAccount.setTotalBalance(discretionaryAccount.getTotalBalance().subtract(amount));

        smsbl.recordStaffAction(staffId, "Perform cash withdraw SGD$ "+amount+" for Discretionary Account Id " + discretionaryAccount.getId(), discretionaryAccount.getCustomer().getId());
        return discretionaryAccountId;
    }
    
     @Override
    public Long transferBackToSavingWithNotEnoughBalance(Long staffId, Long customerId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException {
        
        DiscretionaryAccount discretionaryAccount = em.find(DiscretionaryAccount.class, discretionaryAccountId);

        if (amount.compareTo(discretionaryAccount.getBalance()) == 1) {
            throw new NotEnoughAmountException("There is not enough amount of money in this Discretionary Account");
        }
        BigDecimal cutline=new BigDecimal(200000);
        BigDecimal processingFee=new BigDecimal(1.15);
        
      
        
        if (discretionaryAccount.getTotalBalance().compareTo(cutline)==1 ||discretionaryAccount.getTotalBalance().compareTo(cutline)==0 ){       
        BigDecimal totalBalance=discretionaryAccount.getTotalBalance().subtract(amount);
        BigDecimal tier1=cutline.subtract(totalBalance);
        amount=amount.subtract(tier1);
        amount=amount.add(tier1.multiply(processingFee));
    }else{
        amount=amount.multiply(processingFee);    
        }

       

        discretionaryAccount.setBalance(discretionaryAccount.getBalance().subtract(amount));
       discretionaryAccount.setTotalBalance(discretionaryAccount.getTotalBalance().subtract(amount));

       smsbl.recordStaffAction(staffId, "Perform cash withdraw SGD$ "+amount+" for Discretionary Account Id " + discretionaryAccount.getId(), discretionaryAccount.getCustomer().getId());
        return discretionaryAccountId;
    }
    
    @Override
    public List<Portfolio> displayPortfoliosUnderStaff(Long staffId){
        Staff staff = em.find(Staff.class, staffId);
        return staff.getPortfolios();
    }
    
    @Override
    public List<Product> displayProduct(Long portfolioId){
              Portfolio portfolio = em.find(Portfolio.class, portfolioId);
              return portfolio.getProducts();
    }
    @Override
    public List<Good>displayGood(Long productId){
        Product product=em.find(Product.class,productId);
        return product.getGoods();
    }
    
    @Override
    public List<Good> buyExistingGood(Long staffId, Long productId,Long goodId,BigDecimal unitPrice, Integer numOfUnits) throws NotEnoughAmountException{
        Product product = em.find(Product.class, productId);
        Good good = em.find(Good.class, goodId);
        
        BigDecimal number=new BigDecimal(numOfUnits);
        BigDecimal totalAmount=unitPrice.multiply(number);
        
        if ((product.getPurchaseAmount().add(totalAmount)).compareTo(product.getExpectedAmount())==1){
            throw new NotEnoughAmountException ("According to contract, there is not enough amount to purchase this good. This may due to your have exceed the total amount of this category or there is not enough money it the account.");
        }
        BigDecimal existingAmount=new BigDecimal(good.getNumOfUnits());
        
        BigDecimal newUnitPrice=totalAmount.add(good.getUnitPrice().multiply(existingAmount));
        Integer newNumber=numOfUnits+good.getNumOfUnits();
        good.setUnitPrice(newUnitPrice);
        good.setNumOfUnits(newNumber);
        
        product.setPurchaseAmount(product.getPurchaseAmount().add(totalAmount));
        
        recordTransaction(product.getPortfolio().getId(), product,good,unitPrice,numOfUnits,"buy");
        smsbl.recordStaffAction(staffId, "buy goods for wealth product " + good.getId(), product.getPortfolio().getDiscretionaryAccount().getCustomer().getId());
        return displayGood(product.getId());
    }
    
    @Override
    public List<Good> buyNewGood(Long staffId,Long productId,String productName,BigDecimal unitPrice, Integer numOfUnits) throws NotEnoughAmountException{
         Product product = em.find(Product.class, productId);
        
        BigDecimal number=new BigDecimal(numOfUnits);
        BigDecimal totalAmount=unitPrice.multiply(number);
        
        if ((product.getPurchaseAmount().add(totalAmount)).compareTo(product.getExpectedAmount())==1){
            throw new NotEnoughAmountException ("According to contract, there is not enough amount to purchase this good. This may due to your have exceed the total amount of this category or there is not enough money it the account.");
        }
        
        Good good=new Good(productName,unitPrice, numOfUnits, product);
        em.persist(good);
        em.flush();
        
        List<Good>goods=product.getGoods();
        goods.add(good);
        product.setGoods(goods);
        
        product.setPurchaseAmount(product.getPurchaseAmount().add(totalAmount));
        
        recordTransaction(product.getPortfolio().getId(), product,good,unitPrice,numOfUnits,"buy");
         smsbl.recordStaffAction(staffId, "buy goods for wealth product " + good.getId(), product.getPortfolio().getDiscretionaryAccount().getCustomer().getId());
        return displayGood(product.getId());
    }
    
     @Override
    public List<Good> sellGood(Long staffId, Long productId,Long goodId,BigDecimal unitPrice, Integer numOfUnits) throws NotEnoughAmountException{
        Product product = em.find(Product.class, productId);
        Good good = em.find(Good.class, goodId);
        
        BigDecimal number=new BigDecimal(numOfUnits);
        BigDecimal totalAmount=unitPrice.multiply(number);
        
         if (numOfUnits>good.getNumOfUnits()){
            throw new NotEnoughAmountException ("You don't have enough "+good.getName()+"in account");
        }
        BigDecimal existingAmount=new BigDecimal(good.getNumOfUnits());
        
        
        Integer newNumber=numOfUnits-good.getNumOfUnits();
        good.setNumOfUnits(newNumber);
        
        product.setPurchaseAmount(product.getPurchaseAmount().subtract(good.getUnitPrice().multiply(number)));
        
        product.setCurrentAmount(product.getCurrentAmount().subtract(good.getUnitPrice().multiply(number)).add(totalAmount));
        
        updatePortfolioAmount(product.getPortfolio());
        updateDiscretionaryAccount(product.getPortfolio().getDiscretionaryAccount());
            
        recordTransaction(product.getPortfolio().getId(), product,good,unitPrice,numOfUnits,"sell");
        smsbl.recordStaffAction(staffId, "sell goods for wealth product " + good.getId(), product.getPortfolio().getDiscretionaryAccount().getCustomer().getId());
        return product.getGoods();
        
        
    }
    
    private void updatePortfolioAmount(Portfolio portfolio){
        List<Product> products=portfolio.getProducts();
        BigDecimal amount= new BigDecimal(0);
        
        for (int i=0;i<products.size();i++){
            amount.add(products.get(i).getCurrentAmount());
            
        }
        portfolio.setPresentValue(amount);
        
        BigDecimal rate=amount.subtract(portfolio.getInvestAmount());
        BigDecimal trueRate = rate.divide(portfolio.getInvestAmount(),MathContext.DECIMAL128);
       
        Double realRate=trueRate.doubleValue();
        portfolio.setMonthlyInterestRate(realRate);
        em.flush();
        
    }
    
    private void updateDiscretionaryAccount(DiscretionaryAccount discretionaryAccount){
        List<Portfolio> portfolios=discretionaryAccount.getPortfolios();
        BigDecimal amount=new BigDecimal(0);
        
        for (int i=0;i<portfolios.size();i++){
            amount.add(portfolios.get(i).getPresentValue());
        }
        
        discretionaryAccount.setTotalBalance(amount.add(discretionaryAccount.getBalance()));
        em.flush();
        
    }

 
    
    private void recordTransaction(Long portfolioId, Product product,Good good,BigDecimal unitPrice,Integer numOfUnits,String transactionType){
        Portfolio portfolio = em.find(Portfolio.class, portfolioId);
        BigDecimal temp=new BigDecimal(0);
        BigDecimal number=new BigDecimal(numOfUnits);
        PortfolioTransaction portfolioTransaction=new PortfolioTransaction();
        
        if (transactionType.equals("buy")){
          portfolioTransaction=new PortfolioTransaction(Calendar.getInstance().getTime(), product.getProductName(), good.getName(), unitPrice, numOfUnits, temp, unitPrice.multiply(number), portfolio);  
        }else if (transactionType.equals("sell")){
         portfolioTransaction=new PortfolioTransaction(Calendar.getInstance().getTime(), product.getProductName(), good.getName(), unitPrice, numOfUnits, unitPrice.multiply(number), temp, portfolio);     
        }
        em.persist(portfolioTransaction);
        em.flush();
        
        List<PortfolioTransaction> portfolioTransactions=portfolio.getPortfolioTransactions();
        portfolioTransactions.add(portfolioTransaction);       
        portfolio.setPortfolioTransactions(portfolioTransactions);
        em.flush();
        
    }
}
