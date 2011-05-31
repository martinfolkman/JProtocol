package org.jprotocol.framework.handler;

import org.jprotocol.framework.dsl.IProtocolMessage;

public class ProtocolSnifferProxy implements IProtocolSniffer {
	private IProtocolSniffer target;

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