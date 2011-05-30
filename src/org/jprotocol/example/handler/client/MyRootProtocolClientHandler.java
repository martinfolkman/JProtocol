package org.jprotocol.example.handler.client;

import org.jprotocol.example.api.MyRootProtocol_Response_API;
import org.jprotocol.example.handler.AbstractMyRootProtocolHandler;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.IFlushable;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;

public class MyRootProtocolClientHandler extends AbstractMyRootProtocolHandler {

	private final IFlushable flushable;

	protected MyRootProtocolClientHandler(IFlushable flushable, boolean msbFirst, IProtocolState protocolState, IProtocolSniffer sniffer) {
		super(Type.Client, msbFirst, MyRootProtocol_Response_API.RootSwitchResp.RootSwitchResp_ArgName, 0, 0, protocolState, sniffer);
		this.flushable = flushable;
	}

	@Override
	protected void flush(IProtocolMessage p) {
		flushable.flush(p.getData());
	}

	@Override
	protected void makeHeader(IProtocolMessage header, IProtocolMessage payload, int headerValue) {
		createRequest(header).getRootSwitch().setBitValue(headerValue);
	}

}
