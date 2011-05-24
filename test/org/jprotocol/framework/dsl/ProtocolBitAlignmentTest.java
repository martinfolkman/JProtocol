package org.jprotocol.framework.dsl;

import junit.framework.TestCase;


public class ProtocolBitAlignmentTest extends TestCase {
    public void test() {
//        IProtocol p = new Protocol(new BitAlignFactory().getHostToSlaveProtocol(), false);
//        assertEquals(2, p.getSize());
//        assertEquals(0, p.getValueAsNameValuePair("arg1").getValue());
//        assertEquals(0, p.getValueAsNameValuePair("arg2").getValue());
//        p.setBitValue("arg1", 256);
//        assertEquals(256, p.getValueAsNameValuePair("arg1").getValue());
//        assertEquals(0, p.getValueAsNameValuePair("arg2").getValue());
//        p.setBitValue("arg2", 2);
//        assertEquals(256, p.getValueAsNameValuePair("arg1").getValue());
//        assertEquals(2, p.getValueAsNameValuePair("arg2").getValue());
        
    }
}


class BitAlignFactory extends ProtocolLayoutFactory {

    protected BitAlignFactory() {
        super("Name", false);
        protocols(
          request(
            arg("arg1", 12, 0),
            arg("arg2", 8, 12)
          ),
          response()
        );
    }
    
}