package org.jprotocol.framework.facade;

import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.handler.IFlushable;
import org.jprotocol.framework.test.Request;
import org.jprotocol.framework.test.Response;

abstract public class AbstractClientFacade extends AbstractFacade {

	protected AbstractClientFacade(IFlushable flushable, Type type) {
		super(flushable, type);
	}
	public void expect(Response response) {
		getMockery().expect(response.toString());
	}
	public void allow(Response response) {
		getMockery().allow(response.toString());
	}
	public void send(Request request) {
		getMockery().send(request.toString());
	}
}
