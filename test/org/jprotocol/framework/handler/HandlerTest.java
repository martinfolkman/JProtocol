package org.jprotocol.framework.handler;

import static org.jprotocol.quantity.Quantity.quantity;
import static org.jprotocol.quantity.Unit.ms;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jprotocol.framework.dsl.AbstractDecoratedProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutFactory;
import org.jprotocol.framework.dsl.ProtocolLayoutFactory;
import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.handler.IProtocolSniffer.InhibitException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.jprotocol.quantity.Quantity;
import org.jprotocol.util.Contract.ContractError;


abstract public class HandlerTest {
	protected static final int HEADER_SEND_VALUE = 1;
	protected static final int HEADER_RECEIVE_VALUE = 0;
	protected static final String HEADER_FIELD_NAME = "a1";
	protected static final String PROTOCOL_NAME = "Protocol";
	
	@Mock protected IProtocolState protocolState;
	@Mock protected IProtocolSniffer sniffer;
	@Mock protected IUpperHandler upperHandler1;
	@Mock protected IUpperHandler upperHandler2;
	@Mock protected ILowerHandler lowerHandler;
	protected IProtocolLayoutFactory factory;
	protected RegularTestHandler handler;
	
	@Before
	public void before() {
		factory = new HandlerTestFactory(PROTOCOL_NAME);
		MockitoAnnotations.initMocks(this);
		when(lowerHandler.getQualifiedName()).thenReturn(new QualifiedName("//LH"));
		when(upperHandler2.getHeaderSendValue()).thenReturn(1);
		handler = createHandler();

		when(lowerHandler.switchValueStrOf(handler)).thenReturn("Switch");
		configHandlers();
	}
	private void configHandlers() {
		handler.register(0, upperHandler1);
		handler.register(1, upperHandler2);
		handler.setLowerHandler(lowerHandler);
	}
	private RegularTestHandler createHandler() {
		return new RegularTestHandler(factory, getType(), false, HEADER_FIELD_NAME, HEADER_RECEIVE_VALUE, HEADER_SEND_VALUE, protocolState, sniffer);
	}
	
	@Test
	public void testUpperHandlerConfig() {
		assertEquals(2, handler.getUpperHandlers().length);
		assertSame(upperHandler1, handler.getUpperHandlers()[0]);
		assertSame(upperHandler2, handler.getUpperHandlers()[1]);
	}
	@Test
	public void testLowerHandlerConfig() {
		assertSame(lowerHandler, handler.lowerHandler);
	}
	
	@Test
	public void testHeaderFieldName() {
		assertSame(HEADER_FIELD_NAME, handler.getHeaderFieldName());
	}

	@Test
	public void testFactory() {
		assertSame(factory, handler.getFactory());
	}
	@Test
	public void testProtocolName() {
		assertEquals(PROTOCOL_NAME, handler.getProtocolName());
	}

	@Test
	public void testQualifiedName() {
		assertEquals(new QualifiedName("//LH//Switch"), handler.getQualifiedName());
	}

