package org.jprotocol.framework.dsl;

import junit.framework.TestCase;


public class MemoryLayoutTest extends TestCase {
    class MemLayoutTestFactory extends MemoryLayoutFactory {

        protected MemLayoutTestFactory() {
            super("layout");
            layout(
              argByte("a1", 0)
            );
        }
        
    }
    public void test() {
        IProtocolLayoutType layout = new MemLayoutTestFactory().getMemoryLayout(); 
        assertEquals("a1", layout.argOf("a1").getName());   
    }

}
