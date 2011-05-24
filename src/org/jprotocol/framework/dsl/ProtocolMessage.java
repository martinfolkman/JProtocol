package org.jprotocol.framework.dsl;

import static org.jprotocol.quantity.Quantity.quantity;
import static org.jprotocol.util.Contract.check;
import static org.jprotocol.util.Contract.invariant;
import static org.jprotocol.util.Contract.isNotNull;
import static org.jprotocol.util.Contract.nonNegative;
import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jprotocol.framework.dsl.argiters.ArgOfIter;
import org.jprotocol.framework.dsl.argiters.DynOffsetSizeArgIter;
import org.jprotocol.framework.dsl.argiters.FindEndIndexIter;
import org.jprotocol.framework.dsl.argiters.GetAllArgsIter;
import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.ProtocolState;

import org.jprotocol.quantity.Quantity;

public final class ProtocolMessage implements IProtocolMessage {         
    private final IProtocolLayoutType type;
    private final List<Integer> data; 
    private final Decoder[] decoders; 
    private final Encoder[] encoders;
    private final boolean msbFirst;
    private final IProtocolState protocolState;
    public ProtocolMessage(IProtocolLayoutType type, boolean msbFirst, int...data) {
        this(type, false, msbFirst, null, data);
    }
    public ProtocolMessage(IProtocolLayoutType type, boolean msbFirst, IProtocolState protocolState, int...data) {
        this(type, false, msbFirst, protocolState, data);
    }
    public ProtocolMessage(IProtocolLayoutType type, boolean msbFirst) {
        this(type, true, msbFirst, null, new int[type.getSizeInBytes()]);
    }
    public ProtocolMessage(IProtocolLayoutType type, boolean msbFirst, IProtocolState protocolState) {
        this(type, true, msbFirst, protocolState, new int[type.getSizeInBytes()]);
    }

    public ProtocolMessage(IProtocolLayoutType type, boolean msbFirst, byte... data) {
        this(type, false, msbFirst, null, makeIntArray(data));
    }

    public ProtocolMessage(IProtocolLayoutType type, boolean msbFirst, IProtocolState protocolState, byte... data) {
        this(type, false, msbFirst, protocolState, makeIntArray(data));
    }

    private ProtocolMessage(IProtocolLayoutType type, boolean defaults, boolean msbFirst, IProtocolState protocolState, int... data) {
        require(notNull(data));
        require(notNull(type));  
        require(data.length >= 0);
        this.type = type;
        this.msbFirst = msbFirst;
        decoders = new Decoder[]{new IntDecoder(this), new BitFieldDecoder(this), new BinaryDataDecoder(this), new StringDecoder(this)};
        encoders = new Encoder[]{new IntEncoder(this), new BitFieldEncoder(this), new BinaryDataEncoder(this), new StringEncoder(this)};
        if (protocolState == null) this.protocolState = new ProtocolState(); else this.protocolState = protocolState; 
        this.data = new ArrayList<Integer>();
        for (int i = 0; i < data.length; i++) {
            this.data.add(data[i]);
        }
        require(this.data.size() >= 0);
        if (defaults) {
            setDefaultValues();
        }
    }
    
    private void setDefaultValues() {
        setDefaultValues(0);
    }
    
    private void setDefaultValues(int counter) {
        try {
            for (IArgumentType a : getArguments()) {
                setDefaultValue(a);
            }
        } catch (IllegalByteArrayValue e) {
            if (counter > 100) {
                throw e; //Avoid endless recursion
            }
            setDefaultValue(e.getArg());
            setDefaultValues(counter + 1);
        }
    }
    
    public void setDefaultValue(IArgumentType a) {
        require(isNotNull(a));
        if (a.isVirtual()) return;
        if (a.isEnumType()) {
            setValue(a, a.getValues()[0].getName());
        }
    }
    private static int[] makeIntArray(byte[] data) {
        int[] result = new int[data.length];
        for (int i = 0; i < data.length; i++) result[i] = intOf(data[i]); 
        return result;
    }

    private static int intOf(byte b) {
        return ~(b ^ 0xff) & b;
    }
    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public String getRealValueAsString(String name, int... indexes) {
        IArgumentType arg = argOf(name, indexes);
        if (arg.isEnumType()) {
            return getValue(arg);
        }
        return getRealValue(arg) + "";
    }
    @Override
    public void setRealValueAsString(String name, String value, int... indexes) {
        IArgumentType arg = argOf(name, indexes);
        if (arg.isEnumType()) {
            setValue(arg, value);
        } else {
            setRealValue(arg, Double.valueOf(value));
        }
    }
    
