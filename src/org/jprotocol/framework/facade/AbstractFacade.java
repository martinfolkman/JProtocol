package org.jprotocol.framework.facade;

import org.jprotocol.framework.handler.IFlushable;
import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.test.ProtocolMockery;

abstract public class AbstractFacade {
	protected final IFlushable flushable;
	protected final Type type;
	AbstractFacade(IFlushable flushable, Type type) {
		this.flushable = flushable;
		this.type = type;
	}
	public boolean isOk() {
		return getErrorMessage().isEmpty();
	}
	public String getErrorMessage() {
		return getMockery().getErrorMessages();
	}

	abstract protected ProtocolMockery getMockery();
	
	/**
	 * @note This method should not be used by clients!!
	 * @param data
	 */
	abstract public void receive(byte[] data);

}
