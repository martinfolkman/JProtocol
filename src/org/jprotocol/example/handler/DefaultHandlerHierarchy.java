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
	private final Handler<?, ?> root;
	public final ProtocolMockery mockery;
	public DefaultHandlerHierarchy(Type type, final IFlushable flushable) {
		this.type = type;
		this.msbFirst = false;
		this.protocolState = new ProtocolState();
		this.sniffer = new ProtocolSnifferProxy();
		this.root = new DefaultMyRootProtocolHandler(type, msbFirst, MyRootProtocol_Request_API.RootSwitch.RootSwitch_ArgName, MyRootProtocol_Response_API.RootSwitchResp.RootSwitchResp_ArgName, 0, 0, protocolState, sniffer) {
			protected void flush(IProtocolMessage p) {
				flushable.flush(p.getData());
			}
		};
		init();
		this.mockery = new ProtocolMockery(root, new NullProtocolLogger(), true);
		sniffer.init(mockery);
	}
	
	public void init() {
		root(createRoot(), 
		  handler(createMiddleA(), 
			handler(createLeafA())
		  ),
		  handler(createMiddleB(), 
		    handler(createLeafB())
		  )
		);
	}
	protected Handler<?, ?> createRoot() {
		return root;
	}
	protected Handler<?, ?> createLeafB() {
		return new DefaultMyLeafProtocolBHandler(type, msbFirst, null, null, MyMiddleProtocolB_Request_API.MiddleSwitch.B, MyMiddleProtocolB_Response_API.MiddleSwitch.B, protocolState, sniffer);
	}
	protected Handler<?, ?> createMiddleB() {
		return new DefaultMyMiddleProtocolBHandler(type, msbFirst, MyMiddleProtocolB_Request_API.MiddleSwitch.MiddleSwitch_ArgName, MyMiddleProtocolB_Response_API.MiddleSwitch.MiddleSwitch_ArgName, MyRootProtocol_Request_API.RootSwitch.B, MyRootProtocol_Response_API.RootSwitchResp.B, protocolState, sniffer);
	}
	protected Handler<?, ?> createLeafA() {
		return new DefaultMyLeafProtocolAHandler(type, msbFirst, null, null, MyMiddleProtocolA_Request_API.MiddleSwitch.A, MyMiddleProtocolA_Response_API.MiddleSwitch.A, protocolState, sniffer);
	}
	protected Handler<?, ?> createMiddleA() {
		return new DefaultMyMiddleProtocolAHandler(type, msbFirst, MyMiddleProtocolA_Request_API.MiddleSwitch.MiddleSwitch_ArgName, MyMiddleProtocolA_Response_API.MiddleSwitch.MiddleSwitch_ArgName, MyRootProtocol_Request_API.RootSwitch.A, MyRootProtocol_Response_API.RootSwitchResp.A, protocolState, sniffer);
	}

	public void receive(byte[] data) {
		root.receive(data);
	}
	
}
