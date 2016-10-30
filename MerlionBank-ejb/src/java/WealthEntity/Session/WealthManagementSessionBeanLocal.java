/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import CommonEntity.Staff;
import Exception.EmailNotSendException;
import Exception.ListEmptyException;
import Exception.NotEnoughAmountException;
import WealthEntity.Good;
import WealthEntity.Portfolio;
import WealthEntity.Product;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface WealthManagementSessionBeanLocal {

    //After creating discretionary account, before activation, staff top up mpney for customer
    public Long topUpAccount(Long staffId, Long accountId, BigDecimal amount);

    //Manage pending tailored plan
    public List<Portfolio> viewAllPendingApproveTailoredPlan();

    public List<Portfolio> staffRejectPortfolios(Long staffId, Long portfolioId) throws EmailNotSendException;

    public List<Portfolio> staffApprovePortfolios(Long staffId, Long portfolioId) throws EmailNotSendException;
  

    public Portfolio displayPortfolio(Long portfolioId);

    public List<Portfolio> staffModifyPortfolios(Long staffId, Long portfolioId, Double expectedRateOfReturn, Double foreignExchange, Double equity, Double bond, int term) throws EmailNotSendException;

    //View pending Activation plan, haven't finished staff activate plan function
    public List<Portfolio> viewAllPendingAcivationTailoredPlan(Long customerId);
    
    public Portfolio staffActivateLoan(Long staffId, Long portfolioId, Date startDate);
    
    //Staff perform discretionary account cash withdraw
     public Boolean compareAmount(long discretionaryAccountId, BigDecimal amount);
      public Long discreationaryAccountMoneyWithdrawWithEnoughBalance(Long staffId, Long customerId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException;
       public Long transferBackToSavingWithNotEnoughBalance(Long staffId, Long customerId, Long discretionaryAccountId, BigDecimal amount) throws NotEnoughAmountException;
  
       //After staff approve tailored plan, they need to assign a RM
  public List<Staff> retrieveStaffsAccordingToRole(String roleName)throws ListEmptyException;
    public Long assignRM(Long portfolioId,Long staffId);
    
    //buy & sell product
    public List<Portfolio> displayPortfoliosUnderStaff(Long staffId);
     public List<Product> displayProduct(Long customerId,Long portfolioId);
      public List<Good>displayGood(Long productId);
       public Long buyExistingGood(Long staffId, Long productId,Long goodId,BigDecimal unitPrice, Integer numOfUnits) throws NotEnoughAmountException;
       public Long buyNewGood(Long staffId,Long productId,String productName,BigDecimal unitPrice, Integer numOfUnits) throws NotEnoughAmountException;
        public List<Good> sellGood(Long staffId, Long productId,Long goodId,BigDecimal unitPrice, Integer numOfUnits) throws NotEnoughAmountException;
}
