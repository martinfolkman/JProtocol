package org.jprotocol.example.handler;

import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.handler.IFlushable;
import org.jprotocol.framework.handler.ProtocolSnifferProxy;
import org.jprotocol.framework.handler.ProtocolState;
import org.jprotocol.framework.test.IProtocolLogger.NullProtocolLogger;
import org.jprotocol.framework.test.ProtocolMockery;

public class DefaultHandlerHierarchyWithMockery extends DefaultHandlerHierarchy {
	
	public final ProtocolMockery mockery;
	public DefaultHandlerHierarchyWithMockery(Type type, final IFlushable flushable) {
		this(type, flushable, new ProtocolSnifferProxy());
	}
	private DefaultHandlerHierarchyWithMockery(Type type, final IFlushable flushable, ProtocolSnifferProxy sniffer) {
		super(type, flushable, new ProtocolState(), sniffer);
		this.mockery = new ProtocolMockery(getRoot(), new NullProtocolLogger(), true);
		sniffer.init(mockery);
	}
	
	
}
