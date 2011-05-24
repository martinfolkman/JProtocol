package org.jprotocol.framework.api;

import org.jprotocol.framework.dsl.AbstractDecoratedProtocolMessage;

public interface APICommand<T extends AbstractDecoratedProtocolMessage> {
	T execute(T target);
}
