package org.jprotocol.example.handler.server;

import org.jprotocol.example.api.MyRootProtocol_Request_API;
import org.jprotocol.example.api.MyRootProtocol_Response_API;
import org.jprotocol.example.handler.AbstractMyRootProtocolHandler;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;

public class MyRootProtocolServerHandler extends AbstractMyRootProtocolHandler {

	protected MyRootProtocolServerHandler(boolean msbFirst, IProtocolState protocolState, IProtocolSniffer sniffer) {
		super(Type.Server, msbFirst, MyRootProtocol_Request_API.RootSwitch.RootSwitch_ArgName, 0, 0, protocolState, sniffer);
	}

	@Override
	protected void flush(IProtocolMessage p) {
	}

	@Override
	protected void makeHeader(IProtocolMessage header, IProtocolMessage payload, int headerValue) {
		MyRootProtocol_Response_API response = createResponse(header);
		//TODO
	}

}
