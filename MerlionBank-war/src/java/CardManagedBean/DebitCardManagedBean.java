/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardManagedBean;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Bella
 */
@Named(value = "cardManagedBean")
@SessionScoped
public class DebitCardManagedBean implements Serializable{

    
    public DebitCardManagedBean() {
    }
    
}
