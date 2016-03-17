package de.tub.ise.hermes.callbacks;

import de.tub.ise.hermes.AsyncCallbackRecipient;
import de.tub.ise.hermes.Response;

public class EchoAsyncCallback implements AsyncCallbackRecipient {

    public boolean isEchoSuccessful() {
        return echoSuccessful;
    }

    public void setEchoSuccessful(boolean echoSuccessful) {
        this.echoSuccessful = echoSuccessful;
    }

    private boolean echoSuccessful;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    private Response response;

    @Override
    public void callback(Response resp) {
        setResponse(resp);
        setEchoSuccessful(resp.responseCode());
    }
}
