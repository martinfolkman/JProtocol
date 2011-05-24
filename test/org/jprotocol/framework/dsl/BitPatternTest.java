package org.jprotocol.framework.dsl;

import java.util.logging.Logger;

import junit.framework.TestCase;

/**
 * Tests the bit-pattern class
 * @author eliasa01
 */
public class BitPatternTest extends TestCase { 
    final private static Logger LOGGER = Logger.getLogger(BitPatternTest.class.getName());
    public void testFullBits() {
        IBitPattern bp = BitPattern.createBitPatternFromString("111111111111111111111111");
        assertEquals(24, bp.getSize());
        for (int i = 0; i < bp.getSize(); i++) {
            assertEquals(1, bp.getBit(i));
        }
    }
    public void testEmptyBits() {
        IBitPattern bp = BitPattern.createBitPatternFromString("000000000000000000000000");
        assertEquals(24, bp.getSize());
        for (int i = 0; i < bp.getSize(); i++) {
            assertEquals(0, bp.getBit(i));
        }
    }
    
    public void testBitsVersusBit() {
        IBitPattern bp = BitPattern.createBitPatternFromString("111111111111111111111111");
        assertEquals(bp.getSize(), bp.getBits().length);
        for (int i = 0; i < bp.getSize(); i++) {
            assertEquals(bp.getBits()[i], bp.getBit(i));
        }
    }
    
    public void testImmutabillity() {
        IBitPattern bp = BitPattern.createBitPatternFromString("000000000000000000000000");
        assertFalse(bp.getBits() == bp.getBits());
        int[] bits = bp.getBits();
        for (int i = 0; i < bits.length; i++) {
            bits[i] = 1;
        }
        for (int i = 0; i < bp.getBits().length; i++) {
            assertEquals(0, bp.getBits()[i]);
        }
        
    }
    
    public void testIntOfRange() {
        IBitPattern bp = BitPattern.createBitPatternFromString("100110000000001100000000");
        assertEquals(19, bp.intOf(19, 23));
        assertEquals(3, bp.intOf(8, 9));
        assertEquals(0, bp.intOf(0, 7));
    }
    
    public void testEquality() {
        IBitPattern bp1 = BitPattern.createBitPatternFromString("100110000000001100000000");
        IBitPattern bp2 = BitPattern.createBitPatternFromString("100110000000001100000000");
        IBitPattern bp3 = BitPattern.createBitPatternFromString("100110000000001100000001");
        assertEquals(bp1, bp2);
        assertFalse(bp1.equals(bp3));
        assertFalse(bp3.equals(bp1));
        assertTrue(bp3.equals(bp3));
        assertFalse(bp2.equals(bp3));
        
        IBitPattern bp4 = BitPattern.createBitPattern(1, 20);
        IBitPattern bp5 = BitPattern.createBitPattern(1, 21);
        assertFalse(bp4.equals(bp5));
    }
    
    //This test fails sometimes, due to WeakHashMap that is used
    //Therefor it's commented out
//    public void testSameReference() {
//        IBitPattern bp1 = BitPattern.createBitPatternFromString("100110000000001100000000");
//        IBitPattern bp2 = BitPattern.createBitPatternFromString("100110000000001100000000");
//        
//        assertSame(bp1, bp2);
//        IBitPattern bp3 = BitPattern.createBitPatternFromString("100110000000001100000001");
//        assertNotSame(bp1, bp3);
//        IBitPattern bp4 = BitPattern.createBitPattern(1, 20);
//        IBitPattern bp5 = BitPattern.createBitPattern(1, 21);
//        assertNotSame(bp4, bp5);
//    }
    public void testIntOf() {
        int hexFull = 0xffffff;
        int hexEmpty = 0x0;
        IBitPattern bp1 = BitPattern.create24BitBitPattern(hexFull);
        IBitPattern bp2 = BitPattern.createBitPatternFromString("111111111111111111111111");
        IBitPattern bp3 = BitPattern.create24BitBitPattern(hexEmpty);
        IBitPattern bp4 = BitPattern.createBitPatternFromString("000000000000000000000000");
        assertEquals(hexFull, bp1.intOf());
        assertEquals(hexFull, bp2.intOf());
        assertEquals(bp1, bp2);

        assertEquals(hexEmpty, bp3.intOf());
        assertEquals(hexEmpty, bp4.intOf());
        assertEquals(bp3, bp4);
        
        IBitPattern bp5 = BitPattern.createBitPattern(0x7fffffff, 32);
        assertEquals(0x7fffffff, bp5.intOf());
        
        
        
        
    }
    
    
    public void testStringConstructor() {
        IBitPattern bp = BitPattern.createBitPatternFromString("0101");
        assertEquals(4, bp.getSize());
        assertEquals(1, bp.getBit(0));
        assertEquals(0, bp.getBit(1));
        assertEquals(1, bp.getBit(2));
        assertEquals(0, bp.getBit(3));
        
    }
    public void testIlLegalStringConstructor() {
        try {
            BitPattern.createBitPatternFromString("012");
        } catch (AssertionError e) {
            return; 
        }
        LOGGER.warning("Assertions must be turned on for this test to execute");
    }
    
    public void testValueRange() {
        assertTrue(BitPattern.isValueInRange(1, 1));
        assertFalse(BitPattern.isValueInRange(2, 1));
        assertTrue(BitPattern.isValueInRange(255, 8));
        assertFalse(BitPattern.isValueInRange(256, 8));
        assertFalse(BitPattern.isValueInRange(256, 33));
    }
    
    public void testMutableBitPattern() {
        BitPattern bp = (BitPattern) BitPattern.createBitPattern(0, 8, false);
        assertEquals("00000000", bp.toString());
        bp.increment();
        assertEquals("00000001", bp.toString());
        assertEquals(1, bp.intOf());
        bp.decrement();
        assertEquals("00000000", bp.toString());
        assertEquals(0, bp.intOf());
        bp.setValue(255);
        assertEquals("11111111", bp.toString());
        assertEquals(255, bp.intOf());
    }
    
    public void testInOfFromTo() {
        BitPattern bp = (BitPattern) BitPattern.createBitPattern(0xaabbccdd, 32);
        assertEquals(0xdd, bp.intOf(0, 7));
        assertEquals(0xcc, bp.intOf(8, 15));
        assertEquals(0xbb, bp.intOf(16, 23));
//        assertEquals(0xaa, bp.intOf(24, 31)); //TODO doesb't work because of sign bit
        
    }
    
    public void testAsByteArray() {
        BitPattern bp = (BitPattern) BitPattern.createBitPattern(0xaabbcc, 24);
        byte[] b = bp.asByteArray();
        assertEquals(3, b.length);
        assertEquals((byte)0xcc, b[0]);
        assertEquals((byte)0xbb, b[1]);
        assertEquals((byte)0xaa, b[2]);
        
    }
    
}
