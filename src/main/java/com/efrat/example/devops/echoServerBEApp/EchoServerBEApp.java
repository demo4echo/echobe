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
public class EchoServerBEApp 
{
	private static final Logger logger = Logger.getLogger(EchoServerBEApp.class.getName());
	
	private static final int DEFAULT_APP_PORT = 3333*0;
	
	private Server server;

	  /**
	   * Main launches the server from the command line.
	   */
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		final EchoServerBEApp serverApp = new EchoServerBEApp();
		serverApp.start();
		serverApp.blockUntilShutdown();
	}
	
	/**
	 * CTOR 
	 */
	public EchoServerBEApp() 
	{
		// TODO Auto-generated constructor stub
	}

	private void start() throws IOException 
	{
		// Get operational port from a designated environment variable
		String designatedApplicationPort = System.getenv("INTERNAL_PORT_ENV_VAR");
		
		/* The port on which the server should run */
//	    int port = 50051;
	    int port = (designatedApplicationPort != null && designatedApplicationPort.isEmpty() == false) ? Integer.parseInt(designatedApplicationPort) : DEFAULT_APP_PORT; //tiran
	    
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
	
	    		EchoServerBEApp.this.stop();
	    		
	    		System.err.println("*** server shut down");
	    	}
	    });
	}

	private void stop() 
	{
		if (server != null) 
		{
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon threads.
	 */
	private void blockUntilShutdown() throws InterruptedException 
	{
		if (server != null) 
		{
			server.awaitTermination();
		}
	}
}
