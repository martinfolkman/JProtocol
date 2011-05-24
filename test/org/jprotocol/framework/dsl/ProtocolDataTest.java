package org.jprotocol.framework.dsl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


public class ProtocolDataTest extends TestCase {
    private final ProtocolData pd = new ProtocolData(1, 2, 3, 4);
    
    
    public void testSize() {
        assertEquals(4, pd.size());
    }
    
    public void testGet() {
        assertEquals(1, pd.getInt(0));
        assertEquals(2, pd.getInt(1));
        assertEquals(1, pd.getByte(0));
        assertEquals(2, pd.getByte(1));
    }
    
    public void testSetByte() {
        pd.setByte(0, (byte) 3);
        assertEquals(3, pd.getByte(0));
        assertEquals(3, pd.getInt(0));
        assertEquals(2, pd.getInt(1));
        assertEquals(2, pd.getByte(1));
    }
    public void testSetInt() {
        pd.setInt(0, 3);
        assertEquals(3, pd.getByte(0));
        assertEquals(3, pd.getInt(0));
        assertEquals(2, pd.getInt(1));
        assertEquals(2, pd.getByte(1));
    }
    
    public void testSetIntOutsideSize() {
        pd.setInt(10, 11);
        assertOutSideSize();
    }
    public void testSetByteOutsideSize() {
        pd.setByte(10, (byte)11);
        assertEquals(11, pd.size());
        assertOutSideSize();
    }
    
    private void assertOutSideSize() {
        assertEquals(11, pd.size());
        assertEquals(1, pd.getInt(0));
        assertEquals(2, pd.getInt(1));
        assertEquals(3, pd.getInt(2));
        assertEquals(4, pd.getInt(3));
        assertEquals(0, pd.getInt(4));
        assertEquals(0, pd.getInt(5));
        assertEquals(0, pd.getInt(6));
        assertEquals(0, pd.getInt(7));
        assertEquals(0, pd.getInt(8));
        assertEquals(0, pd.getInt(9));
        assertEquals(11, pd.getInt(10));
    }
    
    public void testGetIntData() {
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(4);
        assertEquals(new ImmutableList<Integer>(expected), pd.getIntData());
    }
    public void testGetByteData() {
        List<Byte> expected = new ArrayList<Byte>();
        expected.add((byte) 1);
        expected.add((byte) 2);
        expected.add((byte) 3);
        expected.add((byte) 4);
        assertEquals(new ImmutableList<Byte>(expected), pd.getByteData());
    }
    public void testGetByteDataWithIndex() {
        List<Byte> expected = new ArrayList<Byte>();
        expected.add((byte) 1);
        expected.add((byte) 2);
        expected.add((byte) 3);
        expected.add((byte) 4);
        assertEquals(new ImmutableList<Byte>(expected, 2), pd.getByteData(2));
    }
    public void testSetSize() {
    	pd.setSize(2);
    	assertEquals(2, pd.size());
    }
    
}
