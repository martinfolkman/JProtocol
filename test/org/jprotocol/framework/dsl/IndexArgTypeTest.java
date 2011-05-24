package org.jprotocol.framework.dsl;

import junit.framework.TestCase;


public class IndexArgTypeTest extends TestCase {
    public void testIndexArg() {
        IndexArgumentType arg = new IndexArgumentType("indexArg", 50, 
                                            new ArgumentType("arg", 8, 0),
                                            new IndexArgumentType("arg", 100, new ArgumentType("arg", 8, 8)));
        assertTrue(arg.isIndexedType());
        
        assertEquals(5050, arg.getSizeInBytes());
        assertEquals(0, arg.getOffsetWithinByte());
        assertEquals(5049, arg.getEndByteIndex());
    }
}
