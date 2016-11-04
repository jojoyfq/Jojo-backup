/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessIntelligenceEntity.Session;

import javax.ejb.Local;

/**
 *
 * @author a0113893
 */
@Local
public interface SpendingSegmentationTimerSessionBeanLocal {
   public void debitCardSegmentation();
    public void creditCardSegmentation();
     public void sendBirthdayCoupon();
   
}
