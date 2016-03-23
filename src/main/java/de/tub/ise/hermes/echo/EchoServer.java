/**
 *
 */
package de.tub.ise.hermes.echo;

import de.tub.ise.hermes.Receiver;
import de.tub.ise.hermes.RequestHandlerRegistry;
import de.tub.ise.hermes.handlers.EchoRequestHandler;

/**
 * Stand-alone class for echo testing using {@link EchoRequestHandler}. This
 * class should be run on the server.
 *
 * @author David Bermbach
 *         <p>
 *         created on: 25.04.2012
 */
public class EchoServer {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Requires server port as first argument.");
			System.exit(-1);
		}
		RequestHandlerRegistry.getInstance().registerHandler("echo",
				new EchoRequestHandler());
		Receiver r = new Receiver(Integer.parseInt(args[0]));
		r.start();
	}

}
