package org.jprotocol.quantity;

import static org.jprotocol.quantity.Quantity.quantity;
import static org.jprotocol.quantity.Unit.A;
import static org.jprotocol.quantity.Unit.V;
import static org.jprotocol.quantity.Unit.bitSize;
import static org.jprotocol.quantity.Unit.bpm;
import static org.jprotocol.quantity.Unit.byteSize;
import static org.jprotocol.quantity.Unit.hz;
import static org.jprotocol.quantity.Unit.intSize;
import static org.jprotocol.quantity.Unit.mV;
import static org.jprotocol.quantity.Unit.ms;
import static org.jprotocol.quantity.Unit.noUnit;
import static org.jprotocol.quantity.Unit.s;
import junit.framework.TestCase;


public class QuantityTest extends TestCase {

    
    public void testVoltConversion() {
        Quantity q = quantity(200, mV);
        assertEquals(.2, q.convert(V).getValue());
        assertEquals(200.0, q.convert(V).convert(mV).getValue());
        assertEquals(200.0, q.convert(mV).getValue());
    }

    public void testBpmSecondConversion() {
        Quantity q1 = quantity(60, bpm);
        assertEquals(1.0, q1.convert(s).getValue());
        assertEquals(60.0, quantity(1000, ms).convert(bpm).getValue());
        assertEquals(1.0, q1.convert(s).getValue());
        assertEquals(60.0, quantity(1.0, s).convert(bpm).getValue());
        assertEquals(60.0, q1.convert(s).convert(bpm).getValue());
        assertEquals(1000.0, q1.convert(ms).getValue());
        assertEquals(1000.0, q1.convert(s).convert(bpm).convert(ms).getValue());
        assertEquals(1.5, quantity(40, bpm).convert(s).getValue());
    }

    public void testHerzSecondConversion() {
        assertEquals(7.8125, quantity(128, hz).convert(ms).getValue());
        assertEquals(128.0, quantity(7.8125, ms).convert(hz).getValue());
    }
    
    public void testBitSizeConversion() {
        assertEquals(32D, quantity(1, intSize).convert(bitSize).getValue());
        assertEquals(4D, quantity(1, intSize).convert(byteSize).getValue());
    }
    public void testIncompatibleConversion() {
        try {
            Quantity q = quantity(200, ms);
            q.convert(mV);
        } catch (UnitIncompatible e) {
            return;
        }
        assertFalse(true);
    }
    
    public void testEquality() {
        assertEquals(quantity(200, mV), quantity(200, mV));
        assertEquals(quantity(200, mV).convert(V), quantity(200, mV).convert(V));
    }
    public void testNotEqual() {
        assertFalse(quantity(200, mV).equals(quantity(200, mV).convert(V)));
        
    }
    
    public void testAddition() {
        assertEquals(quantity(120, bpm), quantity(60, bpm).add(quantity(60, bpm)));
        assertEquals(quantity(120, bpm), quantity(60, bpm).add(quantity(1, s)));
        
    }
    public void testSubtractition() {
        assertEquals(quantity(0, bpm), quantity(60, bpm).subtract(quantity(60, bpm)));
        assertEquals(quantity(0, bpm), quantity(60, bpm).subtract(quantity(1, s)));
        
    }
    
    public void testIsBigger() {
        assertTrue(quantity(100, mV).isBigger(quantity(90, mV)));
        assertFalse(quantity(90, mV).isBigger(quantity(100, mV)));
        assertFalse(quantity(100, mV).isBigger(quantity(100, mV)));
        assertTrue(quantity(1, V).isBigger(quantity(100, mV)));
        assertFalse(quantity(1, V).isBigger(quantity(1000, mV)));
    }
    public void testIsBiggerOrEqual() {
        assertTrue(quantity(100, mV).isBiggerOrEqual(quantity(90, mV)));
        assertFalse(quantity(90, mV).isBiggerOrEqual(quantity(100, mV)));
        assertTrue(quantity(100, mV).isBiggerOrEqual(quantity(100, mV)));
        assertTrue(quantity(1, V).isBiggerOrEqual(quantity(100, mV)));
        assertTrue(quantity(1, V).isBiggerOrEqual(quantity(1000, mV)));
    }
    public void testIsSmallerOrEqual() {
        assertFalse(quantity(100, mV).isSmallerOrEqual(quantity(90, mV)));
        assertTrue(quantity(90, mV).isSmallerOrEqual(quantity(100, mV)));
        assertTrue(quantity(100, mV).isSmallerOrEqual(quantity(100, mV)));
        assertFalse(quantity(1, V).isSmallerOrEqual(quantity(100, mV)));
        assertTrue(quantity(1, V).isSmallerOrEqual(quantity(1000, mV)));
    }
    
    
    public void testAbs() {
        assertEquals(quantity(10, ms), quantity(-10, ms).abs());
        assertEquals(quantity(10, ms), quantity(10, ms).abs());
    }
    
//    public void testMultiplication() {
//        assertEquals(quantity(100, V), quantity(10, ohm).multiply(quantity(10, A)));
//    }
    public void testDivision() {
        assertEquals(quantity(10, A), quantity(100, A).divide(quantity(10, noUnit)));
    }
    
    public void testMin() {
        assertEquals(quantity(10.0, ms), quantity(10.0, ms).min(quantity(10.0, s)));
    }
    public void testMax() {
        assertEquals(quantity(10000, ms), quantity(10.0, ms).max(quantity(10.0, s)));
    }
}
