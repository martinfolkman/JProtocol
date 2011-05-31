package org.jprotocol.framework.dsl;

import junit.framework.TestCase;

public class EnumerationTest extends TestCase {
	public void testEquality() {
		assertEquals(new EnumerationImpl(new NameValuePair("v1", 0x0)), new EnumerationImpl(new NameValuePair("v1", 0x0)));
		assertEquals(new EnumerationImpl(new NameValuePair("v1", 0x0), new NameValuePair("v2", 0x1)), new EnumerationImpl(new NameValuePair("v1", 0x0), new NameValuePair("v2", 0x1)));
	}
	public void testInequality() {
		assertNotEquals(new EnumerationImpl(new NameValuePair("v2", 0x0)), new EnumerationImpl(new NameValuePair("v1", 0x0)));
		assertNotEquals(new EnumerationImpl(new NameValuePair("v1", 0x1)), new EnumerationImpl(new NameValuePair("v1", 0x0)));
		assertNotEquals(new EnumerationImpl(new NameValuePair("v1", 0x0), new NameValuePair("v2", 0x1)), new EnumerationImpl(new NameValuePair("v1", 0x0)));
	}
	
	public void testHashcode() {
		assertEquals(new EnumerationImpl(new NameValuePair("v1", 0x0)).hashCode(), new EnumerationImpl(new NameValuePair("v1", 0x0)).hashCode());
	}
	private void assertNotEquals(IEnumeration e1, IEnumeration e2) {
		assertFalse(e1.equals(e2));
	}
}
