# hermes
hermes is a very simple communication middleware that focuses on ease-of-use instead of performance, scalability, etc. Originally developed for use in teaching of Cloud Computing courses at Karlsruhe Institute of Technology, we now use it for courses at TU Berlin. The middleware uses a simple event-driven server implementation model and corresponding clients.

### Getting started
Once you have hermes on the buildpath of your Java project, writing a server and client is straightforward:

1. Create a handler for particular events by implementing the IRequestHandler interface.

   You will have to implement two methods: `handleRequest(Request req)` will be invoked by the middleware whenever a request for that specific handler comes in. `requiresResponse()` allows one-way communication: If this method returns false, the middleware will respond to the client directly before invoking `handleRequest(Request req)`. `Request` parameters may be empty objects but will never be null, `handleRequest(Request req)` may only return null if `requiresResponse()` returns false. For a simple test, you may also use the built-in `EchoHandler`.
2. Create a new instance of the handler and register it with the `RequestHandlerRegistry`:

   ```java
RequestHandlerRegistry reg = RequestHandlerRegistry.getInstance();
reg.registerHandler("targetName", handlerImplementationObject);
```

3. Create and start a new `Receiver` instance:

   ```java
Receiver receiver = new Receiver(port);
receiver.start();
```

4. If you have made sure that the `port` parameter is a valid port number that is reachable for TCP traffic, then clients can now send requests to the server by specifying the target name used in step 2:

   ```java
Sender sender = new Sender(host, port);
```
   `Sender` instances provide methods `sendMessage` and `sendMessageAsync` to communicate with hermes servers. The parameters for both messages are the target name (ID of the handler), any number of `Serializable` objects, a timeout, and an unterinterpreted `String` which is available at the server side through the `request.getOriginator()` method. The latter is usually used to pass on metadata or a sender ID. Clients can send messages asynchronously in the background by providing an instance of the `AsyncCallbackRecipient` interface whose `callback(Response resp)` method will then be invoked with the response to the original request.
