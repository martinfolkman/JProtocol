package org.jprotocol.example.handler.server;

import org.jprotocol.example.handler.AbstractHandlerHierarchy;
import org.jprotocol.example.handler.AbstractMyRootProtocolHandler;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.RegularHandler;

public class ServerHandlerHierarchy extends AbstractHandlerHierarchy {

	private final IProtocolSniffer sniffer;
	private final IProtocolState protocolState;
	private final boolean msbFirst;

	public ServerHandlerHierarchy(AbstractMyRootProtocolHandler root) {
		super(root);
		this.msbFirst = false;
		this.protocolState = null;
		this.sniffer = null;
		
	}

	@Override
	protected RegularHandler<?, ?> createLeafB() {
		return null;
//		return new MyLeafProtocolBServerHandler(msbFirst, protocolState, sniffer);
	}

	@Override
	protected RegularHandler<?, ?> createMiddleB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RegularHandler<?, ?> createLeafA() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RegularHandler<?, ?> createMiddleA() {
		// TODO Auto-generated method stub
		return null;
	}

}
