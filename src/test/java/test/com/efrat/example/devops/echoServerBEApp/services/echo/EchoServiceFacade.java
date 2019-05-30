/**
 * 
 */
package test.com.efrat.example.devops.echoServerBEApp.services.echo;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.efrat.example.devops.echoServerBEApp.generated.services.echo.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

/**
 * @author tmeltse
 * 
 * A simple client that requests an echo from the echo server.
 */
public class EchoServiceFacade 
{
	private static final Logger logger = Logger.getLogger(EchoServiceFacade.class.getName());

	private final ManagedChannel channel;
	private final EchoGrpc.EchoBlockingStub blockingStub;

	/** Construct client connecting to Echo server at {@code host:port}. */
	public EchoServiceFacade(String host,int port) 
	{
		this(ManagedChannelBuilder.forAddress(host,port)
				// Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid needing certificates.
				.usePlaintext()
				.build());

		logger.info("Finished building a managed channel for host [" + host + "] and port [" + port + "]");
	}

	/** Construct client for accessing Echo server using the existing channel. */
	private EchoServiceFacade(ManagedChannel channel) 
	{
		this.channel = channel;
		this.blockingStub = EchoGrpc.newBlockingStub(channel);
	}

	/** Close communication channel */
	public void shutdown() throws InterruptedException 
	{
		channel.shutdown().awaitTermination(5,TimeUnit.SECONDS);
	}

	/** Send some echo to the server and return the response. */
	public String echo(String what) 
	{
		logger.info("Will try to echo [" + what + "] ...");
	    
		EchoQuery request = EchoQuery.newBuilder().setQueryMessage(what).build();
		EchoReply response;
		try 
		{
			response = blockingStub.echo(request);
		} 
		catch (StatusRuntimeException e) 
		{
			logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			return "";
		}
	    
		logger.info("Echo response is: [" + response.getReplyMessage() + "]");
		
		// Return the echo response
		return response.getReplyMessage();
	}
}
