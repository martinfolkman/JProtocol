package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.check;
import static org.jprotocol.util.Contract.implies;
import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jprotocol.framework.dsl.argiters.FindEndIndexArgIter;
import org.jprotocol.framework.dsl.argiters.ValueNameOfIter;


public class ProtocolLayoutType implements IProtocolLayoutType {
    private final static IDiscriminator happyDiscriminator = new HappyDiscr();
    private final IArgumentType[] args;  
    private final Map<String, IArgumentType> argMap = new HashMap<String, IArgumentType>();
    private final String name;
    private final IDiscriminator discriminator;
    private final Direction direction;
    private final int targetTypeOffset;
    private final boolean hasTargetType;
    
    
    public ProtocolLayoutType(String name, String protocolName, Direction dir, IDiscriminator discriminator, IArgumentType... args) {
        this(name, protocolName, dir, discriminator, null, 0, args);
    }
    
    public ProtocolLayoutType(final String name, final String protocolName, final Direction dir, final IDiscriminator discriminator, final IProtocolLayoutType targetType, final int targetTypeOffset, final IArgumentType... args) {
        require(notNull(name));
        require(notNull(protocolName));
        require(notNull(dir));
        this.name = name;
        implies(dir == Direction.Request, name.endsWith(Direction.Request.toString()));
        implies(dir == Direction.Response, name.endsWith(Direction.Response.toString()));
        this.direction = dir;
        this.discriminator = discriminator;
        this.targetTypeOffset = targetTypeOffset;
        final List<IArgumentType> l = new ArrayList<IArgumentType>();
        for (IArgumentType arg : args) {
            argMap.put(arg.getName(), arg);
            l.add(arg);
        }
        hasTargetType = targetType != null;
        if (hasTargetType) {
            for (IArgumentType arg: targetType.getArguments()) {
                ArgTypeOffsetProxy proxy = new ArgTypeOffsetProxy(arg, "", targetTypeOffset, "");
                argMap.put(proxy.getName(), proxy);
                l.add(proxy);
            }
        }
        this.args = l.toArray(new IArgumentType[l.size()]);
    }
    
    public ProtocolLayoutType(String name, String protocolName, Direction dir, IArgumentType... args) {
        this(name, protocolName, dir, null, 0, args);
    }
    public ProtocolLayoutType(String name, String protocolName, Direction dir, IProtocolLayoutType targetType, int targetTypeOffset, IArgumentType... args) {
        this(name, protocolName, dir, happyDiscriminator, targetType, targetTypeOffset, args);
    }    

    public IArgumentType[] getArguments() {
        return args;
    }
    
    public IArgumentType argOf(String argName) {
        return argMap.get(argName);
    }
    
    public String getName() {
        return name;
    }
    public String getProtocolName() {
        if (getName().endsWith(" " + Direction.Request.toString())) {
            return getName().substring(0, getName().lastIndexOf(Direction.Request.toString()) - 1);
        }
        if (getName().endsWith(" " + Direction.Response.toString())) {
            return getName().substring(0, getName().lastIndexOf(Direction.Response.toString()) - 1);
        }
        return getName();
    }

    public String toString() { return name; }



    public int getSizeInBytes() {
        IArgumentType arg = new FindEndIndexArgIter(this).getFoundArg();
        if (arg == null) {
            return 0;
        }
        return arg.getEndByteIndex() + 1;
    }
    
    public int getSizeInBits() {
        IArgumentType arg = new FindEndIndexArgIter(this).getFoundArg();
        if (arg == null) {
            return 0;
        }
        return arg.getOffset() + arg.getSizeInBits();
    }


    public IArgumentType indexedArgOf(String argName, IArgumentType a, int index) {
        if (a.isIndexedType()) {
            for (IArgumentType subA : a.getChildren()) {
                if (subA.getName().equals(argName)) {
                    check(index < a.getMaxEntries());
                    INameValuePair[] values = new NameValuePair[0]; 
                    if (subA.isEnumType()) {
                        values = subA.getValues();
                    }
                    return new ArgumentType(subA.getName(), subA.getSizeInBits(), subA.getOffset() + index * a.getSizeInByteOfOneIndexedArg() * 8, values);
                }
            }
        }
        return null;
    }

    public String valueNameOf(String argName, int value) {
        return new ValueNameOfIter(argName, value, this).valueName;
    }

    @Override
    public int getPayloadStartIndex() {
        return getSizeInBytes();
    }
    
    @Override
    public boolean hasPayload() {
        return getPayloadStartIndex() >= 0;
    }
    @Override
    public IDiscriminator getDiscriminator() {
        return discriminator;
    }
    @Override
    public Direction getDirection() {
        return direction;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IProtocolLayoutType)) {
        	return false;
        }
        IProtocolLayoutType other = (IProtocolLayoutType)o;
        boolean result = getName().equals(other.getName()) &&
        				 getDirection() == other.getDirection() &&
        				 areArgsEqual(other);
        return result;
    }
    
    private boolean areArgsEqual(IProtocolLayoutType other) {
    	if (getArguments().length != other.getArguments().length) {
    		return false;
    	}
    	for (int i = 0; i < getArguments().length; i++) {
    		if (!getArguments()[i].equals(other.getArguments()[i])) {
    			return false;
    		}
    	}
		return true;
	}

	@Override
    public int hashCode() {
        return getName().hashCode();
    }


    @Override
    public int getTargetTypeOffset() {
        return targetTypeOffset;
    }

    @Override
    public boolean hasTargetType() {
        return hasTargetType;
    }
}



class HappyDiscr implements IDiscriminator {
    @Override
    public boolean isInUse(IProtocolMessage protocol, IArgumentType argType) {
        return true;
    }
}
class EmptyTarget extends ProtocolLayoutType {
    public EmptyTarget() {
        super("Empty", "Empty", Direction.Layout);
    }
    
}