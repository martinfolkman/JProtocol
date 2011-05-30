package org.jprotocol.example.handler.server;

import org.jprotocol.example.api.MyLeafProtocolB_Request_API;
import org.jprotocol.example.api.MyLeafProtocolB_Response_API;
import org.jprotocol.example.api.MyMiddleProtocolB_Request_API;
import org.jprotocol.example.api.MyMiddleProtocolB_Response_API;
import org.jprotocol.example.handler.AbstractMyLeafProtocolBHandler;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;

public class MyLeafProtocolBServerHandler extends AbstractMyLeafProtocolBHandler {

	public MyLeafProtocolBServerHandler(boolean msbFirst, IProtocolState protocolState, IProtocolSniffer sniffer) {
		super(Type.Server, msbFirst, null, MyMiddleProtocolB_Request_API.MiddleSwitch.B, MyMiddleProtocolB_Response_API.MiddleSwitch.B, protocolState, sniffer);
	}

	@Override
	protected void flush(IProtocolMessage p) {
	}

	@Override
	protected void makeHeader(IProtocolMessage header, IProtocolMessage payload, int headerValue) {
	}
    protected void unsupportedProtocol(IProtocolMessage p) {
        //
    }

    @Override
    protected void receiveRequest(MyLeafProtocolB_Request_API request, MyLeafProtocolB_Response_API response) {
		if (response == null) {
			response = createResponse();
		}
		sendResponse(response);
    }
    
}
