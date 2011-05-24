package org.jprotocol.quantity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


/**
 * Supports addition, subtraction and conversion of quantities i.e. values including units
 * Quantity is an immutable object like java.lang.String. 
 * @author eliasa01
 */
public class Quantity {
    private final static DecimalFormatSymbols fs = new DecimalFormatSymbols();
    private final static DecimalFormat df        = new DecimalFormat("0.000000#");
    static {
        fs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(fs);
    }
    private final Unit unit; 
    private final double value;
    
    public static Quantity quantity(double value, Unit unit) {
        return new Quantity(value, unit);
    }
    
    private Quantity(double value, Unit unit) {
        this.value = Double.valueOf(df.format(value));
        this.unit = unit;
    }
    public double getValue() {
        return value;
    }
    
    /**
     * Add two quantities
     * @param quantity
     * @return the new quantity which has the same unit as this quantity, if two units aren't the same a conversion takes place first
     * @note if the units of the quantities are not compatible a UnitIncompatible exception is thrown 
     */
    public Quantity add(Quantity quantity) {
        return quantity(getValue() + quantity.convert(getUnit()).getValue(), getUnit());
    }
    /**
     * Subtract two quantities
     * @param quantity
     * @return the new quantity which has the same unit as this quantity, if two units aren't the same a conversion takes place first
     * @note if the units of the quantities are not compatible a UnitIncompatible exception is thrown 
     */
    public Quantity subtract(Quantity quantity) {
        return quantity(getValue() - quantity.convert(getUnit()).getValue(), getUnit());
    }
    public Quantity multiply(Quantity quantity) {
        return quantity(getValue() * quantity.convert(getUnit()).getValue(), getUnit());
    }
    public Quantity divide(Quantity quantity) {
        if (quantity.getUnit() == Unit.noUnit) {
            return quantity(getValue() / quantity.getValue(), getUnit());
        }
        throw new UnitIncompatible(getUnit(), quantity.getUnit());
    }
    public Quantity divide(double otherValue) {
        return quantity(getValue() / otherValue, getUnit());
    }
    
    /**
     * Convert this quantity to another unit
     * @param quantity
     * @return the new quantity which has the same unit passed as argument
     * @note if the units of the quantities are not compatible a UnitIncompatible exception is thrown 
     */
    public Quantity convert(Unit toUnit) {
        return quantity(getUnit().convert(getValue(), toUnit), toUnit);
    }

    /**
     * Can this quantity be converted to the quantity passed in as argument
     * @param quantity
     * @return
     */
    public boolean isCompatible(Quantity quantity) {
        try {
            quantity.convert(getUnit());
            return true;
        } catch (UnitIncompatible e) {
            //
        }
        return false;
    }
    
    /**
     * Is this quantity bigger than the passed quantity, if two units aren'r the same a conversion takes place first
     * @param quantity
     * @return
     */
    public boolean isBigger(Quantity quantity) {
        return getValue() > quantity.convert(getUnit()).getValue();
    }
    
    /**
     * Is this quantity bigger or equal than the passed quantity, if two units aren't the same a conversion takes place first
     * @param quantity
     * @return
     */
    public boolean isBiggerOrEqual(Quantity quantity) {
        return getValue() >= quantity.convert(getUnit()).getValue();
    }
    
    /**
     * Is this quantity smaller than the passed quantity, if two units aren't the same a conversion takes place first
     * @param quantity
     * @return
     */
    public boolean isSmaller(Quantity quantity) {
        return getValue() < quantity.convert(getUnit()).getValue();
    }
    
    /**
     * Is this quantity smaller or equal than the passed quantity, if two units aren't the same a conversion takes place first
     * @param quantity
     * @return
     */
    public boolean isSmallerOrEqual(Quantity quantity) {
        return getValue() <= quantity.convert(getUnit()).getValue();
    }
    
    /**
     * Is this quantity equal to the passed quantity, if two units aren't the same a conversion takes place first
     * @param quantity
     * @return
     */
    public boolean isEqual(Quantity quantity) {
        return getValue() == quantity.convert(getUnit()).getValue();
    }
    
    public Quantity abs() {
        return quantity(Math.abs(getValue()), getUnit());
    }
    
    /**
     * Reduce number of max decimals
     * @param nrOfDecimals
     * @return
     */
    public Quantity decimals(int nrOfDecimals) {
        return quantity(Double.valueOf(df.format(value)), getUnit());
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Quantity)) return false;
        Quantity q = (Quantity) o;
        return getValue() == q.getValue() && getUnit() == q.getUnit();
    }
    @Override
    public int hashCode() {
        return new Double(getValue()).hashCode() << 16 + getUnit().hashCode();
    }
    
    @Override
    public String toString() {
        return getValue() + " " + getUnit();
    }
    public Unit getUnit() {
        return unit;
    }

    public Quantity min(Quantity quantity) {
        return quantity(Math.min(getValue(), quantity.convert(getUnit()).getValue()), getUnit());
    }
    public Quantity max(Quantity quantity) {
        return quantity(Math.max(getValue(), quantity.convert(getUnit()).getValue()), getUnit());
    }
}
