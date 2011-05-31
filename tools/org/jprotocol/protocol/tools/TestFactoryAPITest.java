package org.jprotocol.protocol.tools;

import static org.jprotocol.util.Contract.neverGetHere;
import junit.framework.TestCase;

import org.jprotocol.framework.dsl.StringBuilderProtocolMessage;
import org.jprotocol.protocol.tools.TestFactory_API.Indexed;
import org.jprotocol.protocol.tools.TestFactory_API.Indexed.Indexed2;
import org.jprotocol.protocol.tools.TestFactory_API.TestFactory_API_Test;

public class TestFactoryAPITest extends TestCase {
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
	}
	public void testDefaultCreation() {
		final TestFactory_API req = new TestFactory_API(new TestFactory().getMemoryLayout());
		assertEquals("TestFactory", TestFactory_API.NAME);
		assertEquals(0, req.getReal().getBitValue());
		for (Indexed indexed: req.getIndexed()) {
			assertEquals(0, indexed.getiArg0().getBitValue());
			assertTrue(indexed.getiArg1().isGood());
			assertFalse(indexed.getiArg1().isBad());
		}
		assertTrue(req.getSwitch().isOff());
		assertFalse(req.getSwitch().isOn());
		try {
			req.getSwitch().getCount().getBitValue();
		} catch (AssertionError e)  {
			return;
		}
		neverGetHere();
	}

	public void testSwitch() {
		final TestFactory_API req = new TestFactory_API(new TestFactory().getMemoryLayout());
		req.getSwitch().setOn();
		assertTrue(req.getSwitch().isOn());
		assertEquals(0, req.getSwitch().getCount().getBitValue());
		assertEquals("", req.getSwitch().getStr().getValue());
		
		req.getSwitch().getCount().setBitValue(255);
		assertEquals(255, req.getSwitch().getCount().getBitValue());
		assertEquals("", req.getSwitch().getStr().getValue());
		
		req.getSwitch().getStr().setValue("12345");
		assertEquals(255, req.getSwitch().getCount().getBitValue());
		assertEquals("12345", req.getSwitch().getStr().getValue());
		assertEquals(0, req.getReal().getBitValue());
	}
	
	public void testIndexed() {
		final TestFactory_API req = new TestFactory_API(new TestFactory().getMemoryLayout());
		int i = 0;
		Indexed indexed0 = req.getIndexed(0);
		indexed0.getiArg0().setBitValue(1);
		indexed0.getiArg1().setBad();
		assertEquals(1, indexed0.getiArg0().getBitValue());
		assertEquals(1.0, indexed0.getiArg0().getRealValue());
		assertEquals(1.0, indexed0.getiArg0().getRealQuantity().getValue());
		assertFalse(indexed0.getiArg1().isGood());
		assertTrue(indexed0.getiArg1().isBad());
		int j = 0;
		for (Indexed2 indexed2: indexed0.getIndexed2()) {
			indexed2.getiArg3().setBitValue(3);
			assertEquals(3, indexed2.getiArg3().getBitValue());
			j++;
		}
		assertEquals(3, indexed0.getIndexed2(0).getiArg3().getBitValue());
		assertEquals(2, j);
		assertEquals(j, indexed0.getNrOfIndexed2());
		for (Indexed indexed: req.getIndexed()) {
			if (i > 0) {
				assertEquals(0, indexed.getiArg0().getBitValue());
				assertTrue(indexed.getiArg1().isGood());
				assertFalse(indexed.getiArg1().isBad());
				j =  0;
				for (Indexed2 indexed2: indexed.getIndexed2()) {
					assertEquals(0, indexed2.getiArg3().getBitValue());
					j++;
				}
				assertEquals(2, j);
			}
			i++;
		}
		assertEquals(10, i);
		assertEquals(i, req.getNrOfIndexed());
	}
	
	public void testToString() {
//		final TestFactory_API req = new TestFactory_API(new TestFactory().getMemoryLayout());
//		assertEquals("TestFactory, Switch=Off, Real=0, vArg=vValue1, iArg0=0, iArg1=Good, iArg0=0, iArg1=Good, iArg0=0, iArg1=Good, iArg0=0, iArg1=Good, iArg0=0, iArg1=Good, iArg0=0, iArg1=Good, iArg0=0, iArg1=Good, iArg0=0, iArg1=Good, iArg0=0, iArg1=Good, iArg0=0, iArg1=Good", req.toString());
	}
	
	public void testTestAPI() {
		final TestFactory_API_Test req = TestFactory_API.createTest(new StringBuilderProtocolMessage(new TestFactory().getMemoryLayout()));
		assertEquals("(\"TestFactory\")", req.toString());
		req.getSwitch().setOn();
		assertEquals("(\"TestFactory\" (\"Switch\" \"On\"))", req.toString());
	}
	
	
}
