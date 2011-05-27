package org.jprotocol.example.handler.server;

public class ServerFacade {
	private final ServerHandlerHierarchy hierarchy;

	public ServerFacade() {
		hierarchy = new ServerHandlerHierarchy();
	}
	
	public void expect() {
//		hierarchy.mockery.expect()
	}
	public void allow() {
//		hierarchy.mockery.allow(request)
	}
	public void control() {
//		hierarchy.mockery.addResponse(requestResponse, removeWhenMatched)
	}
	public void send() {
//		hierarchy.mockery.send(spontaniusProtocolData)
	}
	
	
}
