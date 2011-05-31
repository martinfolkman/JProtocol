package org.jprotocol.quantity; 


public abstract class Conversion {
    protected final Unit toUnit;

    Conversion(Unit toUnit) {
        this.toUnit = toUnit;
    }

    public Unit getToUnit() {
        return toUnit;
    }

    abstract public double convert(double value);
    
}
