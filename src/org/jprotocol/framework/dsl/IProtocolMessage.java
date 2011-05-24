package org.jprotocol.framework.dsl;

import org.jprotocol.quantity.Quantity;


/**
 * Represents an instance of a protocol or memory layout.
 * It is the operation part of it's type {@link IProtocolLayoutType} 
 * @alias IProtocol
 */
public interface IProtocolMessage {

    /**
     * Adjust the size of the underlying byte array to the smallest possible
     */
    void adjustSize();

    /**
     * Get the argument type of a name and index
     * @param name
     * @param indexes
     * @return
     */
    IArgumentType argOf(String name, int...indexes);
    
    /**
     * Is the argument name valid in this protocol state
     * @param name
     * @param indexes
     * @return
     */
    boolean isValid(String name, int...indexes);

    
    /**
     * Get the size of the byte array that contains the state of this protocol
     * @return
     */
    int getSize();
    
    /**
     * Get value as string, if enum then enum string value is returned, otherwise the raw bit-value is returned.
     * @param name of arg
     * @param index if this is indexed, use this parameter
     * @return
     */
    String getValue(String name, int...index);
    String getValue(IArgumentType argType);
    /**
     * Set value of the the arg by name 'name'
     * @param name of arg
     * @param value the new value
     * @param index if this is indexed, use this parameter
     */
    void setValue(String name, String value, int...index);
    
    /**
     * @see #argOf(String, int...)
     * @param argType
     * @param value
     */
    void setValue(IArgumentType argType, String value);

    String getRealValueAsString(String name, int...index);
    void setRealValueAsString(String name, String value, int...index);

    /**
     * @param name
     * @param indexes
     * @note can only be used for non enum types
     * @return
     */
    Quantity getRealQuantity(String name, int...indexes);
    /**
     * @param name
     * @param indexes
     * @note can only be used for non enum types
     * @return
     */
    Quantity getRealQuantity(IArgumentType argType);
    /**
     * @param name
     * @param indexes
     * @note can only be used for non enum types
     * @return
     */
    void setRealQuantity(String name, Quantity value, int...indexes);
    /**
     * @param name
     * @param indexes
     * @note can only be used for non enum types
     * @return
     */
    void setRealQuantity(IArgumentType argType, Quantity value);
    
    /**
     * Get value as name value pair
     * @param name of arg
     * @param index if this is indexed, use this parameter
     * @return
     */
    INameValuePair getValueAsNameValuePair(String name, int...index);
    
    /**
     * @see #argOf(String, int...)
     * @param argType
     * @return
     */
    INameValuePair getValueAsNameValuePair(IArgumentType argType);
    
    
    /**
     * Set bit representation value of the arg by name 'name'
     * @param name
     * @param bitValue the new bit value
     * @param index if this is indexed, use this parameter
     */
    void setBitValue(String name, int bitValue, int...index);
    
    /**
     * @see #argOf(String, int...)
     * @param argType
     * @param bitValue
     */
    void setBitValue(IArgumentType argType, int bitValue);

    
    /**
     * Get the underlying byte array
     * @return
     */
    byte[] getData();

    /**
     * Get the underlying byte array from a startindex
     * @param startIndex
     * @return
     */
    byte[] getData(int startIndex);
    /**
     * Get the underlying byte array as an int array
     * @return
     */
    int[] getDataAsInts();

    /**
     * Set the underlaying byte array starting from an index
     * @param payload
     * @param startIndex
     */
    void setData(byte[] payload, int startIndex);
    void setData(int[] payload, int startIndex);

    
    /**
     * Get the underlying byte array in readable comma separated hex string format, i.e. 0x0, 0x1, 0x9 etc.
     * @return
     */
    String readableData();
    
    /**
     * The meta data of this protocol
     * @return
     */
    IProtocolLayoutType getProtocolType(); 
    
    /**
     * Get all currently valid arguments
     * @return
     */
    IArgumentType[] getArguments();
    

    boolean hasPayload();
    
    /**
     * Get the number of entries for this indexed argument type
     * @note only of indexed argument type
     * @param arg
     * @return
     */
    int noOfEntriesOf(IArgumentType arg);

    /**
     * Where does this protocol end and the payload start
     * @return
     */
    int getHeaderEndIndex();

    void setRealValue(IArgumentType argType, double value);

    void setRealValue(String name, double value, int... indexes);

    double getRealValue(IArgumentType argType);

    double getRealValue(String name, int...indexes);

    void setDefaultValue(IArgumentType arg);
    
    /**
     * Create a snapshot copy of this protocol instance, with it's own copied protocol state.
     *  
     * @return
     */
    IProtocolMessage createSnapshot();

	boolean isMsbFirst();

	void setData(int index, int value);

}
