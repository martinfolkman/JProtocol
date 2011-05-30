package org.jprotocol.example.handler.server;

import org.jprotocol.example.api.MyRootProtocol_Request_API;
import org.jprotocol.example.handler.AbstractMyRootProtocolHandler;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.IFlushable;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;

public class MyRootProtocolServerHandler extends AbstractMyRootProtocolHandler {

	private final IFlushable flushable;

	protected MyRootProtocolServerHandler(IFlushable flushable, boolean msbFirst, IProtocolState protocolState, IProtocolSniffer sniffer) {
		super(Type.Server, msbFirst, MyRootProtocol_Request_API.RootSwitch.RootSwitch_ArgName, 0, 0, protocolState, sniffer);
		this.flushable = flushable;
	}

	@Override
	protected void flush(IProtocolMessage p) {
		flushable.flush(p.getData());
	}

	@Override
	protected void makeHeader(IProtocolMessage header, IProtocolMessage payload, int headerValue) {
		createResponse(header).getRootSwitchResp().setBitValue(headerValue);
	}

}
