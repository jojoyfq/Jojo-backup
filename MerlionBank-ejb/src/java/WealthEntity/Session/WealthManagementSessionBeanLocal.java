/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import Exception.EmailNotSendException;
import WealthEntity.Portfolio;
import java.math.BigDecimal;
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

    public List<Portfolio> staffModifyPortfolios(Long staffId, Long portfolioId, Double expectedRateOfReturn, Double foreignExchange, Double equity, Double stock, int term) throws EmailNotSendException;

    //View pending Activation plan, haven't finished staff activate plan function
    public List<Portfolio> viewAllPendingAcivationTailoredPlan();

}
