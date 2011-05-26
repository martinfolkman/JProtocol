package org.jprotocol.example.handler.server;

import org.jprotocol.example.api.MyMiddleProtocolA_Request_API;
import org.jprotocol.example.api.MyRootProtocol_Request_API;
import org.jprotocol.example.api.MyRootProtocol_Response_API;
import org.jprotocol.example.handler.AbstractMyMiddleProtocolAHandler;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;

public class MyMiddleProtocolAServerHandler extends AbstractMyMiddleProtocolAHandler {

	public MyMiddleProtocolAServerHandler(boolean msbFirst, IProtocolState protocolState, IProtocolSniffer sniffer) {
		super(Type.Server, msbFirst, MyMiddleProtocolA_Request_API.MiddleSwitch.MiddleSwitch_ArgName, MyRootProtocol_Request_API.RootSwitch.A, MyRootProtocol_Response_API.RootSwitchResp.A, protocolState, sniffer);
	}

	@Override
	protected void flush(IProtocolMessage p) {
	}

	@Override
	protected void makeHeader(IProtocolMessage header, IProtocolMessage payload, int headerValue) {
	}

}
