package org.jprotocol.example.handler.server;

import org.jprotocol.example.api.MyMiddleProtocolB_Request_API;
import org.jprotocol.example.api.MyRootProtocol_Request_API;
import org.jprotocol.example.api.MyRootProtocol_Response_API;
import org.jprotocol.example.handler.AbstractMyMiddleProtocolBHandler;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;

public class MyMiddleProtocolBServerHandler extends AbstractMyMiddleProtocolBHandler {

	public MyMiddleProtocolBServerHandler(boolean msbFirst, IProtocolState protocolState, IProtocolSniffer sniffer) {
		super(Type.Server, msbFirst, MyMiddleProtocolB_Request_API.MiddleSwitch.MiddleSwitch_ArgName, MyRootProtocol_Request_API.RootSwitch.B, MyRootProtocol_Response_API.RootSwitchResp.B, protocolState, sniffer);
	}

	@Override
	protected void flush(IProtocolMessage p) {
	}

	@Override
	protected void makeHeader(IProtocolMessage header, IProtocolMessage payload, int headerValue) {
	}

}
