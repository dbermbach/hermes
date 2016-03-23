/**
 *
 */
package de.tub.ise.hermes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * a request which can be sent using this package
 *
 * @author David Bermbach
 *         <p>
 *         created on: 24.04.2012
 */
public class Response {

	/**
	 * payload
	 */
	private List<Serializable> items = new ArrayList<Serializable>();

	/**
	 * optional response message, may be empty string but not null
	 */
	private String responseMessage;

	/**
	 * true if successful, false otherwise (use responseMessage to explain in
	 * the latter case)
	 */
	private boolean responseCode;

	private String requestId;

	/**
	 * 
	 * @param responseMessage
	 *            human-readable response message
	 * @param responseCode
	 *            true if request was successful, if false use responseMessage
	 *            to explain
	 * @param originalReq
	 *            the original request
	 * @param items
	 *            the payload
	 */
	public Response(String responseMessage, boolean responseCode,
			Request originalReq, List<Serializable> items) {
		super();
		this.items.addAll(items);
		this.responseMessage = responseMessage;
		this.responseCode = responseCode;
		requestId = originalReq == null ? "N/A" : originalReq.getRequestId();
	}

	/**
	 * @param responseMessage
	 *            human-readable response message
	 * @param responseCode
	 *            true if request was successful, if false use responseMessage
	 *            to explain
	 * @param originalReq
	 *            the original request
	 * @param items
	 *            the payload
	 */
	public Response(String responseMessage, boolean responseCode,
			Request originalReq, Serializable... items) {
		super();
		this.items.addAll(Arrays.asList(items));
		this.responseMessage = responseMessage;
		this.responseCode = responseCode;
		requestId = originalReq == null ? "N/A" : originalReq.getRequestId();
	}

	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return this.responseMessage;
	}

	/**
	 * @return the items
	 */
	public List<Serializable> getItems() {
		return this.items;
	}

	/**
	 * @return the responseCode
	 */
	public boolean responseCode() {
		return this.responseCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = " Response message\nResponse code: "
				+ (responseCode ? "Success" : "Failure") + "\nText response: "
				+ responseMessage + "\nBody:";
		int counter = 1;
		if (items.isEmpty()) {
			s += "***No items in Message body***";
		} else {
			for (Serializable ser : items)
				s += "\n\tItem " + counter++ + ": " + ser;
		}
		return s.trim();
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return this.requestId;
	}

	/**
	 * @param requestId
	 *            the requestId to set
	 */
	void setRequestId(String requestId) {
		if (this.requestId.equals("N/A") || requestId != null)
			this.requestId = requestId;
	}

}
