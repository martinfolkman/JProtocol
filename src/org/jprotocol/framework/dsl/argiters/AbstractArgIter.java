package org.jprotocol.framework.dsl.argiters;

import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import org.jprotocol.framework.dsl.ArgTypeOffsetProxy;
import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.INameValuePair;
import org.jprotocol.framework.dsl.IProtocolMessage;


abstract public class AbstractArgIter {
    protected final IProtocolMessage protocol;
    private final int address;
    private final int size;
    AbstractArgIter(IProtocolMessage protocol) {
        require(notNull(protocol));
        this.protocol = protocol;
        this.address = getAddressArg();
        this.size = getSizeArg();
    }
    
    private int getAddressArg() {
        if (protocol.getProtocolType().hasTargetType()) {
            for (IArgumentType a: protocol.getProtocolType().getArguments()) {
                if (a.isAddress()) {
                    return protocol.getValueAsNameValuePair(a).getValue();
                }
            }
        }
        return 0;
    }
    private int getSizeArg() {
        if (protocol.getProtocolType().hasTargetType()) {
            for (IArgumentType a: protocol.getProtocolType().getArguments()) {
                if (a.isSize()) {
                    return protocol.getValueAsNameValuePair(a).getValue();
                }
            }
            return protocol.getProtocolType().getSizeInBytes() - protocol.getProtocolType().getTargetTypeOffset();
        }
        return 0;
    }

    public final void iterate() {
        iterate(null, protocol.getProtocolType().getArguments(), null);
    }

    private IArgumentType proxyOf(IArgumentType a) {
        if (!protocol.getProtocolType().hasTargetType()) return a;
        int targetOffset = protocol.getProtocolType().getTargetTypeOffset() * 8;
        if (a.getOffset() < targetOffset) {
            return a;
        } else if (a.getOffset() - targetOffset  >= address * 8 && a.getOffset() + a.getSizeInBits() <= targetOffset + (address + size) * 8) {
            return new ArgTypeOffsetProxy(a, "", -address, "");
        }
        return null;
    }


    private void iterate(IArgumentType parent, IArgumentType[] args, String value) {
        //Recurse with bredth first, not depth
        for (IArgumentType arg : args) {
            if (isValidArg(parent, value) && !arg.isIndexedType()) {
                boolean result = store(arg);
                if (!result) return;
            }
        }
        for (IArgumentType arg : args) {
            if (isValidArg(parent, value)) {
                _iter(arg);
            }
        }
    }

    
    public boolean store(IArgumentType arg) {
        final IArgumentType proxy = proxyOf(arg);
        if (proxy == null) {
            return true;
        }
        return iter(proxy);
    }
    
    abstract protected boolean iter(IArgumentType arg);

    private void _iter(IArgumentType arg) {
        if (arg.isEnumType()) {
            for (INameValuePair nvp : arg.getValues()) {
                iterate(arg, nvp.getArgTypes(), nvp.getName());
            }
        } else if (arg.isIndexedType()) {
            iterateIndexed(arg);
        }
    }

    protected void iterateIndexed(IArgumentType arg, int...indexes) {
        require(arg.isIndexedType());
        iterateIndexedChildren(arg.getChildren());
    }
    private void iterateIndexedChildren(IArgumentType[] args) {
        for (IArgumentType subArg : args) {
            if (!subArg.isIndexedType()) {
                store(subArg);
            } else {
                iterateIndexedChildren(subArg.getChildren());
            }
        }
    }

    
    
    private boolean isValidArg(IArgumentType arg, String value) {
        if (arg == null) return true;
        return protocol.getProtocolType().getDiscriminator().isInUse(protocol, arg) && value.equals(protocol.getValue(arg));
    }
}
