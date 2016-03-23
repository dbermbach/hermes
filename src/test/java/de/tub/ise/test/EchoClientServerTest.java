/**
 *
 */
package de.tub.ise.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import de.tub.ise.hermes.AsyncCallbackRecipient;
import de.tub.ise.hermes.Receiver;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.RequestHandlerRegistry;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tub.ise.hermes.handlers.EchoRequestHandler;

public class EchoClientServerTest {

    @Test
    public void echoSyncClientTest() throws IOException {
        RequestHandlerRegistry.getInstance().registerHandler("echo",
                new EchoRequestHandler());
        Receiver r = new Receiver(9090);
        r.start();

        Sender s = new Sender("localhost", 9090);
        Request req = new Request("echo", "TestSender");
        Response response = s.sendMessage(req, 1000);
        Assert.assertTrue(response.responseCode());
        r.terminate();
    }

    @Test
    public void echoAsyncClientTest() throws IOException, InterruptedException {
        RequestHandlerRegistry.getInstance().registerHandler("echo",
                new EchoRequestHandler());
        Receiver r = new Receiver(9091);
        r.start();

        Sender s = new Sender("localhost", 9091);
        Request req = new Request("echo", "TestSender");
        EchoAsyncCallback echoAsyncCallback = new EchoAsyncCallback();
        boolean received = s.sendMessageAsync(req, echoAsyncCallback);
        Assert.assertTrue(received);
        while (true) {
            Thread.sleep(50);
            if (echoAsyncCallback.getResponse() != null) break;
        }
        Assert.assertTrue(echoAsyncCallback.isEchoSuccessful());
        r.terminate();
    }

    private class EchoAsyncCallback implements AsyncCallbackRecipient {

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

}


