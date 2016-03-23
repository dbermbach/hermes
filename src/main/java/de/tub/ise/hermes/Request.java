/**
 *
 */
package de.tub.ise.hermes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * a request which can be sent using this package
 *
 * @author David Bermbach
 *         <p>
 *         created on: 24.04.2012
 */
public class Request {

    private static volatile Long nextId = 1L;
    /**
     * unique id per request
     */
    private final String requestId;
    /**
     * payload
     */
    private List<Serializable> items;
    /**
     * handler id @see {@link Sender}
     */
    private String target;
    /**
     * id of the original sender, is by default set with the sender of this request
     */
    private String originator;
    /**
     * creation timestamp. Transient field that is not serialized. Serves the
     * purpose of tracking internally how long requests exist before they are
     * served.
     */
    private Date timestamp = new Date();

    /**
     * @param items
     * @param target
     */
    public Request(List<Serializable> items, String target, String sender) {
        super();
        this.items = items;
        this.target = target;
        this.requestId = sender + getNextId();
        this.originator = sender;
    }

    Request(List<Serializable> items, String target, String messageid,
            Object ignoredParam) {
        super();
        this.items = items;
        this.target = target;
        this.requestId = messageid;
    }

    /**
     * @param target
     */
    public Request(String target, String sender) {
        super();
        this.items = Collections.emptyList();
        this.target = target;
        this.requestId = sender + getNextId();
        this.originator = sender;
    }

    /**
     * @param items
     * @param target
     */
    public Request(Serializable item, String target, String sender) {
        super();
        this.items = new ArrayList<Serializable>();
        items.add(item);
        this.target = target;
        this.requestId = sender + getNextId();
        this.originator = sender;
    }

    private long getNextId() {
        synchronized (nextId) {
            nextId++;
            return nextId - 1;
        }
    }

    /**
     * appends the specified item to the message body
     *
     * @param item
     */
    public void addItem(Serializable item) {
        items.add(item);
    }

    /**
     * @return the number of items in the payload/message body
     */
    public int getNumberOfItems() {
        return items.size();
    }

    /**
     * @return the items
     */
    public List<Serializable> getItems() {
        return this.items;
    }

    /**
     * @return the target
     */
    public String getTarget() {
        return this.target;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String s = "Request for handler " + target + "\nBody:";
        int counter = 1;
        if (items.isEmpty()) {
            s += "***No items in Message body***";
        } else {
            for (Serializable ser : items) {
                s += "\n\tItem " + counter++ + ": " + ser;
            }
        }
        return s.trim();
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * @return the requestId
     */
    public String getRequestId() {
        return this.requestId;
    }

    /**
     * @return the originator
     */
    public String getOriginator() {
        return this.originator;
    }

    /**
     * @param originator the originator to set
     */
    public void setOriginator(String originator) {
        this.originator = originator;
    }

}
