package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.require;

import java.util.ArrayList;
import java.util.List;


public class ProtocolData {
    private final List<Integer> intData = new ArrayList<Integer>();
    private final List<Byte> byteData = new ArrayList<Byte>();
    
    public ProtocolData(byte...data) {
        this(makeIntArray(data), data);
    }
    
    public ProtocolData(int...data) {
        this(data, makeByteArray(data));
    }
    
    protected ProtocolData(int[] intData, byte[] byteData) {
        require(intData.length == byteData.length);
        for (int i = 0; i < intData.length; i++) {
            this.intData.add(intData[i]);
            this.byteData.add(byteData[i]);
        }
    }

    public int size() {
        return intData.size();
    }
    
    public int getInt(int ix) {
        return intData.get(ix);
    }
    public byte getByte(int ix) {
        return (byte) getInt(ix);
    }
    
    
    public ImmutableList<Integer> getIntData() {
    	return new ImmutableList<Integer>(intData);
    }
    
    public ImmutableByteList getByteData() {
        return new ImmutableByteList(byteData); 
    }
    
    
    public ImmutableByteList getByteData(int startIx) {
    	return new ImmutableByteList(byteData, startIx);
    }
    public void setInt(int ix, int value) {
        setIntByte(ix, value, (byte) value);
    }

    public void setByte(int ix, byte value) {
        setIntByte(ix, intOf(value), value);
    }

    private void setIntByte(int ix, int intValue, byte byteValue) {
        if (intData.size() - 1 < ix) {
            while (intData.size() - 1  < ix) {
                intData.add(0);
                byteData.add((byte)0);
            }
        } 
        intData.set(ix, intValue);
        byteData.set(ix, byteValue);
    }

    public void setInts(int ix, int...values) {
        for (int i = ix; i < ix + values.length; i++) {
            setInt(i, values[i - ix]);
        }
    }

    public void setBytes(int ix, byte...values) {
        for (int i = ix; i < ix + values.length; i++) {
            setByte(i, values[i - ix]);
        }
    }
	public void setSize(int newSize) {
        while (intData.size() > newSize) {
            intData.remove(intData.size() - 1);
            byteData.remove(byteData.size() - 1);
        }
	}
    
    private static int intOf(byte b) {
        return ~(b ^ 0xff) & b;
    }

    private static int[] makeIntArray(byte[] data) {
        int[] result = new int[data.length];
        for (int i = 0; i < data.length; i++) result[i] = intOf(data[i]); 
        return result;
    }
    private static byte[] makeByteArray(int[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) result[i] = (byte) data[i]; 
        return result;
    }


}

