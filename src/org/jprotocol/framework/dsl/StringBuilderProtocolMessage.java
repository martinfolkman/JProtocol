package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.neverGetHere;

import org.jprotocol.quantity.Quantity;


public class StringBuilderProtocolMessage implements IProtocolMessage {
    private final IProtocolMessage realProtocol; 
    private final StringBuffer expectations = new StringBuffer();
 
    public StringBuilderProtocolMessage(IProtocolLayoutType type) {
        realProtocol = new ProtocolMessage(type, false);
    }
    
    @Override
    public String toString() {
        if (expectations.toString().isEmpty()) {
            return "(\"" + getProtocolName() + "\")";
        }
        return "(\"" + getProtocolName() + "\" " + expectations.toString() + ")";
    }
    
    private String getProtocolName() {
        return realProtocol.getProtocolType().getProtocolName();
    }

    @Override
    public void adjustSize() {
        realProtocol.adjustSize();
    }

    @Override
    public IArgumentType argOf(String name, int... indexes) {
        return realProtocol.argOf(name, indexes);
    }

    @Override
    public IArgumentType[] getArguments() {
        return realProtocol.getArguments();
    }

    @Override
    public byte[] getData() {
        return realProtocol.getData();
    }

    @Override
    public byte[] getData(int startIndex) {
        return realProtocol.getData(startIndex);
    }

    @Override
    public int[] getDataAsInts() {
        return realProtocol.getDataAsInts();
    }


    @Override
    public int getHeaderEndIndex() {
        return realProtocol.getHeaderEndIndex();
    }

    @Override
    public IProtocolLayoutType getProtocolType() {
        return realProtocol.getProtocolType();
    }

    @Override
    public Quantity getRealQuantity(String name, int... indexes) {
        return realProtocol.getRealQuantity(name, indexes);
    }

    @Override
    public Quantity getRealQuantity(IArgumentType argType) {
        return realProtocol.getRealQuantity(argType);
    }

    @Override
    public double getRealValue(IArgumentType argType) {
        return realProtocol.getRealValue(argType);
    }

    @Override
    public double getRealValue(String name, int... indexes) {
        return realProtocol.getRealValue(name, indexes);
    }

    @Override
    public String getRealValueAsString(String name, int... index) {
        return realProtocol.getRealValueAsString(name, index);
    }

    @Override
    public int getSize() {
        return realProtocol.getSize();
    }

    @Override
    public String getValue(String name, int... index) {
        return realProtocol.getValue(name, index);
    }

    @Override
    public String getValue(IArgumentType argType) {
        return realProtocol.getValue(argType);
    }

    @Override
    public INameValuePair getValueAsNameValuePair(String name, int... index){
        return realProtocol.getValueAsNameValuePair(name, index);
    }

    @Override
    public INameValuePair getValueAsNameValuePair(IArgumentType argType) {
        return realProtocol.getValueAsNameValuePair(argType);
    }

    @Override
    public boolean hasPayload() {
        return realProtocol.hasPayload();
    }

    @Override
    public boolean isValid(String name, int... indexes) {
        return realProtocol.isValid(name, indexes);
    }

    @Override
    public int noOfEntriesOf(IArgumentType arg) {
        return realProtocol.noOfEntriesOf(arg);
    }

    @Override
    public String readableData() {
        return realProtocol.readableData();
    }

    @Override
    public void setBitValue(String name, int bitValue, int... index) {
        realProtocol.setBitValue(name, bitValue, index);
        addToExpectations(name, index);
    }

    @Override
    public void setBitValue(IArgumentType argType, int bitValue) {
        realProtocol.setBitValue(argType, bitValue);
        addToExpectations(argType);
    }

    @Override
    public void setData(byte[] payload, int startIndex) {
        neverGetHere();
    }

    @Override
    public void setData(int[] payload, int startIndex) {
        neverGetHere();
    }

    @Override
    public void setRealQuantity(String name, Quantity value, int... indexes) {
        realProtocol.setRealQuantity(name, value, indexes);
        addToExpectations(name, indexes);
    }

    @Override
    public void setRealQuantity(IArgumentType argType, Quantity value) {
        realProtocol.setRealQuantity(argType, value);
        addToExpectations(argType);
    }

    @Override
    public void setRealValue(IArgumentType argType, double value) {
        realProtocol.setRealValue(argType, value);
        addToExpectations(argType);
    }

    @Override
    public void setRealValue(String name, double value, int... indexes) {
        realProtocol.setRealValue(name, value, indexes);
        addToExpectations(name, indexes);
    }

    @Override
    public void setRealValueAsString(String name, String value, int... index) {
        realProtocol.setRealValueAsString(name, value, index);
        addToExpectations(name, index);
    }

    @Override
    public void setValue(String name, String value, int... index) {
        realProtocol.setValue(name, value, index);
        addToExpectations(name);
    }


    @Override
    public void setValue(IArgumentType argType, String value) {
        realProtocol.setValue(argType, value);
        addToExpectations(argType);
    }

    private void addToExpectations(String name, int...indexes) {
        addToExpectations(realProtocol.argOf(name, indexes));
    }
    /**
     * TODO support of indexes in the expectation string
     * @param argType
     */
    private void addToExpectations(IArgumentType argType) {
        if (expectations.length() != 0) {
            expectations.append(" ");
        }
        expectations.append("(\"");
        expectations.append(argType.getName());
        expectations.append("\" \"");
        expectations.append(realProtocol.getValue(argType.getName()));
        expectations.append("\")");
    }

    @Override
    public void setDefaultValue(IArgumentType arg) {
        realProtocol.setDefaultValue(arg);
    }

    @Override
    public IProtocolMessage createSnapshot() {
        neverGetHere();
        return null;
    }

	@Override
	public boolean isMsbFirst() {
		return false;
	}

	@Override
	public void setData(int index, int value) {
        neverGetHere();
	}    

}
