package org.jprotocol.example.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ExampleTest {

	private TestAPI testApi;

	@Before
	public void before() {
		testApi = new TestAPI();
	}
	
	@Test
	public void testA() {
		testApi.server.expect(testApi.server.requests().MyLeafProtocolA_Request_API());
		testApi.client.expect(testApi.client.responses().MyLeafProtocolA_Response_API());
		testApi.client.send(testApi.client.requests().MyLeafProtocolA_Request_API());
		assertTrue(testApi.server.getErrorMessage(), testApi.server.isOk());
		assertTrue(testApi.client.getErrorMessage(), testApi.client.isOk());
	}
	@Test
	public void testB() {
		testApi.server.expect(testApi.server.requests().MyLeafProtocolB_Request_API());
		testApi.client.expect(testApi.client.responses().MyLeafProtocolB_Response_API());
		testApi.client.send(testApi.client.requests().MyLeafProtocolB_Request_API());
		assertTrue(testApi.server.getErrorMessage(), testApi.server.isOk());
		assertTrue(testApi.client.getErrorMessage(), testApi.client.isOk());
	}
}
