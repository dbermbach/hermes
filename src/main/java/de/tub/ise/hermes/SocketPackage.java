/**
 *
 */
package de.tub.ise.hermes;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * container for sockets and corresponding streams
 *
 * @author David Bermbach
 *         <p>
 *         created on: 25.04.2012
 */
public class SocketPackage {

    public ObjectOutputStream out;
    public ObjectInputStream in;
    public Socket socket;

}
