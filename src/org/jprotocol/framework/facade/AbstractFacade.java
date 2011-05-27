package org.jprotocol.framework.facade;

import org.jprotocol.framework.test.ProtocolMockery;

abstract public class AbstractFacade {
	public boolean isOk() {
		return getErrorMessage().isEmpty();
	}
	public String getErrorMessage() {
		return getMockery().getErrorMessages();
	}

	abstract protected ProtocolMockery getMockery();

}
