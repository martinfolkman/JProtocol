package org.jprotocol.framework.handler;

import org.jprotocol.framework.dsl.IProtocolLayoutFactory;
import org.jprotocol.framework.handler.Handler.Type;

public class HandlerContext {
	public final Type type;
	public final IProtocolState protocolState;
	public final IProtocolSniffer sniffer;
	public final IProtocolLayoutFactory factory;
	public HandlerContext(IProtocolLayoutFactory factory, Type type, IProtocolState protocolState, IProtocolSniffer sniffer) {
		this.factory = factory;
		this.type = type;
		this.protocolState = protocolState;
		this.sniffer = sniffer;
	}
}