    public void setData(byte[] payload, int startIndex) {
        setData(makeIntArray(payload), startIndex);
    }
    
    public void setData(int[] payload, int startIndex) {
        require(notNull(payload));
        require(startIndex >= 0);
        for (int i = 0; i < payload.length; i++) {
            int v = payload[i];
            if (i + startIndex < data.size()) {
                data.set(startIndex + i, v);
            } else {
                data.add(v);
            }
        }
    }
    
    @Override
    public void setData(int index, int value) {
        while (index >= data.size()) {
            data.add(0);
        }
        data.set(index, value);
    }

    final public IProtocolLayoutType getProtocolType() { return type; }

    final public INameValuePair getValueAsNameValuePair(String name) {
        require(notNull(name));
        IArgumentType argType = argOf(name);
        check(notNull(argType), "Protocol: " + type.getName() + ", arg: " + name);
        return getValueAsNameValuePair(argType);
    }

    final public INameValuePair getValueAsNameValuePair(IArgumentType argType) {
        if (argType.isVirtual()) {
            return protocolState.getValue(getProtocolType(), argType);
        }
        return decodeToNV(argType);
    }
    
    public int noOfEntriesOf(IArgumentType arg) {
        require(arg.isIndexedType());
        int s1 = getSize() - arg.getStartByteIndex() + 1;
        int s2 = arg.getSizeInBytes() / arg.getMaxEntries();
        return Math.min(arg.getMaxEntries(), s1 / s2);
    }
    
    
    
    
    final public String getValue(IArgumentType argType) {
        INameValuePair nvp = getValueAsNameValuePair(argType);
        if (nvp == null) return null;
        return nvp.getName();
    }

    @Override
    public double getRealValue(String name, int...indexes) {
        return getRealValue(argOf(name, indexes));
    }

    @Override
    public Quantity getRealQuantity(String name, int...indexes) {
        return getRealQuantity(argOf(name, indexes));
    }
    @Override
    public Quantity getRealQuantity(IArgumentType argType) {
        return quantity(getRealValue(argType), argType.getUnit());
    }

    @Override
    public double getRealValue(IArgumentType argType) {
        require(!argType.isEnumType());
        invariant(notNull(getValueAsNameValuePair(argType)), "arg=", argType.getName());
        return argType.getRealOffset() + getValueAsNameValuePair(argType).getValue() * argType.getResolution();
    }

    @Override
    public void setRealValue(String name, double value, int... indexes) {
        setRealValue(argOf(name, indexes), value);
    }
    @Override
    public void setRealQuantity(String name, Quantity value, int...indexes) {
        setRealQuantity(argOf(name, indexes), value);
    }
    @Override
    public void setRealQuantity(IArgumentType argType, Quantity value) {
        setRealValue(argType, value.convert(argType.getUnit()).getValue());
    }

    @Override
    public void setRealValue(IArgumentType argType, double value) {
        require(!argType.isEnumType());
        setBitValue(argType,  (int) Math.round((value - argType.getRealOffset()) / argType.getResolution()));
    }

    final public INameValuePair getValueAsNameValuePair(String name, int...index) {
        if (index.length == 0) {
            return getValueAsNameValuePair(name);
        }
        return getValueAsNameValuePair(argOf(name, index)); 
    }
    
    final public String getValue(String name, int...index) {
        INameValuePair nvp = getValueAsNameValuePair(name, index);
        if (nvp == null) return null;
        return nvp.getName();
    }

    
    final public void setValue(String name, String value) {
        require(notNull(name));
        require(notNull(value));
        setValue(argOf(name), value);
    }

    public final void setValue(IArgumentType argType, String value) {
        require(notNull(argType));
        if (argType.isVirtual()) {
            setVirtualValue(argType, value);
        } else {
            encode(argType, value);
        }
    }
    public void adjustSize() {
        int newSize = new DynOffsetSizeArgIter(this).getSizeInBytes();
        while (data.size() > newSize) {
            data.remove(data.size() - 1);
        }
    }

    
    private void setVirtualValue(IArgumentType argType, String value) {
        protocolState.setValue(getProtocolType(), argType, value);
    }
    public final void setBitValue(String name, int bitValue, int...index) {
        setBitValue(argOf(name, index), bitValue);
    }
    
    public final void setBitValue(IArgumentType argType, int bitValue) {
        setValue(argType, getProtocolType().valueNameOf(argType.getName(), bitValue));
    }


