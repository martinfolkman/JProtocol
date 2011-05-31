package org.jprotocol.quantity;


class ResolutionConversion extends Conversion {
    private final double resolution;
    ResolutionConversion(Unit toUnit, double resolution) {
        super(toUnit);
        this.resolution = resolution;
    }
    @Override
    public double convert(double value) {
        return value * resolution;
    }

}
