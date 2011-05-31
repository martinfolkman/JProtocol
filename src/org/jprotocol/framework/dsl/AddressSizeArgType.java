package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.neverGetHere;
import static org.jprotocol.util.Contract.require;

import org.jprotocol.quantity.Unit;


public class AddressSizeArgType extends AbstractArgumentType {
    private final boolean isAddress;

    AddressSizeArgType(String name, int sizeInBits, int offset, boolean isAddress) {
        super(name, sizeInBits, offset);
        this.isAddress = isAddress;
    }

    @Override
    public IArgumentType argOf(String name) {
        return null;
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
    public double getRealOffset() {
        return 0;
    }

    @Override
    public double getResolution() {
        return 1;
    }

    @Override
    public int getSizeInByteOfOneIndexedArg() {
        neverGetHere();
        return 0;
    }

    @Override
    public Unit getUnit() {
        return Unit.noUnit;
    }

    @Override
    public INameValuePair[] getValues() {
        require(isEnumType());
        return null;
    }
	@Override
	public IEnumeration getEnumeration() {
        require(isEnumType());
		return null;
	}

    @Override
    public boolean isEnumType() {
        return false;
    }

    @Override
    public boolean isStr() {
        return false;
    }

    @Override
    public boolean isVirtual() {
        return false;
    }

    @Override
    public String nameOf(int value) {
        return value + "";
    }

    @Override
    public INameValuePair nvpOf(int value) {
        neverGetHere();
        return null;
    }

    @Override
    public int valueOf(String name) {
        return Integer.valueOf(name);
    }

    @Override
    public boolean isAddress() {
        return isAddress;
    }

    @Override
    public boolean isSize() {
        return !isAddress;
    }



}
