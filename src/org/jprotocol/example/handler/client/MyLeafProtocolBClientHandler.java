package org.jprotocol.example.handler.client;

import org.jprotocol.example.api.MyMiddleProtocolB_Request_API;
import org.jprotocol.example.api.MyMiddleProtocolB_Response_API;
import org.jprotocol.example.handler.AbstractMyLeafProtocolBHandler;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.IUpperHandler;

public class MyLeafProtocolBClientHandler extends AbstractMyLeafProtocolBHandler {

	public MyLeafProtocolBClientHandler(boolean msbFirst, IProtocolState protocolState, IProtocolSniffer sniffer) {
		super(Type.Client, msbFirst, null, MyMiddleProtocolB_Request_API.MiddleSwitch.B, MyMiddleProtocolB_Response_API.MiddleSwitch.B, protocolState, sniffer);
	}

	@Override
	protected void flush(IProtocolMessage p) {
	}

	@Override
	protected void makeHeader(IProtocolMessage header, IProtocolMessage payload, int headerValue) {
	}

	@Override
    protected void notifyUpperHandler(IProtocolMessage p, IUpperHandler uh) {
	}
}
