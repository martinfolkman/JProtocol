package org.jprotocol.framework.dsl;

import junit.framework.TestCase;


public class ArgTypeTest extends TestCase {
    public void testArgType() {
        IArgumentType arg = new ArgumentType("arg", 7, 20, new NameValuePair("Name", 1));
        assertEquals("Name", arg.nameOf(1));
        assertEquals(1, arg.valueOf("Name"));
        assertEquals(7, arg.getSizeInBits());
        assertEquals(20, arg.getOffset());
        assertEquals(1, arg.getSizeInBytes());
        assertEquals(2, arg.getStartByteIndex());
        assertEquals(2, arg.getEndByteIndex());
        assertTrue(arg.isBitField());
        assertFalse(arg.isIndexedType());
    }
    
    public void testEquality() {
    	assertEquals(createIntArg(), createIntArg());
    	assertEquals(createEnumArg1(), createEnumArg1());
    	assertFalse(createEnumArg1().equals(createEnumArg2()));
    }
    
    private IArgumentType createIntArg() {
    	return new ArgumentType("arg", 7, 20, new NameValuePair("Name", 1));
    }
    private IArgumentType createEnumArg1() {
    	return new ArgumentType("arg", 7, 20, new NameValuePair("Name", 1));
    }
    private IArgumentType createEnumArg2() {
    	return new ArgumentType("arg", 7, 20, new NameValuePair("Name", 1, 10, createIntArg()));
    }
}
