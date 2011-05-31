package org.jprotocol.example.handler;

import static org.jprotocol.framework.handler.HandlerDsl.handler;
import static org.jprotocol.framework.handler.HandlerDsl.root;

import org.jprotocol.example.api.MyMiddleProtocolA_Request_API;
import org.jprotocol.example.api.MyMiddleProtocolA_Response_API;
import org.jprotocol.example.api.MyMiddleProtocolB_Request_API;
import org.jprotocol.example.api.MyMiddleProtocolB_Response_API;
import org.jprotocol.example.api.MyRootProtocol_Request_API;
import org.jprotocol.example.api.MyRootProtocol_Response_API;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.Handler;
import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.handler.HandlerContext;
import org.jprotocol.framework.handler.IFlushable;
import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.ProtocolSnifferProxy;
import org.jprotocol.framework.handler.ProtocolState;
import org.jprotocol.framework.test.IProtocolLogger.NullProtocolLogger;
import org.jprotocol.framework.test.ProtocolMockery;

public class DefaultHandlerHierarchy {
	
	protected final Type type;
	protected final boolean msbFirst;
	protected final IProtocolState protocolState;
	protected final ProtocolSnifferProxy sniffer;
	private final Root root;
	public final ProtocolMockery mockery;
	public DefaultHandlerHierarchy(Type type, final IFlushable flushable) {
		this.type = type;
		this.msbFirst = false;
		this.protocolState = new ProtocolState();
		this.sniffer = new ProtocolSnifferProxy();
		this.root = createRoot(flushable);
		this.mockery = new ProtocolMockery(getRoot(), new NullProtocolLogger(), true);
		sniffer.init(mockery);
		init();
	}
	
	public void init() {
		root(getRoot(), 
		  handler(createMiddleA(new HandlerContext(type, msbFirst, MyMiddleProtocolA_Request_API.MiddleSwitch.MiddleSwitch_ArgName, MyMiddleProtocolA_Response_API.MiddleSwitch.MiddleSwitch_ArgName, MyRootProtocol_Request_API.RootSwitch.A, MyRootProtocol_Response_API.RootSwitchResp.A, protocolState, sniffer)), 
			handler(createLeafA(new HandlerContext(type, msbFirst, null, null, MyMiddleProtocolA_Request_API.MiddleSwitch.A, MyMiddleProtocolA_Response_API.MiddleSwitch.A, protocolState, sniffer)))
		  ),
		  handler(createMiddleB(new HandlerContext(type, msbFirst, MyMiddleProtocolB_Request_API.MiddleSwitch.MiddleSwitch_ArgName, MyMiddleProtocolB_Response_API.MiddleSwitch.MiddleSwitch_ArgName, MyRootProtocol_Request_API.RootSwitch.B, MyRootProtocol_Response_API.RootSwitchResp.B, protocolState, sniffer)), 
		    handler(createLeafB(new HandlerContext(type, msbFirst, null, null, MyMiddleProtocolB_Request_API.MiddleSwitch.B, MyMiddleProtocolB_Response_API.MiddleSwitch.B, protocolState, sniffer)))
		  )
		);
	}
	protected Root createRoot(IFlushable flushable) {
		return new Root(getRootContext(), flushable);
	}
	protected final HandlerContext getRootContext() {
		return new HandlerContext(type, msbFirst, MyRootProtocol_Request_API.RootSwitch.RootSwitch_ArgName, MyRootProtocol_Response_API.RootSwitchResp.RootSwitchResp_ArgName, 0, 0, protocolState, sniffer);
	}
	private Root getRoot() {
		return root;
	}
	protected Handler<?, ?> createLeafB(HandlerContext context) {
		return new DefaultMyLeafProtocolBHandler(context);
	}
	protected Handler<?, ?> createMiddleB(HandlerContext context) {
		return new DefaultMyMiddleProtocolBHandler(context);
	}
	protected Handler<?, ?> createLeafA(HandlerContext context) {
		return new DefaultMyLeafProtocolAHandler(context);
	}
	protected Handler<?, ?> createMiddleA(HandlerContext context) {
		return new DefaultMyMiddleProtocolAHandler(context);
	}

	public void receive(byte[] data) {
		root.receive(data);
	}
	public static class Root extends DefaultMyRootProtocolHandler {
		private final IFlushable flushable;

		Root(HandlerContext context, IFlushable flushable) {
			super(context);
			this.flushable = flushable;
		}
		@Override
		protected void flush(IProtocolMessage p) {
			flushable.flush(p.getData());
		}
	}
	
}
