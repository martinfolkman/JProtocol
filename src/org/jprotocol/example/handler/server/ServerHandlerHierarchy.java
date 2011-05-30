package org.jprotocol.example.handler.server;

import org.jprotocol.example.handler.AbstractHandlerHierarchy;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.Handler;
import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.handler.IFlushable;
import org.jprotocol.framework.handler.IHandler;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.ProtocolState;
import org.jprotocol.framework.test.ProtocolMockery;
import org.jprotocol.framework.test.IProtocolLogger.NullProtocolLogger;

public class ServerHandlerHierarchy extends AbstractHandlerHierarchy {

	public final ProtocolMockery mockery;
	private final IProtocolState protocolState;
	private final boolean msbFirst;
	private final MyRootProtocolServerHandler root;
	private final Sniffer sniffer;

	public ServerHandlerHierarchy(IFlushable flushable) {
		this.msbFirst = false;
		this.protocolState = new ProtocolState();
		this.sniffer = new Sniffer();
		this.root = new MyRootProtocolServerHandler(flushable, msbFirst, protocolState, sniffer);
		init();
		this.mockery = new ProtocolMockery(root, new NullProtocolLogger(), true);
		sniffer.init(mockery);
	}
	@Override
	protected Handler<?, ?> createRoot() {
		return root;
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
	public void receive(byte[] data) {
		root.receive(data);
	}
	@Override
	protected Type getType() {
		return Type.Server;
	}


}
final class Sniffer implements IProtocolSniffer {
	private IProtocolSniffer target;

	public Sniffer() {
	}

	public void init(IProtocolSniffer sniffer) {
		this.target = sniffer;
	}

	@Override
	public IProtocolMessage sniff(IProtocolMessage protocol, IHandler handler) throws InhibitException {
		return target.sniff(protocol, handler);
	}

	@Override
	public void sniffSend(IProtocolMessage protocol, IHandler handler) {
		target.sniffSend(protocol, handler);
	}
	
}