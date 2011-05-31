package org.jprotocol.framework.dsl;

import static org.jprotocol.quantity.Quantity.quantity;
import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import java.util.ArrayList;
import java.util.List;

import org.jprotocol.quantity.Quantity;
import org.jprotocol.quantity.Unit;
import org.jprotocol.util.IName;


abstract public class AbstractMemoryLayoutFactory implements IName { 
    private static final int BYTE_SIZE = 8;
    private final boolean includePayload;
    private final String name;
    /**
     * @param includePayload include payload or sub protocols
     */
    protected AbstractMemoryLayoutFactory(String name, boolean includePayload) {
    	require(notNull(name));
        this.name = name;
        this.includePayload = includePayload;
    }

    @Override
    public final String getName() {
        return name;
    }

    /**
     * An array of arguments
     * @param argTypes
     * @return An array of arguments
     */
    protected final IArgumentType[] args(IArgumentType... argTypes) {
        if (!includePayload) {
            return removeNulls(argTypes);
        }
        return argTypes;
    }

    protected final IArgumentType[] args(IProtocolLayoutType subProtocol, String prefix, int offset) {
        List<IArgumentType> l = new ArrayList<IArgumentType>();
        if (includePayload) {
            for (IArgumentType a : subProtocol.getArguments()) {
                l.add(new ArgTypeOffsetProxy(a, prefix, offset));
            }
        }
        IArgumentType[] result = new IArgumentType[l.size()];
        return l.toArray(result);
    }

    private IArgumentType[] removeNulls(IArgumentType... argTypes) {
        List<IArgumentType> l = new ArrayList<IArgumentType>();
        for (IArgumentType a : argTypes) {
            if (a != null) {
                l.add(a);
            }
        }
        IArgumentType[] result = new IArgumentType[l.size()];
        return l.toArray(result);
    }
    
    protected final IArgumentType[] args(List<IArgumentType> argTypes) {
        require(notNull(argTypes));
        IArgumentType[] result = new IArgumentType[argTypes.size()];
        return args(argTypes.toArray(result));
    }
    

    /**
     * Create a bit size argument
     * @param argName of argument
     * @param size in bits of the argument
     * @param offset in bits of argument
     * @param values if this is an enum provide values here
     * @return the created argument
     */
    protected final IArgumentType arg(String argName, int size, int offset, INameValuePair... values) {
        return new ArgumentType(argName, size, offset, values);
    }
    
    protected final IArgumentType arg(String name, int size, int offset, IEnumeration e) {
    	return new ArgumentType(name, size, offset, e);
    }
    
    /**
     * Create a virtual argument
     * @param argName
     * @param values
     * @return
     */
    protected final IArgumentType vArg(String argName, INameValuePair... values) {
        return new ArgumentType(argName, BYTE_SIZE * 2, 0, true, values);
    }
    
    /**
     * Create an argument devined in bits
     * @param argName
     * @param size in bits
     * @param offset
     * @param realOffset
     * @param resolution
     * @param unit
     * @return
     */
    protected final IArgumentType arg(String argName, int size, int offset, double realOffset, double resolution, Unit unit) {
        return new ArgumentType(argName, size, offset, realOffset, resolution, unit);
    }
    
    /**
     * Create a byte size argument
     * @param argName of argument
     * @param size in bytes of the argument
     * @param offset in bytes of argument
     * @param values if this is an enum provide values here
     * @return the created argument
     */
    protected final IArgumentType argByte(String argName, int size, int offset, INameValuePair... values) {
        return arg(argName, size * BYTE_SIZE, offset * BYTE_SIZE, values);
    }
    protected final IArgumentType argByte(String argName, int size, int offset, double realOffset, double resolution, Unit unit) {
        return arg(argName, size * BYTE_SIZE, offset * BYTE_SIZE, realOffset, resolution, unit);
    }
    
    /**
     * Create a byte size argument
     * @param argName of argument
     * @param offset in bytes of argument
     * @param values if this is an enum provide values here
     * @return the created argument
     */
    protected final IArgumentType argByte(String argName, int offset, INameValuePair... values) {
        return argByte(argName, 1, offset, values);
    }
    protected IArgumentType argByte(String argName, int offset, double realOffset, double resolution, Unit unit) {
        return argByte(argName, 1, offset, realOffset, resolution, unit);
    }

    protected final IArgumentType argByte(IProtocolLayoutType subProtocol, String prefix, int offset) {
        if (includePayload) {
            require(subProtocol.getArguments().length == 1);
            return new ArgTypeOffsetProxy(subProtocol.getArguments()[0], prefix, offset);
        }
        return null;
    }

    
    /**
     * Create a short size (2 bytes) argument
     * @param argName of argument
     * @param offset in bytes of argument
     * @param values if this is an enum provide values here
     * @return the created argument
     */
    protected final IArgumentType argShort(String argName, int offset, INameValuePair... values) {
        return argByte(argName, 2, offset, values);
    }
    protected final IArgumentType argShort(String argName, int offset, double realOffset, double resolution, Unit unit) {
        return argByte(argName, 2, offset, realOffset, resolution, unit);
    }
    
