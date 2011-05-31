package org.jprotocol.framework.dsl;

import static org.jprotocol.quantity.Quantity.quantity;
import junit.framework.TestCase;

import org.jprotocol.quantity.Unit;


public class StringBuilderProtocolTest extends TestCase {
    private final IProtocolMessage testedObject = new StringBuilderProtocolMessage(new StringBuilderTestFactory().getRequestProtocol());
    
    public void testDefault() {
        assertEquals("(\"TF\")", testedObject.toString());
    }
    public void testEnumSetValueName() {
        testedObject.setValue("a2", "v1");
        assertEquals("(\"TF\" (\"a2\" \"v1\"))", testedObject.toString());
    }
    public void testEnumSetValueArg() {
        testedObject.setValue(testedObject.argOf("a2"), "v1");
        assertEquals("(\"TF\" (\"a2\" \"v1\"))" , testedObject.toString());
    }
    public void testEnumSetBitValueName() {
        testedObject.setBitValue("a2", 1);
        assertEquals("(\"TF\" (\"a2\" \"v2\"))", testedObject.toString());
    }
    public void testEnumSetBitValueArg() {
        testedObject.setBitValue(testedObject.argOf("a2"), 1);
        assertEquals("(\"TF\" (\"a2\" \"v2\"))", testedObject.toString());
    }
    public void testRealQuantityName() {
        testedObject.setRealQuantity("a1", quantity(4, Unit.ms));
        assertEquals("(\"TF\" (\"a1\" \"2\"))", testedObject.toString());
    }
    public void testRealQuantityArg() {
        testedObject.setRealQuantity(testedObject.argOf("a1"), quantity(4, Unit.ms));
        assertEquals("(\"TF\" (\"a1\" \"2\"))", testedObject.toString());
    }
    public void testRealValueName() {
        testedObject.setRealValue("a1", 4.0, new int[]{});
        assertEquals("(\"TF\" (\"a1\" \"2\"))", testedObject.toString());
    }
    public void testRealValueArg() {
        testedObject.setRealValue(testedObject.argOf("a1"), 4.0);
        assertEquals("(\"TF\" (\"a1\" \"2\"))", testedObject.toString());
    }
    public void testRealValueAsStringName() {
        testedObject.setRealValueAsString("a1", "4.0");
        assertEquals("(\"TF\" (\"a1\" \"2\"))", testedObject.toString());
    }
    public void testValueName() {
        testedObject.setValue("a1", "2");
        assertEquals("(\"TF\" (\"a1\" \"2\"))", testedObject.toString());
    }
    public void testValueArg() {
        testedObject.setValue(testedObject.argOf("a1"), "2");
        assertEquals("(\"TF\" (\"a1\" \"2\"))", testedObject.toString());
    }
    public void testTwoValues() {
        testedObject.setValue("a1", "2");
        testedObject.setValue("a2", "v2");
        assertEquals("(\"TF\" (\"a1\" \"2\") (\"a2\" \"v2\"))", testedObject.toString());
    }
    
    public void testGets() {
        assertEquals("0", testedObject.getValue("a1"));
        assertEquals("v1", testedObject.getValue("a2"));
        testedObject.setValue("a1", "2");
        assertEquals("2", testedObject.getValue("a1"));
        testedObject.setValue("a2", "v2");
        assertEquals("v2", testedObject.getValue("a2"));
    }
    
}

class StringBuilderTestFactory extends ProtocolLayoutFactory {

    protected StringBuilderTestFactory() {
        super("TF", false);
        protocols(
          request(
            args(
              argInt("a1", 0, 0, 2, Unit.ms),
              argByte("a2", 4, values(value("v1", 0x0), value("v2", 0x1)))
            )
          ), 
          response()
        );
    }
    
}

