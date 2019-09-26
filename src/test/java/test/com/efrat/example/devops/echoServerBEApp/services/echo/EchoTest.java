/**
 * 
 */
package test.com.efrat.example.devops.echoServerBEApp.services.echo;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author tmeltse
 *
 */
public class EchoTest 
{
//	public static final String DEFAULT_ECHOBE_SERVICE_HOST = "localhost"; 
//	public static final String DEFAULT_ECHOBE_SERVICE_PORT = "3333"; 

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
	}

	@Test
	public final void test() 
	{
		System.out.println("ECHOBE_SERVICE_ENDPOINT_NAMESPACE environment variable value is: [" + System.getenv().get("ECHOBE_SERVICE_ENDPOINT_NAMESPACE") + "]");

//		String echobeServiceHostAsStr = System.getProperty("com.efrat.echobe.serviceEndPoint.host",DEFAULT_ECHOBE_SERVICE_HOST);
//		String echobeServicePortAsStr = System.getProperty("com.efrat.echobe.serviceEndPoint.port",DEFAULT_ECHOBE_SERVICE_PORT);
		String echobeServiceHostAsStr = System.getProperty("com.efrat.echobe.serviceEndPoint.host");
		String echobeServicePortAsStr = System.getProperty("com.efrat.echobe.serviceEndPoint.port");
		String echobeServiceNamespaceStr = System.getenv().get("ECHOBE_SERVICE_ENDPOINT_NAMESPACE");

		// Validate we have what we need
		assertNotNull(echobeServiceHostAsStr);
		assertNotNull(echobeServicePortAsStr);
		assertNotNull(echobeServiceNamespaceStr);
		
//		HelloWorldClient client = new HelloWorldClient("localhost", 50051);
		String echobeServiceName = echobeServiceHostAsStr + "." + echobeServiceNamespaceStr;
		EchoServiceFacade client = new EchoServiceFacade(echobeServiceName,Integer.parseInt(echobeServicePortAsStr));

		try 
		{
			/* Access a service running on the local machine on port 3333 */
			String query = "Tiran";
			
			String reply = client.echo(query);
			assertEquals(query,reply);
		} 
		finally 
		{
			try 
			{
				client.shutdown();
			} 
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("Exception has been thrown!");
			}
		}
	}
}
