package org.jprotocol.framework.dsl;

import junit.framework.TestCase;

import org.jprotocol.quantity.Quantity;
import org.jprotocol.quantity.Unit;

public class DynamicArgTest extends TestCase {
	public void test() {
//		IProtocol p = new Protocol(new DynLayout().getMemoryLayout(), false);
//		assertEquals("", p.getValue("Size"));
//		assertEquals(0, p.noOfEntriesOf(new ArgTypeOfIter("Data", p.getProtocolType()).foundArgType));
//		assertEquals(1, p.argOf("Footer").getOffset() / 8);
//		
//		p.setValue("Size", "1");
//		assertEquals(1, p.noOfEntriesOf(new ArgTypeOfIter("Data", p.getProtocolType()).foundArgType));
//		assertEquals(3, p.argOf("Footer").getOffset() / 8);
//		p.setValue("Size", "2");
//		assertEquals(2, p.noOfEntriesOf(new ArgTypeOfIter("Data", p.getProtocolType()).foundArgType));
//		assertEquals(5, p.argOf("Footer").getOffset() / 8);
	}
}


//class DynLayout extends MemoryLayoutFactory {
//
//	protected DynLayout() {
//		super("Dyn");
//		layout(
//		  argByte("Size", size(1), offset(0)),
//		  iArg("Data", new A(), 
//			argByte("ia0", size(1), offset(1)),
//			argByte("ia1,", size(1), offset(2))
//		  ),
//		  argByte("Footer", size(1), new B())
//		);
//	}
//	
//}
class A implements IDynamicCalcStrategy {
	@Override
	public Quantity calc(IArgumentType a, IProtocolMessage p) {
		return Quantity.quantity(p.getValueAsNameValuePair("Size").getValue(), Unit.byteSize);
	}
	
}
class B implements IDynamicCalcStrategy {

	@Override
	public Quantity calc(IArgumentType a, IProtocolMessage p) {
		return Quantity.quantity(p.getValueAsNameValuePair("Size").getValue() * 2 + 1, Unit.byteSize);
	}
	
}
