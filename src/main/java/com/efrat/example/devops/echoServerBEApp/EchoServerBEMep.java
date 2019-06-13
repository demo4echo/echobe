/**
 * 
 */
package com.efrat.example.devops.echoServerBEApp;

import java.io.IOException;
import java.util.logging.Logger;

import com.efrat.example.devops.echoServerBEApp.services.echo.EchoService;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * @author tmeltse
 *
 */
public class EchoServerBEMep 
{
	private static final Logger logger = Logger.getLogger(EchoServerBEMep.class.getName());
	
	private static final int DEFAULT_APP_PORT = 3333*0;
	
	private Server server;

	/**
	 * CTOR 
	 */
	public EchoServerBEMep() 
	{
		// TODO Auto-generated constructor stub
	}

	void start() throws IOException 
	{
		// Get operational port from a designated environment variable
		String designatedApplicationPort = System.getenv("INTERNAL_PORT_ENV_VAR");
		
		// Determine the port on which the server should run
//	    int port = 50051;
	    int port = (designatedApplicationPort != null && designatedApplicationPort.isEmpty() == false) ? Integer.parseInt(designatedApplicationPort) : DEFAULT_APP_PORT;
	    
	    // Build the server
	    server = ServerBuilder.forPort(port)
	        .addService(new EchoService()) // All services go here
	        .build()
	        .start();
	    
	    logger.info("Server started, listening on " + port);
	    
	    /* Add shutdown hook */
	    Runtime.getRuntime().addShutdownHook(new Thread() 
	    {
	    	@Override
	    	public void run() 
	    	{
	    		// Use stderr here since the logger may have been reset by its JVM shutdown hook.
	    		System.err.println("*** shutting down gRPC server since JVM is shutting down");
	
	    		EchoServerBEMep.this.stop();
	    		
	    		System.err.println("*** server shut down");
	    	}
	    });
	}

	void stop() 
	{
		if (server != null) 
		{
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon threads.
	 */
	void blockUntilShutdown() throws InterruptedException 
	{
		if (server != null) 
		{
			server.awaitTermination();
		}
	}
}
