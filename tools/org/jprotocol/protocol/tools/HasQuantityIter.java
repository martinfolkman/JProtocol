package org.jprotocol.protocol.tools;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.IProtocolLayoutType;
import org.jprotocol.framework.dsl.argiters.ArgTypeIter;

public class HasQuantityIter extends ArgTypeIter {
	private boolean quantityFound;
	protected HasQuantityIter(IProtocolLayoutType type) {
		super(type);
		iterate();
	}

	@Override
	protected boolean iter(IArgumentType arg) {
		if (arg.isReal()) {
			quantityFound = true;
			return false;
		}
		return true;
	}
	public boolean hasQuantity() {
		return quantityFound;
	}

}
