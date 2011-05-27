package org.jprotocol.framework.handler;

public class Flushable implements IFlushable {
	private final IUpperHandler handler;
	public Flushable(IUpperHandler handler) {
		this.handler = handler;
	}
	@Override
	public void flush(byte[] data) {
		handler.receive(data);
	}
}
