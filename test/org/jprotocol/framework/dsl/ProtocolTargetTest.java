package org.jprotocol.framework.dsl;

import junit.framework.TestCase;


public class ProtocolTargetTest extends TestCase {
    
    public void test() {
        
    }
    public void testProtocolWithAddressAndSize() {
        IProtocolMessage p = new ProtocolMessage(new TestFactory().getMemoryLayout(), false);
        assertEquals(2, p.getArguments().length);
        assertEquals("Address", p.getArguments()[0].getName());
        assertEquals("Size", p.getArguments()[1].getName());
        
        p.setValue("Size", "1");
        assertEquals(3, p.getArguments().length);
        assertEquals("Address", p.getArguments()[0].getName());
        assertEquals("Size", p.getArguments()[1].getName());
        assertEquals("a1", p.getArguments()[2].getName());
        assertEquals(8, p.getArguments()[2].getOffset());
        
//        assertEquals(p.getArguments()[2], p.argOf("a1"));
        assertEquals(p.getArguments()[2].getName(), p.argOf("a1").getName());
        assertEquals(p.getArguments()[2].getOffset(), p.argOf("a1").getOffset());

        p.setValue("Size", "5");
        assertEquals(4, p.getArguments().length);
        assertEquals("Address", p.getArguments()[0].getName());
        assertEquals("Size", p.getArguments()[1].getName());
        assertEquals("a1", p.getArguments()[2].getName());
        assertEquals(8, p.getArguments()[2].getOffset());
        assertEquals("a2", p.getArguments()[3].getName());
        assertEquals(2 * 8, p.getArguments()[3].getOffset());
        
        p.setValue("Address", "1");
        assertEquals(4, p.getArguments().length);
        assertEquals("Address", p.getArguments()[0].getName());
        assertEquals("Size", p.getArguments()[1].getName());
        assertEquals("a2", p.getArguments()[2].getName());
        assertEquals(1 * 8, p.getArguments()[2].getOffset());
        assertEquals("a3", p.getArguments()[3].getName());
        assertEquals(5 * 8, p.getArguments()[3].getOffset());

        assertEquals(p.getArguments()[3].getName(), p.argOf("a3").getName());
        assertEquals(p.getArguments()[3].getOffset(), p.argOf("a3").getOffset());
        
        p.setValue("Address", "7");
        assertEquals(7, p.getArguments().length);
        assertEquals("Address", p.getArguments()[0].getName());
        assertEquals("Size", p.getArguments()[1].getName());
        assertEquals("ia5", p.getArguments()[2].getName());
        assertEquals(1 * 8, p.getArguments()[2].getOffset());
        assertEquals("ia6", p.getArguments()[3].getName());
        assertEquals(2 * 8, p.getArguments()[3].getOffset());
        assertEquals("ia5", p.getArguments()[4].getName());
        assertEquals(3 * 8, p.getArguments()[4].getOffset());
        assertEquals("ia6", p.getArguments()[5].getName());
        assertEquals(4 * 8, p.getArguments()[5].getOffset());
        assertEquals("ia5", p.getArguments()[6].getName());
        assertEquals(5 * 8, p.getArguments()[6].getOffset());
        
        assertEquals(p.getArguments()[2].getName(), p.argOf("ia5").getName());
        assertEquals(p.getArguments()[2].getOffset(), p.argOf("ia5").getOffset());
    }
    
    public void testProtocolWithNoAddressAndSize() {
        IProtocolMessage p = new ProtocolMessage(new TestFactoryWithNoAddressSize().getMemoryLayout(), false);
        assertEquals(25, p.getArguments().length);
        assertEquals("ID", p.getArguments()[0].getName());
        assertEquals("a1", p.getArguments()[1].getName());
        assertEquals(1 * 8, p.getArguments()[1].getOffset());
        assertEquals("a2", p.getArguments()[2].getName());
        assertEquals(2 * 8, p.getArguments()[2].getOffset());
        assertEquals("a3", p.getArguments()[3].getName());
        assertEquals(6 * 8, p.getArguments()[3].getOffset());
        assertEquals("a4", p.getArguments()[4].getName());
        assertEquals(7 * 8, p.getArguments()[4].getOffset());
        
        for (int i = 0; i < 10; i++) {
            assertEquals("ia5", p.getArguments()[5 + i * 2].getName());
            assertEquals((8 + i) * 8, p.getArguments()[5 + i].getOffset());
            assertEquals("ia6", p.getArguments()[6 + i * 2].getName());
            assertEquals((9 + i) * 8, p.getArguments()[6 + i].getOffset());
        }        
    }
    
}

class TestFactoryWithNoAddressSize extends MemoryLayoutFactory {

    protected TestFactoryWithNoAddressSize() {
        super("Test");
        layout(
          new TargetFactory().getMemoryLayout(), offset(1),
          argByte("ID", size(1), offset(0))
        );
    }
    
}
class TestFactory extends MemoryLayoutFactory {

    protected TestFactory() {
        super("Test");
        layout(
          new TargetFactory().getMemoryLayout(), offset(1),
          address("Address", size(4), offset(0) ),
          size("Size", size(4), offset(4))
        );
    }
    
}
class TargetFactory extends MemoryLayoutFactory {

    protected TargetFactory() {
        super("Target");
        layout(
          argByte("a1", offset(0)),
          argInt("a2", offset(1)),
          argByte("a3", offset(5), value("v1", 0), value("v2", 1)),
          argByte("a4", size(1), offset(6)),
          iArg("indexed", 10, 
             argByte("ia5", 7),
             argByte("ia6", 8, value("iv1", 0x1), value("iv2", 0x2))
          )
        );
    }
    
}