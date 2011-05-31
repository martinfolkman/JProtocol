package org.jprotocol.example.handler.server;

import org.jprotocol.example.api.RequestAPIFactory;
import org.jprotocol.example.api.ResponseAPIFactory;
import org.jprotocol.example.handler.DefaultHandlerHierarchyWithMockery;
import org.jprotocol.framework.facade.AbstractServerFacade;
import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.handler.IFlushable;
import org.jprotocol.framework.test.ProtocolMockery;

public class ServerFacade extends AbstractServerFacade {
	private DefaultHandlerHierarchyWithMockery hierarchy;
	private final RequestAPIFactory requestFactory;
	private final ResponseAPIFactory responseFactory;

	public ServerFacade(IFlushable flushable) {
		super(flushable, Type.Server);
		requestFactory = new RequestAPIFactory();
		responseFactory = new ResponseAPIFactory();
	}
	
	public final ServerFacade init() {
		hierarchy = createHierarchy();
		return this;
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
