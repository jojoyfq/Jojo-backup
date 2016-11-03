/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WealthEntity.Session;

import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface WealthTimerSessionBeanLocal {
 public void closeAccount();
 public void interestCrediting();
 public void commissionFeeCalculation();
  public void closePortfolio();
   public void updateDiscretionaryAccountInterestRate();
  public void preDefinedPlanInterestCrediting();
}
