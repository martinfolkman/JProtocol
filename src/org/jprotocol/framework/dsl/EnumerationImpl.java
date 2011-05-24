package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.notEmpty;
import static org.jprotocol.util.Contract.require;

import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class EnumerationImpl implements IEnumeration {
	private final INameValuePair[] values;

	
	public static IEnumeration e(INameValuePair...values) {
		return new EnumerationImpl(values);
	}
	
	public static INameValuePair value(String name, int value) {
		return new NameValuePair(name, value);
	}

	public static INameValuePair[] values(INameValuePair...values) {
		return values;
	}
	public EnumerationImpl(List<INameValuePair> values) {
		this(values.toArray(new INameValuePair[values.size()]));
	}
	public EnumerationImpl(INameValuePair...values) {
		require(notEmpty(values));
		this.values = values;
	}

	@Override
	public INameValuePair[] getValues() {
		return values;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().toHashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IEnumeration)) {
			return false;
		}
		IEnumeration other = (IEnumeration) obj;
		return areValuesEqual(other.getValues());
	}
	
	private boolean areValuesEqual(INameValuePair[] otherValues) {
		if (getValues().length != otherValues.length) {
			return false;
		}
		for (int i = 0; i < getValues().length; i++) {
			if (!getValues()[i].equals(otherValues[i])) {
				return false;
			}
		}
		return true;
	}
}
