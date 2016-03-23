/**
 *
 */
package de.tub.ise.hermes.echo;

import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Sender;
import de.tub.ise.hermes.handlers.EchoRequestHandler;

/**
 * Stand-alone class for echo testing using {@link EchoRequestHandler}. This
 * class should be run on a client machine.
 *
 * @author David Bermbach
 *         <p>
 *         created on: 25.04.2012
 */
public class EchoClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Requires host address and port as parameters");
            System.exit(-1);
        }
        Sender s = new Sender(args[0], Integer.parseInt(args[1]));
        Request req = new Request("echo", "dummy body entry");
        System.out.println("Sending:\n" + req);
        System.out.println("Received:\n" + s.sendMessage(req, 100000));

    }

}
