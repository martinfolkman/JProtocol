package org.jprotocol.framework.dsl;

import junit.framework.TestCase;


public class ByteAllignementProtocolTest extends TestCase {
	private ProtocolMessage p;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    IProtocolLayoutType type = new SomeTestFactory().getResponseProtocol();
	    p = new ProtocolMessage(type, false);
        p.setValue("Id", "Sense Event");
	}
	
	public void testFactory() {
        assertEquals(3, p.getData().length);
        assertEquals("Sense Event", p.getValue("Id"));
        assertEquals("0", p.getValue("Interval"));
	}
	
    public void testEnumValues() {
        p.setValue("Id", "Sense Event");
        
        p.setValue("FirstArg", "on");
        assertEquals("on", p.getValue("FirstArg"));
        assertEquals("0", p.getValue("Interval"));
        
        p.setValue("Chamber", "left");
        assertEquals("left", p.getValue("Chamber"));
        assertEquals("0", p.getValue("Interval"));

        p.setValue("Interval", "0");
        assertEquals("left", p.getValue("Chamber"));
        assertEquals("on", p.getValue("FirstArg"));
    }
    
    public void testSetIntValue() {
        p.setBitValue("Interval", 0x3ff);//All ones
        assertEquals("right", p.getValue("Chamber"));
        assertEquals("off", p.getValue("FirstArg"));
    }
    
}


class SomeTestFactory extends ProtocolLayoutFactory {
    SomeTestFactory() {
        super("SomeTestFactory", false);
        protocols(
          request(),
          response(
            args(
              arg("Id", 5, 19, 
                values(
                  value("Reserved", 0x0),
                  value("Sense Event", 0x1, 
                    args(
                      arg("FirstArg", size(1), offset(0), value("off", 0x0), value("on", 0x1)),
                      arg("Interval", size(10), offset(1)),
                      arg("Chamber", 1, 11, value("right", 0x0), value("left", 0x1)),
                      arg("Msb", 1, 18)
                    )
                  )
                )
              )
            )
          )
        );
    }
}