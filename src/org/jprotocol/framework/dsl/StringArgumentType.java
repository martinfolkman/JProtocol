package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.neverGetHere;

import org.jprotocol.quantity.Quantity;
import org.jprotocol.quantity.Unit;


public class StringArgumentType extends AbstractArgumentType {
 
    public StringArgumentType(String name, Quantity size, Quantity offset) {
        super(name, (int)size.convert(Unit.bitSize).getValue(), (int)offset.convert(Unit.bitSize).getValue());
    }

    @Override
    public IArgumentType argOf(String name) {
        neverGetHere();
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
        neverGetHere();
        return 0;
    }

    @Override
    public double getResolution() {
        neverGetHere();
        return 0;
    }

    @Override
    public int getSizeInByteOfOneIndexedArg() {
        neverGetHere();
        return 0;
    }

    @Override
    public Unit getUnit() {
        neverGetHere();
        return null;
    }

    @Override
    public INameValuePair[] getValues() {
        neverGetHere();
        return null;
    }
	@Override
	public IEnumeration getEnumeration() {
        neverGetHere();
		return null;
	}

    @Override
    public boolean isEnumType() {
        return false;
    }

    @Override
    public boolean isVirtual() {
        return false;
    }

    @Override
    public String nameOf(int value) {
        neverGetHere();
        return null;
    }

    @Override
    public INameValuePair nvpOf(int value) {
        neverGetHere();
        return null;
    }

    @Override
    public int valueOf(String name) {
        neverGetHere();
        return 0;
    }

    @Override
    public boolean isStr() {
        return true;
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
