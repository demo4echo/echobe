/**
 * 
 */
package com.efrat.example.devops.echoServerBEApp.services.echo;

import com.efrat.example.devops.echoServerBEApp.generated.services.echo.*;

import io.grpc.stub.StreamObserver;

/**
 * @author tmeltse
 *
 */
public class EchoService extends EchoGrpc.EchoImplBase 
{
	/**
	 * 
	 */
	public EchoService() 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void echo(EchoQuery request, StreamObserver<EchoReply> responseObserver) 
	{
		EchoReply reply = EchoReply.newBuilder().setReplyMessage(request.getQueryMessage()).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}
}
