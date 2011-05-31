package org.jprotocol.framework.dsl;

import static org.jprotocol.framework.dsl.BitFilterUtil.arrayOf;
import static org.jprotocol.framework.dsl.BitFilterUtil.filter;
import static org.jprotocol.framework.dsl.BitFilterUtil.filterByte;
import static org.jprotocol.framework.dsl.BitFilterUtil.intOf;
import static org.jprotocol.framework.dsl.BitFilterUtil.offset;
import static org.jprotocol.framework.dsl.BitFilterUtil.size;
import junit.framework.TestCase;

import org.jprotocol.util.Contract.ContractError;


public class BitFilterUtilTest extends TestCase
{
    private void assertEquals(int[] a1, int[] a2) {
        assertEquals(a1.length, a2.length);
        for (int i = 0; i < a1.length; i++) {
            assertEquals(a1[i], a2[i]);
        }
    }
    public void testFilterOnByte() {
        assertEquals(0x80, filterByte(0x80, offset(0), size(8)));
        assertEquals(0x80, filterByte(0x81, offset(7), size(1)));
        assertEquals(0xc0, filterByte(0xc3, offset(6), size(2)));
        assertEquals(0x1e, filterByte(0xff, offset(1), size(4)));

    }
    
    public void testFilter() {
        assertEquals(new int[]{0x10}, filter(new int[]{0x10}, 0, 8));
        
        assertEquals(new int[]{0xf0, 0xff, 0x3, 0x0}, filter(new int[]{0xff, 0xff, 0xff, 0xff}, offset(4), size(14)));
        assertEquals(new int[]{0x20, 0x0, 0x0}, filter(new int[]{0xff, 0xff, 0xff}, offset(5), size(1)));
        
    }
    
    public void testIntOfInOneByte() {
    	assertEquals(0x3, intOf(new int[]{0xff}, offset(1), size(2)));
    }
    public void testIntOfInTwoBytes() {
    	assertEquals(0x3, intOf(new int[]{0xff, 0xff}, offset(7), size(2)));
    }
    public void testIntOfInThreeBytes() {
    	assertEquals(0x3ff, intOf(new int[]{0xff, 0xff, 0xff}, offset(7), size(10)));
    }
    
    public void testArrayOfInOneByte() {
    	assertEquals(new int[]{0x6}, arrayOf(0x3, new int[]{0x0}, offset(1), size(2)));
    }
    public void testArrayOfTwoBytes() {
    	assertEquals(new int[]{0x80, 0x1}, arrayOf(0x3, new int[]{0x0, 0x0}, offset(7), size(2)));
    }
    public void testArrayOfInThreeBytes() {
    	assertEquals(new int[]{0x80, 0xff, 0x1}, arrayOf(0x3ff, new int[]{0x0, 0x0, 0x0}, offset(7), size(10)));
    }

    public void testIntOfIndexOutOfBounds() {
    	try {
    		assertEquals(0x3, intOf(new int[]{0xff}, offset(7), size(2)));
    	} catch (ContractError e) {
    		return;
    	}
    	assertTrue(false);
    }
    
    public void testArrayOfIndexOutOfBounds() {
    	try {
        	assertEquals(new int[]{0x80, 0x1}, arrayOf(0x3, new int[]{0x0}, offset(7), size(2)));
    	} catch (ContractError e) {
    		return;
    	}
    	assertTrue(false);
    }
    
}
