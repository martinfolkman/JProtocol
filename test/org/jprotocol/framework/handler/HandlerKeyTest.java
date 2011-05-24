package org.jprotocol.framework.handler;

import junit.framework.TestCase;


public class HandlerKeyTest extends TestCase {
    public void testEquality() {
        assertEquals(new HandlerKey("Name1", "c1", "c2"), new HandlerKey("Name1", "c1", "c2")); 
        assertFalse(new HandlerKey("Name2", "c1", "c2").equals(new HandlerKey("Name1", "c1", "c2"))); 
        assertFalse(new HandlerKey("Name1", "c3", "c2").equals(new HandlerKey("Name1", "c1", "c2"))); 
        assertFalse(new HandlerKey("Name1", "c1", "c5").equals(new HandlerKey("Name1", "c1", "c2"))); 
        assertFalse(new HandlerKey("Name1", "c1", "c2").equals(new HandlerKey("Name1", "c1"))); 
    }
    public void testHashCode() {
        assertEquals(new HandlerKey("Name1", "c1", "c2").hashCode(), new HandlerKey("Name1", "c1", "c2").hashCode()); 
        assertTrue(new HandlerKey("Name2", "c1", "c2").hashCode() != new HandlerKey("Name1", "c1", "c2").hashCode()); 
        assertTrue(new HandlerKey("Name1", "c3", "c2").hashCode() != new HandlerKey("Name1", "c1", "c2").hashCode()); 
        assertTrue(new HandlerKey("Name1", "c1", "c5").hashCode() != new HandlerKey("Name1", "c1", "c2").hashCode()); 
        assertTrue(new HandlerKey("Name1", "c1", "c2").hashCode() != new HandlerKey("Name1", "c1").hashCode()); 
    }
}
