/**
 *
 */
package de.tub.ise.hermes.handlers;

import de.tub.ise.hermes.IRequestHandler;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Response;

/**
 * writes the data from the request into the response
 *
 * @author David Bermbach
 *         <p>
 *         created on: 25.04.2012
 */
public class EchoRequestHandler implements IRequestHandler {

    /*
     * (non-Javadoc)
     *
     * @see
     * IRequestHandler#handleRequest(edu.kit.aifb.dbe
     * .hermes.Request)
     */
    @Override
    public Response handleRequest(Request req) {
        return new Response(req.getItems(), "Echo okay for target: "
                + req.getTarget(), true, req);

    }

    /*
     * (non-Javadoc)
     *
     * @see IRequestHandler#requiresResponse()
     */
    @Override
    public boolean requiresResponse() {
        return true;
    }

    @Override
    public boolean hasPriority() {
        return false;
    }

}
