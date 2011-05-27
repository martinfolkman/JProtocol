package org.jprotocol.example.handler.server;

import org.jprotocol.example.handler.AbstractHandlerHierarchy;
import org.jprotocol.example.handler.AbstractMyRootProtocolHandler;
import org.jprotocol.framework.handler.Handler;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.ProtocolState;

public class ServerHandlerHierarchy extends AbstractHandlerHierarchy {

	private final IProtocolSniffer sniffer;
	private final IProtocolState protocolState;
	private final boolean msbFirst;

	public ServerHandlerHierarchy(AbstractMyRootProtocolHandler root) {
		super(root);
		this.msbFirst = false;
		this.protocolState = new ProtocolState();
		this.sniffer = null;
	}

	@Override
	protected Handler<?, ?> createLeafB() {
		return new MyLeafProtocolBServerHandler(msbFirst, protocolState, sniffer);
	}

	@Override
	protected Handler<?, ?> createMiddleB() {
		return new MyMiddleProtocolBServerHandler(msbFirst, protocolState, sniffer);
	}

	@Override
	protected Handler<?, ?> createLeafA() {
		return new MyLeafProtocolAServerHandler(msbFirst, protocolState, sniffer);
	}

	@Override
	protected Handler<?, ?> createMiddleA() {
		return new MyMiddleProtocolAServerHandler(msbFirst, protocolState, sniffer);
	}

}
