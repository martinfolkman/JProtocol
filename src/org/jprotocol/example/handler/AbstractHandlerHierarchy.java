package org.jprotocol.example.handler;

import static org.jprotocol.framework.handler.HandlerDsl.*;

import org.jprotocol.framework.handler.RegularHandler;

public abstract class AbstractHandlerHierarchy {
	private final AbstractMyRootProtocolHandler root;
	protected AbstractHandlerHierarchy(AbstractMyRootProtocolHandler root) {
		this.root = root;
	}
	public void create() {
		root(root, 
		  handler(createMiddleA(), 
			handler(createLeafA())
		  ),
		  handler(createMiddleB(), 
		    handler(createLeafB())
		  )
		);
	}
	abstract protected RegularHandler<?, ?> createLeafB();
	abstract protected RegularHandler<?, ?> createMiddleB();
	abstract protected RegularHandler<?, ?> createLeafA();
	abstract protected RegularHandler<?, ?> createMiddleA();
}
