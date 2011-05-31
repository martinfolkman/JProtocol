package org.jprotocol.framework.dsl;

import org.jprotocol.quantity.Unit;


public class ArgTypeOffsetProxy implements IArgumentType {      
    private final IArgumentType target;
    private final int startIndex;
    private final String name;
    private final IArgumentType[] children;
    private final INameValuePair[] values;
	private final EnumerationImpl enumeration;
    public ArgTypeOffsetProxy(IArgumentType target, String prefix, int startIndex) {
        this(target, prefix, startIndex, "_");
    }
    public ArgTypeOffsetProxy(IArgumentType target, String prefix, int startIndex, String delim) {
        this.target = target;
        this.startIndex = startIndex;
        this.name = prefix + delim + target.getName();
        children = new IArgumentType[target.getChildren().length];
        for (int i = 0; i < children.length; i++) {
            children[i] = new ArgTypeOffsetProxy(target.getChildren()[i], prefix, startIndex, delim);
        }
        if (target.isEnumType()) {
            values = new INameValuePair[target.getValues().length];
            for (int i = 0; i < values.length; i++) {
                values[i] = new NVPOffsetProxy(target.getValues()[i], prefix, startIndex, delim);
            }
            enumeration = new EnumerationImpl(values);
        } else {
            values = new INameValuePair[0];
            enumeration = null;
        }
        
    }

    public IArgumentType[] getChildren() {
        return children;
    }

    public int getEndByteIndex() {
        return startIndex + target.getEndByteIndex();
    }

    public int getMaxEntries() {
        return target.getMaxEntries();
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return target.getSimpleName();
    }
    public int getOffset() {
        return startIndex * 8 + target.getOffset();
    }

    public int getOffsetWithinByte() {
        return target.getOffsetWithinByte();
    }

    public int getSizeInBits() {
        return target.getSizeInBits();
    }

    public int getSizeInByteOfOneIndexedArg() {
        return target.getSizeInByteOfOneIndexedArg();
    }

    public int getSizeInBytes() {
        return target.getSizeInBytes();
    }

    public int getStartByteIndex() {
        return startIndex + target.getStartByteIndex();
    }

    public INameValuePair[] getValues() {
        return values;
    }
	@Override
	public IEnumeration getEnumeration() {
		return enumeration;
	}

    public boolean isBitField() {
        return target.isBitField();
    }

    public boolean isEnumType() {
        return target.isEnumType();
    }

    public boolean isIndexedType() {
        return target.isIndexedType();
    }

    public String nameOf(int value) {
        return target.nameOf(value);
    }

    public int valueOf(String n) {
        return target.valueOf(n);
    }

    public String toString() {
        return target.toString();
    }

    public INameValuePair nvpOf(int value) {
        try {
            return target.nvpOf(value);
        } catch (IllegalByteArrayValue e) {
            throw new IllegalByteArrayValue(e.getMessage(), this);
        }
    }


    public IArgumentType argOf(String n) {
        return target.argOf(n);
    }

    @Override
    public double getRealOffset() {
        return target.getRealOffset();
    }

    @Override
    public double getResolution() {
        return target.getResolution();
    }

    @Override
    public Unit getUnit() {
        return target.getUnit();
    }

    @Override
    public boolean isVirtual() {
        return target.isVirtual();
    }

    @Override
    public boolean isStr() {
        return target.isStr();
    }


    @Override
    public boolean isAddress() {
        return target.isAddress();
    }

    @Override
    public boolean isSize() {
        return target.isSize();
    }
	@Override
	public boolean isReal() {
		return target.isReal();
	}
//	@Override
//	public boolean equals(Object obj) {
//		if (!(obj instanceof IArgType)) return false;
//		return AbstractArgType.equals(this, (IArgType) obj);
//	}
//	@Override
//	public int hashCode() {
//		return target.getName().hashCode();
//	}
}
