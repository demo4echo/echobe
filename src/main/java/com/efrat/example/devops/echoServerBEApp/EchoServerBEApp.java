/**
 * 
 */
package com.efrat.example.devops.echoServerBEApp;

import java.io.IOException;

/**
 * @author tmeltse
 *
 */
public class EchoServerBEApp 
{
	/**
	 * Main launches the server from the command line.
	 */
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		final EchoServerBEMep serverApp = new EchoServerBEMep();
		serverApp.start();
		serverApp.blockUntilShutdown();
	}
}
