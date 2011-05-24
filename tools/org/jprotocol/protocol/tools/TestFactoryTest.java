package org.jprotocol.protocol.tools;

import junit.framework.TestCase;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.INameValuePair;
import org.jprotocol.framework.dsl.IProtocolLayoutType;
import org.jprotocol.framework.dsl.MemoryLayoutFactory;

public class TestFactoryTest extends TestCase {
	public void testEquals() {
		MemoryLayoutFactory ref = new TestFactory();
		MemoryLayoutFactory obj = new GeneratedTestFactory();
		
		assertEquals(ref.getBlockOffset(), obj.getBlockOffset());
		assertEquals(ref.getMemoryLayout(), obj.getMemoryLayout());
		
	}
	private void assertEquals(IProtocolLayoutType ref, IProtocolLayoutType obj) {
		assertEquals(ref.getArguments(), obj.getArguments());
	}
	private void assertEquals(IArgumentType[] ref, IArgumentType[] obj) {
		assertEquals(ref.length, obj.length);
		for (int i = 0; i < ref.length; i++) {
			assertEquals(ref[i], obj[i]);
		}
	}
	private void assertEquals(IArgumentType ref, IArgumentType obj) {
		assertEquals(ref.getName(), obj.getName());
		assertEquals(ref.getEndByteIndex(), obj.getEndByteIndex());
		assertEquals(ref.getOffset(), obj.getOffset());
		assertEquals(ref.getOffsetWithinByte(), obj.getOffsetWithinByte());
		assertEquals(ref.getSizeInBits(), obj.getSizeInBits());
		assertEquals(ref.getSizeInBytes(), obj.getSizeInBytes());
		assertEquals(ref.getStartByteIndex(), obj.getStartByteIndex());
		assertEquals(ref.isAddress(), obj.isAddress());
		assertEquals(ref.isBitField(), obj.isBitField());
		assertEquals(ref.isEnumType(), obj.isEnumType());
		assertEquals(ref.isIndexedType(), obj.isIndexedType());
		assertEquals(ref.isReal(), obj.isReal());
		assertEquals(ref.isSize(), obj.isSize());
		assertEquals(ref.isStr(), obj.isStr());
		assertEquals(ref.isVirtual(), obj.isVirtual());
		if (ref.isEnumType()) {
			assertEquals(ref.getValues(), ref.getValues());
		} else if (ref.isIndexedType()) {
			assertEquals(ref.getMaxEntries(), obj.getMaxEntries());
			assertEquals(ref.getChildren(), obj.getChildren());
		} else if (ref.isReal()) {
			assertEquals(ref.getRealOffset(), obj.getRealOffset());
			assertEquals(ref.getResolution(), obj.getResolution());
			assertEquals(ref.getUnit(), obj.getUnit());
		}
	}
	
	private void assertEquals(INameValuePair[] ref, INameValuePair[] obj) {
		assertEquals(ref.length, obj.length);
		for (int i = 0; i < ref.length; i++) {
			assertEquals(ref[i], obj[i]);
		}
	}
	private void assertEquals(INameValuePair ref, INameValuePair obj) {
		assertEquals(ref.getName(), ref.getName());
		assertEquals(ref.getPayloadStartIndex(), ref.getPayloadStartIndex());
		assertEquals(ref.getValue(), ref.getValue());
		assertEquals(ref.getArgTypes(), ref.getArgTypes());
	}
}
