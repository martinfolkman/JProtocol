package org.jprotocol.example.handler.client;

import org.jprotocol.example.api.RequestAPIFactory;
import org.jprotocol.example.api.ResponseAPIFactory;
import org.jprotocol.framework.facade.AbstractClientFacade;
import org.jprotocol.framework.handler.IFlushable;
import org.jprotocol.framework.test.ProtocolMockery;

public class ClientFacade extends AbstractClientFacade {
	private final ClientHandlerHierarchy hierarchy;
	private final RequestAPIFactory requestFactory;
	private final ResponseAPIFactory responseFactory;

	public ClientFacade(IFlushable flushable) {
		hierarchy = new ClientHandlerHierarchy(flushable);
		requestFactory = new RequestAPIFactory();
		responseFactory = new ResponseAPIFactory();
	}
	public RequestAPIFactory requests() {
		return requestFactory;
	}
	public ResponseAPIFactory responses() {
		return responseFactory;
	}

	@Override
	protected ProtocolMockery getMockery() {
		return hierarchy.mockery;
	}
	public void receive(byte[] data) {
		hierarchy.receive(data);
	}
	
}
