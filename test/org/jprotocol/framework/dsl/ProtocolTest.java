package org.jprotocol.framework.dsl;

import static org.jprotocol.quantity.Quantity.quantity;
import static org.jprotocol.util.Contract.neverGetHere;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.ProtocolState;

import org.jprotocol.quantity.Unit;
import org.jprotocol.util.Contract.ContractError;


class TestDBTFactory extends ProtocolLayoutFactory {
    TestDBTFactory() {
        super("DataBlock", true);
        protocols(
          request(),
          response( 
            args(
              arg("arg0", 8, 0, 
                values(
                  value("arg0V0", 0, 
                    args(
                      arg("condArg", 8, 300 * 8)
                    )
                  ),
                  value("arg0V1", 2)
                )
              ),
              arg("arg1", 4, 8,
                values(
                  value("arg1V0", 0),
                  value("arg1V1", 3)
                )
              ),
              arg("arg2", 3, 12),
              arg("arg3", 24, 16),
              iArg("iArg", 100,
                args(
                  arg("ia0", 8, 40),
                  arg("ia1", 8, 48)
                )
              )
            )
          )
        );
    }
}

public class ProtocolTest extends TestCase {
    IProtocolLayoutType protocolType;
    public void setUp() {
        protocolType = new TestDBTFactory().getResponseProtocol();
    }
 
    
    
    
    
    class VarSizeProtocol extends ProtocolLayoutFactory {

        VarSizeProtocol() {
            super("Var", true);
            protocols(
              request(),
              response(
                args(
                  argByte("a1", 1, 0),
                  iArg("iArg", 4, 
                    argByte("index", 1, 1),
                    argByte("marker", 4, 2)
                  )
                )
              )
            );
        }
        
    }
     
    public void testFailedVariableSizeProtocol() {
//        try {
//            IProtocolType type = new VarSizeProtocol().getSlaveToHostProtocol();
//            IProtocol p = new Protocol(type, false, new byte[]{0, 1, 0, 0});
//        } catch (Exception e) {
//            return;
//        }
//        neverGetHere();
//        
    }
    
    class ValuePrefixTestFactory extends ProtocolLayoutFactory {

        protected ValuePrefixTestFactory() {
            super("name", false);
            protocols(
              request(
                args(
                  argByte("a1", 0, 
                    values(
                      valuePrefix("v1", 0x0, 
                        args(
                          argByte("a2", 2),
                          argByte("a3", 3)
                        )
                      )
                    )
                  )
                )
              ), 
              response());
        }
        
    }
    
    public void testValuePrefix() {
        IProtocolMessage p = new ProtocolMessage(new ValuePrefixTestFactory().getRequestProtocol(), false);
        assertTrue(p.argOf("a1") != null);
//        assertTrue(p.argOf("a2") == null);
//        assertTrue(p.argOf("a3") == null);
        assertTrue(p.argOf("v1_a2") != null);
        assertTrue(p.argOf("v1_a3") != null);
        
    }
    
    
    public void testVariableSizeProtocol() {
        IProtocolLayoutType type = new VarSizeProtocol().getResponseProtocol();
        IProtocolMessage p;
        
        p = new ProtocolMessage(type, false, new byte[]{0, 1, 0, 0, 2, 0});
        assertEquals(3, p.getArguments().length);
        assertEquals(1, indexOf(p, 0));
        assertEquals(0x20000, markerOf(p, 0));
        assertEquals(21, type.getSizeInBytes());

        p = new ProtocolMessage(type, false, new byte[]{0, 1, 0, 0, 2, 1, 0, 0, 2, 0, 1});
        assertEquals(5, p.getArguments().length);
        assertEquals(1, indexOf(p, 0));
        assertEquals(0x1020000, markerOf(p, 0));
        assertEquals(0, indexOf(p, 1));
        assertEquals(0x1000200, markerOf(p, 1));
        assertEquals(21, type.getSizeInBytes());

        p = new ProtocolMessage(type, false, new byte[]{0, 1, 0, 0, 2, 1, 0, 0, 2, 1, 0, 0, 2, 3, 4, 5});
        assertEquals(7, p.getArguments().length);
        assertEquals(0, indexOf(p, 2));
        assertEquals(0x5040302, markerOf(p, 2));
        assertEquals(21, type.getSizeInBytes());
        
        p = new ProtocolMessage(type, false, new byte[]{0, 1, 0, 0, 2, 1, 0, 0, 2, 1, 0, 0, 2, 3, 4, 5, 3, 1, 2, 3, 4});
        assertEquals(3, indexOf(p, 3));
        assertEquals(0x4030201, markerOf(p, 3));
        assertEquals(9, p.getArguments().length);
        assertEquals(21, type.getSizeInBytes());
    }
    
