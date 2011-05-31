package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.check;
import static org.jprotocol.util.Contract.neverGetHere;
import static org.jprotocol.util.Contract.notNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jprotocol.framework.dsl.argiters.ArgTypeArrayIter;
import org.jprotocol.framework.dsl.argiters.FindParentArgType;

import org.jprotocol.quantity.Unit;


public class IndexArgumentType extends AbstractArgumentType {  
   
    private final List<IArgumentType> argTypes = new ArrayList<IArgumentType>();
    private final Map<String, IArgumentType> argMap = new HashMap<String, IArgumentType>(); 
    private final int maxEntries;

    public IndexArgumentType(String name, int maxEntries, IArgumentType... argTypes) {
        super(name, sizeInBits(maxEntries, argTypes), offsetOf(argTypes));
        this.maxEntries = maxEntries;
        for (IArgumentType at : argTypes){
            this.argTypes.add(at);
            argMap.put(at.getName(), at);
        }
    }

    private static int offsetOf(IArgumentType[] argTypes) {
        int o = -1;
        for (IArgumentType a : argTypes) {
            if (o < 0) {
                o = a.getOffset();
            } else {
                o = Math.min(o, a.getOffset());
            }
        }
        return o;
    }

    private static int sizeInBits(int maxEntries, IArgumentType[] argTypes) {
        ArgTypeArrayIter iter = new ArgTypeArrayIter(argTypes);
        if (iter.getArgAtGreatestOffset() == null) {
            return 0;
        }
        return (iter.getArgAtGreatestOffset().getOffset() + iter.getArgAtGreatestOffset().getSizeInBits() - iter.getArgAtSmallestOffset().getOffset()) * maxEntries;
    }

    public NameValuePair[] getValues() {
        neverGetHere();
        return null;
    }
	@Override
	public IEnumeration getEnumeration() {
        neverGetHere();
		return null;
	}

    public boolean isEnumType() {
        return false;
    }

    public String nameOf(int value) {
        neverGetHere();
        return null;
    }

    public int valueOf(String name) {
        neverGetHere();
        return 0;
    }


    public IArgumentType[] getChildren() {
        IArgumentType[] result = new IArgumentType[argTypes.size()];
        return argTypes.toArray(result);
    }


    public int getMaxEntries() {
        return maxEntries;
    }


    public int getSizeInByteOfOneIndexedArg() {
        return byteSizeOf(getSizeInBits()) / getMaxEntries();
    }

    public INameValuePair nvpOf(int value) {
        neverGetHere();
        return null;
    }

    public IArgumentType argOf(String name) {
        return argMap.get(name);
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
    public Unit getUnit() {
        neverGetHere();
        return null;
    }

    @Override
    public boolean isVirtual() {
        return false;
    }

    @Override
    public boolean isStr() {
        return false;
    }

    @Override
    public boolean isAddress() {
        return false;
    }

    @Override
    public boolean isSize() {
        return false;
    }
    
    
    public static IArgumentType indexedArgTypeOf(IProtocolLayoutType type, IArgumentType arg, int[] indexes) {
        return indexedArgOf(arg, calcOffset(type, arg, indexes));
        
    }
    
    
    private static int calcOffset(IProtocolLayoutType type, IArgumentType arg, int...indexes) {
        int result = 0;
        IArgumentType currArg = arg;
        for (int i = indexes.length - 1; i >= 0; i--) {
            int index = indexes[i];
            check(index >= 0);
            currArg = parentOf(type, currArg);
            check(notNull(currArg));
            check(currArg.isIndexedType());
            check(index < currArg.getMaxEntries());
            result += index * currArg.getSizeInByteOfOneIndexedArg() * 8;
        }
        return result;
    }

    private static IArgumentType indexedArgOf(IArgumentType arg, int ofs) {
//        return new ArgTypeOffsetProxy(arg, "", index * parent.getSizeInBytes());
        INameValuePair[] values = new NameValuePair[0]; 
        if (arg.isEnumType()) {
            values = arg.getValues();
        }
        if (arg.isEnumType()) {
            return new ArgumentType(arg.getName(), arg.getSizeInBits(), arg.getOffset() + ofs, values);
        }
        return new ArgumentType(arg.getName(), arg.getSizeInBits(), arg.getOffset() + ofs, arg.getRealOffset(), arg.getResolution(), arg.getUnit());
    }
    

    
    private static IArgumentType parentOf(IProtocolLayoutType type, IArgumentType argType) {
        return new FindParentArgType(argType, type).parentArgType;
    }


}
