package org.jprotocol.framework.dsl;

import java.util.WeakHashMap;

import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * Implementation of a 0-32 bit-pattern.
 * This class can only be instantiated via static 'create' methods. The reason for that is, if the bit-pattern is created
 * immutable, object instances can be reused. If the bit-pattern is created mutable, the instances are not reused.
 * This class will be used in the realtime system, so many instances will be created all the time at a fast pace. By reusing 
 * instances we can conserve memory. 
 *  
 * @author eliasa01
 */
@SuppressWarnings({ "unchecked", "serial" })
final public class BitPattern implements IBitPattern {
    final static private WeakHashMap<Integer, IBitPattern>[] cache = new WeakHashMap[32];
    static {
        for (int i = 0; i < cache.length; i++) {
            cache[i] = new WeakHashMap<Integer, IBitPattern>();
        }
    }
    private int intValue = -1;
    final private int[] bits;
    final private boolean immutable;
    
    /**
     * Creates a bit-pattern that is 24 bits wide
     * @param intValue the integer value tat should become the bit-pattern
     * @return
     */
    public static IBitPattern create24BitBitPattern(int intValue) {
        return createBitPattern(intValue, 24);
    }

    /**
     * Creates a bit-pattern that is numberOfBits bits wide.
     * @param intValue the integer value that should become the bit-pattern
     * @param numberOfBits width of the bit-pattern
     * @return
     */
    public static IBitPattern createBitPattern(int intValue, int numberOfBits) {
        return createBitPattern(intValue, numberOfBits, true);
    }
    /**
     * 
     * @param intValue the integer value that should become the bit-pattern
     * @param numberOfBits width of the bit-pattern
     * @param immutable if true, the created object is immutable, if false the create object is mutable
     * @return
     * @note If immutable is true, a cast to BitPattern is needed to use the 'set' methods.
     */
    public static IBitPattern createBitPattern(int intValue, int numberOfBits, boolean immutable) {
        assert BitPattern.isValueInRange(intValue, numberOfBits): intValue + " is out of range";
        IBitPattern bp;
        if (immutable) {
            Integer intObject = new Integer(intValue);
            bp = getFromCache(numberOfBits, intObject);
            if (bp == null) {
                bp = new BitPattern(intValue, numberOfBits, immutable);
                saveInCache(numberOfBits, bp, intObject);
            }
        } else {
            bp = new BitPattern(intValue, numberOfBits, immutable);
        }
        return bp;
    }
    
    public static boolean isValueInRange(int intValue, int numberOfBits) {
        return numberOfBits <= 32 && numberOfBits > 0 && intValue < twoToThePowerOf(numberOfBits);
    }

    /**
     * 
     * @param bits
     * @return
     */
    public static IBitPattern createBitPattern(int[] bits) {
        return createBitPattern(intValueOf(bits), bits.length);
    }
    
    /**
     * Creates a bit-pattern from a binary string representation.
     * For example:
     * <pre><code>
     * IBitPattern bp = BitPattern.createBitPatternFromString("010101");
     * </code></pre>
     * will create a bit pattern with a width of 6, with the integer value of 21.
     * @param bitPatternAsString
     * @return
     */
    public static IBitPattern createBitPatternFromString(String bitPatternAsString) {
        int[] bits = new int[bitPatternAsString.length()];
        for (int i = 0; i < bits.length; i++) {
            char ch = bitPatternAsString.charAt(i);
            assert ch == '0' || ch == '1' : "BitPattern: " + bitPatternAsString; 
            bits[bits.length -  i - 1] = Integer.parseInt(ch + "");
        }
        return createBitPattern(bits);
    }
    
    /**
     * Construct a bitpattern from a an int value
     * @param intValue the value
     * @param numberOfBits the number of bits the bitpattern should contain
     */
    private BitPattern(int intValue, int numberOfBits, boolean immutable) {
        bits = new int[numberOfBits];
        this.intValue = intValue;
        this.immutable = immutable;
        BitPattern.bitsOf(intValue, bits);
    }
    
    private static long twoToThePowerOf(int numberOfBits) {
        return 1L<<numberOfBits;
    }

    private static void bitsOf(int intValue, int[] bits) {
        for (int i = 0; i < bits.length; i++) {
            bits[i] = (intValue & 1 << i) >> i;
        }
    }
    
    public int getBit(int pos) {
        assert pos >= 0;
        assert pos < getSize();
        return bits[pos];
    }

    public int getSize() {
        return bits.length;
    }

    
    public boolean equals(Object obj) {
        if (!(obj instanceof IBitPattern)) {
            return false;
        }
        IBitPattern bp = (IBitPattern)obj;
        if (getSize() != bp.getSize()) {
            return false;
        }
        return equals(bp, 0, bits.length - 1);
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).
        append(intOf()).
        append(getSize()).
        toHashCode();
    }
    
    public boolean equals(IBitPattern bp, int startPos, int endPos) {
        
        assert bits.length == bp.getSize();
        assert startPos >= 0;
        assert endPos < bits.length;
        assert startPos <= endPos;
        
        for (int i = startPos; i <= endPos; i++) {
            if (bits[i] != bp.getBit(i)) {
                return false;
            }
        }
        return true;
    }
    /**
     * @return
     */
    public int[] getBits() {
        int[] dest = new int[bits.length];
        System.arraycopy(bits, 0, dest, 0, bits.length);
        return dest;
    }

    public int intOf() {
        if (intValue < 0) {
            intValue = BitPattern.intValueOf(bits);
        }
        return intValue;
    }

    
    public int intOf(int startPos, int endPos) {
        assert startPos >= 0;
        assert endPos < bits.length;
        assert endPos >= startPos;
        return BitPattern.intValueOf(bits, startPos, endPos);
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(24);
        for (int i = getSize() - 1; i >= 0; i--) {
            sb.append(getBit(i));
        }
        return sb.toString();
    }

    
    public void increment() {
        setValue(intOf() + 1);
    }
    public void decrement() {
        setValue(intOf() - 1);
    }
    
    public void setValue(int intValue) {
        assert !immutable;
        assert BitPattern.isValueInRange(intValue, getSize());
        this.intValue = intValue;
        BitPattern.bitsOf(intValue, bits);
    }
    
    
    private static int intValueOf(int[] bits) {
        return BitPattern.intValueOf(bits, 0, bits.length - 1);
    }

    private static int intValueOf(int[] bits, int startPos, int endPos) {
        int intValue = 0;
        for (int i = startPos; i <= endPos; i++) {
            if (bits[i] > 0) {
                intValue += twoToThePowerOf(i - startPos);
            }
        }
        return intValue;
    }

    public byte[] asByteArray() {
        byte[] b = new byte[getSize() / 8];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) intOf(i * 8, 8 *(i + 1) - 1);
        }
        return b;
    }


    private static synchronized void saveInCache(int numberOfBits, IBitPattern bp, Integer intObject) {
        cache[numberOfBits - 1].put(intObject, bp);
    }

    private static synchronized IBitPattern getFromCache(int numberOfBits, Integer intObject) {
        IBitPattern bp;
        bp = cache[numberOfBits - 1].get(intObject);
        return bp;
    }
}
