package org.jprotocol.framework.facade;

import org.jprotocol.framework.test.Request;
import org.jprotocol.framework.test.Response;

abstract public class AbstractClientFacade extends AbstractFacade {

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