    class DynamicSizeProtocol extends ProtocolLayoutFactory {

        protected DynamicSizeProtocol() {
            super("Dyn", false);
            protocols(
              request(
                args(
                  argByte("a1", offset(0),
                    values(
                      value("v0", 0),
                      value("v1", 1, 
                        args(
                          argByte("a2", offset(3),
                            values(
                              value("v0", 0),
                              value("v1", 1, 
                                args(
                                  argByte("a3", offset(10))
                                )
                              )
                            )
                          )
                        )
                      )
                    )
                  )
                )
              ),
              response()
            );
        }
        
    }
    public void testDynamicSizeProtocolSize() {
        IProtocolLayoutType type = new DynamicSizeProtocol().getRequestProtocol();
        IProtocolMessage p = new ProtocolMessage(type, false);
        assertEquals(11, p.getSize());
        p.setValue("a1", "v1");
        assertEquals(11, p.getSize());
        p.setValue("a2", "v1");
        assertEquals(11, p.getSize());
        p.setValue("a1", "v0");
        assertEquals(11, p.getSize());
        p.adjustSize();
        assertEquals(1, p.getSize());
        
        p = new ProtocolMessage(type, false);
        p.setValue("a1", "v1");
        p.adjustSize();
        assertEquals(4, p.getSize());
    }
    
    private static int indexOf(IProtocolMessage p, int index) {
        return p.getValueAsNameValuePair("index", index).getValue();
    }
    private static int markerOf(IProtocolMessage p, int index) {
        return p.getValueAsNameValuePair("marker", index).getValue();
    }
    
    class ResolutionFactory extends ProtocolLayoutFactory {

