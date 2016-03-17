/**
 *
 */
package de.tub.ise.hermes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;


/**
 * a registry for request handlers
 *
 * @author David Bermbach
 *         <p>
 *         created on: 24.04.2012
 */
public class RequestHandlerRegistry {

    private static final Logger log = LoggerFactory
            .getLogger(RequestHandlerRegistry.class);

    /**
     * Singleton
     */
    private static final RequestHandlerRegistry instance = new RequestHandlerRegistry();

    /**
     * holds all the known request handlers. key = target specified by clients,
     * value = object which can handle such a request
     */
    private static final HashMap<String, IRequestHandler> handlers = new HashMap<String, IRequestHandler>();

    private RequestHandlerRegistry() {
    }

    /**
     * @return the singleton
     */
    public static RequestHandlerRegistry getInstance() {
        return RequestHandlerRegistry.instance;
    }

    /**
     * @param id id of the request handler
     * @return the request handler for this id or null
     */
    public synchronized IRequestHandler getHandlerForID(String id) {
        return handlers.get(id);
    }

    /**
     * registers a handler for the specified id if the id is not in use
     *
     * @param id      should be the handlers class name
     * @param handler implementation of {@link IRequestHandler}
     * @return true if registered or false if the id was already in use
     */
    public synchronized boolean registerHandler(String id,
                                                IRequestHandler handler) {
        IRequestHandler old = handlers.put(id, handler);
        if (old != null) {
            handlers.put(id, old);
            return false;
        }
        log.info("Registered handler of type "
                + handler.getClass().getSimpleName() + " for target " + id);
        return true;
    }

    /**
     * removes all known handlers, use with caution
     */
    public synchronized void clearRegistry() {
        handlers.clear();
        log.info("Registry cleared");
    }

    /**
     * unregisters the handler for the specified id or does nothing if no such
     * mapping exists
     *
     * @param id
     */
    public synchronized void unregisterHandler(String id) {
        handlers.remove(id);
        log.info("Unregistered target " + id);
    }

    /**
     * prints all known handlers
     */
    public synchronized void printHandlers() {
        String s = "Known handlers:";
        for (String id : handlers.keySet()) {
            s += "\n\t" + handlers.get(id).getClass().getSimpleName() + "(target: " + id + ")";
        }
        log.info(s);
    }
}
