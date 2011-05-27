package org.jprotocol.framework.facade;

import org.jprotocol.framework.test.Request;
import org.jprotocol.framework.test.Response;

abstract public class AbstractServerFacade extends AbstractFacade {

	public void expect(Request request) {
		getMockery().expect(request.toString());
	}
	public void allow(Request request) {
		getMockery().allow(request.toString());
	}
	public void when(Request request) {
//		hierarchy.mockery.addResponse(requestResponse, removeWhenMatched)
	}
	public void send(Response response) {
		getMockery().send(response.toString());
	}
}