        protected ResolutionFactory() {
            super("Res", false);
            protocols(
              request(
                args(
                  arg("arg1", 8, 0, .5, .75, Unit.noUnit),
                  arg("arg2", 8, 8, value("v1", 0), value("v2", 1))
                )
              ),
              response()
            );
        }
        
    }
    public void testRealOffsetAndResolutionAndUnit() {
        IProtocolMessage p = new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false);
        assertEquals(0.5, p.getRealQuantity("arg1").getValue());
        assertEquals(0.5, p.getRealValue("arg1"));
    }
    
    public void testRealAsString() {
        IProtocolMessage p = new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false);
        assertEquals("0.5", p.getRealValueAsString("arg1"));
        assertEquals("v1", p.getRealValueAsString("arg2"));
        p.setRealValueAsString("arg1", "1.25");
        assertEquals("1.25", p.getRealValueAsString("arg1"));
        assertEquals(1, p.getValueAsNameValuePair("arg1").getValue());
        p.setRealValueAsString("arg2", "v2");
        assertEquals("v2", p.getRealValueAsString("arg2"));
    }
    
    
    public void testData() {
    	IProtocolMessage p = new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false);
    	assertEquals(new byte[]{0, 0}, p.getData());
    	p.setValue("arg2", "v2");
    	assertEquals(new byte[]{0, 1}, p.getData());
    	assertEquals(new byte[]{1}, p.getData(1));
    	
    	p = new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false, 0);
    	assertEquals(new byte[]{0}, p.getData());
    	p.setData(1, 1);
    	assertEquals("v2", p.getValue("arg2"));
    	assertEquals(new byte[]{0, 1}, p.getData());
    }
    public void testReadableData() {
    	IProtocolMessage p = new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false);
    	assertEquals("0,0", p.readableData());
    	p.setValue("arg2", "v2");
    	assertEquals("0,1", p.readableData());
    }

    public void testIllegalByteArrayValue() {
    	IProtocolMessage p = new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false, 0, 2);
    	try {
    		p.getValue("arg2");
    	} catch (IllegalByteArrayValue e) {
    		assertEquals("Protocol: Res Request Illegal value: 0x2 for arg: arg2 can't be mapped to a name, offset: 1", e.getMessage());
    		return;
    	}
    	neverGetHere();
    }
    public void testIndexOutOfBounds() {
    	IProtocolMessage p = new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false, 0);
    	try {
    		p.getValue("arg2");
    	} catch (IllegalByteArrayValue e) {
    		assertEquals("Index out of bounds for: arg2 in Res Request. Actual size: 1, expected size: 2", e.getMessage());
    		return;
    	}
    	neverGetHere();
    }

    public void testHeaderIndex() {
    	IProtocolMessage p = new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false);
    	assertEquals(1, p.getHeaderEndIndex());
    	p = new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false, 0);
    	assertEquals(1, p.getHeaderEndIndex());
    }
    public void testToString() {
    	assertEquals("Res Request", new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false).toString());
    }
    
    public void testIsValidArg() {
    	IProtocolMessage p = new ProtocolMessage(new ResolutionFactory().getRequestProtocol(), false);
    	assertTrue(p.isValid("arg1"));
    	assertTrue(p.isValid("arg2"));
//    	assertFalse(p.isValid("arg10"));
    }
    
    
    void assertEquals(byte[] b1, byte[] b2) {
    	assertEquals(b1.length, b2.length);
    	
    }

    
    public void testSetValueInBits() {
        IProtocolMessage p = new ProtocolMessage(protocolType, false);
        assertEquals("arg1V0", p.getValue("arg1"));
        p.setBitValue("arg1", 3);
        assertEquals("arg1V1", p.getValue("arg1"));
    }
    
    public void testWrongArg() {
        try {
            IProtocolMessage p = new ProtocolMessage(protocolType, false);
            p.getValue("Wrong");
            p.setValue("Wrong", "");
        } catch (ContractError e) {
            return;
        }
        neverGetHere();
    }
    public void testWrongValue() {
        try {
            IProtocolMessage p = new ProtocolMessage(protocolType, false);
            p.setValue("arg1", "Wrong");
        } catch (ContractError e) {
            return;
        }
        neverGetHere();
    }
    
    public void testConditionalArg() {
        IProtocolMessage db = new ProtocolMessage(protocolType, false);
        db.setValue("arg0", "arg0V1");
        assertEquals(204, db.getArguments().length);
        db.setValue("arg0", "arg0V0");
        assertEquals(205, db.getArguments().length);
        assertEquals("0", db.getValue("condArg"));
        db.setValue("condArg", "1");
        assertEquals("1", db.getValue("condArg"));
    }
    
    public void testSizeInBits() {
        assertEquals(2408, protocolType.getSizeInBits());
    }
    
    public void testSizeInBytes() {
        assertEquals(301, protocolType.getSizeInBytes());
    }
    
    public void testCreationWithDefaultValues() {
        IProtocolMessage db = new ProtocolMessage(protocolType, false);
        assertEquals("arg0V0", db.getValue("arg0"));
        assertEquals("arg1V0", db.getValue("arg1"));
        assertEquals("0", db.getValue("arg2"));
    }
    
    private byte[] createByteArray() {
        List<Byte> a = new ArrayList<Byte>();
        //Regular fields
        a.add((byte) 0x2);
        a.add((byte) 0x53);
        a.add((byte) 0x1);
        a.add((byte) 0x2);
        a.add((byte) 0x3);
        
        //Indexed fields
        for (int i = 0; i < 100; i++) {
            a.add((byte)0x0);
            a.add((byte)0x0);
        }
        
        for (int i = 0; i <= 95; i++) {
            a.add((byte) 0x0);
        }
        byte[] result = new byte[a.size()];
        int i = 0;
        for (byte b : a) {
            result[i] = b;
            i++;
        }
        return result;
    }
    
    public void testCreationWithByteArray() {
        IProtocolMessage db = new ProtocolMessage(protocolType, false, createByteArray());
        assertEquals("arg0V1", db.getValue("arg0"));
        assertEquals("arg1V1", db.getValue("arg1"));
        assertEquals("5", db.getValue("arg2"));
        assertEquals("197121", db.getValue("arg3"));
    }
    
    public void testSetArg0() {
        IProtocolMessage db = new ProtocolMessage(protocolType, false);
        db.setValue("arg0", "arg0V1");
        assertEquals("arg0V1", db.getValue("arg0"));
    }
    
    public void testSetArg1() {
        IProtocolMessage db = new ProtocolMessage(protocolType, false);
        db.setValue("arg1", "arg1V1");
        assertEquals("arg1V1", db.getValue("arg1"));
    }
    
    public void testSetArg2() {
        IProtocolMessage db = new ProtocolMessage(protocolType, false);
        db.setValue("arg2", "7");
        assertEquals("7", db.getValue("arg2"));
    }
    
    public void testIndexArg() {
        IProtocolMessage db = new ProtocolMessage(protocolType, false);
        for (int i = 0; i < 100; i++) {
            assertEquals("0", db.getValue("ia0", i));
            assertEquals("0", db.getValue("ia1", i));
            db.setValue("ia0", i + "", i);
            db.setValue("ia1", (i + 10) + "", i);
            assertEquals(i + "", db.getValue("ia0", i));
            assertEquals((i + 10) + "", db.getValue("ia1", i));
        }
    }
    
    
    class StringFactory extends ProtocolLayoutFactory {
        StringFactory() {
            super("String", false);
            protocols(
              request(argStr("Str", quantity(0, Unit.byteSize), quantity(10, Unit.byteSize))),
              response()
            );
        }
    }

    
    public void testStrArg() {
        
        IProtocolMessage p = new ProtocolMessage(new StringFactory().getRequestProtocol(), false);
        assertEquals("", p.getValue("Str"));
        p.setValue("Str", "Hej");
        assertEquals("Hej", p.getValue("Str"));
        p.setValue("Str", "TooLongString");
        assertEquals("TooLongStr", p.getValue("Str"));
    }
    class VirtualFactory extends ProtocolLayoutFactory {

        VirtualFactory() {
            super("VFactory", false);
            protocols(
              request(
                args(
                  argByte("arg1", size(1), offset(0), 
                    values(
                      value("v1", 0, 
                        args(
                          vArg("vArg1", 
                             values(
                               value("vv1", 0, 
                                 args(
                                   argByte("arg2", size(4), offset(1))
                                 )
                               ),
                               value("vv2", 1)
                             )
                          )
                        )
                      ),
                      value("v2", 1)
                    )
                  )
                )
              ),
              response(
                args(
                  vArg("vArg1", values(value("v1", 0)))
                )
              )
            );
        }
        
    }
    
    public void testVirtualArg() {
        IProtocolLayoutType vType = new VirtualFactory().getRequestProtocol();
        IProtocolState ps = new ProtocolState();
        IProtocolMessage p = new ProtocolMessage(vType, false, ps);
        assertEquals(5, p.getSize());
        assertEquals("vv1", p.getValue("vArg1"));
        assertEquals("v1", p.getValue("arg1"));
        p.setValue("vArg1", "vv2");
        assertEquals("v1", p.getValue("arg1"));
        assertEquals("vv2", p.getValue("vArg1"));
        
        p = new ProtocolMessage(new VirtualFactory().getRequestProtocol(), false, ps);
        assertEquals("vv2", p.getValue("vArg1"));
        
    }
    
    
    class EqualityFactory extends ProtocolLayoutFactory {

        EqualityFactory() {
            super("EqFactory", false);
            protocols(
              request(
                args(
                  arg("Arg1", 1, 5, 
                    values(
                      value("v1", 0x0),
                      value("v2", 0x1, 
                        args(
                          argByte("v3", 1, 1)
                        )
                      )
                    )
                  )
                )
              ),
              response() 
            );
        }
        
    }
    
    public void testEquality() {
        IProtocolLayoutType type = new EqualityFactory().getRequestProtocol();
        assertEquals(new ProtocolMessage(type, false), new ProtocolMessage(type, false));
        IProtocolMessage p1;
        IProtocolMessage p2; 
        p1 = new ProtocolMessage(type, false, new byte[]{0x21, 1, 0});
        p2 = new ProtocolMessage(type, false, new byte[]{0x20, 1, 1});
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p1.hashCode());
        p1 = new ProtocolMessage(type, false, new byte[]{0x21, 1, 0});
        p2 = new ProtocolMessage(type, false, new byte[]{0x11, 1, 1});
        assertFalse(p1.equals(p2));
        assertEquals(p1.hashCode(), p1.hashCode());
        
        p1 = new ProtocolMessage(type, false, new byte[]{0, 0});
        p2 = new ProtocolMessage(type, false, new byte[]{1, 1});
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p1.hashCode());
        
    }
    

    private static class MsbLsbFactory extends ProtocolLayoutFactory {
        MsbLsbFactory() {
            super("p", true);
            protocols(
              request( 
                args(
                  argByte("v", 4, 0)
                )
              ),
              response()
            );
        }
    }
    public void testGetMsbLsbOnInt() {
        IProtocolLayoutType type = new MsbLsbFactory().getRequestProtocol();
        IProtocolMessage pLsb = new ProtocolMessage(type, false, 1, 2, 3, 4);
        assertEquals("67305985", pLsb.getValue("v"));
        IProtocolMessage pMsb = new ProtocolMessage(type, true, 1, 2, 3, 4);
        assertEquals("16909060", pMsb.getValue("v"));
    }

    
    
    public void testSetMsbLsbOnInt() {
        IProtocolLayoutType type = new MsbLsbFactory().getRequestProtocol();
        IProtocolMessage pLsb = new ProtocolMessage(type, false);
        pLsb.setValue("v", "67305985");
        assertArray(pLsb);

        IProtocolMessage pMsb = new ProtocolMessage(type, true);
        pMsb.setValue("v", "16909060");
        assertArray(pMsb);
    }
    private void assertArray(IProtocolMessage p) {
        for (int i = 0; i < p.getDataAsInts().length; i++) {
            assertEquals(i + 1, p.getDataAsInts()[i]);
        }
    }
    class DefaultFactory extends ProtocolLayoutFactory {
        DefaultFactory() {
            super("DF", true);
            protocols(
              request(
                args(
                  argByte("a1", 0,
                    values(
                      value("v1", 0x1, 
                        args(
                          argByte("a2", 1, 
                            values(
                              value("v2", 0x2, 
                                args(
                                  argByte("a3", 2,
                                    values(
                                      value("v3", 0x3)
                                    )
                                  )
                                )
                              )
                            )
                          )
                        )
                      )
                    )
                  )
                )
              ),
              response()
            );
        }
    }
    
    public void testDefaultValues() {
        new ProtocolMessage(new DefaultFactory().getRequestProtocol(), false);
    }
    
