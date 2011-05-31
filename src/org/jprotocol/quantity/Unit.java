package org.jprotocol.quantity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author eliasa01
 */
public enum Unit {
    noUnit("noUnit"),
    mV("mV"), 
    V("V"),
    V_Per_s("V_Per_s"),
    ms("ms"),
    microseconds("microseconds"),
    s("s"),
    minutes("minutes"),
    h("h"),
    months("months"),
    days("days"),
    years("years"),
    bpm("bpm"), 
    hz("hz"),
    uA("uA"),
    mA("mA"),
    A("A"),
    ohm("ohm"),
    percent("percent"),
    ADC_Counts("ADC_Counts"),
    count("count"),
    ppm("ppm"),
    mAh("mAh"),
    mg("mg"),
    bitSize("bitSize"),
    byteSize("byteSize"),
    shortSize("shortSize"),
    intSize("intSize")
    ;
    
    private static final Map<String, Unit> unitMap = new HashMap<String, Unit>();
    static {
        for (Unit u : Unit.values()) {
            unitMap.put(u.toString(), u);
        }
        addResolutionConversion(V, mV, 1000);
        addResolutionConversion(s, ms, 1000);
        addResolutionConversion(minutes, s, 60);
        addResolutionConversion(h, minutes, 60);
        addResolutionConversion(days, h, 24);
        addResolutionConversion(intSize, shortSize, 2);
        addResolutionConversion(shortSize, byteSize, 2);
        addResolutionConversion(byteSize, bitSize, 8);

        addInversionConversion(60000, bpm, ms);
        addInversionConversion(hz, s);
        addResolutionConversion(mA, uA, 1000);
        addResolutionConversion(A, mA, 1000);
        addMultiplicationConversion(ohm, A, V);
        
    }
    private final String name;
    private final List<Conversion> conversions = new ArrayList<Conversion>();
    private final List<MultiplicationConversion> multiplicationConversions = new ArrayList<MultiplicationConversion>();
    Unit(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    public double convert(double value, Unit toUnit) {
        return convert(value, toUnit, new HashSet<Conversion>());
    }
    public double convert(double value, Unit toUnit, Set<Conversion> alreadyVisited) {
        if (this == toUnit) return value;
        for (Conversion c : conversions) {
           if (!alreadyVisited.contains(c)) {
               alreadyVisited.add(c);
               if (c.getToUnit() == toUnit) {
                   return c.convert(value);
               }
               try {
                   if (c.getToUnit() != this) {  //Avoid endless recursion
                       return c.getToUnit().convert(c.convert(value), toUnit, alreadyVisited);
                   }
               } catch (UnitIncompatible e) {
                   //try next
               }
           }
        }
        throw new UnitIncompatible(this, toUnit);
    }

    public static boolean exists(String name) {
        return unitMap.get(name) != null;
    }
    public static Unit unitOf(String name) {
        assert exists(name): name + " doesn't exists";
        return unitMap.get(name);
    }
    
    void addConversion(Conversion conversion) {
        conversions.add(conversion);
    }
    
    @Override
    public String toString() {
        return this == noUnit ? "" : name;
    }
    private static void addInversionConversion(Unit fromUnit, Unit toUnit) {
        fromUnit.addConversion(new InversionConversion(toUnit));
        toUnit.addConversion(new InversionConversion(fromUnit));
    }
    private static void addInversionConversion(double factor, Unit fromUnit, Unit toUnit) {
        fromUnit.addConversion(new InversionConversion(factor, toUnit));
        toUnit.addConversion(new InversionConversion(factor, fromUnit));
    }
    private static void addResolutionConversion(Unit fromUnit, Unit toUnit, double resolution) {
        fromUnit.addConversion(new ResolutionConversion(toUnit, resolution));
        toUnit.addConversion(new ResolutionConversion(fromUnit, 1 / resolution));
        
    }
    private static void addMultiplicationConversion(Unit fromUnit, Unit otherUnit, Unit toUnit) {
        fromUnit.addMultiplicationConversion(new MultiplicationConversion(otherUnit, toUnit));
    }
    private void addMultiplicationConversion(MultiplicationConversion multiplicationConversion) {
        multiplicationConversions.add(multiplicationConversion);
    }

}


