package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.check;
import static org.jprotocol.util.Contract.isNotNull;

import org.jprotocol.framework.dsl.argiters.ArgTypeOfIter;

import org.jprotocol.quantity.Quantity;



public class AbstractDecoratedArgument  extends AbstractDecorated {
    private final String argName;
	protected final int[] indexes;
    protected AbstractDecoratedArgument(IProtocolMessage protocol, String argName) {
    	this(protocol, argName, new int[]{});
    }
    protected AbstractDecoratedArgument(IProtocolMessage protocol, String argName, int[] indexes) {
        super(protocol);
        this.argName = argName;
        this.indexes = indexes;
    }
    /**
     * @note will not work for indexed values
     * @return
     */
    public INameValuePair getValueAsNameValuePair() {
        return protocol.getValueAsNameValuePair(argName);
    }
    
    public int getSizeInBits() {
        return protocol.getProtocolType().argOf(argName).getSizeInBits();
    }

    public INameValuePair[] getValues() {
        IArgumentType arg = new ArgTypeOfIter(argName, protocol.getProtocolType()).foundArgType;
        check(isNotNull(arg != null));
        return arg.getValues();
    }
    public String getValueAsStr(int...indexes) {
        return _getValueAsStr(indexes);
    }
    protected int _getValue(int...indexes) {
        return protocol.getValueAsNameValuePair(argName, indexes).getValue();
    }
    
    protected String _getValueAsStr(int...indexes) {
        return protocol.getValue(argName, indexes);
    }
    protected boolean _isValue(String valueName, int...indexes) {
        return valueName.equals(protocol.getValue(argName, indexes));
    }
    protected void _setValue(String valueName, int...indexes) {
        protocol.setValue(argName, valueName, indexes);
    }
    protected int _getBitValue(int...indexes) {
        return protocol.getValueAsNameValuePair(argName, indexes).getValue();
    }
    protected void _setBitValue(int bitValue, int...indexes) {
        protocol.setBitValue(argName, bitValue, indexes);
    }
    
    protected Quantity _getRealQuantity(int...indexes) {
        return protocol.getRealQuantity(argName, indexes);
    }
    protected void _setRealQuantity(Quantity value, int...indexes) {
        protocol.setRealQuantity(argName, value, indexes);
    }
    protected double _getRealValue(int...indexes) {
        return protocol.getRealValue(argName, indexes);
    }
    protected void _setRealValue(double value, int...indexes) {
        protocol.setRealValue(argName, value, indexes);
    }
    
    @Override
    public String toString() {
        return _getValueAsStr();
    }

}

