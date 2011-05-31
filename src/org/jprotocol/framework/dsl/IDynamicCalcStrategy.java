package org.jprotocol.framework.dsl;

import org.jprotocol.quantity.Quantity;

public interface IDynamicCalcStrategy {
	Quantity calc(IArgumentType a, IProtocolMessage p); 
}
