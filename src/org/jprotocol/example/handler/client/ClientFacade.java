package org.jprotocol.example.handler.client;

import org.jprotocol.example.api.RequestAPIFactory;
import org.jprotocol.example.api.ResponseAPIFactory;
import org.jprotocol.example.handler.DefaultHandlerHierarchyWithMockery;
import org.jprotocol.framework.facade.AbstractClientFacade;
import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.handler.IFlushable;
import org.jprotocol.framework.test.ProtocolMockery;

public class ClientFacade extends AbstractClientFacade {
	private final DefaultHandlerHierarchyWithMockery hierarchy;
	private final RequestAPIFactory requestFactory;
	private final ResponseAPIFactory responseFactory;

	public ClientFacade(IFlushable flushable) {
		super(flushable, Type.Client);
		requestFactory = new RequestAPIFactory();
		responseFactory = new ResponseAPIFactory();
		hierarchy = createHierarchy();
	}
	
	/**
	 * Override to provide specialized hierarchy
	 * @param type 
	 * @param flushable
	 * @return
	 */
	protected DefaultHandlerHierarchyWithMockery createHierarchy() {
		return new DefaultHandlerHierarchyWithMockery(type, flushable);
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
	@Override
	public void receive(byte[] data) {
		hierarchy.receive(data);
	}
	
}