    /**
     * Create a int size  (4 bytes) argument
     * @param argName of argument
     * @param offset in bytes of argument
     * @param values if this is an enum provide values here
     * @return the created argument
     */
    protected final IArgumentType argInt(String argName, int offset, INameValuePair... values) {
        return argByte(argName, 4, offset, values);
    }
    protected IArgumentType argInt(String argName, int offset, double realOffset, double resolution, Unit unit) {
        return argByte(argName, 4, offset, realOffset, resolution, unit);
    }
    /**
     * 
     * @param argName
     * @param offset in bytes
     * @param size in bytes
     * @return
     */
    protected final IArgumentType argStr(String argName, Quantity offset, Quantity size) {
        return new StringArgumentType(argName, size, offset);
    }
    /**
     * @deprecated
     * @param argName
     * @param offset
     * @param size
     * @return
     */
    protected final IArgumentType argStr(String argName, int offset, int size) {
    	return argStr(argName, quantity(offset, Unit.byteSize), quantity(size, Unit.byteSize));
    }
    
    /**
     * Create a indexed argument
     * @param argName of argument
     * @param maxEntries the max number of entries this argument should contain
     * @param argTypes the arguments contained in the indexed argument
     * @return the created argument
     */
    protected final IArgumentType iArg(String argName, int maxEntries, IArgumentType... argTypes) {
        IArgumentType[] at = argTypes;
        if (!includePayload) {
            at = removeNulls(argTypes);
        }
        return new IndexArgumentType(argName, maxEntries, at);
    }
    protected final IArgumentType address(String argName, int size, int offset) {
        return new AddressSizeArgType(argName, size, offset, true);
    }
    protected final IArgumentType size(String argName, int size, int offset) {
        return new AddressSizeArgType(argName, size, offset, false);
    }
    protected final INameValuePair[] values(INameValuePair... values) {
        return values;
    }
    protected final INameValuePair[] values(List<INameValuePair> values) {
        INameValuePair[] result = new INameValuePair[values.size()];
        return values.toArray(result);
    }
    
    
    /**
     * Create a value in an enum argument
     * @param valueName of the value
     * @param value the bit value of the value
     * @return the newly created value
     */
    protected final INameValuePair value(String valueName, int value) {
        return new NameValuePair(valueName, value);
    }

    /**
     * Create a value in an enum argument that contains a sub protocol
     * @param valueName of the value
     * @param value the bit value of the value
     * @param type the sub protocol 
     * @param prefix to prevent name clashes always provide a prefix for the sub protocol
     * @param startIndex the start index of the sub protocol
     * @return the newly created value
     */
    protected final INameValuePair value(String valueName, int value, IProtocolLayoutType type, String prefix, int startIndex) {
        if (includePayload) {
            return new NameValuePair(valueName, value, type, prefix, startIndex);
        }
        return value(valueName, value);
    }
    
    /**
     * Create a value in an enum argument that contains sub arguments
     * @param name of the value
     * @param value the bit value of the value
     * @param argTypes the sub arguments
     * @return the newly created value
     */
    protected final INameValuePair value(String valueName, int value, IArgumentType...argTypes) {
        return new NameValuePair(valueName, value, 0, argTypes);
    }
    protected final INameValuePair value(String valueName, int value, List<IArgumentType> argTypes) {
        return new NameValuePair(valueName, value, 0, asArray(argTypes));
    }

    private static IArgumentType[] asArray(List<IArgumentType> argTypes) {
		return argTypes.toArray(new IArgumentType[argTypes.size()]);
	}

	protected final INameValuePair valuePrefix(String valueName, int value, IArgumentType...argTypes) {
        return new NameValuePair(valueName, value, 0, prefixProxyOf(valueName, argTypes));
    }

    protected static int offset(int x) {
        return x;
    }
    protected static int size(int x) {
        return x;
    }
    protected static int entries(int x) {
        return x;
    }
    protected static double realOffset(double x) {
        return x;
    }
    protected static double resolution(double x) {
        return x;
    }
    protected static String prefix(String x) {
        return x;
    }
    
    
    private IArgumentType[] prefixProxyOf(String valueName, IArgumentType[] argTypes) {
        IArgumentType[] result = new IArgumentType[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            result[i] = new ArgTypeOffsetProxy(argTypes[i], valueName, 0);
        }
        return result;
    }

    public final String toString() {
        return getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
    }
}