    final public void setValue(String name, String value, int...index) {
        if (index.length == 0) {
            setValue(name, value);
            return;
        }
        require(notNull(value));
        setValue(argOf(name, index), value);
    }
    
    
    final public IArgumentType[] getArguments() {
        return new GetAllArgsIter(this).getArgs();
    }

    
    public IArgumentType argOf(String name, int...indexes) {
        require(notNull(name));
        IArgumentType argType = new ArgOfIter(this, name).foundArg;
        doArgCheck(argType, name);
        if (indexes.length == 0) {
            return argType;
        }
        return IndexArgumentType.indexedArgTypeOf(getProtocolType(), argType, indexes);
    }
    
    private void doArgCheck(IArgumentType argType, String name) {
        StringBuffer str = new StringBuffer("");
        if (argType == null) {
            str.append(name);
            str.append(" doesn't exist on ");
            str.append(getProtocolType().getName());
            str.append("\nThe following do exist:");
            for (IArgumentType a : getArguments()) {
                str.append(a.getName());
                str.append("\n");
            }
        }
        check(notNull(argType), str.toString());
    }
    
    
    private INameValuePair decodeToNV(IArgumentType argType) {
        try {
            for (Decoder d : decoders) {
                INameValuePair result = d.decodeToNV(argType);
                if (result != null) return result;
            }
            return null;
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalByteArrayValue("Index out of bounds for: " + argType.getName() + " in " + getProtocolType().getName() + ". Actual size: " + getSize() + ", expected size: " + getProtocolType().getSizeInBytes(), argType);
        } catch (IllegalByteArrayValue e) {
            throw new IllegalByteArrayValue("Protocol: " + getProtocolType().getName() + " "+ e.getMessage(), argType);
        }
    }

    public byte[] getData(){
        //Always return a copy, we don't want to expose internal state
        byte[] result = new byte[data.size()];
        for (int i = 0; i < result.length; i++) result[i] = (byte) data.get(i).intValue();
        return result;
    }
    public byte[] getData(int startIndex) {
        require(nonNegative(startIndex));
        require(startIndex <= data.size());
        byte[] d = getData();
        int size = d.length - startIndex;
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = d[i + startIndex]; 
        }
        return result;
    }
    public int[] getDataAsInts() {
        int[] result = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }
    

    public boolean equals(Object o) {
        if (null == o) return false;
        if (!(o instanceof IProtocolMessage)) return false;
        IProtocolMessage p = (IProtocolMessage) o;
        if (!getProtocolType().getName().equals(p.getProtocolType().getName())) {
            return false;
        }
        DynOffsetSizeArgIter sizeAndOffset = getOffsetAndSize();
        if (getSize() < sizeAndOffset.getSizeInBytes() || p.getSize() < sizeAndOffset.getSizeInBytes()) {
            return false;
        }
        int[] d1 = BitFilterUtil.filter(getDataAsInts(), sizeAndOffset.getOffsetInBits(), sizeAndOffset.getSizeInBits());
        int[] d2 = BitFilterUtil.filter(p.getDataAsInts(), sizeAndOffset.getOffsetInBits(), sizeAndOffset.getSizeInBits());
        for (int i = 0; i < sizeAndOffset.getSizeInBytes(); i++) {
            if (d1[i] != d2[i]) {
                return false;
            }
        }
        return true;
    }

    private DynOffsetSizeArgIter getOffsetAndSize() {
        return new DynOffsetSizeArgIter(this);
    }
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getProtocolType().getName()).toHashCode();
    }
    @Override
    public boolean hasPayload() {
        return getProtocolType().hasPayload();
    }

    
    public String toString() { return type.getName(); }
    
    public String readableData() { 
        return readableData(data); 
    }
    
    private static String readableData(List<Integer> data) {
        StringBuffer result = new StringBuffer(500);
        if (data == null) return result.toString();
        for (int b : data) {
            if (result.length() > 0) result.append(",");
            result.append(b);
        }
        return result.toString();
    }
    
    @Override
    public boolean isValid(String name, int...indexes) {
        return argOf(name, indexes) != null;
    }
    

    
    @Override
    public int getHeaderEndIndex() {
        return new FindEndIndexIter(this).endIndex;
    }
    

    private void encode(IArgumentType argType, String value) {
        for (Encoder e : encoders ) {
            if (e.encode(argType, value)) return;
        }
    }
    
    

    
    
    
    @Override
    public IProtocolMessage createSnapshot() {
        return new ProtocolMessage(getProtocolType(), msbFirst, protocolState.makeCopy(), getData());
    }
	@Override
	public boolean isMsbFirst() {
		return msbFirst;
	}

}