//    private static class IllegalValueTestFactory extends ProtocolFactory {
//        IllegalValueTestFactory() {
//            super("p", true);
//            protocols(
//              request(
//                args(
//                  arg("arg0", 1, 0,
//                    values(
//                      value("0", 0x0, 
//                        args(
//                          arg("arg1", 2, 1, 
//                            values(
//                              value("10", 0x0),
//                              value("11", 0x1)
//                            )
//                          )
//                        )
//                      ),
//                      value("1", 0x1, 
//                        args(
//                          arg("arg2", 2, 1, 
//                            values(
//                              value("20", 0x2),
//                              value("21", 0x3)
//                            )
//                          )
//                        )
//                      )
//                    )
//                  )
//                )
//              ),
//              response()
//            );
//        }
//    }
//    
//    public void testIllegaValue() {
//        IProtocol p = new Protocol(new IllegalValueTestFactory().getSlaveToHostProtocol());
//        assertEquals("10", p.getValue("arg1"));
//        p.setValue("arg0", "1");
//        assertEquals("20", p.getValue("arg2"));
//    }
    
    public void testPayloadReqursion() {
        IProtocolMessage message = new ProtocolMessage(new Message().getResponseProtocol(), false);
//        assertEquals(103, message.getSizeInBytes());
        message.setValue("arg1", "v2");
        assertEquals(1, message.getArguments().length);
        message.setValue("arg1", "v1");
        assertEquals(40, message.getArguments().length);
        assertEquals("v3", message.getValue("pl1_arg1"));
        String[] REF_ARGS = {"arg1", "pl1_arg1", "pl1_pl2_arg1", "pl1_pl2_Sequence Counter", 
                             "pl1_pl2_Sample0", "pl1_pl2_Sample1", "pl1_pl2_Sample2", "pl1_pl2_Sample3", "pl1_pl2_Marker0", "pl1_pl2_Marker1",
                             "pl1_pl2_Sample0", "pl1_pl2_Sample1", "pl1_pl2_Sample2", "pl1_pl2_Sample3", "pl1_pl2_Marker0", "pl1_pl2_Marker1",
                             "pl1_pl2_Sample0", "pl1_pl2_Sample1", "pl1_pl2_Sample2", "pl1_pl2_Sample3", "pl1_pl2_Marker0", "pl1_pl2_Marker1",
                             "pl1_pl2_Sample0", "pl1_pl2_Sample1", "pl1_pl2_Sample2", "pl1_pl2_Sample3", "pl1_pl2_Marker0", "pl1_pl2_Marker1",
                             "pl1_pl2_Sample0", "pl1_pl2_Sample1", "pl1_pl2_Sample2", "pl1_pl2_Sample3", "pl1_pl2_Marker0", "pl1_pl2_Marker1",
                             "pl1_pl2_Sample0", "pl1_pl2_Sample1", "pl1_pl2_Sample2", "pl1_pl2_Sample3", "pl1_pl2_Marker0", "pl1_pl2_Marker1"
                             };
        for (int i = 0; i < message.getArguments().length; i++) {
            assertEquals(REF_ARGS[i], message.getArguments()[i].getName());
        }
        for (int i = 0; i < 6; i++) {
            assertEquals("0", message.getValue("pl1_pl2_Sample0", i));
            assertEquals("0", message.getValue("pl1_pl2_Sample1", i));
            assertEquals("0", message.getValue("pl1_pl2_Sample2", i));
            assertEquals("0", message.getValue("pl1_pl2_Sample3", i));
            assertEquals("0", message.getValue("pl1_pl2_Marker0", i));
            assertEquals("0", message.getValue("pl1_pl2_Marker1", i));
        }
        
    }
    
    public void testPayloadReqursionWithData() {
        int[] data = new int[]{0, 0, 1, 
                               0, 1, 2, 3, 
                               3,   2,  1,  0,  4,  5,  6,  7,  8,  9, 
                               13, 12, 11, 10, 14, 15, 16, 17, 18, 19, 
                              };
        IProtocolMessage message = new ProtocolMessage(new Message().getResponseProtocol(), false, data);
        assertEquals("v1", message.getValue("arg1"));
        assertEquals("v3", message.getValue("pl1_arg1"));
        assertEquals("v2", message.getValue("pl1_pl2_arg1"));
        assertEquals("50462976", message.getValue("pl1_pl2_Sequence Counter"));
        assertEquals("3", message.getValue("pl1_pl2_Sample0", 0));
        assertEquals("2", message.getValue("pl1_pl2_Sample1", 0));
        assertEquals("1", message.getValue("pl1_pl2_Sample2", 0));
        assertEquals("0", message.getValue("pl1_pl2_Sample3", 0));
        assertEquals("394500", message.getValue("pl1_pl2_Marker0", 0));
        assertEquals("591879", message.getValue("pl1_pl2_Marker1", 0));

        assertEquals("13", message.getValue("pl1_pl2_Sample0", 1));
        assertEquals("12", message.getValue("pl1_pl2_Sample1", 1));
        assertEquals("11", message.getValue("pl1_pl2_Sample2", 1));
        assertEquals("10", message.getValue("pl1_pl2_Sample3", 1));
        assertEquals("1052430", message.getValue("pl1_pl2_Marker0", 1));
        assertEquals("1249809", message.getValue("pl1_pl2_Marker1", 1));
    }
    
    
    public void testProtocolTypeEquality() {
//    	assertEquals(new Message().getResponseProtocol(), new Message().getResponseProtocol()); //Doesn't work yet
    	assertEquals(new TestDBTFactory().getResponseProtocol(), new TestDBTFactory().getResponseProtocol());
    	assertEquals(new VarSizeProtocol().getResponseProtocol(), new VarSizeProtocol().getResponseProtocol());
//    	assertEquals(new ValuePrefixTestFactory().getRequestProtocol(), new ValuePrefixTestFactory().getRequestProtocol()); //Doesn't work yet
    	assertEquals(new DynamicSizeProtocol().getRequestProtocol(), new DynamicSizeProtocol().getRequestProtocol()); 
    	assertEquals(new ResolutionFactory().getRequestProtocol(), new ResolutionFactory().getRequestProtocol());
    }
    
    public static class Message extends ProtocolLayoutFactory {
        public Message() {
            super("db1", true);
            protocols(
               request(),
               response(
                 args(
                   argByte("arg1", 1, 0, 
                     values(
                       value("v1", 0, new Payload1().getResponseProtocol(), "pl1", 1),
                       value("v2", 1)
                     )
                   )
                 )
               )
             );
        }
    }
}



class Payload1 extends ProtocolLayoutFactory implements IProtocolLayoutFactory {
    Payload1() {
      super("db1", true);
      protocols(
        request(),
        response(
          args(
            argByte("arg1", 1, 0, 
              values(
                value("v3", 0, new Payload2().getResponseProtocol(), "pl2", 1),
                value("v4", 1)
              )
            )
          )
        )
      );
    }
}

class Payload2 extends ProtocolLayoutFactory implements IProtocolLayoutFactory {
    Payload2() {
      super("db1", true);
      protocols(
        request(),
        response(
          args(
            argByte("arg1", 1, 0, 
              values(
                value("v1", 0),
                value("v2", 1)
              )
            ),
            argByte("Sequence Counter", 4, 1),
            iArg("EGM Data", 6, 
              argByte("Sample0", 1, 5),
              argByte("Sample1", 1, 6),
              argByte("Sample2", 1, 7),
              argByte("Sample3", 1, 8),
              argByte("Marker0", 3, 9),
              argByte("Marker1", 3, 12)
            )
          )
        )
      );
    }
}
    