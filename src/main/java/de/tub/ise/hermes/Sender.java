/**
 *
 */
package de.tub.ise.hermes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * This class represents the theoretical connection to a particular endpoint
 *
 * @author David Bermbach
 *         <p>
 *         created on: 24.04.2012
 */
public class Sender {

    private static final Logger log = LoggerFactory.getLogger(Sender.class);

    /**
     * target host
     */
    private final String host;

    /**
     * target port
     */
    private final int port;

    /**
     * determines the number of items which shall be sent before reading back
     */
    private int noOfSendItems = 0;

    /**
     * the number of items already sent
     */
    private int sentItems = 0;

    /**
     * the active socket if one exists
     */
    private Socket socket;

    /**
     * part of socket
     */
    private ObjectOutputStream out;

    /**
     * part of socket
     */
    private ObjectInputStream in;

    /**
     * generates a new sender.
     *
     * @param host target host
     * @param port target port
     */
    public Sender(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    /**
     * opens up a new connection. If there is an existing connection the old
     * connection is closed.
     *
     * @param noOfSendItems the number of items which shall be sent before reading back
     * @param target        the String id of the target RequestHandler on the server side
     * @return true if the session is active
     */
    private boolean startSession(int noOfSendItems, String target,
                                 String messageid, String originator) {
        try {
            if (socket != null) {
                closeSession();
            }
            if (noOfSendItems < 0) {
                noOfSendItems = 0;
            }
            this.noOfSendItems = noOfSendItems;
            if (target == null) {
                log.error("You need to specify a target handler.");
                return false;
            }
            log.debug("Creating connection to " + host + ":" + port + " at "
                    + new Date().getTime());
            socket = new Socket(host, port);
            log.debug(
                    "Created connection to " + host + ":" + port + " at "
                            + new Date().getTime());
            out = new ObjectOutputStream(new BufferedOutputStream(
                    socket.getOutputStream()));
            out.flush();
            log.debug(
                    "Created streams to " + host + ":" + port + " at "
                            + new Date().getTime());
            // log.debug("Outputstream generated and flushed.");
            in = new ObjectInputStream(new BufferedInputStream(
                    socket.getInputStream()));
            // log.debug("Inputstream generated.");
            this.sentItems = 0;
            // send header information
            // send number of items first
            out.writeObject(noOfSendItems);
            out.flush();
            // send handler id (target)
            out.writeObject(target);
            out.flush();
            // send message id
            out.writeObject(messageid);
            out.flush();
            // send originator
            out.writeObject(originator);
            out.flush();
        } catch (Exception e) {
            closeSession();
            log.error("Could not start session.", e);
            return false;
        }
        return true;
    }

    /**
     * sends the item over the network if the maximum number of items has not
     * been reached yet.
     *
     * @param item
     * @return true if data was sent, false if a failure occurred or if there
     * was no active session
     */
    private boolean send(Serializable item) {
        if (socket == null) {
            log.error("No active session.");
            return false;
        }
        if (item == null) {
            log.error("Cannot send null item.");
            return false;
        }
        // connection exists
        try {
            if (sentItems >= noOfSendItems) {
                log.error("All items specified at session start have been sent, cannot send more.");
                return false;
            }
            // send next item
            out.writeObject(item);
            out.flush();
            sentItems++;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * reads a response back. This method must be called to close a session
     *
     * @return * a {@link Response} if everything was okay <br>
     * * an {@link Response} without payload/message body if no response
     * was required by the server<br>
     * * null if no active session existed or if an error occurred
     */
    private Response receive(Request req) {
        if (socket == null) {
            log.error("No active session.");
            return new Response("No active session", false, req);
        }
        if (sentItems != noOfSendItems) {
            String error = "Out of " + noOfSendItems + " items only " + sentItems
                    + " have been sent. Must complete before reading back.";
            log.error(error);
            return new Response(error,
                    false, req);
        }
        Response response;
        try {
            int attachedItems = (Integer) in.readObject(); // number of items
            boolean success = (Boolean) in.readObject(); // success code
            String responseMessage = (String) in.readObject(); // response message text
            String requestId = (String) in.readObject(); // request id
            if (!success) {
                log.debug("An error occurred at the server: " + responseMessage);
                return new Response("An error occurred at the server: " + responseMessage,
                        false, req);
            } else
                log.debug("Request successful at the server: " + responseMessage);
            List<Serializable> res = new ArrayList<Serializable>(attachedItems);
            if (attachedItems == 0) {
                closeSession();
                response = new Response(res, responseMessage, success, null);
                response.setRequestId(requestId);
                return response;
            }
            // read items
            for (int i = 0; i < attachedItems; i++) {
                Object o = in.readObject();
                res.add((Serializable) o);
            }
            response = new Response(res, responseMessage, success, null);
            response.setRequestId(requestId);
        } catch (Exception e) {
            log.error("An error has occured (session closed): " + e);
            return new Response("An error has occured (session closed): " + e,
                    false, req);
        }
        closeSession();
        return response;
    }

    /**
     * closes the current session, will never throw an {@link Exception}
     */
    private void closeSession() {
        try {
            if (socket != null)
                socket.close();
            socket = null;
            out = null;
            in = null;
        } catch (Exception e) {
        }
    }

    /**
     * sends a message with timeout (in ms)
     *
     * @return * a {@link Response} if everything was okay <br>
     * * an {@link Response} without payload/message body if no response
     * was required by the server<br>
     * * null if an error occurred
     */
    public Response sendMessage(Request req, long timeout) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        SenderCallable sc = new SenderCallable(req);
        Future<Response> future = exec.submit(sc);
        Response resp;
        try {
            resp = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            resp = new Response(
                    "A timeout or an error during processing has occurred: "
                            + e.getMessage(), false, req);
        }
        return resp;
    }

    /**
     * sends a message asynchronously
     *
     * @return false if callback or req is null
     */
    public boolean sendMessageAsync(final Request req,
                                    final AsyncCallbackRecipient callback) {
        if (req == null || callback == null)
            return false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response resp = sendMessage(req, 120000L);
                callback.callback(resp);
            }
        }).start();
        return true;
    }

    class SenderCallable implements Callable<Response> {

        Request req;

        public SenderCallable(Request req) {
            this.req = req;
        }

        @Override
        public Response call() throws Exception {
            boolean success = startSession(req.getNumberOfItems(),
                    req.getTarget(), req.getRequestId(), req.getOriginator());
            if (!success) {
                log.error("Could not start session.");
                return new Response("Could not start session.", false, req);
            }
            // for empty messages without body read response directly
            if (req.getNumberOfItems() == 0) {
                return receive(req);
            }
            for (Serializable o : req.getItems()) {
                success = send(o);
                if (!success) {
                    closeSession();
                    log.error("Error while sending an item, session closed.");
                    return new Response(
                            "Error while sending an item, session closed.",
                            false, req);
                }
            }
            return receive(req);
        }
    }

}
