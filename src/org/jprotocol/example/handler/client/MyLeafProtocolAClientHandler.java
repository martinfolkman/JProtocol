package org.jprotocol.example.handler.client;

import org.jprotocol.example.api.MyMiddleProtocolA_Request_API;
import org.jprotocol.example.api.MyMiddleProtocolA_Response_API;
import org.jprotocol.example.handler.AbstractMyLeafProtocolAHandler;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.IUpperHandler;

public class MyLeafProtocolAClientHandler extends AbstractMyLeafProtocolAHandler {

	public MyLeafProtocolAClientHandler(boolean msbFirst, IProtocolState protocolState, IProtocolSniffer sniffer) {
		super(Type.Client, msbFirst, null, MyMiddleProtocolA_Request_API.MiddleSwitch.A, MyMiddleProtocolA_Response_API.MiddleSwitch.A, protocolState, sniffer);
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
