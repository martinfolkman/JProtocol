package org.jprotocol.example.handler.server;

import org.jprotocol.example.api.RequestAPIFactory;
import org.jprotocol.example.api.ResponseAPIFactory;
import org.jprotocol.framework.test.Request;
import org.jprotocol.framework.test.Response;

public class ServerFacade {
	private final ServerHandlerHierarchy hierarchy;
	private final RequestAPIFactory requestFactory;
	private final ResponseAPIFactory responseFactory;

	public ServerFacade() {
		hierarchy = new ServerHandlerHierarchy();
		requestFactory = new RequestAPIFactory();
		responseFactory = new ResponseAPIFactory();
	}
	public RequestAPIFactory requests() {
		return requestFactory;
	}
	public ResponseAPIFactory responses() {
		return responseFactory;
	}
	public void expect(Request request) {
		hierarchy.mockery.expect(request.toString());
	}
	public void allow(Request request) {
		hierarchy.mockery.allow(request.toString());
	}
	public void when(Request request) {
//		hierarchy.mockery.addResponse(requestResponse, removeWhenMatched)
	}
	public void inject(Response response) {
		hierarchy.mockery.send(response.toString());
	}
	
	public boolean isOk() {
		return !getErrorMessage().isEmpty();
	}
	public String getErrorMessage() {
		return hierarchy.mockery.getErrorMessages();
	}
	
}
