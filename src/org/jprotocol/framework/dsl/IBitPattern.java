package org.jprotocol.framework.dsl;

import java.io.Serializable;

/**
 * A bit-pattern representation of an integer. The interface makes it easier to access individual bits or bit ranges in an integer. 
 * The individual bits are defined as an integer that can have a value of 0 or 1. For example, the decimal value of 5 is represented as
 * a bit-pattern with bit 0 and 2 set to 1 and bit 1 set to 0. The following is true:
 * <pre><code>
 * IBitPattern bp = //Create a bit-pattern with the decimal value of 5, and witdth or size of 3
 * assert bp.intOf() == 5;
 * assert bp.getBit(0) == 1;
 * assert bp.getBit(1) == 0;
 * assert bp.getBit(2) == 1;
 * assert bp.getSize() == 3;
 * </code></pre> 
 * The bit-pattern interface is immutable, cannot be changed once created.
 * @author eliasa01
 * @alias IBitPattern
 */
public interface IBitPattern extends Serializable {
    /**
     * The bit at position pos
     * @param pos
     * @return the bit as 0 or 1
     */
    int getBit(int pos);
    
    /**
     * The size or number of bits
     * @return
     */
    int getSize();
    
    /**
     * The bits in this bit-pattern 
     * @return an array of ints if a bit is set the value is > 0 otherwise 0
     */
    int[] getBits();
    /**
     * An int value of this bit-pattern
     * @return 
     */
    int intOf();
    /**
     * An int value of a part of the bit-pattern
     * @param startPos start position
     * @param endPos end position
     * @return the int representing the value between the start and end position
     */
    int intOf(int startPos, int endPos);

    /**
     * Sees if two bit-pattern parts are equal
     * @param bp the other bit-pattern
     * @param startPos the start bit
     * @param endPos the end bit
     * @return true if the range from startPos to endPos is equal
     */
    boolean equals(IBitPattern bp, int startPos, int endPos);
    
    /**
     * Return as a byte array lsb in index 0
     * @return
     */
    byte[] asByteArray();
}
