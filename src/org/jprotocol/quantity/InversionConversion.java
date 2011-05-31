package org.jprotocol.quantity;
 

class InversionConversion extends Conversion {

    private final double factor;

    InversionConversion(Unit toUnit) {
        this(1, toUnit);
    }
    InversionConversion(double factor, Unit toUnit) {
        super(toUnit);
        this.factor = factor;
    }

    @Override
    public double convert(double value) {
        if (value == 0) return 0;
        return (factor / value);
    }

}
