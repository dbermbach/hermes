/**
 *
 */
package de.tub.ise.test;

import de.tub.ise.hermes.Receiver;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.RequestHandlerRegistry;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tub.ise.hermes.callbacks.EchoAsyncCallback;
import de.tub.ise.hermes.handlers.EchoRequestHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class EchoClientServerTest {

    @Test
    public void echoSyncClientTest() throws IOException {
        RequestHandlerRegistry.getInstance().registerHandler("echo",
                new EchoRequestHandler());
        Receiver r = new Receiver(9090, 1, 1);
        r.start();

        Sender s = new Sender("localhost", 9090);
        Request req = new Request("echo", "TestSender");
        Response response = s.sendMessage(req, 1000);
        Assert.assertTrue(response.responseCode());
    }

    @Test
    public void echoAsyncClientTest() throws IOException, InterruptedException {
        RequestHandlerRegistry.getInstance().registerHandler("echo",
                new EchoRequestHandler());
        Receiver r = new Receiver(9090, 1, 1);
        r.start();

        Sender s = new Sender("localhost", 9090);
        Request req = new Request("echo", "TestSender");
        EchoAsyncCallback echoAsyncCallback = new EchoAsyncCallback();
        boolean received = s.sendMessageAsync(req, echoAsyncCallback);
        Assert.assertTrue(received);
        while (true) {
            Thread.sleep(50);
            if (echoAsyncCallback.getResponse() != null) break;
        }
        Assert.assertTrue(echoAsyncCallback.isEchoSuccessful());
    }


}
