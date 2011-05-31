package org.jprotocol.example.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ExampleTest {

	private ClientServerTestFacade o;

	@Before
	public void before() {
		o = new ClientServerTestFacade();
	}
	
	
	
	@Test
	public void successA() {
		o.server.expect(o.server.requests().MyLeafProtocolA_Request_API());
		o.client.expect(o.client.responses().MyLeafProtocolA_Response_API());
		o.client.send(o.client.requests().MyLeafProtocolA_Request_API());
		assertTrue(o.server.getErrorMessage(), o.server.isOk());
		assertTrue(o.client.getErrorMessage(), o.client.isOk());
	}
	@Test
	public void successB() {
		o.server.expect(o.server.requests().MyLeafProtocolB_Request_API());
		o.client.expect(o.client.responses().MyLeafProtocolB_Response_API());
		o.client.send(o.client.requests().MyLeafProtocolB_Request_API());
		assertTrue(o.server.getErrorMessage(), o.server.isOk());
		assertTrue(o.client.getErrorMessage(), o.client.isOk());
	}
	
	@Test
	public void protocolAHasNotBeenReceived() {
		o.server.expect(o.server.requests().MyLeafProtocolB_Request_API());
		assertFalse(o.server.getErrorMessage(), o.server.isOk());
		assertFalse(o.server.getErrorMessage(), o.server.isOk());
	}
	
	
	
	
}