	@Test 
	public void testResetState() {
		handler.resetState();
		verify(upperHandler1).resetState();
		verify(upperHandler2).resetState();
	}
	@Test 
	public void testProtocolState() {
		assertSame(protocolState, handler.getProtocolState());
	}
	@Test 
	public void testActive() {
		assertTrue(handler.isActive());
		handler.inactivate();
		assertFalse(handler.isActive());
		handler.receive(new byte[]{0});
		handler.activate();
		assertTrue(handler.isActive());
	}	
	@Test 
	public void testToString() {
		assertEquals("Switch", handler.toString());
	}
	@Test 
	public void testSwitchValue() {
		assertEquals(0, handler.switchValueOf(upperHandler1).intValue());
		assertEquals(1, handler.switchValueOf(upperHandler2).intValue());
	}
	@Test 
	public void testSendPayloadFromUpperHandler1() {
		IProtocolMessage payload = mock(IProtocolMessage.class);
		when(payload.getData()).thenReturn(new byte[]{0, 1});
		handler.send(payload, upperHandler1);
		verify(sniffer).sniffSend(any(IProtocolMessage.class), same(handler));
		verify(lowerHandler).send(any(IProtocolMessage.class), same(handler));
		assertEquals(0, handler.headerValue);
	}	
	@Test 
	public void testSendPayloadFromUpperHandler2() {
		IProtocolMessage payload = mock(IProtocolMessage.class);
		when(payload.getData()).thenReturn(new byte[]{0, 1});
		handler.send(payload, upperHandler2);
		verify(sniffer).sniffSend(any(IProtocolMessage.class), same(handler));
		verify(lowerHandler).send(any(IProtocolMessage.class), same(handler));
		assertEquals(1, handler.headerValue);
	}	
	@Test 
	public void testReceiveUpperHandler1Request() throws InhibitException {
		handler.receive(new byte[]{0, 0x10});
		verify(sniffer).sniff(any(IProtocolMessage.class), same(handler));
		verify(upperHandler1).receive(new byte[]{0x10});
	}
	@Test 
	public void testReceiveUpperHandler2Request() throws InhibitException {
		handler.receive(new byte[]{1, 0x11});
		verify(sniffer, times(1)).sniff(any(IProtocolMessage.class), same(handler));
		verify(upperHandler2).receive(new byte[]{0x11});
	}
	@Test 
	public void testReceiveUnsupportedRequest() throws InhibitException {
		try {
			handler.receive(new byte[]{2, 0x11});
		} catch (UnsupportedProtocol e) {
			verify(sniffer, times(1)).sniff(any(IProtocolMessage.class), same(handler));
			return;
		}
		fail();
	}
	@Test 
	public void testIllegalRegister() {
		try {
			handler.register(10, mock(IUpperHandler.class));
		} catch (ContractError e) {
			return;
		}
		fail();
	}
	
	abstract Type getType();
}

class RegularTestHandler extends RegularHandler<Request, Response> {

	int headerValue;

	protected RegularTestHandler(
			IProtocolLayoutFactory factory,
			Handler.Type type,
			boolean msbFirst, 
			String upperHandlerFieldName,
			int headerReceiveValue, 
			int headerSendValue,
			IProtocolState protocolState,
			IProtocolSniffer sniffer) {
		super(factory, type, msbFirst, upperHandlerFieldName, headerReceiveValue, headerSendValue, protocolState, sniffer);
	}

	@Override
	public Request createRequest(IProtocolMessage p) {
		return new Request(p);
	}

	@Override
	public Response createResponse(IProtocolMessage p) {
		return new Response(p);
	}

	@Override
	protected void makeHeader(IProtocolMessage header, IProtocolMessage payload, int headerValue) {
		this.headerValue = headerValue;
		
	}
	@Override
	protected Quantity getTimeout() {
		return quantity(1, ms);
	}
}

class Request extends AbstractDecoratedProtocolMessage {

	protected Request(IProtocolMessage protocol) {
		super(protocol);
		// TODO Auto-generated constructor stub
	}
}
class Response extends AbstractDecoratedProtocolMessage {

	protected Response(IProtocolMessage protocol) {
		super(protocol);
		// TODO Auto-generated constructor stub
	}
}

class HandlerTestFactory extends ProtocolLayoutFactory {

	HandlerTestFactory(String name) {
		super(name, false);
		protocols(
		   request(
			  argByte(ServerHandlerTest.HEADER_FIELD_NAME, offset(0), 
				values(
				   value("v1", 0),
				   value("v2", 1),
				   value("v3", 2)
				)
			  )
		   ),
		   response(
			  argByte(ServerHandlerTest.HEADER_FIELD_NAME, offset(0), 
				values(
				   value("r1", 0),
				   value("r2", 1),
				   value("r3", 2)
				)
			  )
		   )
		);
	}
	
}
