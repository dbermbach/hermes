/**
 *
 */
package de.tub.ise.hermes;

/**
 * @author David Bermbach
 *         <p>
 *         created on: 24.04.2012
 */
public interface IRequestHandler {

	/**
	 * receives a request and returns a response. response may never be null
	 * (but may have zero payload) unless method requiresReponse() returns false
	 *
	 * @param req
	 *            is guaranteed to be != null
	 * @return
	 */
	public Response handleRequest(Request req);

	/**
	 * @return if the socket connection needs to wait for completion in the
	 *         handleRequest() method. if false the receiver will respond right
	 *         away with an "ok".
	 */
	public boolean requiresResponse();

}
