/**
 *
 */
package de.tub.ise.hermes;

/**
 * Implementations of this interface can be used to for asychronous callbacks for sendMessageAsync()
 *
 * @author David Bermbach
 *         <p>
 *         created on: 03.05.2012
 */
public interface AsyncCallbackRecipient {

    /**
     * Some time after calling sendMessageAsync this method will be called
     *
     * @param resp
     */
    public void callback(Response resp);

}
