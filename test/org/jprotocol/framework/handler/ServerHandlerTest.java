package org.jprotocol.framework.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

import org.jprotocol.framework.handler.Handler.Type;
import org.junit.Test;
public class ServerHandlerTest extends HandlerTest {

	
	
	@Test
	public void testType() {
		assertTrue(handler.isServer());
		assertFalse(handler.isClient());
	}
	
	
	
	
	@Test 
	public void testSwitchValueStr() {
 		assertEquals("v1", handler.switchValueStrOf(upperHandler1));
		assertEquals("v2", handler.switchValueStrOf(upperHandler2));
	}
	
	@Test 
	public void testGetSwitchValues() {
		assertEquals(2, handler.getSwitchValues().size());
		assertEquals("v1", handler.getSwitchValues().get(0));
		assertEquals("v2", handler.getSwitchValues().get(1));
	}

	@Test 
	public void testSendResponse() {
		Response r = handler.createResponse();
		handler.sendResponse(r);
		verify(sniffer).sniffSend(eq(r.getProtocol()), same(handler));
		verify(lowerHandler).send(eq(r.getProtocol()), same(handler));
	}
	@Test 
	public void testSendRealtimeResponse() {
		Response r = handler.createResponse();
		handler.sendRealtimeResponse(r);
		verify(sniffer).sniffSend(eq(r.getProtocol()), same(handler));
		verify(lowerHandler).send(eq(r.getProtocol()), same(handler));
	}

	@Test 
	public void testHeaderSendValue() {
		assertEquals(HEADER_SEND_VALUE, handler.getHeaderSendValue());
	}
	
	@Override
	Type getType() {
		return Type.Server;
	}

}


