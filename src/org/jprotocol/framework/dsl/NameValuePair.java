package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.ensure;
import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;


public class NameValuePair implements INameValuePair {
    private final String _name;
    private final int value;
    private final IArgumentType[] argTypes;
    private final int startIndex;

    
    
    public NameValuePair(String name, int value) {
        this(name, value, 0, new IArgumentType[]{});
    }
    
    public NameValuePair(String name, int value, IProtocolLayoutType payloadType, String prefix, int startIndex) {
        this(name, value, startIndex, argsOf(payloadType, prefix, startIndex));
    }
    
    private static IArgumentType[] argsOf(IProtocolLayoutType payloadType, String prefix, int startIndex) {
        List<IArgumentType> l = new ArrayList<IArgumentType>();
        for (IArgumentType a : payloadType.getArguments()) {
            l.add(new ArgTypeOffsetProxy(a, prefix, startIndex));
        }
        return l.toArray(new IArgumentType[l.size()]);
    }
    

    public NameValuePair(String name, int value, int startIndex, IArgumentType...argTypes) {
       require(notNull(name));
       require(startIndex >= 0);
//      require(value >= 0); 
      this._name  = name;
      this.value = value;
      this.startIndex = startIndex;
      this.argTypes = argTypes;
      ensure(getName() == name);
      ensure(getValue() == value);
    }

    
    public String getName() { return _name; }
    
    public int getValue() { return value; }
    
    public IArgumentType[] getArgTypes() {
        return argTypes;
    }

    @Override
    public IArgumentType argOf(String name) {
        require(notNull(name));
        for (IArgumentType a: argTypes) {
            if (a.getName().equals(name)) return a;
        }
        return null;
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof INameValuePair)) return false;
        INameValuePair other = (INameValuePair) o;
        return getName().equals(other.getName()) && 
        	   getValue() == other.getValue() &&
        	   areSubArgsEqual(other);
    }
    
    private boolean areSubArgsEqual(INameValuePair other) {
    	if (getArgTypes().length != other.getArgTypes().length) {
    		return false;
    	}
    	for (int i = 0; i < getArgTypes().length; i++) {
    		if (!getArgTypes()[i].equals(other.getArgTypes()[i])) {
    			return false;
    		}
    	}
		return true;
	}

	public int hashCode() {
        return new HashCodeBuilder(17, 34).append(getName()).append(getValue()).toHashCode();
    }
    
    public String toString() { return _name + ", 0x" + Integer.toHexString(value); }

    @Override
    public int getPayloadStartIndex() {
        return startIndex;
    }

    @Override
    public boolean hasPayload() {
        return getPayloadStartIndex() > 0;
    }
}

class NVPOffsetProxy implements INameValuePair {

    private final INameValuePair target;
    private final IArgumentType[] argTypes;
    public NVPOffsetProxy(INameValuePair target, String prefix, int startIndex) {
        this(target, prefix, startIndex, "_");
    }
    public NVPOffsetProxy(INameValuePair target, String prefix, int startIndex, String delim) {
        this.target = target;
        argTypes = new IArgumentType[target.getArgTypes().length];
        for (int i = 0; i < target.getArgTypes().length; i++) {
            argTypes[i] = new ArgTypeOffsetProxy(target.getArgTypes()[i], prefix, startIndex, delim);
        }
    }

    public IArgumentType[] getArgTypes() {
        return argTypes;
    }
    
    @Override
    public IArgumentType argOf(String name) {
        require(notNull(name));
        for (IArgumentType a: argTypes) {
            if (a.getName().equals(name)) return a;
        }
        return null;
    }
    public String getName() {
        return target.getName();
    }

    public int getValue() {
        return target.getValue();
    }
    public boolean equals(Object o) {
        return target.equals(o);
    }
    
    public int hashCode() {
        return target.hashCode();
    }
    
    public String toString() { return target.toString(); }

    @Override
    public int getPayloadStartIndex() {
        return target.getPayloadStartIndex();
    }

    @Override
    public boolean hasPayload() {
        return target.hasPayload();
    }
    
}