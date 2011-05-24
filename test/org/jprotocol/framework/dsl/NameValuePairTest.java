package org.jprotocol.framework.dsl;

import junit.framework.TestCase;

import org.jprotocol.framework.dsl.IProtocolLayoutType.Direction;


public class NameValuePairTest extends TestCase {
    public void testPayload() {
        IArgumentType arg = new ArgumentType("arg1", 16, 16);
        IProtocolLayoutType payload1 = new ProtocolLayoutType("payload " + Direction.Request, "payload", Direction.Request, arg);
        INameValuePair nvp = new NameValuePair("nvp1", 0, payload1, "Prefix", 10);
        assertEquals(1, nvp.getArgTypes().length);
        assertEquals(0, nvp.getArgTypes()[0].getOffsetWithinByte());
        assertEquals(12, nvp.getArgTypes()[0].getStartByteIndex());
        assertEquals("Prefix_arg1", nvp.getArgTypes()[0].getName());
    }
    
    public void testEquality() {
    	assertEquals(new NameValuePair("v1", 0x0), new NameValuePair("v1", 0x0));
    	assertEquals(new NameValuePair("v1", 0x0, 1, createSubArg()), new NameValuePair("v1", 0x0, 1, createSubArg()));
    }
    public void testInequality() {
    	assertNotEquals(new NameValuePair("v2", 0x0), new NameValuePair("v1", 0x0));
    	assertNotEquals(new NameValuePair("v1", 0x1), new NameValuePair("v1", 0x0));
    	assertNotEquals(new NameValuePair("v1", 0x0, 1, createSubArg("a2")), new NameValuePair("v1", 0x0, 1, createSubArg("a1")));
    }

	private void assertNotEquals(NameValuePair v1, NameValuePair v2) {
		assertFalse(v1.equals(v2));
	}

	private IArgumentType createSubArg() {
		return createSubArg("a1");
	}
	private IArgumentType createSubArg(String name) {
		return new ArgumentType(name, 1, 0, new NameValuePair("v2", 0x1));
	}
}
