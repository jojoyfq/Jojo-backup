/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

/**
 *
 * @author Bella
 */
public class SearchException extends Exception {

    /**
     * Creates a new instance of <code>SearchException</code> without detail
     * message.
     */
    public SearchException() {
        super();
    }

    public SearchException(String msg) {
        super(msg);
    }
}
