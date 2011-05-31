package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.check;
import static org.jprotocol.util.Contract.ensure;
import static org.jprotocol.util.Contract.nonNegative;
import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;
import static java.util.Arrays.copyOf;
import static org.jprotocol.framework.dsl.BitPattern.createBitPattern;


public class BitFilterUtil {
    static final int SIZE_OF_BYTE = 8;
    /**
     * 
     * @param data the byte array to filter, int is used as type, but logically it's a byte array
     * @param offset in bits
     * @param size in bits
     * @return
     */
    public static int[] filter(int[] data, int offset, int size) {
        require(notNull(data));
        require(offset >= 0);
        require(size >= 0);
        int[] result = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = filterByte(data[i], calcOffset(offset, i), calcSize(offset, size, i));
        }
        return result;
    }
    /**
     * 
     * @param value
     * @param offset in bits
     * @param size in bits
     * @return
     */
    public static int filterByte(int value, int offset, int size) {
        require(offset >= 0);
        require(offset < SIZE_OF_BYTE);
        require(size >= 0);
        require(size <= SIZE_OF_BYTE);
        int mask = ((int)Math.pow(2, size) - 1) << offset;
        return (value & mask);
    }
    
    /**
     * Decode the int value in the data array at the offset
     * @param data the data array
     * @param offset in bits
     * @param size in bits
     * @return
     */
    public static int intOf(int[] data, int offset, int size) {
    	assertions(data, offset, size);
    	int[] bits = new int[size];
    	for (int i = offset; i < offset + size; i++) {
    		bits[i - offset] = bitOf(data, i);
    	}
    	return createBitPattern(bits).intOf();
    }
    

	
	
	/**
     * Encode the value in a copy of the data array at the offset
     * @param value
     * @param offset in bits
     * @param size in bits
     * @param data the input array data
     * @return
     */
    public static int[] arrayOf(int value, int[] data, int offset, int size) {
    	assertions(data, offset, size);
    	require(nonNegative(value));
    	final int[] copy = copyOf(data, data.length);
    	int[] bits = createBitPattern(value, size).getBits();
    	for (int i = offset; i < offset + size; i++) {
    		setBit(copy, i, bits[i - offset]);
    	}
    	ensureByte(copy);
    	ensure(data != copy);
    	return copy;
    }

	private static void requireByte(int[] data) {
		assertByte(data);
		
	}
	private static void ensureByte(int[] data) {
		assertByte(data);
		
	}
	private static void assertByte(int[] data) {
		for (int value: data) {
			check(nonNegative(value));
			check(value <= 255);
		}
	}
	private static int bitOf(int[] data, int index) {
		return data[byteIndexOf(index)] & (1 << indexInByte(index));
	}
	
	private static int indexInByte(int index) {
		return index % SIZE_OF_BYTE;
	}
	
    private static void setBit(int[] data, int index, int value) {
    	int[] bits = createBitPattern(data[byteIndexOf(index)], SIZE_OF_BYTE).getBits();
    	bits[indexInByte(index)] = value;
    	data[byteIndexOf(index)] = BitPattern.createBitPattern(bits).intOf();
	}
    
	private static void assertions(int[] data, int offset, int size) {
    	require(notNull(data));
    	requireByte(data);
    	require(nonNegative(offset));
    	require(nonNegative(size));
    	require(bitSizeOf(data.length) >= offset + size);
    }
    
    private static int bitSizeOf(int value) {
		return value * SIZE_OF_BYTE;
	}
    
    public static int offset(int v) {
    	return v;
    }
    
    public static int size(int v) {
    	return v;
    }
    
    private static int calcOffset(int offset, int byteIx) {
        return byteIndexOf(offset) == byteIx ? indexInByte(offset) : 0;
    }

    private static int calcSize(int offset, int size, int byteIx) {
        int calcSize = 0;
        int bStartIx = byteIndexOf(offset);
        if (byteIx == bStartIx) {
            calcSize = Math.min(SIZE_OF_BYTE - (indexInByte(offset)), size);
        } else if (byteIx > bStartIx) {
            int bEndIx = byteIndexOf(offset + size);
            if (byteIx < bEndIx) {
                calcSize = SIZE_OF_BYTE;
            } else if(byteIx == bEndIx) {
                calcSize = (offset + size) % SIZE_OF_BYTE;
            }
        }
        return calcSize;
    }
    private static int byteIndexOf(int bitIndex) {
        return bitIndex / SIZE_OF_BYTE;
    }

}
