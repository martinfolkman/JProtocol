package org.jprotocol.example.test;
import static org.junit.Assert.assertTrue;

import org.jprotocol.example.handler.DefaultHandlerHierarchyWithMockery;
import org.jprotocol.example.handler.DefaultMyMiddleProtocolAHandler;
import org.jprotocol.example.handler.server.ServerFacade;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.Handler;
import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.handler.HandlerContext;
import org.jprotocol.framework.handler.IFlushable;
import org.junit.Before;
import org.junit.Test;

public class SpecializedExampleTest {
	private SpecializedClientServerTestFacade o;

	@Before
	public void before() {
		o = new SpecializedClientServerTestFacade();
	}
	
	@Test
	public void specializedMakeHeaderForMiddleA() {
		o.client.expect(o.client.responses().MyMiddleProtocolA_Response_API().getMiddleSwitch().setA().getMiddleHeader().setZ());
		o.client.send(o.client.requests().MyLeafProtocolA_Request_API());
		assertTrue(o.client.getErrorMessage(), o.client.isOk());
		
	}
}


class SpecializedClientServerTestFacade extends ClientServerTestFacade {
	@Override
	protected ServerFacade createServerFacade(IFlushable flushable) {
		return new SpecializedServerFacade(flushable);
	}

}

class SpecializedServerFacade extends ServerFacade {

	public SpecializedServerFacade(IFlushable flushable) {
		super(flushable);
	}
	@Override
	protected SpecializedHandlerHierarchyWithMockery createHierarchy() {
		return new SpecializedHandlerHierarchyWithMockery(type, flushable);
	}

}

class SpecializedHandlerHierarchyWithMockery extends DefaultHandlerHierarchyWithMockery {

	public SpecializedHandlerHierarchyWithMockery(Type type, IFlushable flushable) {
		super(type, flushable);
	}
	@Override
	protected Handler<?, ?> createMiddleA(HandlerContext context) {
		return new SpecializedMyMiddleProtocolAHandler(context);
	}

	
	
}

class SpecializedMyMiddleProtocolAHandler extends DefaultMyMiddleProtocolAHandler {
	protected SpecializedMyMiddleProtocolAHandler(HandlerContext context) {
		super(context);
	}
	
	@Override
	protected void makeHeader(IProtocolMessage header, IProtocolMessage payload, int headerValue) {
		createResponse(header).getMiddleHeader().setZ();
	}
}
