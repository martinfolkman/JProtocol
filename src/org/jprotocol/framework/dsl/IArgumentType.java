package org.jprotocol.framework.dsl;

import org.jprotocol.quantity.Unit;


/**
 * Describes an argument in a protocol or layout.
 * Properties such as name, size and offset are captured in this interface.
 * The interface supports a number of different argument types, the type decides how the argument should be decoded/encoded:
 * <li>Enum: The argument is an enumerated type and can contain a specific value set</li>
 * <li>Indexed: The argument is an indexed/array type</li>
 * <li>Str: The argument is of type string</li>
 * <li>Virtual: The argument is of type string. This means that the value of the argument 
 *     is not decoded/encoded from the underlying byte array like all other type, instead the protocol state {@link IProtocolState} is used</li>
 *   
 * @author eliasa01
 *
 */
public interface IArgumentType {
    
    
    /**
     * Name of argument
     * @return name of argument
     */
    String getName();
    
    String getSimpleName();
    
    /**
     * Size in bits (not bytes) of this argument
     * @return size in bits
     */
    int getSizeInBits();

    /**
     * Size in bytes (not bits) of this argument
     * @return size in bytes
     */
    int getSizeInBytes();
    
    /**
     * Offset in bits in the byte array that represents this data block/command (IProtocol)
     * @return offset in bits
     */
    int getOffset();
    
    int getOffsetWithinByte();
    
    int getStartByteIndex();

    int getEndByteIndex();
    
    /**
     * If this is an enum type this method returns the allowed values of this argument
     * @return array of name values
     */
    INameValuePair[] getValues();
    IEnumeration getEnumeration();
    
    /**
     * Get a specific name value pair of a name. Note that this is only valid for enum types
     * @param name of the value
     * @return the value of the enum type
     */
    int valueOf(String name);
    
    /**
     * 
     * @return true if argument type is an enum, false otherwise
     */
    boolean isEnumType();

    boolean isStr();
    
    /**
     * If the size is less than a byte return true
     * @return true the size is less than a byte, false otherwise
     */
    boolean isBitField();

    boolean isVirtual();
    /**
     * The name of a value
     * @param value
     * @return
     */
    String nameOf(int value);
    
    INameValuePair nvpOf(int value);

    boolean isIndexedType();
    
    boolean isAddress();
    
    boolean isSize();
    
    /**
     * Is this a real value with quantity
     * @return
     */
    boolean isReal();
    
    IArgumentType[] getChildren();
    
    IArgumentType argOf(String name);

    /**
     * Max number of entries in an indexed type
     * @return
     */
    int getMaxEntries();
    
    int getSizeInByteOfOneIndexedArg();

    double getRealOffset();

    double getResolution();
    
    Unit getUnit();


}
