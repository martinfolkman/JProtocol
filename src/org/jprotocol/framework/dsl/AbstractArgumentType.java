package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import org.apache.commons.lang.builder.HashCodeBuilder;


public abstract class AbstractArgumentType implements IArgumentType {  
    private final String name;
    private final int sizeInBits;
    private final int offset;

    AbstractArgumentType(String name, int sizeInBits, int offset) {
        require(notNull(name));
        require(sizeInBits > 0, "Size is zero for ", name);
        this.name = name;
        this.sizeInBits = sizeInBits;
        this.offset = offset;
    }
    
    @Override
    final public String getName() { return name; }

    @Override
    final public String getSimpleName() { return name; }
    
    @Override
    public int getSizeInBits() { return sizeInBits; }

    @Override
    final public int getOffset() { return offset; }

    @Override
    public String toString() { 
        return name + ", Bit-size:" + getSizeInBits() + ", Bit-offset: " + offset; 
    }
    
    @Override
    final public int getStartByteIndex() {
        return getOffset() / 8;
    }
    
    @Override
    final public int getEndByteIndex() {
        return getEndByteIndex(this);
    }
    
    @Override
    final public int getSizeInBytes() {
        return byteSizeOf(getSizeInBits());
    }

    @Override
    final public boolean isBitField() {
        return isBitField(this);
    }

    @Override
    final public int getOffsetWithinByte() {
        return getOffset() - getStartByteIndex() * 8;
    }

    @Override
    final public boolean isIndexedType() { 
        return getChildren().length > 0; 
    }
    
	@Override
	public final boolean isReal() {
		return !(isAddress() || isEnumType() || isIndexedType() || isSize() || isStr() || isVirtual());
	}

    
    static int getOffsetWithinByte(IArgumentType argType) {
        return argType.getOffset() - argType.getStartByteIndex() * 8;
    }

    public static int byteSizeOf(int bits) {
        return (bits / 8) + (((bits % 8) == 0) ? 0 : 1);
    }

    static int getStartByteIndex(IArgumentType argType) {
        return argType.getOffset() / 8;
    }
    
    static int getEndByteIndex(IArgumentType argType) {
        return argType.getStartByteIndex() + argType.getSizeInBytes() - 1;
    }
    
    static boolean isBitField(IArgumentType argType) {
        return argType.getSizeInBits() < 8;
    }

    @Override
    public int hashCode() {
    	return new HashCodeBuilder(17, 34).
    		append(getName()).
    		append(getOffset()).
    		append(getSizeInBits()).
    		toHashCode();
    }
    @Override
    public boolean equals(Object obj) {
    	if (!(obj instanceof IArgumentType)) {
    		return false;
    	}
    	return equals(this, (IArgumentType) obj);
    }
    public static boolean equals(IArgumentType a1, IArgumentType a2) {
    	boolean result = 
    		   a1.getName().equals(a2.getName()) &&
    		   a1.isAddress() == a2.isAddress() &&
    		   a1.isBitField() == a2.isBitField() &&
    		   a1.isEnumType() == a2.isEnumType() &&
    		   a1.isIndexedType() == a2.isIndexedType() &&
    		   a1.isReal() == a2.isReal() &&
    		   a1.isSize() == a2.isSize() &&
    		   a1.isStr() == a2.isStr() &&
    		   a1.isVirtual() == a2.isVirtual() &&
    		   a1.getOffset() == a2.getOffset() &&
    		   a1.getSizeInBits() == a2.getSizeInBits();
    	
    	if (a1.isIndexedType()) {
    		result = result && 
    				 areChildrenEqual(a1, a2) &&
    				 a1.getMaxEntries() == a2.getMaxEntries();
    	}
    	if (a1.isEnumType()) {
    		result = result && areValueEqual(a1, a2);
    	}
    	if (a1.isReal()) {
 		   result = result && a1.getRealOffset() == a2.getRealOffset() &&
 		   a1.getResolution() == a2.getResolution() &&
 		   a1.getUnit() == a2.getUnit();

    	}
    	return result;
    }

	private static boolean areValueEqual(IArgumentType a1, IArgumentType a2) {
		if (a1.getValues().length != a2.getValues().length) {
			return false;
		}
		for(int i = 0; i < a1.getValues().length; i++) {
			if (!a1.getValues()[i].equals(a2.getValues()[i])) {
				return false;
			}
		}
		return true;
	}

	private static boolean areChildrenEqual(IArgumentType a1, IArgumentType a2) {
		if (a1.getChildren().length != a2.getChildren().length) {
			return false;
		}
		for (int i = 0; i < a1.getChildren().length; i++) {
			if (!a1.getChildren()[i].equals(a2.getChildren()[i])) {
				return false;
			}
		}
		return true;
	}

}
