package org.jprotocol.framework.handler;

public interface IFlushable {

	void flush(byte[] data);

	
	public static class NullFlushable implements IFlushable {
		@Override
		public void flush(byte[] data) {
		}
	}
}
