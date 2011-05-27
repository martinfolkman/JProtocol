package org.jprotocol.example.handler;

import static org.jprotocol.framework.handler.HandlerDsl.handler;
import static org.jprotocol.framework.handler.HandlerDsl.root;

import org.jprotocol.framework.handler.Handler;

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
	abstract protected Handler<?, ?> createLeafB();
	abstract protected Handler<?, ?> createMiddleB();
	abstract protected Handler<?, ?> createLeafA();
	abstract protected Handler<?, ?> createMiddleA();
}
