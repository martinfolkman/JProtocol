package org.jprotocol.example.handler;

import static org.jprotocol.framework.handler.HandlerDsl.handler;
import static org.jprotocol.framework.handler.HandlerDsl.root;

import org.jprotocol.framework.handler.Handler;

public abstract class AbstractHandlerHierarchy {
	public void init() {
		root(createRoot(), 
		  handler(createMiddleA(), 
			handler(createLeafA())
		  ),
		  handler(createMiddleB(), 
		    handler(createLeafB())
		  )
		);
	}
	abstract protected Handler<?, ?> createRoot();
	abstract protected Handler<?, ?> createLeafB();
	abstract protected Handler<?, ?> createMiddleB();
	abstract protected Handler<?, ?> createLeafA();
	abstract protected Handler<?, ?> createMiddleA();
}
