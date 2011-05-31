package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.check;
import static org.jprotocol.util.Contract.isNotNull;
import static org.jprotocol.util.Contract.neverGetHere;
import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import java.util.HashMap;
import java.util.Map;

import org.jprotocol.quantity.Unit;



public class ArgumentType extends AbstractArgumentType {      
    private final INameValuePair[] values;
    private final Map<String, INameValuePair> valueMap = new HashMap<String, INameValuePair>();
    private final Map<Integer, INameValuePair> nameMap = new HashMap<Integer, INameValuePair>();
    private final double resolution;
    private final double realOffset;
    private final Unit unit;
    private final boolean virtual;
	private final EnumerationImpl enumeration;

    public ArgumentType(String name, int size, int offset, IEnumeration e) {
    	this(name, size, offset, e.getValues());
    }	
    public ArgumentType(String name, int size, int offset, INameValuePair... values) {
        this(name, size, offset, false, values);
    }
    
    public ArgumentType(String name, int size, int offset, boolean virtual, INameValuePair... values) {
        super(name, size, offset);
        require(notNull(values));
        this.realOffset = 0;
        this.resolution = 1;
        this.virtual = virtual;
        this.unit = Unit.noUnit;
        this.values = values;
        for (INameValuePair v : values) {
            valueMap.put(v.getName(), v);
            nameMap.put(v.getValue(), v);
        }
        if (isEnumType()) {
        	enumeration = new EnumerationImpl(values);
        } else {
        	enumeration = null;
        }
    }
    
    public ArgumentType(String name, int size, int offset, double realOffset, double resolution, Unit unit) {
        super(name, size, offset);
        require(resolution > 0);
        this.realOffset = realOffset;
        this.resolution = resolution;
        this.virtual = false;
        this.unit = unit;
        this.values = new INameValuePair[]{};
        this.enumeration = null;
    }

    @Override
    public INameValuePair[] getValues() {
        require(isEnumType());
        return values; 
    }
    
	@Override
	public IEnumeration getEnumeration() {
		return enumeration;
	}

    
    @Override
    public int valueOf(String nameOfValue) {
        int result;
        if (isEnumType()) {
            check(isNotNull(valueMap.get(nameOfValue)), getName(), " doesn't contain this value: ", nameOfValue, valueMap);
            result = valueMap.get(nameOfValue).getValue();
        } else {
            result = Integer.valueOf(nameOfValue);
        }
        // TODO: Contract not valid for dynamic size arguments
//        ensure(result < Math.pow(2, getSizeInBits()), "Arg:", getName(), ", value:", result, ", max:", Math.pow(2, getSizeInBits()));
        return result;
    }

    @Override
    public boolean isEnumType() {
        return values.length > 0;
    }
    
    @Override
    public INameValuePair nvpOf(int value) {
        if (nameMap.get(value) == null) throw new IllegalByteArrayValue("Illegal value: 0x" + Integer.toHexString(value) + " for arg: " + getName() + " can't be mapped to a name, offset: " + getStartByteIndex(), this);
//            check(notNull(nameMap.get(value)), "Int value: " + value + " for arg: " + getName() + " can't be mapped to a name");
        return nameMap.get(value);
    }
    
    @Override
    public String nameOf(int value) {
        if (isEnumType()) {
            return nvpOf(value).getName();
        }
        return value + "";
    }
    
    @Override
    public IArgumentType[] getChildren() {
        return new IArgumentType[]{};
    }
    
    @Override
    public int getMaxEntries() {
        neverGetHere();
        return 0;
    }
    
    @Override
    public int getSizeInByteOfOneIndexedArg() {
        neverGetHere();
        return 0;
    }
    
    @Override
    public IArgumentType argOf(String name) {
        return null;
    }
    
    @Override
    public double getRealOffset() {
        return realOffset;
    }
    
    @Override
    public double getResolution() {
        return resolution;
    }
    
    @Override
    public Unit getUnit() {
        return unit;
    }
    
    @Override
    public boolean isVirtual() {
        return virtual;
    }
    
    @Override
    public boolean isStr() {
        return false;
    }

    @Override
    public boolean isAddress() {
        return false;
    }

    @Override
    public boolean isSize() {
        return false;
    }

}
