package org.jprotocol.quantity;


/**
 * Two units that cannot be converted to each other
 * @author eliasa01
 *
 */
public class UnitIncompatible extends RuntimeException {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnitIncompatible(Unit unit, Unit toUnit) {
        super("Unit " + unit + " is incompatible with " + toUnit);
    }

}
