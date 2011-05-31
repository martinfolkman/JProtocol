package org.jprotocol.framework.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.handler.Handler.Type;
import org.junit.Test;

public class ClientHandlerTest extends HandlerTest {

	@Test
	public void testType() {
		assertFalse(handler.isServer());
		assertTrue(handler.isClient());
	}
	@Test 
	public void testSwitchValueStr() {
 		assertEquals("r1", handler.switchValueStrOf(upperHandler1));
		assertEquals("r2", handler.switchValueStrOf(upperHandler2));
	}
	@Test 
	public void testGetSwitchValues() {
		assertEquals(2, handler.getSwitchValues().size());
		assertEquals("r1", handler.getSwitchValues().get(0));
		assertEquals("r2", handler.getSwitchValues().get(1));
	}


	@Test 
	public void testHeaderSendValue() {
		assertEquals(HEADER_RECEIVE_VALUE, handler.getHeaderSendValue());
	}
	
	@Test 
	public void testSendRequest() {
		assertTrue(handler.getLastRequest() == null);
		Request r = handler.createRequest();
		handler.sendRequest(r);
		verify(sniffer).sniffSend(eq(r.getProtocol()), same(handler));
		verify(lowerHandler).send(eq(r.getProtocol()), same(handler));
		assertSame(r, handler.getLastRequest());
	}
	@Test 
	public void testBlockingSendRequest() {
		try {
			handler.sendBlockingRequest(handler.createRequest());
		} catch (ProtocolTimeoutException e) {
			verify(sniffer).sniffSend(any(IProtocolMessage.class), same(handler));
			verify(lowerHandler).send(any(IProtocolMessage.class), same(handler));
			return;
		}
		fail();
	}

	
	@Override
	Type getType() {
		return Handler.Type.Client;
	}

}
